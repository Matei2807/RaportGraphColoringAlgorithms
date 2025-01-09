package TestGenerators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MediumGraphsGenerator {
    public static List<List<Integer>> generateRandomGraph(int nodes, double probability) {
        List<List<Integer>> graph = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < nodes; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < nodes; i++) {
            for (int j = i + 1; j < nodes; j++) {
                if (random.nextDouble() < probability) {
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
        // parametrii grafului: numarul de noduri si probabilitatea
        int[] nodes = {12, 15, 20, 25, 30, 32};
        double[] probabilities = {0.4, 0.3, 0.4, 0.35, 0.35, 0.3};
        // numele fisierelor de salvare
        String[] names = {"MediumGraph12", "MediumGraph15", "MediumGraph20", "MediumGraph25", "MediumGraph30", "MediumGraph32"};

        for (int i = 0; i < nodes.length; i++) {
            try {
                List<List<Integer>> graph = generateRandomGraph(nodes[i], probabilities[i]);
                String filename = (i + 11) + "_" + names[i] + ".txt";
                saveGraphToFile(filename, graph);
                System.out.println("Graph saved: " + filename);
            } catch (IOException e) {
                System.err.println("Error saving graph: " + e.getMessage());
            }
        }
    }
}
