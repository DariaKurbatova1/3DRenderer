package com.daria;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ShapeFactory {


    public static Tetrahedron createTetrahedron() {
        List<Triangle> tris = new ArrayList<>();
        tris.add(new Triangle(
                new Vertex(100, 100, 100),
                new Vertex(-100, -100, 100),
                new Vertex(-100, 100, -100),
                Color.WHITE
        ));
        tris.add(new Triangle(
                new Vertex(100, 100, 100),
                new Vertex(-100, -100, 100),
                new Vertex(100, -100, -100),
                Color.RED
        ));
        tris.add(new Triangle(
                new Vertex(-100, 100, -100),
                new Vertex(100, -100, -100),
                new Vertex(100, 100, 100),
                Color.GREEN
        ));
        tris.add(new Triangle(
                new Vertex(-100, 100, -100),
                new Vertex(100, -100, -100),
                new Vertex(-100, -100, 100),
                Color.BLUE
        ));
        return new Tetrahedron(tris);
    }

    public static Cube createCube() {
        List<Square> squares = new ArrayList<>();

        Vertex v1 = new Vertex(-100, -100, -100);
        Vertex v2 = new Vertex(100, -100, -100);
        Vertex v3 = new Vertex(100, 100, -100);
        Vertex v4 = new Vertex(-100, 100, -100);
        Vertex v5 = new Vertex(-100, -100, 100);
        Vertex v6 = new Vertex(100, -100, 100);
        Vertex v7 = new Vertex(100, 100, 100);
        Vertex v8 = new Vertex(-100, 100, 100);

        squares.add(new Square(v1, v2, v3, v4, Color.WHITE));  
        squares.add(new Square(v5, v6, v7, v8, Color.RED));  
        squares.add(new Square(v1, v2, v6, v5, Color.GREEN));  
        squares.add(new Square(v2, v3, v7, v6, Color.BLUE));  
        squares.add(new Square(v3, v4, v8, v7, Color.YELLOW)); 
        squares.add(new Square(v4, v1, v5, v8, Color.MAGENTA)); 

        return new Cube(squares);
    }
}
