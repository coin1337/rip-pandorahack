package com.pandora.client.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.client.module.Module;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BedAura extends Module {
   Setting.Integer range;
   Setting.Boolean announceUsage;
   Setting.Integer placedelay;
   Setting.Boolean placeesp;
   private int playerHotbarSlot = -1;
   private int lastHotbarSlot = -1;
   private EntityPlayer closestTarget;
   private String lastTickTargetName;
   private int bedSlot = -1;
   private BlockPos placeTarget;
   private float rotVar;
   private int blocksPlaced;
   private double diffXZ;
   private boolean firstRun;
   private boolean nowTop = false;

   public BedAura() {
      super("BedAura", Module.Category.Combat);
   }

   public void setup() {
      this.range = this.registerInteger("Range", "range", 7, 0, 9);
      this.placedelay = this.registerInteger("Place Delay", "PlaceDelay", 15, 8, 20);
      this.announceUsage = this.registerBoolean("Announce usage", "AnnounceUsage", false);
      this.placeesp = this.registerBoolean("Place ESP", "PlaceESP", true);
   }

   public void onEnable() {
      if (mc.field_71439_g == null) {
         this.toggle();
      } else {
         MinecraftForge.EVENT_BUS.register(this);
         this.firstRun = true;
         this.blocksPlaced = 0;
         this.playerHotbarSlot = mc.field_71439_g.field_71071_by.field_70461_c;
         this.lastHotbarSlot = -1;
      }
   }

   public void onDisable() {
      if (mc.field_71439_g != null) {
         MinecraftForge.EVENT_BUS.unregister(this);
         if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            mc.field_71439_g.field_71071_by.field_70461_c = this.playerHotbarSlot;
         }

         this.playerHotbarSlot = -1;
         this.lastHotbarSlot = -1;
         if (this.announceUsage.getValue()) {
            MessageBus.sendClientPrefixMessage(TextFormatting.GRAY + "[" + TextFormatting.BLUE + "BedAura+" + TextFormatting.GRAY + "]" + ChatFormatting.RED.toString() + " Disabled" + ChatFormatting.RESET.toString() + "!");
         }

         this.blocksPlaced = 0;
      }
   }

   public void onUpdate() {
      if (mc.field_71439_g != null) {
         if (mc.field_71439_g.field_71093_bK == 0) {
            MessageBus.sendClientPrefixMessage("You are in the overworld!");
            this.toggle();
         }

         try {
            this.findClosestTarget();
         } catch (NullPointerException var10) {
         }

         if (this.closestTarget == null && mc.field_71439_g.field_71093_bK != 0 && this.firstRun) {
            this.firstRun = false;
            if (this.announceUsage.getValue()) {
               MessageBus.sendClientPrefixMessage(TextFormatting.GRAY + "[" + TextFormatting.BLUE + "BedAura+" + TextFormatting.GRAY + "]" + TextFormatting.WHITE + " enabled, " + TextFormatting.WHITE + "waiting for target.");
            }
         }

         if (this.firstRun && this.closestTarget != null && mc.field_71439_g.field_71093_bK != 0) {
            this.firstRun = false;
            this.lastTickTargetName = this.closestTarget.func_70005_c_();
            if (this.announceUsage.getValue()) {
               MessageBus.sendClientPrefixMessage(TextFormatting.GRAY + "[" + TextFormatting.BLUE + "BedAura+" + TextFormatting.GRAY + "]" + TextFormatting.WHITE + " enabled" + TextFormatting.WHITE + ", target: " + ChatFormatting.BLUE.toString() + this.lastTickTargetName);
            }
         }

         if (this.closestTarget != null && this.lastTickTargetName != null && !this.lastTickTargetName.equals(this.closestTarget.func_70005_c_())) {
            this.lastTickTargetName = this.closestTarget.func_70005_c_();
            if (this.announceUsage.getValue()) {
               MessageBus.sendClientPrefixMessage(TextFormatting.GRAY + "[" + TextFormatting.BLUE + "BedAura+" + TextFormatting.GRAY + "]" + TextFormatting.WHITE + " New target: " + ChatFormatting.BLUE.toString() + this.lastTickTargetName);
            }
         }

         try {
            this.diffXZ = mc.field_71439_g.func_174791_d().func_72438_d(this.closestTarget.func_174791_d());
         } catch (NullPointerException var9) {
         }

         try {
            if (this.closestTarget != null) {
               this.placeTarget = new BlockPos(this.closestTarget.func_174791_d().func_72441_c(1.0D, 1.0D, 0.0D));
               this.nowTop = false;
               this.rotVar = 90.0F;
               BlockPos block1 = this.placeTarget;
               if (!this.canPlaceBed(block1)) {
                  this.placeTarget = new BlockPos(this.closestTarget.func_174791_d().func_72441_c(-1.0D, 1.0D, 0.0D));
                  this.rotVar = -90.0F;
                  this.nowTop = false;
               }

               BlockPos block2 = this.placeTarget;
               if (!this.canPlaceBed(block2)) {
                  this.placeTarget = new BlockPos(this.closestTarget.func_174791_d().func_72441_c(0.0D, 1.0D, 1.0D));
                  this.rotVar = 180.0F;
                  this.nowTop = false;
               }

               BlockPos block3 = this.placeTarget;
               if (!this.canPlaceBed(block3)) {
                  this.placeTarget = new BlockPos(this.closestTarget.func_174791_d().func_72441_c(0.0D, 1.0D, -1.0D));
                  this.rotVar = 0.0F;
                  this.nowTop = false;
               }

               BlockPos block4 = this.placeTarget;
               if (!this.canPlaceBed(block4)) {
                  this.placeTarget = new BlockPos(this.closestTarget.func_174791_d().func_72441_c(0.0D, 2.0D, -1.0D));
                  this.rotVar = 0.0F;
                  this.nowTop = true;
               }

               BlockPos blockt1 = this.placeTarget;
               if (this.nowTop && !this.canPlaceBed(blockt1)) {
                  this.placeTarget = new BlockPos(this.closestTarget.func_174791_d().func_72441_c(-1.0D, 2.0D, 0.0D));
                  this.rotVar = -90.0F;
               }

               BlockPos blockt2 = this.placeTarget;
               if (this.nowTop && !this.canPlaceBed(blockt2)) {
                  this.placeTarget = new BlockPos(this.closestTarget.func_174791_d().func_72441_c(0.0D, 2.0D, 1.0D));
                  this.rotVar = 180.0F;
               }

               BlockPos blockt3 = this.placeTarget;
               if (this.nowTop && !this.canPlaceBed(blockt3)) {
                  this.placeTarget = new BlockPos(this.closestTarget.func_174791_d().func_72441_c(1.0D, 2.0D, 0.0D));
                  this.rotVar = 90.0F;
               }
            }

            mc.field_71441_e.field_147482_g.stream().filter((e) -> {
               return e instanceof TileEntityBed;
            }).filter((e) -> {
               return mc.field_71439_g.func_70011_f((double)e.func_174877_v().func_177958_n(), (double)e.func_174877_v().func_177956_o(), (double)e.func_174877_v().func_177952_p()) <= (double)this.range.getValue();
            }).sorted(Comparator.comparing((e) -> {
               return mc.field_71439_g.func_70011_f((double)e.func_174877_v().func_177958_n(), (double)e.func_174877_v().func_177956_o(), (double)e.func_174877_v().func_177952_p());
            })).forEach((bed) -> {
               if (mc.field_71439_g.field_71093_bK != 0) {
                  mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(bed.func_174877_v(), EnumFacing.UP, EnumHand.OFF_HAND, 0.0F, 0.0F, 0.0F));
               }

            });
            if (mc.field_71439_g.field_70173_aa % this.placedelay.getValue() == 0 && this.closestTarget != null) {
               this.findBeds();
               ++mc.field_71439_g.field_70173_aa;
               this.doDaMagic();
            }
         } catch (NullPointerException var8) {
            var8.printStackTrace();
         }

      }
   }

   private void doDaMagic() {
      if (this.diffXZ <= (double)this.range.getValue()) {
         for(int i = 0; i < 9 && this.bedSlot == -1; ++i) {
            ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack.func_77973_b() instanceof ItemBed) {
               this.bedSlot = i;
               if (i != -1) {
                  mc.field_71439_g.field_71071_by.field_70461_c = this.bedSlot;
               }
               break;
            }
         }

         this.bedSlot = -1;
         if (this.blocksPlaced == 0 && mc.field_71439_g.field_71071_by.func_70301_a(mc.field_71439_g.field_71071_by.field_70461_c).func_77973_b() instanceof ItemBed) {
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.START_SNEAKING));
            mc.field_71439_g.field_71174_a.func_147297_a(new Rotation(this.rotVar, 0.0F, mc.field_71439_g.field_70122_E));
            this.placeBlock(new BlockPos(this.placeTarget), EnumFacing.DOWN);
            mc.field_71439_g.field_71174_a.func_147297_a(new CPacketEntityAction(mc.field_71439_g, Action.STOP_SNEAKING));
            this.blocksPlaced = 1;
            this.nowTop = false;
         }

         this.blocksPlaced = 0;
      }

   }

   private void findBeds() {
      if ((mc.field_71462_r == null || !(mc.field_71462_r instanceof GuiContainer)) && mc.field_71439_g.field_71071_by.func_70301_a(0).func_77973_b() != Items.field_151104_aV) {
         for(int i = 9; i < 36; ++i) {
            if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == Items.field_151104_aV) {
               mc.field_71442_b.func_187098_a(mc.field_71439_g.field_71069_bz.field_75152_c, i, 0, ClickType.SWAP, mc.field_71439_g);
               break;
            }
         }
      }

   }

   private boolean canPlaceBed(BlockPos pos) {
      return (mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150350_a || mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150324_C) && mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos)).isEmpty();
   }

   private void findClosestTarget() {
      List<EntityPlayer> playerList = mc.field_71441_e.field_73010_i;
      this.closestTarget = null;
      Iterator var2 = playerList.iterator();

      while(var2.hasNext()) {
         EntityPlayer target = (EntityPlayer)var2.next();
         if (target != mc.field_71439_g && !Friends.isFriend(target.func_70005_c_()) && isLiving(target) && !(target.func_110143_aJ() <= 0.0F)) {
            if (this.closestTarget == null) {
               this.closestTarget = target;
            } else if (mc.field_71439_g.func_70032_d(target) < mc.field_71439_g.func_70032_d(this.closestTarget)) {
               this.closestTarget = target;
            }
         }
      }

   }

   private void placeBlock(BlockPos pos, EnumFacing side) {
      BlockPos neighbour = pos.func_177972_a(side);
      EnumFacing opposite = side.func_176734_d();
      Vec3d hitVec = (new Vec3d(neighbour)).func_72441_c(0.5D, 0.5D, 0.5D).func_178787_e((new Vec3d(opposite.func_176730_m())).func_186678_a(0.5D));
      mc.field_71442_b.func_187099_a(mc.field_71439_g, mc.field_71441_e, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
   }

   public static boolean isLiving(Entity e) {
      return e instanceof EntityLivingBase;
   }

   @SubscribeEvent
   public void render(RenderWorldLastEvent event) {
      if (this.placeTarget != null && mc.field_71441_e != null && this.closestTarget != null) {
         if (!this.placeesp.getValue()) {
            ;
         }
      }
   }
}
