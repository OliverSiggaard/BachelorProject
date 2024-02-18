package com.digitalmicrofluidicbiochips.bachelorProject.mappers.json.dmf_platform;

import com.digitalmicrofluidicbiochips.bachelorProject.mappers.generic.dmf_platform.IPlatformStateMapper;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.PlatformInformation;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.dmf_platform.JsonDmfPlatformInformation;
import com.digitalmicrofluidicbiochips.bachelorProject.model.io.json.dmf_platform.JsonDmfPlatformState;

import java.util.List;

public class JsonDmfInformationMapper implements IPlatformStateMapper<JsonDmfPlatformState> {

    @Override
    public PlatformInformation mapToInternal(JsonDmfPlatformState external) {

        JsonDmfPlatformInformation jsonInfo = external.getInformation();
        List<Electrode> electrodes = external.getElectrodes().stream()
                .map(e -> new Electrode(
                        e.getName(),
                        e.getID(),
                        e.getElectrodeID(),
                        e.getDriverID(),
                        e.getPositionX(),
                        e.getPositionY(),
                        e.getSizeX(),
                        e.getSizeY(),
                        e.getStatus()
                ))
                .toList();

        return new PlatformInformation(jsonInfo.getSizeX(), jsonInfo.getSizeY(), electrodes);
    }
}
