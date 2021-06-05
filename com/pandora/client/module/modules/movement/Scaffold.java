package com.pandora.client.module.modules.movement;

import com.pandora.api.util.misc.Wrapper;
import com.pandora.api.util.world.EntityUtil;
import com.pandora.client.module.Module;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {
   private List blackList;
   private int future = 3;

   public Scaffold() {
      super("Scaffold", Module.Category.Movement);
      this.blackList = Arrays.asList(Blocks.field_150477_bB, Blocks.field_150486_ae, Blocks.field_150447_bR);
   }

   public static Vec3d getEyesPos() {
      return new Vec3d(Wrapper.getPlayer().field_70165_t, Wrapper.getPlayer().field_70163_u + (double)Wrapper.getPlayer().func_70047_e(), Wrapper.getPlayer().field_70161_v);
   }

   public void onUpdate() {
      if (this.isEnabled() && mc.field_71439_g != null) {
         BlockPos down = (new BlockPos(EntityUtil.getInterpolatedPos(mc.field_71439_g, (float)this.future))).func_177977_b();
         BlockPos down2 = down.func_177977_b();
         if (Wrapper.getWorld().func_180495_p(down).func_185904_a().func_76222_j()) {
            int currentItem = -1;

            int currentItem2;
            for(currentItem2 = 0; currentItem2 < 9; ++currentItem2) {
               ItemStack getStackInSlot = Wrapper.getPlayer().field_71071_by.func_70301_a(currentItem2);
               if (getStackInSlot != ItemStack.field_190927_a && getStackInSlot.func_77973_b() instanceof ItemBlock) {
                  Block getBlock = ((ItemBlock)getStackInSlot.func_77973_b()).func_179223_d();
                  if (!this.blackList.contains(getBlock) && !(getBlock instanceof BlockContainer) && Block.func_149634_a(getStackInSlot.func_77973_b()).func_176223_P().func_185913_b() && (!(((ItemBlock)getStackInSlot.func_77973_b()).func_179223_d() instanceof BlockFalling) || !Wrapper.getWorld().func_180495_p(down2).func_185904_a().func_76222_j())) {
                     currentItem = currentItem2;
                     break;
                  }
               }
            }

            if (currentItem != -1) {
               currentItem2 = Wrapper.getPlayer().field_71071_by.field_70461_c;
               Wrapper.getPlayer().field_71071_by.field_70461_c = currentItem;
               if (!this.hasNeighbour(down)) {
                  EnumFacing[] values = EnumFacing.values();
                  int length = values.length;
                  int j = 0;

                  while(true) {
                     if (j >= length) {
                        return;
                     }

                     BlockPos offset = down.func_177972_a(values[j]);
                     if (this.hasNeighbour(offset)) {
                        down = offset;
                        break;
                     }

                     ++j;
                  }
               }

               placeBlockScaffold(down);
               Wrapper.getPlayer().field_71071_by.field_70461_c = currentItem2;
            }
         }
      }

   }

   public static boolean canBeClicked(BlockPos blockPos) {
      return getBlock(blockPos).func_176209_a(getState(blockPos), false);
   }

   public static void faceVectorPacketInstant(Vec3d vec3d) {
      float[] neededRotations2 = getNeededRotations2(vec3d);
      Wrapper.getPlayer().field_71174_a.func_147297_a(new Rotation(neededRotations2[0], neededRotations2[1], Wrapper.getPlayer().field_70122_E));
   }

   private static PlayerControllerMP getPlayerController() {
      return Minecraft.func_71410_x().field_71442_b;
   }

   public static boolean placeBlockScaffold(BlockPos blockPos) {
      Vec3d vec3d = new Vec3d(Wrapper.getPlayer().field_70165_t, Wrapper.getPlayer().field_70163_u + (double)Wrapper.getPlayer().func_70047_e(), Wrapper.getPlayer().field_70161_v);
      EnumFacing[] var2 = EnumFacing.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing enumFacing = var2[var4];
         BlockPos offset = blockPos.func_177972_a(enumFacing);
         EnumFacing getOpposite = enumFacing.func_176734_d();
         if (vec3d.func_72436_e((new Vec3d(blockPos)).func_72441_c(0.5D, 0.5D, 0.5D)) < vec3d.func_72436_e((new Vec3d(offset)).func_72441_c(0.5D, 0.5D, 0.5D)) && canBeClicked(offset)) {
            Vec3d add = (new Vec3d(offset)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(getOpposite.func_176730_m())).func_186678_a(0.5D));
            if (vec3d.func_72436_e(add) <= 18.0625D) {
               faceVectorPacketInstant(add);
               processRightClickBlock(offset, getOpposite, add);
               Wrapper.getPlayer().func_184609_a(EnumHand.MAIN_HAND);
               mc.field_71467_ac = 4;
               return true;
            }
         }
      }

      return false;
   }

   public static void processRightClickBlock(BlockPos blockPos, EnumFacing enumFacing, Vec3d vec3d) {
      getPlayerController().func_187099_a(Wrapper.getPlayer(), mc.field_71441_e, blockPos, enumFacing, vec3d, EnumHand.MAIN_HAND);
   }

   public static IBlockState getState(BlockPos blockPos) {
      return Wrapper.getWorld().func_180495_p(blockPos);
   }

   private boolean hasNeighbour(BlockPos blockPos) {
      EnumFacing[] values = EnumFacing.values();
      int length = values.length;

      for(int i = 0; i < length; ++i) {
         if (!Wrapper.getWorld().func_180495_p(blockPos.func_177972_a(values[i])).func_185904_a().func_76222_j()) {
            return true;
         }
      }

      return false;
   }

   private static float[] getNeededRotations2(Vec3d vec3d) {
      Vec3d eyesPos = getEyesPos();
      double x = vec3d.field_72450_a - eyesPos.field_72450_a;
      double y = vec3d.field_72448_b - eyesPos.field_72448_b;
      double y2 = vec3d.field_72449_c - eyesPos.field_72449_c;
      return new float[]{Wrapper.getPlayer().field_70177_z + MathHelper.func_76142_g((float)Math.toDegrees(Math.atan2(y2, x)) - 90.0F - Wrapper.getPlayer().field_70177_z), Wrapper.getPlayer().field_70125_A + MathHelper.func_76142_g((float)(-Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + y2 * y2)))) - Wrapper.getPlayer().field_70125_A)};
   }

   public static Block getBlock(BlockPos blockPos) {
      return getState(blockPos).func_177230_c();
   }
}
