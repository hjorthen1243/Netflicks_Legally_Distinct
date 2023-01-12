package GUI.Model;

public class PMCModel {
    private final MovieModel movieModel;
    private final CategoryModel categoryModel;

    public PMCModel() throws Exception {
        movieModel = new MovieModel();
        categoryModel = new CategoryModel();
    }

    public MovieModel getMovieModel() {return movieModel;}

    public CategoryModel getCategoryModel() {return categoryModel;}

}
