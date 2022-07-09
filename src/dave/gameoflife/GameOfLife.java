package dave.gameoflife;

import dave.gameoflife.model.Cell;
import dave.gameoflife.model.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOfLife extends Frame {

    public static final int CELL_ROWS = 120;
    public static final int CELL_COLS = 200;
    public static final int CELL_SIZE = 10;
    public static final int MATRIX_VISIBILITY = 80;
    public static final int REGEN_EACH = 0;
    public static final int ZOOM_INCR = 2;

    private final int resX;
    private final int resY;
    private final Matrix matrix;
    private final Matrix visibleMatrix;
    private boolean started;
    private int cellSize;
    public GameOfLife() {
        this.matrix = new Matrix(CELL_COLS, CELL_ROWS, MATRIX_VISIBILITY);
        this.visibleMatrix = matrix.getVisibleMatrix();

        //setUndecorated(true);
        setTitle("Game of Life");
        this.cellSize = CELL_SIZE;
        this.resX = this.visibleMatrix.getCols() * this.cellSize;
        this.resY = this.visibleMatrix.getRows() * this.cellSize;
        setSize(this.resX, this.resY);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(Color.WHITE);
        setForeground(Color.lightGray);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point click = e.getPoint();
                Cell clickedCell = getCellByPoint(click.getX(), click.getY());
                clickedCell.setAlive(!clickedCell.isAlive());
                repaint();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 32 -> started = !started;                                          /* space */
                    case 37 -> matrix.setVisibleOffsetX(matrix.getVisibleOffsetX() + 1);    /* arrow left */
                    case 38 -> matrix.setVisibleOffsetY(matrix.getVisibleOffsetY() + 1);    /* arrow up */
                    case 39 -> matrix.setVisibleOffsetX(matrix.getVisibleOffsetX() - 1);    /* arrow right */
                    case 40 -> matrix.setVisibleOffsetY(matrix.getVisibleOffsetY() - 1);    /* arrow down */
                    case 107 -> {                                                           /* zoom in (char '+') */
                        cellSize += ZOOM_INCR;
                        repaint();
                    }
                    case 109 -> {                                                           /* zoom out (char '-') */
                        cellSize -= ZOOM_INCR;
                        repaint();
                    }
                }
            }
        });
    }

    private Matrix getMatrix() {
        return this.matrix;
    }

    private boolean isStarted() {
        return this.started;
    }

    private Cell getCellByPoint(double x, double y) {
        int cellX = (int) ((x - x % this.cellSize) / this.cellSize);
        int cellY = (int) ((y - y % this.cellSize) / this.cellSize);
        return this.visibleMatrix.getCell(cellX, cellY);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.lightGray);
        this.matrix.traverseX(x -> g.drawLine(x * this.cellSize, 0, x * this.cellSize, this.resY));
        this.matrix.traverseY(y -> g.drawLine(0, y * this.cellSize, this.resX, y * this.cellSize));

        g.setColor(Color.black);
        this.visibleMatrix.traverse((int x, int y) -> {
            Cell cell = this.visibleMatrix.getCell(x, y);
            if (cell.isAlive()) {
                int xPoint = (x + this.matrix.getVisibleOffsetX()) * this.cellSize + 1;
                int yPoint = (y + this.matrix.getVisibleOffsetY()) * this.cellSize + 1;
                g.fillRect(xPoint,yPoint, this.cellSize - 1, this.cellSize - 1);
            }
        });
    }

    private record PaintListener(GameOfLife gof) implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            gof.repaint();
            if (gof.isStarted())
                gof.getMatrix().nextGen();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GameOfLife gof = new GameOfLife();
            ActionListener listener = new PaintListener(gof);
            Timer t = new Timer(REGEN_EACH, listener);
            t.start();
        });
    }
}