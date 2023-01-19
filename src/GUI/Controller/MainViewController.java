//TODO all buttons should start with btn_Name

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

import static javax.swing.JOptionPane.showMessageDialog;

public class MainViewController extends BaseController implements Initializable {

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
    private String imdbMinStr;
    private String imdbMaxStr;
    private String pRateMinStr;
    private String pRateMaxStr;

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
     * When the program is loading, the slider is set to only be able to hit integers.
     * Listeners are added to the different components.
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
     * After window is closed, it tries to update the movieTable
     */
    public void startRemoveMovie() {
        delController = new RemoveMovieController();
        delController.setup();
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
        try {
            addController = new  AddMovieController();
            addController.setup();
            Methods.openNewView("AddMovie.fxml", "Add a movie");

            movieTable.setItems(movieModel.getAllMovies());
            updateCategories();
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
    public void searchHandle(ActionEvent event) {
        if(!searchField.getText().isEmpty()){
            try{
                String query = searchField.getText();
                movieModel.searchMovie(query);
                updateCategories();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        else if(!imdbMin.getText().isEmpty() && imdbMax.getText().isEmpty()){
            try {
                String query = imdbMin.getText();
                    ObservableList<Movie> movies = movieTable.getItems();
                    movieTable.setItems(movieModel.imdbSearchMin(query, movies));
                    updateCategories();
                }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if (!imdbMax.getText().isEmpty() && imdbMin.getText().isEmpty()) {
            String query = imdbMax.getText();
            try {
                    ObservableList<Movie> movies = movieTable.getItems();
                    movieTable.setItems(movieModel.imdbSearchMax(query, movies));
                    updateCategories();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if(!imdbMax.getText().isEmpty() && !imdbMin.getText().isEmpty()){
            try{
                movieTable.setItems(movieModel.imdbSearchMinAndMax(imdbMin.getText(), imdbMax.getText()));
                updateCategories();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        else if(!pRatingMin.getText().isEmpty() && pRatingMax.getText().isEmpty()){
            String query =  pRatingMin.getText();
            try {
                ObservableList<Movie> movies = movieTable.getItems();
                movieTable.setItems(movieModel.pRateSearchMin(query, movies));
                updateCategories();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if (!pRatingMax.getText().isEmpty() && pRatingMin.getText().isEmpty()){
            String query = pRatingMax.getText();
            try {
                    ObservableList<Movie> movies = movieTable.getItems();
                    movieTable.setItems(movieModel.pRateSearchMax(query, movies));
                    updateCategories();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if(!pRatingMax.getText().isEmpty() && !pRatingMin.getText().isEmpty()){
            try{
                movieTable.setItems(movieModel.pRateSearchMinAndMax(pRatingMin.getText(), pRatingMax.getText()));
                updateCategories();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "All search fields are empty");
            alert.show();
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
                Methods.setValues(titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, lastViewColumn, movieTable);
                categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Categories"));
                //sets the cellValueFactory to all the entities, that a movie has
                Methods.setValues(titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, lastViewColumn, movieTable);
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
     *
     * @throws Exception
     */
    private void updateCategories() throws Exception {
        categoryModel = new CategoryModel();
        Map<Integer, List<Category>> categoriesAttachedToMovies = categoryModel.getObservableCategories();
        StringBuilder c = new StringBuilder();
        for (int i = 0; i < movieTable.getItems().size(); i++) {
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

            }}
            else {
            for (Category category : allCategories) {
                if (category.getCategory().equals(categoryChosen)) {

                    try {
                        movieTable.getItems().clear();
                        movieTable.setItems(movieModel.getObservableMoviesCategory(category));
                        updateCategories();
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

    /**
     * Handles the mouse button clicks in the movie table
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
            if (!Desktop.isDesktopSupported()){
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
        Movie movie = (Movie) movieTable.getSelectionModel().getSelectedItem();
        if (movie!= null) {
            Methods.openNewView("EditView.fxml", "Edit:  " + movie.getTitle());
        }
        else {
            Methods.openNewView("EditView.fxml", "Edit categories");
        }
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
    private ArrayList<Movie> iMDbRateSearch() {
        ArrayList<Movie> minValue = new ArrayList<Movie>();
        for (int i = 0; i < movieTable.getHeight(); i++) {

            double minimumVal = Double.parseDouble(imdbMin.getText());
            if (Double.parseDouble((String) movieTable.getColumns().get(4)) <= minimumVal && Double.parseDouble((String) movieTable.getColumns().get(4)) >= minimumVal) {

                minValue.add(i, (Movie) movieTable.getColumns());
            }
        }

        ArrayList<Movie> maxValue = new ArrayList<>();
        for (int i = 0; i < movieTable.getHeight(); i++) {
            double maximumVal = Double.parseDouble(imdbMax.getText());
            if (Double.parseDouble((String) movieTable.getColumns().get(4)) <= maximumVal && Double.parseDouble((String) movieTable.getColumns().get(4)) >= maximumVal) {
                maxValue.add(i, (Movie) movieTable.getColumns());

            }

        }

        ArrayList<Movie> minToMaxValue = null;
        if (Double.parseDouble(imdbMin.getText()) < Double.parseDouble(imdbMax.getText())) {
            minToMaxValue = minValue;
            minToMaxValue.retainAll(maxValue);

            System.out.println(minToMaxValue);
        }
        return minToMaxValue;
    }
}
