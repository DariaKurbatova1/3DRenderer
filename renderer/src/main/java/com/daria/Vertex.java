package com.daria;

public class Vertex {
    //movement in left-right direction
    double x;
    //movement in up-down direction
    double y;
    //depth(movement towards/away user)
    double z;

    Vertex(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public double getNormalLenght(){
        double normalLength = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (normalLength != 0) {
            this.x /= normalLength;
            this.y /= normalLength;
            this.z /= normalLength;
        }
        return normalLength;
    }
}
