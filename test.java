package source;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class RWSample {
    private final Map<String,String> m = new TreeMap<String,String>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
    public String get(String key) {
        r.lock();
        System.out.println("read lock locked!");
        try {
            return m.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }  finally{
            r.unlock();
        }
    }
    public String put(String key,String entry) {
        w.lock();
        System.out.println("write lock locked!");
        try {
            return m.put(key, entry);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            w.unlock();
        }
    }
}

/**
 * test
 */
public class StampedSample {
    private final StampedLock sl  = new StampedLock();

    void mutete(){
        long stamp = sl.writeLock();
        try {
            write();
        } catch (Exception e) {
            //TODO: handle exception
        }finally{
            sl.unlockWrite(stamp);
        }
    }

    Data access(){
        long stamp = sl.tryOptimisticRead();
        Data data = read();
        if(!sl.validate(stamp)){
            stamp =sl.readLock();
            try {
                data = read();
            } catch (Exception e) {
                //TODO: handle exception
            } finally{
                sl.unlockRead(stamp);
            }
        }
        return data;
    }
}

/**
 * test
 */
public class TestCountDownLatch {
    private CountDownLatch countDownLatch = new CountDownLatch(4);
    public static void main(String[] args) {
        TestCountDownLatch testCountDownLatch = new TestCountDownLatch();
        testCountDownLatch.begin();
    }
    private class Runner implements Runnable {
        private int result;
        public Runner(int result){
            this.result =result;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                //TODO: handle exception
                e.printStackTrace();
            }
            
        }
    }
    private void begin(){
        System.out.println("run");
        Random random = new Random(System.currentTimeMillis());
        for(int i = 0 ;i <4 ;i ++){
            int result = random.nextInt()+1;
            new Thread(new Runner(result)).start();
        }
        try {
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("all runn finish!");
    }
}