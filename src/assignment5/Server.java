package assignment5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	private ServerSocket serverSocket;
	private ConnectionThread[] connections = new ConnectionThread[2];
	
	public Server(int portNum) {
		try {
			this.serverSocket = new	 ServerSocket(portNum);
			System.out.printf("Server starts listening on port %d.\n", portNum);
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
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
		// Make sure you do create a connectionThread and add it into 'connections'		
	}
	
	
	
	
	public static void main(String[] args) {
		Server server = new Server(8000);
		server.accept2();
	}
	
	// Define an inner class (class name should be ConnectionThread)
	public class ConnectionThread extends Thread{
		private Socket client;
		private PrintWriter writer;
		private BufferedReader reader;
		
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
					String line = reader.readLine();
					System.out.println("server get client's message: " + line);
				} catch(IOException e) {
					e.printStackTrace();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public void sendMessage(String message){
			writer.println(message);
			writer.flush();
		}
	}

}
