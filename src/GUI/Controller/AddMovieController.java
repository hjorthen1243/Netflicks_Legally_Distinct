package GUI.Controller;

import BE.Category;
import BE.Movie;
import GUI.Controller.Methods.Methods;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import com.xuggle.xuggler.IContainer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.VideoTrack;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static javax.swing.JOptionPane.showMessageDialog;


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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            makeList();
            Methods.addAllCategoriesToComboBox(categoryDropDown);
            Methods.addListenersToNumFields(txtFieldYear);
            Methods.addListenersToNumFields(txtFieldPersonalRating);

            btnAddMovie.setDisable(true);
            btnRemoveCategory.setDisable(true);
            btnAddCategory.setDisable(true);

            addListenerCategoryTable();
            categoryDropDown.getItems().remove(0);
            clicks();

        } catch (Exception e) {
            e.printStackTrace();
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
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Files", "*.mp4"));
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
                if (file.exists()) {
                    desktop.open(file);              //opens the specified file
                } else {
                    //shows a message, like the alert boxes
                    Alert alert = new Alert(Alert.AlertType.ERROR, "This movie does not exist on the given filepath");
                    alert.showAndWait();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            IContainer container = IContainer.make();
            int result = container.open(filePath, IContainer.Type.READ, null);
            length = String.valueOf(container.getDuration() / 1000000);

            if (personalRating > 10 || personalRating < 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Personal Rating should be between 0 and 10");
                alert.showAndWait();

            } else if (imdbRating > 10 || imdbRating < 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "IMDB Rating should be between 0 and 10");
                alert.showAndWait();
            } else if (year < 1895) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "The first movie came out in 1895, so I don't think so smartass!");
                alert.showAndWait();
            } else {
                List<Category> categories = categoryTable.getItems().subList(0, categoryTable.getItems().size());
                List<Category> updatedCategories = categoryModel.getUpdatedCategories(categories);
                Movie movie = movieModel.addNewMovie(title, year, length, imdbRating, personalRating, java.sql.Date.valueOf(localDate), filePath);
                int mID = movie.getId();
                categoryModel.addCategoriesToMovie(mID, updatedCategories);
                closeWindow();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
     * Looks at the movie the user searched for.
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
            e.printStackTrace();
        }
    }


    /**
     * Looks at the movie table, with new movies and sets the values in the text-fields
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
                        addCategoriesToChosenMovie();
                    } else {
                        categoriesInAddMovie.clear();
                        selectedMovie = (Movie) tableViewSearchMovie.getSelectionModel().getSelectedItem();
                        Movie m = movieModel.searchSelectedMovie(selectedMovie.getImdbID());
                        txtFieldMovieTitle.setText(selectedMovie.getTitle());
                        txtFieldYear.setText(selectedMovie.getYearString());
                        txtFieldIMDBRating.setText(String.valueOf(m.getImdbRating()));
                        addCategoriesToChosenMovie();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the categoryTable over the different categories linked to the chosen movie
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
            e.printStackTrace();
        }
    }

    /**
     * when categories is clicked, it opens the EditView Window. there the user can do the
     * deleted and create/add-functions to categorize
     */
    public void handleCategoriesClick() {
        editController = new EditViewController();
        Methods.openNewView("EditView.fxml", "Edit");
    }

    /**
     * Closes the window
     */
    public void closeWindow() {
        Stage stage = (Stage) btnAddMovie.getScene().getWindow();
        stage.close();
    }

    /**
     * When a category is chosen this method looks at what has been chosen, and if the category is already
     * linked to the movie
     */
    public void categoryChosen() {
        Category category = new Category(categoryDropDown.getSelectionModel().getSelectedItem().toString());
        //checks if the category already is linked to the movie
        if (!categoryTable.toString().contains(category.getCategory())) {
            btnAddCategory.setDisable(false);
        }
    }

    public void handleAddCategory() {
        if (txtFieldMovieTitle.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You need to add a title before you add Categories");
            alert.showAndWait();
        } else {
            Category category = new Category(categoryDropDown.getSelectionModel().getSelectedItem().toString());
            categoriesInAddMovie = categoryTable.getItems();
            categoriesInAddMovie.add(category);
            categoryDropDown.setValue(1);
            categoryTable.setItems(categoriesInAddMovie);
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
            categoryTable.getColumns().addAll();
        }

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
     * When button is clicked, it removes the specific category from the movie.
     */
    public void handleRemoveCategory() {
        Category category = (Category) categoryTable.getSelectionModel().getSelectedItem();
        categoriesInAddMovie.remove(category);
        categoryTable.setItems(categoriesInAddMovie);
    }
}

