package com.pandora.api.mixin.mixins;

import com.pandora.client.module.ModuleManager;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiScreen.class})
public class MixinGuiScreen {
   RenderItem itemRender = Minecraft.func_71410_x().func_175599_af();
   ResourceLocation resource;
   FontRenderer fontRenderer;

   public MixinGuiScreen() {
      this.fontRenderer = Minecraft.func_71410_x().field_71466_p;
   }

   @Inject(
      method = {"renderToolTip"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo info) {
      this.resource = new ResourceLocation("textures/gui/container/shulker_box.png");
      if (ModuleManager.isModuleEnabled("ShulkerViewer") && stack.func_77973_b() instanceof ItemShulkerBox) {
         NBTTagCompound tagCompound = stack.func_77978_p();
         if (tagCompound != null && tagCompound.func_150297_b("BlockEntityTag", 10)) {
            NBTTagCompound blockEntityTag = tagCompound.func_74775_l("BlockEntityTag");
            if (blockEntityTag.func_150297_b("Items", 9)) {
               info.cancel();
               NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(27, ItemStack.field_190927_a);
               ItemStackHelper.func_191283_b(blockEntityTag, nonnulllist);
               GlStateManager.func_179147_l();
               GlStateManager.func_179101_C();
               RenderHelper.func_74518_a();
               GlStateManager.func_179140_f();
               GlStateManager.func_179097_i();
               int x1 = x + 4;
               int y1 = y - 30;
               this.itemRender.field_77023_b = 300.0F;
               Minecraft.func_71410_x().field_71446_o.func_110577_a(this.resource);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               Minecraft.func_71410_x().field_71456_v.func_73729_b(x1, y1, 7, 5, 162, 66);
               this.fontRenderer.func_78276_b(stack.func_82833_r(), x + 6, y - 28, Color.DARK_GRAY.getRGB());
               GlStateManager.func_179147_l();
               GlStateManager.func_179141_d();
               GlStateManager.func_179098_w();
               GlStateManager.func_179145_e();
               GlStateManager.func_179126_j();
               RenderHelper.func_74520_c();

               for(int i = 0; i < nonnulllist.size(); ++i) {
                  int iX = x + 5 + i % 9 * 18;
                  int iY = y + 1 + (i / 9 - 1) * 18;
                  ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                  this.itemRender.func_180450_b(itemStack, iX, iY);
                  this.itemRender.func_180453_a(this.fontRenderer, itemStack, iX, iY, (String)null);
               }

               RenderHelper.func_74518_a();
               this.itemRender.field_77023_b = 0.0F;
               GlStateManager.func_179145_e();
               GlStateManager.func_179126_j();
               RenderHelper.func_74519_b();
               GlStateManager.func_179091_B();
            }
         }
      }

   }
}
