import Controller.Controller;
import Domain.Room;
import Domain.User;
import Repository.HotelRepository;
import Repository.ReservationRepository;
import Repository.UserRepository;
import Service.HotelService;
import Service.ReservationService;
import Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static javafx.beans.binding.Bindings.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testController {
    private Controller controller;
    private UserService userService;
    private HotelService hotelService;
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        HotelRepository hotelRepository = new HotelRepository("/Users/anastasiacutulima/Desktop/siemens/siemensHotel/src/main/resources/hotels.json");
        UserRepository userRepository = new UserRepository();
        ReservationRepository reservationRepository = new ReservationRepository();
        reservationRepository.setHotelUserRepo(hotelRepository, userRepository);

        hotelService = new HotelService(hotelRepository);
        userService = new UserService(userRepository);
        reservationService = new ReservationService(reservationRepository);

        this.controller = new Controller(hotelService, userService, reservationService);
    }

    @Test
    public void testFindHotelsInRadius() {
        assert controller.findHotelsInRadius(10, 1).size() == 3; //for user in Cluj
        assert controller.findHotelsInRadius(100, 5).size() == 0; //for user in the USA
    }

    @Test
    public void testCalculateDistance() {
        Controller controller1 = new Controller(null, null, null);

        double userLat = 40.7128; // New York
        double userLong = -74.0060;
        double hotelLat = 34.052235; // Los Angeles
        double hotelLong = -118.243683;

        double expectedDistance = Math.sqrt(Math.pow(userLat - hotelLat, 2) + Math.pow(userLong - hotelLong, 2));
        double actualDistance = controller1.calculateDistance(userLat, userLong, hotelLat, hotelLong);

        assert (expectedDistance == actualDistance);
    }

    @Test
    public void testConvertUsingPositionLatitude() {
        Controller controller1 = new Controller(null, null, null);

        double latitude = 40.7128;

        double expectedDistance = 111132.92 - 559.82 * Math.cos(2 * Math.toRadians(latitude)) +
                1.175 * Math.cos(4 * Math.toRadians(latitude)) -
                0.0023 * Math.cos(6 * Math.toRadians(latitude));

        double actualDistance = controller1.convertUsingPositionLatitude(latitude);
        double delta = 0.001;
        assertEquals(expectedDistance, actualDistance, delta);

    }

    @Test
    public void testConvertUsingPositionLongitude() {
        Controller controller = new Controller(null, null, null);
        double latitude = 40.7128; // New York

        double expectedDistance = 111412.84 * Math.cos(Math.toRadians(latitude)) -
                93.5 * Math.cos(3 * Math.toRadians(latitude)) +
                0.118 * Math.cos(5 * Math.toRadians(latitude));

        double actualDistance = controller.convertUsingPositionLongitude(latitude);

        double delta = 0.001;

        assertEquals(expectedDistance, actualDistance, delta);
    }

    @Test
    public void testFindAvailableRoom() {

    }

    @Test
    public void testMakeReservation() {

    }

    @Test
    public void testUpdateReservation() {

    }

    @Test
    public void testCancelReservation() {

    }

    @Test
    public void testLeaveFeedback() {

    }

    @Test
    public void testGetRezervareByUser() {

    }

}
