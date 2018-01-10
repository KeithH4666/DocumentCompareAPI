package ie.gmit.sw;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{

	private BlockingQueue<Shingle> queue;
	private int k;
	private int [] minHashes;
	
	Map <Integer,List<Integer>> map;	
	
	
	//Constructor
	public Consumer(BlockingQueue<Shingle> q, Map <Integer,List<Integer>> map,int k,int[] hashes) {
		super();
		this.queue = q;
		this.k = k;
		this.map = map;
		this.minHashes = hashes;
	}
	
	public void run() {
		int docCount = 1;
		int max = Integer.MAX_VALUE;
		int value = 0;
		
		//While document is available (either 1/2)
		while(docCount>0) {
			
			try {		
				Shingle s = queue.take();
				
				//Has EOF been encountered in queue (Solve for null pointer error)
				if(s.getHashCode()==0) {
					docCount--;
					continue;
				}

				List<Integer> list = map.get(s.getDocID());
				
				if(list == null) {
					list = new ArrayList<Integer>(k);
					
					for(int j = 0; j <k;j++) {
						list.add(j, max);
					}
					
					map.put(s.getDocID(), list);	
				}

				
				for(int i = 0;i<minHashes.length;i++) {
					
					value =s.getHashCode() ^ minHashes[i];
									
					if(list.get(i) > value) {
						list.set(i, value);
					}
				}
					
			}
			
			 catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// this is a blocking method
			
		}
	}


}
