package dave.gameoflife;

import dave.gameoflife.model.Cell;
import dave.gameoflife.model.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static dave.gameoflife.Constants.*;

public class GameOfLife extends Frame {
    private final Matrix matrix;
    private boolean started;
    public GameOfLife() {
        this.matrix = new Matrix();

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
                Cell clickedCell = matrix.getCellByPoint(click.getX(), click.getY());
                clickedCell.setAlive(!clickedCell.isAlive());
                if (clickedCell.isVisible()) repaint();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    started = !started;
                }
            }
        });
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public boolean isStarted() {
        return this.started;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(MATRIX_BUFFER * CELL_SIZE, MATRIX_BUFFER * CELL_SIZE,
                RES_X - MATRIX_BUFFER * CELL_SIZE * 2, RES_Y - MATRIX_BUFFER * CELL_SIZE * 2);

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

    record PaintListener(GameOfLife gof) implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (!gof.isStarted()) return;
            gof.repaint();
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