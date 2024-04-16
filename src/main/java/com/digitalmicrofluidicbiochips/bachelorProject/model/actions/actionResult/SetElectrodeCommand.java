package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfCommandUtils;

public class SetElectrodeCommand implements IDmfCommand {

    protected final Electrode electrode;

    public SetElectrodeCommand(Electrode electrode) {
        this.electrode = electrode;
    }

    @Override
    public String getDmfCommand() {
        return DmfCommandUtils.getSetElectrodeCommand(electrode.getID());
    }

    @Override
    public void updateModelWithCommand() {
        electrode.setStatus(1);
    }
}