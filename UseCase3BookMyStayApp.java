import java.util.HashMap;

/**
 * UseCase3BookMyStayApp
 *
 * Demonstrates centralized room inventory management using HashMap.
 * Availability is stored in a single data structure instead of scattered variables.
 *
 * @author Niranjan Manivannan
 * @version 1.0
 */
public class UseCase3BookMyStayApp {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display inventory
        System.out.println("===== BookMyStay Room Inventory =====");
        inventory.displayInventory();

        // Example update
        System.out.println("\nBooking a Single Room...");
        inventory.updateAvailability("Single Room", -1);

        // Display updated inventory
        System.out.println("\nUpdated Inventory:");
        inventory.displayInventory();
    }
}

/**
 * RoomInventory class manages centralized availability of rooms.
 */
class RoomInventory {

    private HashMap<String, Integer> roomAvailability;

    /**
     * Constructor initializes room inventory
     */
    public RoomInventory() {

        roomAvailability = new HashMap<>();

        // Register room types with availability
        roomAvailability.put("Single Room", 10);
        roomAvailability.put("Double Room", 5);
        roomAvailability.put("Suite Room", 2);
    }

    /**
     * Retrieve availability of a specific room type
     */
    public int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 0);
    }

    /**
     * Update availability of a room type
     */
    public void updateAvailability(String roomType, int change) {

        int current = getAvailability(roomType);
        roomAvailability.put(roomType, current + change);
    }

    /**
     * Display all room inventory
     */
    public void displayInventory() {

        for (String roomType : roomAvailability.keySet()) {
            System.out.println(roomType + " Available: " + roomAvailability.get(roomType));
        }
    }
}