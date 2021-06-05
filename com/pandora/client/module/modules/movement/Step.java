package com.pandora.client.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.world.EntityUtil;
import com.pandora.api.util.world.MotionUtils;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import java.text.DecimalFormat;
import java.util.ArrayList;
import net.minecraft.network.play.client.CPacketPlayer.Position;

public class Step extends Module {
   private int ticks = 0;
   Setting.Double height;
   Setting.Boolean timer;
   Setting.Boolean reverse;
   Setting.Mode mode;

   public Step() {
      super("Step", Module.Category.Movement);
   }

   public void setup() {
      ArrayList<String> modes = new ArrayList();
      modes.add("Normal");
      modes.add("Vanilla");
      this.height = this.registerDouble("Height", "Height", 2.5D, 0.5D, 2.5D);
      this.timer = this.registerBoolean("Timer", "Timer", false);
      this.reverse = this.registerBoolean("Reverse", "Reverse", false);
      this.mode = this.registerMode("Modes", "Modes", modes, "Normal");
   }

   public void onUpdate() {
      if (mc.field_71441_e != null && mc.field_71439_g != null && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_180799_ab() && !mc.field_71439_g.func_70617_f_() && !mc.field_71474_y.field_74314_A.func_151470_d()) {
         if (!ModuleManager.isModuleEnabled("Speed")) {
            if (this.mode.getValue().equalsIgnoreCase("Normal")) {
               if (this.timer.getValue()) {
                  if (this.ticks == 0) {
                     EntityUtil.resetTimer();
                  } else {
                     --this.ticks;
                  }
               }

               if (mc.field_71439_g != null && mc.field_71439_g.field_70122_E && !mc.field_71439_g.func_70090_H() && !mc.field_71439_g.func_70617_f_() && this.reverse.getValue()) {
                  for(double y = 0.0D; y < this.height.getValue() + 0.5D; y += 0.01D) {
                     if (!mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(0.0D, -y, 0.0D)).isEmpty()) {
                        mc.field_71439_g.field_70181_x = -10.0D;
                        break;
                     }
                  }
               }

               double[] dir = MotionUtils.forward(0.1D);
               boolean twofive = false;
               boolean two = false;
               boolean onefive = false;
               boolean one = false;
               if (mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.6D, dir[1])).isEmpty() && !mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.4D, dir[1])).isEmpty()) {
                  twofive = true;
               }

               if (mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.1D, dir[1])).isEmpty() && !mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.9D, dir[1])).isEmpty()) {
                  two = true;
               }

               if (mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.6D, dir[1])).isEmpty() && !mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.4D, dir[1])).isEmpty()) {
                  onefive = true;
               }

               if (mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.0D, dir[1])).isEmpty() && !mc.field_71441_e.func_184144_a(mc.field_71439_g, mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 0.6D, dir[1])).isEmpty()) {
                  one = true;
               }

               if (mc.field_71439_g.field_70123_F && (mc.field_71439_g.field_191988_bg != 0.0F || mc.field_71439_g.field_70702_br != 0.0F) && mc.field_71439_g.field_70122_E) {
                  double[] twoFiveOffset;
                  int i;
                  if (one && this.height.getValue() >= 1.0D) {
                     twoFiveOffset = new double[]{0.42D, 0.753D};

                     for(i = 0; i < twoFiveOffset.length; ++i) {
                        mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + twoFiveOffset[i], mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
                     }

                     if (this.timer.getValue()) {
                        EntityUtil.setTimer(0.6F);
                     }

                     mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.0D, mc.field_71439_g.field_70161_v);
                     this.ticks = 1;
                  }

                  if (onefive && this.height.getValue() >= 1.5D) {
                     twoFiveOffset = new double[]{0.42D, 0.75D, 1.0D, 1.16D, 1.23D, 1.2D};

                     for(i = 0; i < twoFiveOffset.length; ++i) {
                        mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + twoFiveOffset[i], mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
                     }

                     if (this.timer.getValue()) {
                        EntityUtil.setTimer(0.35F);
                     }

                     mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 1.5D, mc.field_71439_g.field_70161_v);
                     this.ticks = 1;
                  }

                  if (two && this.height.getValue() >= 2.0D) {
                     twoFiveOffset = new double[]{0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D};

                     for(i = 0; i < twoFiveOffset.length; ++i) {
                        mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + twoFiveOffset[i], mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
                     }

                     if (this.timer.getValue()) {
                        EntityUtil.setTimer(0.25F);
                     }

                     mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 2.0D, mc.field_71439_g.field_70161_v);
                     this.ticks = 2;
                  }

                  if (twofive && this.height.getValue() >= 2.5D) {
                     twoFiveOffset = new double[]{0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D};

                     for(i = 0; i < twoFiveOffset.length; ++i) {
                        mc.field_71439_g.field_71174_a.func_147297_a(new Position(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + twoFiveOffset[i], mc.field_71439_g.field_70161_v, mc.field_71439_g.field_70122_E));
                     }

                     if (this.timer.getValue()) {
                        EntityUtil.setTimer(0.15F);
                     }

                     mc.field_71439_g.func_70107_b(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + 2.5D, mc.field_71439_g.field_70161_v);
                     this.ticks = 2;
                  }
               }
            }

            if (this.mode.getValue().equalsIgnoreCase("Vanilla")) {
               DecimalFormat df = new DecimalFormat("#");
               mc.field_71439_g.field_70138_W = Float.parseFloat(df.format(this.height.getValue()));
            }

         }
      }
   }

   public void onDisable() {
      mc.field_71439_g.field_70138_W = 0.5F;
   }

   public String getHudInfo() {
      String t = "";
      if (this.mode.getValue().equalsIgnoreCase("Normal")) {
         t = "[" + ChatFormatting.WHITE + "Normal" + ChatFormatting.GRAY + "]";
      }

      if (this.mode.getValue().equalsIgnoreCase("Vanilla")) {
         t = "[" + ChatFormatting.WHITE + "Vanilla" + ChatFormatting.GRAY + "]";
      }

      return t;
   }
}
