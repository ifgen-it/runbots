package runbots;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

public class AutoBot {

    private int xBot;
    private int yBot;
    private int widthBot;
    private final int BOT_STEP = 4;

    private final Color COLOR_BOT_BLUE = Color.BLUE;
    private final Color COLOR_BOT_RED = Color.RED;

    private final Color COLOR_PAINT_BLUE = new Color(30, 144, 255);
    private final Color COLOR_PAINT_RED = new Color(250, 128, 114);

    private Color colorBot; // = COLOR_BOT_BLUE;
    private Color colorPaint; //= COLOR_PAINT_BLUE;
    private boolean workDone;

    private Desk desk;
    private boolean inDesk;
    private int deskRow = -1;
    private int deskColumn = -1;
    private boolean cellChanged = true;
    private Timer paintTimer;
    private final int PAINT_START_TIME = 1600;
    private final int PAINT_GRADE_TIME = 100;

    private BotsJFrame frame;
    private Graphics g2;

    private Coordinates autoBotTarget;

    public AutoBot(int xBot, int yBot, int widthBot, Color colorBot, Desk desk, BotsJFrame frame, Graphics g2) {

        this.xBot = xBot;
        this.yBot = yBot;
        this.widthBot = widthBot;
        this.desk = desk;
        this.frame = frame;
        this.g2 = g2;
        if (colorBot == COLOR_BOT_BLUE) {
            this.colorBot = COLOR_BOT_BLUE;
            this.colorPaint = COLOR_PAINT_BLUE;
        } else {
            this.colorBot = COLOR_BOT_RED;
            this.colorPaint = COLOR_PAINT_RED;
        }

        update();
        System.out.println("Bot created");
    }
    
    public Color getColorBot(){
        return colorBot;
    }
    
    public void setWorkDone(boolean workDone){
        this.workDone = workDone;
    }
    
    public boolean getWorkDone(){
        return workDone;
    }
    public void setAutoBotTarget(Coordinates autoBotTarget) {
        this.autoBotTarget = autoBotTarget;
    }
    
    public Coordinates getAutoBotTarget() {
        return autoBotTarget;
    }

    public void changeColorBot() {
        if (colorBot == COLOR_BOT_BLUE) {
            this.colorBot = COLOR_BOT_RED;
            this.colorPaint = COLOR_PAINT_RED;
        } else {
            this.colorBot = COLOR_BOT_BLUE;
            this.colorPaint = COLOR_PAINT_BLUE;
        }
        update();
    }

    public boolean isCellChanged() {
        return cellChanged;
    }

    public boolean isBotInDesk() {
        return inDesk;
    }

    public int getX() {
        return xBot;
    }

    public int getY() {
        return yBot;
    }

    private Coordinates getShift() {

        int x1 = xBot;
        int y1 = yBot;
        int x2 = autoBotTarget.getX();
        int y2 = autoBotTarget.getY();
        double dist = Math.pow((Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)), 0.5);
        if (dist < 3) {
            return null;
        }
        double dx = Math.round(BOT_STEP * (x2 - x1) / dist);
        double dy = Math.round(BOT_STEP * (y2 - y1) / dist);
        int dX = (int) dx;
        int dY = (int) dy;

        return new Coordinates(dX, dY);
    }

    public boolean move() {

        Coordinates shift = getShift();
        if (shift == null) {
            return false;
        }
        frame.repaint(xBot - widthBot / 2 - 5, yBot - widthBot / 2 - 5, widthBot + 10, widthBot + 10);

        xBot += shift.getX();
        yBot += shift.getY();

        inDesk = desk.isDesk(xBot, yBot);

        if (inDesk) {
            Cell cell = desk.getCell(xBot, yBot);

            int newDeskRow = cell.getRow();
            int newDeskCol = cell.getColumn();

            if (deskRow == newDeskRow && deskColumn == newDeskCol) {
                cellChanged = false;
            } else {
                cellChanged = true;
                deskRow = newDeskRow;
                deskColumn = newDeskCol;

                //System.out.println("paintTimer = " + paintTimer);
                if (paintTimer != null) {
                    paintTimer.cancel();
                }

                paintTimer = new Timer();
                paintTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        boolean isPainting = desk.paintDesk(deskRow, deskColumn, colorPaint);
                        if (isPainting) {
                            workDone = false;
                        }
                        else {
                            paintTimer.cancel();
                            workDone = true;
                        }
                    }
                },
                        PAINT_START_TIME, PAINT_GRADE_TIME);

            }

        } else {
            if (paintTimer != null) {
                paintTimer.cancel();
            }
        }

        update();
        return true;
    }

    public void update() {
        g2.setColor(colorBot);
        g2.fillOval(xBot - widthBot / 2, yBot - widthBot / 2, widthBot, widthBot);
    }

    public Color getRandomColor() {

        Color col = new Color(
                (int) Math.round(Math.random() * 255),
                (int) Math.round(Math.random() * 255),
                (int) Math.round(Math.random() * 255));
        return col;
    }

    public int getDeskRow() {
        return deskRow;
    }

    public int getDeskColumn() {
        return deskColumn;
    }

}
