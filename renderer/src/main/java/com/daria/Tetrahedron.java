package com.daria;
import java.util.ArrayList;
import java.util.List;

import com.daria.Triangle;

public class Tetrahedron {
    List<Triangle> triangles = new ArrayList<>();
    Tetrahedron(List<Triangle> triangles){
        // if(triangles.size() != 4){
        //     throw new Exception("Triangle list passed to Tetrahedron must be of size 4.");
        // }
        this.triangles = triangles; 
        
    }
}
