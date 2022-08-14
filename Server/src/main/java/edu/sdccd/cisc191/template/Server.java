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
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());

        ArrayList<Beverage> inputLine;
        LocalDate date;
        inputLine = (ArrayList<Beverage>)in.readObject();
        System.out.println(inputLine + "THIS123");
        date = (LocalDate) in.readObject();
        System.out.println(date + "THIS159");

        Node bst = arrayToBST(null, inputLine, 0, inputLine.size()-1);
        Node filteredBst = searchBST(bst, date);
        out.writeObject(filteredBst);
//        while ((inputLine = in.readLine()) != null) {
//            System.out.println(inputLine + "WEEE");
//            String request = inputLine;
//            String response = inputLine + "abc";
//            out.println(response);
//        }
    }
//
public void stop() throws IOException {
    in.close();
    out.close();
    clientSocket.close();
    serverSocket.close();
}

    Node root;









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
        Server server = new Server();
        try {
            server.start(4444);
            SpringApplication.run(Server.class, args);
//            server.stop();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
} //end class Server

