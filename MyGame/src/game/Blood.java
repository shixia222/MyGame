package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

/**
 * 
 *Title:Blood
 *Description:����Ѫ����,����Ϸ�����л�����ڳ����г��� 
 */
public class Blood {
    int x,y,w,h;
    Body tc;
    int step=0;
    public boolean live=true;
    Random r = new Random();
	private int[][] pos={
    		              {350,300},{360,300},{370,290},{380,280},{370,250},{360,250}
                      };
	//�������
	public Blood(){
    	x=(r.nextInt(20)+3)*25;
    	y=(r.nextInt(20)+3)*25;
    	w=h=10;
    }
    //��Ѫ��
	public void draw(Graphics g){
    	if(!live){
    		return;
    	}
    	ImageIcon blood = new ImageIcon("images/Blood.png");
		g.drawImage(blood.getImage(),x, y ,w+5,h+10, null, tc);
        move1();
    }
    private void move1(){
    	if(step>10){
    		step=0;
    	}
    	step++;
    	if(step<=5){
    	  this.x+=1;
    	  this.y-=1;
    	}else{
    		this.x-=1;
    		this.y+=1;
    	}
    }
	//Ѫ���ƶ�
    private void move() {
		// TODO Auto-generated method stub
		step++;
		if(step==pos.length){
			step=0;
		}
		x=pos[step][0];
		y=pos[step][1];
	}
    public Rectangle getRect() {
		return new Rectangle(x, y, w , h);
	}
    /**
     * 
     * @param ǽ
     * @return �Ƿ�����ǽ��
     */
	public boolean CollidesWithWall(Wall w){
		if(this.live&&this.getRect().intersects(w.getRect())){
			this.live=false;
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param �ܶ�ǽ
	 * @return �Ƿ�����ǽ��
	 */
	public boolean CollidesWithWalls(List<Wall> walls){
		for(int i=0;i<walls.size();i++){
			Wall w = walls.get(i);
			this.CollidesWithWall(w);
			return true;
		}
		return false;
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
}
