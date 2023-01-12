package BE;


import java.util.Date;

public class Movie {

    private int id;
    private String title;
    private int year;
    private String length;
    private String imdbID;
    private double imdbRating;
    private int personalRating;
    private Date lastViewDate;
    private String pathToFile;



    public Movie(int id, String title, int year, String length, double imdbRating, int personalRating, Date lastViewDate, String pathToFile){
        this.id = id;
        this.title = title;
        this.year = year;
        this.length = length;
        this.imdbRating = imdbRating;
        this.personalRating = personalRating;
        this.lastViewDate = lastViewDate;
        this.pathToFile = pathToFile;
    }

    public Movie(String title, int year, String imdbID){
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
    }
    public Movie(String length, double imdbRating){
        this.length = length;
        this.imdbRating = imdbRating;
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

    public int getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(int personalRating) {
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
    public void setYear(int year) {
        this.year = year;
    }
    public String getYearString() {return String.valueOf(year);}

    public Date getLastViewDate() {
        return lastViewDate;

    }

    public void setLastViewDate(Date lastViewDate) {
        this.lastViewDate = lastViewDate;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;

    }

    public String getImdbID() {
        return imdbID;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", length='" + length + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", imdbRating=" + imdbRating +
                ", personalRating=" + personalRating +
                ", lastViewDate=" + lastViewDate +
                ", pathToFile='" + pathToFile + '\'' +
                '}';
    }
}
