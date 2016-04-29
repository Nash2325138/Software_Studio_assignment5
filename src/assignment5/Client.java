package assignment5;

import java.awt.KeyEventPostProcessor;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	private String destinationIPAddr;
	private int destinationPortNum;
	private Socket socket;
	
	private PrintWriter printer;
	private BufferedReader	reader;
	
	private GameStage gameStage;
	
	public Client() {
		// TODO Auto-generated constructor stub
		gameStage = new GameStage(this);
		gameStage.setVisible(true);
		
	}
}
