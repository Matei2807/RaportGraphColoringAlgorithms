package TestGenerators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiComponentGraphsGenerator {
    public static List<List<Integer>> generateMultiComponentGraph(int[][] components) {
        List<List<Integer>> graph = new ArrayList<>();
        int totalNodes = 0;

        for (int[] component : components) {
            totalNodes += component[0];
        }

        for (int i = 0; i < totalNodes; i++) {
            graph.add(new ArrayList<>());
        }

        Random random = new Random();
        int offset = 0;

        for (int[] component : components) {
            int nodes = component[0];
            double probability = component[1] / 100.0;

            for (int i = offset; i < offset + nodes; i++) {
                for (int j = i + 1; j < offset + nodes; j++) {
                    if (random.nextDouble() < probability) {
                        graph.get(i).add(j);
                        graph.get(j).add(i);
                    }
                }
            }

            offset += nodes;
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
        // componente: [nr de noduri, probabilitatea (%) per component]
        int[][][] multiComponentConfigs = {
                {{10, 50}, {7, 70}, {5, 30}}, // Graph 29
                {{8, 60}, {12, 40}, {6, 50}, {4, 30}}, // Graph 30
                {{15, 70}, {10, 50}}, // Graph 31
                {{10, 60}, {8, 50}, {7, 40}, {6, 70}}, // Graph 32
        };

        String[] names = {"MultiComponentGraph29", "MultiComponentGraph30", "MultiComponentGraph31", "MultiComponentGraph32"};

        for (int i = 0; i < multiComponentConfigs.length; i++) {
            try {
                List<List<Integer>> graph = generateMultiComponentGraph(multiComponentConfigs[i]);
                String filename = (i + 30) + "_" + names[i] + ".txt";
                saveGraphToFile(filename, graph);
                System.out.println("Graph saved: " + filename);
            } catch (IOException e) {
                System.err.println("Error saving graph: " + e.getMessage());
            }
        }
    }
}
