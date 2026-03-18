import java.util.*;

class Reservation {
    String reservationId;
    String roomType;
    String roomId;
    boolean active;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
        this.active = true;
    }
}

class InventoryService {
    private final Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public void increment(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public int get(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

class BookingStore {
    private final Map<String, Reservation> reservations = new HashMap<>();

    public void add(Reservation r) {
        reservations.put(r.reservationId, r);
    }

    public Reservation get(String id) {
        return reservations.get(id);
    }
}

class CancellationService {

    private final BookingStore store;
    private final InventoryService inventory;
    private final Stack<String> releasedRooms = new Stack<>();

    public CancellationService(BookingStore store, InventoryService inventory) {
        this.store = store;
        this.inventory = inventory;
    }

    public void cancel(String reservationId) {

        Reservation r = store.get(reservationId);

        if (r == null) {
            System.out.println("FAILED: " + reservationId + " | Reservation not found");
            return;
        }

        if (!r.active) {
            System.out.println("FAILED: " + reservationId + " | Already cancelled");
            return;
        }

        releasedRooms.push(r.roomId);

        inventory.increment(r.roomType);

        r.active = false;

        System.out.println("CANCELLED: " + reservationId + " -> " + r.roomId);
    }

    public void printRollbackStack() {
        System.out.println("Rollback Stack: " + releasedRooms);
    }
}

public class UseCase10CancellationBookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Deluxe", 1);

        BookingStore store = new BookingStore();

        Reservation r1 = new Reservation("R1", "Deluxe", "D101");
        Reservation r2 = new Reservation("R2", "Deluxe", "D102");

        store.add(r1);
        store.add(r2);

        CancellationService service = new CancellationService(store, inventory);

        service.cancel("R1");
        service.cancel("R1");
        service.cancel("R3");

        service.printRollbackStack();

        System.out.println("Available Deluxe: " + inventory.get("Deluxe"));
    }
}
