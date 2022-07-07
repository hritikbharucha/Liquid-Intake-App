package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ViewDailyIntake extends Application {

    private Canvas gameCanvas;
    private Scene gameScene;
    private Group gameGroup;

    Stage window;
    Button button;
    int totalCalories;
    int totalRuns;
    ListView<String> listView;
    Beverage[] beverages;
    int beverageNum;

    public static void main(String[] args)
    {

        Application.launch();
    }

    //start stage and show window of the logs
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Daily Intake Calculator");
        button = new Button();
        button.setText("Add");
        beverages = new Beverage[10];
        beverageNum = 0;

        listView = new ListView<>();

        Label runs = new Label();

        HBox totalLayout = new HBox(5);
        Label totalLabel = new Label("Total");
        Label totalNumber = new Label();

        ChoiceBox<String> totalUnitsDropdown = new ChoiceBox<>();
        totalUnitsDropdown.getItems().addAll("OZ", "ML");
        totalUnitsDropdown.setValue("OZ");
        totalUnitsDropdown.setOnAction(e -> {
            String units = totalUnitsDropdown.getValue();
            String total = setPreferredTotal(beverages, units);
            totalNumber.setText(total);
        });

        totalLayout.getChildren().addAll(totalLabel, totalUnitsDropdown, totalNumber);

        button.setOnAction(e -> {
            Beverage beverage = AddNewEntry.display();
            if (beverage != null) {
                beverages[beverageNum] = beverage;
                String beverageString = beverage.toString();

                listView.getItems().add(beverageString);
                beverageNum += 1;

                String units = totalUnitsDropdown.getValue();
                String total = setPreferredTotal(beverages, units);
                totalNumber.setText(total);

                if (beverage instanceof Soda) {
                    totalCalories += ((Soda) beverage).getCalories();
                    totalRuns += ((Soda) beverage).caloriesToRuns();
                    runs.setText("You need to run " + totalRuns + " times to burn " + totalCalories + " calories.");
                }
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10,10,10,10));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(listView, button, totalLayout, runs);

        Scene scene = new Scene(layout, 300, 300);
        window.setScene(scene);
        window.show();

    }

    //Convert all liquid amounts to one preferred unit to total all liquids consumed
    public String setPreferredTotal(Beverage[] drinks, String unit) {
        double total = 0;
        System.out.println(unit);
        for (Beverage drink : drinks) {
            if (drink != null) {
                total += drink.convertToPreferred(unit);
            }
        }
       return String.valueOf(total) + " " + unit;
    }

}
