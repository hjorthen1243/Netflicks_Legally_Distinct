package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Optional;

public class MainViewController {

    AddMovieController addController;
    DeleteMovieController delController;
    private Label label;

    public void addMovieHandle(ActionEvent event) {
        addController = new AddMovieController();
        OpenNewView(event, "AddMovie.fxml", "Add a movie", addController);

    }

    public void removeMovieHandle(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete File");
        alert.setHeaderText("You are about to delete the movie:\n" + "BATMAN" + "\tfrom" + "0000" + "\nAre you sure want to delete this movie?");
        alert.setContentText("");
        Optional<ButtonType> option = alert.showAndWait();
        option.get();

        //delController = new DeleteMovieController();
        //OpenNewView(event, "DeleteMovie.fxml", "Delete a movie", delController);
    }

    private void OpenNewView(ActionEvent event, String fxmlName, String displayName, BaseController controller) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GUI/View/" + fxmlName));
            AnchorPane pane = loader.load();
            controller = loader.getController();
            //controller.setModel(super.getModel());
            //controller.setup();
            // Create the dialog stage
            Stage dialogWindow = new Stage();
            dialogWindow.setTitle(displayName);
            dialogWindow.initModality(Modality.WINDOW_MODAL);
            dialogWindow.initOwner(((Node) event.getSource()).getScene().getWindow());
            Scene scene = new Scene(pane);
            dialogWindow.setScene(scene);
            dialogWindow.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePersonalRatingHandle(ActionEvent event) {
    }

    public void saveLastSeenHandle(ActionEvent event) {
    }

    public void searchHandle(ActionEvent event) {
    }
}
