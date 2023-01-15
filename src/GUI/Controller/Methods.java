package GUI.Controller;

import BE.Movie;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Methods {

    static void setValues(TableColumn titleColumn, TableColumn yearColumn, TableColumn lengthColumn, TableColumn ratingColumn, TableColumn pRatingColumn, TableColumn lastViewColumn, TableView movieTable) {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("Year"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("ImdbRating"));
        pRatingColumn.setCellValueFactory(new PropertyValueFactory<>("PersonalRating"));
        lastViewColumn.setCellValueFactory(new PropertyValueFactory<>("LastViewDate"));

        movieTable.getColumns().addAll();
    }
}
