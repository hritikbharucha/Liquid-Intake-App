package edu.sdccd.cisc191.template;

public class Soda extends Beverage implements Liquid, Calories{

    private int calories;

    public Soda(double amount, String unit, int calories) {
        super(amount, unit);
        this.calories = calories;
    }


    @Override
    public int caloriesToRuns() {
        return calories / 100;
    }



    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

}
