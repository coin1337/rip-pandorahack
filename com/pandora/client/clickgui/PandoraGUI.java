package com.pandora.client.clickgui;

import com.lukflug.panelstudio.Animation;
import com.lukflug.panelstudio.CollapsibleContainer;
import com.lukflug.panelstudio.DraggableContainer;
import com.lukflug.panelstudio.FixedComponent;
import com.lukflug.panelstudio.Interface;
import com.lukflug.panelstudio.hud.HUDClickGUI;
import com.lukflug.panelstudio.hud.HUDPanel;
import com.lukflug.panelstudio.settings.BooleanComponent;
import com.lukflug.panelstudio.settings.EnumComponent;
import com.lukflug.panelstudio.settings.NumberComponent;
import com.lukflug.panelstudio.settings.SimpleToggleable;
import com.lukflug.panelstudio.settings.Toggleable;
import com.lukflug.panelstudio.settings.ToggleableContainer;
import com.lukflug.panelstudio.theme.ColorScheme;
import com.lukflug.panelstudio.theme.GameSenseTheme;
import com.lukflug.panelstudio.theme.Theme;
import com.pandora.api.config.PositionConfig;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.font.FontUtils;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.gui.ClickGuiModule;
import com.pandora.client.module.modules.gui.ColorMain;
import com.pandora.client.module.modules.hud.HUDModule;
import com.pandora.client.module.modules.hud.TabGUIModule;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.Stack;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class PandoraGUI extends GuiScreen implements Interface {
   public static final int WIDTH = 100;
   public static final int HEIGHT = 12;
   public static final int DISTANCE = 10;
   public static final int HUD_BORDER = 2;
   private final Toggleable colorToggle = new Toggleable() {
      public void toggle() {
         ColorMain.colorModel.increment();
      }

      public boolean isOn() {
         return ColorMain.colorModel.getValue().equals("HSB");
      }
   };
   public final HUDClickGUI gui;
   public static final Theme theme = new GameSenseTheme(new PandoraGUI.GameSenseScheme(), 12, 2);
   private Point mouse = new Point();
   private boolean lButton = false;
   private boolean rButton = false;
   private static final FloatBuffer MODELVIEW = GLAllocation.func_74529_h(16);
   private static final FloatBuffer PROJECTION = GLAllocation.func_74529_h(16);
   private static final IntBuffer VIEWPORT = GLAllocation.func_74527_f(16);
   private static final FloatBuffer COORDS = GLAllocation.func_74529_h(3);
   private Stack<Rectangle> clipRect = new Stack();

   public PandoraGUI() {
      Point pos = new Point(10, 10);
      this.gui = new HUDClickGUI(this);
      Iterator var2 = ModuleManager.getModules().iterator();

      while(var2.hasNext()) {
         Module module = (Module)var2.next();
         if (module instanceof HUDModule) {
            this.gui.addHUDComponent(new PandoraGUI.GameSenseHUDPanel(((HUDModule)module).getComponent(), module));
         }
      }

      TabGUIModule.populate();
      Module.Category[] var9 = Module.Category.values();
      int var10 = var9.length;

      for(int var4 = 0; var4 < var10; ++var4) {
         Module.Category category = var9[var4];
         DraggableContainer panel = new DraggableContainer(category.name(), theme.getPanelRenderer(), new SimpleToggleable(false), new PandoraGUI.GameSenseAnimation(), new Point(pos), 100) {
            protected int getScrollHeight(int childHeight) {
               return ClickGuiModule.scrolling.getValue().equals("Screen") ? childHeight : Math.min(childHeight, Math.max(48, PandoraGUI.this.field_146295_m - this.getPosition(PandoraGUI.this).y - this.renderer.getHeight() - 12));
            }
         };
         this.gui.addComponent(panel);
         pos.translate(110, 0);
         Iterator var7 = ModuleManager.getModulesInCategory(category).iterator();

         while(var7.hasNext()) {
            Module module = (Module)var7.next();
            this.addModule(panel, module);
         }
      }

   }

   public void render() {
      if (!this.gui.isOn()) {
         this.renderGUI();
      }

   }

   private void renderGUI() {
      GlStateManager.func_179111_a(2982, MODELVIEW);
      GlStateManager.func_179111_a(2983, PROJECTION);
      GlStateManager.func_187445_a(2978, VIEWPORT);
      begin();
      this.gui.render();
      end();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.renderGUI();
      this.mouse = new Point(mouseX, mouseY);
      int scroll = Mouse.getDWheel();
      if (scroll != 0) {
         if (ClickGuiModule.scrolling.getValue().equals("Screen")) {
            Iterator var5 = this.gui.getComponents().iterator();

            while(var5.hasNext()) {
               FixedComponent component = (FixedComponent)var5.next();
               if (!(component instanceof HUDPanel)) {
                  Point p = component.getPosition(this);
                  if (scroll > 0) {
                     p.translate(0, ClickGuiModule.scrollSpeed.getValue());
                  } else {
                     p.translate(0, -ClickGuiModule.scrollSpeed.getValue());
                  }

                  component.setPosition(this, p);
               }
            }
         }

         if (scroll > 0) {
            this.gui.handleScroll(-ClickGuiModule.scrollSpeed.getValue());
         } else {
            this.gui.handleScroll(ClickGuiModule.scrollSpeed.getValue());
         }
      }

   }

   public void func_73864_a(int mouseX, int mouseY, int clickedButton) {
      this.mouse = new Point(mouseX, mouseY);
      switch(clickedButton) {
      case 0:
         this.lButton = true;
         break;
      case 1:
         this.rButton = true;
      }

      this.gui.handleButton(clickedButton);
   }

   public void func_146286_b(int mouseX, int mouseY, int releaseButton) {
      this.mouse = new Point(mouseX, mouseY);
      switch(releaseButton) {
      case 0:
         this.lButton = false;
         break;
      case 1:
         this.rButton = false;
      }

      this.gui.handleButton(releaseButton);
   }

   public void handleKeyEvent(int scancode) {
      if (scancode != 1) {
         this.gui.handleKey(scancode);
      }

   }

   protected void func_73869_a(char typedChar, int keyCode) {
      if (keyCode == 1) {
         this.gui.exit();
         this.field_146297_k.func_147108_a((GuiScreen)null);
         if (this.gui.isOn()) {
            this.gui.toggle();
         }
      } else {
         this.gui.handleKey(keyCode);
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   public Point getMouse() {
      return new Point(this.mouse);
   }

   public boolean getButton(int button) {
      switch(button) {
      case 0:
         return this.lButton;
      case 1:
         return this.rButton;
      default:
         return false;
      }
   }

   public void drawString(Point pos, String s, Color c) {
      end();
      int x = pos.x + 2;
      int y = pos.y + 1;
      if (!ColorMain.customFont.getValue()) {
         ++x;
         ++y;
      }

      FontUtils.drawStringWithShadow(ColorMain.customFont.getValue(), s, x, y, new PandoraColor(c));
      begin();
   }

   public int getFontWidth(String s) {
      return Math.round((float)FontUtils.getStringWidth(ColorMain.customFont.getValue(), s)) + 4;
   }

   public int getFontHeight() {
      return Math.round((float)FontUtils.getFontHeight(ColorMain.customFont.getValue())) + 2;
   }

   public void fillTriangle(Point pos1, Point pos2, Point pos3, Color c1, Color c2, Color c3) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      bufferbuilder.func_181668_a(4, DefaultVertexFormats.field_181706_f);
      bufferbuilder.func_181662_b((double)pos1.x, (double)pos1.y, (double)this.field_73735_i).func_181666_a((float)c1.getRed() / 255.0F, (float)c1.getGreen() / 255.0F, (float)c1.getBlue() / 255.0F, (float)c1.getAlpha() / 255.0F).func_181675_d();
      bufferbuilder.func_181662_b((double)pos2.x, (double)pos2.y, (double)this.field_73735_i).func_181666_a((float)c2.getRed() / 255.0F, (float)c2.getGreen() / 255.0F, (float)c2.getBlue() / 255.0F, (float)c2.getAlpha() / 255.0F).func_181675_d();
      bufferbuilder.func_181662_b((double)pos3.x, (double)pos3.y, (double)this.field_73735_i).func_181666_a((float)c3.getRed() / 255.0F, (float)c3.getGreen() / 255.0F, (float)c3.getBlue() / 255.0F, (float)c3.getAlpha() / 255.0F).func_181675_d();
      tessellator.func_78381_a();
   }

   public void drawLine(Point a, Point b, Color c1, Color c2) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      bufferbuilder.func_181668_a(1, DefaultVertexFormats.field_181706_f);
      bufferbuilder.func_181662_b((double)a.x, (double)a.y, (double)this.field_73735_i).func_181666_a((float)c1.getRed() / 255.0F, (float)c1.getGreen() / 255.0F, (float)c1.getBlue() / 255.0F, (float)c1.getAlpha() / 255.0F).func_181675_d();
      bufferbuilder.func_181662_b((double)b.x, (double)b.y, (double)this.field_73735_i).func_181666_a((float)c2.getRed() / 255.0F, (float)c2.getGreen() / 255.0F, (float)c2.getBlue() / 255.0F, (float)c2.getAlpha() / 255.0F).func_181675_d();
      tessellator.func_78381_a();
   }

   public void fillRect(Rectangle r, Color c1, Color c2, Color c3, Color c4) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
      bufferbuilder.func_181662_b((double)r.x, (double)(r.y + r.height), (double)this.field_73735_i).func_181666_a((float)c4.getRed() / 255.0F, (float)c4.getGreen() / 255.0F, (float)c4.getBlue() / 255.0F, (float)c4.getAlpha() / 255.0F).func_181675_d();
      bufferbuilder.func_181662_b((double)(r.x + r.width), (double)(r.y + r.height), (double)this.field_73735_i).func_181666_a((float)c3.getRed() / 255.0F, (float)c3.getGreen() / 255.0F, (float)c3.getBlue() / 255.0F, (float)c3.getAlpha() / 255.0F).func_181675_d();
      bufferbuilder.func_181662_b((double)(r.x + r.width), (double)r.y, (double)this.field_73735_i).func_181666_a((float)c2.getRed() / 255.0F, (float)c2.getGreen() / 255.0F, (float)c2.getBlue() / 255.0F, (float)c2.getAlpha() / 255.0F).func_181675_d();
      bufferbuilder.func_181662_b((double)r.x, (double)r.y, (double)this.field_73735_i).func_181666_a((float)c1.getRed() / 255.0F, (float)c1.getGreen() / 255.0F, (float)c1.getBlue() / 255.0F, (float)c1.getAlpha() / 255.0F).func_181675_d();
      tessellator.func_78381_a();
   }

   public void drawRect(Rectangle r, Color c1, Color c2, Color c3, Color c4) {
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      bufferbuilder.func_181668_a(2, DefaultVertexFormats.field_181706_f);
      bufferbuilder.func_181662_b((double)r.x, (double)(r.y + r.height), (double)this.field_73735_i).func_181666_a((float)c4.getRed() / 255.0F, (float)c4.getGreen() / 255.0F, (float)c4.getBlue() / 255.0F, (float)c4.getAlpha() / 255.0F).func_181675_d();
      bufferbuilder.func_181662_b((double)(r.x + r.width), (double)(r.y + r.height), (double)this.field_73735_i).func_181666_a((float)c3.getRed() / 255.0F, (float)c3.getGreen() / 255.0F, (float)c3.getBlue() / 255.0F, (float)c3.getAlpha() / 255.0F).func_181675_d();
      bufferbuilder.func_181662_b((double)(r.x + r.width), (double)r.y, (double)this.field_73735_i).func_181666_a((float)c2.getRed() / 255.0F, (float)c2.getGreen() / 255.0F, (float)c2.getBlue() / 255.0F, (float)c2.getAlpha() / 255.0F).func_181675_d();
      bufferbuilder.func_181662_b((double)r.x, (double)r.y, (double)this.field_73735_i).func_181666_a((float)c1.getRed() / 255.0F, (float)c1.getGreen() / 255.0F, (float)c1.getBlue() / 255.0F, (float)c1.getAlpha() / 255.0F).func_181675_d();
      tessellator.func_78381_a();
   }

   public synchronized int loadImage(String name) {
      try {
         ResourceLocation rl = new ResourceLocation("gamesense:gui/" + name);
         InputStream stream = Minecraft.func_71410_x().field_110451_am.func_110536_a(rl).func_110527_b();
         BufferedImage image = ImageIO.read(stream);
         int texture = TextureUtil.func_110996_a();
         TextureUtil.func_110987_a(texture, image);
         return texture;
      } catch (IOException var6) {
         var6.printStackTrace();
         return 0;
      }
   }

   public void drawImage(Rectangle r, int rotation, boolean parity, int image) {
      if (image != 0) {
         int[][] texCoords = new int[][]{{0, 1}, {1, 1}, {1, 0}, {0, 0}};

         int temp1;
         int temp2;
         for(temp1 = 0; temp1 < rotation % 4; ++temp1) {
            temp2 = texCoords[3][0];
            int temp2 = texCoords[3][1];
            texCoords[3][0] = texCoords[2][0];
            texCoords[3][1] = texCoords[2][1];
            texCoords[2][0] = texCoords[1][0];
            texCoords[2][1] = texCoords[1][1];
            texCoords[1][0] = texCoords[0][0];
            texCoords[1][1] = texCoords[0][1];
            texCoords[0][0] = temp2;
            texCoords[0][1] = temp2;
         }

         if (parity) {
            temp1 = texCoords[3][0];
            temp2 = texCoords[3][1];
            texCoords[3][0] = texCoords[0][0];
            texCoords[3][1] = texCoords[0][1];
            texCoords[0][0] = temp1;
            texCoords[0][1] = temp2;
            temp1 = texCoords[2][0];
            temp2 = texCoords[2][1];
            texCoords[2][0] = texCoords[1][0];
            texCoords[2][1] = texCoords[1][1];
            texCoords[1][0] = temp1;
            texCoords[1][1] = temp2;
         }

         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder bufferbuilder = tessellator.func_178180_c();
         GlStateManager.func_179144_i(image);
         GlStateManager.func_179098_w();
         bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
         bufferbuilder.func_181662_b((double)r.x, (double)(r.y + r.height), (double)this.field_73735_i).func_187315_a((double)texCoords[0][0], (double)texCoords[0][1]).func_181675_d();
         bufferbuilder.func_181662_b((double)(r.x + r.width), (double)(r.y + r.height), (double)this.field_73735_i).func_187315_a((double)texCoords[1][0], (double)texCoords[1][1]).func_181675_d();
         bufferbuilder.func_181662_b((double)(r.x + r.width), (double)r.y, (double)this.field_73735_i).func_187315_a((double)texCoords[2][0], (double)texCoords[2][1]).func_181675_d();
         bufferbuilder.func_181662_b((double)r.x, (double)r.y, (double)this.field_73735_i).func_187315_a((double)texCoords[3][0], (double)texCoords[3][1]).func_181675_d();
         tessellator.func_78381_a();
         GlStateManager.func_179090_x();
      }
   }

   private void scissor(Rectangle r) {
      if (r == null) {
         GL11.glScissor(0, 0, 0, 0);
         GL11.glEnable(3089);
      } else {
         GLU.gluProject((float)r.x, (float)r.y, this.field_73735_i, MODELVIEW, PROJECTION, VIEWPORT, COORDS);
         float x1 = COORDS.get(0);
         float y1 = COORDS.get(1);
         GLU.gluProject((float)(r.x + r.width), (float)(r.y + r.height), this.field_73735_i, MODELVIEW, PROJECTION, VIEWPORT, COORDS);
         float x2 = COORDS.get(0);
         float y2 = COORDS.get(1);
         GL11.glScissor(Math.round(Math.min(x1, x2)), Math.round(Math.min(y1, y2)), Math.round(Math.abs(x2 - x1)), Math.round(Math.abs(y2 - y1)));
         GL11.glEnable(3089);
      }
   }

   public void window(Rectangle r) {
      if (this.clipRect.isEmpty()) {
         this.scissor(r);
         this.clipRect.push(r);
      } else {
         Rectangle top = (Rectangle)this.clipRect.peek();
         if (top == null) {
            this.scissor((Rectangle)null);
            this.clipRect.push((Object)null);
         } else {
            int x1 = Math.max(r.x, top.x);
            int y1 = Math.max(r.y, top.y);
            int x2 = Math.min(r.x + r.width, top.x + top.width);
            int y2 = Math.min(r.y + r.height, top.y + top.height);
            if (x2 > x1 && y2 > y1) {
               Rectangle rect = new Rectangle(x1, y1, x2 - x1, y2 - y1);
               this.scissor(rect);
               this.clipRect.push(rect);
            } else {
               this.scissor((Rectangle)null);
               this.clipRect.push((Object)null);
            }
         }
      }

   }

   public void restore() {
      if (!this.clipRect.isEmpty()) {
         this.clipRect.pop();
         if (this.clipRect.isEmpty()) {
            GL11.glDisable(3089);
         } else {
            this.scissor((Rectangle)this.clipRect.peek());
         }
      }

   }

   private void addModule(CollapsibleContainer panel, Module module) {
      CollapsibleContainer container = new ToggleableContainer(module.getName(), theme.getContainerRenderer(), new SimpleToggleable(false), new PandoraGUI.GameSenseAnimation(), module);
      panel.addComponent(container);
      Iterator var4 = PandoraMod.getInstance().settingsManager.getSettingsForMod(module).iterator();

      while(var4.hasNext()) {
         Setting property = (Setting)var4.next();
         if (property instanceof Setting.Boolean) {
            container.addComponent(new BooleanComponent(property.getName(), theme.getComponentRenderer(), (Setting.Boolean)property));
         } else if (property instanceof Setting.Integer) {
            container.addComponent(new NumberComponent(property.getName(), theme.getComponentRenderer(), (Setting.Integer)property, (double)((Setting.Integer)property).getMin(), (double)((Setting.Integer)property).getMax()));
         } else if (property instanceof Setting.Double) {
            container.addComponent(new NumberComponent(property.getName(), theme.getComponentRenderer(), (Setting.Double)property, ((Setting.Double)property).getMin(), ((Setting.Double)property).getMax()));
         } else if (property instanceof Setting.Mode) {
            container.addComponent(new EnumComponent(property.getName(), theme.getComponentRenderer(), (Setting.Mode)property));
         } else if (property instanceof Setting.ColorSetting) {
            container.addComponent(new SyncableColorComponent(theme, (Setting.ColorSetting)property, this.colorToggle, new PandoraGUI.GameSenseAnimation()));
         }
      }

      container.addComponent(new PandoraKeybind(theme.getComponentRenderer(), module));
   }

   private static void begin() {
      GlStateManager.func_179147_l();
      GlStateManager.func_179090_x();
      GlStateManager.func_187428_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
      GlStateManager.func_179103_j(7425);
      GlStateManager.func_187441_d(2.0F);
   }

   private static void end() {
      GlStateManager.func_179103_j(7424);
      GlStateManager.func_179098_w();
      GlStateManager.func_179084_k();
   }

   public static void renderItem(ItemStack item, Point pos) {
      GlStateManager.func_179098_w();
      GlStateManager.func_179132_a(true);
      GL11.glPushAttrib(524288);
      GL11.glDisable(3089);
      GlStateManager.func_179086_m(256);
      GL11.glPopAttrib();
      GlStateManager.func_179126_j();
      GlStateManager.func_179118_c();
      GlStateManager.func_179094_E();
      Minecraft.func_71410_x().func_175599_af().field_77023_b = -150.0F;
      RenderHelper.func_74520_c();
      Minecraft.func_71410_x().func_175599_af().func_180450_b(item, pos.x, pos.y);
      Minecraft.func_71410_x().func_175599_af().func_175030_a(Minecraft.func_71410_x().field_71466_p, item, pos.x, pos.y);
      RenderHelper.func_74518_a();
      Minecraft.func_71410_x().func_175599_af().field_77023_b = 0.0F;
      GlStateManager.func_179121_F();
      GlStateManager.func_179097_i();
      GlStateManager.func_179132_a(false);
      begin();
   }

   public static void renderEntity(EntityLivingBase entity, Point pos) {
      GlStateManager.func_179098_w();
      GlStateManager.func_179132_a(true);
      GL11.glPushAttrib(524288);
      GL11.glDisable(3089);
      GlStateManager.func_179086_m(256);
      GL11.glPopAttrib();
      GlStateManager.func_179126_j();
      GlStateManager.func_179118_c();
      GlStateManager.func_179094_E();
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GuiInventory.func_147046_a(pos.x, pos.y, 43, 28.0F, 60.0F, entity);
      GlStateManager.func_179121_F();
      GlStateManager.func_179097_i();
      GlStateManager.func_179132_a(false);
      begin();
   }

   private class GameSenseHUDPanel extends HUDPanel implements PositionConfig {
      public GameSenseHUDPanel(FixedComponent component, Toggleable module) {
         super(component, PandoraGUI.theme.getPanelRenderer(), module, new PandoraGUI.GameSenseAnimation(), new Toggleable() {
            public void toggle() {
            }

            public boolean isOn() {
               return PandoraGUI.this.gui.isOn() && ClickGuiModule.showHUD.isOn();
            }
         }, 2);
      }

      public Point getConfigPos() {
         return this.component instanceof PositionConfig ? ((PositionConfig)this.component).getConfigPos() : this.getPosition(PandoraGUI.this);
      }

      public void setConfigPos(Point pos) {
         if (this.component instanceof PositionConfig) {
            ((PositionConfig)this.component).setConfigPos(pos);
         } else {
            this.setPosition(PandoraGUI.this, pos);
         }

      }
   }

   public static class GameSenseAnimation extends Animation {
      protected int getSpeed() {
         return ClickGuiModule.animationSpeed.getValue();
      }
   }

   public static class GameSenseScheme implements ColorScheme {
      public Color getActiveColor() {
         return ClickGuiModule.enabledColor.getValue();
      }

      public Color getInactiveColor() {
         return ClickGuiModule.backgroundColor.getValue();
      }

      public Color getBackgroundColor() {
         return ClickGuiModule.settingBackgroundColor.getValue();
      }

      public Color getOutlineColor() {
         return ClickGuiModule.outlineColor.getValue();
      }

      public Color getFontColor() {
         return ClickGuiModule.fontColor.getValue();
      }

      public int getOpacity() {
         return ClickGuiModule.opacity.getValue();
      }
   }
}
