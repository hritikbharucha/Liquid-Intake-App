package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.control.Alert.AlertType;
import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;
import java.net.*;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Client extends Application {

    private Canvas gameCanvas;
    private Scene gameScene;
    private Group gameGroup;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    Stage window;
    Button addButton;
    Button searchButton;
    int totalCalories;
    int totalRuns;
    ListView<String> listView;
    ArrayList<Beverage> beverages;
    int beverageNum;

    public static void main(String[] args) {
        Client client = new Client();

        try {
           Application.launch();

//            client.stopConnection();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
//
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public Node bstRequest(ArrayList<Beverage> sortedBevs, LocalDate date) throws Exception {
        out.writeObject(sortedBevs);
        out.writeObject(date);
        return (Node)in.readObject();

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

        Button saveFileButton = new Button("Save File");

        totalLayout.getChildren().addAll(totalLabel, totalUnitsDropdown, totalNumber);
        HBox bottomLayout = new HBox(50);
        bottomLayout.getChildren().addAll(totalLayout, saveFileButton);
        bottomLayout.setPadding(new Insets(20, 0, 10, 0));
        bottomLayout.setAlignment(Pos.BOTTOM_CENTER);
        Alert fileError = new Alert(AlertType.WARNING);
        fileError.setContentText("Error saving entries file.");

        Alert fileSuccess = new Alert(AlertType.INFORMATION);
        fileSuccess.setContentText("Entries File saved in DailyIntakeEntries.txt.");

        boolean error = true;
        saveFileButton.setOnAction(e -> {
            try {
                runScannerStuff();
                fileSuccess.show();
            } catch (IOException err) {
                fileError.show();
            }

        });



        addButton.setOnAction(e -> {
            Beverage beverage = Client.addNewEntry();
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
                searchNewEntry(beverages);
            }
        });



        HBox buttonsLayout = new HBox(50);
        buttonsLayout.getChildren().addAll(searchButton, addButton);
        buttonsLayout.setPadding(new Insets(20, 0, 10, 0));
        buttonsLayout.setAlignment(Pos.BOTTOM_CENTER);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10,10,10,10));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(listView, buttonsLayout, bottomLayout, runs);

        Scene scene = new Scene(layout, 300, 300);
        window.setScene(scene);
        window.show();

    }

    //display new window for adding new entries for intake log
    public void searchNewEntry(ArrayList<Beverage> beverages) {
        Stage window = new Stage();
        ListView<String> listView = new ListView<>();
        Collections.sort(beverages);

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Search Entries For Date");
        window.setMinWidth(250);

        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());

        Button addButton = new Button("Search");
        addButton.setOnAction(e -> {
            System.out.println("Searching Entries");
            listView.getItems().clear();

            searchBeverages(listView, beverages, datePicker.getValue());
        });

        Button cancelButton = new Button("Done");
        cancelButton.setOnAction(e -> {
            System.out.println("CANCELING");
            window.close();
        });

        HBox buttonsLayout = new HBox(50);
        buttonsLayout.getChildren().addAll(cancelButton, addButton);
        buttonsLayout.setPadding(new Insets(20, 0, 10, 0));
        buttonsLayout.setAlignment(Pos.BOTTOM_CENTER);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 20, 10));
        layout.getChildren().addAll(listView, datePicker, buttonsLayout);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public void runScannerStuff() throws IOException {
        new PrintWriter("DailyIntakeEntries.txt").close();
        String entries = "";
        for(int i = 0; i < beverages.size(); i++) {

            entries += beverages.get(i).toString();
            if (i != beverages.size()-1) {
                entries += "\n";
            }
        }

        byte entriesBytes[] = entries.getBytes();
        Path p = Paths.get("./DailyIntakeEntries.txt");

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(entriesBytes, 0, entriesBytes.length);
        }
    }


    static Beverage beverage;

    //display new window for adding new entries for intake log
    public static Beverage addNewEntry() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create New Entry");
        window.setMinWidth(250);

        ChoiceBox<String> beverageDropdown = new ChoiceBox<>();
        beverageDropdown.getItems().addAll("Water", "Soda", "Custom");
        beverageDropdown.setValue("Water");

        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();
        nameTextField.setText("Drink");
        HBox nameLayout = new HBox();
        nameLayout.getChildren().addAll(nameLabel, nameTextField);
        nameLayout.setSpacing(10);
        nameLayout.setAlignment(Pos.CENTER);
        nameLayout.setVisible(false);

        Label calorieLabel = new Label("Calories:");
        TextField calorieTextField = new TextField();
        calorieTextField.setText("100");
        HBox calorieLayout = new HBox();
        calorieLayout.getChildren().addAll(calorieLabel, calorieTextField);
        calorieLayout.setSpacing(10);
        calorieLayout.setAlignment(Pos.CENTER);
        calorieLayout.setVisible(false);

        beverageDropdown.setOnAction(e -> {
            if (beverageDropdown.getValue() == "Custom") {
                nameLayout.setVisible(true);
                calorieLayout.setVisible(true);
            } else if (beverageDropdown.getValue() == "Soda") {
                nameLayout.setVisible(false);
                calorieLayout.setVisible(true);
            } else {
                nameLayout.setVisible(false);
                calorieLayout.setVisible(false);
            }
        });

        ChoiceBox<String> unitsDropdown = new ChoiceBox<>();
        unitsDropdown.getItems().addAll("OZ", "ML");
        unitsDropdown.setValue("OZ");

        Label amountLabel = new Label("Amount:");
        TextField amountTextField = new TextField ();
        amountTextField.setText("10");
        HBox amountLayout = new HBox();
        amountLayout.getChildren().addAll(amountLabel, amountTextField);
        amountLayout.setSpacing(10);
        amountLayout.setAlignment(Pos.CENTER);

        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            System.out.println("ADDING ENTRY");
            if (beverageDropdown.getValue() == "Water") {
                int amount = Integer.valueOf(amountTextField.getText());
                String units = unitsDropdown.getValue();
                LocalDate date = datePicker.getValue();
                beverage = new Water(amount, units, date);
            } else if (beverageDropdown.getValue() == "Soda") {
                int amount = Integer.valueOf(amountTextField.getText());
                String units = unitsDropdown.getValue();
                int calories = Integer.valueOf(calorieTextField.getText());
                LocalDate date = datePicker.getValue();
                beverage = new Soda(amount, units, calories, date);
            } else {
                int amount = Integer.valueOf(amountTextField.getText());
                String units = unitsDropdown.getValue();
                int calories = Integer.valueOf(calorieTextField.getText());
                String name = nameTextField.getText();
                LocalDate date = datePicker.getValue();
                beverage = new Custom(amount, units, name, calories, date);
            }
            window.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            System.out.println("CANCELING");
            beverage = null;
            window.close();
        });

        HBox buttonsLayout = new HBox(50);
        buttonsLayout.getChildren().addAll(cancelButton, addButton);
        buttonsLayout.setPadding(new Insets(20, 0, 10, 0));
        buttonsLayout.setAlignment(Pos.BOTTOM_CENTER);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 20, 10));
        layout.getChildren().addAll(beverageDropdown, amountLayout, unitsDropdown, datePicker, calorieLayout, nameLayout, buttonsLayout);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return beverage;
    }

    public void searchBeverages(ListView<String> list, ArrayList<Beverage> sortedBevs, LocalDate date) {
        Node filteredBst = null;

        try {
            startConnection("127.0.0.1", 4444);
            filteredBst = bstRequest(sortedBevs, date);
            stopConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setContentText("No beverage found for selected date.");

        if (filteredBst == null) {
            info.show();
        } else {
            String beverageString = filteredBst.data.toString();
            list.getItems().add(beverageString);
        }
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

//
//    static class Node {
//        Beverage data;
//        Node left, right;
//        Node(Beverage data) {
//            this.data = data;
//            this.left = null;
//            this.right = null;
//        }
//    }

}
