package edu.sdccd.cisc191.template;

public class Beverage implements Liquid{
    private double amount;
    private String unit;

    public Beverage(double amount, String unit) {
        this.amount = amount;
        this.unit = unit;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

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
    public double ozToMl() {
        return getAmount() * 29.5735;
    }

    @Override
    public double mlToOz() {
        return getAmount() * 0.033814;
    }
}
