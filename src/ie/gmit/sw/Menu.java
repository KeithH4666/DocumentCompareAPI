package ie.gmit.sw;
import java.util.Scanner;

public class Menu {

	public void show() throws InterruptedException  {
		
		String fileNameA;
		String fileNameB;
		int shingles;
		int poolSize;
		Scanner console = new Scanner(System.in);
		
		//Take in the first file
		System.out.print("\nEnter File Name A: ");
		fileNameA = console.nextLine();
		
		//Take in the second file
		System.out.print("\nEnter File Name B: ");
		fileNameB = console.nextLine();
		
		//Take in amount of shingles user wants to use.
		System.out.print("\nEnter Number of Shingles (Optimum for text files 3-5): ");
		shingles = console.nextInt();
		
		//Calls Launcher, gives both file name, wordpool size and shingle size
		new Launcher(fileNameA,fileNameB,100,shingles);
		console.close();
	}

}