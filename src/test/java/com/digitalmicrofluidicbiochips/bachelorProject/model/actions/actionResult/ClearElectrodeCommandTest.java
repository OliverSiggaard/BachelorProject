package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class ClearElectrodeCommandTest {
    @Test
    void testSetElectrodeCommand() {
        Electrode electrode = mock(Electrode.class);
        when(electrode.getID()).thenReturn(3);

        ClearElectrodeCommand sut = new ClearElectrodeCommand(electrode);
        Assertions.assertEquals(DmfPlatformUtils.getClearElectrodeCommand(3), sut.getBioAssemblyInstruction());
    }
}
