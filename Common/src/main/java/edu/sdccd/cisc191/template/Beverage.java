package edu.sdccd.cisc191.template;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.persistence.*;

@Service
@Entity(name="Beverage")
public class Beverage implements Liquid,Comparable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(
            name="id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "amount",
            nullable = false,
            updatable = true
    )
    private double amount;

    @Column(
            name = "unit",
            nullable = false,
            updatable = true
    )
    private String unit;

    @Column(
            name = "type",
            nullable = false,
            columnDefinition = "TEXT",
            updatable = true
    )
    private String type;

    @Column(
            name = "date",
            nullable = false,
            updatable = true
    )
    private LocalDate date;

    //Constructor for Bevarage class with amount of beverage and in which units
    public Beverage(double amount, String unit, String type, LocalDate date) {
        this.amount = amount;
        this.unit = unit;
        this.type = type;
        this.date = date;
    }

    public Beverage() {

    }

    //return date for beverage
    public LocalDate getDate() {
        return date;
    }

    //set date for beverage
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

    //get type of beverage
    public String getType() {
        return type;
    }

    // set type of beverage
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

    //to string to format beverage string
    @Override
    public String toString() {
        return (amount + " " + unit + " of " + type + " recorded on " + date);
    }

    //convert oz to ml
    @Override
    public double ozToMl() {
        return getAmount() * 29.5735;
    }

    //convert ml to oz
    @Override
    public double mlToOz() {
        return getAmount() * 0.033814;
    }

    //compare Beverage objects by date
    @Override
    public int compareTo(Object o) {
        LocalDate compareDate = ((Beverage)o).getDate();
        return this.date.compareTo(compareDate);
    }

    //search beverages and add to listview
    public static void searchBeverages(ListView<String> list, Node filteredBst) throws Exception {

            String beverageString = filteredBst.data.toString();
            list.getItems().add(beverageString);

    }


}
