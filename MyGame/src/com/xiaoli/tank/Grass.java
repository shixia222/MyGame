package com.xiaoli.tank;

import java.awt.Graphics;

import javax.swing.ImageIcon;
/**
 * 
 *Title:Grass
 *Description:草地类 
 *@author xiaoli
 *@date2017年1月4日
 */
public class Grass {
     int x,y;
     ImageIcon grass = new ImageIcon("images/grass.gif");
     public Grass(int x, int y) {
		this.x = x;
		this.y = y;
	}
     public void draw(Graphics g){
    	 g.drawImage(grass.getImage(), x, y,null);
     }
}
