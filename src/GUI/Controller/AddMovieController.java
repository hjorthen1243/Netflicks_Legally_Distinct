package GUI.Controller;

import GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddMovieController extends BaseController {
    public Button btnInsertFile;
    public TextField txtFiledMovieFile;
    public TextField txtFieldIMDBRating;
    public TextField txtFieldPersonalRating;
    public TextField barURL;
    public TextField txtFieldMovieTitle;
    public TextField txtFieldMovieGenres;
    public TextField txtFieldYear;
    public DatePicker datePickerLastSeen;
    public java.sql.Date date;
    public LocalDate localDate;
    MovieModel movieModel;
    ArrayList<TextField> allTextfiels;

    @Override
    public void setup() {
        //All the values needed to create a new movie
        allTextfiels = new ArrayList<>();
        allTextfiels.add(txtFieldMovieTitle);
        allTextfiels.add(txtFieldMovieGenres);
        allTextfiels.add(txtFieldYear);
        allTextfiels.add(txtFieldIMDBRating);
        allTextfiels.add(txtFieldPersonalRating);
        allTextfiels.add(txtFiledMovieFile);

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
    public void handleAddMovie(ActionEvent actionEvent) throws RuntimeException {
        try {
            movieModel = new MovieModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String title = txtFieldMovieTitle.getText();
        int year = Integer.parseInt(txtFieldYear.getText());
        String lenght = null;
        double imdbRating = Double.parseDouble(txtFieldIMDBRating.getText());
        int personalRating = Integer.parseInt(txtFieldPersonalRating.getText());
        String filePath = txtFiledMovieFile.getText();
        try {
            movieModel.addNewMovie(title, year, null, imdbRating, personalRating, java.sql.Date.valueOf(localDate), filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //Date lastView = lastViewed.
    }


    public void datePicked(ActionEvent event) {
        localDate = datePickerLastSeen.getValue();
        /**
        System.out.println("Det virker  " + convertToDateViaInstant(localDate));
        date = new java.sql.Date();
        date = new java.sql.Date(java.sql.Date) convertToDateViaInstant(localDate);
         */
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
