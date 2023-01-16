//TODO remove instead of delete
//TODO @FXML
//TODO genre should be named categories

package GUI.Controller;

import BE.Category;
import BE.Movie;
import GUI.Controller.Methods.Methods;
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

public class MainViewController extends BaseController implements Initializable {

    @FXML
    private Slider sliderPR;
    @FXML
    private Button btnSavePR, btnSaveLastSeen, btnRemoveMovie, btnEdit;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField pRatingMax, pRatingMin, imdbMin, imdbMax;
    @FXML
    private ComboBox<String> categoryDropDown;
    @FXML
    private TableView movieTable;
    @FXML
    private TableColumn titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, categoryColumn, lastViewColumn;
    private MovieModel movieModel;
    private CategoryModel categoryModel;
    private AddMovieController addController;
    private DeleteMovieController delController;
    private EditViewController editController;
    private boolean programStarted = true;

    /**
     * setup runs one time, when the program starts up
     */
    @Override
    public void setup() {

        try {
            updateMovieList();
            Methods.addAllCategoriesToComboBox(categoryDropDown);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sliderPR.setMajorTickUnit(1);
        eventHandler();
        disableEnableComponents(true);
        addListenerMovieTable();
        Methods.addListenersToNumFields(imdbMax);
        Methods.addListenersToNumFields(imdbMin);
        Methods.addListenersToNumFields(pRatingMax);
        Methods.addListenersToNumFields(pRatingMin);
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
     * After window is closed, it tries to update the movietable
     */
    public void startRemoveMovie() {
        delController = new DeleteMovieController();
        Methods.openNewView("RemoveMovie.fxml", "Remove old movies");
        try {
            movieTable.setItems(movieModel.getAllMovies());
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
        Methods.openNewView("AddMovie.fxml", "Add a movie");
        try {
            movieTable.setItems(movieModel.getAllMovies());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    //TODO describe searchHandle();
    public void searchHandle() {
    }

    /**
     * When program opens it updates the MovieTable to show all available movies.
     * Adds the different categories to the movies.
     * If necessary it opens the "RemoveMovie" window
     */
    private void updateMovieList() {
        movieModel = getModel().getMovieModel();
        try {
            //sets the cellValueFactory to all the entities, that a movie has
            Methods.setValues(titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, lastViewColumn, movieTable);
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Categories"));
            updateCategories();

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO add something to the method

    public void CategorySelected() {
    }

    /**
     *
     * @throws Exception
     */
    private void updateCategories() throws Exception {
        categoryModel = new CategoryModel();
        Map<Integer, List<Category>> categoriesAttachedToMovies = categoryModel.getObservableCategories();
        StringBuilder c = new StringBuilder();
        for (int i = 0; i < movieTable.getItems().size() ; i++) {
            Movie m = (Movie) movieTable.getItems().get(i);
            int mID = m.getId();
            if (categoriesAttachedToMovies.containsKey(mID)) { //If the movies from the movietable have a matching ID in  the categoriesAttachedToMovies list, we can get the attached catagories.
                for (int j = 0; j < categoriesAttachedToMovies.get(mID).size(); j++) {
                    c.append(categoriesAttachedToMovies.get(mID).get(j)).append(", ");
                }
                c = c.replace(c.length()-2, c.length(), ""); //Remove the last comma
                m.setCategories(c.toString()); //Set the categories in the movie Object
                c = new StringBuilder(); //Clear the contents of the old String builder
            }
       }
    }




    public void CategorySelected(ActionEvent event) {
        movieModel = getModel().getMovieModel();
        Object selectedItem = categoryDropDown.getSelectionModel().getSelectedItem();
        String categoryChosen = selectedItem.toString();
        ArrayList<Category> allCategories;
        allCategories = categoryModel.getAllCategories();
        for (Category category: allCategories) {
            if(category.getCategory().equals(categoryChosen)){

                try {
                    movieTable.getItems().clear();
                    movieTable.setItems(movieModel.getObservableMoviesCategory(category));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void eventHandler() {
        EventHandler<MouseEvent> onClick = this::clicks;
        movieTable.setRowFactory(param -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(onClick);
            return row;
        });
    }

    /**
     * Handles the mouse button clicks inside songsTable & songsInsidePlaylist table
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

    private void playMedia() {
        try {
            //constructor of file class having file as argument
            Movie movie = (Movie) movieTable.getSelectionModel().getSelectedItem();

            File file = new File(movie.getPathToFile());
            //check if Desktop is supported by Platform or not
            if (!Desktop.isDesktopSupported()){
                System.out.println("not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            //checks file exists or not
            if (file.exists()) {
                desktop.open(file);              //opens the specified file
            } else {
                System.out.println("not existing");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveLastSeenHandle() {
        LocalDate lastSeen = datePicker.getValue();
        Movie movie = (Movie) movieTable.getSelectionModel().getSelectedItem();
        movie.setLastViewDate(java.sql.Date.valueOf(lastSeen));
        try {
            movieModel.updateMovie(movie);
            movieTable.setItems(movieModel.getAllMovies());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(lastSeen);
    }

    public void handleEditCategories() {
        editController = new EditViewController();
        Methods.openNewView("EditView.fxml", "Edit");
    }

    public void handleSavePR() {
        int personalRating = (int) sliderPR.getValue();
        System.out.println(personalRating);
        Movie movie = (Movie) movieTable.getSelectionModel().getSelectedItem();
        movie.setPersonalRating(personalRating);
        try {
            movieModel.updateMovie(movie);
            movieTable.setItems(movieModel.getAllMovies());
        } catch (Exception e) {
            e.printStackTrace();
        }
        movie.setPersonalRating(personalRating);
    }

    private void addListenerMovieTable() {
        movieTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //If something is selected, buttons will be enabled, else they will be disabled
            disableEnableComponents(newValue == null);
        });
    }
}
