import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    String id;
    String roomType;
    String roomId;

    public Reservation(String id, String roomType, String roomId) {
        this.id = id;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String toString() {
        return id + " " + roomType + " " + roomId;
    }
}

class Inventory implements Serializable {
    Map<String, Integer> data = new HashMap<>();

    public void add(String type, int count) {
        data.put(type, count);
    }

    public String toString() {
        return data.toString();
    }
}

class DataStore implements Serializable {
    List<Reservation> bookings = new ArrayList<>();
    Inventory inventory = new Inventory();
}

class PersistenceService {

    private final String file = "bookmystay.db";

    public void save(DataStore store) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(store);
            out.close();
            System.out.println("STATE SAVED");
        } catch (Exception e) {
            System.out.println("SAVE FAILED");
        }
    }

    public DataStore load() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            DataStore store = (DataStore) in.readObject();
            in.close();
            System.out.println("STATE RESTORED");
            return store;
        } catch (Exception e) {
            System.out.println("NO VALID STATE FOUND, STARTING FRESH");
            return new DataStore();
        }
    }
}

public class UseCase12PersistenceBookMyStayApp {

    public static void main(String[] args) {

        PersistenceService ps = new PersistenceService();

        DataStore store = ps.load();

        if (store.inventory.data.isEmpty()) {
            store.inventory.add("Deluxe", 2);
            store.inventory.add("Suite", 1);
        }

        store.bookings.add(new Reservation("R1", "Deluxe", "D101"));
        store.bookings.add(new Reservation("R2", "Suite", "S201"));

        System.out.println("Bookings:");
        for (Reservation r : store.bookings) {
            System.out.println(r);
        }

        System.out.println("Inventory:");
        System.out.println(store.inventory);

        ps.save(store);
    }
}
