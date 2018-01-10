import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class DocumentParser implements Runnable {
	
	private BlockingQueue<Shingle> queue;
	private String file;
	private Deque<String> buffer = new LinkedList<>();
	private int ss,k,docID;
	
	public DocumentParser(BlockingQueue<Shingle> queue, String file, Deque<String> buffer, int ss, int k, int docID) {
		super();
		this.queue = queue;
		this.file = file;
		this.buffer = buffer;
		this.ss = ss;
		this.k = k;
		this.docID = docID;
	}
	
	public void run() {
		
		FileInputStream fis = null;
		String line = null;
		
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		try {
			while((line = br.readLine())!=null){
				
				String uLine = line.toUpperCase();
				String [] words = uLine.split(" ");
				
				addWordsToBuffer(words);
				
				Shingle s = getNextShingle();
				queue.put(s);
				
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Shingle getNextShingle() {
		StringBuilder sb = new StringBuilder();
		int counter= 0;
		
		while(counter < ss){
			if(buffer.peek()!=null) {
				sb.append(buffer.poll());
				counter++;
			}
			else {
				return null;
			}
		}
		return(new Shingle(docID,sb.toString().hashCode()));
		
	}

	private void addWordsToBuffer(String[] words) {
		for(String s:words) {
			buffer.add(s);
		}
		
	}
	
	

}
