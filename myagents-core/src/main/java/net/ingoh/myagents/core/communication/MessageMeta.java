package net.ingoh.myagents.core.communication;

import net.ingoh.myagents.core.basetypes.Agent;

import java.util.List;

public class MessageMeta {
    public Agent sender;
    public List<Agent> receivers;
    public String extra;

    public MessageMeta(Agent sender, List<Agent> receivers, String extra) {
        this.sender = sender;
        this.receivers = receivers;
        this.extra = extra;
    }
}
