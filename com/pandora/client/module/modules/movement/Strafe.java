package com.pandora.client.module.modules.movement;

import com.pandora.api.settings.Setting;
import com.pandora.client.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;

public class Strafe extends Module {
   int waitCounter;
   int forward = 1;
   Setting.Boolean jump;

   public Strafe() {
      super("Strafe", Module.Category.Movement);
   }

   public void setup() {
      this.jump = this.registerBoolean("Jump", "jump", true);
   }

   public void onUpdate() {
      boolean boost = Math.abs(mc.field_71439_g.field_70759_as - mc.field_71439_g.field_70177_z) < 90.0F;
      if (mc.field_71439_g.field_191988_bg != 0.0F) {
         if (!mc.field_71439_g.func_70051_ag()) {
            mc.field_71439_g.func_70031_b(true);
         }

         float yaw = mc.field_71439_g.field_70177_z;
         if (mc.field_71439_g.field_191988_bg > 0.0F) {
            if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F) {
               yaw += mc.field_71439_g.field_71158_b.field_78902_a > 0.0F ? -45.0F : 45.0F;
            }

            this.forward = 1;
            mc.field_71439_g.field_191988_bg = 1.0F;
            mc.field_71439_g.field_70702_br = 0.0F;
         } else if (mc.field_71439_g.field_191988_bg < 0.0F) {
            if (mc.field_71439_g.field_71158_b.field_78902_a != 0.0F) {
               yaw += mc.field_71439_g.field_71158_b.field_78902_a > 0.0F ? 45.0F : -45.0F;
            }

            this.forward = -1;
            mc.field_71439_g.field_191988_bg = -1.0F;
            mc.field_71439_g.field_70702_br = 0.0F;
         }

         if (mc.field_71439_g.field_70122_E) {
            mc.field_71439_g.func_70637_d(false);
            if (this.waitCounter < 1) {
               ++this.waitCounter;
               return;
            }

            this.waitCounter = 0;
            float f = (float)Math.toRadians((double)yaw);
            EntityPlayerSP var10000;
            if (this.jump.getValue()) {
               mc.field_71439_g.field_70181_x = 0.405D;
               var10000 = mc.field_71439_g;
               var10000.field_70159_w -= (double)(MathHelper.func_76126_a(f) * 0.2F) * (double)this.forward;
               var10000 = mc.field_71439_g;
               var10000.field_70179_y += (double)(MathHelper.func_76134_b(f) * 0.2F) * (double)this.forward;
            } else if (mc.field_71474_y.field_74314_A.func_151468_f()) {
               mc.field_71439_g.field_70181_x = 0.405D;
               var10000 = mc.field_71439_g;
               var10000.field_70159_w -= (double)(MathHelper.func_76126_a(f) * 0.2F) * (double)this.forward;
               var10000 = mc.field_71439_g;
               var10000.field_70179_y += (double)(MathHelper.func_76134_b(f) * 0.2F) * (double)this.forward;
            }
         } else {
            if (this.waitCounter < 1) {
               ++this.waitCounter;
               return;
            }

            this.waitCounter = 0;
            double currentSpeed = Math.sqrt(mc.field_71439_g.field_70159_w * mc.field_71439_g.field_70159_w + mc.field_71439_g.field_70179_y * mc.field_71439_g.field_70179_y);
            double speed = boost ? 1.0064D : 1.001D;
            if (mc.field_71439_g.field_70181_x < 0.0D) {
               speed = 1.0D;
            }

            double direction = Math.toRadians((double)yaw);
            mc.field_71439_g.field_70159_w = -Math.sin(direction) * speed * currentSpeed * (double)this.forward;
            mc.field_71439_g.field_70179_y = Math.cos(direction) * speed * currentSpeed * (double)this.forward;
         }
      }

   }
}
