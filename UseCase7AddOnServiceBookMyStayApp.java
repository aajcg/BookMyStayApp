import java.util.*;

// Represents an optional add-on service
class AddOnService {
    private final String serviceId;
    private final String name;
    private final double cost;

    public AddOnService(String serviceId, String name, double cost) {
        this.serviceId = serviceId;
        this.name = name;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " ($" + cost + ")";
    }
}

// Manages mapping between reservation and services
class AddOnServiceManager {

    // reservationId → list of services
    private final Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    // Add services to a reservation
    public void addServices(String reservationId, List<AddOnService> services) {
        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .addAll(services);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, Collections.emptyList());
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        return reservationServices
                .getOrDefault(reservationId, Collections.emptyList())
                .stream()
                .mapToDouble(AddOnService::getCost)
                .sum();
    }

    // Print summary
    public void printReservationServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        System.out.println("\nReservation: " + reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Selected Services:");
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }

        System.out.println("Total Add-On Cost: $" + calculateTotalCost(reservationId));
    }
}

// Simulating existing reservation system (unchanged)
class Reservation {
    String reservationId;
    String roomId;

    public Reservation(String reservationId, String roomId) {
        this.reservationId = reservationId;
        this.roomId = roomId;
    }
}

public class UseCase7AddOnServiceBookMyStayApp {

    public static void main(String[] args) {

        // Existing reservation (from UC6)
        Reservation r1 = new Reservation("R1", "D12345");

        // Create add-on services
        AddOnService breakfast = new AddOnService("S1", "Breakfast", 20.0);
        AddOnService airportPickup = new AddOnService("S2", "Airport Pickup", 50.0);
        AddOnService spa = new AddOnService("S3", "Spa Access", 80.0);

        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addServices(r1.reservationId, Arrays.asList(breakfast, spa));
        manager.addServices(r1.reservationId, Arrays.asList(airportPickup));

        // Print details
        manager.printReservationServices(r1.reservationId);
    }
}
