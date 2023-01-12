package GUI.Controller;

import BE.Category;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import BE.Movie;
import GUI.Model.PMCModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.net.URL;
import java.util.ResourceBundle;
public class AddMovieController extends BaseController implements Initializable{
    public DatePicker datePickerLastSeen;
    public java.sql.Date date;
    public LocalDate localDate;
    public Button btnAddMovie;
    public ComboBox categoryDropDown;
    ArrayList<TextField> allTextfiels;

    public EditViewController editController;
    private Movie selectedMovie;
    @FXML
    private TableView tableViewSearchMovie;
    @FXML
    private TableColumn titleColumn, yearColumn;
    @FXML
    private TextField txtFieldSearch, txtFieldIMDBRating,  txtFieldPersonalRating, txtFieldMovieTitle, txtFiledMovieFile, txtFieldMovieCategories, txtFieldYear;
    @FXML
    private Button btnInsertFile, btnSearchMovie;
    MainViewController methods;

    private MovieModel movieModel;

    private PMCModel pmcModel;
    private CategoryModel categoryModel;


    @Override
    public void setup() {
    }

    private void genreDropDownUpdate() {
        try {
            categoryModel = new CategoryModel();
            ArrayList<Category> allCategories;
            allCategories = categoryModel.getAllCategories();
            for (Category category: allCategories) {
                categoryDropDown.getItems().add(category.getCategory());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            makelist();
            disableBtn();
            genreDropDownUpdate();
            clicks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makelist() {
        //All the values needed to create a new movie
        allTextfiels = new ArrayList<>();
        allTextfiels.add(txtFieldMovieTitle);
        allTextfiels.add(txtFieldYear);
        allTextfiels.add(txtFieldIMDBRating);
        allTextfiels.add(txtFieldPersonalRating);
        allTextfiels.add(txtFiledMovieFile);
    }


    @FXML
    private void shouldNotDisable() {

        for (TextField textfield: allTextfiels) {
            if(textfield.getText().equals("") || textfield.getText() == null){
                System.out.println("Still need: " + textfield.getId());
                return;
            }
        }
        btnAddMovie.setDisable(false);

    }

    private void disableBtn() {
        btnAddMovie.setDisable(true);
    }

    public void handleInsertFile(ActionEvent actionEvent) {
        shouldNotDisable();
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
    public void handleAddMovie(ActionEvent actionEvent) throws RuntimeException {
        try {
            movieModel = new MovieModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String title = txtFieldMovieTitle.getText();
        int year = Integer.parseInt(txtFieldYear.getText());
        //String lenght = null;
        double imdbRating = Double.parseDouble(txtFieldIMDBRating.getText());
        int personalRating = Integer.parseInt(txtFieldPersonalRating.getText());
        String filePath = txtFiledMovieFile.getText();

        try {
            movieModel.addNewMovie(title, year, null, imdbRating, personalRating, java.sql.Date.valueOf(localDate), filePath);
            closeWindow();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void datePicked(ActionEvent event) {
        shouldNotDisable();
        localDate = datePickerLastSeen.getValue();
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

    public void handleCategoriesClick(MouseEvent mouseEvent) {
        editController = new EditViewController();
        OpenNewView(mouseEvent, "EditView.fxml", "Edit", editController);
    }

    private void OpenNewView(MouseEvent event, String fxmlName, String displayName, BaseController controller) {
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

        /**
         * Closes the window
         */
        public void closeWindow() {
            Stage stage = (Stage) btnAddMovie.getScene().getWindow();
            stage.close();

        }
    }

