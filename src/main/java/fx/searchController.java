package fx;

import Controller.Controller;
import Domain.Hotel;
import Domain.ReservationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class searchController implements Initializable {
    public ImageView headerImage;
    Controller controller;
    ObservableList<Hotel> hotelObservableList = FXCollections.observableArrayList();
    @FXML
    TableView<Hotel> hotelTableView;
    @FXML
    public TextField userId;

    @FXML
    public TextField radius;

    public ListView<Hotel> hotelListView;
    public int hotelId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("INIT : am in lista bilete ");
        System.out.println("END INIT!!!!!!!!!");
        Image image = new Image(getClass().getResourceAsStream("/Дизайн без названия.png"));
        headerImage.setImage(image);
        hotelListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                hotelId = newValue.getId();
                openBookingWindow(newValue);
            }
        });

    }

    private void openBookingWindow(Hotel hotel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/booking.fxml"));
            Parent root = loader.load();

            bookingController bookingController = loader.getController();
            bookingController.setUserId(Integer.parseInt(userId.getText()));
            bookingController.setHotelId(hotelId);
            bookingController.setController(controller);
            // Optionally pass data to the booking controller here

            Stage stage = new Stage();
            stage.setTitle("Book Room at " + hotel.getName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void searchNearbyHotels(ActionEvent event) {
        hotelObservableList.clear();
        int user = Integer.parseInt(userId.getText());
        int raza = Integer.parseInt(radius.getText());
        Collection<Hotel> searchedHotels = new ArrayList<>();

        try {
            searchedHotels = this.controller.findHotelsInRadius(raza, user);
            hotelObservableList.setAll(searchedHotels);
            //hotelTableView.setItems(hotelObservableList);
            hotelListView.setItems(hotelObservableList);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("Error finding nearby hotels!");
            alert.setContentText("Try again!");
            alert.showAndWait();
        }

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void showMyReservation(ActionEvent event) {
        try {
            if (userId.getText().equals("")){
                throw new ReservationException("Please enter your id first");
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/myReservations.fxml"));
            Parent root = loader.load();

            reservationsController myReservationsController = loader.getController();
            myReservationsController.setUserId(Integer.parseInt(userId.getText()));
            myReservationsController.setController(controller);
            // Pass necessary data to the controller if needed

            Stage stage = new Stage();
            stage.setTitle("My Reservations");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ReservationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("Please enter your id first");
            alert.setContentText("Try again!");
            alert.showAndWait();
        }
    }
}
