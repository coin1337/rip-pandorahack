package com.pandora.client.module.modules.render;

import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.api.util.render.PandoraTessellator;
import com.pandora.client.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class CityESP extends Module {
   Setting.Mode targetMode;
   Setting.Mode selectMode;
   Setting.Mode renderMode;
   Setting.Integer range;
   Setting.Integer width;
   Setting.ColorSetting color;

   public CityESP() {
      super("CityESP", Module.Category.Render);
   }

   public void setup() {
      ArrayList<String> targetModes = new ArrayList();
      targetModes.add("Single");
      targetModes.add("All");
      ArrayList<String> selectModes = new ArrayList();
      selectModes.add("Closest");
      selectModes.add("All");
      ArrayList<String> renderModes = new ArrayList();
      renderModes.add("Outline");
      renderModes.add("Fill");
      renderModes.add("Both");
      this.range = this.registerInteger("Range", "Range", 20, 1, 30);
      this.targetMode = this.registerMode("Target", "Target", targetModes, "Single");
      this.selectMode = this.registerMode("Select", "Select", selectModes, "Closest");
      this.renderMode = this.registerMode("Render", "Render", renderModes, "Both");
      this.width = this.registerInteger("Width", "Width", 1, 1, 10);
      this.color = this.registerColor("Color", "Color", new PandoraColor(102, 51, 153));
   }

   public void onWorldRender(RenderEvent event) {
      if (mc.field_71439_g != null || mc.field_71441_e != null) {
         mc.field_71441_e.field_73010_i.stream().filter((entityPlayer) -> {
            return entityPlayer.func_70032_d(mc.field_71439_g) <= (float)this.range.getValue();
         }).filter((entityPlayer) -> {
            return entityPlayer != mc.field_71439_g;
         }).filter((entityPlayer) -> {
            return !Friends.isFriend(entityPlayer.func_70005_c_());
         }).forEach((entityPlayer) -> {
            if (entityPlayer != mc.field_71439_g) {
               if (this.isTrapped(entityPlayer)) {
                  List<BlockPos> renderBlocks = new ArrayList();
                  renderBlocks.addAll(this.getBlocksToRender(entityPlayer));
                  if (renderBlocks != null) {
                     this.renderBox(renderBlocks);
                  }

                  if (this.targetMode.getValue().equalsIgnoreCase("All")) {
                     return;
                  }
               }

            }
         });
      }

   }

   private boolean isTrapped(EntityPlayer entityPlayer) {
      BlockPos blockPos = new BlockPos(entityPlayer.field_70165_t, entityPlayer.field_70163_u, entityPlayer.field_70161_v);
      return mc.field_71441_e.func_180495_p(blockPos.func_177974_f()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177976_e()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177978_c()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177968_d()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2)).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177977_b()).func_177230_c() != Blocks.field_150350_a;
   }

   private List<BlockPos> getBlocksToRender(EntityPlayer entityPlayer) {
      NonNullList<BlockPos> blockPosList = NonNullList.func_191196_a();
      BlockPos blockPos = new BlockPos(entityPlayer.field_70165_t, entityPlayer.field_70163_u, entityPlayer.field_70161_v);
      if (mc.field_71441_e.func_180495_p(blockPos.func_177974_f()).func_177230_c() != Blocks.field_150357_h) {
         blockPosList.add(blockPos.func_177974_f());
      }

      if (mc.field_71441_e.func_180495_p(blockPos.func_177976_e()).func_177230_c() != Blocks.field_150357_h) {
         blockPosList.add(blockPos.func_177976_e());
      }

      if (mc.field_71441_e.func_180495_p(blockPos.func_177978_c()).func_177230_c() != Blocks.field_150357_h) {
         blockPosList.add(blockPos.func_177978_c());
      }

      if (mc.field_71441_e.func_180495_p(blockPos.func_177968_d()).func_177230_c() != Blocks.field_150357_h) {
         blockPosList.add(blockPos.func_177968_d());
      }

      return blockPosList;
   }

   private void renderBox(List<BlockPos> blockPosList) {
      String var2 = this.selectMode.getValue();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -1763776967:
         if (var2.equals("Closest")) {
            var3 = 0;
         }
         break;
      case 65921:
         if (var2.equals("All")) {
            var3 = 1;
         }
      }

      switch(var3) {
      case 0:
         BlockPos renderPos = (BlockPos)blockPosList.stream().sorted(Comparator.comparing((blockPosx) -> {
            return blockPosx.func_185332_f((int)mc.field_71439_g.field_70165_t, (int)mc.field_71439_g.field_70163_u, (int)mc.field_71439_g.field_70161_v);
         })).findFirst().orElse((Object)null);
         if (renderPos != null) {
            this.renderBox2(renderPos);
         }
         break;
      case 1:
         Iterator var4 = blockPosList.iterator();

         while(var4.hasNext()) {
            BlockPos blockPos = (BlockPos)var4.next();
            this.renderBox2(blockPos);
         }
      }

   }

   private void renderBox2(BlockPos blockPos) {
      PandoraColor pandoraColor1 = new PandoraColor(this.color.getValue(), 255);
      PandoraColor pandoraColor2 = new PandoraColor(this.color.getValue(), 50);
      String var4 = this.renderMode.getValue();
      byte var5 = -1;
      switch(var4.hashCode()) {
      case 2076577:
         if (var4.equals("Both")) {
            var5 = 0;
         }
         break;
      case 2189731:
         if (var4.equals("Fill")) {
            var5 = 2;
         }
         break;
      case 558407714:
         if (var4.equals("Outline")) {
            var5 = 1;
         }
      }

      switch(var5) {
      case 0:
         PandoraTessellator.drawBox(blockPos, 1.0D, pandoraColor2, 63);
         PandoraTessellator.drawBoundingBox(blockPos, 1.0D, (float)this.width.getValue(), pandoraColor1);
         break;
      case 1:
         PandoraTessellator.drawBoundingBox(blockPos, 1.0D, (float)this.width.getValue(), pandoraColor1);
         break;
      case 2:
         PandoraTessellator.drawBox(blockPos, 1.0D, pandoraColor2, 63);
      }

   }
}
