package Controller;

import Domain.*;
import Service.HotelService;
import Service.ReservationService;
import Service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static java.lang.Math.*;

public class Controller {
    private static final LocalTime CHECK_IN = LocalTime.NOON;
    private HotelService hotelService;
    private UserService userService;
    private ReservationService reservationService;
    private HashMap<Integer, List<Rezervare>> reservationsPerHotel;

    public Controller(HotelService hotelService, UserService userService, ReservationService reservationService) {
        this.hotelService = hotelService;
        this.userService = userService;
        this.reservationService = reservationService;
        this.reservationsPerHotel = new HashMap<>();
    }

    public Collection<Hotel> findHotelsInRadius(int radius, int userId) {
        Collection<Hotel> nearbyHotels = new ArrayList<>();
        double userLat = convertUsingPositionLatitude(userService.getLatitudeOfUser(userId));
        System.out.println("userLat: " + userLat);
        double userLong = convertUsingPositionLongitude(userService.getUserLongitude(userId));
        System.out.println("userLong" + userLong);

        for (Hotel hotel : hotelService.getAll()) {
            double hotelLat = convertUsingPositionLatitude(hotel.getLatitude());
            System.out.println("hoteLat " + hotelLat);
            double hotelLong = convertUsingPositionLongitude(hotel.getLogitude());

            double distance = round(calculateDistance(userLat, userLong, hotelLat, hotelLong));
            System.out.println("distanta " + distance / 10 + "km");
            if (distance/10 <= radius)
                nearbyHotels.add(hotel);
        }
        return nearbyHotels;
    }

    public double calculateDistance(double userLat, double userLong, double hotelLat, double hotelLong) {
        double distance = 0;
        distance = sqrt(Math.pow(userLat - hotelLat, 2) + Math.pow(userLong - hotelLong, 2));
        return distance;
    }

    public double convertUsingPositionLatitude(double latitude) {
        double distanceLatitudeMeters = 0;
        double phiLat = Math.toRadians(latitude);
        distanceLatitudeMeters = 111132.92 - 559.82 * cos(2 * phiLat) + 1.175 * cos(4 * phiLat) - 0.0023 * cos(6 * phiLat);
        return distanceLatitudeMeters;
    }

    public double convertUsingPositionLongitude(double latitude) {
        double distanceLongitudeMeters = 0;
        double phiLat = Math.toRadians(latitude);
        distanceLongitudeMeters = 111412.84 * cos(phiLat) - 93.5 * cos(3 * phiLat) + 0.118 * cos(5 * phiLat);
        return distanceLongitudeMeters;
    }

    public Collection<Room> findAvailableRoom(int hotelId, String startDate, String endDate){
        Collection<Room> rooms = new ArrayList<>();
        LocalDate filteringStartDate = LocalDate.parse(startDate);
        LocalDate filteringEndDate = LocalDate.parse(endDate);

        Hotel hotel = hotelService.findHotelById(hotelId);
        rooms = hotel.getRooms();

        Set<Integer> reservedRoomNumbers = new HashSet<>();

        for (Rezervare rezervare : this.reservationService.getAll()) {
            LocalDate rezervareStartDate = LocalDate.parse(rezervare.getStartDate());
            LocalDate rezervareEndDate = LocalDate.parse(rezervare.getEndDate());
            Hotel hotelRezervare = rezervare.getHotel();

            if (hotelRezervare.getId().equals(hotelId) && checkOverlap(filteringStartDate, filteringEndDate, rezervareStartDate, rezervareEndDate)) {
                Room reservedRoom = rezervare.getRoom();
                reservedRoomNumbers.add(reservedRoom.getId());
            }
        }

        Collection<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (!reservedRoomNumbers.contains(room.getId())) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    private boolean checkOverlap(LocalDate filteringStartDate, LocalDate filteringEndDate, LocalDate rezervareStartDate, LocalDate rezervareEndDate) {
        if (filteringStartDate.equals(rezervareStartDate) && filteringEndDate.equals(rezervareEndDate))
            return true;
        else if (filteringStartDate.isBefore(rezervareEndDate) && filteringEndDate.isAfter(rezervareStartDate))
            return true;
        return false;
    }

    public void makeReservation(int userId, int hotelId, int roomNumber, String startDate, String endDate) throws ReservationException {
        boolean available = false;
        User user = userService.findById(userId);
        Hotel hotel = hotelService.findHotelById(hotelId);
        Room room = hotelService.findRoomByHotelId(roomNumber, hotelId);
        Rezervare reservation = new Rezervare(user, startDate ,endDate, hotel, room);
        for (Room availableRoom : findAvailableRoom(hotelId, startDate, endDate)){
            if (availableRoom.equals(room))
                available = true;
        }

        if (available)
            this.reservationService.add(reservation);
        else
            throw new ReservationException("Sorry the room you selected is not available for this period!");

        room.setAvailable(false);

        reservationsPerHotel.computeIfAbsent(reservation.getHotel().getId(), k -> new ArrayList<>()).add(reservation);
    }

    public void updateReservation(int reservationId, int updatedRoomNumber) throws ReservationException {
        Rezervare rezervare = reservationService.findById(reservationId);
        if (rezervare == null)
            throw new ReservationException("Could not find reservation with this id "+reservationId);
        int hoteId = rezervare.getHotel().getId();
        Room newRoom = hotelService.findRoomByHotelId(updatedRoomNumber, hoteId);
        if (LocalDate.now().isBefore(LocalDate.parse(rezervare.getStartDate())) || LocalTime.now().isBefore(CHECK_IN)) {
            Rezervare updatedRezervare = new Rezervare(reservationId, rezervare.getUser(), rezervare.getStartDate(),
                    rezervare.getEndDate(), rezervare.getHotel(), newRoom);
            reservationService.update(updatedRezervare, reservationId);
        } else
            throw new ReservationException("Sorry! You cannot change your room now!");
    }

    public void cancelReservation(int reservationId) throws ReservationException {
        Rezervare rezervare = reservationService.findById(reservationId);
        if (rezervare == null)
            throw new ReservationException("Could not find reservation with this id "+reservationId);
        if (LocalDate.now().isBefore(LocalDate.parse(rezervare.getStartDate())) || LocalTime.now().isBefore(CHECK_IN)){
            this.reservationService.delete(rezervare);
        }
        else
            throw new ReservationException("Could not cancel reservation");
    }

    public void leaveFeedback(int reservationId, String feedback) throws ReservationException {
        Rezervare rezervare = reservationService.findById(reservationId);
        rezervare.setReview(feedback);
        if (rezervare == null)
            throw new ReservationException("Could not find reservation with this id "+reservationId);
        Rezervare updatedRezervare = new Rezervare(reservationId, rezervare.getUser(), rezervare.getStartDate(),
                rezervare.getEndDate(), rezervare.getHotel(), rezervare.getRoom(), feedback);
        reservationService.leaveFeedback(updatedRezervare, reservationId);
    }

    public Collection<Rezervare> getRezervareByUser(int userId){
        Collection rezervari = new ArrayList<>();
        for (Rezervare rezervare : reservationService.getAll()){
            if (rezervare.getUser().getId().equals(userId))
                rezervari.add(rezervare);
        }
        return rezervari;
    }
}
