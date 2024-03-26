package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class IfAction extends ActionBase {

    private final String condition;

    @Setter
    private ActionBase trueNextAction = null;
    @Setter
    private ActionBase falseNextAction = null;

    public IfAction(
            String id,
            String condition
    ) {
        super(id);
        this.condition = condition;
    }

    @Override
    public Set<Droplet> affectedDroplets() {
        return null;
    }

    @Override
    public void beforeExecution() {

    }

    @Override
    public void executeTick(ProgramConfiguration programConfiguration) {

    }

    @Override
    public void afterExecution() {

    }
}
