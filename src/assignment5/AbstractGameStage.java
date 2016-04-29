package assignment5;


import java.awt.image.BufferedImage;

import javax.swing.JFrame;

enum GameState{
	BEGIN, RUNNING, WAITING, END
}
/*
我口水好多＝ ＝
我含不到五分鐘吧...
會滿出來＝﹍﹍﹍﹍＝
漱口水吞下去會怎樣，可是我已經吞一點了
....
已經十點惹...
你怎麼不帶電腦來打作業
那如果不想看到呢..會撞車
陀螺儀
等等幫我顧電腦，我會放著睡眠
*/
public abstract class AbstractGameStage extends JFrame{
	protected Client client;
	
	protected int currentScore, winScore;
	protected GameState state;
	protected int windowHeight, typingWidth, displayWidth;
	protected TypingPanel typingPanel;
	protected DisplayPanel displayPanel;
	protected BufferedImage winImage;
	protected Thread typingThread, displayThread;
	
	abstract public void start();
	abstract public void replay();
	abstract public void end();
	public void addScore(int addition){
		this.currentScore += addition;
		this.displayPanel.updateScore(currentScore);
		if(currentScore >= winScore) {
			this.state = GameState.END;
			this.end();
		}
		if(addition > 0) displayPanel.duckSwim();
	}
	public int getScore(){
		return currentScore;
	}
	public void setWinScore(int winScore){
		this.winScore = winScore;
	}
	
	public int getTypingWidth(){
		return typingWidth;
	}
	public int getDisplayWidth(){
		return displayWidth;
	}
	public int getWindowHeight(){
		return windowHeight;
	}
	public boolean isWin(){
		return (currentScore >= winScore);
	}
}
