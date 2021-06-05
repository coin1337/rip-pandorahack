package com.pandora.client.module.modules.render;

import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.api.util.render.PandoraTessellator;
import com.pandora.api.util.world.BlockUtils;
import com.pandora.client.module.Module;
import io.netty.util.internal.ConcurrentSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class VoidESP extends Module {
   Setting.Integer renderDistance;
   Setting.Integer activeYValue;
   Setting.Mode renderType;
   Setting.Mode renderMode;
   Setting.Integer width;
   Setting.ColorSetting color;
   private ConcurrentSet<BlockPos> voidHoles;

   public VoidESP() {
      super("VoidESP", Module.Category.Render);
   }

   public void setup() {
      ArrayList<String> render = new ArrayList();
      render.add("Outline");
      render.add("Fill");
      render.add("Both");
      ArrayList<String> modes = new ArrayList();
      modes.add("Box");
      modes.add("Flat");
      this.renderDistance = this.registerInteger("Distance", "Distance", 10, 1, 40);
      this.activeYValue = this.registerInteger("Activate Y", "ActivateY", 20, 0, 256);
      this.renderType = this.registerMode("Render", "Render", render, "Both");
      this.renderMode = this.registerMode("Mode", "Mode", modes, "Flat");
      this.width = this.registerInteger("Width", "Width", 1, 1, 10);
      this.color = this.registerColor("Color", "Color", new PandoraColor(255, 255, 0));
   }

   public void onUpdate() {
      if (mc.field_71439_g.field_71093_bK != 1) {
         if (mc.field_71439_g.func_180425_c().func_177956_o() <= this.activeYValue.getValue()) {
            if (this.voidHoles == null) {
               this.voidHoles = new ConcurrentSet();
            } else {
               this.voidHoles.clear();
            }

            List<BlockPos> blockPosList = BlockUtils.getCircle(getPlayerPos(), 0, (float)this.renderDistance.getValue(), false);
            Iterator var2 = blockPosList.iterator();

            while(var2.hasNext()) {
               BlockPos blockPos = (BlockPos)var2.next();
               if (!mc.field_71441_e.func_180495_p(blockPos).func_177230_c().equals(Blocks.field_150357_h) && !this.isAnyBedrock(blockPos, VoidESP.Offsets.center)) {
                  this.voidHoles.add(blockPos);
               }
            }

         }
      }
   }

   public void onWorldRender(RenderEvent event) {
      if (mc.field_71439_g != null && this.voidHoles != null) {
         if (mc.field_71439_g.func_180425_c().func_177956_o() <= this.activeYValue.getValue()) {
            if (!this.voidHoles.isEmpty()) {
               this.voidHoles.forEach((blockPos) -> {
                  if (this.renderMode.getValue().equalsIgnoreCase("Box")) {
                     this.drawBox(blockPos);
                  } else {
                     this.drawFlat(blockPos);
                  }

                  this.drawOutline(blockPos, this.width.getValue());
               });
            }
         }
      }
   }

   public static BlockPos getPlayerPos() {
      return new BlockPos(Math.floor(mc.field_71439_g.field_70165_t), Math.floor(mc.field_71439_g.field_70163_u), Math.floor(mc.field_71439_g.field_70161_v));
   }

   private boolean isAnyBedrock(BlockPos origin, BlockPos[] offset) {
      BlockPos[] var3 = offset;
      int var4 = offset.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockPos pos = var3[var5];
         if (mc.field_71441_e.func_180495_p(origin.func_177971_a(pos)).func_177230_c().equals(Blocks.field_150357_h)) {
            return true;
         }
      }

      return false;
   }

   private void drawFlat(BlockPos blockPos) {
      if (this.renderType.getValue().equalsIgnoreCase("Fill") || this.renderType.getValue().equalsIgnoreCase("Both")) {
         PandoraColor c = new PandoraColor(this.color.getValue(), 50);
         if (this.renderMode.getValue().equalsIgnoreCase("Flat")) {
            PandoraTessellator.drawBox(blockPos, 1.0D, c, 1);
         }
      }

   }

   private void drawBox(BlockPos blockPos) {
      if (this.renderType.getValue().equalsIgnoreCase("Fill") || this.renderType.getValue().equalsIgnoreCase("Both")) {
         PandoraColor c = new PandoraColor(this.color.getValue(), 50);
         PandoraTessellator.drawBox(blockPos, 1.0D, c, 63);
      }

   }

   private void drawOutline(BlockPos blockPos, int width) {
      if (this.renderType.getValue().equalsIgnoreCase("Outline") || this.renderType.getValue().equalsIgnoreCase("Both")) {
         if (this.renderMode.getValue().equalsIgnoreCase("Box")) {
            PandoraTessellator.drawBoundingBox(blockPos, 1.0D, (float)width, this.color.getValue());
         }

         if (this.renderMode.getValue().equalsIgnoreCase("Flat")) {
            PandoraTessellator.drawBoundingBoxWithSides((BlockPos)blockPos, width, this.color.getValue(), 1);
         }
      }

   }

   private static class Offsets {
      static final BlockPos[] center = new BlockPos[]{new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 2, 0)};
   }
}
