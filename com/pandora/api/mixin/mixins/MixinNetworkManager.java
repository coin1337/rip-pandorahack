package com.pandora.api.mixin.mixins;

import com.pandora.api.event.events.PacketEvent;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.misc.NoKick;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetworkManager.class})
public class MixinNetworkManager {
   @Inject(
      method = {"sendPacket(Lnet/minecraft/network/Packet;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void preSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
      PacketEvent.Send event = new PacketEvent.Send(packet);
      PandoraMod.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = {"channelRead0"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void preChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
      PacketEvent.Receive event = new PacketEvent.Receive(packet);
      PandoraMod.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = {"sendPacket(Lnet/minecraft/network/Packet;)V"},
      at = {@At("TAIL")},
      cancellable = true
   )
   private void postSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
      PacketEvent.PostSend event = new PacketEvent.PostSend(packet);
      PandoraMod.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = {"channelRead0"},
      at = {@At("TAIL")},
      cancellable = true
   )
   private void postChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
      PacketEvent.PostReceive event = new PacketEvent.PostReceive(packet);
      PandoraMod.EVENT_BUS.post(event);
      if (event.isCancelled()) {
         callbackInfo.cancel();
      }

   }

   @Inject(
      method = {"exceptionCaught"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_, CallbackInfo info) {
      if (p_exceptionCaught_2_ instanceof IOException && ModuleManager.isModuleEnabled("NoKick") && ((NoKick)ModuleManager.getModuleByName("NoKick")).noPacketKick.getValue()) {
         info.cancel();
      }

   }
}
