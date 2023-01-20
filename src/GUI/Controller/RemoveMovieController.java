package GUI.Controller;

import BE.Category;
import BE.Movie;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RemoveMovieController extends BaseController implements Initializable {
    @FXML
    private TableColumn titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, lastViewColumn, categoryColumn;
    @FXML
    private Button btnRemoveMovie;
    @FXML
    private TableView movieTable;
    private MovieModel movieModel;
    private ObservableList<Movie> observableMovies;
    private ArrayList<Movie> movies;
    private boolean isStarting = true;
    private ArrayList<Movie> moviesToDelete;
    private CategoryModel categoryModel;

    @Override
    public void setup() {
        try{
            movieModel = new MovieModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs one time, when the window opens.
     * Starts by disable the button remove., because nothing is selected.
     * makes an observable list.
     * sets the values in the movieTable and adds a listener.
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
        disableEnableComponents(true);
        observableMovies = FXCollections.observableArrayList();
        updateMovieList();
        addListenerMovieTable();
    }

    /**
     * Disables or enables the button depending on the boolean sent into the method
     * @param bool if true: disable. if false: enable
     */
    private void disableEnableComponents(Boolean bool) {
        btnRemoveMovie.setDisable(bool);
    }

    /**
     * Gives the user the option to remove all the low-rated not seen for 2 years movies at once.
     * It opens an alert box to make sure, that is what the user wants.
     */
    public void removeAll() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove all the movies with a personal" +
                    "rating below 6 and have not been seen the last 2 years", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                for (Movie m: moviesToDelete) {
                    movieModel.deleteMovie(m);
                }
                observableMovies.clear();
                updateMovieList();
            }
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }
    }

    /**
     * Linked to the remove movie button, it is enabled, when something in the tableView is chosen.
     * Opens an alert to make sure the user want to remove the specific movie, if yes, it removes the movie.
     */
    public void removeMovie() {
        try {
            Movie m = (Movie) movieTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove; " + m.getTitle() + " - " + m.getYearString() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                movieModel.deleteMovie(m);
                observableMovies.remove(m);
                updateMovieList();
            }
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }
    }

    /**
     * When window opens the different lists are created and the movies that has the specific values are added to the
     * remove-Movie-Table.
     */
    public void updateMovieList() {
        try {
            if(isStarting) {
                CategoryModel categoryModel = new CategoryModel();
                isStarting = false;
                movies = new ArrayList<>();
                moviesToDelete = new ArrayList<>();
                movieModel = new MovieModel();
                Date currentDate = new Date();
                movies = movieModel.getMovies();

                //Adds movies to the tableView
                for (Movie movie : movies) {
                    Date movieDate = movie.getLastViewDate();
                    long diffInMillis = Math.abs(currentDate.getTime() - movieDate.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                    long biggestDiff = 730;
                    if (diff > biggestDiff && movie.getPersonalRating() < 6) {
                        observableMovies.add(movie);
                        moviesToDelete.add(movie);
                        categoryModel.setValues(titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, lastViewColumn, movieTable);
                        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Categories"));
                    }
                }
            }
            movieTable.setItems(observableMovies);
            updateCategories();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }
    }

    private void updateCategories() throws Exception {
        categoryModel = new CategoryModel();
        Map<Integer, List<Category>> categoriesAttachedToMovies = categoryModel.getObservableCategories();
        StringBuilder c = new StringBuilder();
        for (int i = 0; i < movieTable.getItems().size(); i++) {
            Movie m = (Movie) movieTable.getItems().get(i);
            int mID = m.getId();
            if (categoriesAttachedToMovies.containsKey(mID)) {
                //If the movies from the movieTable have a matching ID in  the categoriesAttachedToMovies list, we can get the attached categories.
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
     * Looks at the table, to find out, if there are any new values, that are selected.
     * If something is selected, then the disabled button, will be enabled
     */
    private void addListenerMovieTable() {
        movieTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //If something is selected, buttons will be enabled, else they will be disabled
                disableEnableComponents(newValue == null);
        });
    }
}
