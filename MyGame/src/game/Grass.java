package game;

import java.awt.Graphics;

import javax.swing.ImageIcon;
/**
 * 
 *Title:Grass
 *Description:≤›µÿ¿‡ 
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
