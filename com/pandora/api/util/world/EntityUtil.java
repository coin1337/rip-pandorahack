package com.pandora.api.util.world;

import com.google.gson.JsonParser;
import com.pandora.api.util.misc.Wrapper;
import java.io.IOException;
import java.net.URL;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.io.IOUtils;

public class EntityUtil {
   private static final Minecraft mc = Minecraft.func_71410_x();

   public static boolean isPassive(Entity e) {
      if (e instanceof EntityWolf && ((EntityWolf)e).func_70919_bu()) {
         return false;
      } else if (!(e instanceof EntityAnimal) && !(e instanceof EntityAgeable) && !(e instanceof EntityTameable) && !(e instanceof EntityAmbientCreature) && !(e instanceof EntitySquid)) {
         return e instanceof EntityIronGolem && ((EntityIronGolem)e).func_70643_av() == null;
      } else {
         return true;
      }
   }

   public static boolean isLiving(Entity e) {
      return e instanceof EntityLivingBase;
   }

   public static boolean isFakeLocalPlayer(Entity entity) {
      return entity != null && entity.func_145782_y() == -100 && Wrapper.getPlayer() != entity;
   }

   public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
      return new Vec3d((entity.field_70165_t - entity.field_70142_S) * x, (entity.field_70163_u - entity.field_70137_T) * y, (entity.field_70161_v - entity.field_70136_U) * z);
   }

   public static String getNameFromUUID(String uuid) {
      try {
         String jsonUrl = IOUtils.toString(new URL("https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names"));
         JsonParser parser = new JsonParser();
         return parser.parse(jsonUrl).getAsJsonArray().get(parser.parse(jsonUrl).getAsJsonArray().size() - 1).getAsJsonObject().get("name").toString();
      } catch (IOException var3) {
         return null;
      }
   }

   public static Block isColliding(double posX, double posY, double posZ) {
      Block block = null;
      if (mc.field_71439_g != null) {
         AxisAlignedBB bb = mc.field_71439_g.func_184187_bx() != null ? mc.field_71439_g.func_184187_bx().func_174813_aQ().func_191195_a(0.0D, 0.0D, 0.0D).func_72317_d(posX, posY, posZ) : mc.field_71439_g.func_174813_aQ().func_191195_a(0.0D, 0.0D, 0.0D).func_72317_d(posX, posY, posZ);
         int y = (int)bb.field_72338_b;

         for(int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d) + 1; ++x) {
            for(int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f) + 1; ++z) {
               block = mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
            }
         }
      }

      return block;
   }

   public static double getBaseMoveSpeed() {
      double baseSpeed = 0.2873D;
      if (mc.field_71439_g != null && mc.field_71439_g.func_70644_a(Potion.func_188412_a(1))) {
         int amplifier = mc.field_71439_g.func_70660_b(Potion.func_188412_a(1)).func_76458_c();
         baseSpeed *= 1.0D + 0.2D * (double)(amplifier + 1);
      }

      return baseSpeed;
   }

   public static boolean isInLiquid() {
      if (mc.field_71439_g == null) {
         return false;
      } else if (mc.field_71439_g.field_70143_R >= 3.0F) {
         return false;
      } else {
         boolean inLiquid = false;
         AxisAlignedBB bb = mc.field_71439_g.func_184187_bx() != null ? mc.field_71439_g.func_184187_bx().func_174813_aQ() : mc.field_71439_g.func_174813_aQ();
         int y = (int)bb.field_72338_b;

         for(int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d) + 1; ++x) {
            for(int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f) + 1; ++z) {
               Block block = mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
               if (!(block instanceof BlockAir)) {
                  if (!(block instanceof BlockLiquid)) {
                     return false;
                  }

                  inLiquid = true;
               }
            }
         }

         return inLiquid;
      }
   }

   public static void setTimer(float speed) {
      Minecraft.func_71410_x().field_71428_T.field_194149_e = 50.0F / speed;
   }

   public static void resetTimer() {
      Minecraft.func_71410_x().field_71428_T.field_194149_e = 50.0F;
   }

   public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
      return getInterpolatedAmount(entity, vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
   }

   public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
      return getInterpolatedAmount(entity, ticks, ticks, ticks);
   }

   public static boolean isMobAggressive(Entity entity) {
      if (entity instanceof EntityPigZombie) {
         if (((EntityPigZombie)entity).func_184734_db() || ((EntityPigZombie)entity).func_175457_ck()) {
            return true;
         }
      } else {
         if (entity instanceof EntityWolf) {
            return ((EntityWolf)entity).func_70919_bu() && !Wrapper.getPlayer().equals(((EntityWolf)entity).func_70902_q());
         }

         if (entity instanceof EntityEnderman) {
            return ((EntityEnderman)entity).func_70823_r();
         }
      }

      return isHostileMob(entity);
   }

   public static boolean isNeutralMob(Entity entity) {
      return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
   }

   public static boolean isFriendlyMob(Entity entity) {
      return entity.isCreatureType(EnumCreatureType.CREATURE, false) && !isNeutralMob(entity) || entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof EntityVillager || entity instanceof EntityIronGolem || isNeutralMob(entity) && !isMobAggressive(entity);
   }

   public static boolean isHostileMob(Entity entity) {
      return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity);
   }

   public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
      return (new Vec3d(entity.field_70142_S, entity.field_70137_T, entity.field_70136_U)).func_178787_e(getInterpolatedAmount(entity, (double)ticks));
   }

   public static Vec3d getInterpolatedRenderPos(Entity entity, float ticks) {
      return getInterpolatedPos(entity, ticks).func_178786_a(Wrapper.getMinecraft().func_175598_ae().field_78725_b, Wrapper.getMinecraft().func_175598_ae().field_78726_c, Wrapper.getMinecraft().func_175598_ae().field_78723_d);
   }

   public static boolean isInWater(Entity entity) {
      if (entity == null) {
         return false;
      } else {
         double y = entity.field_70163_u + 0.01D;

         for(int x = MathHelper.func_76128_c(entity.field_70165_t); x < MathHelper.func_76143_f(entity.field_70165_t); ++x) {
            for(int z = MathHelper.func_76128_c(entity.field_70161_v); z < MathHelper.func_76143_f(entity.field_70161_v); ++z) {
               BlockPos pos = new BlockPos(x, (int)y, z);
               if (Wrapper.getWorld().func_180495_p(pos).func_177230_c() instanceof BlockLiquid) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static boolean isDrivenByPlayer(Entity entityIn) {
      return Wrapper.getPlayer() != null && entityIn != null && entityIn.equals(Wrapper.getPlayer().func_184187_bx());
   }

   public static boolean isAboveWater(Entity entity) {
      return isAboveWater(entity, false);
   }

   public static boolean isAboveWater(Entity entity, boolean packet) {
      if (entity == null) {
         return false;
      } else {
         double y = entity.field_70163_u - (packet ? 0.03D : (isPlayer(entity) ? 0.2D : 0.5D));

         for(int x = MathHelper.func_76128_c(entity.field_70165_t); x < MathHelper.func_76143_f(entity.field_70165_t); ++x) {
            for(int z = MathHelper.func_76128_c(entity.field_70161_v); z < MathHelper.func_76143_f(entity.field_70161_v); ++z) {
               BlockPos pos = new BlockPos(x, MathHelper.func_76128_c(y), z);
               if (Wrapper.getWorld().func_180495_p(pos).func_177230_c() instanceof BlockLiquid) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
      double dirx = me.field_70165_t - px;
      double diry = me.field_70163_u - py;
      double dirz = me.field_70161_v - pz;
      double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
      dirx /= len;
      diry /= len;
      dirz /= len;
      double pitch = Math.asin(diry);
      double yaw = Math.atan2(dirz, dirx);
      pitch = pitch * 180.0D / 3.141592653589793D;
      yaw = yaw * 180.0D / 3.141592653589793D;
      yaw += 90.0D;
      return new double[]{yaw, pitch};
   }

   public static boolean isPlayer(Entity entity) {
      return entity instanceof EntityPlayer;
   }

   public static double getRelativeX(float yaw) {
      return (double)MathHelper.func_76126_a(-yaw * 0.017453292F);
   }

   public static double getRelativeZ(float yaw) {
      return (double)MathHelper.func_76134_b(yaw * 0.017453292F);
   }

   public static float clamp(float val, float min, float max) {
      if (val <= min) {
         val = min;
      }

      if (val >= max) {
         val = max;
      }

      return val;
   }
}
