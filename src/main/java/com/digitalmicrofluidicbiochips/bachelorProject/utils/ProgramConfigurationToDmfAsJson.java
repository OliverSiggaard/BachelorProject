package com.digitalmicrofluidicbiochips.bachelorProject.utils;

import com.digitalmicrofluidicbiochips.bachelorProject.model.ProgramConfiguration;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.ElectrodeGrid;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProgramConfigurationToDmfAsJson {
    public static JsonNode convertProgramConfigurationToDmfAsJson(ProgramConfiguration programConfiguration, List<Droplet> droplets) {
        ElectrodeGrid electrodeGrid = programConfiguration.getElectrodeGrid();

        // File paths
        String originalFilePath = "src/main/resources/simulator_platform640.JSON";

        // Create ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable indentation for better readability

        try {
            JsonNode originalData = mapper.readTree(new File(originalFilePath));
            ArrayNode dropletsArray = (ArrayNode) originalData.get("droplets");

            List<String> colors = generateHexColors(droplets.size());
            int id = 0;
            for (Droplet droplet : droplets) {
                Electrode electrode = electrodeGrid.getElectrode(droplet.getPositionX(), droplet.getPositionY());
                int electrodeWidth = electrode.getSizeX();
                int dropletDiameterElectrodeSpan = DmfPlatformUtils.electrodeSpanRequiredToMoveDroplet(droplet, electrodeWidth);
                int offsetToCenter = dropletDiameterElectrodeSpan * electrodeWidth / 2;

                ObjectNode dropletNode = mapper.createObjectNode();
                dropletNode.put("name", droplet.getID());
                dropletNode.put("ID", id);
                dropletNode.put("substance_name", droplet.getID());
                dropletNode.put("positionX", droplet.getPositionX() * electrodeWidth + offsetToCenter);
                dropletNode.put("positionY", droplet.getPositionY() * electrodeWidth + offsetToCenter);
                dropletNode.put("sizeX", droplet.getDiameter());
                dropletNode.put("sizeY", droplet.getDiameter());
                dropletNode.put("color", colors.get(id));
                dropletNode.put("temperature", 20);
                dropletNode.put("electrodeID", electrode.getID());
                dropletsArray.add(dropletNode);
                id++;
            }

            return originalData;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<String> generateHexColors(int numberOfColors) {
        List<String> colors = new ArrayList<>();

        // Add pure Green, and Blue initially. Electrodes are red, so we want to avoid that color.
        colors.add("#00FF00"); // Green
        colors.add("#0000FF"); // Blue

        // Generate rest of the colors randomly
        Random random = new Random();
        for (int i = 3; i <= numberOfColors; i++) {
            String color = String.format("#%06X", random.nextInt(0xFFFFFF + 1));
            colors.add(color);
        }

        return colors;
    }
}
