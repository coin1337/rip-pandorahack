package com.pandora.client.module.modules.render;

import com.pandora.api.event.events.TransformSideFirstPersonEvent;
import com.pandora.api.settings.Setting;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ViewModel extends Module {
   public Setting.Boolean cancelEating;
   Setting.Double xRight;
   Setting.Double yRight;
   Setting.Double zRight;
   Setting.Double xLeft;
   Setting.Double yLeft;
   Setting.Double zLeft;
   @EventHandler
   private final Listener<TransformSideFirstPersonEvent> eventListener = new Listener((event) -> {
      if (event.getHandSide() == EnumHandSide.RIGHT) {
         GlStateManager.func_179137_b(this.xRight.getValue(), this.yRight.getValue(), this.zRight.getValue());
      } else if (event.getHandSide() == EnumHandSide.LEFT) {
         GlStateManager.func_179137_b(this.xLeft.getValue(), this.yLeft.getValue(), this.zLeft.getValue());
      }

   }, new Predicate[0]);

   public ViewModel() {
      super("ViewModel", Module.Category.Render);
   }

   public void setup() {
      this.cancelEating = this.registerBoolean("No Eat", "NoEat", false);
      this.xLeft = this.registerDouble("Left X", "LeftX", 0.0D, -2.0D, 2.0D);
      this.yLeft = this.registerDouble("Left Y", "LeftY", 0.2D, -2.0D, 2.0D);
      this.zLeft = this.registerDouble("Left Z", "LeftZ", -1.2D, -2.0D, 2.0D);
      this.xRight = this.registerDouble("Right X", "RightX", 0.0D, -2.0D, 2.0D);
      this.yRight = this.registerDouble("Right Y", "RightY", 0.2D, -2.0D, 2.0D);
      this.zRight = this.registerDouble("Right Z", "RightZ", -1.2D, -2.0D, 2.0D);
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
