package edu.sdccd.cisc191.template;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AddNewEntry {

    static Beverage beverage;

    //display new window for adding new entries for intake log
    public static Beverage display() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create New Entry");
        window.setMinWidth(250);



        ChoiceBox<String> beverageDropdown = new ChoiceBox<>();
        beverageDropdown.getItems().addAll("Water", "Soda");
        beverageDropdown.setValue("Water");

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

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            System.out.println("ADDING ENTRY");
            if (beverageDropdown.getValue() == "Water") {
                int amount = Integer.valueOf(amountTextField.getText());
                String units = unitsDropdown.getValue();
                beverage = new Water(amount, units);
            } else {
                int amount = Integer.valueOf(amountTextField.getText());
                String units = unitsDropdown.getValue();
                beverage = new Soda(amount, units, 100);
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
        layout.getChildren().addAll(beverageDropdown, amountLayout, unitsDropdown, buttonsLayout);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return beverage;
    }

}
