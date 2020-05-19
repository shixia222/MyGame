package game;

import java.io.IOException;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
/**
 * 
 *Title:Audio
 *Description:这个类主要是处理声音,包括背景音乐 
 */
public class Audio implements Runnable {
	private String url = "audio/";
	private int type = 0;
	public Audio(int type) {
		this.type = type;
		switch (type) {
		case 0: // 背景音乐
			url = url + "main.wav";
			break;
		case 1: // 移动音效
			url = url + "move.wav";
			break;
		case 2: // 发射炮弹音效
			url = url + "shoot.wav";
			break;
		case 3: // 爆炸音效
			url = url + "explode.wav";
			break;
		case 4: // 吃血块音�?
			url = url + "eatblood.wav";
			break;
		case 5: // 升级音效
			url = url + "levelup.wav";
			break;	
		case 6: // 游戏结束
			url = url + "gameover.wav";
			break;
		}
		new Thread(this).start();
	}

	@Override
	public void run() {
		AudioStream ais = null;
		InputStream is = null;
		try {
			if (type == 0) { // 背景音乐循环
				while (true) {
					is = ClassLoader.getSystemResourceAsStream(url);
					ais = new AudioStream(is);
					AudioPlayer.player.start(ais);
					Thread.sleep(60000);
				}
			} else { // 音效只播放一�?
				is = ClassLoader.getSystemResourceAsStream(url);
				ais = new AudioStream(is);
				AudioPlayer.player.start(ais);
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) { // 关闭输入�?
					is.close();
				}
				if (ais != null) { // 关闭音频�?
					ais.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}