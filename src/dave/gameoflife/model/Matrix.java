package dave.gameoflife.model;

public class Matrix {

    private final Cell[][] cells;
    private final boolean[][] nextCellStatus;
    private final int totalCols;
    private final int totalRows;
    private final int visibleCols;
    private final int visibleRows;
    private final Coordinate visibleStart;
    private final Coordinate visibleEnd;

    public Matrix(int cols, int rows, int visiblePortion) {
        this.totalCols = cols;
        this.totalRows = rows;
        this.cells = new Cell[this.totalCols][this.totalRows];
        this.nextCellStatus = new boolean[this.totalCols][this.totalRows];

        int totalCells = cols * rows;
        int visibleCells = totalCells * visiblePortion / 100;
        this.visibleCols = this.totalCols * visibleCells / totalCells;
        this.visibleRows = this.totalRows * visibleCells / totalCells;
        int visibleXStart = (this.totalCols - this.visibleCols) / 2;
        int visibleYStart = (this.totalRows - this.visibleRows) / 2;
        this.visibleStart = new Coordinate(visibleXStart, visibleYStart);
        int visibleXEnd = visibleXStart + this.visibleCols;
        int visibleYEnd = visibleYStart + this.visibleRows;
        this.visibleEnd = new Coordinate(visibleXEnd, visibleYEnd);
        this.traverse((x, y) -> {
            cells[x][y] = new Cell();
            cells[x][y].setVisible(
                    x >= visibleXStart && y >= visibleYStart && x < visibleXEnd && y < visibleYEnd
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

    public int getTotalCols() {
        return totalCols;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getVisibleCols() {
        return visibleCols;
    }

    public int getVisibleRows() {
        return visibleRows;
    }

    public Coordinate getVisibleStart() {
        return visibleStart;
    }

    public Coordinate getVisibleEnd() {
        return visibleEnd;
    }

    public void traverse(MatrixOperation op) {
        for (int x = 0; x < this.totalCols; x++) {
            for (int y = 0; y < this.totalRows; y++) {
                op.execute(x, y);
            }
        }
    }

    public void traverseX(ArrayOperation op) {
        for (int x = 0; x < this.totalCols; x++) {
            op.execute(x);
        }
    }

    public void traverseY(ArrayOperation op) {
        for (int y = 0; y < this.totalRows; y++) {
            op.execute(y);
        }
    }

    public static class Coordinate
    {
        int x;

        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
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
