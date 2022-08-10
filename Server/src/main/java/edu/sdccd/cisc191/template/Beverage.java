package edu.sdccd.cisc191.template;
import java.time.LocalDate;

public class Beverage implements Liquid{
    private double amount;
    private String unit;
    private String type;

    private LocalDate date;

    //Constructor for Bevarage class with amount of beverage and in which units
    public Beverage(double amount, String unit, String type, LocalDate date) {
        this.amount = amount;
        this.unit = unit;
        this.type = type;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    //get the amount of the beverage
    public double getAmount() {
        return amount;
    }

    //set the amount of the beverage
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //get the unit of the beverage
    public String getUnit() {
        return unit;
    }

    //set the unit of the beverage
    public void setUnit(String unit) {
        this.unit = unit;
    }

    //convert units from ml to oz or from oz to ml
    public double convertToPreferred(String pUnit) {
        switch (pUnit) {

            case ("ML"):
                if (unit.equals("ML"))
                    return amount;
                else
                    return ozToMl();
            case ("OZ"):
                if (unit.equals("OZ"))
                    return amount;
                else
                    return mlToOz();
            default:
                break;


        }
        return amount;
    }

    @Override
    public String toString() {
        return (amount + " " + unit + " of " + type + " recorded on " + date);
    }

    @Override
    public double ozToMl() {
        return getAmount() * 29.5735;
    }

    @Override
    public double mlToOz() {
        return getAmount() * 0.033814;
    }
}
