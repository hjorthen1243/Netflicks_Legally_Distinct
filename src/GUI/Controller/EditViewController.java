//TODO Add functionality to this class

package GUI.Controller;

import BE.Category;
import GUI.Controller.Methods.Methods;
import GUI.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javax.swing.JOptionPane.showMessageDialog;

public class EditViewController extends BaseController implements Initializable {
    @FXML
    private TextField txtFieldAddCatDb;
    @FXML
    private Button btnAddCatDb, btnAddCatMovie, btnSaveChanges, btnRemoveCatDb, btnRemoveCatMovie;
    @FXML
    private ComboBox comboBoxRemoveCatMovie, comboBoxAddCatMovie, comboBoxRemoveCatDb;
    private CategoryModel categoryModel;


    @Override
    public void setup() {
        try {
            categoryModel = new CategoryModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            btnAddCatDb.setDisable(true);
            btnRemoveCatDb.setDisable(true);
            addRemovableCategories();
            //adds all the categories to the comboBox, where the user can add a category to the specific movie
            Methods.addAllCategoriesToComboBox(comboBoxAddCatMovie);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method adds all the categories, from the database, that the user is allowed to remove
     */
    private void addRemovableCategories() {
        try {
            categoryModel = new CategoryModel();
            ArrayList<Category> allCategories = categoryModel.getAllCategories();
            for (Category category : allCategories) {
                if (category.getId() > 29){
                    comboBoxRemoveCatDb.getItems().add(category.getCategory());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Listens to, when the input is changed, and if the text is valid, then it can be added to the database
     */
    public void listenerToTxtField() {
        btnAddCatDb.setDisable(txtFieldAddCatDb.getText().equals(""));
    }


    /**
     * Here the user can add a category to the database
     */
    public void addCatDbHandle() {
        try {
            categoryModel = new CategoryModel();
            String category = txtFieldAddCatDb.getText();
            categoryModel.createNewCategory(category);
            txtFieldAddCatDb.setText("");
            //shows a message, like the alert boxes
            showMessageDialog(null, "The new category: "
                    + category + " has been added to the database");
            btnAddCatDb.setDisable(true);
            addRemovableCategories();
            Methods.addAllCategoriesToComboBox(comboBoxAddCatMovie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * listens to, when the input of the box for removable items has changed
     */
    public void listenerRemoveFromDB() {
        btnRemoveCatDb.setDisable(comboBoxRemoveCatDb.getSelectionModel().getSelectedItem() == null || comboBoxRemoveCatDb.getSelectionModel().getSelectedItem().equals("") );
    }

    /**
     * Button action for removing the category from the database
     */
    public void removeCatDbHandle() {
        try {
            ArrayList<Category> allCategories = categoryModel.getAllCategories();
            for (Category category : allCategories) {
                if (category.getCategory().equals(comboBoxRemoveCatDb.getSelectionModel().getSelectedItem().toString())) {
                    categoryModel.removeCategory(category);
                    //shows a message, like the alert boxes
                    showMessageDialog(null, "The category: "
                            + category.getCategory() + " has been removed from the database");
                    comboBoxRemoveCatDb.getItems().remove(comboBoxRemoveCatDb.getSelectionModel().getSelectedItem());
                    comboBoxRemoveCatDb.setValue(1);
                    btnRemoveCatDb.setDisable(true);
                    Methods.addAllCategoriesToComboBox(comboBoxAddCatMovie);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //TODO start here
    /**
     * Button action for adding a category to the movie
     * @param actionEvent
     */
    public void addCatMovieHandle(ActionEvent actionEvent) {
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

    public void catAddToMovieSelectedHandle(ActionEvent event) {
    }

}
