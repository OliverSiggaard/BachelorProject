package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ElectrodeTest {

    private final String name = "electrode";
    private final int elementID = 1;
    private final int electrodeID = 2;
    private final int driverID = 3;
    private final int positionX = 4;
    private final int positionY = 5;
    private final int sizeX = 6;
    private final int sizeY = 7;
    private final int status = 8;

    private Electrode sut;

    @BeforeEach
    public void initializeElectrode() {
        sut = new Electrode(
                name,
                elementID,
                electrodeID,
                driverID,
                positionX,
                positionY,
                sizeX,
                sizeY,
                status
        );
    }

    @Test
    public void electrodeInitializationTest() {
        Assertions.assertEquals(name, sut.getName());
        Assertions.assertEquals(elementID, sut.getID());
        Assertions.assertEquals(electrodeID, sut.getElectrodeID());
        Assertions.assertEquals(driverID, sut.getDriverID());
        Assertions.assertEquals(positionX, sut.getPositionX());
        Assertions.assertEquals(positionY, sut.getPositionY());
        Assertions.assertEquals(sizeX, sut.getSizeX());
        Assertions.assertEquals(sizeY, sut.getSizeY());
        Assertions.assertEquals(status, sut.getStatus());
    }

    @Test
    public void electrodeStatusTest() {
        sut.setStatus(0);
        Assertions.assertEquals(0, sut.getStatus());
        sut.setStatus(1);
        Assertions.assertEquals(1, sut.getStatus());
    }
}
