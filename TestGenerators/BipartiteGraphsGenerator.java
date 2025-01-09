package TestGenerators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BipartiteGraphsGenerator {
    public static List<List<Integer>> generateRandomBipartiteGraph(int nodes) {
        List<List<Integer>> graph = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < nodes; i++) {
            graph.add(new ArrayList<>());
        }

        // two partitions
        int mid = nodes / 2;
        for (int i = 0; i < mid; i++) {
            for (int j = mid; j < nodes; j++) {
                if (random.nextDouble() < 0.5) { // 50% probability
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
        // parametrii grafului: numarul de noduri
        int[] nodes = {45, 50, 55, 60};
        // nuemle fisierelor de salvare
        String[] names = {"BipartiteGraph45", "BipartiteGraph50", "BipartiteGraph55", "BipartiteGraph60"};

        for (int i = 0; i < nodes.length; i++) {
            try {
                List<List<Integer>> graph = generateRandomBipartiteGraph(nodes[i]);
                String filename = (i + 17) + "_" + names[i] + ".txt";
                saveGraphToFile(filename, graph);
                System.out.println("Graph saved: " + filename);
            } catch (IOException e) {
                System.err.println("Error saving graph: " + e.getMessage());
            }
        }
    }
}
