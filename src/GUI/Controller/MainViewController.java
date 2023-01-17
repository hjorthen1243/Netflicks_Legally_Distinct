package GUI.Controller;

import BE.Category;
import BE.Movie;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.util.*;
import java.awt.Label;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MainViewController extends BaseController implements Initializable {

    public Slider sliderPR;
    public Button btnSavePR;
    public Button btnSaveLastSeen;
    public DatePicker datePicker;
    public Button btnRemoveMovie;
    @FXML
    private TextField pRatingMax, pRatingMin, imdbMin, imdbMax;
    @FXML
    private ComboBox<String> genreDropDown;
    @FXML
    private Button btnEdit;
    @FXML
    private TableView movieTable;
    @FXML
    private TableColumn titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, categoryColumn, lastViewColumn;
    private MovieModel movieModel;
    private CategoryModel categoryModel;
    AddMovieController addController;
    DeleteMovieController delController;
    EditViewController editController;
    private boolean programStarted = true;

    @Override
    public void setup() {
        updateMovieList();
        addAllCategoriesToComboBox();
    }

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
     * these are the components that needs to be disabled before a movie is chosen
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

    public void startRemoveMovie() {
        delController = new DeleteMovieController();
        Methods.openNewView("DeleteMovie.fxml", "Delete old movies");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateMovieList();
    }

    public void addMovieHandle() {
        addController = new AddMovieController();
        Methods.openNewView("AddMovie.fxml", "Add a movie");
        updateMovieList();
        try {
            movieTable.setItems(movieModel.getAllMovies());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeMovieHandle() {

        try {
            Movie m = (Movie) movieTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove; " + m.getTitle() + " - " + m.getYearString() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                movieModel.deleteMovie(m);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void searchHandle() {
    }

    private void addAllCategoriesToComboBox() {
        categoryModel = getModel().getCategoryModel();
        ArrayList<Category> allCategories;
        allCategories = categoryModel.getAllCategories();
        for (Category category : allCategories) {
            genreDropDown.getItems().add(category.getCategory());
        }
    }


    private void updateMovieList() {
        movieModel = getModel().getMovieModel();
        ObservableList<Movie> m = movieModel.getObservableMovies();
        try {
            if (programStarted) {
                Methods.setValues(titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, lastViewColumn, movieTable);
                categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Categories"));


                programStarted = false;
                ArrayList<Movie> movies;
                Date currentDate = new Date();
                movies = movieModel.getMovies();
                for (Movie movie : movies) {
                    Date movieDate = movie.getLastViewDate();
                    long diffInMillis = Math.abs(currentDate.getTime() - movieDate.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                    long biggestDiff = 730;
                    if (diff > biggestDiff && movie.getPersonalRating() < 6) {
                        startRemoveMovie();
                        break;
                    }
                }
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

    public void CategorySelected() {
    }

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




    public void CategorySelected(ActionEvent event) {
        movieModel = getModel().getMovieModel();
        Object selectedItem = genreDropDown.getSelectionModel().getSelectedItem();
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
