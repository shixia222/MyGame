package com.xiaoli.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

public class Tank {
	//坦克X方向速度,设置游戏难度时可拓展
    public static int XSPEED = 5;
    //坦克Y方向速度,设置游戏难度时可拓展
	public static int YSPEED = 5;
	//坦克宽度
	public static final int WIDTH = 30;
	//坦克高度
	public static final int HEIGHT = 30;
	//坦克是否活着
	private boolean live = true;
	//坦克的生命值
	private int life = 100;
	//持有对TankClient大管家的引用
	TankClient tc;
	//判断是否是我方坦克,默认true
	private boolean good=true;
	//用于记录坦克原来的坐标,碰到墙、坦克时方便退一步
	private int oldX,oldY;
	//绘制坦克的左上角坐标
	private int x, y;
	//用于产生随机数
	private static Random r = new Random();
	//用于控制敌人随机发出子弹
	private int step = r.nextInt(30)+10;
	//判断是否按下方向键
	private boolean bL=false, bU=false, bR=false, bD = false;
	//枚举类型定义了坦克的八个方向,和静止时的方向
	enum Direction {L, LU, U, RU, R, RD, D, LD, STOP};
	//坦克的方向
	private Direction dir = Direction.STOP;
	//炮筒的方向
	private Direction ptDir = Direction.D;
    //血条
	private BloodBar bar = new BloodBar();
	//构造方法
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}
	//构造方法
	public Tank(int x, int y, boolean good, Direction dir,TankClient tc) {
		this(x, y, good);
		this.tc = tc;
		this.oldX=x;
		this.oldY=y;
		this.dir=dir;
	}
	/*
	 @Description:画出坦克
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
		//血条
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
	 * 坦克移动
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
			 //敌人发子弹
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
	 *@Description:用于撞到墙、坦克时返回上一步
	 */
	private void stay(){
		x=oldX;
		y=oldY;
	}
	/*
	 *@Description:按下键时监听
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
	    	
	    	//关卡、分数重置
	    	tc.score=0;
	    	tc.level=1;
	    	//草地
	    	tc.newGrass();
	    	//河流
	    	tc.newRiver();
	    	//墙
	    	tc.newWall();
	    	//当在区域中没有坦克时，就出来坦克      
	    	if (tc.tanks.size() == 0) {   
	    		tc.newTank();
			}
			tc.myTank = new Tank(220, 560, true, Direction.STOP, tc);//设置自己出现的位置
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
	 *@Description:定位坦克的方向
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
     *@Description:松开键时监听
     */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_K:
			if(this.good){
				if(!this.live){
					this.live=true;
					this.life=100;
					//复活次数加1
					tc.count++;
				}
			}
			break;
		case KeyEvent.VK_J:
			superFire();
			break;
		case KeyEvent.VK_CONTROL:
			fire();
			//发射炮弹音效
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
	/*Description:坦克开火
	 return 炮弹对象
	 */
	public Missile fire() {
		if(!live)return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, ptDir,this.good, this.tc);
		tc.missiles.add(m);
		return m;
	}
	/*Description:坦克根据方向开火
	 return 炮弹对象
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
	 * Description:超级炮弹,可以向八个方向开火
	 */
	public void superFire(){
		Direction[] dirs = Direction.values();
		for(int i=0;i<8;i++){
			fire(dirs[i]);
		}
	}
	/*
	 Description:判断坦克是否撞墙
	 return 是否撞墙了
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
	/*Description:判断坦克是否相撞
	 *return 是否和坦克相撞了
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
	 *Description:坦克是否碰到了鹰碉堡
	 * return 是否碰到了鹰碉堡
	 */
	public boolean CollidesWithHome(Home h){
		if(this.live&&h.isLive()&&this.getRect().intersects(h.getRect())){
			this.stay();
			return true;
		}
		return false;
	}
	/*
	 *Description:坦克是否碰到了河流,主要是地方坦克调用,我方坦克能直接渡河
	 * return 是否碰到了河流
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
	 *Description:坦克是否碰到了河流,主要是地方坦克调用,我方坦克能直接渡河
	 * return 是否碰到了河流
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
     *Description:我方坦克的血条,用于显示我方坦克的生命值 
     */
	private class BloodBar{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.PINK);
			g.drawRect(x,y-10,WIDTH,8);
			//里面的
			g.setColor(Color.PINK);
			int w =WIDTH*life/100;
			g.fillRect(x, y-10, w, 8);
			g.setColor(c);
		}
	}
	/*
	 * 
	 *Description:吃血块,主要是我方坦克调用
	 *return 是否吃到了血块
	 */
	public boolean eat(Blood b){
		if(this.live&&b.isLive()&&this.getRect().intersects(b.getRect())){
			this.life=100;
			b.setLive(false);
			tc.bloods.remove(b);
			//吃血块音效
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
