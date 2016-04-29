package assignment5;

import java.awt.Color;
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
import javax.swing.JPanel;
import javax.swing.JTextField;



public class TypingPanel extends JPanel implements KeyListener, Runnable {
	private GameStage gs;
	private String[] knownFilePath, unknownFilePath;
	public JTextField textField;
	
	private HashMap<String, String> knownMap, allMap;
	private int []knownOrder;
	private int []unknownOrder;
	private int knownIter=0, unknownIter=0;
	
	private boolean isKnownLeft;
	private int bothKnownCount=0;
	
	private int wordY;
	private BufferedImage knownImg, unknownImg;
	private PrintWriter writer;
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
		
		this.knownFilePath = new String[51];
		File file = new File("materials/known_words.txt");
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Scanner scanner = new Scanner(fileInput);
		
		knownMap = new HashMap<String, String>();
		allMap = new HashMap<String, String>();
		while(scanner.hasNext()){
			String line = scanner.nextLine();
			//System.out.println(line);
			String[] store = line.split(" ");
			knownMap.put(store[0], store[1]);
			allMap.put(store[0], store[1]);
		}
		int j = 0;
		//System.out.println("Before iterate knownMap");
		for(HashMap.Entry<String, String> entry : knownMap.entrySet()){
			//System.out.println(entry.getKey() + " " + entry.getValue());
			knownFilePath[j++] = entry.getKey();
		}
		scanner.close();
		try {
			fileInput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		file = new File("materials/unknown_words.txt");
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println("yoyo");
		}
		scanner = new Scanner(fileInput);
		j=0;
		this.unknownFilePath = new String[73];
		while(scanner.hasNext()){
			unknownFilePath[j++] = scanner.next();
			//System.out.println(unknownFilePath[j-1]);
		}
		scanner.close();
		
		this.knownOrder = new int[51];
		this.unknownOrder = new int[73];
		System.out.println(knownOrder.length);
		System.out.println(unknownOrder.length);
		this.shuffleOrder(knownOrder, knownOrder.length);
		this.shuffleOrder(unknownOrder, unknownOrder.length);
		
		this.wordUpdate();
		
		Random random = new Random();
		isKnownLeft = random.nextBoolean();
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		if(isKnownLeft){
			g.drawImage(knownImg, 10, wordY, null);
			g.drawImage(unknownImg, knownImg.getWidth()+20, wordY, null);
		} else {
			g.drawImage(unknownImg, 10, wordY, null);
			g.drawImage(knownImg, unknownImg.getWidth()+20, wordY, null);
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			if(gs.state==GameState.END) break;
			try {
				Thread.sleep(45);
				if(wordY < this.gs.getHeight() - 70) wordY+=2;
				else {
					wordUpdate();
					textField.setText("");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.repaint();
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(gs.state==GameState.BEGINNING){
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				gs.state = GameState.RUNNING;
				gs.start();
			}
		} else if(gs.state==GameState.RUNNING){
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				//gs.addScore(4);
				//if(gs.currentScore > 0)return;
				String line = textField.getText();
				String[] str = line.split(" ");
				if(str.length != 2){
					wordUpdate();
					textField.setText("");
					return;
				}
				if(bothKnownCount > 0){
					if(knownMap.get(knownFilePath[knownOrder[knownIter-1]]).equals(str[0]) 
						&& knownMap.get(knownFilePath[knownOrder[knownIter]]).equals(str[1]) ){
						bothKnownCount--;
						gs.addScore(4);
						
					} else {
						Random random = new Random();
						bothKnownCount = random.nextInt(3)+1;
					}
				} else {
					//System.out.println(str[0] + " " + str[1]);
					if(isKnownLeft){
						//System.out.println("KnownLeft: " + knownFilePath[knownOrder[knownIter] ] + " " + knownMap.get(knownFilePath[knownOrder[knownIter]]));
						if(knownMap.get(knownFilePath[knownOrder[knownIter]]).equals(str[0]) ){
							gs.addScore(4);
							allMap.put(unknownFilePath[unknownOrder[unknownIter]], str[1]);
						} else {
							Random random = new Random();
							bothKnownCount = random.nextInt(3)+1;
						}
					} else {
						//System.out.println("KnownRight: " + knownFilePath[knownOrder[knownIter]] + " " + knownMap.get(knownFilePath[knownOrder[knownIter]]));
						if(knownMap.get(knownFilePath[knownOrder[knownIter]]).equals(str[1]) ){
							gs.addScore(4);
							allMap.put(unknownFilePath[unknownOrder[unknownIter]], str[0]);
						} else {
							Random random = new Random();
							bothKnownCount = random.nextInt(3)+1;
						}
					}
				}
				this.wordUpdate();
				textField.setText("");
			}
		} else if(gs.state==GameState.END){
			if(e.getKeyChar()==KeyEvent.VK_ENTER){
			}
			else if(e.getKeyChar()==KeyEvent.VK_ESCAPE){
			}
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void shuffleOrder(int[] order, int size)
	{
		Random random = new Random();
		for(int i=0 ; i<size ; i++) order[i] = i;
		for(int i=0 ; i<size ; i++) {
			int toSwap = random.nextInt(size);
			int temp = order[toSwap];
			order[toSwap] = order[i];
			order[i] = temp;
		}
	}
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
		
	}
}