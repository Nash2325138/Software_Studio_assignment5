package assignment5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class Reassembler {
	public static void main(String[] args) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(new File(args[0]) );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
		Scanner scanner = new Scanner(inputStreamReader);
		
		String[][] store = new String[99][99];
		while(scanner.hasNext())
		{
			String total, temp;
			total = scanner.nextLine();
			temp = total.substring(0, 5);
			String []str = temp.split("-");
			int row = Integer.valueOf(str[0]);
			int col = Integer.valueOf(str[1]);
			String meaning = total.substring(10, total.length());
			//System.out.println(row  + " " + col + " " + meaning);
			store[row][col] = meaning;
		}
		
		for(int i=1 ; i<=15 ; i++){
			for(int j=1 ; j<=15 ; j++){
				if(store[i][j]==null) System.out.print("___ ");
				else System.out.print(store[i][j] + " ");
			}
			System.out.println();
		}
		
		try {
			fileInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanner.close();
	}
}
