package GUI.Controller;

import GUI.Model.MovieModel;

public abstract class BaseController {

    private MovieModel model;

    /**
     * Sets the model of the controller
     * @param model
     */
    public void setModel(MovieModel model) {this.model = model;}

    /**
     * Get the model of the controller
     * @return the model of the controller
     */

    /**
     * Method for setting up the window
     */
    public abstract void setup();

}