package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ViewDailyIntake extends Application {

    private Canvas gameCanvas;
    private Scene gameScene;
    private Group gameGroup;

    Stage window;
    Button addButton;
    Button searchButton;
    int totalCalories;
    int totalRuns;
    ListView<String> listView;
    ArrayList<Beverage> beverages;
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
        addButton = new Button();
        addButton.setText("Add");
        searchButton = new Button();
        searchButton.setText("Search Date");
        beverages = new ArrayList<Beverage>();
        beverageNum = 0;

        listView = new ListView<>();

        Label runs = new Label();

        HBox totalLayout = new HBox(5);
        Label totalLabel = new Label("Total: ");
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

        addButton.setOnAction(e -> {
            Beverage beverage = AddNewEntry.display();
            if (beverage != null) {
                beverages.add(beverageNum,beverage);
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

        Alert warning = new Alert(AlertType.WARNING);
        warning.setContentText("Please add entries to search.");

        searchButton.setOnAction(e -> {
            if (beverages.isEmpty()) {
                warning.show();
            } else {
                SearchNewEntry.display(beverages);
            }
        });

        HBox buttonsLayout = new HBox(50);
        buttonsLayout.getChildren().addAll(searchButton, addButton);
        buttonsLayout.setPadding(new Insets(20, 0, 10, 0));
        buttonsLayout.setAlignment(Pos.BOTTOM_CENTER);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10,10,10,10));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(listView, buttonsLayout, totalLayout, runs);

        Scene scene = new Scene(layout, 300, 300);
        window.setScene(scene);
        window.show();

    }

    //Convert all liquid amounts to one preferred unit to total all liquids consumed
    public String setPreferredTotal(ArrayList<Beverage> drinks, String unit) {
        AtomicReference<Double> total = new AtomicReference<>((double) 0);
        ArrayList<Thread> threads = new ArrayList<Thread>();
        System.out.println(unit);
        for (Beverage drink : drinks) {
            if (drink != null) {
                threads.add(new Thread(() -> {
                    total.updateAndGet(v -> new Double((double) (v + drink.convertToPreferred(unit))));
                }));
                threads.get(threads.size()-1).start();

            }
        }

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch(InterruptedException e) {
                System.out.println(e);
            };});


       return String.valueOf(total.get()) + " " + unit;
    }

}
