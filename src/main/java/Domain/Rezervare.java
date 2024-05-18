package Domain;

import java.time.LocalDate;
import java.util.Objects;

public class Rezervare implements Entity<Integer>{
    private int id;
    private User user;
    String startDate;
    String endDate;
    private Hotel hotel;
    private Room room;

    private String review;

    public Rezervare(int id, User user, String startDate, String endDate, Hotel hotel, Room room, String review) {
        this.id = id;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotel = hotel;
        this.room = room;
        this.review = review;
    }

    public Rezervare(int id, User user, String startDate, String endDate, Hotel hotel, Room room) {
        this.id = id;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotel = hotel;
        this.room = room;
        this.review = "";
    }

    public Rezervare(User user, String startDate, String endDate, Hotel hotel, Room room) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotel = hotel;
        this.room = room;
        this.review = "";
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Rezervare() {}

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rezervare rezervare)) return false;
        return Objects.equals(getUser(), rezervare.getUser()) && Objects.equals(getStartDate(), rezervare.getStartDate()) && Objects.equals(getEndDate(), rezervare.getEndDate()) && Objects.equals(getHotel(), rezervare.getHotel()) && Objects.equals(getRoom(), rezervare.getRoom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getHotel());
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isAvailable(LocalDate startDate, LocalDate endDate) {
        LocalDate localStartDate = LocalDate.parse(this.startDate);
        LocalDate localEndDate = LocalDate.parse(this.endDate);

        return localEndDate.isBefore(startDate) && localStartDate.isAfter(endDate);
    }

    @Override
    public String toString() {
        return "Reservation at " + hotel.getName() + ":\n" +
                "User: " + user.getNume() + "\n" +
                "Room: " + room.getId() + "\n" +
                "Start Date: " + startDate + "\n" +
                "End Date: " + endDate + "\n" +
                "Total cost: "+room.getPrice()*(int)LocalDate.parse(startDate).datesUntil(LocalDate.parse(endDate)).count() + "\n" +
                "Review: "+this.getReview();
    }
}
