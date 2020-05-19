package com.xiaoli.tank;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
/*
 *Description:������,���ڻ��Ƴ���Ч�����ҷ�̹���ܹ��� 
 */
public class River {
   int x,y;
   ImageIcon river = new ImageIcon("images/river.gif");
   public River(int x,int y){
	   this.x=x;
	   this.y=y;
   }
   public void draw(Graphics g){
	   g.drawImage(river.getImage(),x, y,null);
   }
   public Rectangle getRect() {
		return new Rectangle(x, y,river.getIconWidth(),river.getIconHeight());
	}
}
