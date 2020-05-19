package com.xiaoli.tank;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
/**
 * 
 *Title:Home
 *Description:用户城堡类 
 *@author xiaoli
 *@date2017年1月4日
 */
public class Home {
	int x,y;
	private TankClient tc;
	private boolean live=true;
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	ImageIcon home = new ImageIcon("images/home.jpg");
	public Home(int x, int y,TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc=tc;
	}
	/**
	 * 
	 * @param 画笔
	 *  画出鹰碉堡
	 */
    public void draw(Graphics g){
    	if(live){
    	  g.drawImage(home.getImage(), x, y,null);
    	}else{
    		gameOver(g);
    	}
    }
    
    public void gameOver(Graphics g){
    	tc.bloods.clear();
    	tc.grasses.clear();
    	tc.rivers.clear();
    	tc.walls.clear();
    	tc.missiles.clear();
    	tc.tanks.clear();
    	tc.explodes.clear();
    	
    	tc.home.setLive(false);
    	tc.myTank.setLive(false);
    	//清空复活次数
    	tc.count=0;
    	Color c = g.getColor();
    	g.setColor(Color.MAGENTA);
    	Font f = g.getFont();
    	g.setFont(new Font(" ", Font.PLAIN, 40));
		g.drawString("很遗憾,您输了！", 245, 250);
		g.drawString("请按R键重新开始！ ", 230, 300);
		g.setFont(f);
		g.setColor(c);
    }
    public Rectangle getRect() {
		return new Rectangle(x, y, home.getIconWidth(),home.getIconHeight());
	}
}
