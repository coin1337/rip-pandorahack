package com.pandora.api.util.world;

import com.pandora.api.event.events.PacketEvent;
import com.pandora.client.PandoraMod;
import java.util.Arrays;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;

public class TpsUtils {
   private static final float[] tickRates = new float[20];
   private int nextIndex = 0;
   private long timeLastTimeUpdate;
   @EventHandler
   Listener<PacketEvent.Receive> listener = new Listener((event) -> {
      if (event.getPacket() instanceof SPacketTimeUpdate) {
         this.onTimeUpdate();
      }

   }, new Predicate[0]);

   public TpsUtils() {
      this.nextIndex = 0;
      this.timeLastTimeUpdate = -1L;
      Arrays.fill(tickRates, 0.0F);
      PandoraMod.EVENT_BUS.subscribe((Object)this);
   }

   public static float getTickRate() {
      float numTicks = 0.0F;
      float sumTickRates = 0.0F;
      float[] var2 = tickRates;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         float tickRate = var2[var4];
         if (tickRate > 0.0F) {
            sumTickRates += tickRate;
            ++numTicks;
         }
      }

      return MathHelper.func_76131_a(sumTickRates / numTicks, 0.0F, 20.0F);
   }

   private void onTimeUpdate() {
      if (this.timeLastTimeUpdate != -1L) {
         float timeElapsed = (float)(System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0F;
         tickRates[this.nextIndex % tickRates.length] = MathHelper.func_76131_a(20.0F / timeElapsed, 0.0F, 20.0F);
         ++this.nextIndex;
      }

      this.timeLastTimeUpdate = System.currentTimeMillis();
   }
}
