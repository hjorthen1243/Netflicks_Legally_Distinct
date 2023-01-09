package BE;


import java.io.File;
import java.util.Date;

public class Movie {
    private String lastViewed;
    private int id;
    private double imdbRating;
    private double personalRating;
    private String title;
    private String pathToFile;
    private int year;
    private Date lastView;

    public Movie(int id, double imdbRating, double personalRating, String title, String pathToFile, int year){
        this.id = id;
        this.imdbRating = imdbRating;
        this.personalRating = personalRating;
        this.title = title;
        this.pathToFile = pathToFile;
        this.year = year;
    }
    public Movie(int id, double imdbRating, String title, String pathToFile, String lastViewed){
        this.id = id;
        this.imdbRating = imdbRating;
        this.title = title;
        this.pathToFile = pathToFile;
        this.lastViewed = lastViewed;
    }

    public int getId() {
        return id;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public double getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(double personalRating) {
        this.personalRating = personalRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public int getYear() {
        return year;
    }
    public String getYearString(){return String.valueOf(year);}

    public void setYear(int year) {
        this.year = year;
    }
    public Date getLastView() {
        return lastView;
    }

    public void setLastView(Date lastView) {
        this.lastView = lastView;
    }
}
