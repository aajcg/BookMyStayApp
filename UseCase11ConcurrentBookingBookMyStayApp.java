import java.util.*;
import java.util.concurrent.*;

class BookingRequest {
    String id;
    String roomType;

    public BookingRequest(String id, String roomType) {
        this.id = id;
        this.roomType = roomType;
    }
}

class InventoryService {
    private final Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Deluxe", 2);
    }

    public synchronized boolean allocate(String type) {
        int count = inventory.getOrDefault(type, 0);
        if (count <= 0) return false;
        inventory.put(type, count - 1);
        return true;
    }

    public int get(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

class BookingProcessor implements Runnable {

    private final Queue<BookingRequest> queue;
    private final InventoryService inventory;
    private final Set<String> allocated = new HashSet<>();

    public BookingProcessor(Queue<BookingRequest> queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            BookingRequest req;

            synchronized (queue) {
                if (queue.isEmpty()) break;
                req = queue.poll();
            }

            if (req != null) {
                process(req);
            }
        }
    }

    private void process(BookingRequest req) {

        synchronized (this) {
            boolean success = inventory.allocate(req.roomType);

            if (!success) {
                System.out.println(Thread.currentThread().getName() + " FAILED: " + req.id);
                return;
            }

            String roomId = "R" + UUID.randomUUID().toString().substring(0, 4);

            if (allocated.contains(roomId)) {
                System.out.println(Thread.currentThread().getName() + " DUPLICATE: " + roomId);
                return;
            }

            allocated.add(roomId);

            System.out.println(Thread.currentThread().getName() +
                    " CONFIRMED: " + req.id + " -> " + roomId);
        }
    }
}

public class UseCase11ConcurrentBookingBookMyStayApp {

    public static void main(String[] args) throws InterruptedException {

        Queue<BookingRequest> queue = new LinkedList<>();

        queue.add(new BookingRequest("B1", "Deluxe"));
        queue.add(new BookingRequest("B2", "Deluxe"));
        queue.add(new BookingRequest("B3", "Deluxe"));
        queue.add(new BookingRequest("B4", "Deluxe"));

        InventoryService inventory = new InventoryService();

        BookingProcessor processor = new BookingProcessor(queue, inventory);

        Thread t1 = new Thread(processor, "T1");
        Thread t2 = new Thread(processor, "T2");
        Thread t3 = new Thread(processor, "T3");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Remaining Deluxe: " + inventory.get("Deluxe"));
    }
}
