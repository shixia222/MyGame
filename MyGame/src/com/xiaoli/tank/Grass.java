package com.xiaoli.tank;

import java.awt.Graphics;

import javax.swing.ImageIcon;
/**
 * 
 *Title:Grass
 *Description:�ݵ��� 
 *@author xiaoli
 *@date2017��1��4��
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
