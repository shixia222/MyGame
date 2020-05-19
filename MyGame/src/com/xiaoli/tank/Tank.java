package com.xiaoli.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

public class Tank {
	//̹��X�����ٶ�,������Ϸ�Ѷ�ʱ����չ
    public static int XSPEED = 5;
    //̹��Y�����ٶ�,������Ϸ�Ѷ�ʱ����չ
	public static int YSPEED = 5;
	//̹�˿��
	public static final int WIDTH = 30;
	//̹�˸߶�
	public static final int HEIGHT = 30;
	//̹���Ƿ����
	private boolean live = true;
	//̹�˵�����ֵ
	private int life = 100;
	//���ж�TankClient��ܼҵ�����
	TankClient tc;
	//�ж��Ƿ����ҷ�̹��,Ĭ��true
	private boolean good=true;
	//���ڼ�¼̹��ԭ��������,����ǽ��̹��ʱ������һ��
	private int oldX,oldY;
	//����̹�˵����Ͻ�����
	private int x, y;
	//���ڲ��������
	private static Random r = new Random();
	//���ڿ��Ƶ�����������ӵ�
	private int step = r.nextInt(30)+10;
	//�ж��Ƿ��·����
	private boolean bL=false, bU=false, bR=false, bD = false;
	//ö�����Ͷ�����̹�˵İ˸�����,�;�ֹʱ�ķ���
	enum Direction {L, LU, U, RU, R, RD, D, LD, STOP};
	//̹�˵ķ���
	private Direction dir = Direction.STOP;
	//��Ͳ�ķ���
	private Direction ptDir = Direction.D;
    //Ѫ��
	private BloodBar bar = new BloodBar();
	//���췽��
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}
	//���췽��
	public Tank(int x, int y, boolean good, Direction dir,TankClient tc) {
		this(x, y, good);
		this.tc = tc;
		this.oldX=x;
		this.oldY=y;
		this.dir=dir;
	}
	/*
	 @Description:����̹��
	 */
	public void draw(Graphics g) {
		if(!live) {
			if(!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		Color c = g.getColor();
		if(good) g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		//g.fillOval(x, y, WIDTH, HEIGHT);
		ImageIcon tank1 = new ImageIcon("images/tank.jpg");
		g.drawImage(tank1.getImage(),x, y,WIDTH,HEIGHT,null);
		g.setColor(c);
		//Ѫ��
	    if(good) bar.draw(g); 
		switch(ptDir) {
		case L:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
			break;
		}
		
		move();
	}
	/*
	 * ̹���ƶ�
	 */
	void move() {
		this.oldX=x;
		this.oldY=y;
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		if(this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
		if(!good){
			Direction[] dirs = Direction.values();
			if(step==0){
				step=r.nextInt(30)+10;
				int rn = r.nextInt(9);
				this.dir=dirs[rn];
			}
			step--;
			 //���˷��ӵ�
			if(r.nextInt(40)>36){
			   this.fire();
			}
		}
		if(x < 0) x = 0;
		if(y < 55) y = 55;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
	}
	/*
	 *@Description:����ײ��ǽ��̹��ʱ������һ��
	 */
	private void stay(){
		x=oldX;
		y=oldY;
	}
	/*
	 *@Description:���¼�ʱ����
	 */
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_R:
			tc.bloods.clear();
	    	tc.grasses.clear();
	    	tc.rivers.clear();
	    	tc.walls.clear();
	    	tc.missiles.clear();
	    	tc.tanks.clear();
	    	tc.explodes.clear();
	    	
	    	//�ؿ�����������
	    	tc.score=0;
	    	tc.level=1;
	    	//�ݵ�
	    	tc.newGrass();
	    	//����
	    	tc.newRiver();
	    	//ǽ
	    	tc.newWall();
	    	//����������û��̹��ʱ���ͳ���̹��      
	    	if (tc.tanks.size() == 0) {   
	    		tc.newTank();
			}
			tc.myTank = new Tank(220, 560, true, Direction.STOP, tc);//�����Լ����ֵ�λ��
			if(!tc.home.isLive()){
				tc.home.setLive(true);
			}
			tc.dispose();
			new TankClient().lauchFrame();
			break;
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		}
		locateDirection();
	}
	/*
	 *@Description:��λ̹�˵ķ���
	 */
	void locateDirection() {
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.LU;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.RU;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.RD;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
    /*
     *@Description:�ɿ���ʱ����
     */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_K:
			if(this.good){
				if(!this.live){
					this.live=true;
					this.life=100;
					//���������1
					tc.count++;
				}
			}
			break;
		case KeyEvent.VK_J:
			superFire();
			break;
		case KeyEvent.VK_CONTROL:
			fire();
			//�����ڵ���Ч
			new Audio(2);
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			new Audio(1);
			break;
		case KeyEvent.VK_UP :
			bU = false;
			new Audio(1);
			break;
		case KeyEvent.VK_RIGHT :
			bR = false;
			new Audio(1);
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			new Audio(1);
			break;
		}
		locateDirection();		
	}
	/*Description:̹�˿���
	 return �ڵ�����
	 */
	public Missile fire() {
		if(!live)return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, ptDir,this.good, this.tc);
		tc.missiles.add(m);
		return m;
	}
	/*Description:̹�˸��ݷ��򿪻�
	 return �ڵ�����
	 */
	public Missile fire(Direction dir) {
		if(!live)return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, dir,this.good, this.tc);
		tc.missiles.add(m);
		return m;
	}
	/*
	 * Description:�����ڵ�,������˸����򿪻�
	 */
	public void superFire(){
		Direction[] dirs = Direction.values();
		for(int i=0;i<8;i++){
			fire(dirs[i]);
		}
	}
	/*
	 Description:�ж�̹���Ƿ�ײǽ
	 return �Ƿ�ײǽ��
	 */
	public boolean CollidesWithWall(Wall w){
		if(this.live&&this.getRect().intersects(w.getRect())){
			this.stay();
			return true;
		}
		return false;
	}
	public boolean CollidesWithWalls(List<Wall> walls){
        for(int i=0;i<walls.size();i++){
        	Wall w = walls.get(i);
        	if(this.live&&this.getRect().intersects(w.getRect())){
        	    this.stay();
        		return true;
        	}
        }
		return false;
	}
	/*Description:�ж�̹���Ƿ���ײ
	 *return �Ƿ��̹����ײ��
	 */
	public boolean collidesWithTanks(List<Tank> tanks){
		for(int i=0;i<tanks.size();i++){
			Tank t = tanks.get(i);
			if(this!=t){
				if(this.live&&t.isLive()&&this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	/* 
	 *Description:̹���Ƿ�������ӥ�ﱤ
	 * return �Ƿ�������ӥ�ﱤ
	 */
	public boolean CollidesWithHome(Home h){
		if(this.live&&h.isLive()&&this.getRect().intersects(h.getRect())){
			this.stay();
			return true;
		}
		return false;
	}
	/*
	 *Description:̹���Ƿ������˺���,��Ҫ�ǵط�̹�˵���,�ҷ�̹����ֱ�Ӷɺ�
	 * return �Ƿ������˺���
	 */
	public boolean CollidesWithRiver(River r){
		if(this.live&&this.getRect().intersects(r.getRect())){
			this.stay();
			return true;
		}
		return false;
	}
	/*
	 * 
	 *Description:̹���Ƿ������˺���,��Ҫ�ǵط�̹�˵���,�ҷ�̹����ֱ�Ӷɺ�
	 * return �Ƿ������˺���
	 */
	public boolean CollidesWithRivers(List<River> rivers){
		for(int i=0;i<rivers.size();i++){
			River t = rivers.get(i);
			if(this.live&&this.getRect().intersects(t.getRect())){
				this.stay();
				return true;
			}
		}
		return false;
	}
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
    /*
     *Title:BloodBar
     *Description:�ҷ�̹�˵�Ѫ��,������ʾ�ҷ�̹�˵�����ֵ 
     */
	private class BloodBar{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.PINK);
			g.drawRect(x,y-10,WIDTH,8);
			//�����
			g.setColor(Color.PINK);
			int w =WIDTH*life/100;
			g.fillRect(x, y-10, w, 8);
			g.setColor(c);
		}
	}
	/*
	 * 
	 *Description:��Ѫ��,��Ҫ���ҷ�̹�˵���
	 *return �Ƿ�Ե���Ѫ��
	 */
	public boolean eat(Blood b){
		if(this.live&&b.isLive()&&this.getRect().intersects(b.getRect())){
			this.life=100;
			b.setLive(false);
			tc.bloods.remove(b);
			//��Ѫ����Ч
			new Audio(4);
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
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	public boolean isGood() {
		return good;
	}
}
