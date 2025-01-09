% MATLAB Implementation
% Define data in MATLAB
nodes = [4, 4, 4, 5, 4, 5, 4, 5, 4, 5, 12, 15, 20, 25, 30, 32, 20, 25, 30];
backtracking_times = [0.005, 0.002, 0.002, 0.002, 0.003, 0.002, 0.002, 0.002, 0.004, 0.011, 0.027, 0.025, 2.560, 13.011, 1.613, 71.054, 1.601, 0.037, 14.309];

greedy_degree_times = [2.157, 1.086, 1.027, 0.995, 0.859, 1.049, 0.717, 0.974, 0.959, 1.158, 3.164, 3.448, 7.660, 7.112, 9.901, 11.655, 6.730, 5.519, 5.762];

greedy_dsat_times = [0.836, 0.680, 0.789, 0.833, 0.685, 0.822, 0.463, 0.684, 0.709, 0.885, 2.157, 2.104, 3.916, 5.369, 8.872, 7.448, 7.166, 2.775, 4.967];

% Exponential Fit for Backtracking
exp_fit = fit(nodes', backtracking_times', 'exp2');

% Plot Backtracking
figure;
scatter(nodes, backtracking_times, 'b');
hold on;
plot(exp_fit, 'r');
title('Backtracking Time (s)');
xlabel('Number of Nodes');
ylabel('Time (s)');
legend('Actual', 'Exponential Fit');

% Linear Fit for Greedy Degree Times
linear_fit_gd = fit(nodes', greedy_degree_times', 'poly1');

% Plot Greedy Degree
figure;
scatter(nodes, greedy_degree_times, 'b');
hold on;
plot(linear_fit_gd, 'r');
title('Greedy Degree Time (ms)');
xlabel('Number of Nodes');
ylabel('Time (ms)');
legend('Actual', 'Linear Fit');

% Linear Fit for Greedy DSATUR Times
linear_fit_gds = fit(nodes', greedy_dsat_times', 'poly1');

% Plot Greedy DSATUR
figure;
scatter(nodes, greedy_dsat_times, 'b');
hold on;
plot(linear_fit_gds, 'r');
title('Greedy DSATUR Time (ms)');
xlabel('Number of Nodes');
ylabel('Time (ms)');
legend('Actual', 'Linear Fit');
