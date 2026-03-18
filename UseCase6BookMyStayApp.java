import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

class BookingRequest {
    String bookingId;
    String roomType;

    public BookingRequest(String bookingId, String roomType) {
        this.bookingId = bookingId;
        this.roomType = roomType;
    }
}

class InventoryService {
    private final Map<String, AtomicInteger> inventory = new ConcurrentHashMap<>();

    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, new AtomicInteger(count));
    }

    public boolean isAvailable(String roomType) {
        return inventory.containsKey(roomType) && inventory.get(roomType).get() > 0;
    }

    public boolean decrement(String roomType) {
        AtomicInteger count = inventory.get(roomType);
        if (count == null) return false;

        while (true) {
            int current = count.get();
            if (current <= 0) return false;
            if (count.compareAndSet(current, current - 1)) {
                return true;
            }
        }
    }

    public int getAvailable(String roomType) {
        return inventory.getOrDefault(roomType, new AtomicInteger(0)).get();
    }
}

class BookingService {

    private final Queue<BookingRequest> requestQueue = new ConcurrentLinkedQueue<>();
    private final InventoryService inventoryService;

    // Track allocated room IDs globally (uniqueness)
    private final Set<String> allocatedRoomIds = ConcurrentHashMap.newKeySet();

    // Map room type → allocated room IDs
    private final Map<String, Set<String>> roomTypeToRooms = new ConcurrentHashMap<>();

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void addBookingRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll();
            if (request != null) {
                processSingleBooking(request);
            }
        }
    }

    private void processSingleBooking(BookingRequest request) {
        String roomType = request.roomType;

        synchronized (this) { // atomic block

            if (!inventoryService.isAvailable(roomType)) {
                System.out.println("Booking FAILED for " + request.bookingId + " (No availability)");
                return;
            }

            String roomId = generateUniqueRoomId(roomType);

            // Assign room
            allocatedRoomIds.add(roomId);
            roomTypeToRooms
                    .computeIfAbsent(roomType, k -> ConcurrentHashMap.newKeySet())
                    .add(roomId);

            // Update inventory (must succeed after availability check)
            boolean updated = inventoryService.decrement(roomType);

            if (!updated) {
                // rollback
                allocatedRoomIds.remove(roomId);
                roomTypeToRooms.get(roomType).remove(roomId);
                System.out.println("Booking FAILED (Race condition) for " + request.bookingId);
                return;
            }

            // Success
            System.out.println("Booking CONFIRMED: " +
                    request.bookingId + " → Room ID: " + roomId);
        }
    }

    private String generateUniqueRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 1).toUpperCase() +
                    UUID.randomUUID().toString().substring(0, 6);
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    public void printAllocations() {
        System.out.println("\n--- Allocations ---");
        for (String type : roomTypeToRooms.keySet()) {
            System.out.println(type + " → " + roomTypeToRooms.get(type));
        }
    }
}

public class UseCase6BookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        inventoryService.addRoomType("Deluxe", 2);
        inventoryService.addRoomType("Suite", 1);

        BookingService bookingService = new BookingService(inventoryService);

        // Add booking requests (FIFO)
        bookingService.addBookingRequest(new BookingRequest("B1", "Deluxe"));
        bookingService.addBookingRequest(new BookingRequest("B2", "Deluxe"));
        bookingService.addBookingRequest(new BookingRequest("B3", "Deluxe")); // should fail
        bookingService.addBookingRequest(new BookingRequest("B4", "Suite"));
        bookingService.addBookingRequest(new BookingRequest("B5", "Suite")); // should fail

        // Process bookings
        bookingService.processBookings();

        // Print results
        bookingService.printAllocations();
    }
}
