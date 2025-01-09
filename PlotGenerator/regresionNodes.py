import pandas as pd
from sklearn.linear_model import LinearRegression
from scipy.optimize import curve_fit
import numpy as np
import matplotlib.pyplot as plt

# Define the data
data = [
    (4, 0.008, 5.0, 2.157, 0.836),
    (4, 0.004, 2.0, 1.086, 0.680),
    (4, 0.003, 2.0, 1.027, 0.789),
    (5, 0.004, 2.0, 0.995, 0.833),
    (4, 0.004, 3.0, 0.859, 0.685),
    (5, 0.004, 2.0, 1.049, 0.822),
    (4, 0.003, 2.0, 0.717, 0.463),
    (5, 0.004, 2.0, 0.974, 0.684),
    (4, 0.005, 4.0, 0.959, 0.709),
    (5, 0.013, 11.0, 1.158, 0.885),
    (12, 0.032, 27.0, 3.164, 2.157),
    (15, 0.031, 25.0, 3.448, 2.104),
    (20, 2.571, 2560.0, 7.660, 3.916),
    (25, 13.023, 13011.0, 7.112, 5.369),
    (30, 1.632, 1613.0, 9.901, 8.872),
    (32, 71.073, 71054.0, 11.655, 7.448),
    (45, 0.037, 5.0, 19.320, 12.732),
    (50, 0.044, 5.0, 17.722, 21.170),
    (55, 0.050, 7.0, 20.382, 22.611),
    (60, 0.056, 7.0, 21.592, 27.470),
    (16, 55.315, 55303.0, 6.127, 5.664),
    (18, 17.682, 17671.0, 5.632, 5.975),
    (20, 1.614, 1601.0, 6.730, 7.166),
    (22, 640.642, 640629.0, 6.761, 6.729),
    (15, 0.020, 16.0, 2.384, 1.776),
    (18, 0.012, 7.0, 2.866, 2.132),
    (20, 0.009, 2.0, 3.849, 2.606),
    (25, 0.046, 37.0, 5.519, 2.775),
    (30, 14.320, 14309.0, 5.762, 4.967),
    (22, 3.853, 3845.0, 4.356, 3.272),
    (30, 0.164, 153.0, 5.968, 4.305),
    (25, 0.365, 353.0, 6.620, 5.844),
    (31, 0.021, 9.0, 7.433, 4.581),
]

# Convert data to DataFrame
columns = ["NrNodes", "TotalTime_s", "BacktrackingTime_ms", "GreedyDegreeTime_ms", "GreedyDSATURTime_ms"]
df = pd.DataFrame(data, columns=columns)

# Filter out unwanted rows
excluded_nodes = [16, 31, 45, 50, 55, 60]
filtered_df = df[~df["NrNodes"].isin(excluded_nodes)]

# Prepare data for regression
X = filtered_df["NrNodes"].values.reshape(-1, 1)
y_backtracking = filtered_df["BacktrackingTime_ms"].values
y_greedy_degree = filtered_df["GreedyDegreeTime_ms"].values
y_greedy_dsat = filtered_df["GreedyDSATURTime_ms"].values

# Perform regression for each algorithm
# Exponential fit for Backtracking
def exponential_func(x, a, b, c):
    return a * np.exp(b * x) + c

params, _ = curve_fit(exponential_func, filtered_df["NrNodes"], y_backtracking, p0=(1, 0.01, 1))

reg_greedy_degree = LinearRegression().fit(X, y_greedy_degree)
reg_greedy_dsat = LinearRegression().fit(X, y_greedy_dsat)

# Coefficients for the models
# Plotting the regression results
nodes_range = np.linspace(0, 100, 500).reshape(-1, 1)

plt.figure(figsize=(15, 5))

# Backtracking
plt.subplot(1, 3, 1)
plt.scatter(filtered_df["NrNodes"], y_backtracking, color='blue', label='Actual')
plt.plot(nodes_range, exponential_func(nodes_range, *params), color='red', label='Exponential Fit')
plt.title("Backtracking")
plt.xlabel("Number of Nodes")
plt.ylabel("Time (ms)")
plt.legend()

# Greedy Degree
plt.subplot(1, 3, 2)
plt.scatter(filtered_df["NrNodes"], y_greedy_degree, color='blue', label='Actual')
plt.plot(nodes_range, reg_greedy_degree.predict(nodes_range), color='red', label='Linear Fit')
plt.title("Greedy Degree")
plt.xlabel("Number of Nodes")
plt.ylabel("Time (ms)")
plt.legend()

# Greedy DSATUR
plt.subplot(1, 3, 3)
plt.scatter(filtered_df["NrNodes"], y_greedy_dsat, color='blue', label='Actual')
plt.plot(nodes_range, reg_greedy_dsat.predict(nodes_range), color='red', label='Linear Fit')
plt.title("Greedy DSATUR")
plt.xlabel("Number of Nodes")
plt.ylabel("Time (ms)")
plt.legend()

plt.tight_layout()
plt.show()
