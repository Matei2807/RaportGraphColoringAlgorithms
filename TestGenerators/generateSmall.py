def save_graph_to_file(filename, adjacency_list):
    with open(filename, "w") as f:
        for node, neighbors in adjacency_list.items():
            line = f"{node}: {' '.join(map(str, neighbors))}\n"
            f.write(line)

# lista de adiacenta(creeata cu AI)
graphs = {
    "C4": {0: [1, 3], 1: [0, 2], 2: [1, 3], 3: [0, 2]},  # Cycle graph with 4 nodes
    "C5": {0: [1, 4], 1: [0, 2], 2: [1, 3], 3: [2, 4], 4: [0, 3]},  # Cycle graph with 5 nodes
    "S4": {0: [1, 2, 3], 1: [0], 2: [0], 3: [0]},  # Star graph with 4 nodes
    "S5": {0: [1, 2, 3, 4], 1: [0], 2: [0], 3: [0], 4: [0]},  # Star graph with 5 nodes
    "K4": {0: [1, 2, 3], 1: [0, 2, 3], 2: [0, 1, 3], 3: [0, 1, 2]},  # Complete graph with 4 nodes
    "K5": {0: [1, 2, 3, 4], 1: [0, 2, 3, 4], 2: [0, 1, 3, 4], 3: [0, 1, 2, 4], 4: [0, 1, 2, 3]},  # Complete graph with 5 nodes
    "Random1": {0: [1, 2], 1: [0, 3], 2: [0], 3: [1]},  # Random graph with 4 nodes
    "Random2": {0: [1], 1: [0, 2, 3], 2: [1], 3: [1]},  # Random graph with 4 nodes
    "Random3": {0: [1, 2], 1: [0, 3], 2: [0, 3], 3: [1, 2]},  # Random graph with 4 nodes
    "Random4": {0: [1, 2], 1: [0, 3], 2: [0, 3], 3: [1, 2, 4], 4: [3]},  # Random graph with 5 nodes
}

for name, adjacency_list in graphs.items():
    filename = f"{name}.txt"
    save_graph_to_file(filename, adjacency_list)
