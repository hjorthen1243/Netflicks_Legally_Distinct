package BE;
public class Category {
    private int id;
    private String category;
    private int numberOfMovies;

    public Category(int id, String category){
        this.id = id;
        this.category = category;
    }
    public Category(String category){
        this.category = category;
    }
    public Category(int id, String category, int numberOfMovies){
        this.id = id;
        this.category = category;
        this.numberOfMovies = numberOfMovies;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public int getNumberOfMovies() {
        return numberOfMovies;
    }

    public void setNumberOfMovies(int numberOfMovies) {
        this.numberOfMovies = numberOfMovies;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category;
    }
}
