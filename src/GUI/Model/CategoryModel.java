package GUI.Model;

import BE.Category;
import BLL.CategoryManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryModel {
    private List<Category> categoriesToBeViewed;
    private CategoryManager categoryManager;
    private ArrayList<Category> allCategories;
    private Category selectedCategory;

    private ObservableList<Category> categoriesAttachedToMovies;

    //TODO what does this gat and set do?
    public Category getSelectedCategory(List<Category> categories) {
        return selectedCategory;
    }
    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    /**
     * Constructor for the categoryModel. Categories to be viewed is set to be all the categories
     * @throws Exception
     */
    public CategoryModel() throws Exception {
        categoryManager = new CategoryManager();
        categoriesToBeViewed = FXCollections.observableArrayList();
        categoriesToBeViewed.addAll(categoryManager.getAllCategories());
    }

    /**
     * Gets all the categories, that has a link to a movie.
     * @return the integer is the movieID and the list of Category is the linked categories
     * @throws SQLServerException
     */
    public Map<Integer, List<Category>> getObservableCategories() throws SQLServerException {
        return categoryManager.getCategoriesAttachedToMovies();
    }

    /**
     * Returns all the categories from the db
     * @return ArrayList<Category>
     */
    public ArrayList<Category> getAllCategories() {
        try {
            return categoryManager.getAllCategoriesArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes the specific category
     * @param deletedCategory category to delete
     * @throws Exception
     */
    public void removeCategory(Category deletedCategory) throws Exception {
        categoryManager.removeCategory(deletedCategory);
        categoriesToBeViewed.remove(deletedCategory);
    }

    /**
     * Creates a new category from a string and adds the category to categories to be viewed
     * @param newcategory String on new name
     * @throws Exception
     */
    public void createNewCategory(String newcategory) throws Exception {
        Category category = categoryManager.createNewCategory(newcategory);
        categoriesToBeViewed.add(category);
    }

    /**
     *
     * @return
     */
    public ObservableList<Category> getMovieCategories() {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        categories.addAll(categoryManager.getMovieCategories());
        return categories;
    }

    public void addCategoriesToMovie(int mID,List<Category> categories) {
        categoryManager.addCategoriesToMovie(mID,categories);
    }

    public List<Category> getUpdatedCategories(List<Category> categories) {
        return categoryManager.getUpdatedCategories(categories);
    }

    /**
     * Adds all the categories to the given combobox
     * @param categoryDropDown given combobox
     * @throws Exception exception thrown
     */
    public void addAllCategoriesToComboBox(ComboBox categoryDropDown) throws Exception {
        CategoryModel categoryModel = new CategoryModel();
        ArrayList<Category> allCategories = categoryModel.getAllCategories();
        for (Category category : allCategories) {
            categoryDropDown.getItems().add(category.getCategory());
        }
    }

    /**
     * Makes sure the user can not write any letters or special characters, but only numbers in the text-fields
     * @param editfield the text-field
     */
    public void addListenersToNumFields(TextField editfield) {
        //force the field to be numeric only
        editfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches(".")){
                System.out.println("inserted a .");
            }
            else if (!newValue.matches("\\d*")) {
                editfield.setText(newValue.replaceAll("\\D", ""));
                System.out.println("inserted a not-number");
            }
            else {
                System.out.println("inserted ?????");
            }
        });
    }

    /**
     * This method opens op a new window
     *
     * @param fxmlName    name of the file
     * @param displayName name of the window
     */
    public void openNewView(String fxmlName, String displayName) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CategoryModel.class.getResource("/GUI/View/" + fxmlName));
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
}