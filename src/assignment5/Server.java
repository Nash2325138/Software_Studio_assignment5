package assignment5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

enum ServerState{
	BEGIN, BOTH_RUNNING, SINGLE_RUNNING, END
}
public class Server {
	private int currentScore, winScore;
	private ServerSocket serverSocket;
	private ConnectionThread[] connections = new ConnectionThread[2];
	private ServerState serverState;
	
	private String[] knownFilePath = new String[51];
	private String[] unknownFilePath = new String[73];;
	private int []knownOrder = new int[51];
	private int []unknownOrder = new int[73];
	private int knownIter=0, unknownIter=0;
	
	private String wordPath;
	
	public void initialWords()
	{
		try {
			File file = new File("materials/known_words.txt");
			FileInputStream fileInput = null;
			fileInput = new FileInputStream(file);
			Scanner scanner = new Scanner(fileInput);
			int j = 0;
			while(scanner.hasNext()){
				String line = scanner.nextLine();
				//System.out.println(line);
				String[] store = line.split(" ");
				knownFilePath[j++] = store[0];
			}
			scanner.close();
			fileInput.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			File file = new File("materials/unknown_words.txt");
			FileInputStream fileInput = null;
			fileInput = new FileInputStream(file);
			Scanner scanner = new Scanner(fileInput);
			int j=0;
			while(scanner.hasNext()){
				unknownFilePath[j++] = scanner.next();
				//System.out.println(unknownFilePath[j-1]);
			}
			scanner.close();
			fileInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(knownOrder.length);
		System.out.println(unknownOrder.length);
		this.shuffleOrder(knownOrder, knownOrder.length);
		this.shuffleOrder(unknownOrder, unknownOrder.length);
		/*for(String str:knownFilePath){
			System.out.println(str);
		}
		for(String str:unknownFilePath){
			System.out.println(str);
		}*/
	}
	
	public Server(int portNum) {
		try {
			this.serverSocket = new	 ServerSocket(portNum);
			System.out.printf("Server starts listening on port %d.\n", portNum);
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		serverState = ServerState.BEGIN;
		initialWords();
		winScore = 100;
		currentScore = 0;
	}
	
	public void accept2() {
		System.out.println("Server starts waiting for client.");
		// Create a loop to make server wait for 2 client
		for(int i=0 ; i<2 ; i++){
			try{
				Socket client = this.serverSocket.accept();
				connections[i]= new ConnectionThread(client);
				connections[i].start();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Have acceped 2 connections");
		sendMessageToBothClient("OK");
		serverState = ServerState.BOTH_RUNNING;
		sendMessageToBothClient(swapWord());
	}
	
	public static void main(String[] args) {
		Server server = new Server(8000);
		server.accept2();
	}
	public boolean ansIsTheSame()
	{
		if(connections[0].ans.equals(connections[1].ans)) return true;
		else return false;
	}
	
	public void sendMessageToBothClient(String message) {
		for(int i=0 ; i<2 ; i++){
			connections[i].sendMessage(message);
		}
	}
	
	// Define an inner class (class name should be ConnectionThread)
	public class ConnectionThread extends Thread{
		private Socket client;
		private PrintWriter writer;
		private BufferedReader reader;
		private String ans;
		
		public ConnectionThread(Socket client){
			this.client = client;
			try{
				writer = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream()));
				reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		public void run(){
			while(true){
				try{
					// use state of game stage to send/receive message to client
					// details are illustrated in Readme.txt
					String line = reader.readLine();
					if(Server.this.serverState==ServerState.BEGIN){
						System.out.println("Begin impossible");
					}
					else if(Server.this.serverState==ServerState.BOTH_RUNNING){
						ans = new String(line);
						Server.this.serverState = ServerState.SINGLE_RUNNING;
					}
					else if(Server.this.serverState==ServerState.SINGLE_RUNNING){
						ans = new String(line);
						Thread.sleep(200);
						if(Server.this.ansIsTheSame()){
							sendMessageToBothClient("Same");
							currentScore += 4;
							sendMessageToBothClient(swapWord());
							if(currentScore >= winScore) Server.this.serverState = ServerState.END;
							else Server.this.serverState = ServerState.BOTH_RUNNING;
						} else {
							sendMessageToBothClient("Diffirent");
							Server.this.serverState = ServerState.BOTH_RUNNING;
						}
					}
					else if(Server.this.serverState==ServerState.END){
						break;
					}
				} catch(IOException e) {
					e.printStackTrace();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		public void sendMessage(String message)
		{
			writer.println(message);
			writer.flush();
		}
	}
	
	// randomly select a word from known or unknown words
	// then return the path of the picture of the word
	// use fuffleOrder in the constructor to get "randomlt select"
	public String swapWord(){
		Random random = new Random();
		if(random.nextInt(51+73) < 51)
		{
			knownIter++;
			if(knownIter >= knownOrder.length){
				knownIter = 0;
				shuffleOrder(knownOrder, knownOrder.length);
			}
			return new String("materials/img/known/" + knownFilePath[knownOrder[knownIter]]);
		}
		else
		{
			unknownIter++;
			if(unknownIter >= unknownFilePath.length){
				unknownIter = 0;
				shuffleOrder(unknownOrder, unknownOrder.length);
			}
			return new String("materials/img/unknown/" + unknownFilePath[unknownOrder[unknownIter]]);
		}
	}
	
	// shuffle the order of a array
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
	
}
