//TODO Add functionality to this class

package GUI.Controller;

import BE.Category;
import BE.Movie;
import GUI.Controller.Methods.Methods;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javax.swing.JOptionPane.showMessageDialog;

public class EditViewController extends BaseController implements Initializable {
    @FXML
    private Text lAddCategoryToMovie, lRemoveCatFromMovie;
    @FXML
    private TextField txtFieldAddCatDb;
    @FXML
    private Button btnAddCatDb, btnSaveChanges, btnRemoveCatDb, btnAddCatMovie, btnRemoveCatMovie;
    @FXML
    private ComboBox comboBoxRemoveCatDb, comboBoxRemoveCatMovie, comboBoxAddCatMovie;
    private CategoryModel categoryModel;
    private Movie chosen;


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
            chosen = MainViewController.chosenMovie;

            if (chosen == null){
                isMovieEmpty(chosen == null);
            } else {
                Methods.addAllCategoriesToComboBox(comboBoxAddCatMovie);
                MovieIsChosen();
            }
            addRemovableCategories();
            //adds all the categories to the comboBox, where the user can add a category to the specific movie

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MovieIsChosen() {
        //removes the unnecessary categories from add categories to movie and remove category from movie
        comboBoxAddCatMovie.getItems().remove(0);
        String[] categories = chosen.getCategories().split(", ");
        ObservableList allCategories = comboBoxAddCatMovie.getItems();

        for (String alreadyInMovie : categories) {
            Category category = new Category(alreadyInMovie);
            comboBoxRemoveCatMovie.getItems().add(category);
        }

        for (String alreadyInMovie : categories) {
            for (Object category : allCategories) {
                if (alreadyInMovie.equals(category.toString())) {
                    comboBoxAddCatMovie.getItems().remove(category);
                    break;
                }
            }
        }
        btnAddCatMovie.setDisable(true);
        btnRemoveCatMovie.setDisable(true);
    }

    public void isMovieEmpty(Boolean bool) {
        ArrayList <Button> buttons = new ArrayList<>();
        buttons.add(btnAddCatMovie);
        buttons.add(btnRemoveCatMovie);
        ArrayList<ComboBox> comboBoxes = new ArrayList<>();
        comboBoxes.add(comboBoxAddCatMovie);
        comboBoxes.add(comboBoxRemoveCatMovie);

        for (Button button: buttons){
            button.setDisable(bool);
            button.setVisible(!bool);
        }
        for (ComboBox comboBox: comboBoxes){
            comboBox.setDisable(bool);
            comboBox.setVisible(!bool);
        }
        lAddCategoryToMovie.setVisible(!bool);
        lRemoveCatFromMovie.setVisible(!bool);
        /**

        btnAddCatMovie.setDisable(bool);
        btnAddCatMovie.setVisible(!bool);

        btnRemoveCatMovie.setDisable(bool);
        btnRemoveCatMovie.setVisible(!bool);

        comboBoxAddCatMovie.setDisable(bool);
        comboBoxAddCatMovie.setVisible(!bool);

        comboBoxRemoveCatMovie.setDisable(bool);
        comboBoxRemoveCatMovie.setVisible(!bool);
         */
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
            e.printStackTrace();
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
        btnRemoveCatDb.setDisable(comboBoxRemoveCatDb.getSelectionModel().getSelectedItem() == null
                || comboBoxRemoveCatDb.getSelectionModel().getSelectedItem().equals("") );
    }

    /**
     * Button action for removing the category from the database
     */
    public void removeCatDb() {
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


    public void listenerCatAddToMovie() {
        btnAddCatMovie.setDisable(comboBoxAddCatMovie.getSelectionModel().getSelectedItem() == null
                || comboBoxAddCatMovie.getSelectionModel().getSelectedItem().equals(""));
    }


    /**
     * Button action for adding a category to the movie
     */
    public void addCatMovie() {
        //Gets all the categories that are alrady in the list, and puts it into the List
        String[] alreadyInMovie = chosen.getCategories().split(", ");
        Category newCategory = new Category(comboBoxAddCatMovie.getSelectionModel().getSelectedItem().toString());
        List<Category> movieCategories = new ArrayList<>();
        for (String inMovie : alreadyInMovie) {
            Category Category = new Category(inMovie);
            movieCategories.add(Category);
        }
        movieCategories.add(newCategory);   //Adds the new Category

        //Goes to DAL and gets all the Categories and the Id's
        List<Category> updatedCategories = categoryModel.getUpdatedCategories(movieCategories);
        int mID = chosen.getId();
        //Adds the link between category and movie
        categoryModel.addCategoriesToMovie(mID, updatedCategories);

        btnAddCatMovie.setDisable(true);
        comboBoxAddCatMovie.setValue(0);
        comboBoxAddCatMovie.getItems().remove(newCategory);
        comboBoxRemoveCatMovie.getItems().add(newCategory);
        btnAddCatMovie.setDisable(true);
        //shows a message, like the alert boxes
        showMessageDialog(null, "The category: "
                + newCategory.getCategory() + " has been added to the movie: " + chosen.getTitle());
    }


    /**
     * The comboBox here gets all the different categories linked to the chosen movie.
     * Here the user can remove a category from the movie.
     */
    public void listenerRemoveCatInMovie() {
        btnRemoveCatMovie.setDisable(comboBoxRemoveCatMovie.getSelectionModel().getSelectedItem() == null
                || comboBoxRemoveCatMovie.getSelectionModel().getSelectedItem().equals(""));
    }


    /**
     * Button action for removing the category from the movie
     */
    public void removeCatMovieHandle() {
        Category category = new Category(comboBoxRemoveCatMovie.getSelectionModel().getSelectedItem().toString());

        try {
            MovieModel movieModel = new MovieModel();
            //Creates a list to send to DAL, to get id of the category
            List<Category> catToDelete = new ArrayList<>();
            catToDelete.add(category);
            List<Category> updatedCategories = categoryModel.getUpdatedCategories(catToDelete);
            Category category1 = updatedCategories.get(0);
            movieModel.removeCategoryFromMovie(chosen.getId(), category1.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnRemoveCatMovie.setDisable(true);
        comboBoxRemoveCatMovie.setValue(0);
        comboBoxRemoveCatMovie.getItems().remove(category);
        comboBoxAddCatMovie.getItems().add(category);
        //shows a message, like the alert boxes
        showMessageDialog(null, "The category: "
                + category.getCategory() + " has been removed from the movie: " + chosen.getTitle());
    }

    /**
     * Button action for closing the window
     */
    public void saveChangesHandle() {
        Stage stage = (Stage) btnSaveChanges.getScene().getWindow();
        stage.close();
    }

}
