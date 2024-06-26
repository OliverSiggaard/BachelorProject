package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;

public class ClearElectrodeCommand implements IDmfCommand {

    private final Electrode electrode;

    public ClearElectrodeCommand(Electrode electrode) {
        this.electrode = electrode;
    }

    @Override
    public String getBioAssemblyInstruction() {
        return DmfPlatformUtils.getClearElectrodeCommand(electrode.getID());
    }
}
