package com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform;

public class ElectrodeGridVisualizer {
    public void visualizeGrid(Electrode[][] grid) {
        StringBuilder gridString = new StringBuilder();
        gridString.append("""

                Visualization of the electrode grid:
                "#" indicates an electrode
                "0" indicates null
                """);

        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[x][y] != null) {
                    gridString.append(" # ");
                } else {
                    gridString.append(" 0 ");
                }
            }
            if (y < grid[0].length - 1) gridString.append("\n");
        }

        System.out.println(gridString);
    }
}
