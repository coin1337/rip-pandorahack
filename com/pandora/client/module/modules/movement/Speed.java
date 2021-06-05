package com.pandora.client.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.api.event.events.PlayerMoveEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.world.EntityUtil;
import com.pandora.api.util.world.MotionUtils;
import com.pandora.api.util.world.Timer;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.ArrayList;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;

public class Speed extends Module {
   Setting.Boolean iceSpeed;
   Setting.Boolean timerBool;
   Setting.Double timerVal;
   Setting.Double jumpHeight;
   Setting.Mode mode;
   private boolean slowDown;
   private boolean isOnIce = false;
   private double playerSpeed;
   private Timer timer = new Timer();
   @EventHandler
   private final Listener<PlayerMoveEvent> playerMoveEventListener = new Listener((event) -> {
      if (!this.isOnIce) {
         if (mc.field_71439_g.func_180799_ab() || mc.field_71439_g.func_70090_H() || mc.field_71439_g.func_70617_f_() || mc.field_71439_g.field_70134_J) {
            return;
         }

         if (this.mode.getValue().equalsIgnoreCase("Strafe")) {
            double speedY = this.jumpHeight.getValue();
            if (mc.field_71439_g.field_70122_E && MotionUtils.isMoving(mc.field_71439_g) && this.timer.hasReached(300L)) {
               EntityUtil.setTimer((float)this.timerVal.getValue());
               if (mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
                  speedY += (double)((float)(mc.field_71439_g.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1F);
               }

               event.setY(mc.field_71439_g.field_70181_x = speedY);
               this.playerSpeed = MotionUtils.getBaseMoveSpeed() * (EntityUtil.isColliding(0.0D, -0.5D, 0.0D) instanceof BlockLiquid && !EntityUtil.isInLiquid() ? 0.9D : 1.901D);
               this.slowDown = true;
               this.timer.reset();
            } else {
               EntityUtil.resetTimer();
               if (!this.slowDown && !mc.field_71439_g.field_70123_F) {
                  this.playerSpeed -= this.playerSpeed / 159.0D;
               } else {
                  this.playerSpeed -= EntityUtil.isColliding(0.0D, -0.8D, 0.0D) instanceof BlockLiquid && !EntityUtil.isInLiquid() ? 0.4D : 0.7D * (this.playerSpeed = MotionUtils.getBaseMoveSpeed());
                  this.slowDown = false;
               }
            }

            this.playerSpeed = Math.max(this.playerSpeed, MotionUtils.getBaseMoveSpeed());
            double[] dir = MotionUtils.forward(this.playerSpeed);
            event.setX(dir[0]);
            event.setZ(dir[1]);
         }
      }

   }, new Predicate[0]);

   public Speed() {
      super("Speed", Module.Category.Movement);
   }

   public void setup() {
      ArrayList<String> modes = new ArrayList();
      modes.add("Strafe");
      modes.add("Fake");
      modes.add("YPort");
      this.mode = this.registerMode("Mode", "Mode", modes, "Strafe");
      this.jumpHeight = this.registerDouble("Jump Speed", "JumpSpeed", 0.41D, 0.0D, 1.0D);
      this.timerBool = this.registerBoolean("Timer", "Timer", true);
      this.timerVal = this.registerDouble("Timer Speed", "TimerSpeed", 1.15D, 1.0D, 1.5D);
      this.iceSpeed = this.registerBoolean("Ice", "Ice", true);
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
      this.playerSpeed = MotionUtils.getBaseMoveSpeed();
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
      this.timer.reset();
      EntityUtil.resetTimer();
   }

   public void onUpdate() {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         BlockPos blockPos = (new BlockPos(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u, mc.field_71439_g.field_70161_v)).func_177977_b();
         if (this.iceSpeed.getValue() && (mc.field_71441_e.func_180495_p(blockPos).func_177230_c() instanceof BlockIce || mc.field_71441_e.func_180495_p(blockPos).func_177230_c() instanceof BlockPackedIce)) {
            this.isOnIce = true;
            MotionUtils.setSpeed(mc.field_71439_g, MotionUtils.getBaseMoveSpeed() + (mc.field_71439_g.func_70644_a(MobEffects.field_76424_c) ? (mc.field_71439_g.field_70173_aa % 2 == 0 ? 0.7D : 0.1D) : 0.4D));
         } else if (this.mode.getValue().equalsIgnoreCase("YPort")) {
            this.handleYPortSpeed();
         }

      } else {
         this.disable();
      }
   }

   private void handleYPortSpeed() {
      if (MotionUtils.isMoving(mc.field_71439_g) && (!mc.field_71439_g.func_70090_H() || !mc.field_71439_g.func_180799_ab()) && !mc.field_71439_g.field_70123_F) {
         if (mc.field_71439_g.field_70122_E) {
            EntityUtil.setTimer(1.15F);
            mc.field_71439_g.func_70664_aZ();
            MotionUtils.setSpeed(mc.field_71439_g, MotionUtils.getBaseMoveSpeed() + (this.isOnIce ? 0.3D : 0.06D));
         } else {
            mc.field_71439_g.field_70181_x = -1.0D;
            EntityUtil.resetTimer();
         }

      }
   }

   public String getHudInfo() {
      String t = "";
      if (this.mode.getValue().equalsIgnoreCase("Strafe")) {
         t = "[" + ChatFormatting.WHITE + "Strafe" + ChatFormatting.GRAY + "]";
      } else if (this.mode.getValue().equalsIgnoreCase("YPort")) {
         t = "[" + ChatFormatting.WHITE + "YPort" + ChatFormatting.GRAY + "]";
      } else if (this.mode.getValue().equalsIgnoreCase("Fake")) {
         t = "[" + ChatFormatting.WHITE + "Fake" + ChatFormatting.GRAY + "]";
      }

      return t;
   }
}
