package com.digitalmicrofluidicbiochips.bachelorProject.model;

import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.ActionBase;
import com.digitalmicrofluidicbiochips.bachelorProject.model.actions.implementations.InputAction;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.DropletStatus;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.PlatformInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProgramConfigurationTest {

    private ProgramConfiguration sut;

    private PlatformInformation platformInformation;
    private List<ActionBase> actions;

    private Droplet droplet1;
    private Droplet droplet2;
    private Droplet droplet3;

    @BeforeEach
    public void setUp() {

        platformInformation = mock(PlatformInformation.class);
        InputAction action1 = mock(InputAction.class);
        ActionBase action2 = mock(ActionBase.class);
        ActionBase action3 = mock(ActionBase.class);
        actions = List.of(action1, action2, action3);

        droplet1 = mock(Droplet.class);
        droplet2 = mock(Droplet.class);
        droplet3 = mock(Droplet.class);
        when(droplet1.getStatus()).thenReturn(DropletStatus.NOT_CREATED);
        when(droplet2.getStatus()).thenReturn(DropletStatus.NOT_CREATED);
        when(droplet3.getStatus()).thenReturn(DropletStatus.NOT_CREATED);

        when(action1.dropletsProducedByExecution()).thenReturn(Set.of(droplet1));
        when(action3.dropletsProducedByExecution()).thenReturn(Set.of(droplet2, droplet3));

        Electrode electrode = mock(Electrode.class);
        when(platformInformation.getElectrodes()).thenReturn(List.of(electrode));
        when(electrode.getSizeX()).thenReturn(20);
        when(electrode.getSizeY()).thenReturn(20);
        when(platformInformation.getSizeX()).thenReturn(20);
        when(platformInformation.getSizeY()).thenReturn(20);

        sut = new ProgramConfiguration(platformInformation, actions);
    }

    @Test
    public void testGetDropletsOnDmfPlatform() {
        Assertions.assertEquals(0, sut.getDropletsOnDmfPlatform().size());
        when(droplet1.getStatus()).thenReturn(DropletStatus.UNAVAILABLE);
        Assertions.assertEquals(1, sut.getDropletsOnDmfPlatform().size());
    }

    @Test
    public void testGetDropletsFromInputActions() {
        Assertions.assertEquals(1, sut.getDropletsFromInputActions().size());
        when(droplet1.getStatus()).thenReturn(DropletStatus.UNAVAILABLE);
        Assertions.assertEquals(1, sut.getDropletsFromInputActions().size());
        when(droplet1.getStatus()).thenReturn(DropletStatus.NOT_CREATED);
        when(droplet2.getStatus()).thenReturn(DropletStatus.UNAVAILABLE);
        Assertions.assertEquals(1, sut.getDropletsFromInputActions().size());
    }

    @Test
    public void testElectrodeGrid() {
        Assertions.assertNotNull(sut.getElectrodeGrid());
        Assertions.assertEquals(1, sut.getElectrodeGrid().getXSize());
        Assertions.assertEquals(1, sut.getElectrodeGrid().getYSize());
    }

    @Test
    public void testPathFinder() {
        Assertions.assertNotNull(sut.getPathFinder());
    }

    @Test
    public void testProgramActions() {
        Assertions.assertEquals(3, sut.getProgramActions().size());
    }

    @Test
    public void testPlatformInformation() {
        Assertions.assertNotNull(sut.getPlatformInformation());
    }

    @Test
    public void testDroplets() {
        Assertions.assertEquals(3, sut.getDroplets().size());
    }


}
