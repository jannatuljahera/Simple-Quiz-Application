package simplequizapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UserDashboardController implements Initializable {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Button startQuizBtn;
    @FXML
    private Button viewScoresBtn;
    @FXML
    private Button logoutBtn;

    // You can pass the username from login to display the user's name
    private String username;
    private String playerUsername;

    public void setPlayerUsername(String username) {
        this.playerUsername = username;
        System.out.println("Logged in as: " + playerUsername);
        // You can also call methods to initialize dashboard data here
    }

    public void setUsername(String username) {
        this.username = username;
        welcomeLabel.setText("Welcome, " + username + "!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Optional: Set default welcome text
        welcomeLabel.setText("Welcome, User!");
    }
    
    public void saveResult(String username, int score, int total) {
        String query = "INSERT INTO results (username, score, total) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setInt(2, score);
            stmt.setInt(3, total);

            stmt.executeUpdate();
            System.out.println("Result saved for user: " + username);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void StartQuiz(ActionEvent event) throws IOException {
        loadScene("StartQuiz.fxml", "Start Quiz");
    }

    @FXML
    private void MyScore(ActionEvent event) throws IOException {
        loadScene("ViewScore.fxml", "My Scores");
    }

    @FXML
    private void Logout(ActionEvent event) throws IOException {
        loadScene("Login.fxml", "Login");
        ((Stage) logoutBtn.getScene().getWindow()).close(); // Close dashboard
    }

    private void loadScene(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
