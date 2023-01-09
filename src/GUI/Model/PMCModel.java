package GUI.Model;

public class PMCModel {
    private MovieModel movieModel;
    private CategoryModel categoryModel;

    public PMCModel() {
        movieModel = new MovieModel();
        categoryModel = new CategoryModel();
    }

    public MovieModel getMovieModel() {return movieModel;}

    public void setMovieModel(){this.movieModel = movieModel;}

    public CategoryModel getCategoryModel() {return categoryModel;}

    public void setCategoryModel() {this.categoryModel = categoryModel;}
}
