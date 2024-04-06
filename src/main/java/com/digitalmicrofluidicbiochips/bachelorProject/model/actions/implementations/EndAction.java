package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult.ActionTickResult;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class EndAction extends ActionBase {

    public EndAction(String id) {
        super(id);
    }

    @Override
    public Set<Droplet> dropletsRequiredForExecution() {
        return new HashSet<>();
    }

    @Override
    public void beforeExecution() {

    }

    @Override
    public ActionTickResult executeTick(ProgramConfiguration programConfiguration) {
        return new ActionTickResult();
    }

    @Override
    public void afterExecution() {

    }

}
