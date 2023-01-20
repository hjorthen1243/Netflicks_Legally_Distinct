package GUI.Controller;

import BE.Category;
import BE.Movie;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static javax.swing.JOptionPane.showMessageDialog;

public class MainViewController extends BaseController implements Initializable {
    public static Movie chosenMovie;
    @FXML
    private Slider sliderPR;
    @FXML
    private Button btnSavePR, btnSaveLastSeen, btnRemoveMovie, btnEditCategories, btnSearch;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField pRatingMax, pRatingMin, imdbMin, imdbMax, searchField;
    @FXML
    private ComboBox<String> categoryDropDown;
    @FXML
    private TableView movieTable;
    @FXML
    private TableColumn titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, categoryColumn, lastViewColumn;
    private MovieModel movieModel;
    private CategoryModel categoryModel;
    private AddMovieController addController;
    private RemoveMovieController delController;
    private EditViewController editController;
    private boolean programStarted = true;

    /**
     * setup runs one time, when the program starts up
     */
    @Override
    public void setup() {
        try {
            updateMovieList();
            categoryModel.addAllCategoriesToComboBox(categoryDropDown);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * When the program is loading, the slider is set to only be able to hit integers.
     * Listeners are added to the different components.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CategoryModel categoryModel = null;
        try {
            categoryModel = new CategoryModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        sliderPR.setMajorTickUnit(1);
        eventHandler();
        disableEnableComponents(true);
        addListenerMovieTable();

        ArrayList<TextField> ratings = new ArrayList<>();
        ratings.add(imdbMax);
        ratings.add(imdbMin);
        ratings.add(imdbMax);
        ratings.add(pRatingMin);
        for (TextField txtF : ratings) {
            categoryModel.addListenersToNumFields(txtF);
        }
    }

    /**
     * these are the components that needs to be disabled before a movie is chosen and enabled,
     * when a movie is chosen
     */
    private void disableEnableComponents(Boolean bool) {
        sliderPR.setDisable(bool);
        sliderPR.setValue(5);
        btnSavePR.setDisable(bool);
        btnSaveLastSeen.setDisable(bool);
        datePicker.setDisable(bool);
        datePicker.setValue(null);
        btnRemoveMovie.setDisable(bool);
    }

    /**
     * Opens the window to remove low-rated old movies, when the program is starting.
     * After window is closed, it tries to update the movieTable
     */
    public void startRemoveMovie() {
        try {
            categoryModel = new CategoryModel();
            delController = new RemoveMovieController();
            delController.setup();
            categoryModel.openNewView("RemoveMovie.fxml", "Remove old movies");
            updateMovieTableAndCategories();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Adds a movie to the db by opening the "AddMovie" window
     * After something is added, it tries to update the movie table
     */
    public void addMovieHandle() {
        addController = new AddMovieController();
        addController.setup();
        categoryModel.openNewView("AddMovie.fxml", "Add a movie");
        updateMovieTableAndCategories();

    }

    /**
     * Opens an alert box, when the user is about to delete a specific movie
     */
    public void removeMovieHandle() {

        try {
            Movie m = (Movie) movieTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove; " + m.getTitle() + " - " + m.getYearString() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                movieModel.deleteMovie(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the searches for the title, year imdb scores and personal scores.
     */
    public void searchHandle(ActionEvent event) {
        // Checks if the Search button has the text Clear
        if (btnSearch.getText().equals("Clear")) {
            //If Search button text is clear it resets all values and gets list of all movies
            try {
                searchField.setText("");
                imdbMin.setText("");
                imdbMax.setText("");
                pRatingMin.setText("");
                pRatingMax.setText("");
                categoryDropDown.setValue("");
                updateMovieList();
                updateCategories();
                //Sets the Search button text to Search
                btnSearch.setText("Search");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Checks if the searchField is empty or not
            if (!searchField.getText().isEmpty()) {
                try {
                    //Sets query to be the user input
                    String query = searchField.getText();
                    //Gets the searchMovie method from MovieModel class and sends the query through it
                    ObservableList<Movie> movies = movieTable.getItems();
                    movieModel.searchMovie(query);
                    updateCategories();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Checks if imdbMin is empty or not
            } else if (!imdbMin.getText().isEmpty() && imdbMax.getText().isEmpty()) {
                //Sets query to be the user input
                String query = imdbMin.getText();
                try {

                    //Creates Observable list called movies and sets value to the values in movieTable
                    ObservableList<Movie> movies = movieTable.getItems();
                    //Sets the values in movieTable to  the result of imdbSearchMin after sending the query and the Observable list of movies through
                    movieTable.setItems(movieModel.imdbSearchMin(query, movies));
                    updateCategories();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Checks if imdbMax is empty or not
            } else if (!imdbMax.getText().isEmpty() && imdbMin.getText().isEmpty()) {
                //Sets query to be the user input
                String query = imdbMax.getText();
                try {
                    //Creates Observable list called movies and sets value to the values in movieTable
                    ObservableList<Movie> movies = movieTable.getItems();
                    //Sets the values in movieTable to  the result of imdbSearchMax after sending the query and the Observable list of movies through
                    movieTable.setItems(movieModel.imdbSearchMax(query, movies));
                    updateCategories();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Checks if imdbMax and imdbMin is empty or not
            } else if (!imdbMax.getText().isEmpty() && !imdbMin.getText().isEmpty()) {
                try {
                    //Sets the values in movieTable to the result of imdbSearchMinAndMax after sending the user input from imdbMax and imdbMin through
                    movieTable.setItems(movieModel.imdbSearchMinAndMax(imdbMin.getText(), imdbMax.getText()));
                    updateCategories();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //Checks if pRatingMin is empty or not
            else if (!pRatingMin.getText().isEmpty() && pRatingMax.getText().isEmpty()) {
                //Sets query to be the user input
                String query = pRatingMin.getText();
                try {
                    //Creates Observable list called movies and sets value to the values in movieTable
                    ObservableList<Movie> movies = movieTable.getItems();
                    //Sets the values in movieTable to  the result of pRatingSearchMin after sending the query and the Observable list of movies through
                    movieTable.setItems(movieModel.pRateSearchMin(query, movies));
                    updateCategories();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //Checks if pRatingMax is empty or not
            else if (!pRatingMax.getText().isEmpty() && pRatingMin.getText().isEmpty()) {
                //Sets query to be the user input
                String query = pRatingMax.getText();
                try {
                    //Creates Observable list called movies and sets value to the values in movieTable
                    ObservableList<Movie> movies = movieTable.getItems();
                    //Sets the values in movieTable to  the result of pRatingSearchMax after sending the query and the Observable list of movies through
                    movieTable.setItems(movieModel.pRateSearchMax(query, movies));
                    updateCategories();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //Checks if pRatingMax and pRatingMin is empty or not
            else if (!pRatingMax.getText().isEmpty() && !pRatingMin.getText().isEmpty()) {
                try {
                    //Sets the values in movieTable to the result of pRatingSearchMinAndMax after sending the user input from pRatingMax and pRatingMin through
                    movieTable.setItems(movieModel.pRateSearchMinAndMax(pRatingMin.getText(), pRatingMax.getText()));
                    updateCategories();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //If all the textFields are empty it sends an alert
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "All search fields are empty");
                alert.show();
            }
            btnSearch.setText("Clear");
            categoryDropDown.setValue("");
        }
    }

    /**
     * When program opens it updates the MovieTable to show all available movies.
     * Adds the different categories to the movies.
     * If necessary it opens the "RemoveMovie" window
     */
    private void updateMovieList() {
        movieModel = getModel().getMovieModel();
        try {
            if (programStarted) {
                categoryModel.setValues(titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, lastViewColumn, movieTable);
                categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Categories"));
                //sets the cellValueFactory to all the entities, that a movie has
                categoryModel.setValues(titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, lastViewColumn, movieTable);
                categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Categories"));


                //checks if there are any movies that has a low personal rating and not been seen more than 2 years
                ArrayList<Movie> movies = movieModel.getMovies();
                Date currentDate = new Date();
                for (Movie movie : movies) {
                    Date movieDate = movie.getLastViewDate();
                    long diffInMillis = Math.abs(currentDate.getTime() - movieDate.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                    int biggestDiff = 730;
                    if (diff > biggestDiff && movie.getPersonalRating() < 6) {
                        startRemoveMovie();
                        break;
                    }
                }
                movieTable.setItems(movieModel.getObservableMovies());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        movieTable.setItems(movieModel.getObservableMovies());
        try {
            updateCategories();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @throws Exception
     */
    private void updateCategories() throws Exception {
        categoryModel = new CategoryModel();
        Map<Integer, List<Category>> categoriesAttachedToMovies = categoryModel.getObservableCategories();
        StringBuilder c = new StringBuilder();
        for (int i = 0; i < movieTable.getItems().size(); i++) {
            Movie m = (Movie) movieTable.getItems().get(i);
            int mID = m.getId();
            if (categoriesAttachedToMovies.containsKey(mID)) { //If the movies from the movieTable have a matching ID in  the categoriesAttachedToMovies list, we can get the attached categories.
                for (int j = 0; j < categoriesAttachedToMovies.get(mID).size(); j++) {
                    c.append(categoriesAttachedToMovies.get(mID).get(j)).append(", ");
                }
                c = c.replace(c.length() - 2, c.length(), ""); //Remove the last comma
                m.setCategories(c.toString()); //Set the categories in the movie Object
                c = new StringBuilder(); //Clear the contents of the old String builder
            }
        }
    }

    /**
     * Sorts though the categories, when a category is chosen
     */
    public void categorySelected() throws Exception {
        movieModel = getModel().getMovieModel();
        Object selectedItem = categoryDropDown.getSelectionModel().getSelectedItem();
        String categoryChosen = selectedItem.toString();
        ArrayList<Category> allCategories;
        allCategories = categoryModel.getAllCategories();
        if (categoryChosen.equals("All")) {
            movieTable.setItems(movieModel.getAllMovies());
            try {
                updateCategories();
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            for (Category category : allCategories) {
                if (category.getCategory().equals(categoryChosen)) {

                    try {
                        movieTable.getItems().clear();
                        movieTable.setItems(movieModel.getObservableMoviesCategory(category));
                        updateCategories();
                        searchField.setText("");
                        imdbMin.setText("");
                        imdbMax.setText("");
                        pRatingMax.setText("");
                        pRatingMin.setText("");
                        btnSearch.setText("Search");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Looks after, if something is clicked and calls clicks
     */
    private void eventHandler() {
        EventHandler<MouseEvent> onClick = this::clicks;
        movieTable.setRowFactory(param -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(onClick);
            return row;
        });
    }

    private void updateMovieTableAndCategories() {
        try {
            movieTable.setItems(movieModel.getAllMovies());
            updateCategories();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the mouse button clicks in the movie table
     *
     * @param event mouse button events, specifically double clicks
     */
    private void clicks(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) { //Check for double-click on left MouseButton
            TableRow<Movie> row = (TableRow<Movie>) event.getSource();
            if (row.getItem() != null) { //If the item we are choosing is not zero play that song
                playMedia();
            }
        }
    }


    /**
     * Plays the movie if it exists on the given filepath
     */
    private void playMedia() {
        try {
            //constructor of file class having file as argument
            Movie movie = (Movie) movieTable.getSelectionModel().getSelectedItem();

            File file = new File(movie.getPathToFile());
            //check if Desktop is supported by Platform or not
            if (!Desktop.isDesktopSupported()) {
                System.out.println("not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            //checks file exists or not
            if (file.exists()) {
                desktop.open(file);              //opens the specified file
            } else {
                //shows a message, like the alert boxes
                showMessageDialog(null, "This movie does not exist on the given filepath");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Saves the given value from the DatePicker, and updates the specific movie.
     */
    public void saveLastSeenHandle() {
        LocalDate lastSeen = datePicker.getValue();
        Movie movie = (Movie) movieTable.getSelectionModel().getSelectedItem();
        movie.setLastViewDate(java.sql.Date.valueOf(lastSeen));
        try {
            movieModel.updateMovie(movie);
            movieTable.setItems(movieModel.getAllMovies());
            updateCategories();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Edit the different categories
     */
    public void handleEditCategories() {
        editController = new EditViewController();
        editController.setup();
        chosenMovie = (Movie) movieTable.getSelectionModel().getSelectedItem();
        EditViewController editViewController = new EditViewController();
        if (chosenMovie != null) {
            categoryModel.openNewView("EditView.fxml", "Edit:  " + chosenMovie.getTitle());
        } else {
            categoryModel.openNewView("EditView.fxml", "Edit categories");
        }
        //Update movieTable and categories
        updateMovieTableAndCategories();
    }

    /**
     * Saves the given value from the PersonalRating Slider, and updates the specific movie.
     */
    public void handleSavePR() {
        int personalRating = (int) sliderPR.getValue();
        Movie movie = (Movie) movieTable.getSelectionModel().getSelectedItem();
        movie.setPersonalRating(personalRating);
        try {
            movieModel.updateMovie(movie);
            movieTable.setItems(movieModel.getAllMovies());
            updateCategories();
        } catch (Exception e) {
            e.printStackTrace();
        }
        movie.setPersonalRating(personalRating);
    }

    /**
     * Adds a listener to the table, and the eventHandler looks after the clicks
     */
    private void addListenerMovieTable() {
        movieTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //If something is selected, buttons will be enabled, else they will be disabled
            disableEnableComponents(newValue == null);
        });
    }
}
