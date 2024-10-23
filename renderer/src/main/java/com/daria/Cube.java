package com.daria;

import java.util.ArrayList;
import java.util.List;

public class Cube {
    List<Square> squares = new ArrayList<>();

    Cube(List<Square> squares) {
        // if(triangles.size() != 4){
        //     throw new Exception("Triangle list passed to Tetrahedron must be of size 4.");
        // }
        this.squares = squares; 
    }
}