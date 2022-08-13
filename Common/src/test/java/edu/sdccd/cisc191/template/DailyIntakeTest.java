package edu.sdccd.cisc191.template;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DailyIntakeTest {

    @Test
    void testBeverage() {
        Beverage beverage = new Beverage(25, "OZ", "Soda", LocalDate.now());

        assertEquals(25, beverage.getAmount());
        assertEquals("OZ", beverage.getUnit());
        assertEquals(739.3375, beverage.convertToPreferred("ML"));
    }

    @Test
    void testSoda() {
        Soda beverage = new Soda(500, "ML", 100, LocalDate.now());

        assertEquals(500, beverage.getAmount());
        assertEquals("ML", beverage.getUnit());
        assertEquals(100, beverage.getCalories());
        assertEquals(1, beverage.caloriesToRuns());
        assertEquals(16.907, beverage.convertToPreferred("OZ"));
    }

    @Test
    void testCustom() {
        Custom beverage = new Custom(500, "ML", "Juice", 100, LocalDate.now());

        assertEquals("Juice", beverage.getType());
        assertEquals(500, beverage.getAmount());
        assertEquals("ML", beverage.getUnit());
        assertEquals(100, beverage.getCalories());
        assertEquals(1, beverage.caloriesToRuns());
        assertEquals(16.907, beverage.convertToPreferred("OZ"));
    }

    @Test
    void testWater() {
        Water beverage = new Water(10, "OZ", LocalDate.now());

        assertEquals(10, beverage.getAmount());
        assertEquals("OZ", beverage.getUnit());
        assertEquals("Water", beverage.getType());
    }

    @Test
    void testRecusrionAndBST() {
        ArrayList<Beverage> beverages = new ArrayList<Beverage>();

        Beverage beverage1 = new Beverage(10, "OZ", "Water", LocalDate.now().minusDays(2));
        Beverage beverage2 = new Beverage(10, "OZ", "Water", LocalDate.now().minusDays(1));
        Beverage beverage3 = new Beverage(10, "OZ", "Water", LocalDate.now());

        beverages.add(beverage1);
        beverages.add(beverage2);
        beverages.add(beverage3);

        Collections.sort(beverages);

        SearchNewEntry.Node bst = SearchNewEntry.arrayToBST(null, beverages, 0, beverages.size()-1);

        SearchNewEntry.Node filteredBst = SearchNewEntry.searchBST(bst,LocalDate.now());

        assertEquals(beverage3, filteredBst.data);
    }
}