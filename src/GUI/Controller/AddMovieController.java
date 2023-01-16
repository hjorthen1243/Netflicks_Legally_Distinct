package GUI.Controller;

import BE.Category;
import BE.Movie;
import BLL.CategoryManager;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import GUI.Model.PMCModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private TableView tableViewSearchMovie, categoryTable;
    @FXML
    private TableColumn titleColumn, yearColumn, categoryColumn;
    @FXML
    private TextField txtFieldSearch, txtFieldIMDBRating,  txtFieldPersonalRating, txtFieldMovieTitle, txtFiledMovieFile, txtFieldMovieCategories, txtFieldYear;
    @FXML
    private Button btnInsertFile, btnSearchMovie;
    MainViewController methods;

    private MovieModel movieModel;

    private PMCModel pmcModel;
    private CategoryModel categoryModel;

    private ObservableList<Category> categoriesInAddMovie;


    @Override
    public void setup() {
    }

    private void genreDropDownUpdate() {
        try {
            categoryModel = new CategoryModel();
            ArrayList<Category> allCategories;
            allCategories = categoryModel.getAllCategories();
            allCategories.remove(0);
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
            Methods.addListenersToNumFields(txtFieldYear);
            Methods.addListenersToNumFields(txtFieldPersonalRating);
            Methods.addListenersToNumFields(txtFieldIMDBRating);
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
        double imdbRating = Double.parseDouble(txtFieldIMDBRating.getText());
        int personalRating = Integer.parseInt(txtFieldPersonalRating.getText());
        String filePath = txtFiledMovieFile.getText();
        List<Category> categories = categoryTable.getItems().subList(0,categoryTable.getItems().size());
        try {
            movieModel.addNewMovie(title, year, null, imdbRating, personalRating, java.sql.Date.valueOf(localDate), filePath);
            categoryModel.addCategoriesToMovie(categories);
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
                addCategoriesToChosenMovie();
            }
        });
    }

    private void addCategoriesToChosenMovie() {
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
        categoryTable.getColumns().addAll();
        categoryTable.setItems(categoryModel.getMovieCategories());
        categoriesInAddMovie = categoryModel.getMovieCategories();

    }

    public void handleCategoriesClick(MouseEvent mouseEvent) {
        editController = new EditViewController();
        Methods.openNewView("EditView.fxml", "Edit");
    }


    private void addListenersToNumFiels(TextField txtfield) {

        // force the field to be numeric only
        txtfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtfield.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    /**
     * Closes the window
     * */
    public void closeWindow() {
        Stage stage = (Stage) btnAddMovie.getScene().getWindow();
        stage.close();
        }

    public void handleAddCategory(ActionEvent actionEvent) {
        if (categoryDropDown.getSelectionModel().getSelectedItem() != null) {
            List<Category> d = categoriesInAddMovie.subList(0, categoriesInAddMovie.size());
            ObservableList<Category> e = FXCollections.observableArrayList();
            Category category = new Category(categoryDropDown.getSelectionModel().getSelectedItem().toString());
            if (!d.toString().contains(category.getCategory())) {
                d.add(category);
                e.addAll(d);
                categoryTable.setItems(e);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Category already attached to movie");
                alert.showAndWait();
            }

        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a Category to be added in the dropdown menu");
            alert.showAndWait();
        }

    }

    public void handleRemoveCategory(ActionEvent actionEvent) {
        if (categoryTable.getFocusModel().getFocusedIndex()  >=0 && categoryTable.getFocusModel().getFocusedIndex() < categoryTable.getItems().size()) {
            categoriesInAddMovie.remove(categoryTable.getFocusModel().getFocusedIndex());
            categoryTable.setItems(categoriesInAddMovie);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please choose a Category to be deleted");
            alert.showAndWait();
        }
    }
}

