package com.pandora.client.module.modules.movement;

import com.pandora.api.settings.Setting;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber({Side.CLIENT})
public class LongJump extends Module {
   private static boolean jumped = false;
   private static boolean boostable = false;
   Setting.Integer speed;
   Setting.Boolean packet;
   Setting.Boolean test;

   public LongJump() {
      super("LongJump", Module.Category.Movement);
   }

   public void setup() {
      this.speed = this.registerInteger("Speed", "Speed", 30, 1, 100);
      this.packet = this.registerBoolean("Packet", "Packet", true);
      this.test = this.registerBoolean("Test", "test", true);
   }

   public void onUpdate() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         if (jumped) {
            if (mc.field_71439_g.field_70122_E || mc.field_71439_g.field_71075_bZ.field_75100_b) {
               jumped = false;
               mc.field_71439_g.field_70159_w = 0.0D;
               mc.field_71439_g.field_70179_y = 0.0D;
               if (this.packet.getValue()) {
                  mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
                  mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t + mc.field_71439_g.field_70159_w, 0.0D, mc.field_71439_g.field_70161_v + mc.field_71439_g.field_70179_y, mc.field_71439_g.field_70122_E));
               }

               this.disable();
               return;
            }

            if (mc.field_71439_g.field_71158_b.field_192832_b == 0.0F && mc.field_71439_g.field_71158_b.field_78902_a == 0.0F) {
               return;
            }

            double yaw = this.getDirection();
            mc.field_71439_g.field_70159_w = -Math.sin(yaw) * (double)((float)Math.sqrt(mc.field_71439_g.field_70159_w * mc.field_71439_g.field_70159_w + mc.field_71439_g.field_70179_y * mc.field_71439_g.field_70179_y) * (boostable ? (float)this.speed.getValue() / 10.0F : 1.0F));
            mc.field_71439_g.field_70179_y = Math.cos(yaw) * (double)((float)Math.sqrt(mc.field_71439_g.field_70159_w * mc.field_71439_g.field_70159_w + mc.field_71439_g.field_70179_y * mc.field_71439_g.field_70179_y) * (boostable ? (float)this.speed.getValue() / 10.0F : 1.0F));
            if (this.test.getValue()) {
               (new Thread(() -> {
                  try {
                     Thread.sleep(200L);
                     mc.field_71439_g.field_70125_A = -9.5F;
                     mc.field_71439_g.func_184598_c(EnumHand.MAIN_HAND);

                     while(true) {
                        if (mc.field_71439_g.func_184612_cw() >= 3) {
                           mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, mc.field_71439_g.func_174811_aO()));
                           mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItem(mc.field_71439_g.func_184600_cs()));
                           mc.field_71439_g.func_184597_cx();
                           break;
                        }
                     }
                  } catch (InterruptedException var1) {
                     var1.printStackTrace();
                  }

               })).start();
            }

            boostable = false;
            this.disable();
         }

         if (mc.field_71439_g.field_71158_b.field_192832_b == 0.0F && mc.field_71439_g.field_71158_b.field_78902_a == 0.0F && jumped) {
            mc.field_71439_g.field_70159_w = 0.0D;
            mc.field_71439_g.field_70179_y = 0.0D;
         }

      }
   }

   @SubscribeEvent
   public static void onJump(LivingJumpEvent event) {
      if (ModuleManager.getModuleByName("LongJump").isEnabled()) {
         if (mc.field_71439_g != null && mc.field_71441_e != null && event.getEntity() == mc.field_71439_g && (mc.field_71439_g.field_71158_b.field_192832_b != 0.0F || mc.field_71439_g.field_71158_b.field_78902_a != 0.0F)) {
            jumped = true;
            boostable = true;
         }

      }
   }

   private double getDirection() {
      float rotationYaw = mc.field_71439_g.field_70177_z;
      if (mc.field_71439_g.field_191988_bg < 0.0F) {
         rotationYaw += 180.0F;
      }

      float forward = 1.0F;
      if (mc.field_71439_g.field_191988_bg < 0.0F) {
         forward = -0.5F;
      } else if (mc.field_71439_g.field_191988_bg > 0.0F) {
         forward = 0.5F;
      }

      if (mc.field_71439_g.field_70702_br > 0.0F) {
         rotationYaw -= 90.0F * forward;
      }

      if (mc.field_71439_g.field_70702_br < 0.0F) {
         rotationYaw += 90.0F * forward;
      }

      return Math.toRadians((double)rotationYaw);
   }
}
