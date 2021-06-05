package com.lukflug.panelstudio.tabgui;

import com.lukflug.panelstudio.Animation;
import com.lukflug.panelstudio.Context;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class TabGUIContainer implements TabGUIComponent {
   protected String title;
   protected TabGUIRenderer renderer;
   protected List<TabGUIComponent> components;
   protected boolean childOpen = false;
   protected int selected = 0;
   protected Animation selectedAnimation = null;

   public TabGUIContainer(String title, TabGUIRenderer renderer, Animation animation) {
      this.title = title;
      this.renderer = renderer;
      this.components = new ArrayList();
      if (animation != null) {
         animation.initValue((double)this.selected);
         this.selectedAnimation = animation;
      }

   }

   public void addComponent(TabGUIComponent component) {
      this.components.add(component);
   }

   public String getTitle() {
      return this.title;
   }

   public void render(Context context) {
      this.getHeight(context);
      int offset = this.selected * this.renderer.getHeight();
      if (this.selectedAnimation != null) {
         offset = (int)(this.selectedAnimation.getValue() * (double)this.renderer.getHeight());
      }

      this.renderer.renderBackground(context, offset, this.renderer.getHeight());

      for(int i = 0; i < this.components.size(); ++i) {
         TabGUIComponent component = (TabGUIComponent)this.components.get(i);
         this.renderer.renderCaption(context, component.getTitle(), i, this.renderer.getHeight(), component.isActive());
      }

      if (this.childOpen) {
         ((TabGUIComponent)this.components.get(this.selected)).render(this.getSubContext(context));
      }

   }

   public void handleButton(Context context, int button) {
      this.getHeight(context);
   }

   public void handleKey(Context context, int scancode) {
      this.getHeight(context);
      if (this.renderer.isEscapeKey(scancode)) {
         this.childOpen = false;
      } else if (!this.childOpen) {
         if (this.renderer.isUpKey(scancode)) {
            if (--this.selected < 0) {
               this.selected = this.components.size() - 1;
            }

            if (this.selectedAnimation != null) {
               this.selectedAnimation.setValue((double)this.selected);
            }
         } else if (this.renderer.isDownKey(scancode)) {
            if (++this.selected >= this.components.size()) {
               this.selected = 0;
            }

            if (this.selectedAnimation != null) {
               this.selectedAnimation.setValue((double)this.selected);
            }
         } else if (this.renderer.isSelectKey(scancode) && ((TabGUIComponent)this.components.get(this.selected)).select()) {
            this.childOpen = true;
         }
      } else {
         ((TabGUIComponent)this.components.get(this.selected)).handleKey(this.getSubContext(context), scancode);
      }

   }

   public void handleScroll(Context context, int diff) {
      this.getHeight(context);
   }

   public void getHeight(Context context) {
      context.setHeight(this.renderer.getHeight() * this.components.size());
   }

   public void exit(Context context) {
      this.getHeight(context);
   }

   public boolean isActive() {
      return false;
   }

   public boolean select() {
      return true;
   }

   protected Context getSubContext(Context context) {
      Point p = context.getPos();
      p.translate(context.getSize().width + this.renderer.getBorder(), this.selected * this.renderer.getHeight());
      return new Context(context.getInterface(), context.getSize().width, p, context.hasFocus(), context.onTop());
   }

   public void releaseFocus() {
   }
}
