package com.pandora.client.module.modules.render;

import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.players.enemy.Enemies;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.api.util.render.PandoraTessellator;
import com.pandora.client.module.Module;
import com.pandora.client.module.modules.gui.ColorMain;
import java.util.ArrayList;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class Tracers extends Module {
   Setting.Integer renderDistance;
   Setting.Mode pointsTo;
   Setting.ColorSetting nearColor;
   Setting.ColorSetting midColor;
   Setting.ColorSetting farColor;
   PandoraColor tracerColor;

   public Tracers() {
      super("Tracers", Module.Category.Render);
   }

   public void setup() {
      this.renderDistance = this.registerInteger("Distance", "Distance", 100, 10, 260);
      ArrayList<String> link = new ArrayList();
      link.add("Head");
      link.add("Feet");
      this.pointsTo = this.registerMode("Draw To", "DrawTo", link, "Feet");
      this.nearColor = this.registerColor("Near Color", "NearColor", new PandoraColor(255, 0, 0, 255));
      this.midColor = this.registerColor("Middle Color", "MidColor", new PandoraColor(255, 255, 0, 255));
      this.farColor = this.registerColor("Far Color", "FarColor", new PandoraColor(0, 255, 0, 255));
   }

   public void onWorldRender(RenderEvent event) {
      mc.field_71441_e.field_72996_f.stream().filter((e) -> {
         return e instanceof EntityPlayer;
      }).filter((e) -> {
         return e != mc.field_71439_g;
      }).forEach((e) -> {
         if (!(mc.field_71439_g.func_70032_d(e) > (float)this.renderDistance.getValue())) {
            if (Friends.isFriend(e.func_70005_c_())) {
               this.tracerColor = ColorMain.getFriendGSColor();
            } else if (Enemies.isEnemy(e.func_70005_c_())) {
               this.tracerColor = ColorMain.getEnemyGSColor();
            } else {
               if (mc.field_71439_g.func_70032_d(e) < 20.0F) {
                  this.tracerColor = this.nearColor.getValue();
               }

               if (mc.field_71439_g.func_70032_d(e) >= 20.0F && mc.field_71439_g.func_70032_d(e) < 50.0F) {
                  this.tracerColor = this.midColor.getValue();
               }

               if (mc.field_71439_g.func_70032_d(e) >= 50.0F) {
                  this.tracerColor = this.farColor.getValue();
               }
            }

            GlStateManager.func_179094_E();
            this.drawLineToEntityPlayer(e, this.tracerColor);
            GlStateManager.func_179121_F();
         }
      });
   }

   public void drawLineToEntityPlayer(Entity e, PandoraColor color) {
      double[] xyz = interpolate(e);
      this.drawLine1(xyz[0], xyz[1], xyz[2], (double)e.field_70131_O, color);
   }

   public static double[] interpolate(Entity entity) {
      double posX = interpolate(entity.field_70165_t, entity.field_70142_S);
      double posY = interpolate(entity.field_70163_u, entity.field_70137_T);
      double posZ = interpolate(entity.field_70161_v, entity.field_70136_U);
      return new double[]{posX, posY, posZ};
   }

   public static double interpolate(double now, double then) {
      return then + (now - then) * (double)mc.func_184121_ak();
   }

   public void drawLine1(double posx, double posy, double posz, double up, PandoraColor color) {
      Vec3d eyes = ActiveRenderInfo.getCameraPosition().func_72441_c(mc.func_175598_ae().field_78730_l, mc.func_175598_ae().field_78731_m, mc.func_175598_ae().field_78728_n);
      if (this.pointsTo.getValue().equalsIgnoreCase("Head")) {
         PandoraTessellator.drawLine(eyes.field_72450_a, eyes.field_72448_b, eyes.field_72449_c, posx, posy + up, posz, color);
      } else {
         PandoraTessellator.drawLine(eyes.field_72450_a, eyes.field_72448_b, eyes.field_72449_c, posx, posy, posz, color);
      }

   }
}
