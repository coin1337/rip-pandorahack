package com.pandora.api.mixin.mixins;

import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.render.NoRender;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LayerBipedArmor.class})
public class MixinLayerBipedArmor {
   @Inject(
      method = {"setModelSlotVisible"},
      at = {@At("HEAD")},
      cancellable = true
   )
   protected void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slotIn, CallbackInfo info) {
      if (ModuleManager.isModuleEnabled("NoRender") && ((NoRender)ModuleManager.getModuleByName("NoRender")).armor.getValue()) {
         info.cancel();
         switch(slotIn) {
         case HEAD:
            model.field_78116_c.field_78806_j = false;
            model.field_178720_f.field_78806_j = false;
         case CHEST:
            model.field_78115_e.field_78806_j = false;
            model.field_178723_h.field_78806_j = false;
            model.field_178724_i.field_78806_j = false;
         case LEGS:
            model.field_78115_e.field_78806_j = false;
            model.field_178721_j.field_78806_j = false;
            model.field_178722_k.field_78806_j = false;
         case FEET:
            model.field_178721_j.field_78806_j = false;
            model.field_178722_k.field_78806_j = false;
         }
      }

   }
}
