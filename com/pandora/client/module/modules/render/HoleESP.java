package com.pandora.client.module.modules.render;

import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.api.util.render.PandoraTessellator;
import com.pandora.client.module.Module;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class HoleESP extends Module {
   public static Setting.Integer rangeS;
   Setting.Boolean hideOwn;
   Setting.Boolean flatOwn;
   Setting.Boolean renderBurrow;
   Setting.Mode mode;
   Setting.Mode type;
   Setting.Double slabHeight;
   Setting.Integer width;
   Setting.ColorSetting bedrockColor;
   Setting.ColorSetting otherColor;
   Setting.ColorSetting burrowColor;
   private ConcurrentHashMap<BlockPos, PandoraColor> renderHoles;
   private final BlockPos[] surroundOffset = new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0)};

   public HoleESP() {
      super("HoleESP", Module.Category.Render);
   }

   public void setup() {
      ArrayList<String> render = new ArrayList();
      render.add("Outline");
      render.add("Fill");
      render.add("Both");
      ArrayList<String> modes = new ArrayList();
      modes.add("Air");
      modes.add("Ground");
      modes.add("Flat");
      modes.add("Slab");
      modes.add("Double");
      rangeS = this.registerInteger("Range", "Range", 5, 1, 20);
      this.renderBurrow = this.registerBoolean("Burrow", "Burrow", true);
      this.hideOwn = this.registerBoolean("Hide Own", "HideOwn", false);
      this.flatOwn = this.registerBoolean("Flat Own", "FlatOwn", false);
      this.type = this.registerMode("Render", "Render", render, "Both");
      this.mode = this.registerMode("Mode", "Mode", modes, "Air");
      this.slabHeight = this.registerDouble("Slab Height", "SlabHeight", 0.5D, 0.1D, 1.5D);
      this.width = this.registerInteger("Width", "Width", 1, 1, 10);
      this.bedrockColor = this.registerColor("Bedrock Color", "BedrockColor", new PandoraColor(0, 255, 0));
      this.otherColor = this.registerColor("Obsidian Color", "ObsidianColor", new PandoraColor(255, 0, 0));
      this.burrowColor = this.registerColor("Burrow Color", "BurrowColor", new PandoraColor(255, 255, 0));
   }

   public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
      List<BlockPos> circleblocks = new ArrayList();
      int cx = loc.func_177958_n();
      int cy = loc.func_177956_o();
      int cz = loc.func_177952_p();

      for(int x = cx - (int)r; (float)x <= (float)cx + r; ++x) {
         for(int z = cz - (int)r; (float)z <= (float)cz + r; ++z) {
            for(int y = sphere ? cy - (int)r : cy; (float)y < (sphere ? (float)cy + r : (float)(cy + h)); ++y) {
               double dist = (double)((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));
               if (dist < (double)(r * r) && (!hollow || !(dist < (double)((r - 1.0F) * (r - 1.0F))))) {
                  BlockPos l = new BlockPos(x, y + plus_y, z);
                  circleblocks.add(l);
               }
            }
         }
      }

      return circleblocks;
   }

   public static BlockPos getPlayerPos() {
      return new BlockPos(Math.floor(mc.field_71439_g.field_70165_t), Math.floor(mc.field_71439_g.field_70163_u), Math.floor(mc.field_71439_g.field_70161_v));
   }

   public void onUpdate() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         if (this.renderHoles == null) {
            this.renderHoles = new ConcurrentHashMap();
         } else {
            this.renderHoles.clear();
         }

         int range = (int)Math.ceil((double)rangeS.getValue());
         List<BlockPos> blockPosList = this.getSphere(getPlayerPos(), (float)range, range, false, true, 0);
         Iterator var3 = blockPosList.iterator();

         while(true) {
            BlockPos pos;
            do {
               do {
                  do {
                     do {
                        if (!var3.hasNext()) {
                           if (this.renderBurrow.getValue()) {
                              mc.field_71441_e.field_73010_i.stream().forEach((entityPlayer) -> {
                                 if (entityPlayer != mc.field_71439_g) {
                                    BlockPos blockPos = entityPlayer.func_180425_c();
                                    if (blockPos != mc.field_71439_g.func_180425_c()) {
                                       if (blockPos.func_185332_f((int)mc.field_71439_g.field_70165_t, (int)mc.field_71439_g.field_70163_u, (int)mc.field_71439_g.field_70161_v) <= (double)rangeS.getValue() && (mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z || mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150477_bB)) {
                                          this.renderHoles.put(blockPos, new PandoraColor(this.burrowColor.getValue(), 255));
                                       }

                                    }
                                 }
                              });
                           }

                           return;
                        }

                        pos = (BlockPos)var3.next();
                     } while(!mc.field_71441_e.func_180495_p(pos).func_177230_c().equals(Blocks.field_150350_a));
                  } while(!mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals(Blocks.field_150350_a));
               } while(!mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c().equals(Blocks.field_150350_a));
            } while(this.hideOwn.getValue() && pos.equals(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)));

            boolean isSafe = true;
            PandoraColor color = new PandoraColor(this.bedrockColor.getValue(), 255);
            BlockPos[] var7 = this.surroundOffset;
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               BlockPos offset = var7[var9];
               Block block = mc.field_71441_e.func_180495_p(pos.func_177971_a(offset)).func_177230_c();
               if (block != Blocks.field_150357_h) {
                  color = new PandoraColor(this.otherColor.getValue(), 255);
               }

               if (block != Blocks.field_150357_h && block != Blocks.field_150343_Z && block != Blocks.field_150477_bB && block != Blocks.field_150467_bQ) {
                  isSafe = false;
                  break;
               }
            }

            if (isSafe) {
               this.renderHoles.put(pos, color);
            }
         }
      }
   }

   public void onWorldRender(RenderEvent event) {
      if (mc.field_71439_g != null && mc.field_71441_e != null && this.renderHoles != null && !this.renderHoles.isEmpty()) {
         this.renderHoles.forEach((blockPos, color) -> {
            this.renderHoles(blockPos, color);
         });
      }
   }

   private void renderHoles(BlockPos blockPos, PandoraColor color) {
      String var3 = this.type.getValue();
      byte var4 = -1;
      switch(var3.hashCode()) {
      case 2076577:
         if (var3.equals("Both")) {
            var4 = 2;
         }
         break;
      case 2189731:
         if (var3.equals("Fill")) {
            var4 = 1;
         }
         break;
      case 558407714:
         if (var3.equals("Outline")) {
            var4 = 0;
         }
      }

      switch(var4) {
      case 0:
         this.renderOutline(blockPos, color);
         break;
      case 1:
         this.renderFill(blockPos, color);
         break;
      case 2:
         this.renderOutline(blockPos, color);
         this.renderFill(blockPos, color);
      }

   }

   private void renderFill(BlockPos blockPos, PandoraColor color) {
      PandoraColor fillColor = new PandoraColor(color, 50);
      String var4 = this.mode.getValue();
      byte var5 = -1;
      switch(var4.hashCode()) {
      case 65834:
         if (var4.equals("Air")) {
            var5 = 0;
         }
         break;
      case 2192281:
         if (var4.equals("Flat")) {
            var5 = 2;
         }
         break;
      case 2579546:
         if (var4.equals("Slab")) {
            var5 = 3;
         }
         break;
      case 2052876273:
         if (var4.equals("Double")) {
            var5 = 4;
         }
         break;
      case 2141373863:
         if (var4.equals("Ground")) {
            var5 = 1;
         }
      }

      switch(var5) {
      case 0:
         if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v))) {
            PandoraTessellator.drawBox(blockPos, 1.0D, fillColor, 1);
         } else {
            PandoraTessellator.drawBox(blockPos, 1.0D, fillColor, 63);
         }
         break;
      case 1:
         PandoraTessellator.drawBox(blockPos.func_177977_b(), 1.0D, fillColor, 63);
         break;
      case 2:
         PandoraTessellator.drawBox(blockPos, 1.0D, fillColor, 1);
         break;
      case 3:
         if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v))) {
            PandoraTessellator.drawBox(blockPos, 1.0D, fillColor, 1);
         } else {
            PandoraTessellator.drawBox(blockPos, this.slabHeight.getValue(), fillColor, 63);
         }
         break;
      case 4:
         if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v))) {
            PandoraTessellator.drawBox(blockPos, 1.0D, fillColor, 1);
         } else {
            PandoraTessellator.drawBox(blockPos, 2.0D, fillColor, 63);
         }
      }

   }

   private void renderOutline(BlockPos blockPos, PandoraColor color) {
      PandoraColor outlineColor = new PandoraColor(color, 255);
      String var4 = this.mode.getValue();
      byte var5 = -1;
      switch(var4.hashCode()) {
      case 65834:
         if (var4.equals("Air")) {
            var5 = 0;
         }
         break;
      case 2192281:
         if (var4.equals("Flat")) {
            var5 = 2;
         }
         break;
      case 2579546:
         if (var4.equals("Slab")) {
            var5 = 3;
         }
         break;
      case 2052876273:
         if (var4.equals("Double")) {
            var5 = 4;
         }
         break;
      case 2141373863:
         if (var4.equals("Ground")) {
            var5 = 1;
         }
      }

      switch(var5) {
      case 0:
         if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v))) {
            PandoraTessellator.drawBoundingBoxWithSides((BlockPos)blockPos, this.width.getValue(), outlineColor, 1);
         } else {
            PandoraTessellator.drawBoundingBox(blockPos, 1.0D, (float)this.width.getValue(), outlineColor);
         }
         break;
      case 1:
         PandoraTessellator.drawBoundingBox(blockPos.func_177977_b(), 1.0D, (float)this.width.getValue(), outlineColor);
         break;
      case 2:
         PandoraTessellator.drawBoundingBoxWithSides((BlockPos)blockPos, this.width.getValue(), outlineColor, 1);
         break;
      case 3:
         if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v))) {
            PandoraTessellator.drawBoundingBoxWithSides((BlockPos)blockPos, this.width.getValue(), outlineColor, 1);
         } else {
            PandoraTessellator.drawBoundingBox(blockPos, this.slabHeight.getValue(), (float)this.width.getValue(), outlineColor);
         }
         break;
      case 4:
         if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v))) {
            PandoraTessellator.drawBoundingBoxWithSides((BlockPos)blockPos, this.width.getValue(), outlineColor, 1);
         } else {
            PandoraTessellator.drawBoundingBox(blockPos, 2.0D, (float)this.width.getValue(), outlineColor);
         }
      }

   }
}
