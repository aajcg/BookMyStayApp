import java.util.*;

class InvalidBookingException extends RuntimeException {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class InventoryService {
    private final Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public void validateRoomType(String type) {
        if (!inventory.containsKey(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }
    }

    public void validateAvailability(String type) {
        int count = inventory.get(type);
        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for: " + type);
        }
    }

    public void decrement(String type) {
        int count = inventory.get(type);
        if (count - 1 < 0) {
            throw new InvalidBookingException("Inventory cannot be negative for: " + type);
        }
        inventory.put(type, count - 1);
    }
}

class BookingValidator {

    public void validate(String bookingId, String roomType, InventoryService inventory) {
        if (bookingId == null || bookingId.isEmpty()) {
            throw new InvalidBookingException("Booking ID cannot be empty");
        }

        if (roomType == null || roomType.isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }

        inventory.validateRoomType(roomType);
        inventory.validateAvailability(roomType);
    }
}

class BookingService {

    private final InventoryService inventory;
    private final BookingValidator validator;

    public BookingService(InventoryService inventory, BookingValidator validator) {
        this.inventory = inventory;
        this.validator = validator;
    }

    public void book(String bookingId, String roomType) {
        try {
            validator.validate(bookingId, roomType, inventory);
            inventory.decrement(roomType);
            String roomId = roomType.substring(0,1).toUpperCase() + UUID.randomUUID().toString().substring(0,5);
            System.out.println("CONFIRMED: " + bookingId + " -> " + roomId);
        } catch (InvalidBookingException e) {
            System.out.println("FAILED: " + bookingId + " | " + e.getMessage());
        }
    }
}

public class UseCase9ErrorHandlingBookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Deluxe", 1);
        inventory.addRoomType("Suite", 0);

        BookingValidator validator = new BookingValidator();
        BookingService service = new BookingService(inventory, validator);

        service.book("B1", "Deluxe");
        service.book("B2", "Deluxe");
        service.book("B3", "Suite");
        service.book("B4", "InvalidType");
        service.book("", "Deluxe");
    }
}
