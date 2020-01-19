package runbots;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Bot {

    private int xBot;
    private int yBot;
    private int widthBot;

    private final Color COLOR_BOT_BLUE = Color.BLUE;
    private final Color COLOR_BOT_RED = Color.RED;

    private final Color COLOR_PAINT_BLUE = new Color(30, 144, 255);
    private final Color COLOR_PAINT_RED = new Color(250, 128, 114);

    private Color colorBot; // = COLOR_BOT_BLUE;
    private Color colorPaint; //= COLOR_PAINT_BLUE;

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
    
    private List<Coordinates> autoBotRouteBlue;

    public Bot(int xBot, int yBot, int widthBot, Color colorBot, Desk desk, BotsJFrame frame, Graphics g2) {

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

    public void setAutoBotRouteBlue(List<Coordinates> autoBotRouteBlue){
        this.autoBotRouteBlue = autoBotRouteBlue;
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

    public void move(int dX, int dY) {

        //frame.repaint((int)(xBot - widthBot*0.75), (int)(yBot - widthBot*0.75), (int)(1.5*widthBot), (int)(1.5*widthBot));
        frame.repaint(xBot - widthBot / 2 - 5, yBot - widthBot / 2 - 5, widthBot + 10, widthBot + 10);

        xBot += dX;
        yBot += dY;
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
                        } else {
                            //System.out.println("isPainting = false");
                            updateAutoBotRoutes(xBot, yBot);
                            paintTimer.cancel();
                            
                        }
                    }
                },
                        PAINT_START_TIME, PAINT_GRADE_TIME);

//                desk.paintDesk(deskRow, deskColumn);
            }

        } else {
            if (paintTimer != null) {
                paintTimer.cancel();
            }
        }

        update();
        //colorBot = getRandomColor();
    }

    private void updateAutoBotRoutes(int xBot, int yBot){
        //System.out.println("update auto bot route blue, autoBotRouteBlue = " + autoBotRouteBlue);
        if (autoBotRouteBlue == null || autoBotRouteBlue.isEmpty()) {
            return;
        }
        
        Cell paintedCell = desk.getCell(xBot, yBot);
        System.out.println("painted cell = " + paintedCell);
        
        Coordinates coord = desk.getCellCenterCoord(paintedCell.getRow(), paintedCell.getColumn());
        
        
        if (autoBotRouteBlue.contains(coord)) {
            autoBotRouteBlue.remove(coord);
        }
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
