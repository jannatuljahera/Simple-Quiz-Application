package simplequizapplication;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewResultController implements Initializable {

    @FXML
    private TableView<Result> resultTable;
    @FXML
    private TableColumn<Result, String> colName;
    @FXML
    private TableColumn<Result, Integer> colScore;
    @FXML
    private TableColumn<Result, String> colDate;

    @FXML
    private Button backBtn, deleteBtn, logoutBtn;

    private ObservableList<Result> resultList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadResults();
    }

    private void loadResults() {
        resultList.clear();
        try (Connection conn = ConnectionDB.getConnection()) {
            String query = "SELECT * FROM results ORDER BY id DESC";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                int score = rs.getInt("score");
                Timestamp timestamp = rs.getTimestamp("date");
                String formattedDate = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                resultList.add(new Result(name, score, formattedDate));
            }

            resultTable.setItems(resultList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not load results.");
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
    private void Delete(ActionEvent event) {
        Result selected = resultTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a result to delete.");
            return;
        }

        try (Connection conn = ConnectionDB.getConnection()) {
            String deleteQuery = "DELETE FROM results WHERE username = ? AND score = ? AND date = ?";
            PreparedStatement pst = conn.prepareStatement(deleteQuery);
            pst.setString(1, selected.getUsername());
            pst.setInt(2, selected.getScore());
            pst.setTimestamp(3, Timestamp.valueOf(selected.getDate()));
            pst.executeUpdate();

            resultList.remove(selected);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete selected result.");
        }
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}
