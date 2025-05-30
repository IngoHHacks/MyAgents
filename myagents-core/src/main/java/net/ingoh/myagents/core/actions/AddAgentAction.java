package net.ingoh.myagents.core.actions;

public final class AddAgentAction extends ScheduledEnvAction {
    public String agentType;
    public Number x;
    public Number y;

    public AddAgentAction(String agentType, Number x, Number y) {
        this.agentType = agentType;
        this.x = x;
        this.y = y;
    }
}
