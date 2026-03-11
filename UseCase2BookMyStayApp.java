/**
 * UseCase2BookMyStayApp
 *
 * Demonstrates object modeling using abstraction, inheritance,
 * polymorphism, and simple availability variables.
 *
 * @author Niranjan Manivannan
 * @version 1.0
 */

public class UseCase2BookMyStayApp {

    public static void main(String[] args) {

        // Creating room objects using polymorphism
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static availability variables
        int singleRoomAvailability = 10;
        int doubleRoomAvailability = 5;
        int suiteRoomAvailability = 2;

        System.out.println("===== BookMyStay Room Availability =====");

        displayRoom(singleRoom, singleRoomAvailability);
        displayRoom(doubleRoom, doubleRoomAvailability);
        displayRoom(suiteRoom, suiteRoomAvailability);

        System.out.println("\nApplication terminated.");
    }

    // Method to display room details
    public static void displayRoom(Room room, int availability) {
        System.out.println("\nRoom Type: " + room.getRoomType());
        System.out.println("Beds: " + room.getBeds());
        System.out.println("Size: " + room.getSize() + " sq ft");
        System.out.println("Price: ₹" + room.getPrice());
        System.out.println("Available: " + availability);
    }
}

/**
 * Abstract class representing a general Room.
 */
abstract class Room {

    private int beds;
    private double size;
    private double price;

    public Room(int beds, double size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getRoomType();
}

/**
 * Single Room implementation
 */
class SingleRoom extends Room {

    public SingleRoom() {
        super(1, 200, 3000);
    }

    public String getRoomType() {
        return "Single Room";
    }
}

/**
 * Double Room implementation
 */
class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 350, 5000);
    }

    public String getRoomType() {
        return "Double Room";
    }
}

/**
 * Suite Room implementation
 */
class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 600, 9000);
    }

    public String getRoomType() {
        return "Suite Room";
    }
}