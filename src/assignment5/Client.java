package assignment5;

import java.awt.KeyEventPostProcessor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;

import javax.swing.JFrame;

public class Client{
	private String destinationIPAddr;
	private int destinationPortNum;
	private Socket socket;
	
	private PrintWriter writer;
	private BufferedReader	reader;
	private ClientReadThread readThread;
	
	private GameStage gameStage;
	
	public Client() {
		// TODO Auto-generated constructor stub
		gameStage = new GameStage(this);
		gameStage.setVisible(true);
		
	}
	private void connect() {
		// TODO Auto-generated method stub
		try {
			socket = new Socket(destinationIPAddr, destinationPortNum);
			writer = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		readThread = new ClientReadThread(reader);
		readThread.start();
	}
	
	public void sendServerMessage(String message)
	{
		writer.println(message);
		writer.flush();
	}
	
	
	public static void main(String[] args) {
		Client client = new Client();
		client.setIPAddress("127.0.0.1").setPort(8000).connect();
	}
	public Client setIPAddress(String IPAddress) {
		this.destinationIPAddr = IPAddress;
		return this;
	}
	public Client setPort(int portNum) {
		this.destinationPortNum = portNum;
		return this;
	}
	
	class ClientReadThread extends Thread{
		private BufferedReader reader;
		public ClientReadThread(BufferedReader reader){
			this.reader = reader;
		}
		
		public void run(){
			System.out.println("ClientThread Start()");
			while(true){
				try{
					String line = this.reader.readLine();
					if(gameStage.state==GameState.BEGIN){
						if(line.equals("OK")){
							
						}
					}
					else if(gameStage.state==GameState.RUNNING){
						
					}
					else if(gameStage.state==GameState.WAITING){
						
					}
					else if(gameStage.state==GameState.END){
						
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
