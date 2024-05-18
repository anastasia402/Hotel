package org.example;

import Controller.Controller;
import Domain.ReservationException;
import Domain.Room;
import Repository.HotelRepository;
import Repository.ReservationRepository;
import Repository.UserRepository;
import Service.HotelService;
import Service.ReservationService;
import Service.UserService;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args){
        HotelRepository hotelRepository = new HotelRepository("/Users/anastasiacutulima/Desktop/siemens/siemensHotel/src/main/resources/hotels.json");
        UserRepository userRepository = new UserRepository();
        ReservationRepository reservationRepository = new ReservationRepository();
        reservationRepository.setHotelUserRepo(hotelRepository, userRepository);

        HotelService hotelService = new HotelService(hotelRepository);
        UserService userService = new UserService(userRepository);
        ReservationService reservationService = new ReservationService(reservationRepository);

        Controller controller = new Controller(hotelService, userService, reservationService);
//        try {
//            controller.makeReservation(3, 1, 125, "2024-05-19", "2024-05-23");
//        } catch (ReservationException e) {
//            throw new RuntimeException(e);
//        }

        try {
            for (Room room : controller.findAvailableRoom(1, "2024-05-19", "2024-05-23")){
                System.out.println(room);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        try {
//            controller.updateReservation(5, 87);
//        } catch (ReservationException e) {
//            throw new RuntimeException(e);
//        }

        try {
            controller.leaveFeedback(5, "Very clean and nice!");
        } catch (ReservationException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(LocalDateTime.now().toLocalTime().isBefore(LocalTime.NOON));
    }
}
