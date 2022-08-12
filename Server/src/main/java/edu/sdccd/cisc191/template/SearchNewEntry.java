package edu.sdccd.cisc191.template;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import org.omg.CORBA.NO_IMPLEMENT;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class SearchNewEntry {

    Node root;

    static class Node {
        Beverage data;
        Node left, right;
        Node(Beverage data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    //display new window for adding new entries for intake log
    public static void display(ArrayList<Beverage> beverages) {
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

    public static void searchBeverages(ListView<String> list, ArrayList<Beverage> sortedBevs, LocalDate date) {

        Node bst = arrayToBST(null, sortedBevs, 0, sortedBevs.size()-1);

        Node filteredBst = searchBST(bst,date);

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setContentText("No beverage found for selected date.");

        if (filteredBst == null) {
            info.show();
        } else {
            String beverageString = filteredBst.data.toString();
            list.getItems().add(beverageString);
        }
    }

    public static void inOrder(Node root)
    {
        if (root != null) {
            inOrder(root.left);
            System.out.print(root.data + " ");
            inOrder(root.right);
        }
    }

    // Recursive function to search the bst
    public static Node searchBST(Node root, LocalDate needle)
    {
        if (root == null) {
            System.out.println("Needle not found");
            return null;
        }

        int result = needle.compareTo(root.data.getDate());

        if (result == 0) {
            return root;
        } else if (result < 0) {
            return searchBST(root.left, needle);
        } else {
            return searchBST(root.right, needle);
        }
    }

    //recursively add beverages of arraylist sorted by date into a binary search tree
    public static Node arrayToBST(Node root, ArrayList<Beverage> values, int start, int end){
        int mid = (start + end) / 2;
        if (end < 0 || start > end) return root;
        root = insert(root, values.get(mid));
        arrayToBST(root, values, start, mid - 1);
        arrayToBST(root, values, mid + 1, end);
        return root;
    }

    public static Node insert(Node root, Beverage needle) {

        if (root == null) {
            root = new Node(needle);
            return root;
        }

        int result = needle.compareTo(root.data);

        if (result < 0) {
            root.left = insert(root.left, needle);
        } else if (result > 0) {
            root.right = insert(root.right, needle);
        }

        return root;
    }

}
