package net.ingoh.myagents.core.basetypes;

import net.ingoh.myagents.lang.execution.Interpreter;

import java.awt.*;

public class Agent extends MyAgentsClassBase {
    public Environment env;
    public int id;
    public double x;
    public double y;

    public Agent(Interpreter interpreter, Environment env, int id, Number x, Number y) {
        super(interpreter);
        this.env = env;
        this.id = id;
        this.x = x.doubleValue();
        this.y = y.doubleValue();
    }
    
    public boolean init() {
        return true;
    }

    public boolean tick(double time) {
        return true;
    }

    public boolean move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        if (this.x < 0) {
            this.x = env.width + this.x;
        }
        if (this.x >= env.width) {
            this.x = this.x - env.width;
        }
        if (this.y < 0) {
            this.y = env.height + this.y;
        }
        if (this.y >= env.height) {
            this.y = this.y - env.height;
        }
        return true;
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int) x - 3, (int) y - 3, 6, 6);
    }
}
