import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF topVIndex;
    private final WeightedQuickUnionUF topBottomVIndices;
    private boolean[] grid;
    private final int width;
    private final int bottomIndex;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be bigger than 0");
        } else {
            this.grid = new boolean[n * n + 2];
            this.bottomIndex = n * n + 1;
            this.grid[0] = true;
            this.grid[this.bottomIndex] = true;
            this.width = n;
            this.topBottomVIndices = new WeightedQuickUnionUF(n * n + 2);
            this.topVIndex = new WeightedQuickUnionUF(n * n + 1);
        }
    }

    // converts (row, col) to the equivalent index in the array
    private int getIndex(int row, int col) {
        return col + this.width * (row - 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row >= 1 && col >= 1 && row <= this.width && col <= this.width) {
            this.grid[this.getIndex(row, col)] = true;
            if (row == 1) {
                this.topVIndex.union(this.getIndex(row, col), 0);
                this.topBottomVIndices.union(this.getIndex(row, col), 0);
            } else if (row == this.width) {
                this.topBottomVIndices.union(this.getIndex(row, col), this.bottomIndex);
            }

            this.tryConnect(row, col, -1, 0);
            this.tryConnect(row, col, 0, -1);
            this.tryConnect(row, col, 1, 0);
            this.tryConnect(row, col, 0, 1);
        } else {
            throw new IllegalArgumentException("Out of bounds.");
        }
    }

    private void tryConnect(int row, int col, int offr, int offc) {
        if (0 < row + offr && row + offr <= this.width && 0 < col + offc && col + offc <= this.width && this.isOpen(row + offr, col + offc)) {
            this.topVIndex.union(this.getIndex((row + offr), (col + offc)), this.getIndex(row, col));
            this.topBottomVIndices.union(this.getIndex((row + offr), (col + offc)), this.getIndex(row, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row >= 1 && col >= 1 && row <= this.width && col <= this.width) {
            return this.grid[this.getIndex(row, col)];
        } else {
            throw new IllegalArgumentException("Out of bounds.");
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row >= 1 && col >= 1 && row <= this.width && col <= this.width) {
            return this.topVIndex.connected(this.getIndex(row, col), 0);
        } else {
            throw new IllegalArgumentException("Out of bounds.");
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int opened = 0;

        for (int i = 1; i <= this.width * this.width; i++) {
            if(this.grid[i]) {
                opened++;
            }
        }

        return opened;
    }

    // does the system percolate?
    public boolean percolates() {
        // 0 -> top index
        return this.topBottomVIndices.connected(0, this.bottomIndex);
    }

    // test client (optional)
    public static void main(String[] args) {
        System.out.println("Use either PercolationStats or InteractivePercolationVisualizer.");
    }
}