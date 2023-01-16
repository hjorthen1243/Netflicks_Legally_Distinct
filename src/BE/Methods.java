package BE;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Methods {

    /**
     * The shorter way to describe what the different columns should contain in the tableview
     * @param titleColumn   title
     * @param yearColumn    year
     * @param lengthColumn  length
     * @param ratingColumn  Imdb
     * @param pRatingColumn persRating
     * @param lastViewColumn last seen
     * @param movieTable    the table
     */
    public static void setValues(TableColumn<Object, Object> titleColumn, TableColumn<Object, Object> yearColumn, TableColumn<Object, Object> lengthColumn, TableColumn<Object, Object> ratingColumn, TableColumn<Object, Object> pRatingColumn, TableColumn<Object, Object> lastViewColumn, TableView<Object> movieTable) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("Year"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("ImdbRating"));
        pRatingColumn.setCellValueFactory(new PropertyValueFactory<>("PersonalRating"));
        lastViewColumn.setCellValueFactory(new PropertyValueFactory<>("LastViewDate"));

        movieTable.getColumns().addAll();
    }

    /**
     * This method opens op a new window
     *
     * @param fxmlName    name of the file
     * @param displayName name of the window
     */
    public static void openNewView(String fxmlName, String displayName) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Methods.class.getResource("/GUI/View/" + fxmlName));
            AnchorPane pane = loader.load();
            //controller.setModel(super.getModel());
            //controller.setup();
            // Create the dialog stage
            Stage dialogWindow = new Stage();
            dialogWindow.setTitle(displayName);
            dialogWindow.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(pane);
            dialogWindow.setScene(scene);
            dialogWindow.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addListenersToNumFields(TextField editfield) {

        // force the field to be numeric only
        editfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                editfield.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }
}
