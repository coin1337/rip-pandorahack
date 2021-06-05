package com.pandora.client.module.modules.render;

import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.api.util.render.PandoraTessellator;
import com.pandora.client.module.Module;
import java.util.ArrayList;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

public class BlockHighlight extends Module {
   Setting.Integer lineWidth;
   Setting.Mode renderType;
   Setting.Mode renderLook;
   Setting.ColorSetting renderColor;
   private int lookInt;

   public BlockHighlight() {
      super("BlockHighlight", Module.Category.Render);
   }

   public void setup() {
      ArrayList<String> renderLooks = new ArrayList();
      renderLooks.add("Block");
      renderLooks.add("Side");
      ArrayList<String> renderTypes = new ArrayList();
      renderTypes.add("Outline");
      renderTypes.add("Fill");
      renderTypes.add("Both");
      this.renderLook = this.registerMode("Render", "Render", renderLooks, "Block");
      this.renderType = this.registerMode("Type", "Type", renderTypes, "Outline");
      this.lineWidth = this.registerInteger("Width", "Width", 1, 0, 5);
      this.renderColor = this.registerColor("Color", "Color");
   }

   public void onWorldRender(RenderEvent event) {
      RayTraceResult rayTraceResult = mc.field_71476_x;
      EnumFacing enumFacing = mc.field_71476_x.field_178784_b;
      PandoraColor colorWithOpacity = new PandoraColor(this.renderColor.getValue(), 50);
      String var7 = this.renderLook.getValue();
      byte var8 = -1;
      switch(var7.hashCode()) {
      case 2576759:
         if (var7.equals("Side")) {
            var8 = 1;
         }
         break;
      case 64279661:
         if (var7.equals("Block")) {
            var8 = 0;
         }
      }

      switch(var8) {
      case 0:
         this.lookInt = 0;
         break;
      case 1:
         this.lookInt = 1;
      }

      if (rayTraceResult != null && rayTraceResult.field_72313_a == Type.BLOCK) {
         BlockPos blockPos = rayTraceResult.func_178782_a();
         AxisAlignedBB axisAlignedBB = mc.field_71441_e.func_180495_p(blockPos).func_185918_c(mc.field_71441_e, blockPos);
         if (axisAlignedBB != null && blockPos != null && mc.field_71441_e.func_180495_p(blockPos).func_185904_a() != Material.field_151579_a) {
            var7 = this.renderType.getValue();
            var8 = -1;
            switch(var7.hashCode()) {
            case 2076577:
               if (var7.equals("Both")) {
                  var8 = 2;
               }
               break;
            case 2189731:
               if (var7.equals("Fill")) {
                  var8 = 1;
               }
               break;
            case 558407714:
               if (var7.equals("Outline")) {
                  var8 = 0;
               }
            }

            switch(var8) {
            case 0:
               this.renderOutline(axisAlignedBB, this.lineWidth.getValue(), this.renderColor.getValue(), enumFacing, this.lookInt);
               break;
            case 1:
               this.renderFill(axisAlignedBB, colorWithOpacity, enumFacing, this.lookInt);
               break;
            case 2:
               this.renderOutline(axisAlignedBB, this.lineWidth.getValue(), this.renderColor.getValue(), enumFacing, this.lookInt);
               this.renderFill(axisAlignedBB, colorWithOpacity, enumFacing, this.lookInt);
            }
         }
      }

   }

   public void renderOutline(AxisAlignedBB axisAlignedBB, int width, PandoraColor color, EnumFacing enumFacing, int lookInt) {
      if (lookInt == 0) {
         PandoraTessellator.drawBoundingBox(axisAlignedBB, (float)width, color);
      } else if (lookInt == 1) {
         PandoraTessellator.drawBoundingBoxWithSides(axisAlignedBB, width, color, this.findRenderingSide(enumFacing));
      }

   }

   public void renderFill(AxisAlignedBB axisAlignedBB, PandoraColor color, EnumFacing enumFacing, int lookInt) {
      int facing = 0;
      if (lookInt == 0) {
         facing = 63;
      } else if (lookInt == 1) {
         facing = this.findRenderingSide(enumFacing);
      }

      PandoraTessellator.drawBox(axisAlignedBB, true, 1.0D, color, facing);
   }

   private int findRenderingSide(EnumFacing enumFacing) {
      int facing = 0;
      if (enumFacing == EnumFacing.EAST) {
         facing = 32;
      } else if (enumFacing == EnumFacing.WEST) {
         facing = 16;
      } else if (enumFacing == EnumFacing.NORTH) {
         facing = 4;
      } else if (enumFacing == EnumFacing.SOUTH) {
         facing = 8;
      } else if (enumFacing == EnumFacing.UP) {
         facing = 2;
      } else if (enumFacing == EnumFacing.DOWN) {
         facing = 1;
      }

      return facing;
   }
}
