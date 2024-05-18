package fx;

import Controller.Controller;
import Domain.ReservationException;
import Domain.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class bookingController implements Initializable {

    public ImageView headerImage;
    private Controller controller;
    @FXML
    public TextField startDate;
    @FXML
    public TextField endDate;
    @FXML
    public Button searchRoomsButton;
    @FXML
    public Button bookRoomButton;
    @FXML
    public ListView<Room> roomsListView;
    int roomNumber;

    public int userId;
    public int hotelId;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public ObservableList<Room> roomObservableList = FXCollections.observableArrayList();
    public void bookRoom(ActionEvent event) {
        try {
            controller.makeReservation(userId, hotelId, roomNumber, startDate.getText(), endDate.getText());
            showMyReservation(event);
        } catch (ReservationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("Error booking a room");
            alert.setContentText("Try again!");
            alert.showAndWait();
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void findAbailableRooms(ActionEvent event) {
        roomObservableList.clear();
        roomsListView.refresh();
        String start = startDate.getText();
        String end = endDate.getText();

        Collection<Room> rooms = new ArrayList<>();

        try {
            rooms = controller.findAvailableRoom(hotelId, start, end);
            roomObservableList.setAll(rooms);
            roomsListView.setItems(roomObservableList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                roomNumber = newValue.getId();
            }
        });
        Image image = new Image(getClass().getResourceAsStream("/Дизайн без названия.png"));
        headerImage.setImage(image);
    }

    public void showMyReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/myReservations.fxml"));
            Parent root = loader.load();

            reservationsController myReservationsController = loader.getController();
            myReservationsController.setUserId(userId);
            myReservationsController.setController(controller);
            // Pass necessary data to the controller if needed

            Stage stage = new Stage();
            stage.setTitle("My Reservations");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
