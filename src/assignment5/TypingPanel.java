package assignment5;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class TypingPanel extends JPanel implements KeyListener, Runnable {
	private GameStage gs;
	private JTextField textField;
	private JLabel label;
	/*
	private HashMap<String, String> knownMap = new HashMap<String, String>();
	private HashMap<String, String> allMap = new HashMap<String, String>();
	*/
	/*
	private boolean isKnownLeft;
	private int bothKnownCount=0;
	private boolean anotherIsOK;
	private boolean correct;
	*/
	
	private int wordY;
	private String wordPath;
	private BufferedImage wordImg;
	
	// Client object who is responsible of this gameStage will call
	// the function to see what word will be displayed from the
	// information the server sent
	public void setWordPath(String wordPath) {
		this.wordPath = wordPath;
		try {
			wordImg = ImageIO.read(new File(wordPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public TypingPanel(Rectangle rec, GameStage gs) {
		// TODO Auto-generated constructor stub
		this.gs = gs;
		
		this.setOpaque(true);
		this.setLayout(null);
		this.setBackground(Color.CYAN);
		this.setBounds(rec);
		

		this.textField = new JTextField();
		this.textField.setBounds(0, this.getBounds().height-30, this.getBounds().width, 30);
		this.textField.setVisible(true);
		this.textField.addKeyListener(this);
		this.add(textField);
		
		label = new JLabel();
		label.setFont(new Font("Serif", Font.PLAIN, 20));
		label.setBounds(10, this.getBounds().height/2, this.getBounds().width, 50);
		label.setVisible(true);
		this.add(label);

		wordY = 20000;
		
	}
	@Override
	// display different content determined by state of game stage
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		if(gs.state==GameState.BEGIN){
			label.setText("Waiting for another client...");
		} else if(gs.state==GameState.RUNNING) {
			label.setText("");
			g.drawImage(wordImg, 10, wordY, null);
		} else if(gs.state==GameState.WAITING) {
			label.setText("Waiting for another answer...");
		} else if(gs.state==GameState.REPEAT) {
			label.setText("Wrong answer! Please enter again");
		}
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			// debug code, which will print out the state of gs once the the state has changed
			System.out.println("gs.state: "+gs.state);
			
			// determine what to do about the word 
			// the client object of this game stage are in charge of decide how to change the state
			if(gs.state==GameState.BEGIN){
				while(gs.state==GameState.BEGIN){
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			} else if(gs.state==GameState.RUNNING) {
				while(gs.state==GameState.RUNNING)
				{
					try {
						Thread.sleep(45);
						if(wordY < this.gs.getHeight() - 70) wordY+=2;
						else wordY = 0;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.repaint();
				}
			} else if(gs.state==GameState.WAITING) {
				repaint();
				while(gs.state==GameState.WAITING){
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					wordY = 0;
				}
			} else if(gs.state==GameState.REPEAT) {
				repaint();
				while(gs.state==GameState.REPEAT){
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				wordY = 0;
			} else if(gs.state==GameState.END) break;
			
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			if (gs.state==GameState.RUNNING){
				String ans = textField.getText();
				gs.client.sendServerMessage(ans);
				gs.state = GameState.WAITING;
				wordY = 0;
			}
			textField.setText("");
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	// old code for assignment 4
	// the function to determine which word to use is now server's responsibility
	/*
	private void wordUpdate()
	{
		if(bothKnownCount > 0){
			wordUpdate_bothKnown();
			return;
		}
		knownIter++;
		unknownIter++;
		if(knownIter >= knownOrder.length){
			knownIter = 0;
			shuffleOrder(knownOrder, knownOrder.length);
		}
		if(unknownIter >= unknownFilePath.length){
			unknownIter = 0;
			shuffleOrder(unknownOrder, unknownOrder.length);
		}
		
		try {
			String path = new String("materials/img/known/" + knownFilePath[knownOrder[knownIter]]);
			knownImg = ImageIO.read(new File(path));
			path = new String("materials/img/unknown/" + unknownFilePath[unknownOrder[unknownIter]]);
			unknownImg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Random random = new Random();
		isKnownLeft = random.nextBoolean();
		wordY = 0;
	}
	private void wordUpdate_bothKnown()
	{
		knownIter++;
		isKnownLeft = true;
		if(knownIter >= knownOrder.length){
			knownIter = 0;
			shuffleOrder(knownOrder, knownOrder.length);
		}
		String path = new String("materials/img/known/" + knownFilePath[knownOrder[knownIter]]);
		try {
			knownImg = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		knownIter++;
		if(knownIter >= knownOrder.length){
			knownIter = 0;
			shuffleOrder(knownOrder, knownOrder.length);
		}
		path = new String("materials/img/known/" + knownFilePath[knownOrder[knownIter]]);
		try {
			unknownImg = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		wordY = 0;
	}
	
	public void outPutUnknownMap()
	{
		try {
			writer = new PrintWriter(new File("output.txt"));
			System.out.println(allMap.size());
			for(HashMap.Entry<String, String> entry:allMap.entrySet()){
					writer.println(entry.getKey()+ " " +entry.getValue());
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
}