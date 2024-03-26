package com.digitalmicrofluidicbiochips.bachelorProject.executor.path_finding;

import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Droplet;
import com.digitalmicrofluidicbiochips.bachelorProject.model.dmf_platform.Electrode;

import java.util.*;

public class AStar implements IPathFinder {

    private boolean logPath = false;

    // Default constructor (logging disabled)
    public AStar() {}

    public AStar(boolean logPath) {
        this.logPath = logPath;
    }

    @Override
    public DropletMove getMove(Droplet activeDroplet, Electrode[][] availableGrid, int goalX, int goalY) {
        // Start and goal nodes
        Node start = new Node(activeDroplet.getPositionX(), activeDroplet.getPositionY());
        Node goal = new Node(goalX, goalY);

        // Run A*
        List<Node> path = findPath(availableGrid, start, goal);

        // Log path if logging is enabled
        if (logPath && path != null) {
            logAStarPath(path);
        }

        // Return the droplet move
        return getMoveFromPath(start, path);
    }

    private static class Node implements Comparable<Node> {
        // Position
        int x, y;

        // g is cost to reach node
        // h is cost to get from node to goal
        // f = g + h
        double g, h, f;

        Node parent;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Node otherNode = (Node) object;
            return x == otherNode.x && y == otherNode.y;
        }

        @Override
        public int compareTo(Node otherNode) {
            return Double.compare(this.f, otherNode.f);
        }
    }

    private List<Node> findPath(Electrode[][] grid, Node start, Node goal) {
            PriorityQueue<Node> openList = new PriorityQueue<>(); // Nodes to be examined
            ArrayList<Node> closedList = new ArrayList<>(); // Nodes that have been examined

            openList.add(start);

        while (!openList.isEmpty()) {
            Node current = openList.poll(); // Get first in list (one containing lowest f value)

            // Check if goal node has been reached
            if (current.x == goal.x && current.y == goal.y) {
                return reconstructPath(current);
            }

            closedList.add(current);

            // Get neighbors for current node
            List<Node> neighbors = getNeighborNodes(current, grid);
            for (Node neighbor : neighbors) {
                if (closedList.contains(neighbor)) continue; // Skip already examined neighbors

                double gCost = current.g + 1; // Simple uniform cost for all moves

                // If neighbor is not the in the open list it is added
                // If the new cost (gCost) is lower than the current neighbor.g its information is updated
                if (!openList.contains(neighbor) || gCost < neighbor.g) {
                    neighbor.parent = current;
                    neighbor.g = gCost;
                    neighbor.h = calculateHeuristic(neighbor, goal);
                    neighbor.f = neighbor.g + neighbor.h;

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        return null; // No path found
    }


    private List<Node> getNeighborNodes(Node node, Electrode[][] grid) {
        List<Node> neighbors = new ArrayList<>();

        // Possible movements (up, down, left, right)
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int[] direction : directions) {
            int neighborX = node.x + direction[0];
            int neighborY = node.y + direction[1];

            if (isValidPosition(neighborX, neighborY, grid)) neighbors.add(new Node(neighborX, neighborY));
        }

        return neighbors;
    }

    // Checks if the position is within bounds and if electrode is available
    private boolean isValidPosition(int x, int y, Electrode[][] grid) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] != null;
    }

    // Heuristic is calculated as Manhattan distance
    // Should make heuristic admissible as it is never overestimated
    private double calculateHeuristic(Node from, Node to) {
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    // Reconstruct the path from start to goal node
    private List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path); // Reverse the order so that it is start -> goal
        return path;
    }

    private DropletMove getMoveFromPath(Node start, List<Node> path) {
        if (path == null) {
            return DropletMove.BLOCKED; // No path could be found
        } else if (path.size() <= 1) {
            return DropletMove.NONE; // Droplet did not move
        }

        Node nextNode = path.get(1); // Get the next node (that is not the starting node)
        if (nextNode.y < start.y) {
            return DropletMove.UP;
        } else if (nextNode.y > start.y) {
            return DropletMove.DOWN;
        } else if (nextNode.x > start.x) {
            return DropletMove.RIGHT;
        } else if (nextNode.x < start.x) {
            return DropletMove.LEFT;
        } else {
            throw new IllegalStateException("No valid move found for path");
        }
    }

    private void logAStarPath(List<Node> path) {
        for (Node node : path) {
            System.out.println("(" + node.x + "," + node.y + ")");
        }
    }
}
