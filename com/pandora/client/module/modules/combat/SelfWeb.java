package com.pandora.client.module.modules.combat;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.world.BlockUtils;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.gui.ColorMain;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWeb;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SelfWeb extends Module {
   Setting.Boolean chatMsg;
   Setting.Boolean shiftOnly;
   Setting.Boolean singleWeb;
   Setting.Boolean rotate;
   Setting.Boolean disableNone;
   Setting.Integer tickDelay;
   Setting.Integer blocksPerTick;
   Setting.Mode placeType;
   private boolean noWeb = false;
   private boolean isSneaking = false;
   private boolean firstRun = false;
   private int blocksPlaced;
   private int delayTimeTicks = 0;
   private final int playerYLevel = 0;
   private int offsetSteps = 0;
   private int oldSlot = -1;

   public SelfWeb() {
      super("SelfWeb", Module.Category.Combat);
   }

   public void setup() {
      ArrayList<String> placeModes = new ArrayList();
      placeModes.add("Single");
      placeModes.add("Double");
      this.placeType = this.registerMode("Place", "Place", placeModes, "Single");
      this.shiftOnly = this.registerBoolean("Shift Only", "ShiftOnly", false);
      this.singleWeb = this.registerBoolean("One Place", "OnePlace", false);
      this.disableNone = this.registerBoolean("Disable No Web", "DisableNoWeb", true);
      this.rotate = this.registerBoolean("Rotate", "Rotate", true);
      this.tickDelay = this.registerInteger("Tick Delay", "TickDelay", 5, 0, 10);
      this.blocksPerTick = this.registerInteger("Blocks Per Tick", "BlocksPerTick", 4, 0, 8);
      this.chatMsg = this.registerBoolean("Chat Msgs", "ChatMsgs", true);
   }

   public void onEnable() {
      if (mc.field_71439_g == null) {
         this.disable();
      } else {
         if (this.chatMsg.getValue()) {
            MessageBus.sendClientPrefixMessage(ColorMain.getEnabledColor() + "SelfWeb turned ON!");
         }

         this.oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
         if (this.findWebSlot() != -1) {
            mc.field_71439_g.field_71071_by.field_70461_c = this.findWebSlot();
         }

      }
   }

   public void onDisable() {
      if (mc.field_71439_g != null) {
         if (this.chatMsg.getValue()) {
            if (this.noWeb) {
               MessageBus.sendClientPrefixMessage(ColorMain.getDisabledColor() + "No web detected... SelfWeb turned OFF!");
            } else {
               MessageBus.sendClientPrefixMessage(ColorMain.getDisabledColor() + "SelfWeb turned OFF!");
            }
         }

         if (this.isSneaking) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
            this.isSneaking = false;
         }

         if (this.oldSlot != mc.field_71439_g.field_71071_by.field_70461_c && this.oldSlot != -1) {
            mc.field_71439_g.field_71071_by.field_70461_c = this.oldSlot;
            this.oldSlot = -1;
         }

         this.noWeb = false;
         this.firstRun = true;
         AutoCrystal.stopAC = false;
      }
   }

   public void onUpdate() {
      if (mc.field_71439_g == null) {
         this.disable();
      } else if (this.disableNone.getValue() && this.noWeb) {
         this.disable();
      } else if (!(mc.field_71439_g.field_70163_u <= 0.0D)) {
         if (this.singleWeb.getValue() && this.blocksPlaced >= 1) {
            this.blocksPlaced = 0;
            this.disable();
         } else {
            if (this.firstRun) {
               this.firstRun = false;
               if (this.findWebSlot() == -1) {
                  this.noWeb = true;
               }
            } else {
               if (this.delayTimeTicks < this.tickDelay.getValue()) {
                  ++this.delayTimeTicks;
                  return;
               }

               this.delayTimeTicks = 0;
            }

            if (!this.shiftOnly.getValue() || mc.field_71439_g.func_70093_af()) {
               this.blocksPlaced = 0;

               while(this.blocksPlaced <= this.blocksPerTick.getValue()) {
                  Vec3d[] offsetPattern;
                  int maxSteps;
                  if (this.placeType.getValue().equalsIgnoreCase("Double")) {
                     offsetPattern = SelfWeb.Offsets.DOUBLE;
                     maxSteps = SelfWeb.Offsets.DOUBLE.length;
                  } else {
                     offsetPattern = SelfWeb.Offsets.SINGLE;
                     maxSteps = SelfWeb.Offsets.SINGLE.length;
                  }

                  if (this.offsetSteps >= maxSteps) {
                     this.offsetSteps = 0;
                     break;
                  }

                  BlockPos offsetPos = new BlockPos(offsetPattern[this.offsetSteps]);
                  BlockPos targetPos = (new BlockPos(mc.field_71439_g.func_174791_d())).func_177982_a(offsetPos.func_177958_n(), offsetPos.func_177956_o(), offsetPos.func_177952_p());
                  boolean tryPlacing = true;
                  if (!mc.field_71441_e.func_180495_p(targetPos).func_185904_a().func_76222_j()) {
                     tryPlacing = false;
                  }

                  if (tryPlacing && this.placeBlock(targetPos)) {
                     ++this.blocksPlaced;
                  }

                  ++this.offsetSteps;
                  if (this.isSneaking) {
                     mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
                     this.isSneaking = false;
                  }
               }

            }
         }
      }
   }

   private int findWebSlot() {
      int slot = -1;

      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack != ItemStack.field_190927_a && stack.func_77973_b() instanceof ItemBlock) {
            Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
            if (block instanceof BlockWeb) {
               slot = i;
               break;
            }
         }
      }

      return slot;
   }

   private boolean placeBlock(BlockPos pos) {
      Block block = mc.field_71441_e.func_180495_p(pos).func_177230_c();
      if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
         return false;
      } else {
         EnumFacing side = BlockUtils.getPlaceableSide(pos);
         if (side == null) {
            return false;
         } else {
            BlockPos neighbour = pos.func_177972_a(side);
            EnumFacing opposite = side.func_176734_d();
            if (!BlockUtils.canBeClicked(neighbour)) {
               return false;
            } else {
               Vec3d hitVec = (new Vec3d(neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
               Block neighbourBlock = mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
               int webSlot = this.findWebSlot();
               if (mc.field_71439_g.field_71071_by.field_70461_c != webSlot) {
                  mc.field_71439_g.field_71071_by.field_70461_c = webSlot;
               }

               if (!this.isSneaking && BlockUtils.blackList.contains(neighbourBlock) || BlockUtils.shulkerList.contains(neighbourBlock)) {
                  mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
                  this.isSneaking = true;
               }

               if (webSlot == -1) {
                  this.noWeb = true;
                  return false;
               } else {
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

   private static class Offsets {
      private static final Vec3d[] SINGLE = new Vec3d[]{new Vec3d(0.0D, 0.0D, 0.0D)};
      private static final Vec3d[] DOUBLE = new Vec3d[]{new Vec3d(0.0D, 0.0D, 0.0D), new Vec3d(0.0D, 1.0D, 0.0D)};
   }
}
