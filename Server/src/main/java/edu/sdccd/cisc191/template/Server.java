package edu.sdccd.cisc191.template;

import java.net.*;
import java.io.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This program is a server that takes connection requests on
 * the port specified by the constant LISTENING_PORT.  When a
 * connection is opened, the program sends the current time to
 * the connected socket.  The program will continue to receive
 * and process connections until it is killed (by a CONTROL-C,
 * for example).  Note that this server processes each connection
 * as it is received, rather than creating a separate thread
 * to process the connection.
 */
@SpringBootApplication
public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

//    public void start(int port) throws Exception {
//        serverSocket = new ServerSocket(port);
//        clientSocket = serverSocket.accept();
//        out = new PrintWriter(clientSocket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//        String jsonRequest;
//
//        while ((jsonRequest = in.readLine()) != null) {
//            HydrationRequest request =  HydrationRequest.toRequest(jsonRequest);
//            HydrationResponse response = new  HydrationResponse(request, 5000,25000,5,4,new String[]{"AC", "LEATHER"});
//            out.println( HydrationResponse.toJSON(response));
//        }
//    }
//
//    public void stop() throws IOException {
//        in.close();
//        out.close();
//        clientSocket.close();
//        serverSocket.close();
//    }

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

    //display new window for adding new entries for intake log
    public static void searchNewEntry(ArrayList<Beverage> beverages) {
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


    public static void main(String[] args) {
//        Server server = new Server();
//        try {
//            server.start(4444);
//            server.stop();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
        SpringApplication.run(Server.class, args);
    }
} //end class Server
