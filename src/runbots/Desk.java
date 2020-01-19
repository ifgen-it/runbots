package runbots;

import java.awt.Color;
import java.awt.Graphics;

public class Desk {

    private int xStart;
    private int yStart;

    private int xFinish;
    private int yFinish;

    private int cellWidth;
    private int rows;
    private int columns;

    private BotsJFrame frame;

    private final Color COLOR_BORDER = Color.ORANGE;
    private final Color COLOR_PAINT_BLUE = new Color(30, 144, 255);
    private final Color COLOR_PAINT_RED = new Color(250, 128, 114);

    //private Color colorPaint = COLOR_PAINT_BLUE;
    private DeskCell[][] deskPainted;
    private final int MAX_PAINT_GRADE = 38;

    private Graphics g2;

    public Desk(int xStart, int yStart, int cellWidth, int rows, int columns) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.cellWidth = cellWidth;
        this.rows = rows;
        this.columns = columns;
        this.g2 = g2;

        this.xFinish = xStart + cellWidth * columns;
        this.yFinish = yStart + cellWidth * rows;

        deskPainted = new DeskCell[rows][];
        Color wantedColor = COLOR_PAINT_BLUE;
        for (int i = 0; i < rows; i++) {
            deskPainted[i] = new DeskCell[columns];
            for (int j = 0; j < columns; j++) {
                deskPainted[i][j] = new DeskCell();
                deskPainted[i][j].setWantedColor(wantedColor);
                wantedColor = wantedColor == COLOR_PAINT_BLUE ? COLOR_PAINT_RED : COLOR_PAINT_BLUE;
            }
        }
    }

    public void setGraphics(Graphics g2) {
        this.g2 = g2;
    }

    public void setFrame(BotsJFrame frame) {
        this.frame = frame;
        System.out.println("frame = " + frame);
    }

    public Coordinates getCellStartCoord(int row, int col) {

        int x = xStart + col * cellWidth;
        int y = yStart + row * cellWidth;
        return new Coordinates(x, y);
    }
    
    public Coordinates getCellCenterCoord(int row, int col) {

        int x = xStart + col * cellWidth + cellWidth/2;
        int y = yStart + row * cellWidth + cellWidth/2;
        return new Coordinates(x, y);
    }

    public boolean paintDesk(int row, int col, Color colorPaint) {
        //System.out.println("colorPaint = " + colorPaint);
        //System.out.println("wantedColor = " + deskPainted[row][col].getWantedColor());
        
        if (!deskPainted[row][col].getWantedColor().equals(colorPaint)) {
            return false;
        }
        
        int newGrade = deskPainted[row][col].getGrade() + 1;
        if (newGrade > MAX_PAINT_GRADE) {
            deskPainted[row][col].setGrade(MAX_PAINT_GRADE);
            return false;
        }
        deskPainted[row][col].setGrade(newGrade);

        Coordinates startCoord = getCellStartCoord(row, col);
        frame.repaint(startCoord.getX(), startCoord.getY(), cellWidth, cellWidth);
        return true;
    }

    public Cell getCell(int x, int y) {
        if (!isDesk(x, y)) {
            return null;
        }
        int col = (x - xStart) / cellWidth;
        int row = (y - yStart) / cellWidth;
        return new Cell(row, col);
    }

    public boolean isDesk(int x, int y) {
        return x > xStart && x < xFinish && y > yStart && y < yFinish;
    }

    public void update() {
        g2.setColor(COLOR_BORDER);

        int yTempStart = yStart;
        for (int i = 0; i < rows; i++) {
            int xTempStart = xStart;
            for (int j = 0; j < columns; j++) {
                g2.setColor(COLOR_BORDER);
                g2.drawRect(xTempStart, yTempStart, cellWidth, cellWidth);
                if (deskPainted[i][j].getGrade() > 0) {
                    g2.setColor(deskPainted[i][j].getWantedColor());
                    int fillXStart = xTempStart + 2;
                    int fillYStart = yTempStart + 2;
                    int fillXWidth = cellWidth - 4;
                    int fillYWidth = (int) (((cellWidth - 4) / (float) MAX_PAINT_GRADE) * deskPainted[i][j].getGrade());

                    g2.fillRect(fillXStart, fillYStart, fillXWidth, fillYWidth);
                }
                xTempStart += cellWidth;
            }
            yTempStart += cellWidth;
        }

    }

}
