package net.ingoh.myagents.core;

public class Vector {
    public double x;
    public double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector normalize() {
        double len = length();
        if (len == 0) return new Vector(0, 0);
        return new Vector(x / len, y / len);
    }
}
