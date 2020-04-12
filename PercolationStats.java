import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double estimates[];

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n > 0 && trials > 0) {
            this.estimates = new double[trials];

            for (int trial = 0; trial < trials; ++trial) {
                Percolation percolation = new Percolation(n);

                while (!percolation.percolates()) {
                    int row = StdRandom.uniform(1, n + 1);
                    int col = StdRandom.uniform(1, n + 1);
                    if (!percolation.isOpen(row, col)) {
                        percolation.open(row, col);
                    }
                }

                this.estimates[trial] = (double)percolation.numberOfOpenSites() / (double)(n * n);
            }

        } else {
            throw new IllegalArgumentException();
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.estimates);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.estimates);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - (1.96 * stddev()) / Math.sqrt(this.estimates.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + (1.96 * stddev()) / Math.sqrt(this.estimates.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        StdOut.println("mean                    =  " + stats.mean());
        StdOut.println("stddev                  =  " + stats.stddev());
        StdOut.println("95% confidence interval =  [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }

}
