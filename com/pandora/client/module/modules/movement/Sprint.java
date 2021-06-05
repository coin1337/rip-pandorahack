package com.pandora.client.module.modules.movement;

import com.pandora.api.event.events.JumpEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.world.MotionUtils;
import com.pandora.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class Sprint extends Module {
   Setting.Boolean reverseSprint;
   @EventHandler
   private final Listener<JumpEvent> jumpEventListener = new Listener((event) -> {
      if (this.reverseSprint.getValue()) {
         double[] direction = MotionUtils.forward(0.01745329238474369D);
         event.getLocation().setX(direction[0] * 0.20000000298023224D);
         event.getLocation().setZ(direction[1] * 0.20000000298023224D);
      }

   }, new Predicate[0]);

   public Sprint() {
      super("Sprint", Module.Category.Movement);
   }

   public void setup() {
      this.reverseSprint = this.registerBoolean("Reverse", "Reverse", false);
   }

   public void onUpdate() {
      if (mc.field_71439_g != null) {
         if (mc.field_71474_y.field_74311_E.func_151470_d()) {
            mc.field_71439_g.func_70031_b(false);
         } else {
            if (mc.field_71439_g.func_71024_bL().func_75116_a() > 6 && this.reverseSprint.getValue()) {
               if (mc.field_71439_g.field_191988_bg == 0.0F && mc.field_71439_g.field_70702_br == 0.0F) {
                  return;
               }
            } else if (!(mc.field_71439_g.field_191988_bg > 0.0F)) {
               return;
            }

            mc.field_71439_g.func_70031_b(true);
         }
      }
   }
}
