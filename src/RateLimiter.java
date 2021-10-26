import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RateLimiter {

    private long bucketSize;
    private long currentBucketSize;
    private double lastProcessedTimestamp;
    private Lock lock;
    public boolean isAllowed = false;

    public RateLimiter(int bucketSize) {
        this.bucketSize = bucketSize;
        currentBucketSize = 0;
        lock = new ReentrantLock();
    }

    public RateLimiter processRequest(long currentTimestamp) {
        lock.lock();
        try {
            refill(currentTimestamp);
            this.isAllowed = this.currentBucketSize > 0;
            if (this.isAllowed) this.currentBucketSize--;
            return this;
        } finally {
            lock.unlock();
        }
    }

    private void refill(long currentTimestamp) {
        double incrementIntervalInMs = ((double) 1000) / ((double) bucketSize);
        double elapsedTimeSinceLastRequestInMs = ((double) currentTimestamp) - lastProcessedTimestamp;
        int additionalAllowableRequests = (int) Math.floor(elapsedTimeSinceLastRequestInMs / incrementIntervalInMs);

        currentBucketSize = Math.min(currentBucketSize + additionalAllowableRequests, bucketSize);
        lastProcessedTimestamp += (((double) additionalAllowableRequests) * incrementIntervalInMs);
    }
}