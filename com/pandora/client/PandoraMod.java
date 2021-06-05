package com.pandora.client;

import com.pandora.api.config.ConfigStopper;
import com.pandora.api.config.LoadConfig;
import com.pandora.api.config.SaveConfig;
import com.pandora.api.event.EventProcessor;
import com.pandora.api.settings.SettingsManager;
import com.pandora.api.util.font.CFontRenderer;
import com.pandora.api.util.players.enemy.Enemies;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.api.util.render.CapeUtils;
import com.pandora.client.clickgui.PandoraGUI;
import com.pandora.client.command.CommandManager;
import com.pandora.client.module.ModuleManager;
import java.awt.Font;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(
   modid = "pandora",
   name = "PandoraHack",
   version = "v1.2",
   clientSideOnly = true
)
public class PandoraMod {
   public static final String MODID = "pandora";
   public static String MODNAME = "PandoraHack+";
   public static final String FORGENAME = "PandoraHack";
   public static final String MODVER = "v1.2";
   public static final Logger log;
   public EventProcessor eventProcessor;
   public SaveConfig saveConfig;
   public LoadConfig loadConfig;
   public ModuleManager moduleManager;
   public SettingsManager settingsManager;
   public static CFontRenderer fontRenderer;
   public CapeUtils capeUtils;
   public PandoraGUI clickGUI;
   public Friends friends;
   public Enemies enemies;
   public static final EventBus EVENT_BUS;
   @Instance
   private static PandoraMod INSTANCE;

   public PandoraMod() {
      INSTANCE = this;
   }

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      this.eventProcessor = new EventProcessor();
      this.eventProcessor.init();
      log.info("Events initialized!");
      fontRenderer = new CFontRenderer(new Font("Verdana", 0, 18), true, true);
      log.info("Custom font initialized!");
      this.settingsManager = new SettingsManager();
      log.info("Settings initialized!");
      this.friends = new Friends();
      this.enemies = new Enemies();
      log.info("Friends and enemies initialized!");
      this.moduleManager = new ModuleManager();
      log.info("Modules initialized!");
      this.clickGUI = new PandoraGUI();
      log.info("ClickGUI initialized!");
      CommandManager.registerCommands();
      log.info("Commands initialized!");
      this.saveConfig = new SaveConfig();
      this.loadConfig = new LoadConfig();
      Runtime.getRuntime().addShutdownHook(new ConfigStopper());
      log.info("Config initialized!");
      log.info("Initialization complete!\n");
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      Display.setTitle(MODNAME + " " + "v1.2");
      this.capeUtils = new CapeUtils();
      log.info("Capes initialised!");
      log.info("PostInitialization complete!\n");
   }

   public static PandoraMod getInstance() {
      return INSTANCE;
   }

   static {
      log = LogManager.getLogger(MODNAME);
      EVENT_BUS = new EventManager();
   }
}
