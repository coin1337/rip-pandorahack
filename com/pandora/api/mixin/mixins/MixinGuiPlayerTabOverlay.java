package com.pandora.api.mixin.mixins;

import com.pandora.api.util.players.enemy.Enemies;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.client.module.modules.gui.ColorMain;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GuiPlayerTabOverlay.class})
public class MixinGuiPlayerTabOverlay {
   @Inject(
      method = {"getPlayerName"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getPlayerName(NetworkPlayerInfo networkPlayerInfoIn, CallbackInfoReturnable returnable) {
      returnable.cancel();
      returnable.setReturnValue(this.getPlayerName(networkPlayerInfoIn));
   }

   public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
      String dname = networkPlayerInfoIn.func_178854_k() != null ? networkPlayerInfoIn.func_178854_k().func_150254_d() : ScorePlayerTeam.func_96667_a(networkPlayerInfoIn.func_178850_i(), networkPlayerInfoIn.func_178845_a().getName());
      if (Friends.isFriend(dname)) {
         return ColorMain.getFriendColor() + dname;
      } else {
         return Enemies.isEnemy(dname) ? ColorMain.getEnemyColor() + dname : dname;
      }
   }
}
