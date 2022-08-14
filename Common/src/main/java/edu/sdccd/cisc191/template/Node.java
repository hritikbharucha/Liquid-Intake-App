package edu.sdccd.cisc191.template;
import java.io.Serializable;

public class Node implements Serializable {
    Beverage data;
    Node left, right;
    Node(Beverage data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}
