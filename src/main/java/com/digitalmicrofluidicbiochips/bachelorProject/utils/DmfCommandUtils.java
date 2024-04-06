package com.digitalmicrofluidicbiochips.bachelorProject.utils;

public class DmfCommandUtils {

    public static String getSetElectrodeCommand(int electrodeID) {
        return "SETELI " + electrodeID + ";";
    }

    public static String getClearElectrodeCommand(int electrodeID) {
        return "CLRELI " + electrodeID + ";";
    }
}
