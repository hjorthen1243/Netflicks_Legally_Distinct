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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.util.*;
import java.awt.Label;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class MainViewController extends BaseController implements Initializable {

    public Slider sliderPR;
    public Button btnSavePR;
    public Button btnSaveLastSeen;
    public DatePicker datePicker;
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

    @Override
    public void setup() {

        try {
            updateMovieList();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        addAllCategoriesToComboBox();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sliderPR.setMajorTickUnit( 1 );
        eventHandler();
        disableEnableComponents(true);
        addListenerMovieTable();
    }

    /**
     * these are the components that needs to be disabled before a movie is choosen
     */
    private void disableEnableComponents(Boolean bool) {
        sliderPR.setDisable(bool);
        btnSavePR.setDisable(bool);
        btnSaveLastSeen.setDisable(bool);
        datePicker.setDisable(bool);
    }

    public void addMovieHandle(ActionEvent event) {
        addController = new AddMovieController();
        OpenNewView(event, "AddMovie.fxml", "Add a movie", addController);
        try {
            movieTable.setItems(movieModel.getAllMovies());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeMovieHandle(ActionEvent event) {

        try {
        Movie m = (Movie) movieTable.getFocusModel().getFocusedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + m.getTitle() + " - " + m.getYearString() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
                movieModel.deleteMovie(m);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private void OpenNewView(ActionEvent event, String fxmlName, String displayName, BaseController controller) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GUI/View/" + fxmlName));
            AnchorPane pane = loader.load();
            controller = loader.getController();
            //controller.setModel(super.getModel());
            //controller.setup();
            // Create the dialog stage
            Stage dialogWindow = new Stage();
            dialogWindow.setTitle(displayName);
            dialogWindow.initModality(Modality.WINDOW_MODAL);
            dialogWindow.initOwner(((Node) event.getSource()).getScene().getWindow());
            Scene scene = new Scene(pane);
            dialogWindow.setScene(scene);
            dialogWindow.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
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

    private void updateMovieList() throws Exception {
        movieModel = getModel().getMovieModel();
        ObservableList<Movie> m = movieModel.getObservableMovies();
        movieTable.getColumns().addAll();
        movieTable.setItems(m);

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("Year"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("ImdbRating"));
        pRatingColumn.setCellValueFactory(new PropertyValueFactory<>("PersonalRating"));
        lastViewColumn.setCellValueFactory(new PropertyValueFactory<>("LastViewDate"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Categories"));
        updateCategories();

    }

    private void updateCategories() throws Exception {
        categoryModel = new CategoryModel();
        Map<Integer, List<Category>> categoriesAttachedToMovies = categoryModel.getObservableCategories();
        StringBuilder c = new StringBuilder();
        for (int i = 0; i < categoriesAttachedToMovies.size(); i++) {
            Movie m = (Movie) movieTable.getItems().get(i);
            int mID = m.getId();
            for (int j = 0; j < categoriesAttachedToMovies.get(mID).size(); j++) {
                c.insert(0, categoriesAttachedToMovies.get(mID).get(j).getCategory() + ", ");
            }
            c.delete(c.length()-1,c.length());
            m.setCategories(c.toString());
        }

        System.out.println(c);

        movieTable.getColumns().addAll();
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
        System.out.println(lastSeen);
    }

    public void handleEditCategories(ActionEvent actionEvent) {
        editController = new EditViewController();
        OpenNewView(actionEvent, "EditView.fxml", "Edit", editController);
    }

    public void handleSavePR(ActionEvent actionEvent) {
        int personalrating = (int) sliderPR.getValue();
        System.out.println(personalrating);
        Movie movie = (Movie) movieTable.getSelectionModel().getSelectedItem();
        movie.setPersonalRating(personalrating);
        try {
            movieModel.updateMovie(movie);
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
