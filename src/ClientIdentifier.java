import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class ClientIdentifier implements Runnable {
    ConcurrentHashMap<Integer, RateLimiter> map = new ConcurrentHashMap<>();
    int clientId;
    int bucketSize;

    public ClientIdentifier(int clientId, int bucketSize) {
        this.clientId = clientId;
        this.bucketSize = bucketSize;
    }

    @Override
    public void run() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH.mm.ss aa");
        RateLimiter rateLimiter = map.get(clientId);
        if (rateLimiter == null) rateLimiter = new RateLimiter(bucketSize);

        long currentTimestamp = System.currentTimeMillis();
        rateLimiter = rateLimiter.processRequest(currentTimestamp);
        boolean allowed = rateLimiter.isAllowed;
        map.put(clientId, rateLimiter);

        //String dateString = String.format("%1$tH:%1$tM:%1$tS", currentTimestamp);
        String dateString = simpleDateFormat.format(new Date(currentTimestamp));
        if (allowed) System.out.println(clientId + "  ::  " + dateString + "  ::  " + allowed);
    }
}