package dave.gameoflife.model;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    private boolean alive;
    private boolean visible;
    private final List<Cell> neighbors;

    public Cell() {
        this.neighbors = new ArrayList<>();
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void addNeighbor(Cell cell) {
        this.neighbors.add(cell);
    }

    public boolean willBeAlive() {
        int aliveNeighbors = this.neighbors.stream()
                .filter(Cell::isAlive)
                .toList().size();
        return
                // any live cell with two or three live neighbours survives
                (this.isAlive() && (aliveNeighbors == 2 || aliveNeighbors == 3)) ||
                // any dead cell with three live neighbours becomes a live cell
                (!this.isAlive() && (aliveNeighbors == 3));
    }

}
