package TestGenerators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlanarGraphsGenerator {
    public static List<List<Integer>> generateRandomPlanarGraph(int nodes, double proximityFactor) {
        List<List<Integer>> graph = new ArrayList<>();
        Random random = new Random();
        double[][] points = new double[nodes][2];

        for (int i = 0; i < nodes; i++) {
            points[i][0] = random.nextDouble(); // x-coordinate
            points[i][1] = random.nextDouble(); // y-coordinate
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < nodes; i++) {
            for (int j = i + 1; j < nodes; j++) {
                double distance = Math.sqrt(Math.pow(points[i][0] - points[j][0], 2) + Math.pow(points[i][1] - points[j][1], 2));
                if (distance < proximityFactor) { // connect if within proximity factor
                    graph.get(i).add(j);
                    graph.get(j).add(i);
                }
            }
        }

        return graph;
    }

    public static void saveGraphToFile(String filename, List<List<Integer>> graph) throws IOException {
        File file = new File("./Tests/" + filename);
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i < graph.size(); i++) {
                writer.write(i + ": " + graph.get(i).toString().replaceAll("[\\[\\],]", "") + "\n");
            }
        }
    }

    public static void main(String[] args) {
        // parametrii grafului: numarul de noduri si factorul de proximitate
        int[] nodes = {15, 18, 20, 25, 30};
        double[] proximityFactors = {0.2, 0.18, 0.15, 0.14, 0.13}; // factorul de proximitate
        String[] names = {"PlanarGraph15", "PlanarGraph18", "PlanarGraph20", "PlanarGraph25", "PlanarGraph30"};

        for (int i = 0; i < nodes.length; i++) {
            try {
                List<List<Integer>> graph = generateRandomPlanarGraph(nodes[i], proximityFactors[i]);
                String filename = (i + 25) + "_" + names[i] + ".txt";
                saveGraphToFile(filename, graph);
                System.out.println("Graph saved: " + filename);
            } catch (IOException e) {
                System.err.println("Error saving graph: " + e.getMessage());
            }
        }
    }
}
