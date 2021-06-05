package com.pandora.api.util.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class MotionUtils {
   public static boolean isMoving(EntityLivingBase entity) {
      return entity.field_191988_bg != 0.0F || entity.field_70702_br != 0.0F;
   }

   public static void setSpeed(EntityLivingBase entity, double speed) {
      double[] dir = forward(speed);
      entity.field_70159_w = dir[0];
      entity.field_70179_y = dir[1];
   }

   public static double getBaseMoveSpeed() {
      double baseSpeed = 0.2873D;
      if (Minecraft.func_71410_x().field_71439_g != null && Minecraft.func_71410_x().field_71439_g.func_70644_a(Potion.func_188412_a(1))) {
         int amplifier = Minecraft.func_71410_x().field_71439_g.func_70660_b(Potion.func_188412_a(1)).func_76458_c();
         baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
      }

      return baseSpeed;
   }

   public static double[] forward(double speed) {
      float forward = Minecraft.func_71410_x().field_71439_g.field_71158_b.field_192832_b;
      float side = Minecraft.func_71410_x().field_71439_g.field_71158_b.field_78902_a;
      float yaw = Minecraft.func_71410_x().field_71439_g.field_70126_B + (Minecraft.func_71410_x().field_71439_g.field_70177_z - Minecraft.func_71410_x().field_71439_g.field_70126_B) * Minecraft.func_71410_x().func_184121_ak();
      if (forward != 0.0F) {
         if (side > 0.0F) {
            yaw += (float)(forward > 0.0F ? -45 : 45);
         } else if (side < 0.0F) {
            yaw += (float)(forward > 0.0F ? 45 : -45);
         }

         side = 0.0F;
         if (forward > 0.0F) {
            forward = 1.0F;
         } else if (forward < 0.0F) {
            forward = -1.0F;
         }
      }

      double sin = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double cos = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double posX = (double)forward * speed * cos + (double)side * speed * sin;
      double posZ = (double)forward * speed * sin - (double)side * speed * cos;
      return new double[]{posX, posZ};
   }
}
