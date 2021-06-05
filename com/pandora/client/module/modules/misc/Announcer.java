package com.pandora.client.module.modules.misc;

import com.pandora.api.event.events.DestroyBlockEvent;
import com.pandora.api.event.events.PacketEvent;
import com.pandora.api.event.events.PlayerJumpEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Finish;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class Announcer extends Module {
   public static int blockBrokeDelay = 0;
   static int blockPlacedDelay = 0;
   static int jumpDelay = 0;
   static int attackDelay = 0;
   static int eattingDelay = 0;
   static long lastPositionUpdate;
   static double lastPositionX;
   static double lastPositionY;
   static double lastPositionZ;
   private static double speed;
   String heldItem = "";
   int blocksPlaced = 0;
   int blocksBroken = 0;
   int eaten = 0;
   public Setting.Boolean clientSide;
   Setting.Boolean walk;
   Setting.Boolean place;
   Setting.Boolean jump;
   Setting.Boolean breaking;
   Setting.Boolean attack;
   Setting.Boolean eat;
   public Setting.Boolean clickGui;
   Setting.Integer delay;
   public static String walkMessage = "I just walked{blocks} meters thanks to Pandora!";
   public static String placeMessage = "I just inserted{amount}{name} into the muliverse thanks to Pandora!";
   public static String jumpMessage = "I just hovered in the air thanks to Pandora!";
   public static String breakMessage = "I just snapped{amount}{name} out of existance thanks to Pandora!";
   public static String attackMessage = "I just disembowed{name} with a{item} thanks to Pandora!";
   public static String eatMessage = "I just gobbled up{amount}{name} thanks to Pandora!";
   public static String guiMessage = "I just opened my advanced hacking console thanks to Pandora!";
   public static String[] walkMessages = new String[]{"I just walked{blocks} meters thanks to Pandora!", "!لقد مشيت للتو على بعد{blocks} متر من الأمتار بفضل Pandora!", "¡Acabo de caminar{blocks} metros gracias a Pandora!", "Je viens de marcher{blocks} mètres grâce à Pandora!", "פשוט הלכתי{blocks} מטרים בזכות Pandora!", "Ich bin gerade{blocks} Meter dank Pandora gelaufen!\n", "Pandoraのおかげで{blocks}メートル歩いたところです!", "Ik heb net{blocks} gelopen met dank aan Pandora!", "Μώλις περπάτησα{blocks} μέτρα χάρη το Pandora!", "Pandora sayesinde{blocks} metre yürüdüm!", "Właśnie przeszedłem{blocks} metry dzięki Pandora!", "Я просто прошел{blocks} метров благодаря Pandora!", "Jeg gik lige{blocks} meter takket være Pandora!", "Unë vetëm eca{blocks} metra falë Pandora!", "多亏了Pandora，我才走了{blocks}米！", "Kävelin juuri{blocks} metriä Pandoran ansiosta!"};
   public static String[] placeMessages = new String[]{"I just inserted{amount}{name} into the muliverse thanks to Pandora!", "لقد أدرجت للتو{amount}{name} في muliverse بفضل Pandora!", "¡Acabo de insertar{amount}{name} en el universo gracias a Pandora!", "Je viens d'insérer{amount}{name} dans le mulivers grâce à Pandora!", "הרגע הכנסתי את{amount}{name} למוליברס בזכות Pandora!", "Ich habe gerade dank Pandora{amount}{name} in das Multiversum eingefÃ¼gt! \n", "Pandoraのおかげで、{amount}{name}をマルチバースに挿入しました！", "Ik heb zojuist{amount}{name} in het muliversum ingevoegd dankzij Pandora!", "Μώλις χρησιμοποιήσα{amount}{name} χάρη το Pandora", "Pandora sayesinde birden fazla kişiye{amount}{name} ekledim!", "Właśnie wstawiłem{amount}{name} do wielu dzięki Pandora!", "Я только что вставил{amount}{name} во вселенную благодаря Pandora!", "Jeg har lige indsat{amount}{name} i muliversen takket være Pandora!", "多亏了Pandora，我刚刚将{amount}{name}插入了多人游戏！", "Unë vetëm futa{amount}{name} në muliverse falë Pandora!"};
   public static String[] jumpMessages = new String[]{"I just hovered in the air thanks to Pandora!", "لقد حومت للتو في الهواء بفضل Pandora!", "¡Acabo de volar en el aire gracias a Pandora!", "Je viens de planer dans les airs grâce à Pandora!", "פשוט ריחפתי באוויר בזכות Pandora!", "Ich habe gerade dank Pandora in der Luft geschwebt!\n", "Pandoraのおかげで宙に浮いただけです！", "Dankzij Pandora zweefde ik gewoon in de lucht!", "Μόλις αιωρήθηκα στον αέρα χάρης το Pandora!", "Pandora sayesinde havada asılı kaldım!", "Po prostu unosiłem się w powietrzu dzięki Pandora!", "Я просто завис в воздухе благодаря Pandora!", "Unë thjesht u fiksova në ajër falë Pandora!", "多亏了Pandora，我才徘徊在空中！", "Minä vain leijuin ilmassa Pandoran ansiosta!"};
   public static String[] breakMessages = new String[]{"I just snapped{amount}{name} out of existance thanks to Pandora!", "لقد قطعت للتو{amount}{name} من خارج بفضل Pandora!", "¡Acabo de sacar{amount}{name} de la existencia gracias a Pandora!", "Je viens de casser{amount}{name} hors de l'existence grâce à Pandora!", "פשוט חטפתי את{amount}{name} מההתקיים בזכות Pandora!", "Ich habe gerade{amount}{name} dank Pandora aus der Existenz gerissen!", "Pandoraのおかげで、{amount}{name}が存在しなくなりました。", "Ik heb zojuist{amount}{name} uit het bestaan \u200b\u200bgehaald dankzij Pandora!", "Μώλις έσπασα το{amount}{name} από την ύπαρξη χάρη στο Pandora!", "Pandora sayesinde{amount}{name} varlığını yeni çıkardım!", "Właśnie wyskoczyłem z gry dzięki{amount}{name} dzięki Pandora!", "Я только что отключил{amount}{name} из существования благодаря Pandora!", "Jeg har lige slået{amount}{name} ud af eksistens takket være Pandora!", "多亏了Pandora，我才将{amount}{name}淘汰了！", "Napsautin juuri{amount}{name} olemassaolosta Pandoran ansiosta!"};
   public static String[] eatMessages = new String[]{"I just ate{amount}{name} thanks to Pandora!", "لقد أكلت للتو{amount}{name} بفضل Pandora!", "¡Acabo de comer{amount}{name} gracias a Pandora!", "Je viens de manger{amount}{name} grâce à Pandora!", "פשוט אכלתי{amount}{name} בזכות Pandora!", "Ich habe gerade dank Pandora{amount}{name} gegessen!", "Pandoraのおかげで{amount}{name}を食べました。", "Ik heb zojuist{amount}{name} gegeten dankzij Pandora!", "Μόλις έφαγα{amount}{name} χάρη στο Pandora!", "Pandora sayesinde sadece{amount}{name} yedim!", "Właśnie zjadłem{amount}{name} dzięki Pandora!", "Jeg spiste lige{amount}{name} takket være Pandora!", "Я только что съел{amount}{name} благодаря Pandora!", "Unë thjesht hëngra{amount}{name} falë Pandora!", "感谢Pandora，我刚吃了{amount}{name}！", "Söin juuri{amount}{name} Gamessenin ansiosta!"};
   @EventHandler
   private final Listener<Finish> eatListener = new Listener((event) -> {
      int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
      if (event.getEntity() == mc.field_71439_g && (event.getItem().func_77973_b() instanceof ItemFood || event.getItem().func_77973_b() instanceof ItemAppleGold)) {
         ++this.eaten;
         if (eattingDelay >= 300 * this.delay.getValue() && this.eat.getValue() && this.eaten > randomNum) {
            Random random = new Random();
            if (this.clientSide.getValue()) {
               MessageBus.sendClientPrefixMessage(eatMessages[random.nextInt(eatMessages.length)].replace("{amount}", " " + this.eaten).replace("{name}", " " + mc.field_71439_g.func_184614_ca().func_82833_r()));
            } else {
               MessageBus.sendServerMessage(eatMessages[random.nextInt(eatMessages.length)].replace("{amount}", " " + this.eaten).replace("{name}", " " + mc.field_71439_g.func_184614_ca().func_82833_r()));
            }

            this.eaten = 0;
            eattingDelay = 0;
         }
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<PacketEvent.Send> sendListener = new Listener((event) -> {
      if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemBlock) {
         ++this.blocksPlaced;
         int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
         if (blockPlacedDelay >= 150 * this.delay.getValue() && this.place.getValue() && this.blocksPlaced > randomNum) {
            Random random = new Random();
            String msg = placeMessages[random.nextInt(placeMessages.length)].replace("{amount}", " " + this.blocksPlaced).replace("{name}", " " + mc.field_71439_g.func_184614_ca().func_82833_r());
            if (this.clientSide.getValue()) {
               MessageBus.sendClientPrefixMessage(msg);
            } else {
               MessageBus.sendServerMessage(msg);
            }

            this.blocksPlaced = 0;
            blockPlacedDelay = 0;
         }
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<DestroyBlockEvent> destroyListener = new Listener((event) -> {
      ++this.blocksBroken;
      int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
      if (blockBrokeDelay >= 300 * this.delay.getValue() && this.breaking.getValue() && this.blocksBroken > randomNum) {
         Random random = new Random();
         String msg = breakMessages[random.nextInt(breakMessages.length)].replace("{amount}", " " + this.blocksBroken).replace("{name}", " " + mc.field_71441_e.func_180495_p(event.getBlockPos()).func_177230_c().func_149732_F());
         if (this.clientSide.getValue()) {
            MessageBus.sendClientPrefixMessage(msg);
         } else {
            MessageBus.sendServerMessage(msg);
         }

         this.blocksBroken = 0;
         blockBrokeDelay = 0;
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<AttackEntityEvent> attackListener = new Listener((event) -> {
      if (this.attack.getValue() && !(event.getTarget() instanceof EntityEnderCrystal) && attackDelay >= 300 * this.delay.getValue()) {
         String msg = attackMessage.replace("{name}", " " + event.getTarget().func_70005_c_()).replace("{item}", " " + mc.field_71439_g.func_184614_ca().func_82833_r());
         if (this.clientSide.getValue()) {
            MessageBus.sendClientPrefixMessage(msg);
         } else {
            MessageBus.sendServerMessage(msg);
         }

         attackDelay = 0;
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<PlayerJumpEvent> jumpListener = new Listener((event) -> {
      if (this.jump.getValue() && jumpDelay >= 300 * this.delay.getValue()) {
         Random random;
         if (this.clientSide.getValue()) {
            random = new Random();
            MessageBus.sendClientPrefixMessage(jumpMessages[random.nextInt(jumpMessages.length)]);
         } else {
            random = new Random();
            MessageBus.sendServerMessage(jumpMessages[random.nextInt(jumpMessages.length)]);
         }

         jumpDelay = 0;
      }

   }, new Predicate[0]);

   public Announcer() {
      super("Announcer", Module.Category.Misc);
   }

   public void setup() {
      this.clientSide = this.registerBoolean("Client Side", "ClientSide", false);
      this.walk = this.registerBoolean("Walk", "Walk", true);
      this.place = this.registerBoolean("Place", "Place", true);
      this.jump = this.registerBoolean("Jump", "Jump", true);
      this.breaking = this.registerBoolean("Breaking", "Breaking", true);
      this.attack = this.registerBoolean("Attack", "Attack", true);
      this.eat = this.registerBoolean("Eat", "Eat", true);
      this.clickGui = this.registerBoolean("DevGUI", "DevGUI", true);
      this.delay = this.registerInteger("Delay", "Delay", 1, 1, 20);
   }

   public void onUpdate() {
      ++blockBrokeDelay;
      ++blockPlacedDelay;
      ++jumpDelay;
      ++attackDelay;
      ++eattingDelay;
      this.heldItem = mc.field_71439_g.func_184614_ca().func_82833_r();
      if (this.walk.getValue() && lastPositionUpdate + 5000L * (long)this.delay.getValue() < System.currentTimeMillis()) {
         double d0 = lastPositionX - mc.field_71439_g.field_70142_S;
         double d2 = lastPositionY - mc.field_71439_g.field_70137_T;
         double d3 = lastPositionZ - mc.field_71439_g.field_70136_U;
         speed = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
         if (!(speed <= 1.0D) && !(speed > 5000.0D)) {
            String walkAmount = (new DecimalFormat("0.00")).format(speed);
            Random random = new Random();
            if (this.clientSide.getValue()) {
               MessageBus.sendClientPrefixMessage(walkMessage.replace("{blocks}", " " + walkAmount));
            } else {
               MessageBus.sendServerMessage(walkMessages[random.nextInt(walkMessages.length)].replace("{blocks}", " " + walkAmount));
            }

            lastPositionUpdate = System.currentTimeMillis();
            lastPositionX = mc.field_71439_g.field_70142_S;
            lastPositionY = mc.field_71439_g.field_70137_T;
            lastPositionZ = mc.field_71439_g.field_70136_U;
         }
      }

   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
      this.blocksPlaced = 0;
      this.blocksBroken = 0;
      this.eaten = 0;
      speed = 0.0D;
      blockBrokeDelay = 0;
      blockPlacedDelay = 0;
      jumpDelay = 0;
      attackDelay = 0;
      eattingDelay = 0;
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
