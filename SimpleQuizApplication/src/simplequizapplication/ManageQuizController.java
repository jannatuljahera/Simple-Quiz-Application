package simplequizapplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ManageQuizController implements Initializable {

    @FXML
    private TextArea taqus;

    @FXML
    private TextField tfoption1;

    @FXML
    private TextField tfoption2;

    @FXML
    private TextField tfoption3;

    @FXML
    private TextField tfoption4;

    @FXML
    private TextField tfoptionF;

    @FXML
    private Button saveBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Optional: initialize any logic here
    }

    @FXML
    private void saveQuestion() {
        String question = taqus.getText().trim();
        String option1 = tfoption1.getText().trim();
        String option2 = tfoption2.getText().trim();
        String option3 = tfoption3.getText().trim();
        String option4 = tfoption4.getText().trim();
        String correctAnswer = tfoptionF.getText().trim();

        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() ||
            option3.isEmpty() || option4.isEmpty() || correctAnswer.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "INSERT INTO question (qus, option1, option2, option3, option4, currect_ans) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, question);
            stmt.setString(2, option1);
            stmt.setString(3, option2);
            stmt.setString(4, option3);
            stmt.setString(5, option4);
            stmt.setString(6, correctAnswer);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Question added successfully!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add question.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        taqus.clear();
        tfoption1.clear();
        tfoption2.clear();
        tfoption3.clear();
        tfoption4.clear();
        tfoptionF.clear();
    }
}
