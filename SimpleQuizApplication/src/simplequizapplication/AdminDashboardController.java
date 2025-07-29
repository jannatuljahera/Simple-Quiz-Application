package simplequizapplication;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;

public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button manageQuizzesBtn;

    @FXML
    private Button viewResultsBtn;

    @FXML
    private Button manageUsersBtn;

    @FXML
    private Button logoutBtn;

    // Handle Manage Quiz Button
    @FXML
    void ManageQuiz(ActionEvent event) throws IOException {
        switchScene(event, "ManageQuiz.fxml");
    }

    // Handle View Results Button
    @FXML
    void ViewResults(ActionEvent event) throws IOException {
        switchScene(event, "ViewResult.fxml");
    }

    // Handle Manage Users Button
    @FXML
    void ManageUsers(ActionEvent event) throws IOException {
        switchScene(event, "ManageUsers.fxml");
    }

    // Handle Logout Button
    @FXML
    void Logout(ActionEvent event) throws IOException {
        switchScene(event, "Login.fxml");
    }

    // Reusable method to switch scenes
    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
