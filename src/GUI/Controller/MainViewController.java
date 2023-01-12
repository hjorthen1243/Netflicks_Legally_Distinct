package GUI.Controller;

import BE.Category;
import BE.Movie;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainViewController extends BaseController implements Initializable {

    public ComboBox genreDropDown;
    public Button btnEdit;
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
            updateMovieList();
            addAllCategoriesToComboBox();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventHandler();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete File");
        alert.setHeaderText("You are about to delete the movie:\n" + "BATMAN" + "\tfrom:   " + "0000" + "\nAre you sure want to delete this movie?");
        alert.setContentText("");
        Optional<ButtonType> option = alert.showAndWait();
        option.get();

        //delController = new DeleteMovieController();
        //OpenNewView(event, "DeleteMovie.fxml", "Delete a movie", delController);
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

    private void updateMovieList() {
        movieModel = getModel().getMovieModel();

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("Year"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("ImdbRating"));
        pRatingColumn.setCellValueFactory(new PropertyValueFactory<>("PersonalRating"));
        lastViewColumn.setCellValueFactory(new PropertyValueFactory<>("LastViewDate"));

        movieTable.getColumns().addAll();
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
                System.out.println("heard the double click");
                playMedia();

            }
        }
    }

    private void playMedia() {
        try {//constructor of file class having file as argument
            File file = new File("C:\\Users\\aneho\\OneDrive\\Billeder\\Filmrulle\\HueFilm.mp4");
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

    public void editHandle(ActionEvent actionEvent) {
        editController = new EditViewController();
        OpenNewView(actionEvent, "EditView.fxml", "Edit", editController);

    }
}
