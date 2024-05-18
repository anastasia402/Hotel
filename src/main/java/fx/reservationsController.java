package fx;

import Controller.Controller;
import Domain.ReservationException;
import Domain.Rezervare;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class reservationsController implements Initializable {
    @FXML
    public TextField roomNumber;
    @FXML
    public Button searchHotelsButton;
    @FXML
    public Button cancelButton;
    @FXML
    public ListView<Rezervare> reservationListView;
    @FXML
    public TextField review;
    @FXML
    public Button myReservationButton;
    public Controller controller;
    public ImageView headerImage;
    private int userId;
    private int reservationId;
    ObservableList<Rezervare> rezervareObservableList = FXCollections.observableArrayList();

    public void setController(Controller controller) {
        this.controller = controller;
        populateListView();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image(getClass().getResourceAsStream("/Дизайн без названия.png"));
        headerImage.setImage(image);
        reservationListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                userId = newValue.getUser().getId();
                reservationId = newValue.getId();
            }
        });
    }

    public void populateListView(){
        rezervareObservableList.clear();
        Collection<Rezervare> rezervari = controller.getRezervareByUser(userId);
        rezervareObservableList.setAll(rezervari);
        reservationListView.setItems(rezervareObservableList);
    }

    public void leaveReview(ActionEvent event) {
        try {
            controller.leaveFeedback(reservationId, review.getText());
            populateListView();
            review.clear();
        } catch (ReservationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("Error leaving review!");
            alert.setContentText("Try again!");
            alert.showAndWait();
        }
    }

    public void updateReservation(ActionEvent event) {
        try {
            controller.updateReservation(reservationId, Integer.parseInt(roomNumber.getText()));
            populateListView();
        } catch (ReservationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("Error updating reservation!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void cancelReservation(ActionEvent event) {
        try {
            controller.cancelReservation(reservationId);
            populateListView();
        } catch (ReservationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("Error canceling reservation!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
