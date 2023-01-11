package GUI.Controller;

import BE.Movie;
import GUI.Model.MovieModel;
import GUI.Model.PMCModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AddMovieController extends BaseController implements Initializable {

    private Movie selectedMovie;
    @FXML
    private TableView tableViewSearchMovie;
    @FXML
    private TableColumn titleColumn, yearColumn;
    @FXML
    private TextField txtFieldSearch, txtFieldIMDBRating,  txtFieldPersonalRating, txtFieldMovieTitle, txtFiledMovieFile, txtFieldMovieCategories, txtFieldYear;
    @FXML
    private Button btnInsertFile, btnSearchMovie;

    private MovieModel movieModel;

    private PMCModel pmcModel;
    @Override
    public void setup() {

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            clicks();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void handleInsertFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select movie");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Files", "*.mp4"));
        Stage stage = (Stage) btnInsertFile.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            txtFiledMovieFile.setText(String.valueOf(selectedFile));
        }
    }

    public void handleAddMovie() {
    }

    @FXML
    private void handleSearchMovie() throws Exception {
        movieModel = new MovieModel();
        movieModel.searchAddMovie(txtFieldSearch.getText());

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("Year"));

        tableViewSearchMovie.getColumns().addAll();
        tableViewSearchMovie.setItems(movieModel.searchAddMovie(txtFieldSearch.getText()));

    }
    private void clicks() throws Exception {
        movieModel = new MovieModel();
        tableViewSearchMovie.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            //If something is selected, set the data from the selected property into the text fields
            if (newValue != null) {
                selectedMovie = (Movie) tableViewSearchMovie.getSelectionModel().getSelectedItem();
                Movie m = movieModel.searchSelectedMovie(selectedMovie.getImdbID());
                txtFieldMovieTitle.setText(selectedMovie.getTitle());
                txtFieldYear.setText(selectedMovie.getYearString());
                txtFieldIMDBRating.setText(String.valueOf(m.getImdbRating()));
                txtFieldMovieCategories.setText(movieModel.getMovieCategories());
            }
        });
    }
}
