package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
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
    public Set<Droplet> dropletsRequiredForExecution() {
        return new HashSet<>();
    }

    @Override
    public void beforeExecution(ProgramConfiguration programConfiguration) {

    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {
        return new ActionTickResult();
    }

    @Override
    public void afterExecution(ProgramConfiguration programConfiguration) {

    }
}
