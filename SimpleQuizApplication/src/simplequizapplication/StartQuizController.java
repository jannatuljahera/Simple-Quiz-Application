package simplequizapplication;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.sql.*;

public class StartQuizController implements Initializable {

    @FXML
    private Label quslbl;
    @FXML
    private RadioButton rdBtn1;
    @FXML
    private ToggleGroup rdBtn;
    @FXML
    private RadioButton rdBtn2;
    @FXML
    private RadioButton rdBtn3;
    @FXML
    private RadioButton rdBtn4;
    @FXML
    private Button submitBtn;
    @FXML
    private Button nextBtn;

    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadQuestionsFromDatabase();
        submitBtn.setDisable(true); // Start with submit disabled
        if (!questions.isEmpty()) {
            showQuestion(currentQuestionIndex);
        }
    }

    private void loadQuestionsFromDatabase() {
        try (Connection con = ConnectionDB.getConnection()) {
            String sql = "SELECT * FROM question LIMIT 10";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String qus = rs.getString("qus");
                String opt1 = rs.getString("option1");
                String opt2 = rs.getString("option2");
                String opt3 = rs.getString("option3");
                String opt4 = rs.getString("option4");
                String ans = rs.getString("currect_ans");

                questions.add(new Question(qus, opt1, opt2, opt3, opt4, ans));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to load questions from database.");
        }
    }

    private void showQuestion(int index) {
        Question q = questions.get(index);
        quslbl.setText((index + 1) + ". " + q.getQuestion());
        rdBtn1.setText(q.getOption1());
        rdBtn2.setText(q.getOption2());
        rdBtn3.setText(q.getOption3());
        rdBtn4.setText(q.getOption4());
        rdBtn.selectToggle(null);
    }

    @FXML
    private void Next(ActionEvent event) {
        if (rdBtn.getSelectedToggle() == null) {
            showAlert("Please select an answer.");
            return;
        }

        String selected = ((RadioButton) rdBtn.getSelectedToggle()).getText();
        if (selected.equals(questions.get(currentQuestionIndex).getAnswer())) {
            score++;
        }

        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size() - 1) {
            showQuestion(currentQuestionIndex);
        } else if (currentQuestionIndex == questions.size() - 1) {
            showQuestion(currentQuestionIndex);
            nextBtn.setDisable(true);
            submitBtn.setDisable(false);
        }
    }

    @FXML
    private void Submit(ActionEvent event) {
        if (rdBtn.getSelectedToggle() == null) {
            showAlert("Please select an answer.");
            return;
        }

        String selected = ((RadioButton) rdBtn.getSelectedToggle()).getText();
        if (selected.equals(questions.get(currentQuestionIndex).getAnswer())) {
            score++;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Result");
        alert.setHeaderText("Your Score");
        alert.setContentText("You scored " + score + " out of " + questions.size());
        alert.showAndWait();

//        saveResultToDatabase(username, score, questions.size());
        resetQuiz();
    }

    private void saveResultToDatabase(String username, int score, int total) {
        String sql = "INSERT INTO results (username, score, total, date_time) VALUES (?, ?, ?, NOW())";
        try {
            Connection conn = ConnectionDB.getConnection(); // your DB utility class
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setInt(2, score);
            pstmt.setInt(3, total);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error saving result: " + e.getMessage());
        }
    }


    private void resetQuiz() {
        currentQuestionIndex = 0;
        score = 0;
        nextBtn.setDisable(false);
        submitBtn.setDisable(true);
        showQuestion(currentQuestionIndex);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
