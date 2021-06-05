package com.pandora.client.module.modules.render;

import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.util.misc.Wrapper;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.api.util.render.PandoraTessellator;
import com.pandora.client.module.Module;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class HitSpheres extends Module {
   public HitSpheres() {
      super("HitSpheres", Module.Category.Render);
   }

   public void onWorldRender(RenderEvent event) {
      Iterator var2 = Wrapper.getWorld().field_72996_f.iterator();

      while(var2.hasNext()) {
         Entity ep = (Entity)var2.next();
         if (!(ep instanceof EntityPlayerSP) && ep instanceof EntityPlayer) {
            double posX = ep.field_70142_S + (ep.field_70165_t - ep.field_70142_S) * (double)Wrapper.getMinecraft().field_71428_T.field_194147_b;
            double posY = ep.field_70137_T + (ep.field_70163_u - ep.field_70137_T) * (double)Wrapper.getMinecraft().field_71428_T.field_194147_b;
            double posZ = ep.field_70136_U + (ep.field_70161_v - ep.field_70136_U) * (double)Wrapper.getMinecraft().field_71428_T.field_194147_b;
            if (Friends.isFriend(ep.func_70005_c_())) {
               (new PandoraColor(38, 38, 255)).glColor();
            } else if (Wrapper.getPlayer().func_70068_e(ep) >= 64.0D) {
               (new PandoraColor(0, 255, 0)).glColor();
            } else {
               (new PandoraColor(255, (int)(Wrapper.getPlayer().func_70032_d(ep) * 255.0F / 150.0F), 0)).glColor();
            }

            PandoraTessellator.drawSphere(posX, posY, posZ, 6.0F, 20, 15);
         }
      }

   }
}
