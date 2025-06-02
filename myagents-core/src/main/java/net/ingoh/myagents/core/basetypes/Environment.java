package net.ingoh.myagents.core.basetypes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import net.ingoh.myagents.core.actions.AddAgentAction;
import net.ingoh.myagents.core.actions.RemoveAgentAction;
import net.ingoh.myagents.core.actions.ScheduledEnvAction;
import net.ingoh.myagents.core.communication.MessageMeta;
import net.ingoh.myagents.lang.execution.ExecutionSource;
import net.ingoh.myagents.lang.execution.Interpreter;
import net.ingoh.myagents.lang.il.TypeIdentifier;
import net.ingoh.myagents.utils.ThreadSafeList;

public class Environment extends MyAgentsClassBase {
    public double tickRate;
    public ThreadSafeList<String> agentTypes;
    public ThreadSafeList<Agent> agents;
    public Hashtable<String, List<Agent>> agentTypeMap;
    public int width;
    public int height;
    public double __speed = 1.0;
    public boolean wrap = false;

    public Color color = Color.BLACK;

    private boolean __ticking = false;
    private List<ScheduledEnvAction> scheduledActions = new LinkedList<>();

    public Environment(Interpreter interpreter, String type) {
        super(interpreter, type);
        this.agentTypes = new ThreadSafeList<>();
        this.agents = new ThreadSafeList<>();
        this.agentTypeMap = new Hashtable<>();
    }

    public Environment(Interpreter interpreter) {
        super(interpreter);
        this.agentTypes = new ThreadSafeList<>();
        this.agents = new ThreadSafeList<>();
        this.agentTypeMap = new Hashtable<>();
    }

