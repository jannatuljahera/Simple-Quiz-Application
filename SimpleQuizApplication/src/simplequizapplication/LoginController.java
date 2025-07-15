/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package simplequizapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author user
 */
public class LoginController implements Initializable {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPass;
    @FXML
    private Hyperlink SignupLink;
    @FXML
    private Button loginBtn;
    @FXML
    private Label lblError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void Signup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Signup.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Signup Form");
        stage.show();
        ((Stage) SignupLink.getScene().getWindow()).close();
    }

    @FXML
    private void Login(ActionEvent event) throws IOException {
        String username = tfUsername.getText().trim();
        String password = pfPass.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and Password must not be empty.");
            return;
        }

        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String role = rs.getString("role");

                        String fxmlFile = null;
                        String title = null;

                        if ("Admin".equalsIgnoreCase(role)) {
                            fxmlFile = "AdminDashboard.fxml";
                            title = "Admin Dashboard";
                        } else if ("user".equalsIgnoreCase(role)) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
                            Parent root = loader.load();


                            UserDashboardController controller = loader.getController();
                            controller.setPatientUsername(username);


                            Stage stage = (Stage) lblError.getScene().getWindow(); 
                            stage.setScene(new Scene(root));
                            stage.setTitle("User Dashboard");
                            stage.show();

                            return;
                        }


                        if (fxmlFile != null) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                            Parent root = loader.load();


                            Stage stage = (Stage) lblError.getScene().getWindow(); 
                            stage.setScene(new Scene(root));
                            stage.setTitle(title);
                            stage.show();
                        } else {
                            System.out.println("Unknown role.");
                        }

                    } else {
                        System.out.println("Invalid username or password.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
