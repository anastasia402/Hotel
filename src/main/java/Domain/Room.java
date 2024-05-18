package Domain;

import java.util.Objects;

public class Room implements Entity<Integer>{
    private int roomNumber;
    private int type;
    private int price;
    private boolean isAvailable;

    public Room() {}

    public Room(int roomNumber, int type, int price, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    @Override
    public Integer getId() {
        return this.roomNumber;
    }

    @Override
    public void setId(Integer id) {
        this.roomNumber = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + ": " +
                "Type " + type + ", " +
                "$" + price + " per night, " +
                "Available: Yes";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        return roomNumber == room.roomNumber && getType() == room.getType() && getPrice() == room.getPrice() && isAvailable() == room.isAvailable();
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, getType(), getPrice(), isAvailable());
    }
}
