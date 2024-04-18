package com.digitalmicrofluidicbiochips.bachelorProject.utils;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DmfPlatformUtilsTest {

    @Test
    void testGetSetElectrodeCommand() {
        int electrodeId = 3;
        String expected = "SETELI " + electrodeId + ";";
        Assertions.assertEquals(expected, DmfPlatformUtils.getSetElectrodeCommand(electrodeId));
    }

    @Test
    void testGetClearElectrodeCommand() {
        int electrodeId = 3;
        String expected = "CLRELI " + electrodeId + ";";
        Assertions.assertEquals(expected, DmfPlatformUtils.getClearElectrodeCommand(electrodeId));
    }

    @Test
    void testElectrodeSpanRequiredToMoveDroplet() {
        Droplet dropletMock = mock(Droplet.class);
        when(dropletMock.getDiameter()).thenReturn(5);
        Assertions.assertEquals(1, DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(dropletMock, 20));

        when(dropletMock.getDiameter()).thenReturn(20);
        Assertions.assertEquals(1, DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(dropletMock, 20));

        when(dropletMock.getDiameter()).thenReturn(25);
        Assertions.assertEquals(1, DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(dropletMock, 20));

        when(dropletMock.getDiameter()).thenReturn(39);
        Assertions.assertEquals(1, DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(dropletMock, 20));

        when(dropletMock.getDiameter()).thenReturn(40);
        Assertions.assertEquals(2, DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(dropletMock, 20));

        when(dropletMock.getDiameter()).thenReturn(45);
        Assertions.assertEquals(2, DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(dropletMock, 20));

        when(dropletMock.getDiameter()).thenReturn(59);
        Assertions.assertEquals(2, DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(dropletMock, 20));

        when(dropletMock.getDiameter()).thenReturn(60);
        Assertions.assertEquals(3, DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(dropletMock, 20));
    }
}
