package GUI.Controller;

import BE.Category;
import BE.Movie;
import GUI.Model.CategoryModel;
import GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class EditViewController extends BaseController {
    public TextField txtFieldAddCatDb;
    public Button btnAddCatDb, btnRemoveCatDb, btnAddCatMovie, btnRemoveCatMovie;
    public Button btnSaveChanges;
    public ComboBox comboBoxAddCatMovie, comboBoxRemoveCatMovie, comboBoxRemoveCatDb;
    private CategoryModel categoryModel;


    public void catInMovieSelectedHandle(ActionEvent actionEvent) {

    }

    public void catToMovieSelectedHandle(ActionEvent actionEvent) {

    }

    public void addCatDbHandle(ActionEvent actionEvent) throws Exception {
        categoryModel = new CategoryModel();

        String category = txtFieldAddCatDb.getText();

        categoryModel.createNewCategory(category);


    }

    public void removeCatDbHandle(ActionEvent actionEvent) throws Exception {
        categoryModel = new CategoryModel();

        categoryModel.deleteCategory((Category) comboBoxRemoveCatDb.getSelectionModel().getSelectedItem());
    }

    public void addCatMovieHandle(ActionEvent actionEvent) {

    }

    public void removeCatMovieHandle(ActionEvent actionEvent) {

    }

    public void saveChangesHandle(ActionEvent actionEvent) {

    }

    @Override
    public void setup() {
        addAllCatToDBComboBox();
    }

    private void addAllCatToDBComboBox(){
        categoryModel = getModel().getCategoryModel();
        ArrayList<Category> allCategories;
        allCategories = categoryModel.getAllCategories();
        for (Category category : allCategories) {
            comboBoxRemoveCatDb.getItems().add(category.getCategory());
        }
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
}
