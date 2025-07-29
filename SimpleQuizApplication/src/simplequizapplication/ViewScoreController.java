package simplequizapplication;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.Parent;

public class ViewScoreController {

    @FXML
    private Label Scorelbl;

    @FXML
    private Button backBtn;

    @FXML
    private Button playBtn;

    @FXML
    private Button homeBtn;

    // This method can be called from the Quiz controller after quiz completion
    public void setScore(int score, int total) {
        Scorelbl.setText("You scored " + score + " of " + total);
    }

    @FXML
    private void Back(ActionEvent event) throws IOException {
        navigateTo("UserDashboard.fxml", event);
    }

    @FXML
    private void PlayAgain(ActionEvent event) throws IOException {
        navigateTo("StartQuiz.fxml", event);
    }

    @FXML
    private void Home(ActionEvent event) throws IOException {
        navigateTo("UserDashboard.fxml", event); // or UserDashboard.fxml based on your flow
    }

    private void navigateTo(String fxmlFile, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
