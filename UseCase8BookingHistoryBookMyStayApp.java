import java.util.*;

class Reservation {
    String reservationId;
    String roomType;
    String roomId;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String toString() {
        return reservationId + " | " + roomType + " | " + roomId;
    }
}

class BookingHistory {
    private final List<Reservation> history = new ArrayList<>();

    public void add(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAll() {
        return new ArrayList<>(history);
    }
}

class BookingReportService {

    public void printAll(List<Reservation> reservations) {
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public void printCount(List<Reservation> reservations) {
        System.out.println("Total Bookings: " + reservations.size());
    }

    public void printRoomTypeSummary(List<Reservation> reservations) {
        Map<String, Integer> map = new HashMap<>();

        for (Reservation r : reservations) {
            map.put(r.roomType, map.getOrDefault(r.roomType, 0) + 1);
        }

        for (String type : map.keySet()) {
            System.out.println(type + " -> " + map.get(type));
        }
    }
}

public class UseCase8BookingHistoryBookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        history.add(new Reservation("R1", "Deluxe", "D101"));
        history.add(new Reservation("R2", "Suite", "S201"));
        history.add(new Reservation("R3", "Deluxe", "D102"));

        List<Reservation> data = history.getAll();

        reportService.printAll(data);
        reportService.printCount(data);
        reportService.printRoomTypeSummary(data);
    }
}
