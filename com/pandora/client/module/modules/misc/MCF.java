package com.pandora.client.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import org.lwjgl.input.Mouse;

public class MCF extends Module {
   @EventHandler
   private final Listener<MouseInputEvent> listener = new Listener((event) -> {
      if (mc.field_71476_x.field_72313_a.equals(Type.ENTITY) && mc.field_71476_x.field_72308_g instanceof EntityPlayer && Mouse.getEventButton() == 2) {
         Friends var10000;
         if (Friends.isFriend(mc.field_71476_x.field_72308_g.func_70005_c_())) {
            var10000 = PandoraMod.getInstance().friends;
            Friends.delFriend(mc.field_71476_x.field_72308_g.func_70005_c_());
            MessageBus.sendClientPrefixMessage(ChatFormatting.RED + "Removed " + mc.field_71476_x.field_72308_g.func_70005_c_() + " from friends list");
         } else {
            var10000 = PandoraMod.getInstance().friends;
            Friends.addFriend(mc.field_71476_x.field_72308_g.func_70005_c_());
            MessageBus.sendClientPrefixMessage(ChatFormatting.GREEN + "Added " + mc.field_71476_x.field_72308_g.func_70005_c_() + " to friends list");
         }
      }

   }, new Predicate[0]);

   public MCF() {
      super("MCF", Module.Category.Misc);
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
   }
}
