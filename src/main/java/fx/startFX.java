package fx;

import Controller.Controller;
import Repository.HotelRepository;
import Repository.ReservationRepository;
import Repository.UserRepository;
import Service.HotelService;
import Service.ReservationService;
import Service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.naming.directory.SearchControls;

public class startFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        HotelRepository hotelRepository = new HotelRepository("/Users/anastasiacutulima/Desktop/siemens/siemensHotel/src/main/resources/hotels.json");
        UserRepository userRepository = new UserRepository();
        ReservationRepository reservationRepository = new ReservationRepository();
        reservationRepository.setHotelUserRepo(hotelRepository, userRepository);

        HotelService hotelService = new HotelService(hotelRepository);
        UserService userService = new UserService(userRepository);
        ReservationService reservationService = new ReservationService(reservationRepository);

        Controller controllerSystem = new Controller(hotelService, userService, reservationService);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();

        FXMLLoader cloader = new FXMLLoader(getClass().getResource("/Search.fxml"));
        Parent croot = cloader.load();

        searchController searchController = cloader.getController();
        searchController.setController(controllerSystem);
        System.out.println("search a setat controllerul");

        mainController.setParent(croot);
        mainController.setController(searchController);

        primaryStage.setTitle("Hotel Booking Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
