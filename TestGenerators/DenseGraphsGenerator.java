package TestGenerators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DenseGraphsGenerator {
    public static List<List<Integer>> generateRandomDenseGraph(int nodes, double density) {
        List<List<Integer>> graph = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < nodes; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < nodes; i++) {
            for (int j = i + 1; j < nodes; j++) {
                if (random.nextDouble() < density) { // density - probability
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
        // parametrii grafului: numarul de noduri si densitatea
        int[] nodes = {16, 18, 20, 22};
        double[] densities = {0.85, 0.75, 0.7, 0.65};
        // numele fisierelor de salvare
        String[] names = {"DenseGraph16", "DenseGraph18", "DenseGraph20", "DenseGraph22"};

        for (int i = 0; i < nodes.length; i++) {
            try {
                List<List<Integer>> graph = generateRandomDenseGraph(nodes[i], densities[i]);
                String filename = (i + 21) + "_" + names[i] + ".txt";
                saveGraphToFile(filename, graph);
                System.out.println("Graph saved: " + filename);
            } catch (IOException e) {
                System.err.println("Error saving graph: " + e.getMessage());
            }
        }
    }
}
