package com.pandora.api.util.world;

import com.pandora.api.util.misc.Wrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BlockUtils {
   public static final List blackList;
   public static final List shulkerList;
   static Minecraft mc = Minecraft.func_71410_x();

   public static IBlockState getState(BlockPos pos) {
      return mc.field_71441_e.func_180495_p(pos);
   }

   public static boolean checkForNeighbours(BlockPos blockPos) {
      if (!hasNeighbour(blockPos)) {
         EnumFacing[] var1 = EnumFacing.values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            EnumFacing side = var1[var3];
            BlockPos neighbour = blockPos.func_177972_a(side);
            if (hasNeighbour(neighbour)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private static boolean hasNeighbour(BlockPos blockPos) {
      EnumFacing[] var1 = EnumFacing.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumFacing side = var1[var3];
         BlockPos neighbour = blockPos.func_177972_a(side);
         if (!Wrapper.getWorld().func_180495_p(neighbour).func_185904_a().func_76222_j()) {
            return true;
         }
      }

      return false;
   }

   public static Block getBlock(BlockPos pos) {
      return getState(pos).func_177230_c();
   }

   public static boolean canBeClicked(BlockPos pos) {
      return getBlock(pos).func_176209_a(getState(pos), false);
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

   public static List<BlockPos> getCircle(BlockPos loc, int y, float r, boolean hollow) {
      List<BlockPos> circleblocks = new ArrayList();
      int cx = loc.func_177958_n();
      int cz = loc.func_177952_p();

      for(int x = cx - (int)r; (float)x <= (float)cx + r; ++x) {
         for(int z = cz - (int)r; (float)z <= (float)cz + r; ++z) {
            double dist = (double)((cx - x) * (cx - x) + (cz - z) * (cz - z));
            if (dist < (double)(r * r) && (!hollow || dist >= (double)((r - 1.0F) * (r - 1.0F)))) {
               BlockPos l = new BlockPos(x, y, z);
               circleblocks.add(l);
            }
         }
      }

      return circleblocks;
   }

   public static EnumFacing getPlaceableSide(BlockPos pos) {
      EnumFacing[] var1 = EnumFacing.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumFacing side = var1[var3];
         BlockPos neighbour = pos.func_177972_a(side);
         if (mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(mc.field_71441_e.func_180495_p(neighbour), false)) {
            IBlockState blockState = mc.field_71441_e.func_180495_p(neighbour);
            if (!blockState.func_185904_a().func_76222_j()) {
               return side;
            }
         }
      }

      return null;
   }

   static {
      blackList = Arrays.asList(Blocks.field_150477_bB, Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150462_ai, Blocks.field_150467_bQ, Blocks.field_150382_bo, Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z);
      shulkerList = Arrays.asList(Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA);
      mc = Minecraft.func_71410_x();
   }
}
