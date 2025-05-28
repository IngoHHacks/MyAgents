package net.ingoh.myagents.core.basetypes;

import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.il.TypeIdentifier;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Environment extends MyAgentsClassBase {
    public double tickRate;
    public List<String> agentTypes;
    public List<Agent> agents;
    public Hashtable<String, List<Agent>> agentTypeMap;
    public int width;
    public int height;

    public Environment(Interpreter interpreter) {
        super(interpreter);
        this.agentTypes = new LinkedList<>();
        this.agents = new LinkedList<>();
        this.agentTypeMap = new Hashtable<>();
    }

    public int run() {
        init();
        initgfx();
        Thread thread = new Thread(() -> {
            long time = System.currentTimeMillis();
            long eTime = time;
            if (tickRate <= 0) {
                tickRate = 10; // Default to 10 ticks per second
            }
            long sleepTime = (long) (1000 / tickRate);
            long excess = 0;
            while (true) {
                try {
                    Thread.sleep(Math.max(sleepTime - (int)(excess * 0.5), 0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                var dt = System.currentTimeMillis() - time;
                time += dt;
                eTime += sleepTime;
                excess = time - eTime;
                for (Agent agent : agents) {
                    if (!agent.tick(dt / 1000.0)) {
                        destroy(agent);
                    }
                }
            }
        });
        thread.start();
        return 0;
    }

    public boolean initgfx() {
        JFrame frame = new JFrame();
        frame.setTitle("MyAgents Environment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Agent agent : agents) {
                    agent.render(g);
                }
            }
        };
        panel.setPreferredSize(new Dimension(width, height));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        Timer timer = new Timer(1000 / 60, e -> {
            panel.repaint();
        });
        timer.start();
        return true;
    }

    public boolean init() {
        return true;
    }

    public boolean tick(double time) {
        return true;
    }

    public boolean create(String agent, Number x, Number y) {
        try {
            if (!agentTypeMap.containsKey(agent)) {
                agentTypeMap.put(agent, new LinkedList<>());
            }
            List<Agent> list = agentTypeMap.get(agent);
            int id = list.size();
            Agent instance = (Agent) interpreter.resolveConstructor(new TypeIdentifier(agent), 5)
                    .invoke(interpreter, null, this, id, x, y);
            agents.add(instance);
            list.add(instance);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean destroy(Agent agent) {
        if (agents.remove(agent)) {
            Class<? extends Agent> agentType = agent.getClass();
            List<Agent> list = agentTypeMap.get(agentType);
            if (list != null) {
                list.remove(agent);
                if (list.isEmpty()) {
                    agentTypeMap.remove(agentType);
                }
                for (int i = 0; i < agents.size(); i++) {
                    if (agents.get(i).id > agent.id) {
                        agents.get(i).id--;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public Agent getAgent(String agentType, int index) {
        if (agentTypeMap.containsKey(agentType)) {
            List<Agent> list = agentTypeMap.get(agentType);
            if (index >= 0 && index < list.size()) {
                return list.get(index);
            }
        }
        return null;
    }

    public List<Agent> getAgents(String agentType) {
        return agentTypeMap.getOrDefault(agentType, new LinkedList<>());
    }


    public int getAgentCount(String agentType) {
        if (agentTypeMap.containsKey(agentType)) {
            return agentTypeMap.get(agentType).size();
        }
        return 0;
    }

    public double distance(Agent a, Agent b) {
        return distance(a, b, false);
    }

    public double distance(Agent a, Agent b, boolean wrap) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Agents cannot be null");
        }
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        if (wrap)
        {
            if (dx > width / 2.0) dx -= width;
            else if (dx < -width / 2.0) dx += width;
            if (dy > height / 2.0) dy -= height;
            else if (dy < -height / 2.0) dy += height;
        }
        return Math.sqrt(dx * dx + dy * dy);
    }
}
