package dave.gameoflife.model;

import static java.math.BigDecimal.valueOf;

public class Matrix {

    private final Cell[][] cells;
    private final boolean[][] nextCellStatus;
    private final int cols;
    private final int rows;
    private Matrix visibleMatrix;
    private int visibleOffsetX;
    private int visibleOffsetY;

    public Matrix(int cols, int rows, int visiblePortion) {
        this.cols = cols;
        this.rows = rows;
        this.cells = new Cell[this.cols][this.rows];
        this.nextCellStatus = new boolean[this.cols][this.rows];

        this.traverse((x, y) -> cells[x][y] = new Cell());

        visiblePortion = Math.min(visiblePortion, 100);
        visiblePortion = Math.max(visiblePortion, 0);
        if (visiblePortion == 100)
            return;
        int totalCells = cols * rows;
        int visibleCells = totalCells * visiblePortion / 100;
        int visibleCols = valueOf(this.cols).multiply(valueOf(visibleCells)).divide(valueOf(totalCells)).intValue();
        int visibleRows = valueOf(this.rows).multiply(valueOf(visibleCells)).divide(valueOf(totalCells)).intValue();
        this.visibleMatrix = new Matrix(visibleCols, visibleRows, 100);

        int visibleXStart = (this.cols - visibleCols) / 2;
        int visibleYStart = (this.rows - visibleRows) / 2;
        int visibleXEnd = visibleXStart + visibleCols;
        int visibleYEnd = visibleYStart + visibleRows;
        this.traverse((x, y) -> {
            Cell currCell = cells[x][y];
            if (x >= visibleXStart && y >= visibleYStart && x < visibleXEnd && y < visibleYEnd)
                this.visibleMatrix.setCell(x - visibleXStart, y - visibleYStart, currCell);

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

    public Matrix getVisibleMatrix() {
        return visibleMatrix;
    }

    public Cell getCell(int x, int y) {
        return this.cells[x][y];
    }

    public void setCell(int x, int y, Cell cell) {
        this.cells[x][y] = cell;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getVisibleOffsetX() {
        return visibleOffsetX;
    }

    public void setVisibleOffsetX(int visibleOffsetX) {
        this.visibleOffsetX = visibleOffsetX;
    }

    public int getVisibleOffsetY() {
        return visibleOffsetY;
    }

    public void setVisibleOffsetY(int visibleOffsetY) {
        this.visibleOffsetY = visibleOffsetY;
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
