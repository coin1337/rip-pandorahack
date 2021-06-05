package com.pandora.api.event.events;

import com.pandora.api.event.PandoraEvent;
import net.minecraft.network.Packet;

public class EventNetworkPacketEvent extends PandoraEvent {
   public Packet m_Packet;

   public EventNetworkPacketEvent(Packet p_Packet) {
      this.m_Packet = p_Packet;
   }

   public Packet GetPacket() {
      return this.m_Packet;
   }

   public Packet getPacket() {
      return this.m_Packet;
   }
}
