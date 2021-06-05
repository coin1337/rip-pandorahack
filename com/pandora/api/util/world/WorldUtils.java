package com.pandora.api.util.world;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public class WorldUtils {
   public static void rotate(float yaw, float pitch) {
      Minecraft.func_71410_x().field_71439_g.field_70177_z = yaw;
      Minecraft.func_71410_x().field_71439_g.field_70125_A = pitch;
   }

   public static void rotate(double[] rotations) {
      Minecraft.func_71410_x().field_71439_g.field_70177_z = (float)rotations[0];
      Minecraft.func_71410_x().field_71439_g.field_70125_A = (float)rotations[1];
   }

   public static String vectorToString(Vec3d vector, boolean... includeY) {
      boolean reallyIncludeY = includeY.length <= 0 || includeY[0];
      StringBuilder builder = new StringBuilder();
      builder.append('(');
      builder.append((int)Math.floor(vector.field_72450_a));
      builder.append(", ");
      if (reallyIncludeY) {
         builder.append((int)Math.floor(vector.field_72448_b));
         builder.append(", ");
      }

      builder.append((int)Math.floor(vector.field_72449_c));
      builder.append(")");
      return builder.toString();
   }
}
