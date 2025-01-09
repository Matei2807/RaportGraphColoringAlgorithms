import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class GraphColoring {

    public static class Result {
        int chromaticNumber;
        int[] colorAssignment;

        public Result(int chromaticNumber, int[] colorAssignment) {
            this.chromaticNumber = chromaticNumber;
            this.colorAssignment = colorAssignment;
        }

        public Result() {
            this.chromaticNumber = -1;
            this.colorAssignment = new int[]{-1};
        }

        public String toString() {
            return chromaticNumber + " culori";
        }
    }

    public static class TestResults {
        double totalTime;
        double backtrackingTime;
        double greedyDegreeTime;
        double greedyDSATURTime;
        double greedyDegreeAccuracy;
        double greedyDSATURAccuracy;
        double greedyDegreeDifference;
        double greedyDSATURDifference;

        public TestResults(double totalTime, double backtrackingTime, double greedyDegreeTime, double greedyDSATURTime, double greedyDegreeAccuracy, double greedyDSATURAccuracy, double greedyDegreeDifference, double greedyDSATURDifference) {
            this.totalTime = totalTime;
            this.backtrackingTime = backtrackingTime;
            this.greedyDegreeTime = greedyDegreeTime;
            this.greedyDSATURTime = greedyDSATURTime;
            this.greedyDegreeAccuracy = greedyDegreeAccuracy;
            this.greedyDSATURAccuracy = greedyDSATURAccuracy;
            this.greedyDegreeDifference = greedyDegreeDifference;
            this.greedyDSATURDifference = greedyDSATURDifference;
        }

        public TestResults() {
            this.totalTime = 0;
            this.backtrackingTime = 0;
            this.greedyDegreeTime = 0;
            this.greedyDSATURTime = 0;
            this.greedyDegreeAccuracy = 0;
            this.greedyDSATURAccuracy = 0;
            this.greedyDegreeDifference = 0;
            this.greedyDSATURDifference = 0;
        }

        public void addResults(TestResults results) {
            this.totalTime += results.totalTime;
            this.backtrackingTime += results.backtrackingTime;
            this.greedyDegreeTime += results.greedyDegreeTime;
            this.greedyDSATURTime += results.greedyDSATURTime;
            this.greedyDegreeAccuracy += results.greedyDegreeAccuracy;
            this.greedyDSATURAccuracy += results.greedyDSATURAccuracy;
            this.greedyDegreeDifference += results.greedyDegreeDifference;
            this.greedyDSATURDifference += results.greedyDSATURDifference;
        }

        public void makeAverage(int tests){
            this.totalTime /= tests;
            this.backtrackingTime /= tests;
            this.greedyDegreeTime /= tests;
            this.greedyDSATURTime /= tests;
            this.greedyDegreeAccuracy /= tests;
            this.greedyDSATURAccuracy /= tests;
            this.greedyDegreeDifference /= tests;
            this.greedyDSATURDifference /= tests;
        }
    }

    static List<TestResults> avgTestResults = new ArrayList<>();

    // backtracking : https://www.geeksforgeeks.org/graph-coloring-applications/
    private static boolean backtrackingColoring(List<List<Integer>> graph, int colors, int node, int[] colorAssignment) {
        if (node == graph.size()) {
            return true;
        }

        for (int color = 0; color < colors; color++) {
            if (isSafe(graph, node, colorAssignment, color)) {
                colorAssignment[node] = color;
                if (backtrackingColoring(graph, colors, node + 1, colorAssignment)) {
                    return true;
                }
                colorAssignment[node] = -1;
            }
        }
        return false;
    }

    private static boolean isSafe(List<List<Integer>> graph, int node, int[] colorAssignment, int color) {
        for (int neighbor : graph.get(node)) {
            if (colorAssignment[neighbor] == color) {
                return false;
            }
        }
        return true;
    }

    public static Result findChromaticNumberBacktracking(List<List<Integer>> graph) {
        int n = graph.size();
        for (int colors = 1; colors <= n; colors++) {
            int[] colorAssignment = new int[n];
            Arrays.fill(colorAssignment, -1);
            if (backtrackingColoring(graph, colors, 0, colorAssignment)) {
                return new Result(colors, colorAssignment);
            }
        }
        return new Result();
    }

    // greedy (cu grad)
    public static Result greedyColoringDegreeOrder(List<List<Integer>> graph) {
        int n = graph.size();
        int[] colorAssignment = new int[n];
        Arrays.fill(colorAssignment, -1);

        List<Integer> sortedNodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            sortedNodes.add(i);
        }
        sortedNodes.sort((a, b) -> {
            int degreeDiff = graph.get(b).size() - graph.get(a).size();
            if (degreeDiff == 0) {
                return new Random().nextInt(2) * 2 - 1; // echivalent cu choice()
            }
            return degreeDiff;
        });

        for (int node : sortedNodes) {
            Set<Integer> usedColors = new HashSet<>();
            for (int neighbor : graph.get(node)) {
                if (colorAssignment[neighbor] != -1) {
                    usedColors.add(colorAssignment[neighbor]);
                }
            }

            for (int color = 0; color < n; color++) {
                if (!usedColors.contains(color)) {
                    colorAssignment[node] = color;
                    break;
                }
            }
        }

        return new Result(Arrays.stream(colorAssignment).max().getAsInt() + 1, colorAssignment);
    }

    // Greedy (DSATUR)
    public static Result greedyColoringDSATUR(List<List<Integer>> graph) {
        int n = graph.size();
        int[] colorAssignment = new int[n];
        Arrays.fill(colorAssignment, -1);
        int[] dsat = new int[n]; // Saturation degree
        int[] degree = new int[n];

        for (int i = 0; i < n; i++) {
            degree[i] = graph.get(i).size();
        }

        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int maxDsatNode = -1;

            for (int j = 0; j < n; j++) {
                if (colorAssignment[j] == -1) {
                    if (maxDsatNode == -1 || dsat[j] > dsat[maxDsatNode] ||
                            (dsat[j] == dsat[maxDsatNode] && degree[j] > degree[maxDsatNode]) ||
                            (dsat[j] == dsat[maxDsatNode] && degree[j] == degree[maxDsatNode] && random.nextBoolean())) {
                        maxDsatNode = j;
                    }
                }
            }

            Set<Integer> usedColors = new HashSet<>();
            for (int neighbor : graph.get(maxDsatNode)) {
                if (colorAssignment[neighbor] != -1) {
                    usedColors.add(colorAssignment[neighbor]);
                }
            }

            for (int color = 0; color < n; color++) {
                if (!usedColors.contains(color)) {
                    colorAssignment[maxDsatNode] = color;
                    break;
                }
            }

            for (int neighbor : graph.get(maxDsatNode)) {
                if (colorAssignment[neighbor] == -1) {
                    dsat[neighbor]++;
                }
            }
        }

        return new Result(Arrays.stream(colorAssignment).max().getAsInt() + 1, colorAssignment);
    }

    public static List<List<Integer>> readGraphFromFile(String filename) throws IOException {
        List<List<Integer>> graph = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            lines.forEach(line -> {
                String[] parts = line.split(":");
                int node = Integer.parseInt(parts[0].trim());
                String[] neighbors = parts[1].trim().split(" ");

                while (graph.size() <= node) {
                    graph.add(new ArrayList<>());
                }

                for (String neighbor : neighbors) {
                    if (!neighbor.isEmpty()) {
                        graph.get(node).add(Integer.parseInt(neighbor));
                    }
                }
            });
        }

        return graph;
    }

    public static TestResults evaluateGraphs(String directory, String outputFilename) throws IOException {
        File folder = new File(directory);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null) {
            throw new IOException("no files found in dir: " + directory);
        }

        // we sort the files by the number
        Arrays.sort(files, (f1, f2) -> {
            int n1 = Integer.parseInt(f1.getName().split("_")[0]);
            int n2 = Integer.parseInt(f2.getName().split("_")[0]);
            return Integer.compare(n1, n2);
        });

        double totalBacktrackingTime = 0;
        double totalGreedyDegreeTime = 0;
        double totalGreedyDSATURTime = 0;

        double totalAccuracyGreedyDegree = 0;
        double totalAccuracyGreedyDSATUR = 0;

        double totalDifferenceGreedyDegree = 0;
        double totalDifferenceGreedyDSATUR = 0;

        int index = 0;
        try (FileWriter writer = new FileWriter(outputFilename)) {
            for (File file : files) {
                String graphName = file.getName();
                List<List<Integer>> graph = readGraphFromFile(file.getPath());

                // backtracking
                long startBacktracking = System.nanoTime();
                Result backtrackingResult = findChromaticNumberBacktracking(graph);
                long endBacktracking = System.nanoTime();
                double timeBacktracking = (endBacktracking - startBacktracking) / 1e6;
                totalBacktrackingTime += timeBacktracking / 1000.0;
                int corectDegree = backtrackingResult.chromaticNumber;

                // greedy (cu grad) x 100 and averages
                int totalGreedyDegreeChromatic = 0;
                int totalCorrectGreedyDegree = 0;
                double totalGreedyDegreeRuntime = 0;
                int currentDifferenceGreedyDegree = 0;

                for (int i = 0; i < 100; i++) {
                    long startGreedyDegree = System.nanoTime();
                    Result greedyDegreeResult = greedyColoringDegreeOrder(graph);
                    long endGreedyDegree = System.nanoTime();
                    totalGreedyDegreeChromatic += greedyDegreeResult.chromaticNumber;
                    totalGreedyDegreeRuntime += (endGreedyDegree - startGreedyDegree) / 1e6;
                    if (greedyDegreeResult.chromaticNumber == backtrackingResult.chromaticNumber) {
                        totalCorrectGreedyDegree++;
                    }
                    totalDifferenceGreedyDegree += Math.abs(corectDegree - greedyDegreeResult.chromaticNumber);
                    currentDifferenceGreedyDegree += Math.abs(corectDegree - greedyDegreeResult.chromaticNumber);
                }
                double avgGreedyDegreeChromatic = totalGreedyDegreeChromatic / 100.0;
                double avgGreedyDegreeTime = totalGreedyDegreeRuntime / 100.0;
                totalGreedyDegreeTime += avgGreedyDegreeTime / 1000.0;
                double greedyDegreeAccuracy = (totalCorrectGreedyDegree / 100.0) * 100;
                totalAccuracyGreedyDegree += greedyDegreeAccuracy;

                // greedy (DSATUR) x 100 and averages
                int totalGreedyDSATURChromatic = 0;
                double totalGreedyDSATURRuntime = 0;
                int totalCorrectGreedyDSATUR = 0;
                int currentDifferenceGreedyDSATUR = 0;

                for (int i = 0; i < 100; i++) {
                    long startGreedyDSATUR = System.nanoTime();
                    Result greedyDSATURResult = greedyColoringDSATUR(graph);
                    long endGreedyDSATUR = System.nanoTime();
                    totalGreedyDSATURChromatic += greedyDSATURResult.chromaticNumber;
                    totalGreedyDSATURRuntime += (endGreedyDSATUR - startGreedyDSATUR) / 1e6;
                    if (greedyDSATURResult.chromaticNumber == backtrackingResult.chromaticNumber) {
                        totalCorrectGreedyDSATUR++;
                    }
                    totalDifferenceGreedyDSATUR += Math.abs(corectDegree - greedyDSATURResult.chromaticNumber);
                    currentDifferenceGreedyDSATUR += Math.abs(corectDegree - greedyDSATURResult.chromaticNumber);
                }
                double avgGreedyDSATURChromatic = totalGreedyDSATURChromatic / 100.0;
                double avgGreedyDSATURTime = totalGreedyDSATURRuntime / 100.0;
                totalGreedyDSATURTime += avgGreedyDSATURTime / 1000.0;
                double greedyDSATURAccuracy = (totalCorrectGreedyDSATUR / 100.0) * 100;
                totalAccuracyGreedyDSATUR += greedyDSATURAccuracy;

                // write the results in the file
                writer.write(graphName + ":\n");

                // make all times to have max 5 decimals
                timeBacktracking = Math.round(timeBacktracking * 100000.0) / 100000.0;
                avgGreedyDegreeTime = Math.round(avgGreedyDegreeTime * 100000.0) / 100000.0;
                avgGreedyDSATURTime = Math.round(avgGreedyDSATURTime * 100000.0) / 100000.0;

                // make all accuracies and chromatic numbers to have max 1 decimal
                avgGreedyDegreeChromatic = Math.round(avgGreedyDegreeChromatic * 10.0) / 10.0;
                avgGreedyDSATURChromatic = Math.round(avgGreedyDSATURChromatic * 10.0) / 10.0;
                greedyDegreeAccuracy = Math.round(greedyDegreeAccuracy * 10.0) / 10.0;
                greedyDSATURAccuracy = Math.round(greedyDSATURAccuracy * 10.0) / 10.0;

                // check to write the backtracking time in s or ms
                if (timeBacktracking > 1000) {
                    timeBacktracking /= 1000;
                    timeBacktracking = Math.round(timeBacktracking * 1000.0) / 1000.0;
                    writer.write("- Backtracking: " + backtrackingResult + " (" + timeBacktracking + " s)\n");
                } else {
                    writer.write("- Backtracking: " + backtrackingResult + " (" + timeBacktracking + " ms)\n");
                }
                writer.write("- Greedy cu grad: avg result " + avgGreedyDegreeChromatic + " culori, avg time " + avgGreedyDegreeTime + " ms, Accuracy = " + greedyDegreeAccuracy + "%\n");
                writer.write("- Greedy DSATUR: avg result " + avgGreedyDSATURChromatic + " culori, avg time " + avgGreedyDSATURTime + " ms, Accuracy = " + greedyDSATURAccuracy + "%\n\n");

                double testTime = timeBacktracking + avgGreedyDegreeTime + avgGreedyDSATURTime;

                TestResults results = new TestResults(testTime, timeBacktracking, avgGreedyDegreeTime, avgGreedyDSATURTime, greedyDegreeAccuracy, greedyDSATURAccuracy, currentDifferenceGreedyDegree / 100.0, currentDifferenceGreedyDSATUR / 100.0);
                avgTestResults.get(index).addResults(results);
                index++;
            }

            // make all total times to have max 5 decimals
            totalBacktrackingTime = Math.round(totalBacktrackingTime * 100000.0) / 100000.0;
            totalGreedyDegreeTime = Math.round(totalGreedyDegreeTime * 100000.0) / 100000.0;
            totalGreedyDSATURTime = Math.round(totalGreedyDSATURTime * 100000.0) / 100000.0;

            // divide the total accuracies by the number of tests
            totalAccuracyGreedyDegree /= files.length;
            totalAccuracyGreedyDSATUR /= files.length;
            totalAccuracyGreedyDSATUR = Math.round(totalAccuracyGreedyDSATUR * 10.0) / 10.0;
            totalAccuracyGreedyDegree = Math.round(totalAccuracyGreedyDegree * 10.0) / 10.0;

            totalDifferenceGreedyDegree /= files.length * 100;
            totalDifferenceGreedyDSATUR /= files.length * 100;

            writer.write("Total times:\n");
            writer.write("- Backtracking: " + totalBacktrackingTime + " s\n");
            writer.write("- Greedy cu grad: " + totalGreedyDegreeTime * 1000 + " ms(100 avg) - total time: " + totalGreedyDegreeTime * 100 + " s - Accuracy = " + totalAccuracyGreedyDegree + "%  - Avg difference = " + totalDifferenceGreedyDegree + " culori\n");
            writer.write("- Greedy DSATUR: " + totalGreedyDSATURTime * 1000 + " ms(100 avg) - total time: " + totalGreedyDSATURTime * 100 + " s - Accuracy = " + totalAccuracyGreedyDSATUR + "% - Avg difference = " + totalDifferenceGreedyDSATUR + " culori\n");

            double total = totalBacktrackingTime + totalGreedyDegreeTime * 100 + totalGreedyDSATURTime * 100;
            total = Math.round(total * 1000.0) / 1000.0;
            writer.write("\n--TOTAL TIME: " + total + " s\n");

            return new TestResults(total, totalBacktrackingTime, totalGreedyDegreeTime, totalGreedyDSATURTime, totalAccuracyGreedyDegree, totalAccuracyGreedyDSATUR, totalDifferenceGreedyDegree, totalDifferenceGreedyDSATUR);
        }
    }

    public static void main(String[] args) {
        int totalRuns = 50; // runing the tests 50 times for each graph
        String resultsDirectory = "./Results/";
        String testsDirectory = "./Tests/";

        File resultsFolder = new File(resultsDirectory);
        if (!resultsFolder.exists()) {
            resultsFolder.mkdir();
        }

        for (int i = 1; i <= 33; i++){
            TestResults results = new TestResults();
            avgTestResults.add(results);
        }

        List<TestResults> testResults = new ArrayList<>();

        try {
            for (int i = 1; i <= totalRuns; i++) {
                String outputFilename = resultsDirectory + "test_result_" + i + ".txt";

                System.out.println("Running test " + i + "...");
                long start = System.nanoTime();
                TestResults results = evaluateGraphs(testsDirectory, outputFilename);
                long end = System.nanoTime();
                double executionTime = (end - start) / 1e6; // ms
                System.out.println("Test " + i + " completed in " + executionTime + " ms.(" + results.totalTime + " s)");

                testResults.add(results);
            }

            String summaryFilename = resultsDirectory + "summary_results.txt";
            try (FileWriter writer = new FileWriter(summaryFilename)) {
                writer.write("Summary of " + totalRuns + " runs:\n\n");

                double avgTotalTime = testResults.stream().mapToDouble(r -> r.totalTime).average().orElse(0.0);
                double avgBacktrackingTime = testResults.stream().mapToDouble(r -> r.backtrackingTime).average().orElse(0.0);
                double avgGreedyDegreeTime = testResults.stream().mapToDouble(r -> r.greedyDegreeTime).average().orElse(0.0) * 1000; // ms
                double avgGreedyDSATURTime = testResults.stream().mapToDouble(r -> r.greedyDSATURTime).average().orElse(0.0) * 1000; // ms
                double avgGreedyDegreeAccuracy = testResults.stream().mapToDouble(r -> r.greedyDegreeAccuracy).average().orElse(0.0);
                double avgGreedyDSATURAccuracy = testResults.stream().mapToDouble(r -> r.greedyDSATURAccuracy).average().orElse(0.0);
                double avgGreedyDegreeDifference = testResults.stream().mapToDouble(r -> r.greedyDegreeDifference).average().orElse(0.0);
                double avgGreedyDSATURDifference = testResults.stream().mapToDouble(r -> r.greedyDSATURDifference).average().orElse(0.0);

                writer.write("- Average Total Time: " + String.format("%.3f", avgTotalTime) + " s\n");
                writer.write("- Average Backtracking Time: " + String.format("%.3f", avgBacktrackingTime) + " s\n");
                writer.write("- Average Greedy Degree Time: " + String.format("%.3f", avgGreedyDegreeTime) + " ms\n");
                writer.write("- Average Greedy DSATUR Time: " + String.format("%.3f", avgGreedyDSATURTime) + " ms\n");
                writer.write("- Average Greedy Degree Accuracy: " + String.format("%.3f", avgGreedyDegreeAccuracy) + "%\n");
                writer.write("- Average Greedy DSATUR Accuracy: " + String.format("%.3f", avgGreedyDSATURAccuracy) + "%\n");
                writer.write("- Average Difference (Greedy Degree): " + String.format("%.3f", avgGreedyDegreeDifference) + "\n");
                writer.write("- Average Difference (Greedy DSATUR): " + String.format("%.3f", avgGreedyDSATURDifference) + "\n\n");

                writer.write("Per Test Details:\n");
                for (int i = 0; i < totalRuns; i++) {
                    TestResults result = testResults.get(i);
                    writer.write("Test " + (i + 1) + ":\n");
                    writer.write("  - Total Time: " + String.format("%.3f", result.totalTime) + " s\n");
                    writer.write("  - Backtracking Time: " + String.format("%.3f", result.backtrackingTime) + " s\n");
                    writer.write("  - Greedy Degree Time: " + String.format("%.3f", result.greedyDegreeTime * 1000) + " ms\n");
                    writer.write("  - Greedy DSATUR Time: " + String.format("%.3f", result.greedyDSATURTime * 1000) + " ms\n");
                    writer.write("  - Greedy Degree Accuracy: " + String.format("%.3f", result.greedyDegreeAccuracy) + "%\n");
                    writer.write("  - Greedy DSATUR Accuracy: " + String.format("%.3f", result.greedyDSATURAccuracy) + "%\n");
                    writer.write("  - Greedy Degree Difference: " + String.format("%.3f", result.greedyDegreeDifference) + "\n");
                    writer.write("  - Greedy DSATUR Difference: " + String.format("%.3f", result.greedyDSATURDifference) + "\n\n");
                }
            }
            System.out.println("All tests completed. Summary saved to " + summaryFilename);

            String avgFilename = resultsDirectory + "avg_results.txt";
            try (FileWriter writer = new FileWriter(avgFilename)) {
                writer.write("Average of " + totalRuns + " runs:\n\n");

                for (int i = 0; i < 33; i++) {
                    writer.write("Test " + (i + 1) + ":\n");
                    TestResults avgResults = avgTestResults.get(i);
                    avgResults.makeAverage(totalRuns);
                    writer.write("  - Total Time: " + String.format("%.3f", avgResults.totalTime) + " s\n");
                    writer.write("  - Backtracking Time: " + String.format("%.3f", avgResults.backtrackingTime) + " s\n");
                    writer.write("  - Greedy Degree Time: " + String.format("%.3f", avgResults.greedyDegreeTime * 1000) + " ms\n");
                    writer.write("  - Greedy DSATUR Time: " + String.format("%.3f", avgResults.greedyDSATURTime * 1000) + " ms\n");
                    writer.write("  - Greedy Degree Accuracy: " + String.format("%.3f", avgResults.greedyDegreeAccuracy) + "%\n");
                    writer.write("  - Greedy DSATUR Accuracy: " + String.format("%.3f", avgResults.greedyDSATURAccuracy) + "%\n");
                    writer.write("  - Greedy Degree Difference: " + String.format("%.3f", avgResults.greedyDegreeDifference) + "\n");
                    writer.write("  - Greedy DSATUR Difference: " + String.format("%.3f", avgResults.greedyDSATURDifference) + "\n\n");
                }
            }

        } catch (IOException e) {
            System.err.println("Error during evaluation: " + e.getMessage());
        }
    }

}
