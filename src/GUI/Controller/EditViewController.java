package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class EditViewController extends BaseController {
    public DatePicker datePickerLastSeen;
    public TextField txtFieldAddCatDb;
    public Button btnAddCatDb;
    public Button btnAddCatMovie;
    public Button btnSaveChanges;
    public Button btnRemoveCatDb;
    public Button btnRemoveCatMovie;
    public ComboBox comboBoxRemoveCatMovie;
    public ComboBox comboBoxAddCatMovie;
    public ComboBox comboBoxRemoveCatDb;

    public void dbCatSelectedHandle(ActionEvent actionEvent) {
    }

    public void catInMovieSelectedHandle(ActionEvent actionEvent) {
    }

    public void catToMovieSelectedHandle(ActionEvent actionEvent) {
    }

    public void addCatDbHandle(ActionEvent actionEvent) {
    }

    public void removeCatDbHandle(ActionEvent actionEvent) {
    }

    public void addCatMovieHandle(ActionEvent actionEvent) {
    }

    public void removeCatMovieHandle(ActionEvent actionEvent) {

    }

    public void saveChangesHandle(ActionEvent actionEvent) {
    }

    @Override
    public void setup() {

    }
}