    public int run() {
        init();
        initgfx();
        Thread thread = new Thread(() -> {
            long time = System.currentTimeMillis();
            long eTime = time;
            if (tickRate <= 0) {
                tickRate = 100; // Default to 100 ticks per second
            }
            long sleepTime = (long) (1000 / tickRate);
            long excess = 0;
            while (true) {
                try {
                    Thread.sleep(Math.max(sleepTime - (int)(excess * 0.5), 0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                __ticking = true;
                var dt = System.currentTimeMillis() - time;
                time += dt;
                eTime += sleepTime;
                excess = time - eTime;
                var tempExecutionSource = interpreter.getExecutionSource();
                for (int i = 0; i < agents.size(); i++) {
                    Agent agent = agents.get(i);
                    interpreter.setExecutionSource(agent);
                    if (!agent.tick(__speed * dt / 1000.0)) {
                        destroy(agent);
                    }
                }
                interpreter.setExecutionSource(tempExecutionSource);
                __ticking = false;
                if (!scheduledActions.isEmpty()) {
                    for (ScheduledEnvAction action : scheduledActions) {
                        if (action instanceof AddAgentAction addAction) {
                            create(addAction.agentType, addAction.x, addAction.y);
                        } else if (action instanceof RemoveAgentAction removeAction) {
                            destroy(removeAction.agent);
                        }
                    }
                    scheduledActions.clear();
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
        frame.setSize(width + 300, height);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        Container contentPane = frame.getContentPane();
        JPanel centerer = new JPanel(new BorderLayout());
        contentPane.add(centerer, BorderLayout.CENTER);
        centerer.setPreferredSize(new Dimension(width, height));
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                var sx = getWidth() / (double) width;
                var sy = getHeight() / (double) height;
                var scale = Math.min(sx, sy);
                var dx = 0;
                var dy = 0;
                if (sx < sy) {
                    dy = (int) ((getHeight() - height * scale) / 2);
                } else {
                    dx = (int) ((getWidth() - width * scale) / 2);
                }
                for (int i = 0; i < agents.size(); i++) {
                    Agent agent = agents.get(i);
                    agent.render(g, dx, dy, scale);
                }
            }
        };
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(color);
        centerer.add(panel, BorderLayout.CENTER);
        var sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(300, height));
        sidePanel.setBackground(Color.LIGHT_GRAY);
        sidePanel.setLayout(new GridLayout(0, 1));
        JLabel titleLabel = new JLabel("<html>"  + this.__type + " Environment</html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        sidePanel.add(titleLabel);
        var agentCountLabel = new JLabel("Agents: " + agents.size(), SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                setText("Agents: " + agents.size());
                super.paintComponent(g);
            }
        };
        agentCountLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        agentCountLabel.setForeground(Color.BLACK);
        sidePanel.add(agentCountLabel);
        var subCounts = new JPanel();
        subCounts.setLayout(new GridLayout(agentTypeMap.size(), 1));
        for (String agentType : agentTypeMap.keySet()) {
            JLabel label = new JLabel("<html>" + agentType + ": " + agentTypeMap.get(agentType).size() + "</html>", SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    if (!agentTypeMap.containsKey(agentType)) {
                        setText("<html>" + agentType + ": 0</html>");
                    } else {
                        setText("<html>" + agentType + ": " + agentTypeMap.get(agentType).size() + "</html>");
                    }
                    super.paintComponent(g);
                }
            };
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            label.setForeground(Color.BLACK);
            subCounts.add(label);
        }
        sidePanel.add(subCounts);
        var simSpeedPanel = new JPanel();
        simSpeedPanel.setLayout(new GridLayout(2, 1));
        JLabel speedLabel = new JLabel("Simulation Speed: " + __speed + "×", SwingConstants.CENTER);
        speedLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        speedLabel.setForeground(Color.BLACK);
        simSpeedPanel.add(speedLabel);
        JSlider simSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 11, 4);
        simSpeedSlider.setMinorTickSpacing(1);
        simSpeedSlider.setPaintTicks(true);
        simSpeedSlider.setValue((int) getSpeedTo(__speed));
        simSpeedSlider.addChangeListener(e -> {
            int value = simSpeedSlider.getValue();
            __speed = getSpeedFrom(value);
            speedLabel.setText("Simulation Speed: " + __speed + "×");
        });
        simSpeedPanel.add(simSpeedSlider);
        sidePanel.add(simSpeedPanel);
        contentPane.add(sidePanel, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);
        Timer timer = new Timer(1000 / 60, e -> {
            panel.repaint();
            sidePanel.repaint();
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
        if (__ticking) {
            scheduledActions.add(new AddAgentAction(agent, x, y));
            return true;
        }
        try {
            if (!agentTypeMap.containsKey(agent)) {
                agentTypeMap.put(agent, new LinkedList<>());
            }
            List<Agent> list = agentTypeMap.get(agent);
            int id = list.size();
            Agent instance = (Agent) interpreter.resolveConstructor(new TypeIdentifier(agent), 6)
                    .invoke(interpreter, ExecutionSource.STATIC, agent, this, id, x, y);
            agents.add(instance);
            list.add(instance);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean destroy(Agent agent) {
        if (__ticking) {
            scheduledActions.add(new RemoveAgentAction(agent));
            return true;
        }
        if (agents.remove(agent)) {
            var agentType = agent.__type;
            List<Agent> list = agentTypeMap.get(agentType);
            if (list != null) {
                list.remove(agent);
                if (list.isEmpty()) {
                    agentTypeMap.remove(agentType);
                }
                for (var listAgent : list) {
                    if (listAgent.id > agent.id) {
                        listAgent.id--;
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

    public Agent nearestAgent(Agent a, String agentType) {
        if (a == null || agentType == null) {
            throw new IllegalArgumentException("Agent and agent type cannot be null");
        }
        List<Agent> agents = getAgents(agentType);
        if (agents.isEmpty()) {
            return null;
        }
        Agent nearest = null;
        double minDistance = Double.MAX_VALUE;
        for (Agent b : agents) {
            if (b == a) continue; // Skip the agent itself
            double dist = a.distanceTo(b).length();
            if (dist < minDistance) {
                minDistance = dist;
                nearest = b;
            }
        }
        return nearest;
    }

    public boolean sendMessage(Agent agent, List<Agent> who, String message) {
        var meta = new MessageMeta(agent, who, message);
        for (Agent recipient : who) {
            if (recipient != null && agents.contains(recipient)) {
                recipient.receiveMessage(message, meta);
            }
        }
        return true;
    }

    public boolean sendMessageToAll(Agent agent, String message) {
        var meta = new MessageMeta(agent, agents.toList(), message);
        for (int i = 0; i < agents.size(); i++) {
            Agent recipient = agents.get(i);
            if (recipient != null && recipient != agent) {
                recipient.receiveMessage(message, meta);
            }
        }
        return true;
    }

    private double getSpeedFrom(int value) {
        switch (value) {
            case 0: return 0.01;
            case 1: return 0.1;
            case 2: return 0.25;
            case 3: return 0.5;
            case 4: return 1.0;
            case 5: return 2.0;
            case 6: return 4.0;
            case 7: return 8.0;
            case 8: return 16.0;
            case 9: return 32.0;
            case 10: return 100.0;
            case 11: return 1000.0;
            default: return 0;
        }
    }

    private double getSpeedTo(double value) {
        if (value <= 0.01) return 0;
        if (value <= 0.1) return 1;
        if (value <= 0.25) return 2;
        if (value <= 0.5) return 3;
        if (value <= 1.0) return 4;
        if (value <= 2.0) return 5;
        if (value <= 4.0) return 6;
        if (value <= 8.0) return 7;
        if (value <= 16.0) return 8;
        if (value <= 32.0) return 9;
        if (value <= 100.0) return 10;
        return 11;
    }
}
