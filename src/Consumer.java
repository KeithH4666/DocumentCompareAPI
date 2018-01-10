import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer implements Runnable{
	
	private BlockingQueue<Shingle> queue;
	private int K;
	private int[] minHashes;
	private Map<Integer,List<Integer>> map = new HashMap<>();
	private ExecutorService pool;
	
	public Consumer(BlockingQueue<Shingle> queue, int k,int poolsize) {
		super();
		this.queue = queue;
		K = k;
		pool = Executors.newFixedThreadPool(poolsize);
		init();
	}
	
	private void init() {
		Random random = new Random();
		minHashes = new int[K];
		
		for(int i = 0;i<minHashes.length;i++) {
			minHashes[i] = random.nextInt();	
		}
	}
	
	public void run() {
		int docCount = 2;
		
		while(docCount == 0) {
			try {
				Shingle s = queue.take();
				if(s.equals(s)) {
					
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	
}
