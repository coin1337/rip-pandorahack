package com.pandora.client.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pandora.api.event.events.PacketEvent;
import com.pandora.api.event.events.RenderEvent;
import com.pandora.api.settings.Setting;
import com.pandora.api.util.misc.MessageBus;
import com.pandora.api.util.players.friends.Friends;
import com.pandora.api.util.render.PandoraColor;
import com.pandora.api.util.render.PandoraTessellator;
import com.pandora.api.util.world.Timer;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.gui.ColorMain;
import com.pandora.client.module.modules.misc.AutoGG;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

public class AutoCrystal extends Module {
   Setting.Boolean breakCrystal;
   Setting.Boolean antiWeakness;
   Setting.Boolean placeCrystal;
   Setting.Boolean autoSwitch;
   Setting.Boolean raytrace;
   Setting.Boolean rotate;
   Setting.Boolean spoofRotations;
   Setting.Boolean chat;
   Setting.Boolean showDamage;
   Setting.Boolean multiPlace;
   Setting.Boolean antiSuicide;
   Setting.Boolean endCrystalMode;
   Setting.Boolean cancelCrystal;
   Setting.Boolean noGapSwitch;
   Setting.Integer facePlaceValue;
   Setting.Integer attackSpeed;
   Setting.Integer antiSuicideValue;
   Setting.Double maxSelfDmg;
   Setting.Double wallsRange;
   Setting.Double minDmg;
   Setting.Double minBreakDmg;
   Setting.Double enemyRange;
   public static Setting.Double placeRange;
   public static Setting.Double breakRange;
   Setting.Mode handBreak;
   Setting.Mode breakMode;
   Setting.Mode hudDisplay;
   Setting.ColorSetting color;
   private boolean switchCooldown = false;
   private boolean isAttacking = false;
   private boolean isPlacing = false;
   private boolean isBreaking = false;
   public boolean isActive = false;
   public static boolean stopAC = false;
   private static boolean togglePitch = false;
   private int oldSlot = -1;
   private int newSlot;
   private int waitCounter;
   private Entity renderEnt;
   private BlockPos render;
   private final ArrayList<BlockPos> PlacedCrystals = new ArrayList();
   private EnumFacing enumFacing;
   Timer timer = new Timer();
   private static boolean isSpoofingAngles;
   private static double yaw;
   private static double pitch;
   @EventHandler
   private final Listener<PacketEvent.Send> packetSendListener = new Listener((event) -> {
      Packet packet = event.getPacket();
      if (packet instanceof CPacketPlayer && this.spoofRotations.getValue() && isSpoofingAngles) {
         ((CPacketPlayer)packet).field_149476_e = (float)yaw;
         ((CPacketPlayer)packet).field_149473_f = (float)pitch;
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener<PacketEvent.Receive> packetReceiveListener = new Listener((event) -> {
      if (event.getPacket() instanceof SPacketSoundEffect) {
         SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
         if (packet.func_186977_b() == SoundCategory.BLOCKS && packet.func_186978_a() == SoundEvents.field_187539_bB) {
            Iterator var2 = Minecraft.func_71410_x().field_71441_e.field_72996_f.iterator();

            while(var2.hasNext()) {
               Entity e = (Entity)var2.next();
               if (e instanceof EntityEnderCrystal && e.func_70011_f(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f()) <= 6.0D) {
                  e.func_70106_y();
               }
            }
         }
      }

   }, new Predicate[0]);

   public AutoCrystal() {
      super("AutoCrystalGS", Module.Category.Combat);
   }

   public void setup() {
      ArrayList<String> hands = new ArrayList();
      hands.add("Main");
      hands.add("Offhand");
      hands.add("Both");
      ArrayList<String> breakModes = new ArrayList();
      breakModes.add("All");
      breakModes.add("Smart");
      breakModes.add("Own");
      ArrayList<String> hudModes = new ArrayList();
      hudModes.add("Mode");
      hudModes.add("None");
      this.breakMode = this.registerMode("Target", "Target", breakModes, "All");
      this.handBreak = this.registerMode("Hand", "Hand", hands, "Main");
      this.breakCrystal = this.registerBoolean("Break", "Break", true);
      this.placeCrystal = this.registerBoolean("Place", "Place", true);
      this.attackSpeed = this.registerInteger("Attack Speed", "AttackSpeed", 16, 0, 20);
      breakRange = this.registerDouble("Hit Range", "HitRange", 4.4D, 0.0D, 10.0D);
      placeRange = this.registerDouble("Place Range", "PlaceRange", 4.4D, 0.0D, 6.0D);
      this.wallsRange = this.registerDouble("Walls Range", "WallsRange", 3.5D, 0.0D, 10.0D);
      this.enemyRange = this.registerDouble("Enemy Range", "EnemyRange", 6.0D, 0.0D, 16.0D);
      this.antiWeakness = this.registerBoolean("Anti Weakness", "AntiWeakness", true);
      this.antiSuicide = this.registerBoolean("Anti Suicide", "AntiSuicide", true);
      this.antiSuicideValue = this.registerInteger("Min Health", "MinHealth", 14, 1, 36);
      this.autoSwitch = this.registerBoolean("Switch", "Switch", true);
      this.noGapSwitch = this.registerBoolean("No Gap Switch", "NoGapSwitch", false);
      this.multiPlace = this.registerBoolean("Multi Place", "MultiPlace", false);
      this.endCrystalMode = this.registerBoolean("1.13 Place", "1.13Place", false);
      this.cancelCrystal = this.registerBoolean("Cancel Crystal", "CancelCrystal", false);
      this.minDmg = this.registerDouble("Min Damage", "MinDamage", 5.0D, 0.0D, 36.0D);
      this.minBreakDmg = this.registerDouble("Min Break Dmg", "MinBreakDmg", 5.0D, 0.0D, 36.0D);
      this.maxSelfDmg = this.registerDouble("Max Self Dmg", "MaxSelfDmg", 10.0D, 1.0D, 36.0D);
      this.facePlaceValue = this.registerInteger("FacePlace HP", "FacePlaceHP", 8, 0, 36);
      this.rotate = this.registerBoolean("Rotate", "Rotate", true);
      this.spoofRotations = this.registerBoolean("Spoof Angles", "SpoofAngles", true);
      this.raytrace = this.registerBoolean("Raytrace", "Raytrace", false);
      this.showDamage = this.registerBoolean("Render Dmg", "RenderDmg", true);
      this.chat = this.registerBoolean("Chat Msgs", "ChatMsgs", true);
      this.hudDisplay = this.registerMode("HUD", "HUD", hudModes, "Mode");
      this.color = this.registerColor("Color", "Color", new PandoraColor(0, 255, 0, 50));
   }

   public void onUpdate() {
      if (mc.field_71439_g != null && mc.field_71441_e != null && !mc.field_71439_g.field_70128_L) {
         if (!stopAC) {
            if (!this.antiSuicide.getValue() || !(mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj() <= (float)this.antiSuicideValue.getValue())) {
               this.isActive = false;
               this.isBreaking = false;
               this.isPlacing = false;
               EntityEnderCrystal crystal = (EntityEnderCrystal)mc.field_71441_e.field_72996_f.stream().filter((entityx) -> {
                  return entityx instanceof EntityEnderCrystal;
               }).filter((e) -> {
                  return (double)mc.field_71439_g.func_70032_d(e) <= breakRange.getValue();
               }).filter((e) -> {
                  return this.crystalCheck(e);
               }).map((entityx) -> {
                  return (EntityEnderCrystal)entityx;
               }).min(Comparator.comparing((c) -> {
                  return mc.field_71439_g.func_70032_d(c);
               })).orElse((Object)null);
               int crystalSlot;
               if (this.breakCrystal.getValue() && crystal != null) {
                  if (!mc.field_71439_g.func_70685_l(crystal) && (double)mc.field_71439_g.func_70032_d(crystal) > this.wallsRange.getValue()) {
                     return;
                  }

                  if (this.antiWeakness.getValue() && mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
                     if (!this.isAttacking) {
                        this.oldSlot = mc.field_71439_g.field_71071_by.field_70461_c;
                        this.isAttacking = true;
                     }

                     this.newSlot = -1;

                     for(crystalSlot = 0; crystalSlot < 9; ++crystalSlot) {
                        ItemStack stack = mc.field_71439_g.field_71071_by.func_70301_a(crystalSlot);
                        if (stack != ItemStack.field_190927_a) {
                           if (stack.func_77973_b() instanceof ItemSword) {
                              this.newSlot = crystalSlot;
                              break;
                           }

                           if (stack.func_77973_b() instanceof ItemTool) {
                              this.newSlot = crystalSlot;
                              break;
                           }
                        }
                     }

                     if (this.newSlot != -1) {
                        mc.field_71439_g.field_71071_by.field_70461_c = this.newSlot;
                        this.switchCooldown = true;
                     }
                  }

                  if (this.timer.getTimePassed() / 50L >= (long)(20 - this.attackSpeed.getValue())) {
                     this.timer.reset();
                     this.isActive = true;
                     this.isBreaking = true;
                     if (this.rotate.getValue()) {
                        this.lookAtPacket(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v, mc.field_71439_g);
                     }

                     mc.field_71442_b.func_78764_a(mc.field_71439_g, crystal);
                     if (this.handBreak.getValue().equalsIgnoreCase("Both") && mc.field_71439_g.func_184592_cb() != null) {
                        mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                        mc.field_71439_g.func_184609_a(EnumHand.OFF_HAND);
                     } else if (this.handBreak.getValue().equalsIgnoreCase("Offhand") && mc.field_71439_g.func_184592_cb() != null) {
                        mc.field_71439_g.func_184609_a(EnumHand.OFF_HAND);
                     } else {
                        mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                     }

                     if (this.cancelCrystal.getValue()) {
                        crystal.func_70106_y();
                        mc.field_71441_e.func_73022_a();
                        mc.field_71441_e.func_72910_y();
                     }

                     this.isActive = false;
                     this.isBreaking = false;
                  }

                  if (!this.multiPlace.getValue()) {
                     return;
                  }
               } else {
                  resetRotation();
                  if (this.oldSlot != -1) {
                     mc.field_71439_g.field_71071_by.field_70461_c = this.oldSlot;
                     this.oldSlot = -1;
                  }

                  this.isAttacking = false;
                  this.isActive = false;
                  this.isBreaking = false;
               }

               crystalSlot = mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP ? mc.field_71439_g.field_71071_by.field_70461_c : -1;
               if (crystalSlot == -1) {
                  for(int l = 0; l < 9; ++l) {
                     if (mc.field_71439_g.field_71071_by.func_70301_a(l).func_77973_b() == Items.field_185158_cP && mc.field_71439_g.func_184586_b(EnumHand.OFF_HAND).func_77973_b() != Items.field_185158_cP) {
                        crystalSlot = l;
                        break;
                     }
                  }
               }

               boolean offhand = false;
               if (mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
                  offhand = true;
               } else if (crystalSlot == -1) {
                  return;
               }

               List<BlockPos> blocks = this.findCrystalBlocks();
               List<Entity> entities = new ArrayList();
               entities.addAll((Collection)mc.field_71441_e.field_73010_i.stream().filter((entityPlayer) -> {
                  return !Friends.isFriend(entityPlayer.func_70005_c_());
               }).sorted(Comparator.comparing((e) -> {
                  return mc.field_71439_g.func_70032_d(e);
               })).collect(Collectors.toList()));
               BlockPos q = null;
               double damage = 0.5D;
               Iterator var9 = entities.iterator();

               label218:
               while(true) {
                  EntityPlayer entity;
                  do {
                     do {
                        if (!var9.hasNext()) {
                           if (damage == 0.5D) {
                              this.render = null;
                              this.renderEnt = null;
                              resetRotation();
                              return;
                           }

                           this.render = q;
                           if (this.placeCrystal.getValue()) {
                              if (!offhand && mc.field_71439_g.field_71071_by.field_70461_c != crystalSlot) {
                                 if (this.autoSwitch.getValue() && (this.noGapSwitch.getValue() && mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_151153_ao || !this.noGapSwitch.getValue())) {
                                    mc.field_71439_g.field_71071_by.field_70461_c = crystalSlot;
                                    resetRotation();
                                    this.switchCooldown = true;
                                 }

                                 return;
                              }

                              if (this.rotate.getValue()) {
                                 this.lookAtPacket((double)q.func_177958_n() + 0.5D, (double)q.func_177956_o() - 0.5D, (double)q.func_177952_p() + 0.5D, mc.field_71439_g);
                              }

                              RayTraceResult result = mc.field_71441_e.func_72933_a(new Vec3d(mc.field_71439_g.field_70165_t, mc.field_71439_g.field_70163_u + (double)mc.field_71439_g.func_70047_e(), mc.field_71439_g.field_70161_v), new Vec3d((double)q.func_177958_n() + 0.5D, (double)q.func_177956_o() - 0.5D, (double)q.func_177952_p() + 0.5D));
                              if (this.raytrace.getValue()) {
                                 if (result == null || result.field_178784_b == null) {
                                    q = null;
                                    this.enumFacing = null;
                                    this.render = null;
                                    resetRotation();
                                    this.isActive = false;
                                    this.isPlacing = false;
                                    return;
                                 }

                                 this.enumFacing = result.field_178784_b;
                              }

                              if (this.switchCooldown) {
                                 this.switchCooldown = false;
                                 return;
                              }

                              if (q != null && mc.field_71439_g != null) {
                                 this.isActive = true;
                                 this.isPlacing = true;
                                 if (this.raytrace.getValue() && this.enumFacing != null) {
                                    mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(q, this.enumFacing, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
                                 } else if (q.func_177956_o() == 255) {
                                    mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(q, EnumFacing.DOWN, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
                                 } else {
                                    mc.field_71439_g.field_71174_a.func_147297_a(new CPacketPlayerTryUseItemOnBlock(q, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
                                 }

                                 mc.field_71439_g.field_71174_a.func_147297_a(new CPacketAnimation(EnumHand.MAIN_HAND));
                                 this.PlacedCrystals.add(q);
                                 if (ModuleManager.isModuleEnabled("AutoGG")) {
                                    AutoGG.INSTANCE.addTargetedPlayer(this.renderEnt.func_70005_c_());
                                 }
                              }

                              if (isSpoofingAngles) {
                                 EntityPlayerSP var10000;
                                 if (togglePitch) {
                                    var10000 = mc.field_71439_g;
                                    var10000.field_70125_A = (float)((double)var10000.field_70125_A + 4.0E-4D);
                                    togglePitch = false;
                                 } else {
                                    var10000 = mc.field_71439_g;
                                    var10000.field_70125_A = (float)((double)var10000.field_70125_A - 4.0E-4D);
                                    togglePitch = true;
                                 }
                              }

                              return;
                           }
                        }

                        entity = (EntityPlayer)var9.next();
                     } while(entity == mc.field_71439_g);
                  } while(entity.func_110143_aJ() <= 0.0F);

                  Iterator var11 = blocks.iterator();

                  while(true) {
                     BlockPos blockPos;
                     double d;
                     double targetDamage;
                     float targetHealth;
                     do {
                        do {
                           double x;
                           double y;
                           double z;
                           do {
                              if (!var11.hasNext()) {
                                 continue label218;
                              }

                              blockPos = (BlockPos)var11.next();
                              entity.func_174818_b(blockPos);
                              x = (double)blockPos.func_177958_n() + 0.0D;
                              y = (double)blockPos.func_177956_o() + 1.0D;
                              z = (double)blockPos.func_177952_p() + 0.0D;
                           } while(entity.func_70092_e(x, y, z) >= this.enemyRange.getValue() * this.enemyRange.getValue());

                           d = (double)calculateDamage((double)blockPos.func_177958_n() + 0.5D, (double)(blockPos.func_177956_o() + 1), (double)blockPos.func_177952_p() + 0.5D, entity);
                        } while(d <= damage);

                        targetDamage = (double)calculateDamage((double)blockPos.func_177958_n() + 0.5D, (double)(blockPos.func_177956_o() + 1), (double)blockPos.func_177952_p() + 0.5D, entity);
                        targetHealth = entity.func_110143_aJ() + entity.func_110139_bj();
                     } while(targetDamage < this.minDmg.getValue() && targetHealth > (float)this.facePlaceValue.getValue());

                     double self = (double)calculateDamage((double)blockPos.func_177958_n() + 0.5D, (double)(blockPos.func_177956_o() + 1), (double)blockPos.func_177952_p() + 0.5D, mc.field_71439_g);
                     if (!(self >= this.maxSelfDmg.getValue()) && !(self >= (double)(mc.field_71439_g.func_110143_aJ() + mc.field_71439_g.func_110139_bj()))) {
                        damage = d;
                        q = blockPos;
                        this.renderEnt = entity;
                     }
                  }
               }
            }
         }
      } else {
         this.disable();
      }
   }

   public void onWorldRender(RenderEvent event) {
      if (this.render != null) {
         PandoraTessellator.drawBox(this.render, 1.0D, new PandoraColor(this.color.getValue(), 50), 63);
         PandoraTessellator.drawBoundingBox(this.render, 1.0D, 1.0F, new PandoraColor(this.color.getValue(), 255));
      }

      if (this.showDamage.getValue() && this.render != null && this.renderEnt != null) {
         double d = (double)calculateDamage((double)this.render.func_177958_n() + 0.5D, (double)(this.render.func_177956_o() + 1), (double)this.render.func_177952_p() + 0.5D, this.renderEnt);
         String[] damageText = new String[]{(Math.floor(d) == d ? (int)d : String.format("%.1f", d)) + ""};
         PandoraTessellator.drawNametag((double)this.render.func_177958_n() + 0.5D, (double)this.render.func_177956_o() + 0.5D, (double)this.render.func_177952_p() + 0.5D, damageText, new PandoraColor(255, 255, 255), 1);
      }

   }

   private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
      double[] v = calculateLookAt(px, py, pz, me);
      setYawAndPitch((float)v[0], (float)v[1]);
   }

   private boolean crystalCheck(Entity crystal) {
      if (!(crystal instanceof EntityEnderCrystal)) {
         return false;
      } else if (this.breakMode.getValue().equalsIgnoreCase("All")) {
         return true;
      } else {
         if (this.breakMode.getValue().equalsIgnoreCase("Own")) {
            Iterator var2 = (new ArrayList(this.PlacedCrystals)).iterator();

            while(var2.hasNext()) {
               BlockPos pos = (BlockPos)var2.next();
               if (pos != null && pos.func_185332_f((int)crystal.field_70165_t, (int)crystal.field_70163_u, (int)crystal.field_70161_v) <= 3.0D) {
                  return true;
               }
            }
         } else if (this.breakMode.getValue().equalsIgnoreCase("Smart")) {
            EntityLivingBase target = this.renderEnt != null ? (EntityLivingBase)this.renderEnt : this.GetNearTarget(crystal);
            if (target != null && target != mc.field_71439_g) {
               float targetDmg = calculateDamage(crystal.field_70165_t + 0.5D, crystal.field_70163_u + 1.0D, crystal.field_70161_v + 0.5D, target);
               return (double)targetDmg >= this.minBreakDmg.getValue() || (double)targetDmg > this.minBreakDmg.getValue() && target.func_110143_aJ() > (float)this.facePlaceValue.getValue();
            }

            return false;
         }

         return false;
      }
   }

   private boolean validTarget(Entity entity) {
      if (entity == null) {
         return false;
      } else if (!(entity instanceof EntityLivingBase)) {
         return false;
      } else if (Friends.isFriend(entity.func_70005_c_())) {
         return false;
      } else if (!entity.field_70128_L && !(((EntityLivingBase)entity).func_110143_aJ() <= 0.0F)) {
         if (entity instanceof EntityPlayer) {
            return entity != mc.field_71439_g;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private EntityLivingBase GetNearTarget(Entity distanceTarget) {
      return (EntityLivingBase)mc.field_71441_e.field_72996_f.stream().filter((entity) -> {
         return this.validTarget(entity);
      }).map((entity) -> {
         return (EntityLivingBase)entity;
      }).min(Comparator.comparing((entity) -> {
         return distanceTarget.func_70032_d(entity);
      })).orElse((Object)null);
   }

   public boolean canPlaceCrystal(BlockPos blockPos) {
      BlockPos boost = blockPos.func_177982_a(0, 1, 0);
      BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
      if (!this.endCrystalMode.getValue()) {
         return (mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && mc.field_71441_e.func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
      } else {
         return (mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h || mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z) && mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
      }
   }

   public static BlockPos getPlayerPos() {
      return new BlockPos(Math.floor(mc.field_71439_g.field_70165_t), Math.floor(mc.field_71439_g.field_70163_u), Math.floor(mc.field_71439_g.field_70161_v));
   }

   private List<BlockPos> findCrystalBlocks() {
      NonNullList<BlockPos> positions = NonNullList.func_191196_a();
      positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)placeRange.getValue(), (int)placeRange.getValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
      return positions;
   }

   public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
      List<BlockPos> circleblocks = new ArrayList();
      int cx = loc.func_177958_n();
      int cy = loc.func_177956_o();
      int cz = loc.func_177952_p();

      for(int x = cx - (int)r; (float)x <= (float)cx + r; ++x) {
         for(int z = cz - (int)r; (float)z <= (float)cz + r; ++z) {
            for(int y = sphere ? cy - (int)r : cy; (float)y < (sphere ? (float)cy + r : (float)(cy + h)); ++y) {
               double dist = (double)((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));
               if (dist < (double)(r * r) && (!hollow || !(dist < (double)((r - 1.0F) * (r - 1.0F))))) {
                  BlockPos l = new BlockPos(x, y + plus_y, z);
                  circleblocks.add(l);
               }
            }
         }
      }

      return circleblocks;
   }

   public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
      float doubleExplosionSize = 12.0F;
      double distancedsize = entity.func_70011_f(posX, posY, posZ) / (double)doubleExplosionSize;
      Vec3d vec3d = new Vec3d(posX, posY, posZ);
      double blockDensity = (double)entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
      double v = (1.0D - distancedsize) * blockDensity;
      float damage = (float)((int)((v * v + v) / 2.0D * 7.0D * (double)doubleExplosionSize + 1.0D));
      double finald = 1.0D;
      if (entity instanceof EntityLivingBase) {
         finald = (double)getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion(mc.field_71441_e, (Entity)null, posX, posY, posZ, 6.0F, false, true));
      }

      return (float)finald;
   }

   public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
      if (entity instanceof EntityPlayer) {
         EntityPlayer ep = (EntityPlayer)entity;
         DamageSource ds = DamageSource.func_94539_a(explosion);
         damage = CombatRules.func_189427_a(damage, (float)ep.func_70658_aO(), (float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
         int k = EnchantmentHelper.func_77508_a(ep.func_184193_aE(), ds);
         float f = MathHelper.func_76131_a((float)k, 0.0F, 20.0F);
         damage *= 1.0F - f / 25.0F;
         if (entity.func_70644_a(Potion.func_188412_a(11))) {
            damage -= damage / 4.0F;
         }

         damage = Math.max(damage, 0.0F);
         return damage;
      } else {
         damage = CombatRules.func_189427_a(damage, (float)entity.func_70658_aO(), (float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
         return damage;
      }
   }

   private static float getDamageMultiplied(float damage) {
      int diff = mc.field_71441_e.func_175659_aa().func_151525_a();
      return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
   }

   private static void setYawAndPitch(float yaw1, float pitch1) {
      yaw = (double)yaw1;
      pitch = (double)pitch1;
      isSpoofingAngles = true;
   }

   private static void resetRotation() {
      if (isSpoofingAngles) {
         yaw = (double)mc.field_71439_g.field_70177_z;
         pitch = (double)mc.field_71439_g.field_70125_A;
         isSpoofingAngles = false;
      }

   }

   public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
      double dirx = me.field_70165_t - px;
      double diry = me.field_70163_u - py;
      double dirz = me.field_70161_v - pz;
      double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
      dirx /= len;
      diry /= len;
      dirz /= len;
      double pitch = Math.asin(diry);
      double yaw = Math.atan2(dirz, dirx);
      pitch = pitch * 180.0D / 3.141592653589793D;
      yaw = yaw * 180.0D / 3.141592653589793D;
      yaw += 90.0D;
      return new double[]{yaw, pitch};
   }

   public void onEnable() {
      PandoraMod.EVENT_BUS.subscribe((Object)this);
      this.PlacedCrystals.clear();
      this.isActive = false;
      this.isPlacing = false;
      this.isBreaking = false;
      if (this.chat.getValue() && mc.field_71439_g != null) {
         MessageBus.sendClientPrefixMessage(ColorMain.getEnabledColor() + "AutoCrystal turned ON!");
      }

   }

   public void onDisable() {
      PandoraMod.EVENT_BUS.unsubscribe((Object)this);
      this.render = null;
      this.renderEnt = null;
      resetRotation();
      this.PlacedCrystals.clear();
      this.isActive = false;
      this.isPlacing = false;
      this.isBreaking = false;
      if (this.chat.getValue()) {
         MessageBus.sendClientPrefixMessage(ColorMain.getDisabledColor() + "AutoCrystal turned OFF!");
      }

   }

   public String getHudInfo() {
      String t = "";
      if (this.hudDisplay.getValue().equalsIgnoreCase("Mode")) {
         if (this.breakMode.getValue().equalsIgnoreCase("All")) {
            t = "[" + ChatFormatting.WHITE + "All" + ChatFormatting.GRAY + "]";
         }

         if (this.breakMode.getValue().equalsIgnoreCase("Smart")) {
            t = "[" + ChatFormatting.WHITE + "Smart" + ChatFormatting.GRAY + "]";
         }

         if (this.breakMode.getValue().equalsIgnoreCase("Own")) {
            t = "[" + ChatFormatting.WHITE + "Own" + ChatFormatting.GRAY + "]";
         }
      }

      if (this.hudDisplay.getValue().equalsIgnoreCase("None")) {
         t = "";
      }

      return t;
   }
}
