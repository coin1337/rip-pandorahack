package com.pandora.client.module.modules.combat;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.world.BlockUtils;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.gui.ColorMain;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class HoleFill extends Module {
   Setting.Boolean chatMsgs;
   Setting.Boolean autoSwitch;
   Setting.Boolean rotate;
   Setting.Integer placeDelay;
   Setting.Double horizontalRange;
   Setting.Double verticalRange;
   Setting.Mode mode;
   private boolean isSneaking = false;
   private int delayTicks = 0;
   private int oldHandEnable = -1;

   public HoleFill() {
      super("HoleFill", Module.Category.Combat);
   }

   public void setup() {
      ArrayList<String> modes = new ArrayList();
      modes.add("Obby");
      modes.add("Echest");
      modes.add("Both");
      modes.add("Web");
      this.mode = this.registerMode("Type", "Type", modes, "Obby");
      this.placeDelay = this.registerInteger("Delay", "Delay", 3, 0, 10);
      this.horizontalRange = this.registerDouble("H-Range", "H-Range", 4.0D, 0.0D, 10.0D);
      this.verticalRange = this.registerDouble("V-Range", "V-Range", 2.0D, 0.0D, 5.0D);
      this.rotate = this.registerBoolean("Rotate", "Rotate", true);
      this.autoSwitch = this.registerBoolean("Switch", "Switch", true);
      this.chatMsgs = this.registerBoolean("Chat Msgs", "ChatMsgs", true);
   }

   public void onEnable() {
      if (this.chatMsgs.getValue() && mc.field_71439_g != null) {
         MessageBus.sendClientPrefixMessage(ColorMain.getEnabledColor() + "HoleFill turned ON!");
      }

      if (this.autoSwitch.getValue() && mc.field_71439_g != null) {
         this.oldHandEnable = mc.field_71439_g.field_71071_by.field_70461_c;
      }

   }

   public void onDisable() {
      if (this.chatMsgs.getValue() && mc.field_71439_g != null) {
         MessageBus.sendClientPrefixMessage(ColorMain.getDisabledColor() + "HoleFill turned OFF!");
      }

      if (this.autoSwitch.getValue() && mc.field_71439_g != null) {
         mc.field_71439_g.field_71071_by.field_70461_c = this.oldHandEnable;
      }

      if (this.isSneaking) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
         this.isSneaking = false;
      }

   }

   public void onUpdate() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         List<BlockPos> holePos = new ArrayList();
         holePos.addAll(this.findHoles());
         if (holePos != null) {
            if (this.autoSwitch.getValue()) {
               int oldHand = mc.field_71439_g.field_71071_by.field_70461_c;
               mc.field_71439_g.field_71071_by.field_70461_c = this.findRightBlock(oldHand);
            }

            BlockPos placePos = (BlockPos)holePos.stream().sorted(Comparator.comparing((blockPos) -> {
               return blockPos.func_185332_f((int)mc.field_71439_g.field_70165_t, (int)mc.field_71439_g.field_70163_u, (int)mc.field_71439_g.field_70161_v);
            })).findFirst().orElse((Object)null);
            if (placePos == null) {
               return;
            }

            Iterator var3 = mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(placePos)).iterator();

            while(var3.hasNext()) {
               Entity entity = (Entity)var3.next();
               if (entity instanceof EntityPlayer) {
                  return;
               }
            }

            if (this.delayTicks >= this.placeDelay.getValue() && this.isHoldingRightBlock(mc.field_71439_g.field_71071_by.field_70461_c, mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b()) && this.placeBlock(placePos)) {
               this.delayTicks = 0;
            }

            ++this.delayTicks;
         }

      } else {
         this.disable();
      }
   }

   private List<BlockPos> findHoles() {
      NonNullList<BlockPos> holes = NonNullList.func_191196_a();
      Iterable<BlockPos> worldPosBlockPos = BlockPos.func_177980_a(mc.field_71439_g.func_180425_c().func_177963_a(-this.horizontalRange.getValue(), -this.verticalRange.getValue(), -this.horizontalRange.getValue()), mc.field_71439_g.func_180425_c().func_177963_a(this.horizontalRange.getValue(), this.verticalRange.getValue(), this.horizontalRange.getValue()));
      Iterator var3 = worldPosBlockPos.iterator();

      while(var3.hasNext()) {
         BlockPos blockPos = (BlockPos)var3.next();
         if (this.isSurrounded(blockPos)) {
            holes.add(blockPos);
         }
      }

      return holes;
   }

   private boolean isSurrounded(BlockPos blockPos) {
      return mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177974_f()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177976_e()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177978_c()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177968_d()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177977_b()).func_177230_c() != Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177984_a()).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2)).func_177230_c() == Blocks.field_150350_a;
   }

   private int findRightBlock(int oldHand) {
      int newHand = -1;

      for(int i = 0; i < 9; ++i) {
         ItemStack itemStack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (itemStack != ItemStack.field_190927_a && itemStack.func_77973_b() instanceof ItemBlock) {
            Block block = ((ItemBlock)itemStack.func_77973_b()).func_179223_d();
            if ((this.mode.getValue().equalsIgnoreCase("Obby") || this.mode.getValue().equalsIgnoreCase("Both")) && block instanceof BlockObsidian) {
               newHand = i;
               break;
            }

            if ((this.mode.getValue().equalsIgnoreCase("Echest") || this.mode.getValue().equalsIgnoreCase("Both")) && block instanceof BlockEnderChest) {
               newHand = i;
               break;
            }

            if (this.mode.getValue().equalsIgnoreCase("Web") && block instanceof BlockWeb) {
               newHand = i;
               break;
            }
         }
      }

      if (newHand == -1) {
         newHand = oldHand;
      }

      return newHand;
   }

   private Boolean isHoldingRightBlock(int hand, Item item) {
      if (hand == -1) {
         return false;
      } else {
         if (item instanceof ItemBlock) {
            Block block = ((ItemBlock)item).func_179223_d();
            if (this.mode.getValue().equalsIgnoreCase("Obby") && block instanceof BlockObsidian) {
               return true;
            }

            if (this.mode.getValue().equalsIgnoreCase("Echest") && block instanceof BlockEnderChest) {
               return true;
            }

            if (this.mode.getValue().equalsIgnoreCase("Both") && (block instanceof BlockObsidian || block instanceof BlockEnderChest)) {
               return true;
            }

            if (this.mode.getValue().equalsIgnoreCase("Web") && block instanceof BlockWeb) {
               return true;
            }
         }

         return false;
      }
   }

   private Boolean placeBlock(BlockPos blockPos) {
      if (blockPos == null) {
         return false;
      } else {
         Block block = mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
         if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
         } else {
            EnumFacing side = BlockUtils.getPlaceableSide(blockPos);
            if (side == null) {
               return false;
            } else {
               BlockPos neighbour = blockPos.func_177972_a(side);
               EnumFacing opposite = side.func_176734_d();
               if (!BlockUtils.canBeClicked(neighbour)) {
                  return false;
               } else {
                  Vec3d hitVec = (new Vec3d(neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
                  Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
                  if (!this.isSneaking && BlockUtils.blackList.contains(neighbourBlock) || BlockUtils.shulkerList.contains(neighbourBlock)) {
                     mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
                     this.isSneaking = true;
                  }

                  boolean stoppedAC = false;
                  if (ModuleManager.isModuleEnabled("AutoCrystalGS")) {
                     AutoCrystal.stopAC = true;
                     stoppedAC = true;
                  }

                  if (this.rotate.getValue()) {
                     BlockUtils.faceVectorPacketInstant(hitVec);
                  }

                  mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
                  mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                  mc.field_71467_ac = 4;
                  if (stoppedAC) {
                     AutoCrystal.stopAC = false;
                     stoppedAC = false;
                  }

                  return true;
               }
            }
         }
      }
   }
}
