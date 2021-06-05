package com.pandora.client.module.modules.render;

import com.pandora.api.event.events.BossbarEvent;
import com.pandora.api.settings.Setting;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.material.Material;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class NoRender extends Module {
   public Setting.Boolean armor;
   Setting.Boolean fire;
   Setting.Boolean blind;
   Setting.Boolean nausea;
   public Setting.Boolean hurtCam;
   public Setting.Boolean noOverlay;
   Setting.Boolean noBossBar;
   public Setting.Boolean noSkylight;
   @EventHandler
   public Listener<RenderBlockOverlayEvent> blockOverlayEventListener = new Listener((event) -> {
      if (this.fire.getValue() && event.getOverlayType() == OverlayType.FIRE) {
         event.setCanceled(true);
      }

      if (this.noOverlay.getValue() && event.getOverlayType() == OverlayType.WATER) {
         event.setCanceled(true);
      }

      if (this.noOverlay.getValue() && event.getOverlayType() == OverlayType.BLOCK) {
         event.setCanceled(true);
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<FogDensity> fogDensityListener = new Listener((event) -> {
      if (this.noOverlay.getValue() && (event.getState().func_185904_a().equals(Material.field_151586_h) || event.getState().func_185904_a().equals(Material.field_151587_i))) {
         event.setDensity(0.0F);
         event.setCanceled(true);
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<RenderBlockOverlayEvent> renderBlockOverlayEventListener = new Listener((event) -> {
      event.setCanceled(true);
   }, new Predicate[0]);
   @EventHandler
   private final Listener<RenderGameOverlayEvent> renderGameOverlayEventListener = new Listener((event) -> {
      if (this.noOverlay.getValue()) {
         if (event.getType().equals(ElementType.HELMET)) {
            event.setCanceled(true);
         }

         if (event.getType().equals(ElementType.PORTAL)) {
            event.setCanceled(true);
         }
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<BossbarEvent> bossbarEventListener = new Listener((event) -> {
      if (this.noBossBar.getValue()) {
         event.cancel();
      }

   }, new Predicate[0]);

   public NoRender() {
      super("NoRender", Module.Category.Render);
   }

   public void setup() {
      this.armor = this.registerBoolean("Armor", "Armor", false);
      this.fire = this.registerBoolean("Fire", "Fire", false);
      this.blind = this.registerBoolean("Blind", "Blind", false);
      this.nausea = this.registerBoolean("Nausea", "Nausea", false);
      this.hurtCam = this.registerBoolean("HurtCam", "HurtCam", false);
      this.noSkylight = this.registerBoolean("Skylight", "Skylight", false);
      this.noOverlay = this.registerBoolean("No Overlay", "NoOverlay", false);
      this.noBossBar = this.registerBoolean("No Boss Bar", "NoBossBar", false);
   }

   public void onUpdate() {
      if (this.blind.getValue() && mc.field_71439_g.func_70644_a(MobEffects.field_76440_q)) {
         mc.field_71439_g.func_184589_d(MobEffects.field_76440_q);
      }

      if (this.nausea.getValue() && mc.field_71439_g.func_70644_a(MobEffects.field_76431_k)) {
         mc.field_71439_g.func_184589_d(MobEffects.field_76431_k);
      }

   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
