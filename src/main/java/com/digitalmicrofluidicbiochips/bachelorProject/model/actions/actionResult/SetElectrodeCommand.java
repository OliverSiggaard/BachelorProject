package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;

public class SetElectrodeCommand implements IDmfCommand {

    protected final Electrode electrode;

    public SetElectrodeCommand(Electrode electrode) {
        this.electrode = electrode;
    }

    @Override
    public String getDmfCommand() {
        return DmfPlatformUtils.getSetElectrodeCommand(electrode.getID());
    }
}
