package assignment5;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameStage extends AbstractGameStage {
	public GameStage(Client client) {
		// TODO Auto-generated constructor stub
		this.client = client;
		
		windowHeight = 500;
		typingWidth = 400;
		displayWidth = 800;
		winScore = 100;
		state = GameState.BEGIN;
		
		displayPanel = new DisplayPanel(new Rectangle(typingWidth, 0, displayWidth, windowHeight), this);
		displayPanel.setVisible(true);
		
		typingPanel = new TypingPanel(new Rectangle(0, 0, typingWidth, windowHeight), this);
		typingPanel.setVisible(true);
		
		this.add(typingPanel);
		this.add(displayPanel);
		this.setSize(new Dimension(typingWidth+displayWidth, windowHeight));
		
		this.typingThread = new Thread(typingPanel);
		this.displayThread = new Thread(displayPanel);
	
	}
	@Override
	public void start() {
		// TODO Auto-generated method stub
		state = GameState.RUNNING;
		typingThread.start();
		displayThread.start();
		this.setVisible(true);
	}

	@Override
	public void replay() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void end() {
		// TODO Auto-generated method stub
		typingPanel.outPutUnknownMap();
	}
	
	
}
