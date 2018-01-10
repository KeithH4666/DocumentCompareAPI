package ie.gmit.sw;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class DocumentParser implements Runnable {
	
	private BlockingQueue <Shingle> queue;
	private String file;
	private int shingleSize;
	private int k;
	private Deque<String>Buffer=new LinkedList<>();
	
	public DocumentParser(String f, int ss, BlockingQueue<Shingle> q, int k) {
		this.file=f;
		this.queue = q;
		this.shingleSize=ss;
		this.k=k;
	}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line= null;
			//loop through all the lines in the file
			while((line = br.readLine())!=null){
				
				String uLine= line.toUpperCase();
				
				//split the line into words seperated by spaces
				String [] words = uLine.split(" ");
				addWordsToBuffer(words);
				Shingle s = getNextShingle();
				
				//add shingle to the queue
				if(s == null) {
					continue;
				}
				queue.put(s);
			}
			
			//Put new shingle in queue
			queue.put(new Shingle(0,0));
			
			flushBuffer();
			
			//Close reader
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private void flushBuffer() throws InterruptedException  {
		while(Buffer.size()>0) {
			Shingle s =getNextShingle();
			if(s != null) {
				queue.put(s);	
			}else {
				queue.put(new Poison(0,0));
			}
		}
	}

	private Shingle getNextShingle() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		while(counter < shingleSize) {
			if(Buffer.peek()!=null) 
				sb.append(Buffer.poll());
			counter++;
		}
		if(sb.length()>0) {
			int docID=0;
			
			//returns new instance off Shingle class passing parameters of doc id (1/2) plus shingle in hashcode form
			return (new Shingle(docID,sb.toString().hashCode()));
		}
		else {
			return null;
		}
	}

	private void addWordsToBuffer(String[] words) {
		//loops though array of words
		for(String s: words) {
			
			//Adds to deque
			Buffer.add(s);
		}
	}
}