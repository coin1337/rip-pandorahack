package com.pandora.client.module.modules.combat;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.world.BlockUtils;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.gui.ColorMain;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SelfTrap extends Module {
   Setting.Mode trapType;
   Setting.Boolean shiftOnly;
   Setting.Boolean chatMsg;
   Setting.Boolean rotate;
   Setting.Boolean disableNone;
   Setting.Boolean centerPlayer;
   Setting.Integer tickDelay;
   Setting.Integer blocksPerTick;
   private boolean noObby = false;
   private boolean isSneaking = false;
   private boolean firstRun = false;
   private int blocksPlaced;
   private int delayTimeTicks = 0;
   private final int playerYLevel = 0;
   private int offsetSteps = 0;
   private int oldSlot = -1;
   private Vec3d centeredBlock;

   public SelfTrap() {
      super("SelfTrap", Module.Category.Combat);
      this.centeredBlock = Vec3d.field_186680_a;
   }

   public void setup() {
      ArrayList<String> trapTypes = new ArrayList();
      trapTypes.add("Normal");
      trapTypes.add("No Step");
      trapTypes.add("Simple");
      this.trapType = this.registerMode("Mode", "Mode", trapTypes, "Normal");
      this.shiftOnly = this.registerBoolean("Shift Only", "ShiftOnly", false);
      this.disableNone = this.registerBoolean("Disable No Obby", "DisableNoObby", true);
      this.rotate = this.registerBoolean("Rotate", "Rotate", true);
      this.centerPlayer = this.registerBoolean("Center Player", "CenterPlayer", false);
      this.tickDelay = this.registerInteger("Tick Delay", "TickDelay", 5, 0, 10);
      this.blocksPerTick = this.registerInteger("Blocks Per Tick", "BlocksPerTick", 4, 0, 8);
      this.chatMsg = this.registerBoolean("Chat Msgs", "ChatMsgs", true);
   }

   public void onEnable() {
      if (mc.field_71439_g == null) {
         this.disable();
      } else {
         if (this.chatMsg.getValue()) {
            MessageBus.sendClientPrefixMessage(ColorMain.getEnabledColor() + "SelfTrap turned ON!");
         }

         if (this.centerPlayer.getValue() && mc.field_71439_g.field_70122_E) {
            mc.field_71439_g.field_70159_w = 0.0D;
            mc.field_71439_g.field_70179_y = 0.0D;
         }

         this.centeredBlock = this.getCenterOfBlock(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70163_u);
         this.oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
         if (this.findObsidianSlot() != -1) {
            mc.field_71439_g.field_71071_by.field_70461_c = this.findObsidianSlot();
         }

      }
   }

   public void onDisable() {
      if (mc.field_71439_g != null) {
         if (this.chatMsg.getValue()) {
            if (this.noObby) {
               MessageBus.sendClientPrefixMessage(ColorMain.getDisabledColor() + "No obsidian detected... SelfTrap turned OFF!");
            } else {
               MessageBus.sendClientPrefixMessage(ColorMain.getDisabledColor() + "SelfTrap turned OFF!");
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

         this.centeredBlock = Vec3d.field_186680_a;
         this.noObby = false;
         this.firstRun = true;
         AutoCrystal.stopAC = false;
      }
   }

   public void onUpdate() {
      if (mc.field_71439_g == null) {
         this.disable();
      } else if (this.disableNone.getValue() && this.noObby) {
         this.disable();
      } else if (!(mc.field_71439_g.field_70163_u <= 0.0D)) {
         if (this.firstRun) {
            this.firstRun = false;
            if (this.findObsidianSlot() == -1) {
               this.noObby = true;
            }
         } else {
            if (this.delayTimeTicks < this.tickDelay.getValue()) {
               ++this.delayTimeTicks;
               return;
            }

            this.delayTimeTicks = 0;
         }

         if (!this.shiftOnly.getValue() || mc.field_71439_g.func_70093_af()) {
            if (this.centerPlayer.getValue() && this.centeredBlock != Vec3d.field_186680_a && mc.field_71439_g.field_70122_E) {
               double xDeviation = Math.abs(this.centeredBlock.field_72450_a - mc.field_71439_g.field_70165_t);
               double zDeviation = Math.abs(this.centeredBlock.field_72449_c - mc.field_71439_g.field_70161_v);
               if (xDeviation <= 0.1D && zDeviation <= 0.1D) {
                  this.centeredBlock = Vec3d.field_186680_a;
               } else {
                  double newX;
                  if (mc.field_71439_g.field_70165_t > (double)Math.round(mc.field_71439_g.field_70165_t)) {
                     newX = (double)Math.round(mc.field_71439_g.field_70165_t) + 0.5D;
                  } else if (mc.field_71439_g.field_70165_t < (double)Math.round(mc.field_71439_g.field_70165_t)) {
                     newX = (double)Math.round(mc.field_71439_g.field_70165_t) - 0.5D;
                  } else {
                     newX = mc.field_71439_g.field_70165_t;
                  }

                  double newZ;
                  if (mc.field_71439_g.field_70161_v > (double)Math.round(mc.field_71439_g.field_70161_v)) {
                     newZ = (double)Math.round(mc.field_71439_g.field_70161_v) + 0.5D;
                  } else if (mc.field_71439_g.field_70161_v < (double)Math.round(mc.field_71439_g.field_70161_v)) {
                     newZ = (double)Math.round(mc.field_71439_g.field_70161_v) - 0.5D;
                  } else {
                     newZ = mc.field_71439_g.field_70161_v;
                  }

                  mc.field_71439_g.field_71174_a.func_147297_a(new Position(newX, mc.field_71439_g.field_70163_u, newZ, true));
                  mc.field_71439_g.func_70107_b(newX, mc.field_71439_g.field_70163_u, newZ);
               }
            }

            this.blocksPlaced = 0;

            while(this.blocksPlaced <= this.blocksPerTick.getValue()) {
               int maxSteps;
               Vec3d[] offsetPattern;
               if (this.trapType.getValue().equalsIgnoreCase("Normal")) {
                  offsetPattern = SelfTrap.Offsets.TRAP;
                  maxSteps = SelfTrap.Offsets.TRAP.length;
               } else if (this.trapType.getValue().equalsIgnoreCase("No Step")) {
                  offsetPattern = SelfTrap.Offsets.TRAPFULLROOF;
                  maxSteps = SelfTrap.Offsets.TRAPFULLROOF.length;
               } else {
                  offsetPattern = SelfTrap.Offsets.TRAPSIMPLE;
                  maxSteps = SelfTrap.Offsets.TRAPSIMPLE.length;
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

               Iterator var6 = mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(targetPos)).iterator();

               while(var6.hasNext()) {
                  Entity entity = (Entity)var6.next();
                  if (entity instanceof EntityPlayer) {
                     tryPlacing = false;
                     break;
                  }
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

   private int findObsidianSlot() {
      int slot = -1;

      for(int i = 0; i < 9; ++i) {
         ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack != ItemStack.field_190927_a && stack.func_77973_b() instanceof ItemBlock) {
            Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
            if (block instanceof BlockObsidian) {
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
               int obsidianSlot = this.findObsidianSlot();
               if (mc.field_71439_g.field_71071_by.field_70461_c != obsidianSlot) {
                  mc.field_71439_g.field_71071_by.field_70461_c = obsidianSlot;
               }

               if (!this.isSneaking && BlockUtils.blackList.contains(neighbourBlock) || BlockUtils.shulkerList.contains(neighbourBlock)) {
                  mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
                  this.isSneaking = true;
               }

               if (obsidianSlot == -1) {
                  this.noObby = true;
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

   private Vec3d getCenterOfBlock(double playerX, double playerY, double playerZ) {
      double newX = Math.floor(playerX) + 0.5D;
      double newY = Math.floor(playerY);
      double newZ = Math.floor(playerZ) + 0.5D;
      return new Vec3d(newX, newY, newZ);
   }

   private static class Offsets {
      private static final Vec3d[] TRAP = new Vec3d[]{new Vec3d(0.0D, -1.0D, -1.0D), new Vec3d(1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 2.0D, -1.0D), new Vec3d(0.0D, 2.0D, 0.0D)};
      private static final Vec3d[] TRAPFULLROOF = new Vec3d[]{new Vec3d(0.0D, -1.0D, -1.0D), new Vec3d(1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 2.0D, -1.0D), new Vec3d(0.0D, 2.0D, 0.0D), new Vec3d(0.0D, 3.0D, 0.0D)};
      private static final Vec3d[] TRAPSIMPLE = new Vec3d[]{new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, -1.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(-1.0D, 2.0D, 0.0D), new Vec3d(0.0D, 2.0D, 0.0D)};
   }
}
