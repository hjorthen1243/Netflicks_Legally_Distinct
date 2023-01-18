//TODO Add functionality to this class

package GUI.Controller;

import BE.Category;
import BE.Movie;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class EditViewController extends BaseController {
    @FXML
    private TextField txtFieldAddCatDb;
    @FXML
    private Button btnAddCatDb, btnAddCatMovie, btnSaveChanges, btnRemoveCatDb, btnRemoveCatMovie;
    @FXML
    private ComboBox comboBoxRemoveCatMovie, comboBoxAddCatMovie, comboBoxRemoveCatDb;
    private CategoryModel categoryModel;
    public void addCatDbHandle(ActionEvent actionEvent) throws Exception {
        categoryModel = new CategoryModel();

        String category = txtFieldAddCatDb.getText();

        categoryModel.createNewCategory(category);
    }


    @Override
    public void setup() {
        addAllCatToDBComboBox();
    }


    /**
     * Here the user can add a category to the database
     */
    private void addAllCatToDBComboBox(){
        categoryModel = getModel().getCategoryModel();
        ArrayList<Category> allCategories;
        allCategories = categoryModel.getAllCategories();
        for (Category category : allCategories) {
            comboBoxRemoveCatDb.getItems().add(category.getCategory());
        }
    }
    private void OpenNewView(MouseEvent event, String fxmlName, String displayName, BaseController controller) {
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
