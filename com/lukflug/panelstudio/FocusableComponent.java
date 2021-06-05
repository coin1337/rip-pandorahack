package com.lukflug.panelstudio;

import com.lukflug.panelstudio.theme.Renderer;

public class FocusableComponent implements Component {
   protected String title;
   protected Renderer renderer;
   private boolean focus = false;

   public FocusableComponent(String title, Renderer renderer) {
      this.title = title;
      this.renderer = renderer;
   }

   public String getTitle() {
      return this.title;
   }

   public void render(Context context) {
      context.setHeight(this.renderer.getHeight());
   }

   public void handleKey(Context context, int scancode) {
      context.setHeight(this.renderer.getHeight());
   }

   public void handleButton(Context context, int button) {
      context.setHeight(this.renderer.getHeight());
      this.updateFocus(context, button);
   }

   public void getHeight(Context context) {
      context.setHeight(this.renderer.getHeight());
   }

   public void handleScroll(Context context, int diff) {
      context.setHeight(this.renderer.getHeight());
   }

   public void exit(Context context) {
      context.setHeight(this.renderer.getHeight());
   }

   public boolean hasFocus(Context context) {
      return context.hasFocus() && this.focus;
   }

   public void releaseFocus() {
      this.focus = false;
   }

   protected void updateFocus(Context context, int button) {
      if (context.getInterface().getButton(button)) {
         this.focus = context.isHovered();
         this.handleFocus(context, this.focus && context.hasFocus());
      }

   }

   protected void handleFocus(Context context, boolean focus) {
   }
}
