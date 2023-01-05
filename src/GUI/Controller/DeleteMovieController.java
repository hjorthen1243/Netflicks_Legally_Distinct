package GUI.Controller;

import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DeleteMovieController extends BaseController{
    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;

    public void ClickedYes(ActionEvent event) {
    }


    public void ClickedNo(ActionEvent event) {
        Stage stage = (Stage) btnNo.getScene().getWindow();
        stage.close();
    }
}
