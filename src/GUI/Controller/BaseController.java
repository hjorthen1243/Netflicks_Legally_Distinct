package GUI.Controller;

import GUI.Model.PMCModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class BaseController {

    private PMCModel model;

    /**
     * Sets the model of the controller
     * @param model
     */
    public void setModel(PMCModel model) {this.model = model;}

    /**
     * Get the model of the controller
     * @return the model of the controller
     */
    public PMCModel getModel() {
        return model;
    }

    /**
     * Method for setting up the window
     */
    public abstract void setup();

}