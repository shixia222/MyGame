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
 *Description:������Ǵ�ܼ�,������������,Ҳ�ǳ������ 
 */
public class TankClient extends Frame implements ActionListener {
	//��Ϸ���
	public static final int GAME_WIDTH = 800;
	//��Ϸ�߶�
	public static final int GAME_HEIGHT = 600;
    //�Ƿ����ػ��߳�
	public static boolean printable=true;
	//���ڲ��������
	Random r = new Random();
	//��¼�������
	public static int count=0;
	//�ؿ�
	public static int level=1;
	//����
	public static int score=0;
	//�˵���
	MenuBar jmb = null;
	//�˵�
	Menu jm1 = null, jm2 = null, jm3 = null, jm4 = null;
	//�˵���
	MenuItem jmi1 = null, jmi2 = null, jmi3 = null, jmi4 = null, jmi5 = null,
			jmi6 = null, jmi7 = null, jmi8 = null, jmi9 = null,jmi10=null,jmi11=null;
	//���ϵ��´����һ�ǽ
	Wall w1,w2,w3,w4,w5,w6,w7,w8,w9,w10,w11,w12;
	//�����ҵ�̹��
	Tank myTank = new Tank(220, 560, true,Tank.Direction.STOP, this);
	//����ӥ�ﱤ
	Home home = new Home(379,556,this);
    //ǽ����,���ڴ��ǽ
	List<Wall> walls = new ArrayList<Wall>();
	//��������,���ڴ�ź���
	List<River> rivers = new ArrayList<River>();
	//����һ��Ѫ��
	Blood b=null;
	//Ѫ�鼯��,���ڴ��Ѫ�顣�ü��ϵ�ԭ���Ƿ���ɾ��
	List<Blood> bloods = new ArrayList<Blood>();
	//�ݼ���,���ڴ�Ų�
	List<Grass> grasses = new ArrayList<Grass>();
	//ը������,���ڴ��ը��
	List<Explode> explodes = new ArrayList<Explode>();
	//�ڵ�����,���ڴ���ڵ�
	List<Missile> missiles = new ArrayList<Missile>();
	//̹�˼���,���ڴ��̹��
	List<Tank> tanks = new ArrayList<Tank>();
	//���ڽ��˫������˸����
	Image offScreenImage = null;
	/*
	 * ���Ƴ���
	 */
	public void paint(Graphics g) {
		//�ֹ�
		if (score >= 1000 * level ) {
			setLevel(level + 1); // ������һ��
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
		//��
		home.draw(g);
		//ֻ�ܸ���һ��
		if(myTank.getLife()<=0&&count==1){
			myTank.setLive(false);
			home.gameOver(g);
		}
		if(r.nextInt(50)>48){
			newBlood();
		}
		//��������
		if(tanks.size()<=0){
			for(int i=0; i<6; i++) {
				if(i<3){
				    tanks.add(new Tank(50 + 40*(i+1), 50, false,Tank.Direction.D, this));
				}else{
					tanks.add(new Tank(50 + 40*(i+1), 50, false,Tank.Direction.R, this));
				}
			}
		}
		//g.drawString("Ѫ������:" + bloods.size(), 10, 50);
		//g.drawString("�ӵ�����:" + missiles.size(), 10, 70);
		//g.drawString("ը������:" + explodes.size(), 10, 90);
		//g.drawString("̹������:" + tanks.size(), 10, 110);
		g.drawString("�ؿ�:" + level, 10, 90);
		g.drawString("����:" + score, 10, 110);
		g.drawString("����ֵ:" + myTank.getLife(), 10, 130);
		g.drawString("�������:" + count, 10, 150);
		//��ǽ
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
		//������
		for(int i=0;i<rivers.size();i++){
			rivers.get(i).draw(g);
		}
		myTank.draw(g);
		// ���ݵ�
		for (int i = 0; i < grasses.size(); i++) {
			grasses.get(i).draw(g);
		}
		// ��Ѫ��
		 for (int i = 0; i < bloods.size(); i++) {
			 Blood b = bloods.get(i);
			 b.draw(g);
			 myTank.eat(b);
		}
	}
	/*
	 * repaint�ȵ���update�ٵ���paint�����ڽ��˫������˸����
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
     * ��������ʾ����������
     */
	public void lauchFrame() {
		//�˵���
		newMenu();
		//ǽ
		newWall();
		//����
		newRiver();
		//�ݵ�
		newGrass();
		//̹��
		newTank();
		this.setTitle("̹�˴�ս");
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
		//����
		if(this.level==1){
		  StartAudio startAudio = new StartAudio("audio/7301.wav");
		  startAudio.start();
		}
	}
	/*
	 * 
	 *�˵���
	 */
	public void newMenu(){
		// �����˵����˵�ѡ��
				jmb = new MenuBar();
				jm1 = new Menu("��Ϸ");
				jm2 = new Menu("��ͣ/����");
				jm3 = new Menu("����");
				jm4 = new Menu("��Ϸ�Ѷ�");
				jm1.setFont(new Font("����", Font.BOLD, 15));// ���ò˵���ʾ������
				jm2.setFont(new Font("����", Font.BOLD, 15));// ���ò˵���ʾ������
				jm3.setFont(new Font("TimesRoman", Font.BOLD, 15));// ���ò˵���ʾ������
				jm4.setFont(new Font("TimesRoman", Font.BOLD, 15));// ���ò˵���ʾ������

				jmi1 = new MenuItem("���¿�ʼ");
				jmi2 = new MenuItem("�˳�");
				jmi11= new MenuItem("������������");
				
				jmi3 = new MenuItem("��ͣ");
				jmi4 = new MenuItem("����");
				jmi5 = new MenuItem("��Ϸ˵��");
				jmi6 = new MenuItem("�Ѷ�1");
				jmi7 = new MenuItem("�Ѷ�2");
				jmi8 = new MenuItem("�Ѷ�3");
				jmi9 = new MenuItem("�Ѷ�4");
				jmi10 = new MenuItem("����˵��");
				
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

				this.setMenuBar(jmb);// �˵�Bar�ŵ�JFrame��
	}
	/*
	 * 
	 *����ǽ
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
	 *����Ѫ��
	 */
	public void newBlood(){
		 //Ѫ��
		 b = new Blood();
		//if(b.CollidesWithWalls(walls)){//Ѫ�鲻�ܻ���ǽ����
			//return;
		//}
		if(bloods.size()==0){
		  bloods.add(b);
		}
	}
	/*
	 * 
	 *���ɺ���
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
	 *���ɲݵ�
	 */
	public void newGrass() {
		// �ݵ�
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
	 *����̹��
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
	 *���������������
	 *
	 */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame();
		
	}
	/*
	 * 
	 *�ػ��߳�,ģ�⶯̬Ч�� 
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
	 *��������,��̹�ˡ��ڵ��Ƚ����˻�����
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
     * �Բ˵������
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("NewGame")) {
			printable = false;
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ���¿�ʼ��", "",
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
				new Thread(new PaintThread()).start(); // �߳�����
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
				new Thread(new PaintThread()).start(); // �߳�����
			}
			// System.out.println("����");
		} else if (e.getActionCommand().equals("Exit")) {
			printable = false;
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ�˳���", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.out.println("�˳�");
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // �߳�����

			}

		} else if (e.getActionCommand().equals("help1")) {
			printable = false;
			JOptionPane.showMessageDialog(null, "�������Ǿ����Ļ����������ḻ,��Ϸ��Ϊ�����������бز����ٵ�һ����\n90��̹�˴�ս������90��һ����ͯ��Ļ���,Ҳ��һ�����Ϸ��\n"
					+ "����java��̹�˴�ս�����ø��������java��������̡�swing�����̡����̱߳��"+"\n"+"�ο�:��ʿ��̹�˴�ս��Ƶ�̡̳��Լ���������Դ"+"\n"+"��������:1275302036@qq.com"+"\n"
					+"���й������ַ�������Ȩ��,�뼰ʱ��ϵ����ɾ��,лл!",
					"��ʾ��", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			printable = true;
			new Thread(new PaintThread()).start(); // �߳�����
		} else if(e.getActionCommand().equals("help2")){
			printable = false;
			JOptionPane.showMessageDialog(null, "�á� �� �� �����Ʒ���,CTRL���̷���,J�����ڵ�,K����(ֻ�ܸ���һ��),R���¿�ʼ��\n"+"��������:1275302036@qq.com"+"\n"
			+"���й������ַ�������Ȩ��,�뼰ʱ��ϵ����ɾ��,лл!",
					"��ʾ��", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			printable = true;
			new Thread(new PaintThread()).start(); // �߳�����
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
		JOptionPane.showMessageDialog(null, "�������ܹر�!�벻Ҫ�ظ����!",
				"��ʾ��", JOptionPane.INFORMATION_MESSAGE);
		this.setVisible(true);
		printable = true;
		// �߳�����
		new Thread(new PaintThread()).start(); 
		new Audio(0);
	}
 }
/*
 * ���ùؿ�
 */
public void setLevel(int level) { //��������
		if (level > 10) {
			level = 10;
		}
		this.level = level;
	}
}














