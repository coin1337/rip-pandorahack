package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;

public class PlayerSkinEvent extends PandoraEvent {
   private final NetworkPlayerInfo networkPlayerInfo;

   public PlayerSkinEvent(NetworkPlayerInfo networkPlayerInfo) {
      this.networkPlayerInfo = networkPlayerInfo;
   }

   public NetworkPlayerInfo getNetworkPlayerInfo() {
      return this.networkPlayerInfo;
   }

   public static class HasSkin extends PlayerSkinEvent {
      public boolean result;

      public HasSkin(NetworkPlayerInfo networkPlayerInfo, boolean result) {
         super(networkPlayerInfo);
         this.result = result;
      }
   }

   public static class GetSkin extends PlayerSkinEvent {
      public ResourceLocation skinLocation;

      public GetSkin(NetworkPlayerInfo networkPlayerInfo, ResourceLocation skinLocation) {
         super(networkPlayerInfo);
         this.skinLocation = skinLocation;
      }
   }
}
