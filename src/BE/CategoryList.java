package BE;

public class CategoryList {
    private int id;
    private String title;
    private int numbersOfMovies;


    public CategoryList(int id, String title, int numbersOfMovies){
        this.id = id;
        this.title = title;
        this.numbersOfMovies = numbersOfMovies;
    }

    public String getTitle() {
        return title;
    }

    public int getNumbersOfMovies() {
        return numbersOfMovies;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }
}
