package game;
import java.awt.*;
import java.util.List;
/**
 * 
 *Title:Missile
 *Description:�ڵ���,����̹�˷����ڵ� 
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
	 * �ܹ�����Body����
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
	 *@Description:�����ڵ�
	 * @param ����
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
     *@Description:����̹�˵ķ�������ڵ��ƶ�
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
	 *@Description:�ڵ����̹��
	 * @param ̹�˶���
	 * @return �Ƿ������̹��
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
			//��ը��Ч
			new Audio(3);
			return true;
		}
		return false;
	}
	/**
	 * 
	 *@Description:�ڵ����̹�˼���(��Ҫ�����ж��Ƿ���е���̹��)
	 * @param ����̹�˼���
	 * @return �Ƿ����̹��
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
	 *@Description:�ڵ����ǽ
	 * @param ǽ����
	 * @return �Ƿ������ǽ
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
    *@Description:�ڵ��������ǽ
    * @param walls
    * @return �Ƿ������ǽ
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
    *@Description:�ڵ����ӥ�ﱤ
    * @param Home����
    * @return �Ƿ������ӥ�ﱤ
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
