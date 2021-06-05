package com.lukflug.panelstudio.theme;

import com.lukflug.panelstudio.Context;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class ClearTheme implements Theme {
   protected ColorScheme scheme;
   protected Renderer componentRenderer;
   protected Renderer panelRenderer;
   protected final boolean gradient;

   public ClearTheme(ColorScheme scheme, boolean gradient, int height, int border) {
      this.scheme = scheme;
      this.gradient = gradient;
      this.panelRenderer = new ClearTheme.ComponentRenderer(true, height, border);
      this.componentRenderer = new ClearTheme.ComponentRenderer(false, height, border);
   }

   public Renderer getPanelRenderer() {
      return this.panelRenderer;
   }

   public Renderer getContainerRenderer() {
      return this.componentRenderer;
   }

   public Renderer getComponentRenderer() {
      return this.componentRenderer;
   }

   protected class ComponentRenderer extends RendererBase {
      protected final boolean panel;

      public ComponentRenderer(boolean panel, int height, int border) {
         super(height + 2 * border, border, 0);
         this.panel = panel;
      }

      public void renderTitle(Context context, String text, boolean focus, boolean active) {
         if (this.panel) {
            super.renderTitle(context, text, focus, active);
         } else {
            Color overlayColor;
            if (context.isHovered()) {
               overlayColor = new Color(0, 0, 0, 64);
            } else {
               overlayColor = new Color(0, 0, 0, 0);
            }

            context.getInterface().fillRect(context.getRect(), overlayColor, overlayColor, overlayColor, overlayColor);
            Color fontColor = this.getFontColor(focus);
            if (active) {
               fontColor = this.getMainColor(focus, true);
            }

            Point stringPos = new Point(context.getPos());
            stringPos.translate(0, this.getOffset());
            context.getInterface().drawString(stringPos, text, fontColor);
         }

      }

      public void renderTitle(Context context, String text, boolean focus, boolean active, boolean open) {
         super.renderTitle(context, text, focus, active, open);
         if (!this.panel) {
            Color color = this.getFontColor(active);
            Point p1;
            Point p2;
            Point p3;
            if (open) {
               p3 = new Point(context.getPos().x + context.getSize().width - 2, context.getPos().y + context.getSize().height / 4);
               p2 = new Point(context.getPos().x + context.getSize().width - context.getSize().height / 2, context.getPos().y + context.getSize().height * 3 / 4);
               p1 = new Point(context.getPos().x + context.getSize().width - context.getSize().height + 2, context.getPos().y + context.getSize().height / 4);
            } else {
               p3 = new Point(context.getPos().x + context.getSize().width - context.getSize().height * 3 / 4, context.getPos().y + 2);
               p2 = new Point(context.getPos().x + context.getSize().width - context.getSize().height / 4, context.getPos().y + context.getSize().height / 2);
               p1 = new Point(context.getPos().x + context.getSize().width - context.getSize().height * 3 / 4, context.getPos().y + context.getSize().height - 2);
            }

            context.getInterface().fillTriangle(p1, p2, p3, color, color, color);
         }

      }

      public void renderRect(Context context, String text, boolean focus, boolean active, Rectangle rectangle, boolean overlay) {
         Color overlayColor;
         if (this.panel || active) {
            overlayColor = this.getMainColor(focus, true);
            Color color2 = this.getBackgroundColor(focus);
            if (ClearTheme.this.gradient && this.panel) {
               context.getInterface().fillRect(rectangle, overlayColor, overlayColor, color2, color2);
            } else {
               context.getInterface().fillRect(rectangle, overlayColor, overlayColor, overlayColor, overlayColor);
            }
         }

         if (!this.panel && overlay) {
            if (context.isHovered()) {
               overlayColor = new Color(0, 0, 0, 64);
            } else {
               overlayColor = new Color(0, 0, 0, 0);
            }

            context.getInterface().fillRect(context.getRect(), overlayColor, overlayColor, overlayColor, overlayColor);
         }

         Point stringPos = new Point(rectangle.getLocation());
         stringPos.translate(0, this.getOffset());
         context.getInterface().drawString(stringPos, text, this.getFontColor(focus));
      }

      public void renderBackground(Context context, boolean focus) {
         if (this.panel) {
            Color color = this.getBackgroundColor(focus);
            context.getInterface().fillRect(context.getRect(), color, color, color, color);
         }

      }

      public void renderBorder(Context context, boolean focus, boolean active, boolean open) {
      }

      public Color getMainColor(boolean focus, boolean active) {
         return active ? this.getColorScheme().getActiveColor() : new Color(0, 0, 0, 0);
      }

      public Color getBackgroundColor(boolean focus) {
         Color color = this.getColorScheme().getBackgroundColor();
         return new Color(color.getRed(), color.getGreen(), color.getBlue(), this.getColorScheme().getOpacity());
      }

      public ColorScheme getDefaultColorScheme() {
         return ClearTheme.this.scheme;
      }
   }
}
