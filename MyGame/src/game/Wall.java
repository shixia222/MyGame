package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

//ǽ
public class Wall {
  int x,y,w,h;
  Body tc;
public Wall(int x, int y, int w, int h, Body tc) {
	this.x = x;
	this.y = y;
	this.w = w;
	this.h = h;
	this.tc = tc;
}
public void draw(Graphics g){
	Color c = g.getColor();
	g.setColor(new Color(0xD3C9C9));
	g.fillRect(x, y, w, h);
	g.setColor(c);
}
public Rectangle getRect(){
	return new Rectangle(x,y,w,h);
}
}
