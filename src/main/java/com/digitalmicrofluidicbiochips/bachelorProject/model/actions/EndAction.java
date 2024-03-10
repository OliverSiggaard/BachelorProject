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

}
