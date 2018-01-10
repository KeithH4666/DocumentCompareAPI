package ie.gmit.sw;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {

	int k; //minHashes
	int j; //shingles
	private int [] minHashes;

	public Launcher(String fileName1, String fileName2, int hashes,int shingles) throws InterruptedException  {
		k = hashes;
		j = shingles;
		Random random = new Random();
		
		minHashes = new int [k];
		
		//populating minHashes table with random integers
		for (int i=0;i<minHashes.length;i++) {
			minHashes[i] = random.nextInt();
		}

		//Queues of Shingles for each file
		
		BlockingQueue <Shingle> q1 = new LinkedBlockingQueue<>();
		BlockingQueue <Shingle> q2 = new LinkedBlockingQueue<>();
		
		//Minihash maps
		
		Map <Integer,List<Integer>> m1 = new HashMap<>();
		Map <Integer,List<Integer>> m2 = new HashMap<>();
		
		// Document Parser Threads
		
		Thread t1 = new Thread(new DocumentParser(fileName1,5,q1,k),"T1");
		Thread t2 = new Thread(new DocumentParser(fileName2,5,q2,k),"T2");
		
		t1.start();
		t2.start();

		t1.join();
		t2.join();

		// Consumer threads- creates mini-hashes for Jaccard indexing
		
		Thread t3 = new Thread(new Consumer(q1,m1,k,minHashes),"T3");
		Thread t4 = new Thread(new Consumer(q2,m2,k,minHashes),"T4");
		
		t3.start();
		t4.start();
		
		t3.join();
		t4.join();

		// code here to perform Jaccard calculation on 2 HashMaps
		
		float result = Jaccard(m1.get(0),m2.get(0));
		
		System.out.println("Similarity result: " + result + "%");
		
		if(result > 50.0) {
			System.out.println("Because of this result considering you input shingle size, Both text documents are very similar.");
		}else {
			System.out.println("Because of this result considering you input shingle size, Both text documents are not similar.");
		}
	}
	
	float Jaccard(List<Integer> x,List<Integer> y) {
		
		float result = 0.0f;
		
		//List to hold the intersection of both documents
		List<Integer> intersection = new ArrayList<Integer>(x);
		
		intersection.retainAll(y);
		
		//Calculates result
		result = ((float)intersection.size()/k)*100;
		
		return result;
	}
}