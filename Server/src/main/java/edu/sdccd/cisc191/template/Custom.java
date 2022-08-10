package edu.sdccd.cisc191.template;
import java.time.LocalDate;

public class Custom extends Beverage implements Calories {

    private int calories;

    public Custom(double amount, String unit, String name, int calories, LocalDate date) {
        super(amount, unit, name, date);
        this.calories = calories;
    }

    @Override
    public String toString() {
        return super.toString() + " with " + calories + " calories.";
    }

    @Override
    public int caloriesToRuns() {
        return calories / 100;
    }

    //get calories
    public int getCalories() {
        return calories;
    }

    //set calories
    public void setCalories(int calories) {
        this.calories = calories;
    }
}
