package com.digitalmicrofluidicbiochips.bachelorProject.model.actions.actionResult;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtils;
import com.digitalmicrofluidicbiochips.bachelorProject.utils.DmfPlatformUtilsTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class SetElectrodeCommandTest {

    @Test
    void testSetElectrodeCommand() {

        Electrode electrode = mock(Electrode.class);
        when(electrode.getID()).thenReturn(3);

        SetElectrodeCommand sut = new SetElectrodeCommand(electrode);
        Assertions.assertEquals(DmfPlatformUtils.getSetElectrodeCommand(3), sut.getDmfCommand());
    }

    @Test
    void testUpdateModelWithCommand() {
        Electrode electrode = mock(Electrode.class);
        when(electrode.getID()).thenReturn(3);

        SetElectrodeCommand sut = new SetElectrodeCommand(electrode);

        sut.updateModelWithCommand();
        verify(electrode).setStatus(1);
    }
}
