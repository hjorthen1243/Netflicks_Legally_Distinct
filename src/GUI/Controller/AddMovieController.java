package GUI.Controller;

import BE.Category;
import BE.Movie;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import com.xuggle.xuggler.IContainer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class AddMovieController extends BaseController implements Initializable {
    @FXML
    private DatePicker datePickerLastSeen;
    @FXML
    public ComboBox categoryDropDown;
    @FXML
    private TableView tableViewSearchMovie, categoryTable;
    @FXML
    private TableColumn titleColumn, yearColumn, categoryColumn;
    @FXML
    private TextField txtFieldSearch, txtFieldIMDBRating, txtFieldPersonalRating, txtFieldMovieTitle, txtFiledMovieFile, txtFieldMovieCategories, txtFieldYear;
    @FXML
    private Button btnInsertFile, btnSearchMovie, btnAddMovie, btnRemoveCategory, btnAddCategory;
    ArrayList<TextField> allTextiles;
    public EditViewController editController;
    private Movie selectedMovie;
    private LocalDate localDate;
    private MovieModel movieModel;
    private CategoryModel categoryModel;
    private ObservableList<Category> categoriesInAddMovie;

    public AddMovieController() {
    }

    /**
     * The first thing that runs, when window opens
     */
    @Override
    public void setup() {
        try {
            movieModel = new MovieModel();
            categoryModel = new CategoryModel();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            makeList();
            categoryModel = new CategoryModel();
            categoryModel.addAllCategoriesToComboBox(categoryDropDown);
            categoryModel.addListenersToNumFields(txtFieldYear);
            categoryModel.addListenersToNumFields(txtFieldPersonalRating);


            btnAddMovie.setDisable(true);
            btnRemoveCategory.setDisable(true);
            btnAddCategory.setDisable(true);

            addListenerCategoryTable();
            categoryDropDown.getItems().remove(0);
            clicks();


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }

    }

    /**
     * Makes a list of all the parameters that are needed to make a movie
     */
    private void makeList() {
        //All the values needed to create a new movie
        allTextiles = new ArrayList<>();
        allTextiles.add(txtFieldMovieTitle);
        allTextiles.add(txtFieldYear);
        allTextiles.add(txtFieldIMDBRating);
        allTextiles.add(txtFieldPersonalRating);
        allTextiles.add(txtFiledMovieFile);
    }


    /**
     * Checks for all the different parameters, that the user is giving as input to the program
     */
    @FXML
    private void shouldNotDisable() {
        for (TextField textfield : allTextiles) {
            if (textfield.getText().equals("") || textfield.getText() == null) {
                return;
            }
        }
        btnAddMovie.setDisable(false);
    }

    /**
     * Opens a window "Sti- finder". The user chose the movie file, the path to the file is copied directly into
     * the program.
     */
    public void handleInsertFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select movie");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Files", "*.mp4"));
        Stage stage = (Stage) btnInsertFile.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            txtFiledMovieFile.setText(String.valueOf(selectedFile));
        }
        shouldNotDisable();
    }

    /**
     * When the add button is clicked, the values are added to a new movie and saved in the database
     */
    public void handleAddMovie() {
        try {
            movieModel = new MovieModel();
            categoryModel = new CategoryModel();
            // Gets the values
            String title = txtFieldMovieTitle.getText();
            int year = Integer.parseInt(txtFieldYear.getText());
            double imdbRating = Double.parseDouble(txtFieldIMDBRating.getText());
            int personalRating = Integer.parseInt(txtFieldPersonalRating.getText());
            String filePath = txtFiledMovieFile.getText();
            String length = "0";

            try {
                Movie movie = new Movie(title, year, length, imdbRating, personalRating, filePath);
                File file = new File(movie.getPathToFile());
                //check if Desktop is supported by Platform or not
                if (!Desktop.isDesktopSupported()) {
                    return;
                }
                Desktop desktop = Desktop.getDesktop();
                //checks file exists or not
                if (!file.exists()) {
                    //shows a message, like the alert boxes
                    Alert alert = new Alert(Alert.AlertType.ERROR, "This movie does not exist on the given filepath");
                    alert.showAndWait();
                    return;
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
                alert.showAndWait();
            }
            IContainer container = IContainer.make(); //The IContainer is used to retrieve the length of the Movie file chosen.
            int result = container.open(filePath, IContainer.Type.READ, null);
            length = String.valueOf(container.getDuration() / 1000000); //The information we receive is in nanoseconds, so we devide by a million to get actual seconds.

            if (personalRating > 10 || personalRating < 0) { //Checks the value of the personal rating to see if it is valid
                Alert alert = new Alert(Alert.AlertType.WARNING, "Personal Rating should be between 0 and 10");
                alert.showAndWait();

            } else if (imdbRating > 10 || imdbRating < 0) { //Checks the value of the IMDB rating to see if it is valid
                Alert alert = new Alert(Alert.AlertType.WARNING, "IMDB Rating should be between 0 and 10");
                alert.showAndWait();

            } else if (year < 1895) { //Checks the value of the year to see if it is valid
                Alert alert = new Alert(Alert.AlertType.WARNING, "The first movie came out in 1895, so I don't think so smartass!");
                alert.showAndWait();

            } else {
                List<Category> categories = categoryTable.getItems().subList(0, categoryTable.getItems().size());
                List<Category> updatedCategories = categoryModel.getUpdatedCategories(categories);
                Movie movie = movieModel.addNewMovie(title, year, length, imdbRating, personalRating, java.sql.Date.valueOf(localDate), filePath); //Adds the movie to the DB
                int mID = movie.getId(); //Retrieves the newly created  ID from the DB
                categoryModel.addCategoriesToMovie(mID, updatedCategories); //Adds both movie and categories to the DB
                closeWindow();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }
    }

    /**
     * looks at the value, when the date is chosen
     */
    public void datePicked() {
        shouldNotDisable();
        localDate = datePickerLastSeen.getValue();
    }

    /**
     * Looks at the movie the user searched for and displays the results in the tableview below
     */
    @FXML
    private void handleSearchMovie() {
        try {
            movieModel = new MovieModel();
            movieModel.searchAddMovie(txtFieldSearch.getText());
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
            yearColumn.setCellValueFactory(new PropertyValueFactory<>("Year"));
            tableViewSearchMovie.getColumns().addAll();
            tableViewSearchMovie.setItems(movieModel.searchAddMovie(txtFieldSearch.getText()));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }
    }


    /**
     * When the user chooses a Movie from the tableview, it's information is automatically filled out in the text fields.
     *
     * @throws Exception
     */
    private void clicks() {
        try {
            tableViewSearchMovie.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                //If something is selected, set the data from the selected property into the text fields
                if (newValue != null) {
                    if (categoriesInAddMovie == null) {
                        selectedMovie = (Movie) tableViewSearchMovie.getSelectionModel().getSelectedItem();
                        Movie m = movieModel.searchSelectedMovie(selectedMovie.getImdbID());
                        txtFieldMovieTitle.setText(selectedMovie.getTitle());
                        txtFieldYear.setText(selectedMovie.getYearString());
                        txtFieldIMDBRating.setText(String.valueOf(m.getImdbRating()));
                    } else {
                        categoriesInAddMovie.clear();
                        selectedMovie = (Movie) tableViewSearchMovie.getSelectionModel().getSelectedItem();
                        Movie m = movieModel.searchSelectedMovie(selectedMovie.getImdbID());
                        txtFieldMovieTitle.setText(selectedMovie.getTitle());
                        txtFieldYear.setText(selectedMovie.getYearString());
                        txtFieldIMDBRating.setText(String.valueOf(m.getImdbRating()));
                    }
                    addCategoriesToChosenMovie();
                    removeAddedCategories();
                }
            });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }
    }

    /**
     * Updates the categoryTable with the categories linked to the chosen movie
     */
    private void addCategoriesToChosenMovie() {
        try {
            categoryModel = new CategoryModel();
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
            categoryTable.getColumns().addAll();
            categoriesInAddMovie = categoryModel.getMovieCategories();
            categoriesInAddMovie.addAll(categoryTable.getItems());
            categoryTable.setItems(categoriesInAddMovie);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }
    }

    /**
     * When categories is clicked, it opens the EditView Window.
     * There the user can delete and add categories
     */
    public void handleCategoriesClick() {
        editController = new EditViewController();
        categoryModel.openNewView("EditView.fxml", "Edit");
    }

    /**
     * Closes the window
     */
    public void closeWindow() {
        Stage stage = (Stage) btnAddMovie.getScene().getWindow();
        stage.close();
    }

    /**
     * When the user wishes to add additional categories to the Movie.
     */
    public void handleAddCategory() {
        if (txtFieldMovieTitle.getText().isEmpty()) { //The user needs to input a title before adding Categories
            Alert alert = new Alert(Alert.AlertType.WARNING, "You need to add a title before you add Categories");
            alert.showAndWait();
            return;
        } else if (categoryDropDown.getSelectionModel().getSelectedItem().equals("")) { //The user needs to choose a Category from the dropdown before he can add it
            Alert alert = new Alert(Alert.AlertType.WARNING, "You need to choose a Category before you can add it");
            alert.showAndWait();
            return;
        } else { //Adds the category to the category table.
            Category category = new Category(categoryDropDown.getSelectionModel().getSelectedItem().toString());
            categoriesInAddMovie = categoryTable.getItems();
            categoriesInAddMovie.add(category);
            categoryTable.setItems(categoriesInAddMovie);
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
            categoryTable.getColumns().addAll();
            removeAddedCategories(); //Removes the Category from the dropdown so the user can't add the same Category twice
        }
        categoryDropDown.setValue(""); //Resets the dropdown menu to blank
    }


    /**
     * Looks after, if anything is selected in the categoryTable
     */
    private void addListenerCategoryTable() {
        categoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //If something is selected, the button will be enabled, else it will be disabled
            btnRemoveCategory.setDisable(newValue == null);
        });
        categoryDropDown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //If something is selected, the button will be enabled, else it will be disabled
            btnAddCategory.setDisable(newValue == null);
        });
    }

    /**
     * When the button is clicked, it removes the specific category from the movie.
     */
    public void handleRemoveCategory() {
        Category category = (Category) categoryTable.getSelectionModel().getSelectedItem();
        categoriesInAddMovie.remove(category);
        categoryTable.setItems(categoriesInAddMovie);
        removeAddedCategories();
    }

    /**
     * This removes the Categories in the dropdown table which are already in the Category tableview
     */
    private void removeAddedCategories() {
        ObservableList cT = categoryTable.getItems(); //Gets the Categories in the tableview
        List<Category> cTL = cT.subList(0, cT.size());
        ArrayList<Category> allCategories = categoryModel.getAllCategories();  //Gets all categories
        allCategories.remove(0); //Removes the "All" Category since that should never be able to be added to a movie
        ObservableList<Category> cD = FXCollections.observableArrayList();
        cD.addAll(allCategories);
        for (Category category : cTL) { //Loops through all Categories in the tableview
            for (Object c : cD) { //Loops through all the Categories in the dropdown
                if (c instanceof Category) {
                    String catTemp = ((Category) c).getCategory();
                    if (category.getCategory().equals(catTemp)) { //If they match, remove the Category from the dropdown
                        cD.remove(c);
                        break;
                    }
                }
            }
        }
        categoryDropDown.setItems(cD);
    }
}