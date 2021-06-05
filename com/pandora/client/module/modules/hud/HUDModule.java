package com.pandora.client.module.modules.hud;

import com.lukflug.panelstudio.FixedComponent;
import com.pandora.client.PandoraMod;
import com.pandora.client.module.Module;
import java.awt.Point;

public class HUDModule extends Module {
   protected final FixedComponent component;
   protected final Point position;

   public HUDModule(FixedComponent component, Point defaultPos) {
      super(component.getTitle(), Module.Category.HUD);
      this.component = component;
      this.position = defaultPos;
   }

   public FixedComponent getComponent() {
      return this.component;
   }

   public void resetPosition() {
      this.component.setPosition(PandoraMod.getInstance().clickGUI, this.position);
   }
}
