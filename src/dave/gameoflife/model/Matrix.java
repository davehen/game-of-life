package dave.gameoflife.model;

public class Matrix {

    private final Cell[][] cells;
    private final boolean[][] nextCellStatus;
    private final int cols;
    private final int rows;
    private final int notVisibleBuffer;

    public Matrix(int cols, int rows, int notVisibleBuffer) {
        this.cols = cols;
        this.rows = rows;
        this.notVisibleBuffer = notVisibleBuffer;
        this.cells = new Cell[this.cols][this.rows];
        this.nextCellStatus = new boolean[this.cols][this.rows];

        this.traverse((x, y) -> {
            cells[x][y] = new Cell();
            cells[x][y].setVisible(
                    x >= this.notVisibleBuffer && y >= this.notVisibleBuffer &&
                    x < (this.cols - this.notVisibleBuffer) && y < (this.rows - this.notVisibleBuffer)
            );
        });

        this.traverse((x, y) -> {
            Cell currCell = cells[x][y];
            try { currCell.addNeighbor(this.getCell(x, y - 1)); } catch (ArrayIndexOutOfBoundsException ignore) {}
            try { currCell.addNeighbor(this.getCell(x + 1, y - 1)); } catch (ArrayIndexOutOfBoundsException ignore) {}
            try { currCell.addNeighbor(this.getCell(x + 1, y)); } catch (ArrayIndexOutOfBoundsException ignore) {}
            try { currCell.addNeighbor(this.getCell(x + 1, y + 1)); } catch (ArrayIndexOutOfBoundsException ignore) {}
            try { currCell.addNeighbor(this.getCell(x, y + 1)); } catch (ArrayIndexOutOfBoundsException ignore) {}
            try { currCell.addNeighbor(this.getCell(x - 1, y + 1)); } catch (ArrayIndexOutOfBoundsException ignore) {}
            try { currCell.addNeighbor(this.getCell(x - 1, y)); } catch (ArrayIndexOutOfBoundsException ignore) {}
            try { currCell.addNeighbor(this.getCell(x - 1, y - 1)); } catch (ArrayIndexOutOfBoundsException ignore) {}
        });
    }

    public void nextGen() {
        this.traverse((x, y) -> nextCellStatus[x][y] = cells[x][y].willBeAlive());
        this.traverse((x, y) -> cells[x][y].setAlive(nextCellStatus[x][y]));
    }

    public Cell getCell(int x, int y) {
        return this.cells[x][y];
    }

    public void traverse(MatrixOperation op) {
        for (int x = 0; x < this.cols; x++) {
            for (int y = 0; y < this.rows; y++) {
                op.execute(x, y);
            }
        }
    }

    public void traverseX(ArrayOperation op) {
        for (int x = 0; x < this.cols; x++) {
            op.execute(x);
        }
    }

    public void traverseY(ArrayOperation op) {
        for (int y = 0; y < this.rows; y++) {
            op.execute(y);
        }
    }

    @FunctionalInterface
    public interface MatrixOperation
    {
        void execute(int x, int y);
    }

    @FunctionalInterface
    public interface ArrayOperation
    {
        void execute(int n);
    }

}
