package com.pandora.client.module.modules.movement;

import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class HoleTP extends Module {
   private int packets;
   private boolean jumped;
   private final double[] oneblockPositions = new double[]{0.42D, 0.75D};

   public HoleTP() {
      super("HoleTP", Module.Category.Movement);
   }

   public void onUpdate() {
      if (mc.field_71441_e != null && mc.field_71439_g != null && !ModuleManager.isModuleEnabled("Speed")) {
         if (!mc.field_71439_g.field_70122_E) {
            if (mc.field_71474_y.field_74314_A.func_151470_d()) {
               this.jumped = true;
            }
         } else {
            this.jumped = false;
         }

         if (!this.jumped && (double)mc.field_71439_g.field_70143_R < 0.5D && this.isInHole() && mc.field_71439_g.field_70163_u - this.getNearestBlockBelow() <= 1.125D && mc.field_71439_g.field_70163_u - this.getNearestBlockBelow() <= 0.95D && !this.isOnLiquid() && !this.isInLiquid()) {
            if (!mc.field_71439_g.field_70122_E) {
               ++this.packets;
            }

            if (!mc.field_71439_g.field_70122_E && !mc.field_71439_g.func_70055_a(Material.field_151586_h) && !mc.field_71439_g.func_70055_a(Material.field_151587_i) && !mc.field_71474_y.field_74314_A.func_151470_d() && !mc.field_71439_g.func_70617_f_() && this.packets > 0) {
               BlockPos blockPos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
               double[] var2 = this.oneblockPositions;
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  double position = var2[var4];
                  mc.field_71439_g.field_71174_a.func_147297_a(new Position((double)((float)blockPos.func_177958_n() + 0.5F), mc.field_71439_g.field_70163_u - position, (double)((float)blockPos.func_177952_p() + 0.5F), true));
               }

               mc.field_71439_g.func_70107_b((double)((float)blockPos.func_177958_n() + 0.5F), this.getNearestBlockBelow() + 0.1D, (double)((float)blockPos.func_177952_p() + 0.5F));
               this.packets = 0;
            }
         }

      }
   }

   private boolean isInHole() {
      BlockPos blockPos = new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v);
      IBlockState blockState = mc.field_71441_e.func_180495_p(blockPos);
      return this.isBlockValid(blockState, blockPos);
   }

   private double getNearestBlockBelow() {
      for(double y = mc.field_71439_g.field_70163_u; y > 0.0D; y -= 0.001D) {
         if (!(mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, y, mc.field_71439_g.field_70161_v)).func_177230_c() instanceof BlockSlab) && mc.field_71441_e.func_180495_p(new BlockPos(mc.field_71439_g.field_70165_t, y, mc.field_71439_g.field_70161_v)).func_177230_c().func_176223_P().func_185890_d(mc.field_71441_e, new BlockPos(0, 0, 0)) != null) {
            return y;
         }
      }

      return -1.0D;
   }

   private boolean isBlockValid(IBlockState blockState, BlockPos blockPos) {
      return blockState.func_177230_c() == Blocks.field_150350_a && mc.field_71439_g.func_174818_b(blockPos) >= 1.0D && mc.field_71441_e.func_180495_p(blockPos.func_177984_a()).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2)).func_177230_c() == Blocks.field_150350_a && (this.isBedrockHole(blockPos) || this.isObbyHole(blockPos) || this.isBothHole(blockPos) || this.isElseHole(blockPos));
   }

   private boolean isObbyHole(BlockPos blockPos) {
      BlockPos[] array = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
      BlockPos[] var4 = array;
      int var5 = array.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockPos touching = var4[var6];
         IBlockState touchingState = mc.field_71441_e.func_180495_p(touching);
         if (touchingState.func_177230_c() == Blocks.field_150350_a || touchingState.func_177230_c() != Blocks.field_150343_Z) {
            return false;
         }
      }

      return true;
   }

   private boolean isBedrockHole(BlockPos blockPos) {
      BlockPos[] array = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
      BlockPos[] var4 = array;
      int var5 = array.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockPos touching = var4[var6];
         IBlockState touchingState = mc.field_71441_e.func_180495_p(touching);
         if (touchingState.func_177230_c() == Blocks.field_150350_a || touchingState.func_177230_c() != Blocks.field_150357_h) {
            return false;
         }
      }

      return true;
   }

   private boolean isBothHole(BlockPos blockPos) {
      BlockPos[] array = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
      BlockPos[] var4 = array;
      int var5 = array.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockPos touching = var4[var6];
         IBlockState touchingState = mc.field_71441_e.func_180495_p(touching);
         if (touchingState.func_177230_c() == Blocks.field_150350_a || touchingState.func_177230_c() != Blocks.field_150357_h && touchingState.func_177230_c() != Blocks.field_150343_Z) {
            return false;
         }
      }

      return true;
   }

   private boolean isElseHole(BlockPos blockPos) {
      BlockPos[] array = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
      BlockPos[] var4 = array;
      int var5 = array.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         BlockPos touching = var4[var6];
         IBlockState touchingState = mc.field_71441_e.func_180495_p(touching);
         if (touchingState.func_177230_c() == Blocks.field_150350_a || !touchingState.func_185913_b()) {
            return false;
         }
      }

      return true;
   }

   private boolean isOnLiquid() {
      double y = mc.field_71439_g.field_70163_u - 0.03D;

      for(int x = MathHelper.func_76128_c(mc.field_71439_g.field_70165_t); x < MathHelper.func_76143_f(mc.field_71439_g.field_70165_t); ++x) {
         for(int z = MathHelper.func_76128_c(mc.field_71439_g.field_70161_v); z < MathHelper.func_76143_f(mc.field_71439_g.field_70161_v); ++z) {
            BlockPos pos = new BlockPos(x, MathHelper.func_76128_c(y), z);
            if (mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean isInLiquid() {
      double y = mc.field_71439_g.field_70163_u + 0.01D;

      for(int x = MathHelper.func_76128_c(mc.field_71439_g.field_70165_t); x < MathHelper.func_76143_f(mc.field_71439_g.field_70165_t); ++x) {
         for(int z = MathHelper.func_76128_c(mc.field_71439_g.field_70161_v); z < MathHelper.func_76143_f(mc.field_71439_g.field_70161_v); ++z) {
            BlockPos pos = new BlockPos(x, (int)y, z);
            if (mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid) {
               return true;
            }
         }
      }

      return false;
   }
}
