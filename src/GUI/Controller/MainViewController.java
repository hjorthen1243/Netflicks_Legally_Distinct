package GUI.Controller;

import BE.Category;
import BE.Methods;
import BE.Movie;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.Label;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
    private ComboBox genreDropDown;
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
    private Label label;
    private boolean programmStartet = true;

    @Override
    public void setup() {
        updateMovieList();
        addAllCategoriesToComboBox();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sliderPR.setMajorTickUnit( 1 );
        eventHandler();
        disableEnableComponents(true);
        addListenerMovieTable();
        Methods.addListenersToNumFields(imdbMax);
        Methods.addListenersToNumFields(imdbMin);
        Methods.addListenersToNumFields(pRatingMax);
        Methods.addListenersToNumFields(pRatingMin);
    }

    /**
     * these are the components that needs to be disabled before a movie is choosen
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
    public void startRemoveMovie(){
        delController = new DeleteMovieController();
        //openNewView("DeleteMovie.fxml", "Delete old movies", delController);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GUI/View/DeleteMovie.fxml"));
            AnchorPane pane = loader.load();
            delController = loader.getController();
            Stage dialogWindow = new Stage();
            dialogWindow.setTitle("Remove movies");
            dialogWindow.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(pane);
            dialogWindow.setScene(scene);
            dialogWindow.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMovieHandle(ActionEvent event) {
        addController = new AddMovieController();
        Methods.openNewView("AddMovie.fxml", "Add a movie");
        updateMovieList();
        try {
            movieTable.setItems(movieModel.getAllMovies());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeMovieHandle(ActionEvent event) {

        try {
        Movie m = (Movie) movieTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove; " + m.getTitle() + " - " + m.getYearString() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
                movieModel.deleteMovie(m);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void searchHandle(ActionEvent event) {
    }

    private void addAllCategoriesToComboBox() {
        categoryModel = getModel().getCategoryModel();
        ArrayList<Category> allCategories;
        allCategories = categoryModel.getAllCategories();
        for (Category category: allCategories) {
            genreDropDown.getItems().add(category.getCategory());
        }
    }

    private void updateMovieList() {

        movieModel = getModel().getMovieModel();

        try {
            if (programmStartet) {
                Methods.setValues(titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, lastViewColumn, movieTable);
                programmStartet = false;
                ArrayList<Movie> movies = new ArrayList<>();
                Date currentDate = new Date();
                movies = movieModel.getMovies(movies);
                for (Movie movie : movies) {
                    Date moviedate = movie.getLastViewDate();
                    long diffInMillies = Math.abs(currentDate.getTime() - moviedate.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    long biggestDiff = 730;
                    if (diff > biggestDiff && movie.getPersonalRating() < 6) {
                        startRemoveMovie();
                        updateMovieList();
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        movieTable.setItems(movieModel.getObservableMovies());

    }

    public void CategorySelected(ActionEvent event) {
        movieModel = getModel().getMovieModel();
        Object selectedItem = genreDropDown.getSelectionModel().getSelectedItem();
        String categoryChoosen = selectedItem.toString();
        ArrayList<Category> allCategories;
        allCategories = categoryModel.getAllCategories();
        for (Category category: allCategories) {
            if(category.getCategory().equals(categoryChoosen)){
                try {
                    movieTable.getItems().clear();
                    movieTable.setItems(movieModel.getObservableMoviesCategory(category));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void eventHandler(){
        EventHandler<MouseEvent> onClick = this::clicks;
        movieTable.setRowFactory(param -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(onClick);
            return row;
        });
    }

    /**
     * Handles the mouse button clicks inside songsTable & songsInsidePlaylist table
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
            //File file = new File("C:\\Users\\aneho\\OneDrive\\Skrivebord\\test.txt");
            if (!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
            {
                System.out.println("not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) {         //checks file exists or not
                desktop.open(file);              //opens the specified file
            }
            else {
                System.out.println("not existing");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public void saveLastSeenHandle(ActionEvent actionEvent) {
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

    public void handleEditCategories(ActionEvent actionEvent) {
        editController = new EditViewController();
        Methods.openNewView("EditView.fxml", "Edit");
    }

    public void handleSavePR(ActionEvent actionEvent) {
        int personalrating = (int) sliderPR.getValue();
        System.out.println(personalrating);
        Movie movie = (Movie) movieTable.getSelectionModel().getSelectedItem();
        movie.setPersonalRating(personalrating);
        try {
            movieModel.updateMovie(movie);
            movieTable.setItems(movieModel.getAllMovies());
        } catch (Exception e) {
            e.printStackTrace();
        }
        movie.setPersonalRating(personalrating);
    }

    private void addListenerMovieTable() {
        movieTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //If something is selected, buttons will be enabled, else they will be disabled
            if (newValue != null) {
                disableEnableComponents(false);
            } else {
                disableEnableComponents(true);
            }
        });
    }
}
