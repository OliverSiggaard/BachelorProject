package com.digitalmicrofluidicbiochips.bachelorProject.model.actions;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class EndAction extends ActionBase {

        public EndAction(
                String id
        ) {
            super(id);
        }

    @Override
    public Set<Droplet> affectedDroplets() {
        return new HashSet<>();
    }

    @Override
    public void beforeExecution() {

    }

    @Override
    public void execute() {

    }

    @Override
    public void afterExecution() {

    }
}
