package net.ingoh.myagents.core.basetypes;

import net.ingoh.myagents.core.Vector;
import net.ingoh.myagents.core.communication.MessageMeta;
import net.ingoh.myagents.lang.execution.Interpreter;

import java.awt.*;
import java.util.List;

public class Agent extends MyAgentsClassBase {
    public Environment env;
    public int id;
    public double x;
    public double y;
    public Color color = Color.RED;
    public double size = 6.0;
    public String text = "";

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

    // MOVEMENT METHODS

    public boolean move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        if (env.wrap) {
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
        } else {
            this.x = Math.max(0, Math.min(env.width, this.x));
            this.y = Math.max(0, Math.min(env.height, this.y));
        }
        return true;
    }

    public boolean moveTowards(double targetX, double targetY, double speed) {
        Vector direction = distanceTo(targetX, targetY).normalize();
        this.x += direction.x * speed;
        this.y += direction.y * speed;
        return true;
    }

    // DISTANCE METHODS

    public Vector distanceTo(Agent other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        if (env.wrap)
        {
            return wrap(dx, dy);
        }
        return new Vector(dx, dy);
    }

    public Vector distanceTo(double x, double y) {
        double dx = x - this.x;
        double dy = y - this.y;
        if (env.wrap) {
            return wrap(dx, dy);
        }
        return new Vector(dx, dy);
    }

    private Vector wrap(double dx, double dy) {
        if (dx > env.width / 2.0) dx -= env.width;
        else if (dx < -env.width / 2.0) dx += env.width;
        if (dy > env.height / 2.0) dy -= env.height;
        else if (dy < -env.height / 2.0) dy += env.height;
        return new Vector(dx, dy);
    }

    // COMMUNICATION METHODS
    public boolean tell(String message, Agent who) {
        return sendMessage(who, message);
    }

    public boolean tell(String message, Agent... who) {
        return sendMessage(who, message);
    }

    public boolean tellAll(String message) {
        return sendMessageToAll(message);
    }

    public boolean sendMessage(Agent who, String message) {
        return env.sendMessage(this, List.of(who), message);
    }

    public boolean sendMessage(Agent[] who, String message) {
        return env.sendMessage(this, List.of(who), message);
    }

    public boolean sendMessageToAll(String message) {
        return env.sendMessageToAll(this, message);
    }

    public boolean receiveMessage(String message, MessageMeta meta) {
        return hear(message, meta);
    }

    public boolean hear(String message, MessageMeta meta) {
        // Default implementation does nothing
        return true;
    }

    // RENDERING METHODS

    public void render(Graphics g, double xOffset, double yOffset, double scale) {
        g.setColor(color);
        g.fillOval((int) (xOffset + (x - 0.5* size) * scale),
                   (int) (yOffset + (y - 0.5* size) * scale),
                   (int) (size * scale),
                   (int) (size * scale));
        if (!text.isEmpty()) {
            g.setColor(Color.BLACK);
            FontMetrics metrics = g.getFontMetrics();
            int textWidth = metrics.stringWidth(text);
            int textHeight = metrics.getHeight();
            g.drawString(text, (int) (xOffset + x * scale - textWidth / 2.0),
                         (int) (yOffset + y * scale + textHeight / 2.0));
        }
    }
}
