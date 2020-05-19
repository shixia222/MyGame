package com.xiaoli.tank;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
/**
 * 
 *Title:TankClient
 *Description:这个类是大管家,控制着其他类,也是程序入口 
 */
public class TankClient extends Frame implements ActionListener {
	//游戏宽度
	public static final int GAME_WIDTH = 800;
	//游戏高度
	public static final int GAME_HEIGHT = 600;
    //是否开启重绘线程
	public static boolean printable=true;
	//用于产生随机数
	Random r = new Random();
	//记录复活次数
	public static int count=0;
	//关卡
	public static int level=1;
	//分数
	public static int score=0;
	//菜单条
	MenuBar jmb = null;
	//菜单
	Menu jm1 = null, jm2 = null, jm3 = null, jm4 = null;
	//菜单项
	MenuItem jmi1 = null, jmi2 = null, jmi3 = null, jmi4 = null, jmi5 = null,
			jmi6 = null, jmi7 = null, jmi8 = null, jmi9 = null,jmi10=null,jmi11=null;
	//从上到下从左到右画墙
	Wall w1,w2,w3,w4,w5,w6,w7,w8,w9,w10,w11,w12;
	//创建我的坦克
	Tank myTank = new Tank(220, 560, true,Tank.Direction.STOP, this);
	//创建鹰碉堡
	Home home = new Home(379,556,this);
    //墙集合,用于存放墙
	List<Wall> walls = new ArrayList<Wall>();
	//河流集合,用于存放河流
	List<River> rivers = new ArrayList<River>();
	//定义一个血块
	Blood b=null;
	//血块集合,用于存放血块。用集合的原因是方便删除
	List<Blood> bloods = new ArrayList<Blood>();
	//草集合,用于存放草
	List<Grass> grasses = new ArrayList<Grass>();
	//炸弹集合,用于存放炸弹
	List<Explode> explodes = new ArrayList<Explode>();
	//炮弹集合,用于存放炮弹
	List<Missile> missiles = new ArrayList<Missile>();
	//坦克集合,用于存放坦克
	List<Tank> tanks = new ArrayList<Tank>();
	//用于解决双缓冲闪烁现象
	Image offScreenImage = null;
	/*
	 * 绘制场景
	 */
	public void paint(Graphics g) {
		//分关
		if (score >= 1000 * level ) {
			setLevel(level + 1); // 进入下一关
			new Audio(5);
			grasses.clear();
	    	rivers.clear();
	    	missiles.clear();
	    	tanks.clear();
	        explodes.clear();
	    	walls.clear();
	    	this.dispose();
			new TankClient().lauchFrame();
		}
		//家
		home.draw(g);
		//只能复活一次
		if(myTank.getLife()<=0&&count==1){
			myTank.setLive(false);
			home.gameOver(g);
		}
		if(r.nextInt(50)>48){
			newBlood();
		}
		//敌人死光
		if(tanks.size()<=0){
			for(int i=0; i<6; i++) {
				if(i<3){
				    tanks.add(new Tank(50 + 40*(i+1), 50, false,Tank.Direction.D, this));
				}else{
					tanks.add(new Tank(50 + 40*(i+1), 50, false,Tank.Direction.R, this));
				}
			}
		}
		//g.drawString("血块数量:" + bloods.size(), 10, 50);
		//g.drawString("子弹数量:" + missiles.size(), 10, 70);
		//g.drawString("炸弹数量:" + explodes.size(), 10, 90);
		//g.drawString("坦克数量:" + tanks.size(), 10, 110);
		g.drawString("关卡:" + level, 10, 90);
		g.drawString("分数:" + score, 10, 110);
		g.drawString("生命值:" + myTank.getLife(), 10, 130);
		g.drawString("复活次数:" + count, 10, 150);
		//画墙
		for(int i=0;i<walls.size();i++){
			walls.get(i).draw(g);
		}
		for(int i=0; i<missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitWalls(walls);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitHome(home);
			m.draw(g);
			//if(!m.isLive()) missiles.remove(m);
			//else m.draw(g);
		}
		
		for(int i=0; i<explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.CollidesWithWalls(walls);
			myTank.collidesWithTanks(tanks);
			t.collidesWithTanks(tanks);
			t.CollidesWithHome(home);
			t.CollidesWithRivers(rivers);
			t.draw(g);
		}
		myTank.CollidesWithWalls(walls);
		myTank.CollidesWithHome(home);
		//画河流
		for(int i=0;i<rivers.size();i++){
			rivers.get(i).draw(g);
		}
		myTank.draw(g);
		// 画草地
		for (int i = 0; i < grasses.size(); i++) {
			grasses.get(i).draw(g);
		}
		// 画血块
		 for (int i = 0; i < bloods.size(); i++) {
			 Blood b = bloods.get(i);
			 b.draw(g);
			 myTank.eat(b);
		}
	}
	/*
	 * repaint先调用update再调用paint，用于解决双缓冲闪烁现象
	 */
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(new Color(0xcce8cf));
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
    /*
     * 本方法显示程序主窗口
     */
	public void lauchFrame() {
		//菜单项
		newMenu();
		//墙
		newWall();
		//河流
		newRiver();
		//草地
		newGrass();
		//坦克
		newTank();
		this.setTitle("坦克大战");
		//this.setLocation(600,300);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - getSize().width) / 2,
				(dim.height - getSize().height) / 2);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.addKeyListener(new KeyMonitor());
		
