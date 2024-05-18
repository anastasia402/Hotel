package Domain;

import java.util.Collection;

public class Hotel implements Entity<Integer>{
    private int id;
    private String name;
    private double latitude;
    private double logitude;
    private Collection<Room> rooms;

    public Hotel() {}

    public Hotel(int id, String name, double latitude, double logitude, Collection<Room> rooms) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.logitude = logitude;
        this.rooms = rooms;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLogitude() {
        return logitude;
    }

    public void setLogitude(double logitude) {
        this.logitude = logitude;
    }

    public Collection<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Collection<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "Hotel: " + name + "\n" +
                "ID: " + id + "\n" +
                "Location: (" + latitude + ", " + logitude + ")";
    }
}
