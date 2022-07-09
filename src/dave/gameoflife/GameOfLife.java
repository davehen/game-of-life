package dave.gameoflife;

import dave.gameoflife.model.Cell;
import dave.gameoflife.model.Matrix;
import dave.gameoflife.model.Matrix.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOfLife extends Frame {

    public static final int CELL_ROWS = 100;
    public static final int CELL_COLS = 200;
    public static final int CELL_SIZE = 10;
    public static final int MATRIX_VISIBILITY = 80;
    public static final int REGEN_EACH = 10;
    public static final int RES_X = CELL_COLS * CELL_SIZE;
    public static final int RES_Y = CELL_ROWS * CELL_SIZE;

    private final Matrix matrix;
    private boolean started;
    public GameOfLife() {
        this.matrix = new Matrix(CELL_COLS, CELL_ROWS, MATRIX_VISIBILITY);

        setUndecorated(true);
        setTitle("Game of Life");
        setSize(RES_X, RES_Y);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(Color.lightGray);
        setForeground(Color.lightGray);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point click = e.getPoint();
                Cell clickedCell = getCellByPoint(click.getX(), click.getY());
                clickedCell.setAlive(!clickedCell.isAlive());
                if (clickedCell.isVisible()) repaint();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());
                switch (e.getKeyCode()) {
                    /* space */         case 32 -> started = !started;
                    /* arrow left */    case 37 -> started = !started;
                    /* arrow up */      case 38 -> started = !started;
                    /* arrow right */   case 39 -> started = !started;
                    /* arrow down */    case 40 -> started = !started;
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
        int cellX = (int) ((x - x % CELL_SIZE) / CELL_SIZE);
        int cellY = (int) ((y - y % CELL_SIZE) / CELL_SIZE);
        return this.matrix.getCell(cellX, cellY);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(this.matrix.getVisibleStart().getX() * CELL_SIZE, this.matrix.getVisibleStart().getY() * CELL_SIZE,
                this.matrix.getVisibleCols() * CELL_SIZE, this.matrix.getVisibleRows() * CELL_SIZE);

        g.setColor(Color.lightGray);
        matrix.traverseX(x -> g.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, RES_Y));
        matrix.traverseY(y -> g.drawLine(0, y * CELL_SIZE, RES_X, y * CELL_SIZE));

        g.setColor(Color.black);
        matrix.traverse((x, y) -> {
            Cell cell = matrix.getCell(x, y);
            if (cell.isAlive() && cell.isVisible()) {
                g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 1, CELL_SIZE - 1);
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