package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PlayMovieController {
    public Button btnNo;

    public void ClickedYes(ActionEvent event) {

    }

    public void ClickedNo(ActionEvent event) {
        Stage stage = (Stage) btnNo.getScene().getWindow();
        stage.close();
    }
}
