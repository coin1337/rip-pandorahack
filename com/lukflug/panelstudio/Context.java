package com.lukflug.panelstudio;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public final class Context {
   private Interface inter;
   private Dimension size;
   private Point position;
   private boolean focus;
   private boolean onTop;
   private boolean focusRequested = false;

   public Context(Context context, int border, int offset, boolean focus) {
      this.inter = context.getInterface();
      this.size = new Dimension(context.getSize().width - border * 2, 0);
      this.position = new Point(context.getPos());
      this.position.translate(border, offset);
      this.focus = context.hasFocus() && focus;
      this.onTop = context.onTop();
   }

   public Context(Context context, int border, int offset, boolean focus, boolean onTop) {
      this.inter = context.getInterface();
      this.size = new Dimension(context.getSize().width - border * 2, 0);
      this.position = new Point(context.getPos());
      this.position.translate(border, offset);
      this.focus = context.hasFocus() && focus;
      this.onTop = context.onTop() && onTop;
   }

   public Context(Interface inter, int width, Point position, boolean focus, boolean onTop) {
      this.inter = inter;
      this.size = new Dimension(width, 0);
      this.position = new Point(position);
      this.focus = focus;
      this.onTop = onTop;
   }

   public Interface getInterface() {
      return this.inter;
   }

   public Dimension getSize() {
      return new Dimension(this.size);
   }

   public void setHeight(int height) {
      this.size.height = height;
   }

   public Point getPos() {
      return new Point(this.position);
   }

   public boolean hasFocus() {
      return this.focus;
   }

   public boolean onTop() {
      return this.onTop;
   }

   public void requestFocus() {
      this.focusRequested = true;
   }

   public boolean foucsRequested() {
      return this.focusRequested;
   }

   public boolean isHovered() {
      return (new Rectangle(this.position, this.size)).contains(this.inter.getMouse()) && this.onTop;
   }

   public boolean isClicked() {
      return this.isHovered() && this.inter.getButton(0);
   }

   public Rectangle getRect() {
      return new Rectangle(this.position, this.size);
   }
}
