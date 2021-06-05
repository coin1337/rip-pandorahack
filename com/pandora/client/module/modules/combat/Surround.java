package com.pandora.client.module.modules.combat;

import com.pandora.api.settings.Setting;
import com.pandora.client.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Surround extends Module {
   private Setting.Boolean sneak = this.registerBoolean("Only When Sneaking", "Sneak", false);
   private Setting.Boolean teleport = this.registerBoolean("Auto Center", "Teleport", true);
   private Setting.Boolean endChest = this.registerBoolean("Use EChests", "EndChest", false);
   private Setting.Boolean jumpDisable = this.registerBoolean("Disable On Jump", "JumpDisable", true);
   private Setting.Boolean autoToggle = this.registerBoolean("Auto Toggle", "AutoToggle", false);
   private Setting.Boolean chainPopToggle = this.registerBoolean("Anti Chain Pop", "ChainPopToggle", false);

   public Surround() {
      super("Surround", Module.Category.Combat);
   }

   public static boolean hasNeighbour(BlockPos blockPos) {
      EnumFacing[] var1 = EnumFacing.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumFacing side = var1[var3];
         BlockPos neighbour = blockPos.func_177972_a(side);
         if (!mc.field_71441_e.func_180495_p(neighbour).func_185904_a().func_76222_j()) {
            return true;
         }
      }

      return false;
   }

   public void onUpdate() {
      if (mc.field_71439_g != null) {
         if (!this.sneak.getValue() || mc.field_71474_y.field_74311_E.func_151470_d()) {
            if (!mc.field_71439_g.field_70122_E && this.jumpDisable.getValue()) {
               this.toggle();
            } else {
               Vec3d vec3d = getInterpolatedPos(mc.field_71439_g, 0.0F);
               BlockPos northBlockPos = (new BlockPos(vec3d)).func_177978_c();
               BlockPos southBlockPos = (new BlockPos(vec3d)).func_177968_d();
               BlockPos eastBlockPos = (new BlockPos(vec3d)).func_177974_f();
               BlockPos westBlockPos = (new BlockPos(vec3d)).func_177976_e();
               int newSlot = this.findBlockInHotbar();
               if (newSlot != -1) {
                  BlockPos centerPos = mc.field_71439_g.func_180425_c();
                  double y = (double)centerPos.func_177956_o();
                  double x = (double)centerPos.func_177958_n();
                  double z = (double)centerPos.func_177952_p();
                  Vec3d plusPlus = new Vec3d(x + 0.5D, y, z + 0.5D);
                  Vec3d plusMinus = new Vec3d(x + 0.5D, y, z - 0.5D);
                  Vec3d minusMinus = new Vec3d(x - 0.5D, y, z - 0.5D);
                  Vec3d minusPlus = new Vec3d(x - 0.5D, y, z + 0.5D);
                  int oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
                  mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
                  EnumFacing[] var19;
                  int var20;
                  int var21;
                  EnumFacing side;
                  BlockPos neighbour;
                  if (!hasNeighbour(northBlockPos)) {
                     var19 = EnumFacing.values();
                     var20 = var19.length;

                     for(var21 = 0; var21 < var20; ++var21) {
                        side = var19[var21];
                        neighbour = northBlockPos.func_177972_a(side);
                        if (hasNeighbour(neighbour)) {
                           northBlockPos = neighbour;
                           break;
                        }
                     }
                  }

                  if (!hasNeighbour(southBlockPos)) {
                     var19 = EnumFacing.values();
                     var20 = var19.length;

                     for(var21 = 0; var21 < var20; ++var21) {
                        side = var19[var21];
                        neighbour = southBlockPos.func_177972_a(side);
                        if (hasNeighbour(neighbour)) {
                           southBlockPos = neighbour;
                           break;
                        }
                     }
                  }

                  if (!hasNeighbour(eastBlockPos)) {
                     var19 = EnumFacing.values();
                     var20 = var19.length;

                     for(var21 = 0; var21 < var20; ++var21) {
                        side = var19[var21];
                        neighbour = eastBlockPos.func_177972_a(side);
                        if (hasNeighbour(neighbour)) {
                           eastBlockPos = neighbour;
                           break;
                        }
                     }
                  }

                  if (!hasNeighbour(westBlockPos)) {
                     var19 = EnumFacing.values();
                     var20 = var19.length;

                     for(var21 = 0; var21 < var20; ++var21) {
                        side = var19[var21];
                        neighbour = westBlockPos.func_177972_a(side);
                        if (hasNeighbour(neighbour)) {
                           westBlockPos = neighbour;
                           break;
                        }
                     }
                  }

                  if (mc.field_71441_e.func_180495_p(northBlockPos).func_185904_a().func_76222_j() && this.isEntitiesEmpty(northBlockPos)) {
                     if (mc.field_71439_g.field_70122_E && this.teleport.getValue()) {
                        if (this.getDst(plusPlus) < this.getDst(plusMinus) && this.getDst(plusPlus) < this.getDst(minusMinus) && this.getDst(plusPlus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() + 0.5D;
                           z = (double)centerPos.func_177952_p() + 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(plusMinus) < this.getDst(plusPlus) && this.getDst(plusMinus) < this.getDst(minusMinus) && this.getDst(plusMinus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() + 0.5D;
                           z = (double)centerPos.func_177952_p() - 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(minusMinus) < this.getDst(plusPlus) && this.getDst(minusMinus) < this.getDst(plusMinus) && this.getDst(minusMinus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() - 0.5D;
                           z = (double)centerPos.func_177952_p() - 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(minusPlus) < this.getDst(plusPlus) && this.getDst(minusPlus) < this.getDst(plusMinus) && this.getDst(minusPlus) < this.getDst(minusMinus)) {
                           x = (double)centerPos.func_177958_n() - 0.5D;
                           z = (double)centerPos.func_177952_p() + 0.5D;
                           this.centerPlayer(x, y, z);
                        }
                     }

                     placeBlockScaffold(northBlockPos, true);
                  }

                  if (mc.field_71441_e.func_180495_p(southBlockPos).func_185904_a().func_76222_j() && this.isEntitiesEmpty(southBlockPos)) {
                     if (mc.field_71439_g.field_70122_E && this.teleport.getValue()) {
                        if (this.getDst(plusPlus) < this.getDst(plusMinus) && this.getDst(plusPlus) < this.getDst(minusMinus) && this.getDst(plusPlus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() + 0.5D;
                           z = (double)centerPos.func_177952_p() + 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(plusMinus) < this.getDst(plusPlus) && this.getDst(plusMinus) < this.getDst(minusMinus) && this.getDst(plusMinus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() + 0.5D;
                           z = (double)centerPos.func_177952_p() - 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(minusMinus) < this.getDst(plusPlus) && this.getDst(minusMinus) < this.getDst(plusMinus) && this.getDst(minusMinus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() - 0.5D;
                           z = (double)centerPos.func_177952_p() - 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(minusPlus) < this.getDst(plusPlus) && this.getDst(minusPlus) < this.getDst(plusMinus) && this.getDst(minusPlus) < this.getDst(minusMinus)) {
                           x = (double)centerPos.func_177958_n() - 0.5D;
                           z = (double)centerPos.func_177952_p() + 0.5D;
                           this.centerPlayer(x, y, z);
                        }
                     }

                     placeBlockScaffold(southBlockPos, true);
                  }

                  if (mc.field_71441_e.func_180495_p(eastBlockPos).func_185904_a().func_76222_j() && this.isEntitiesEmpty(eastBlockPos)) {
                     if (mc.field_71439_g.field_70122_E && this.teleport.getValue()) {
                        if (this.getDst(plusPlus) < this.getDst(plusMinus) && this.getDst(plusPlus) < this.getDst(minusMinus) && this.getDst(plusPlus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() + 0.5D;
                           z = (double)centerPos.func_177952_p() + 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(plusMinus) < this.getDst(plusPlus) && this.getDst(plusMinus) < this.getDst(minusMinus) && this.getDst(plusMinus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() + 0.5D;
                           z = (double)centerPos.func_177952_p() - 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(minusMinus) < this.getDst(plusPlus) && this.getDst(minusMinus) < this.getDst(plusMinus) && this.getDst(minusMinus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() - 0.5D;
                           z = (double)centerPos.func_177952_p() - 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(minusPlus) < this.getDst(plusPlus) && this.getDst(minusPlus) < this.getDst(plusMinus) && this.getDst(minusPlus) < this.getDst(minusMinus)) {
                           x = (double)centerPos.func_177958_n() - 0.5D;
                           z = (double)centerPos.func_177952_p() + 0.5D;
                           this.centerPlayer(x, y, z);
                        }
                     }

                     placeBlockScaffold(eastBlockPos, true);
                  }

                  if (mc.field_71441_e.func_180495_p(westBlockPos).func_185904_a().func_76222_j() && this.isEntitiesEmpty(westBlockPos)) {
                     if (mc.field_71439_g.field_70122_E && this.teleport.getValue()) {
                        if (this.getDst(plusPlus) < this.getDst(plusMinus) && this.getDst(plusPlus) < this.getDst(minusMinus) && this.getDst(plusPlus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() + 0.5D;
                           z = (double)centerPos.func_177952_p() + 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(plusMinus) < this.getDst(plusPlus) && this.getDst(plusMinus) < this.getDst(minusMinus) && this.getDst(plusMinus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() + 0.5D;
                           z = (double)centerPos.func_177952_p() - 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(minusMinus) < this.getDst(plusPlus) && this.getDst(minusMinus) < this.getDst(plusMinus) && this.getDst(minusMinus) < this.getDst(minusPlus)) {
                           x = (double)centerPos.func_177958_n() - 0.5D;
                           z = (double)centerPos.func_177952_p() - 0.5D;
                           this.centerPlayer(x, y, z);
                        }

                        if (this.getDst(minusPlus) < this.getDst(plusPlus) && this.getDst(minusPlus) < this.getDst(plusMinus) && this.getDst(minusPlus) < this.getDst(minusMinus)) {
                           x = (double)centerPos.func_177958_n() - 0.5D;
                           z = (double)centerPos.func_177952_p() + 0.5D;
                           this.centerPlayer(x, y, z);
                        }
                     }

                     placeBlockScaffold(westBlockPos, true);
                  }

                  mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
                  if ((this.autoToggle.getValue() || this.chainPopToggle.getValue()) && (mc.field_71441_e.func_180495_p((new BlockPos(vec3d)).func_177978_c()).func_177230_c() == Blocks.field_150343_Z || mc.field_71441_e.func_180495_p((new BlockPos(vec3d)).func_177978_c()).func_177230_c() == Blocks.field_150357_h) && (mc.field_71441_e.func_180495_p((new BlockPos(vec3d)).func_177968_d()).func_177230_c() == Blocks.field_150343_Z || mc.field_71441_e.func_180495_p((new BlockPos(vec3d)).func_177968_d()).func_177230_c() == Blocks.field_150357_h) && (mc.field_71441_e.func_180495_p((new BlockPos(vec3d)).func_177976_e()).func_177230_c() == Blocks.field_150343_Z || mc.field_71441_e.func_180495_p((new BlockPos(vec3d)).func_177976_e()).func_177230_c() == Blocks.field_150357_h) && (mc.field_71441_e.func_180495_p((new BlockPos(vec3d)).func_177974_f()).func_177230_c() == Blocks.field_150343_Z || mc.field_71441_e.func_180495_p((new BlockPos(vec3d)).func_177974_f()).func_177230_c() == Blocks.field_150357_h)) {
                     this.chainPopToggle.setValue(false);
                     this.toggle();
                  }

               }
            }
         }
      }
   }

   private int findBlockInHotbar() {
      int i;
      ItemStack stack;
      Block block;
      for(i = 0; i < 9; ++i) {
         stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
         if (stack != ItemStack.field_190927_a && stack.func_77973_b() instanceof ItemBlock) {
            block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
            if (block instanceof BlockObsidian) {
               return i;
            }
         }
      }

      if (this.endChest.getValue()) {
         for(i = 0; i < 9; ++i) {
            stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != ItemStack.field_190927_a && stack.func_77973_b() instanceof ItemBlock) {
               block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
               if (block instanceof BlockEnderChest) {
                  return i;
               }
            }
         }
      }

      return -1;
   }

   private void centerPlayer(double x, double y, double z) {
      mc.field_71439_g.field_71174_a.func_147297_a(new Position(x, y, z, true));
      mc.field_71439_g.func_70107_b(x, y, z);
   }

   private double getDst(Vec3d vec) {
      return mc.field_71439_g.func_70011_f(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
   }

   private boolean isEntitiesEmpty(BlockPos pos) {
      return mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(pos)).stream().filter((e) -> {
         return !(e instanceof EntityItem) && !(e instanceof EntityXPOrb);
      }).count() == 0L;
   }

   public static void placeBlockScaffold(BlockPos pos, boolean rotate) {
      EnumFacing[] var2 = EnumFacing.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing side = var2[var4];
         BlockPos neighbor = pos.func_177972_a(side);
         EnumFacing side2 = side.func_176734_d();
         if (canBeClicked(neighbor)) {
            Vec3d hitVec = (new Vec3d(neighbor)).func_178787_e(new Vec3d(0.5D, 0.5D, 0.5D)).func_178787_e((new Vec3d(side2.func_176730_m())).func_186678_a(0.5D));
            if (rotate) {
               faceVectorPacketInstant(hitVec);
            }

            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
            processRightClickBlock(neighbor, side2, hitVec);
            mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            mc.field_71467_ac = 0;
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
            return;
         }
      }

   }

   private static PlayerControllerMP getPlayerController() {
      return mc.field_71442_b;
   }

   public static void processRightClickBlock(BlockPos pos, EnumFacing side, Vec3d hitVec) {
      getPlayerController().func_187099_a(mc.field_71439_g, mc.field_71441_e, pos, side, hitVec, EnumHand.MAIN_HAND);
   }

   public static boolean canBeClicked(BlockPos pos) {
      return mc.field_71441_e.func_180495_p(pos).func_177230_c().func_176209_a(mc.field_71441_e.func_180495_p(pos), false);
   }

   public static void faceVectorPacketInstant(Vec3d vec) {
      float[] rotations = getNeededRotations2(vec);
      mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(rotations[0], rotations[1], mc.field_71439_g.field_70122_E));
   }

   private static float[] getNeededRotations2(Vec3d vec) {
      Vec3d eyesPos = getEyesPos();
      double diffX = vec.field_72450_a - eyesPos.field_72450_a;
      double diffY = vec.field_72448_b - eyesPos.field_72448_b;
      double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      return new float[]{mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - mc.field_71439_g.field_70177_z), mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - mc.field_71439_g.field_70125_A)};
   }

   public static Vec3d getEyesPos() {
      return new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v);
   }

   public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
      return (new Vec3d(entity.field_70142_S, entity.field_70137_T, entity.field_70136_U)).func_178787_e(getInterpolatedAmount(entity, (double)ticks));
   }

   public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
      return getInterpolatedAmount(entity, ticks, ticks, ticks);
   }

   public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
      return new Vec3d((entity.field_70165_t - entity.field_70142_S) * x, (entity.field_70163_u - entity.field_70137_T) * y, (entity.field_70161_v - entity.field_70136_U) * z);
   }
}
