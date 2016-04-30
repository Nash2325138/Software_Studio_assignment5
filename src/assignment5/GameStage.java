package assignment5;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
// the GameStage class that will be instantiated
public class GameStage extends AbstractGameStage {
	public GameStage(Client client) {
		// TODO Auto-generated constructor stub
		this.client = client;
		
		windowHeight = 500;
		typingWidth = 400;
		displayWidth = 800;
		winScore = 100;
		state = GameState.BEGIN;
		
		// displayPanel is responsible of the yellow duck
		displayPanel = new DisplayPanel(new Rectangle(typingWidth, 0, displayWidth, windowHeight), this);
		displayPanel.setVisible(true);
		
		// typingPanel is responsible of the dropping word and the 
		typingPanel = new TypingPanel(new Rectangle(0, 0, typingWidth, windowHeight), this);
		typingPanel.setVisible(true);
		
		this.add(typingPanel);
		this.add(displayPanel);
		this.setSize(new Dimension(typingWidth+displayWidth, windowHeight));
		
		this.typingThread = new Thread(typingPanel);
		this.displayThread = new Thread(displayPanel);
		
		// typingThread are to start before the game starts
		// because it needs to display the message of waiting another client
		typingThread.start();
	}
	@Override
	public void start() {
		// TODO Auto-generated method stub
		state = GameState.RUNNING;
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
		this.state = GameState.END;
		//typingPanel.outPutUnknownMap();
	}
	
	
}
