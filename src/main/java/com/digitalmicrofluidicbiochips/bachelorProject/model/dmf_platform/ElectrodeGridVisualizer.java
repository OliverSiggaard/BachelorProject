package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

public class ElectrodeGridVisualizer {
    public void visualizeGrid(ElectrodeGrid grid) {
        StringBuilder gridString = new StringBuilder();
        gridString.append("""

                Visualization of the electrode grid:
                "#" indicates an electrode
                "0" indicates null
                """);

        for (int y = 0; y < grid.getYSize(); y++) {
            for (int x = 0; x < grid.getXSize(); x++) {
                if (grid.getElectrode(x, y) != null) {
                    gridString.append(" # ");
                } else {
                    gridString.append(" 0 ");
                }
            }
            if (y < grid.getYSize() - 1) gridString.append("\n");
        }

        System.out.println(gridString);
    }
}
