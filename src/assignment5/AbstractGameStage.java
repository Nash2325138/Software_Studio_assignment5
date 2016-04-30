package assignment5;


import java.awt.image.BufferedImage;

import javax.swing.JFrame;

enum GameState{
	BEGIN, RUNNING, WAITING, REPEAT, END
}
// abstract class of GameStage
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
	// define some simple function
	public void addScore(int addition){
		this.currentScore += addition;
		this.displayPanel.updateScore(currentScore);
		if(currentScore >= winScore) {
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
