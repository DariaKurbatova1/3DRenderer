package com.daria;
import java.awt.Color;

public class Square {
    Vertex v1;
    Vertex v2;
    Vertex v3;
    Vertex v4;
    Color color;
    Square(Vertex v1, Vertex v2, Vertex v3, Vertex v4, Color color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.color = color;
    }
    public double area() {
        double sideLength = Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2));
        return sideLength * sideLength; 
    }
}
