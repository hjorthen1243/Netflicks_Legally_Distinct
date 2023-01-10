package BE;
public class Category {
    private int id;
    private String name;
    private int numberOfMovies;

    public Category(int id, String name){
        this.id = id;
        this.name = name;
    }
    public Category(int id, String name, int numberOfMovies){
        this.id = id;
        this.name = name;
        this.numberOfMovies = numberOfMovies;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfMovies() {
        return numberOfMovies;
    }

    public void setNumberOfMovies(int numberOfMovies) {
        this.numberOfMovies = numberOfMovies;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
