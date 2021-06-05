package com.pandora.client.module.modules.misc;

import com.mojang.authlib.GameProfile;
import com.pandora.client.module.Module;
import java.util.UUID;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.GameType;

public class FakePlayer extends Module {
   private EntityOtherPlayerMP clonedPlayer;

   public FakePlayer() {
      super("FakePlayer", Module.Category.Misc);
   }

   public void onEnable() {
      if (mc.field_71439_g != null && !mc.field_71439_g.field_70128_L) {
         this.clonedPlayer = new EntityOtherPlayerMP(mc.field_71441_e, new GameProfile(UUID.fromString("fdee323e-7f0c-4c15-8d1c-0f277442342a"), "Fit"));
         this.clonedPlayer.func_82149_j(mc.field_71439_g);
         this.clonedPlayer.field_70759_as = mc.field_71439_g.field_70759_as;
         this.clonedPlayer.field_70177_z = mc.field_71439_g.field_70177_z;
         this.clonedPlayer.field_70125_A = mc.field_71439_g.field_70125_A;
         this.clonedPlayer.func_71033_a(GameType.SURVIVAL);
         this.clonedPlayer.func_70606_j(20.0F);
         mc.field_71441_e.func_73027_a(-1234, this.clonedPlayer);
         this.clonedPlayer.func_70636_d();
      } else {
         this.disable();
      }
   }

   public void onDisable() {
      if (mc.field_71441_e != null) {
         mc.field_71441_e.func_73028_b(-1234);
      }

   }
}
