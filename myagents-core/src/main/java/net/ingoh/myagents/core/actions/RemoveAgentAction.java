package net.ingoh.myagents.core.actions;

import net.ingoh.myagents.core.basetypes.Agent;

public final class RemoveAgentAction extends ScheduledEnvAction {
    public Agent agent;

    public RemoveAgentAction(Agent agent) {
        this.agent = agent;
    }
}
