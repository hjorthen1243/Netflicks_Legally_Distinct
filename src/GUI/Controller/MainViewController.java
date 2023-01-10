package GUI.Controller;

import BE.Category;
import BE.Movie;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.Label;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainViewController extends BaseController implements Initializable {

    public ComboBox genreDropDown;
    @FXML
    private TableView movieTable;
    @FXML
    private TableColumn titleColumn, yearColumn, lengthColumn, ratingColumn, pRatingColumn, categoryColumn, lastViewColumn;
    private MovieModel movieModel;
    private CategoryModel categoryModel;
    AddMovieController addController;
    DeleteMovieController delController;
    private Label label;

    @Override
    public void setup() {
            updateMovieList();
            addAllCategoriesToComboBox();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addMovieHandle(ActionEvent event) {
        addController = new AddMovieController();
        OpenNewView(event, "AddMovie.fxml", "Add a movie", addController);

    }

    public void removeMovieHandle(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete File");
        alert.setHeaderText("You are about to delete the movie:\n" + "BATMAN" + "\tfrom" + "0000" + "\nAre you sure want to delete this movie?");
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

    public void savePersonalRatingHandle(ActionEvent event) {
    }

    public void saveLastSeenHandle(ActionEvent event) {
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
        System.out.println("something new is selected");
        movieModel = getModel().getMovieModel();
        Object selectedItem = genreDropDown.getSelectionModel().getSelectedItem();
        String categoryChoosen = selectedItem.toString();
        System.out.println("the choosen category is: " + categoryChoosen);

        ArrayList<Category> allCategories;
        allCategories = categoryModel.getAllCategories();
        for (Category category: allCategories) {
            if(category.getCategory().equals(categoryChoosen)){
                System.out.println("this is entering the loop");
                //ToDo continue with getting all the movies displayed on the list
                try {
                    System.out.println("this is working");
                    movieTable.getItems().clear();
                    movieTable.setItems(movieModel.getObservableMoviesCategory(category));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
