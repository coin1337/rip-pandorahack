package com.pandora.client.module.modules.combat;

import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.world.BlockUtils;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Auto32K extends Module {
   private static final DecimalFormat df = new DecimalFormat("#.#");
   private final Setting.Boolean rotate = this.registerBoolean("Rotate", "Rotate", false);
   private final Setting.Boolean grabItem = this.registerBoolean("Grab Item", "Grab Item", false);
   private final Setting.Boolean autoEnableHitAura = this.registerBoolean("Auto enable Hit Aura", "Auto enable Hit Aura", false);
   private final Setting.Boolean debugMessages = this.registerBoolean("Debug Messages", "Debug Messages", false);
   private int stage;
   private BlockPos placeTarget;
   private int obiSlot;
   private int dispenserSlot;
   private int shulkerSlot;
   private int redstoneSlot;
   private int hopperSlot;
   private boolean isSneaking;

   public Auto32K() {
      super("Auto32K", Module.Category.Combat);
   }

   protected void onEnable() {
      if (mc.field_71439_g != null && !ModuleManager.isModuleEnabled("Freecam")) {
         df.setRoundingMode(RoundingMode.CEILING);
         this.stage = 0;
         this.placeTarget = null;
         this.obiSlot = -1;
         this.dispenserSlot = -1;
         this.shulkerSlot = -1;
         this.redstoneSlot = -1;
         this.hopperSlot = -1;
         this.isSneaking = false;

         for(int i = 0; i < 9 && (this.obiSlot == -1 || this.dispenserSlot == -1 || this.shulkerSlot == -1 || this.redstoneSlot == -1 || this.hopperSlot == -1); ++i) {
            ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != ItemStack.field_190927_a && stack.func_77973_b() instanceof ItemBlock) {
               Block block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
               if (block == Blocks.field_150438_bZ) {
                  this.hopperSlot = i;
               } else if (BlockUtils.shulkerList.contains(block)) {
                  this.shulkerSlot = i;
               } else if (block == Blocks.field_150343_Z) {
                  this.obiSlot = i;
               } else if (block == Blocks.field_150367_z) {
                  this.dispenserSlot = i;
               } else if (block == Blocks.field_150451_bX) {
                  this.redstoneSlot = i;
               }
            }
         }

         if (this.obiSlot != -1 && this.dispenserSlot != -1 && this.shulkerSlot != -1 && this.redstoneSlot != -1 && this.hopperSlot != -1) {
            if (mc.field_71476_x != null && mc.field_71476_x.func_178782_a() != null && mc.field_71476_x.func_178782_a().func_177984_a() != null) {
               this.placeTarget = mc.field_71476_x.func_178782_a().func_177984_a();
               if (this.debugMessages.getValue()) {
                  MessageBus.sendClientPrefixMessage("Auto32k: Place Target is " + this.placeTarget.field_177962_a + " " + this.placeTarget.field_177960_b + " " + this.placeTarget.field_177961_c + " Distance: " + df.format(mc.field_71439_g.func_174791_d().func_72438_d(new Vec3d(this.placeTarget))));
               }
            } else {
               if (this.debugMessages.getValue()) {
                  MessageBus.sendClientPrefixMessage("Auto32k: Not a valid place target, disabling.");
               }

               this.disable();
            }
         } else {
            if (this.debugMessages.getValue()) {
               MessageBus.sendClientPrefixMessage("Auto32k: Items missing, disabling.");
            }

            this.disable();
         }
      } else {
         this.disable();
      }
   }

   public void onUpdate() {
      if (mc.field_71439_g != null) {
         ModuleManager var10000 = PandoraMod.getInstance().moduleManager;
         if (!ModuleManager.isModuleEnabled("Freecam")) {
            if (this.stage == 0) {
               mc.field_71439_g.field_71071_by.field_70461_c = this.obiSlot;
               this.placeBlock(new BlockPos(this.placeTarget), EnumFacing.DOWN);
               mc.field_71439_g.field_71071_by.field_70461_c = this.dispenserSlot;
               this.placeBlock(new BlockPos(this.placeTarget.func_177982_a(0, 1, 0)), EnumFacing.DOWN);
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
               this.isSneaking = false;
               mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(this.placeTarget.func_177982_a(0, 1, 0), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
               this.stage = 1;
            } else if (this.stage == 1) {
               if (mc.field_71462_r instanceof GuiContainer) {
                  mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71070_bA.field_75152_c, 1, this.shulkerSlot, ClickType.SWAP, mc.field_71439_g);
                  mc.field_71439_g.func_71053_j();
                  mc.field_71439_g.field_71071_by.field_70461_c = this.redstoneSlot;
                  this.placeBlock(new BlockPos(this.placeTarget.func_177982_a(0, 2, 0)), EnumFacing.DOWN);
                  this.stage = 2;
               }
            } else if (this.stage == 2) {
               Block block = mc.field_71441_e.func_180495_p(this.placeTarget.func_177972_a(mc.field_71439_g.func_174811_aO().func_176734_d()).func_177984_a()).func_177230_c();
               if (!(block instanceof BlockAir)) {
                  if (!(block instanceof BlockLiquid)) {
                     mc.field_71439_g.field_71071_by.field_70461_c = this.hopperSlot;
                     this.placeBlock(new BlockPos(this.placeTarget.func_177972_a(mc.field_71439_g.func_174811_aO().func_176734_d())), mc.field_71439_g.func_174811_aO());
                     mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
                     this.isSneaking = false;
                     mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(this.placeTarget.func_177972_a(mc.field_71439_g.func_174811_aO().func_176734_d()), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
                     mc.field_71439_g.field_71071_by.field_70461_c = this.shulkerSlot;
                     if (!this.grabItem.getValue()) {
                        this.disable();
                     } else {
                        this.stage = 3;
                     }
                  }
               }
            } else if (this.stage == 3) {
               if (mc.field_71462_r instanceof GuiContainer) {
                  if (!((GuiContainer)mc.field_71462_r).field_147002_h.func_75139_a(0).func_75211_c().field_190928_g) {
                     mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71070_bA.field_75152_c, 0, 0, ClickType.QUICK_MOVE, mc.field_71439_g);
                     if (this.autoEnableHitAura.getValue()) {
                        ModuleManager.getModuleByName("Bruce Aura").enable();
                        ModuleManager.getModuleByName("YakgodAura").enable();
                     }

                     this.disable();
                  }
               }
            }
         }
      }
   }

   private void placeBlock(BlockPos pos, EnumFacing side) {
      BlockPos neighbour = pos.func_177972_a(side);
      EnumFacing opposite = side.func_176734_d();
      if (!this.isSneaking) {
         mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
         this.isSneaking = true;
      }

      Vec3d hitVec = (new Vec3d(neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
      if (this.rotate.getValue()) {
         BlockUtils.faceVectorPacketInstant(hitVec);
      }

      mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
      mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
   }
}