		setVisible(true);
		
		new Thread(new PaintThread()).start();
		//声音
		if(this.level==1){
		  StartAudio startAudio = new StartAudio("audio/7301.wav");
		  startAudio.start();
		}
	}
	/*
	 * 
	 *菜单项
	 */
	public void newMenu(){
		// 创建菜单及菜单选项
				jmb = new MenuBar();
				jm1 = new Menu("游戏");
				jm2 = new Menu("暂停/继续");
				jm3 = new Menu("帮助");
				jm4 = new Menu("游戏难度");
				jm1.setFont(new Font("宋体", Font.BOLD, 15));// 设置菜单显示的字体
				jm2.setFont(new Font("宋体", Font.BOLD, 15));// 设置菜单显示的字体
				jm3.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体
				jm4.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置菜单显示的字体

				jmi1 = new MenuItem("重新开始");
				jmi2 = new MenuItem("退出");
				jmi11= new MenuItem("开启背景音乐");
				
				jmi3 = new MenuItem("暂停");
				jmi4 = new MenuItem("继续");
				jmi5 = new MenuItem("游戏说明");
				jmi6 = new MenuItem("难度1");
				jmi7 = new MenuItem("难度2");
				jmi8 = new MenuItem("难度3");
				jmi9 = new MenuItem("难度4");
				jmi10 = new MenuItem("操作说明");
				
				jmi1.setFont(new Font("TimesRoman", Font.BOLD, 15));
				jmi2.setFont(new Font("TimesRoman", Font.BOLD, 15));
				jmi3.setFont(new Font("TimesRoman", Font.BOLD, 15));
				jmi4.setFont(new Font("TimesRoman", Font.BOLD, 15));
				jmi5.setFont(new Font("TimesRoman", Font.BOLD, 15));

				jm1.add(jmi1);
				jm1.add(jmi2);
				jm1.add(jmi11);
				
				jm2.add(jmi3);
				jm2.add(jmi4);
				jm3.add(jmi5);
				jm3.add(jmi10);
				
				jm4.add(jmi6);
				jm4.add(jmi7);
				jm4.add(jmi8);
				jm4.add(jmi9);

				jmb.add(jm1);
				jmb.add(jm2);
				jmb.add(jm4);
				jmb.add(jm3);
				jmi1.addActionListener(this);
				jmi1.setActionCommand("NewGame");
				jmi2.addActionListener(this);
				jmi2.setActionCommand("Exit");
				jmi11.addActionListener(this);
				jmi11.setActionCommand("startMain");
				
				jmi3.addActionListener(this);
				jmi3.setActionCommand("Stop");
				jmi4.addActionListener(this);
				jmi4.setActionCommand("Continue");
				jmi5.addActionListener(this);
				jmi5.setActionCommand("help1");
				jmi10.addActionListener(this);
				jmi10.setActionCommand("help2");
				
				jmi6.addActionListener(this);
				jmi6.setActionCommand("level1");
				jmi7.addActionListener(this);
				jmi7.setActionCommand("level2");
				jmi8.addActionListener(this);
				jmi8.setActionCommand("level3");
				jmi9.addActionListener(this);
				jmi9.setActionCommand("level4");

				this.setMenuBar(jmb);// 菜单Bar放到JFrame上
	}
	/*
	 * 
	 *生成墙
	 */
	public void newWall() {
		if(this.level==1){
		w1 = new Wall(210, 200, 100, 30, this);
		w2 = new Wall(210, 200, 30, 190, this);
		w3 = new Wall(280, 200, 30, 190, this);
		w4 = new Wall(210, 370, 100, 30, this);
		w5 = new Wall(440, 200, 100, 30, this);
		w6 = new Wall(440, 200, 30, 95, this);
		w7 = new Wall(470, 265, 70, 30, this);
		w8 = new Wall(510, 200, 30, 190, this);
		w9 = new Wall(440, 370, 100, 30, this);
		w10 = new Wall(379, 530, 43, 20, this);
		w11 = new Wall(280, 530, 30, 70, this);
		w12 = new Wall(510, 530, 30, 70, this);
		walls.add(w1);
		walls.add(w2);
		walls.add(w3);
		walls.add(w4);
		walls.add(w5);
		walls.add(w6);
		walls.add(w7);
		walls.add(w8);
		walls.add(w9);
		walls.add(w10);
		walls.add(w11);
		walls.add(w12);
	}else if(this.level==2){
		w1 = new Wall(210, 200, 100, 30, this);
		w2 = new Wall(210, 200, 30, 190, this);
		w3 = new Wall(280, 200, 30, 190, this);
		w4 = new Wall(210, 370, 100, 30, this);
		
		w5 = new Wall(410, 200, 30, 70, this);
		w6 = new Wall(410, 200, 90, 30, this);
		w7 = new Wall(470, 200, 30, 190, this);
		w8 = new Wall(470, 370, 90, 30, this);
		w9 = new Wall(530, 320, 30, 65, this);
		
		w10 = new Wall(379, 530, 43, 20, this);
		w11 = new Wall(280, 530, 30, 70, this);
		w12 = new Wall(510, 530, 30, 70, this);
		walls.add(w1);
		walls.add(w2);
		walls.add(w3);
		walls.add(w4);
		walls.add(w5);
		walls.add(w6);
		walls.add(w7);
		walls.add(w8);
		walls.add(w9);
		walls.add(w10);
		walls.add(w11);
		walls.add(w12);
	}else if(this.level>=3){
		w1 = new Wall(210, 200, 100, 30, this);
		w2 = new Wall(210, 200, 30, 190, this);
		w3 = new Wall(280, 200, 30, 190, this);
		w4 = new Wall(210, 370, 100, 30, this);
		
		w5 = new Wall(430, 200, 90, 30, this);
		w6 = new Wall(430, 285, 90, 30, this);
		w7 = new Wall(430, 370, 90, 30, this);
		w8 = new Wall(500, 200, 30, 200, this);
		
		w10 = new Wall(379, 530, 43, 20, this);
		w11 = new Wall(280, 530, 30, 70, this);
		w12 = new Wall(510, 530, 30, 70, this);
		walls.add(w1);
		walls.add(w2);
		walls.add(w3);
		walls.add(w4);
		walls.add(w5);
		walls.add(w6);
		walls.add(w7);
		walls.add(w8);
		
		walls.add(w10);
		walls.add(w11);
		walls.add(w12);
	}
	}
	/*
	 * 
	 *生成血块
	 */
	public void newBlood(){
		 //血块
		 b = new Blood();
		//if(b.CollidesWithWalls(walls)){//血块不能画在墙里面
			//return;
		//}
		if(bloods.size()==0){
		  bloods.add(b);
		}
	}
	/*
	 * 
	 *生成河流
	 */
	public void newRiver(){
		for(int i=0;i<6;i++){
			River river = new River(18+i*30,340);
			rivers.add(river);
			river = new River(18+i*30,340+30);
			rivers.add(river);
		}
		for(int i=0;i<6;i++){
			River river = new River(568+i*30,340);
			rivers.add(river);
			river = new River(568+i*30,340+30);
			rivers.add(river);
		}
	}
	/*
	 * 
	 *生成草地
	 */
	public void newGrass() {
		// 草地
		for (int i = 0; i < 10; i++) {
			Grass grass = new Grass((i + 1) * 18, 200);
			grasses.add(grass);
			grass = new Grass((i + 1) * 18, 200 + 1 * 18);
			grasses.add(grass);
			grass = new Grass((i + 1) * 18, 200 + 2 * 18);
			grasses.add(grass);
			grass = new Grass((i + 1) * 18, 200 + 3 * 18);
			grasses.add(grass);
			grass = new Grass((i + 1) * 18, 200 + 4 * 18);
			grasses.add(grass);
			grass = new Grass((i + 1) * 18, 200 + 5 * 18);
			grasses.add(grass);
			grass = new Grass((i + 1) * 18, 200 + 6 * 18);
			grasses.add(grass);
			grass = new Grass((i + 1) * 18, 200 + 7 * 18);
			grasses.add(grass);
		}
		for (int i = 1; i <= 11; i++) {
			Grass grass = new Grass(250 + 18 * 7, 182 + i * 18);
			grasses.add(grass);
			grass = new Grass(250 + 18 * 8, 182 + i * 18);
			grasses.add(grass);
			grass = new Grass(250 + 18 * 5, 182 + i * 18);
			grasses.add(grass);
			grass = new Grass(250 + 18 * 6, 182 + i * 18);
			grasses.add(grass);
		}
		for(int i=0;i<10;i++){
			Grass grass = new Grass(550+(i + 1) * 18, 200);
			grasses.add(grass);
			grass = new Grass(550+(i + 1) * 18, 200 + 1 * 18);
			grasses.add(grass);
			grass = new Grass(550+(i + 1) * 18, 200 + 2 * 18);
			grasses.add(grass);
			grass = new Grass(550+(i + 1) * 18, 200 + 3 * 18);
			grasses.add(grass);
			grass = new Grass(550+(i + 1) * 18, 200 + 4 * 18);
			grasses.add(grass);
			grass = new Grass(550+(i + 1) * 18, 200 + 5 * 18);
			grasses.add(grass);
			grass = new Grass(550+(i + 1) * 18, 200 + 6 * 18);
			grasses.add(grass);
			grass = new Grass(550+(i + 1) * 18, 200 + 7 * 18);
			grasses.add(grass);
		}
	}
	/*
	 * 
	 *生成坦克
	 */
	public void newTank(){
		for (int i = 0; i < 10; i++) {
			if (i < 5) {
				tanks.add(new Tank(50 + 40 * (i + 1), 55, false,
						Tank.Direction.D, this));
			} else {
				tanks.add(new Tank(50 + 40 * (i + 1), 55, false,
						Tank.Direction.R, this));
			}
		}
	}
	/*
	 * 
	 *主函数、程序入口
	 *
	 */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame();
		
	}
	/*
	 * 
	 *重绘线程,模拟动态效果 
	 */
	private class PaintThread implements Runnable {

		public void run() {
			while(printable) {
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/*
	 *按键监听,对坦克、炮弹等进行人机交互
	 */
	private class KeyMonitor extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}
    /*
     * 对菜单项操作
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("NewGame")) {
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要重新开始！", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
                this.score=0;
                this.setLevel(1);
				printable = true;
				this.dispose();
				new TankClient().lauchFrame();
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}

		} else if (e.getActionCommand().endsWith("Stop")) {
			printable = false;
			// try {
			// Thread.sleep(10000);
			//
			// } catch (InterruptedException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
		} else if (e.getActionCommand().equals("Continue")) {

			if (!printable) {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}
			// System.out.println("继续");
		} else if (e.getActionCommand().equals("Exit")) {
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要退出吗", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.out.println("退出");
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动

			}

		} else if (e.getActionCommand().equals("help1")) {
			printable = false;
			JOptionPane.showMessageDialog(null, "随着人们精神文化生活的日益丰富,游戏成为了人们生活中必不可少的一部分\n90《坦克大战》更是90后一代人童年的回忆,也是一款经典游戏。\n"
					+ "开发java版坦克大战有利用更深入理解java面向对象编程、swing界面编程、多线程编程"+"\n"+"参考:马士兵坦克大战视频教程、以及互联网资源"+"\n"+"作者邮箱:1275302036@qq.com"+"\n"
					+"若有关内容侵犯了您的权益,请及时联系作者删除,谢谢!",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			printable = true;
			new Thread(new PaintThread()).start(); // 线程启动
		} else if(e.getActionCommand().equals("help2")){
			printable = false;
			JOptionPane.showMessageDialog(null, "用→ ← ↑ ↓控制方向,CTRL键盘发射,J超级炮弹,K复活(只能复活一次),R重新开始！\n"+"作者邮箱:1275302036@qq.com"+"\n"
			+"若有关内容侵犯了您的权益,请及时联系作者删除,谢谢!",
					"提示！", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			printable = true;
			new Thread(new PaintThread()).start(); // 线程启动
		}
		
		else if (e.getActionCommand().equals("level1")) {
			//Tank.count = 12;
			Tank.XSPEED = 5;
			Tank.YSPEED = 5;
			Missile.XSPEED = 10;
			Missile.YSPEED = 10;
			this.dispose();
			new TankClient().lauchFrame();;
		} else if (e.getActionCommand().equals("level2")) {
			//Tank.count = 12;
			Tank.XSPEED = 8;
			Tank.YSPEED = 8;
			Missile.XSPEED = 14;
			Missile.YSPEED = 14;
			this.dispose();
			new TankClient().lauchFrame();;

		} else if (e.getActionCommand().equals("level3")) {
			//Tank.count = 20;
			Tank.XSPEED = 12;
			Tank.YSPEED = 12;
			Missile.XSPEED = 18;
			Missile.YSPEED = 18;
			this.dispose();
			new TankClient().lauchFrame();;
		} else if (e.getActionCommand().equals("level4")) {
			//Tank.count = 20;
			Tank.XSPEED = 16;
			Tank.YSPEED = 16;
			Missile.XSPEED = 24;
			Missile.YSPEED = 14;
			this.dispose();
			new TankClient().lauchFrame();;
	}else if(e.getActionCommand().equals("startMain")){
		printable = false;
		JOptionPane.showMessageDialog(null, "开启后不能关闭!请不要重复点击!",
				"提示！", JOptionPane.INFORMATION_MESSAGE);
		this.setVisible(true);
		printable = true;
		// 线程启动
		new Thread(new PaintThread()).start(); 
		new Audio(0);
	}
 }
/*
 * 设置关卡
 */
public void setLevel(int level) { //设置三关
		if (level > 10) {
			level = 10;
		}
		this.level = level;
	}
}














