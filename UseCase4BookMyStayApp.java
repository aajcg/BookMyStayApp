import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: $" + price);
        System.out.println("Amenities: " + String.join(", ", amenities));
        System.out.println("-----------------------------");
    }
}

// Inventory as State Holder
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    // Read-only access
    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    // Defensive copy to avoid mutation outside
    public Map<String, Integer> getAllAvailability() {
        return new HashMap<>(availability);
    }
}

// Search Service (Read-only logic)
class SearchService {

    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    // Search available rooms
    public List<Room> searchAvailableRooms() {

        List<Room> availableRooms = new ArrayList<>();

        Map<String, Integer> availability = inventory.getAllAvailability();

        for (String roomType : availability.keySet()) {

            int count = availability.get(roomType);

            // Defensive programming check
            if (count > 0 && roomCatalog.containsKey(roomType)) {
                availableRooms.add(roomCatalog.get(roomType));
            }
        }

        return availableRooms;
    }

    public void displayAvailableRooms() {
        List<Room> rooms = searchAvailableRooms();

        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
            return;
        }

        System.out.println("Available Rooms:");
        System.out.println("================");

        for (Room room : rooms) {
            room.displayDetails();
        }
    }
}

// Simulation of Guest searching
public class RoomSearchApp {

    public static void main(String[] args) {

        // Create room catalog (Domain objects)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single",
                new Room("Single", 100, Arrays.asList("WiFi", "TV", "Air Conditioning")));

        roomCatalog.put("Double",
                new Room("Double", 180, Arrays.asList("WiFi", "TV", "Mini Bar")));

        roomCatalog.put("Suite",
                new Room("Suite", 350, Arrays.asList("WiFi", "TV", "Jacuzzi", "Living Area")));

        // Inventory setup
        Inventory inventory = new Inventory();
        inventory.addRoomType("Single", 5);
        inventory.addRoomType("Double", 0); // unavailable
        inventory.addRoomType("Suite", 2);

        // Search service
        SearchService searchService = new SearchService(inventory, roomCatalog);

        // Guest initiates search
        searchService.displayAvailableRooms();
    }
}
