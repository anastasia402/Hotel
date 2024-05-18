package fx;

import Controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.naming.directory.SearchControls;

public class MainController {

    @FXML
    private Button welcomeButton1;


    Parent parent;
    searchController controller;
    public void handleExit(ActionEvent event) {
    }

    public void handleAbout(ActionEvent event) {
    }

    public void pressWelcome(ActionEvent event) {
        openSearchWindow(event);
    }

    private void openSearchWindow(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Find hotels nearby");
        stage.setScene(new Scene(parent));

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public void setParent(Parent croot) {
        this.parent = croot;
    }

    public void setController(searchController controllerSystem) {
        this.controller = controllerSystem;
    }
}

