package dave.gameoflife.model;

import static dave.gameoflife.Constants.*;

public class Matrix {

    private final Cell[][] cells;
    private final boolean[][] nextCellStatus;

    public Matrix() {
        this.cells = new Cell[CELL_COLS][CELL_ROWS];
        this.nextCellStatus = new boolean[CELL_COLS][CELL_ROWS];

        this.init();
    }

    private void init() {
        this.traverse((x, y) -> {
            cells[x][y] = new Cell();
            cells[x][y].setVisible(
                    x >= MATRIX_BUFFER && y >= MATRIX_BUFFER &&
                    x < (CELL_COLS - MATRIX_BUFFER) && y < (CELL_ROWS - MATRIX_BUFFER)
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

    public Cell getCellByPoint(double x, double y) {
        int cellX = (int) ((x - x % CELL_SIZE) / CELL_SIZE);
        int cellY = (int) ((y - y % CELL_SIZE) / CELL_SIZE);
        return this.getCell(cellX, cellY);
    }

    public void traverse(MatrixOperation op) {
        for (int x = 0; x < CELL_COLS; x++) {
            for (int y = 0; y < CELL_ROWS; y++) {
                op.execute(x, y);
            }
        }
    }

    public void traverseX(ArrayOperation op) {
        for (int x = 0; x < CELL_COLS; x++) {
            op.execute(x);
        }
    }

    public void traverseY(ArrayOperation op) {
        for (int y = 0; y < CELL_ROWS; y++) {
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
