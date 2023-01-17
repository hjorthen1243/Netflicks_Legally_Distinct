//TODO Add functionality to this class

package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class EditViewController extends BaseController {
    @FXML
    private TextField txtFieldAddCatDb;
    @FXML
    private Button btnAddCatDb, btnAddCatMovie, btnSaveChanges, btnRemoveCatDb, btnRemoveCatMovie;
    @FXML
    private ComboBox comboBoxRemoveCatMovie, comboBoxAddCatMovie, comboBoxRemoveCatDb;

    @Override
    public void setup() {

    }


    /**
     * On the comboBox here, the user can choose categories to be deleted, and afterwards
     * remove the category from the db.
     * @param actionEvent
     */

    public void dbCatRemoveSelectedHandle(ActionEvent actionEvent) {
    }


    /**
     * The comboBox here gets all the different categories linked to the chosen movie.
     * Here the user can remove a category from the movie.
     * @param event
     */
    public void removeCatInMovieSelectedHandle(ActionEvent event) {
    }

    /**
     * This comboBox contains all the different categories, so the user can add a category to the specific movie.
     * @param event
     */
    public void catAddToMovieSelectedHandle(ActionEvent event) {
    }

    /**
     * Here the user can add a category to the database
     * @param actionEvent
     */
    public void addCatDbHandle(ActionEvent actionEvent) {
    }

    /**
     * Button action for removing the category from the database
     * @param actionEvent
     */
    public void removeCatDbHandle(ActionEvent actionEvent) {
    }

    /**
     * Button action for adding a category to the movie
     * @param actionEvent
     */
    public void addCatMovieHandle(ActionEvent actionEvent) {
    }


    /**
     * Button action for removing the category from the movie
     * @param actionEvent
     */
    public void removeCatMovieHandle(ActionEvent actionEvent) {

    }

    /**
     * Button action for saving all the changes
     * @param actionEvent
     */
    public void saveChangesHandle(ActionEvent actionEvent) {
    }
}
