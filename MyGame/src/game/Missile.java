package game;
import java.awt.*;
import java.util.List;
/**
 * 
 *Title:Missile
 *Description:炮弹类,用于坦克发射炮弹 
 */
public class Missile {
	public static int XSPEED = 10;
	public static int YSPEED = 10;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	int x, y;
	Role.Direction dir;
	
	private boolean live = true;
	private boolean good=true;
	/**
	 * 能够操作Body变量
	 */
	private Body tc;
	public Missile(int x, int y, Role.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, Role.Direction dir,boolean good, Body tc) {
		this(x, y, dir);
		this.tc = tc;
		this.good=good;
	}
	/**
	 * 
	 *@Description:画出炮弹
	 * @param 画笔
	 */
	public void draw(Graphics g) {
		if(!live) {
			tc.missiles.remove(this);
			return;
		}
		
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
	}
    /**
     * 
     *@Description:根据坦克的方向计算炮弹移动
     */
	private void move() {
		
		
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
		
		if(x < 0 || y < 0 || x > Body.GAME_WIDTH || y > Body.GAME_HEIGHT) {
			live = false;
		}
		
		
	}

	public boolean isLive() {
		return live;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	/**
	 * 
	 *@Description:炮弹打击坦克
	 * @param 坦克对象
	 * @return 是否打中了坦克
	 */
	public boolean hitTank(Role t) {
		if(this.live&&this.getRect().intersects(t.getRect()) && t.isLive()&& this.good != t.isGood()) {
			if(t.isGood()){
				t.setLife(t.getLife()-50);
				 
				if(t.getLife()<=0) t.setLive(false);
			}else{
				t.setLive(false);
				tc.score+=100;
			}
			this.live=false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			//爆炸音效
			new Audio(3);
			return true;
		}
		return false;
	}
	/**
	 * 
	 *@Description:炮弹打击坦克集合(主要用于判断是否击中敌人坦克)
	 * @param 敌人坦克集合
	 * @return 是否击中坦克
	 */
	public boolean hitTanks(List<Role> roles) {
		for(int i=0; i<roles.size(); i++) {
			if(hitTank(roles.get(i))) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 *@Description:炮弹打击墙
	 * @param 墙对象
	 * @return 是否打中了墙
	 */
   public boolean hitWall(Wall w){
	   if(this.live&&this.getRect().intersects(w.getRect())){
		   this.live=false;
		   return true;
	   }
	return false;
   }
   /**
    * 
    *@Description:炮弹打击多面墙
    * @param walls
    * @return 是否打中了墙
    */
   public boolean hitWalls(List<Wall> walls){
	   for(int i=0;i<walls.size();i++){
		   Wall w=walls.get(i);
		   if(this.live&&this.getRect().intersects(w.getRect())){
			   this.live=false;
			   return true;
		   }
	   }
	   return false;
   }
   /**
    * 
    *@Description:炮弹打击鹰碉堡
    * @param Home对象
    * @return 是否打中了鹰碉堡
    */
   public boolean hitHome(Home h){
	   if(this.live&&h.isLive()&&this.getRect().intersects(h.getRect())){
		   this.live=false;
		   h.setLive(false);
		   return true;
	   }
	   return false;
   }
	
}
