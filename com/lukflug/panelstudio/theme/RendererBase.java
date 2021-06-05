package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.Context;
import java.awt.Color;

public abstract class RendererBase implements Renderer {
   protected final int height;
   protected final int offset;
   protected final int border;
   protected ColorScheme scheme = null;

   public RendererBase(int height, int offset, int border) {
      this.height = height;
      this.offset = offset;
      this.border = border;
   }

   public int getHeight() {
      return this.height;
   }

   public int getOffset() {
      return this.offset;
   }

   public int getBorder() {
      return this.border;
   }

   public void renderTitle(Context context, String text, boolean focus) {
      this.renderTitle(context, text, focus, false);
   }

   public void renderTitle(Context context, String text, boolean focus, boolean active) {
      this.renderRect(context, text, focus, active, context.getRect(), true);
   }

   public void renderTitle(Context context, String text, boolean focus, boolean active, boolean open) {
      this.renderTitle(context, text, focus, active);
   }

   public Color getFontColor(boolean focus) {
      return this.getColorScheme().getFontColor();
   }

   public void overrideColorScheme(ColorScheme scheme) {
      this.scheme = scheme;
   }

   public void restoreColorScheme() {
      this.scheme = null;
   }

   protected ColorScheme getColorScheme() {
      return this.scheme == null ? this.getDefaultColorScheme() : this.scheme;
   }
}
