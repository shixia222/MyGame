package game;
import java.awt.*;
/**
 * 
 *Title:Explode
 *Description:炸弹类,用于击中对方坦克时产生爆炸效果 
 */
public class Explode {
	int x, y;
	private boolean live = true;
	
	private Body tc ;
	//炸弹的直径
	int[] diameter = {4, 7, 12, 18, 26, 32, 49, 30, 14, 6};
	//记录直径,用于循环
	int step = 0;
	
	public Explode(int x, int y, Body tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!this.live) {
			tc.explodes.remove(this);
			return;
		}
		
		if(step == diameter.length) {
			live = false;
			step = 0;
			return;
		}
		if(tc.myTank.getLife()<=0){//我方坦克生命小于等于零才画
			return;
		}
		Color c = g.getColor();
		g.setColor(new Color(0xFF5A00));
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		
		step ++;
	}
}
