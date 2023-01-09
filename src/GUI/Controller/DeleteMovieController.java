package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DeleteMovieController extends BaseController{
    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;

    @Override
    public void setup() {

    }

    public void ClickedYes(ActionEvent event) {
    }


    public void ClickedNo(ActionEvent event) {
        Stage stage = (Stage) btnNo.getScene().getWindow();
        stage.close();
    }


}
