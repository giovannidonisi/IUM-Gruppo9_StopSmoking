package ium.unisa.stopsmoking.db;

public class Benefit {

    private String title, description;
    private int reachedIn, percent;

    public Benefit(String title, String description, int reachedIn, int percent) {
        this.title = title;
        this.description = description;
        this.reachedIn = reachedIn;
        this.percent = percent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReachedIn() {
        return reachedIn;
    }

    public void setReachedIn(int reachedIn) {
        this.reachedIn = reachedIn;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

}
