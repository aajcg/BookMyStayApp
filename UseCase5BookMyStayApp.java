import java.util.*;

// Reservation represents a guest's booking request
class Reservation {
    private String guestName;
    private String roomType;
    private String checkInDate;
    private String checkOutDate;

    public Reservation(String guestName, String roomType, String checkInDate, String checkOutDate) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName);
        System.out.println("Requested Room: " + roomType);
        System.out.println("Check-in: " + checkInDate);
        System.out.println("Check-out: " + checkOutDate);
        System.out.println("---------------------------");
    }
}

// BookingRequestQueue manages booking requests using FIFO
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View next request (without removing)
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    // Remove next request (for future allocation stage)
    public Reservation processNextRequest() {
        return requestQueue.poll();
    }

    // Display all queued requests
    public void displayQueue() {
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("Current Booking Request Queue:");
        System.out.println("================================");

        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
    }
}

// Simulation of booking requests
public class BookingRequestApp {

    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guests submitting booking requests
        Reservation r1 = new Reservation("Alice", "Single", "2026-04-01", "2026-04-03");
        Reservation r2 = new Reservation("Bob", "Double", "2026-04-02", "2026-04-05");
        Reservation r3 = new Reservation("Charlie", "Suite", "2026-04-01", "2026-04-04");

        // Add requests to queue (FIFO)
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        System.out.println();

        // Display queued requests
        bookingQueue.displayQueue();

        System.out.println("\nNext request to process (FIFO):");
        Reservation next = bookingQueue.peekNextRequest();
        if (next != null) {
            next.displayReservation();
        }
    }
}
