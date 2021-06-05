package com.pandora.client.module;

import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.util.render.PandoraTessellator;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.modules.combat.Aura2;
import com.pandora.client.module.modules.combat.Auto32K;
import com.pandora.client.module.modules.combat.AutoArmor;
import com.pandora.client.module.modules.combat.AutoCrystal;
import com.pandora.client.module.modules.combat.AutoTotem;
import com.pandora.client.module.modules.combat.AutoTrap;
import com.pandora.client.module.modules.combat.AutoWeb;
import com.pandora.client.module.modules.combat.BedAura;
import com.pandora.client.module.modules.combat.FastBow;
import com.pandora.client.module.modules.combat.HoleFill;
import com.pandora.client.module.modules.combat.KillAura;
import com.pandora.client.module.modules.combat.OffhandCrystal;
import com.pandora.client.module.modules.combat.OffhandGap;
import com.pandora.client.module.modules.combat.Quiver;
import com.pandora.client.module.modules.combat.SelfTrap;
import com.pandora.client.module.modules.combat.SelfWeb;
import com.pandora.client.module.modules.combat.Surround;
import com.pandora.client.module.modules.exploits.CoordExploit;
import com.pandora.client.module.modules.exploits.FastBreak;
import com.pandora.client.module.modules.exploits.LiquidInteract;
import com.pandora.client.module.modules.exploits.MountBypass;
import com.pandora.client.module.modules.exploits.NoInteract;
import com.pandora.client.module.modules.exploits.NoSwing;
import com.pandora.client.module.modules.exploits.PortalGodMode;
import com.pandora.client.module.modules.gui.ClickGuiModule;
import com.pandora.client.module.modules.gui.ColorMain;
import com.pandora.client.module.modules.hud.ArmorHUD;
import com.pandora.client.module.modules.hud.CombatInfo;
import com.pandora.client.module.modules.hud.HUDModule;
import com.pandora.client.module.modules.hud.InventoryViewer;
import com.pandora.client.module.modules.hud.ModuleArrayList;
import com.pandora.client.module.modules.hud.Notifications;
import com.pandora.client.module.modules.hud.PotionEffects;
import com.pandora.client.module.modules.hud.TabGUIModule;
import com.pandora.client.module.modules.hud.TargetHUD;
import com.pandora.client.module.modules.hud.TextRadar;
import com.pandora.client.module.modules.hud.Watermark;
import com.pandora.client.module.modules.hud.Welcomer;
import com.pandora.client.module.modules.misc.Announcer;
import com.pandora.client.module.modules.misc.AutoGG;
import com.pandora.client.module.modules.misc.AutoReply;
import com.pandora.client.module.modules.misc.AutoTool;
import com.pandora.client.module.modules.misc.ChatModifier;
import com.pandora.client.module.modules.misc.ChatSuffix;
import com.pandora.client.module.modules.misc.DiscordRPCModule;
import com.pandora.client.module.modules.misc.FakePlayer;
import com.pandora.client.module.modules.misc.FastPlace;
import com.pandora.client.module.modules.misc.HoosiersDupe;
import com.pandora.client.module.modules.misc.HotbarRefill;
import com.pandora.client.module.modules.misc.MCF;
import com.pandora.client.module.modules.misc.MultiTask;
import com.pandora.client.module.modules.misc.NoEntityTrace;
import com.pandora.client.module.modules.misc.NoKick;
import com.pandora.client.module.modules.misc.PvPInfo;
import com.pandora.client.module.modules.movement.Anchor;
import com.pandora.client.module.modules.movement.Blink;
import com.pandora.client.module.modules.movement.HoleTP;
import com.pandora.client.module.modules.movement.LongJump;
import com.pandora.client.module.modules.movement.NoSlow;
import com.pandora.client.module.modules.movement.PlayerTweaks;
import com.pandora.client.module.modules.movement.ReverseStep;
import com.pandora.client.module.modules.movement.Scaffold;
import com.pandora.client.module.modules.movement.Speed;
import com.pandora.client.module.modules.movement.Sprint;
import com.pandora.client.module.modules.movement.Step;
import com.pandora.client.module.modules.movement.Strafe;
import com.pandora.client.module.modules.movement.Velocity;
import com.pandora.client.module.modules.render.BlockHighlight;
import com.pandora.client.module.modules.render.CapesModule;
import com.pandora.client.module.modules.render.CityESP;
import com.pandora.client.module.modules.render.ESP;
import com.pandora.client.module.modules.render.Freecam;
import com.pandora.client.module.modules.render.Fullbright;
import com.pandora.client.module.modules.render.HitSpheres;
import com.pandora.client.module.modules.render.HoleESP;
import com.pandora.client.module.modules.render.LogoutSpots;
import com.pandora.client.module.modules.render.Nametags;
import com.pandora.client.module.modules.render.NoRender;
import com.pandora.client.module.modules.render.RenderTweaks;
import com.pandora.client.module.modules.render.ShulkerViewer;
import com.pandora.client.module.modules.render.SkyColor;
import com.pandora.client.module.modules.render.Tracers;
import com.pandora.client.module.modules.render.ViewModel;
import com.pandora.client.module.modules.render.VoidESP;
import java.awt.Point;
import java.util.ArrayList;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ModuleManager {
   public static ArrayList<Module> modules;

   public ModuleManager() {
      modules = new ArrayList();
      addMod(new AutoArmor());
      addMod(new Auto32K());
      addMod(new Aura2());
      addMod(new AutoCrystal());
      addMod(new AutoTotem());
      addMod(new AutoTrap());
      addMod(new AutoWeb());
      addMod(new BedAura());
      addMod(new FastBow());
      addMod(new HoleFill());
      addMod(new KillAura());
      addMod(new OffhandCrystal());
      addMod(new OffhandGap());
      addMod(new Quiver());
      addMod(new SelfTrap());
      addMod(new SelfWeb());
      addMod(new Surround());
      addMod(new CoordExploit());
      addMod(new FastBreak());
      addMod(new MountBypass());
      addMod(new LiquidInteract());
      addMod(new NoInteract());
      addMod(new NoSwing());
      addMod(new PortalGodMode());
      addMod(new Anchor());
      addMod(new Blink());
      addMod(new HoleTP());
      addMod(new NoSlow());
      addMod(new LongJump());
      addMod(new PlayerTweaks());
      addMod(new ReverseStep());
      addMod(new Scaffold());
      addMod(new Strafe());
      addMod(new Speed());
      addMod(new Sprint());
      addMod(new Step());
      addMod(new Velocity());
      addMod(new Announcer());
      addMod(new AutoGG());
      addMod(new AutoReply());
      addMod(new AutoTool());
      addMod(new ChatModifier());
      addMod(new ChatSuffix());
      addMod(new DiscordRPCModule());
      addMod(new FastPlace());
      addMod(new FakePlayer());
      addMod(new HoosiersDupe());
      addMod(new HotbarRefill());
      addMod(new MCF());
      addMod(new MultiTask());
      addMod(new NoEntityTrace());
      addMod(new NoKick());
      addMod(new PvPInfo());
      addMod(new BlockHighlight());
      addMod(new CapesModule());
      addMod(new CityESP());
      addMod(new ESP());
      addMod(new Freecam());
      addMod(new Fullbright());
      addMod(new HitSpheres());
      addMod(new HoleESP());
      addMod(new LogoutSpots());
      addMod(new Nametags());
      addMod(new NoRender());
      addMod(new RenderTweaks());
      addMod(new ShulkerViewer());
      addMod(new SkyColor());
      addMod(new Tracers());
      addMod(new ViewModel());
      addMod(new VoidESP());
      addMod(new ArmorHUD());
      addMod(new ModuleArrayList());
      addMod(new CombatInfo());
      addMod(new InventoryViewer());
      addMod(new Notifications());
      addMod(new PotionEffects());
      addMod(new HUDModule(new TabGUIModule(), new Point(10, 10)));
      addMod(new TargetHUD());
      addMod(new TextRadar());
      addMod(new Watermark());
      addMod(new Welcomer());
      addMod(new ClickGuiModule());
      addMod(new ColorMain());
   }

   public static void addMod(Module m) {
      modules.add(m);
   }

   public static void onUpdate() {
      modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
   }

   public static void onRender() {
      modules.stream().filter(Module::isEnabled).forEach(Module::onRender);
      PandoraMod.getInstance().clickGUI.render();
   }

   public static void onWorldRender(RenderWorldLastEvent event) {
      Minecraft.func_71410_x().field_71424_I.func_76320_a("pandora");
      Minecraft.func_71410_x().field_71424_I.func_76320_a("setup");
      PandoraTessellator.prepare();
      RenderEvent e = new RenderEvent(event.getPartialTicks());
      Minecraft.func_71410_x().field_71424_I.func_76319_b();
      modules.stream().filter((module) -> {
         return module.isEnabled();
      }).forEach((module) -> {
         Minecraft.func_71410_x().field_71424_I.func_76320_a(module.getName());
         module.onWorldRender(e);
         Minecraft.func_71410_x().field_71424_I.func_76319_b();
      });
      Minecraft.func_71410_x().field_71424_I.func_76320_a("release");
      PandoraTessellator.release();
      Minecraft.func_71410_x().field_71424_I.func_76319_b();
      Minecraft.func_71410_x().field_71424_I.func_76319_b();
   }

   public static ArrayList<Module> getModules() {
      return modules;
   }

   public static ArrayList<Module> getModulesInCategory(Module.Category c) {
      ArrayList<Module> list = (ArrayList)getModules().stream().filter((m) -> {
         return m.getCategory().equals(c);
      }).collect(Collectors.toList());
      return list;
   }

   public static void onBind(int key) {
      if (key != 0 && key != 0) {
         modules.forEach((module) -> {
            if (module.getBind() == key) {
               module.toggle();
            }

         });
      }
   }

   public static Module getModuleByName(String name) {
      Module m = (Module)getModules().stream().filter((mm) -> {
         return mm.getName().equalsIgnoreCase(name);
      }).findFirst().orElse((Object)null);
      return m;
   }

   public static boolean isModuleEnabled(String name) {
      Module m = (Module)getModules().stream().filter((mm) -> {
         return mm.getName().equalsIgnoreCase(name);
      }).findFirst().orElse((Object)null);
      return m.isEnabled();
   }

   public static boolean isModuleEnabled(Module m) {
      return m.isEnabled();
   }

   public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
      return (new Vec3d(entity.field_70142_S, entity.field_70137_T, entity.field_70136_U)).func_178787_e(getInterpolatedAmount(entity, (double)ticks));
   }

   public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
      return getInterpolatedAmount(entity, ticks, ticks, ticks);
   }

   public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
      return new Vec3d((entity.field_70165_t - entity.field_70142_S) * x, (entity.field_70163_u - entity.field_70137_T) * y, (entity.field_70161_v - entity.field_70136_U) * z);
   }
}
