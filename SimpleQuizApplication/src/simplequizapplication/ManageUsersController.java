package simplequizapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ManageUsersController implements Initializable {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colContact;
    @FXML private TableColumn<User, String> colPass;

    @FXML private TextField tfName;
    @FXML private TextField tfUsername;
    @FXML private TextField tfContact;
    @FXML private PasswordField pfPass;

    @FXML private Button backBtn, addBtn, editBtn, deleteBtn, logoutBtn;

    ObservableList<User> userList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colPass.setCellValueFactory(new PropertyValueFactory<>("password"));

        userTable.setItems(userList);
        loadUserData();

        userTable.setOnMouseClicked(e -> {
            User selected = userTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tfName.setText(selected.getName());
                tfUsername.setText(selected.getUsername());
                tfContact.setText(selected.getContact());
                pfPass.setText(selected.getPassword());
            }
        });
    }

    private void loadUserData() {
        userList.clear();
        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "SELECT * FROM users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                userList.add(new User(
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("contact"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Load Error: " + e.getMessage());
        }
    }

    @FXML
    private void Add(ActionEvent event) {
        String name = tfName.getText().trim();
        String username = tfUsername.getText().trim();
        String contact = tfContact.getText().trim();
        String password = pfPass.getText().trim();

        if (name.isEmpty() || username.isEmpty() || contact.isEmpty() || password.isEmpty()) {
            System.out.println("Please fill all fields.");
            return;
        }

        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "INSERT INTO users (name, username, contact, password) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, contact);
            stmt.setString(4, password);
            stmt.executeUpdate();
            System.out.println("User added.");
            loadUserData();
            clearFields();
        } catch (SQLException e) {
            System.out.println("Add Error: " + e.getMessage());
        }
    }

    @FXML
    private void Edit(ActionEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("No user selected.");
            return;
        }

        String name = tfName.getText().trim();
        String username = tfUsername.getText().trim();
        String contact = tfContact.getText().trim();
        String password = pfPass.getText().trim();

        if (name.isEmpty() || username.isEmpty() || contact.isEmpty() || password.isEmpty()) {
            System.out.println("Please fill all fields.");
            return;
        }

        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "UPDATE users SET name = ?, contact = ?, password = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, contact);
            stmt.setString(3, password);
            stmt.setString(4, username);
            stmt.executeUpdate();
            System.out.println("User updated.");
            loadUserData();
            clearFields();
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }

    @FXML
    private void Delete(ActionEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("No user selected.");
            return;
        }

        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "DELETE FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, selected.getUsername());
            stmt.executeUpdate();
            System.out.println("User deleted.");
            loadUserData();
            clearFields();
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }

    @FXML
    private void Back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Admin Dashboard");
        stage.show();
        ((Stage) backBtn.getScene().getWindow()).close();
    }

    @FXML
    private void Logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Login Form");
        stage.show();
        ((Stage) logoutBtn.getScene().getWindow()).close();
    }

    private void clearFields() {
        tfName.clear();
        tfUsername.clear();
        tfContact.clear();
        pfPass.clear();
    }
}
