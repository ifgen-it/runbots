package runbots;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class BotsJFrame extends JFrame implements KeyListener {

    // CONST
    private final int FORM_WIDTH = 660;
    private final int FORM_HEIGHT = 560;
    private final int CELL_WIDTH = 80;
    private final int X_MARGIN = 40;
    private final int Y_MARGIN = 60;
    private final int ROWS = 5;
    private final int COLUMNS = 5;

    private final int BOT_WIDTH = CELL_WIDTH / 2;

    private final int BOT_STEP = 4;
    private final int BOT_MINI_STEP = (int) Math.round((BOT_STEP / Math.pow(2, 0.5)));

    private final int Y_START_AUTO_BOT = Y_MARGIN + CELL_WIDTH * COLUMNS + CELL_WIDTH / 2;
    
    private final int X_START_AUTO_BOT_BLUE_1 = X_MARGIN + CELL_WIDTH / 2;
    private final int X_START_AUTO_BOT_BLUE_2 = X_MARGIN + CELL_WIDTH / 2 + BOT_WIDTH * 2;
    
    private final int X_START_AUTO_BOT_RED_1 = X_MARGIN + CELL_WIDTH / 2 + BOT_WIDTH * 4;
    private final int X_START_AUTO_BOT_RED_2 = X_MARGIN + CELL_WIDTH / 2 + BOT_WIDTH * 6;

    private final int X_START_BOT_2 = X_START_AUTO_BOT_BLUE_1 + CELL_WIDTH * 3;
    private final int Y_START_BOT = Y_MARGIN + CELL_WIDTH * COLUMNS + CELL_WIDTH / 2;

    // VAR DESK
    private Container contentPane;
    private JPanel panel;
    private BotsJFrame frame;
    private final Color BACKGROUND_COLOR = new Color(42, 113, 45);

    private final Color COLOR_BOT_BLUE = Color.BLUE;
    private final Color COLOR_BOT_RED = Color.RED;

    private JButton btnChangeColor;
    private JButton btnCreateBot;
    private JButton btnStartBot;
    private JButton btnReset;
    private JButton btnChangeBotColor;
    private JButton btnCreateBot2;

    private JLabel labelInfo;
    private JLabel labelStatus;
    private JLabel labelColor;

    // VAR OBJECTS
    private AutoBot autoBotBlue1;
    private AutoBot autoBotBlue2;
    private AutoBot autoBotRed1;
    private AutoBot autoBotRed2;

    private Bot bot2;
    private Thread threadAutoBot1;
    private Thread threadBot2;
    private List<Coordinates> autoBotRouteBlue = new LinkedList<>();
    private List<Coordinates> autoBotRouteRed = new LinkedList<>();

    private Desk desk;

    private boolean isLeft2;
    private boolean isRight2;
    private boolean isUp2;
    private boolean isDown2;

    public BotsJFrame() {

        this.setTitle("Running bots");
        Dimension SIZE = new Dimension(FORM_WIDTH, FORM_HEIGHT);
        Point p1 = new Point(450, 100);
        this.setSize(SIZE);
        this.setLocation(p1);

        init();

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame = this;
        this.addKeyListener(this);
        this.setVisible(true);
        this.setFocusable(true);
    }

    private void init() {

        // CREATE DESK
        desk = new Desk(X_MARGIN, Y_MARGIN, CELL_WIDTH, ROWS, COLUMNS);

        // GUI
        contentPane = this.getContentPane();

        panel = new JPanel();
        contentPane.add(panel);
        panel.setBackground(BACKGROUND_COLOR);
        panel.setLayout(null);
        panel.setDoubleBuffered(true);

        // BUTTONS
        btnChangeColor = new JButton("Change form color");
        btnChangeColor.setSize(140, 30);
        btnChangeColor.setLocation(470, 40);
        panel.add(btnChangeColor);

        btnCreateBot = new JButton("Create auto bot");
        btnCreateBot.setSize(140, 30);
        btnCreateBot.setLocation(470, 70);
        panel.add(btnCreateBot);

        btnStartBot = new JButton("Start auto bot");
        btnStartBot.setSize(140, 30);
        btnStartBot.setLocation(470, 100);
        panel.add(btnStartBot);

        btnReset = new JButton("Reset");
        btnReset.setSize(140, 30);
        btnReset.setLocation(470, 130);
        panel.add(btnReset);

        btnChangeBotColor = new JButton("Change bot color");
        btnChangeBotColor.setSize(140, 30);
        btnChangeBotColor.setLocation(470, 160);
        panel.add(btnChangeBotColor);

        btnCreateBot2 = new JButton("Create bot");
        btnCreateBot2.setSize(140, 30);
        btnCreateBot2.setLocation(470, 190);
        panel.add(btnCreateBot2);

        labelInfo = new JLabel("Info");
        labelInfo.setSize(200, 50);
        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
        labelInfo.setFont(f);
        labelInfo.setLocation(470, 250);
        labelInfo.setForeground(Color.ORANGE);
        panel.add(labelInfo);

        labelStatus = new JLabel("Status");
        labelStatus.setSize(200, 50);
        labelStatus.setFont(f);
        labelStatus.setLocation(470, 280);
        labelStatus.setForeground(Color.ORANGE);
        panel.add(labelStatus);

        labelColor = new JLabel("Color");
        labelColor.setSize(200, 50);
        labelColor.setFont(f);
        labelColor.setLocation(470, 310);
        labelColor.setForeground(Color.ORANGE);
        panel.add(labelColor);

        // ====  HANDLERS  =======
        btnChangeColor.addActionListener((ActionEvent e) -> {
            Color col = BotsJFrame.getRandomColor();
            panel.setBackground(col);

            int red = col.getRed();
            int green = col.getGreen();
            int blue = col.getBlue();
            String strColor = "(" + red + "," + green + "," + blue + ")";
            labelColor.setText(strColor);
            repaint();
            requestFocusInWindow();
        });

        btnCreateBot.addActionListener(((ActionEvent e) -> {
            createAutoBot();
            requestFocusInWindow();
        }));

        btnCreateBot2.addActionListener(((ActionEvent e) -> {
            createBot();
            requestFocusInWindow();
        }));

        btnStartBot.addActionListener(((ActionEvent e) -> {
            startAutoBot();
            requestFocusInWindow();
        }));

        btnReset.addActionListener(((ActionEvent e) -> {
            /*desk = new Desk(X_MARGIN, Y_MARGIN, CELL_WIDTH, ROWS, COLUMNS);
            bot1 = new Bot(X_START_BOT, Y_START_BOT, BOT_WIDTH, COLOR_BOT_BLUE, desk, frame, getGraphics());
            repaint();*/
            requestFocusInWindow();
        }));

        btnChangeBotColor.addActionListener(((ActionEvent e) -> {
            if (bot2 != null) {
                bot2.changeColorBot();
            }
            requestFocusInWindow();
        }));

        // WINDOW CLOSING
        this.addWindowListener(new FrameCloser(this));

    } // INIT

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        desk.setGraphics(g2);

        // DRAWING DESK
        BasicStroke penDesk = new BasicStroke(2);
        g2.setStroke(penDesk);

        desk.update();

        // DRAWING BOT
        if (autoBotBlue1 != null) {
            autoBotBlue1.update();
        }
        if (autoBotBlue2 != null) {
            autoBotBlue2.update();
        }
        if (autoBotRed1 != null) {
            autoBotRed1.update();
        }
        if (autoBotRed2 != null) {
            autoBotRed2.update();
        }

        if (bot2 != null) {
            bot2.update();
        }
    }

    public static Color getRandomColor() {

        Color col = new Color(
                (int) Math.round(Math.random() * 255),
                (int) Math.round(Math.random() * 255),
                (int) Math.round(Math.random() * 255));
        return col;
    }

    public void updateBotStatus() {
        if (autoBotBlue1 == null) {
            return;
        }

        if (autoBotBlue1.isBotInDesk()) {
            int row = autoBotBlue1.getDeskRow();
            int col = autoBotBlue1.getDeskColumn();
            labelStatus.setText("In the Desk: [" + row + "][" + col + "]");

        } else {
            labelStatus.setText("Out of the Desk");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_F1) { // CREATE AUTO BOT
            createAutoBot();
        }

        if (autoBotBlue1 != null) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) { // START AUTO BOT
                startAutoBot();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_F2) { // CREATE BOT
            createBot();
        }

        if (bot2 != null) {
            if (e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyCode() == KeyEvent.VK_LEFT) {
                isLeft2 = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_NUMPAD3 || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                isRight2 = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_NUMPAD5 || e.getKeyCode() == KeyEvent.VK_UP) {
                isUp2 = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_DOWN) {
                isDown2 = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (bot2 != null) {
            if (e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyCode() == KeyEvent.VK_LEFT) {
                isLeft2 = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_NUMPAD3 || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                isRight2 = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_NUMPAD5 || e.getKeyCode() == KeyEvent.VK_UP) {
                isUp2 = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_DOWN) {
                isDown2 = false;
            }
        }
    }

    public void animateBot() {

        if (bot2 != null) {

            int dX = 0;
            int dY = 0;
            if (isLeft2) {
                dX -= BOT_STEP;
            }
            if (isRight2) {
                dX += BOT_STEP;
            }
            if (isUp2) {
                dY -= BOT_STEP;
            }
            if (isDown2) {
                dY += BOT_STEP;
            }

            if (dX != 0 || dY != 0) {
                if (dX != 0 && dY != 0) {
                    dX = dX > 0 ? BOT_MINI_STEP : -BOT_MINI_STEP;
                    dY = dY > 0 ? BOT_MINI_STEP : -BOT_MINI_STEP;
                }

                bot2.move(dX, dY);
            }

        }

    }

    public void animateAutoBot() {

        if (autoBotBlue1 != null) {

            // BOT STARTED -> SET FIRST TARGET
            if (autoBotBlue1.getAutoBotTarget() == null) {
                Coordinates nextTarget = getNextTarget(autoBotBlue1);
                if (nextTarget != null) {
                    autoBotBlue1.setAutoBotTarget(nextTarget);
                } else {
                    autoBotBlue1.setAutoBotTarget(new Coordinates(X_START_AUTO_BOT_BLUE_1, Y_START_AUTO_BOT));
                }
            }

            boolean wasMoved = autoBotBlue1.move();
            boolean workDone = autoBotBlue1.getWorkDone();
            updateBotStatus();

            if (wasMoved == false) {
                // BOT IS WORKING
                if (workDone == false) {
                } // WORK DONE -> SET NEXT TARGET
                else {
                    Coordinates nextTarget = getNextTarget(autoBotBlue1);
                    autoBotBlue1.setWorkDone(false);
                    if (nextTarget != null) {
                        autoBotBlue1.setAutoBotTarget(nextTarget);

                    } else {
                        autoBotBlue1.setAutoBotTarget(new Coordinates(X_START_AUTO_BOT_BLUE_1, Y_START_AUTO_BOT));
                    }
                }
            }

        }

        if (autoBotBlue2 != null) {

            // BOT STARTED -> SET FIRST TARGET
            if (autoBotBlue2.getAutoBotTarget() == null) {
                Coordinates nextTarget = getNextTarget(autoBotBlue2);
                if (nextTarget != null) {
                    autoBotBlue2.setAutoBotTarget(nextTarget);
                } else {
                    autoBotBlue2.setAutoBotTarget(new Coordinates(X_START_AUTO_BOT_BLUE_2, Y_START_AUTO_BOT));
                }
            }

            boolean wasMoved = autoBotBlue2.move();
            boolean workDone = autoBotBlue2.getWorkDone();
            //updateBotStatus();

            if (wasMoved == false) {
                // BOT IS WORKING
                if (workDone == false) {
                } // WORK DONE -> SET NEXT TARGET
                else {
                    Coordinates nextTarget = getNextTarget(autoBotBlue2);
                    autoBotBlue2.setWorkDone(false);
                    if (nextTarget != null) {
                        autoBotBlue2.setAutoBotTarget(nextTarget);

                    } else {
                        autoBotBlue2.setAutoBotTarget(new Coordinates(X_START_AUTO_BOT_BLUE_2, Y_START_AUTO_BOT));
                    }
                }
            }
        }

        if (autoBotRed1 != null) {

            // BOT STARTED -> SET FIRST TARGET
            if (autoBotRed1.getAutoBotTarget() == null) {
                Coordinates nextTarget = getNextTarget(autoBotRed1);
                if (nextTarget != null) {
                    autoBotRed1.setAutoBotTarget(nextTarget);
                } else {
                    autoBotRed1.setAutoBotTarget(new Coordinates(X_START_AUTO_BOT_RED_1, Y_START_AUTO_BOT));
                }
            }

            boolean wasMoved = autoBotRed1.move();
            boolean workDone = autoBotRed1.getWorkDone();
            //updateBotStatus();

            if (wasMoved == false) {
                // BOT IS WORKING
                if (workDone == false) {
                } // WORK DONE -> SET NEXT TARGET
                else {
                    Coordinates nextTarget = getNextTarget(autoBotRed1);
                    autoBotRed1.setWorkDone(false);
                    if (nextTarget != null) {
                        autoBotRed1.setAutoBotTarget(nextTarget);

                    } else {
                        autoBotRed1.setAutoBotTarget(new Coordinates(X_START_AUTO_BOT_RED_1, Y_START_AUTO_BOT));
                    }
                }
            }
        }

        if (autoBotRed2 != null) {

            // BOT STARTED -> SET FIRST TARGET
            if (autoBotRed2.getAutoBotTarget() == null) {
                Coordinates nextTarget = getNextTarget(autoBotRed2);
                if (nextTarget != null) {
                    autoBotRed2.setAutoBotTarget(nextTarget);
                } else {
                    autoBotRed2.setAutoBotTarget(new Coordinates(X_START_AUTO_BOT_RED_2, Y_START_AUTO_BOT));
                }
            }

            boolean wasMoved = autoBotRed2.move();
            boolean workDone = autoBotRed2.getWorkDone();
            //updateBotStatus();

            if (wasMoved == false) {
                // BOT IS WORKING
                if (workDone == false) {
                } // WORK DONE -> SET NEXT TARGET
                else {
                    Coordinates nextTarget = getNextTarget(autoBotRed2);
                    autoBotRed2.setWorkDone(false);
                    if (nextTarget != null) {
                        autoBotRed2.setAutoBotTarget(nextTarget);

                    } else {
                        autoBotRed2.setAutoBotTarget(new Coordinates(X_START_AUTO_BOT_RED_2, Y_START_AUTO_BOT));
                    }
                }
            }
        }

    }

    public void killBots() {
        if (threadBot2 != null) {
            threadBot2.interrupt();
        }
        if (threadAutoBot1 != null) {
            threadAutoBot1.interrupt();
        }
    }

    private void initAutoBotRoutes() {
        // SET AUTO BOT ROUTE
        /*autoBotRouteBlue.add(desk.getCellCenterCoord(3, 3));
            autoBotRouteBlue.add(desk.getCellCenterCoord(0, 4));
            autoBotRouteBlue.add(desk.getCellCenterCoord(0, 2));*/

        autoBotRouteBlue.add(desk.getCellCenterCoord(4, 0));
        autoBotRouteBlue.add(desk.getCellCenterCoord(3, 1));
        autoBotRouteBlue.add(desk.getCellCenterCoord(2, 0));
        autoBotRouteBlue.add(desk.getCellCenterCoord(1, 1));
        autoBotRouteBlue.add(desk.getCellCenterCoord(0, 0));
        autoBotRouteBlue.add(desk.getCellCenterCoord(0, 2));
        autoBotRouteBlue.add(desk.getCellCenterCoord(1, 3));
        autoBotRouteBlue.add(desk.getCellCenterCoord(0, 4));
        autoBotRouteBlue.add(desk.getCellCenterCoord(2, 4));
        autoBotRouteBlue.add(desk.getCellCenterCoord(3, 3));
        autoBotRouteBlue.add(desk.getCellCenterCoord(2, 2));
        autoBotRouteBlue.add(desk.getCellCenterCoord(4, 2));
        autoBotRouteBlue.add(desk.getCellCenterCoord(4, 4));

        autoBotRouteRed.add(desk.getCellCenterCoord(0, 1));
        autoBotRouteRed.add(desk.getCellCenterCoord(0, 3));
        autoBotRouteRed.add(desk.getCellCenterCoord(1, 0));
        autoBotRouteRed.add(desk.getCellCenterCoord(1, 2));
        autoBotRouteRed.add(desk.getCellCenterCoord(1, 4));
        autoBotRouteRed.add(desk.getCellCenterCoord(2, 1));
        autoBotRouteRed.add(desk.getCellCenterCoord(2, 3));
        autoBotRouteRed.add(desk.getCellCenterCoord(3, 0));
        autoBotRouteRed.add(desk.getCellCenterCoord(3, 2));
        autoBotRouteRed.add(desk.getCellCenterCoord(3, 4));
        autoBotRouteRed.add(desk.getCellCenterCoord(4, 1));
        autoBotRouteRed.add(desk.getCellCenterCoord(4, 3));

    }

    private void createAutoBot() {
        autoBotBlue1 = new AutoBot(X_START_AUTO_BOT_BLUE_1, Y_START_AUTO_BOT, BOT_WIDTH, COLOR_BOT_BLUE, desk, frame, getGraphics());
        autoBotBlue2 = new AutoBot(X_START_AUTO_BOT_BLUE_2, Y_START_AUTO_BOT, BOT_WIDTH, COLOR_BOT_BLUE, desk, frame, getGraphics());
        
        autoBotRed1 = new AutoBot(X_START_AUTO_BOT_RED_1, Y_START_AUTO_BOT, BOT_WIDTH, COLOR_BOT_RED, desk, frame, getGraphics());
        autoBotRed2 = new AutoBot(X_START_AUTO_BOT_RED_2, Y_START_AUTO_BOT, BOT_WIDTH, COLOR_BOT_RED, desk, frame, getGraphics());

        desk.setFrame(frame);
        repaint();
        if (autoBotRouteBlue.isEmpty() || autoBotRouteRed.isEmpty()) {
            initAutoBotRoutes();
        }
    }

    private void startAutoBot() {
        if (autoBotBlue1 != null) {

            // RUN THREAD
            if (threadAutoBot1 == null) {
                threadAutoBot1 = new AutoBotMoveThread(frame);
                threadAutoBot1.start();
            }
            repaint();

        }
    }

    private Coordinates getNextTarget(AutoBot autoBot) {

        // BLUE AUTO BOT
        if (autoBot.getColorBot() == COLOR_BOT_BLUE) {
            if (autoBotRouteBlue.isEmpty()) {
                return null;
            }

            int xBot = autoBot.getX();
            int yBot = autoBot.getY();

            List<Integer> distances = new ArrayList<>(autoBotRouteBlue.size());
            for (int i = 0; i < autoBotRouteBlue.size(); i++) {
                int x = autoBotRouteBlue.get(i).getX();
                int y = autoBotRouteBlue.get(i).getY();
                double dblDist = Math.pow((Math.pow(x - xBot, 2) + Math.pow(y - yBot, 2)), 0.5);
                int dist = (int) (Math.round(dblDist));
                distances.add(dist);
            }
            Integer minDist = distances.stream().min(Integer::compare).get();
            int minDistInd = distances.indexOf(minDist);

            Coordinates nextTarget = autoBotRouteBlue.get(minDistInd);
            autoBotRouteBlue.remove(minDistInd);

            return nextTarget;
        } // RED AUTO BOT
        else {
            if (autoBotRouteRed.isEmpty()) {
                return null;
            }

            int xBot = autoBot.getX();
            int yBot = autoBot.getY();

            List<Integer> distances = new ArrayList<>(autoBotRouteRed.size());
            for (int i = 0; i < autoBotRouteRed.size(); i++) {
                int x = autoBotRouteRed.get(i).getX();
                int y = autoBotRouteRed.get(i).getY();
                double dblDist = Math.pow((Math.pow(x - xBot, 2) + Math.pow(y - yBot, 2)), 0.5);
                int dist = (int) (Math.round(dblDist));
                distances.add(dist);
            }
            Integer minDist = distances.stream().min(Integer::compare).get();
            int minDistInd = distances.indexOf(minDist);

            Coordinates nextTarget = autoBotRouteRed.get(minDistInd);
            autoBotRouteRed.remove(minDistInd);

            return nextTarget;
        }

    }

    private void createBot() {
        bot2 = new Bot(X_START_BOT_2, Y_START_BOT, BOT_WIDTH, COLOR_BOT_RED, desk, frame, getGraphics());
        bot2.setAutoBotRouteBlue(autoBotRouteBlue);
        desk.setFrame(frame);
        if (threadBot2 == null) {
            threadBot2 = new BotMoveThread(frame);
            threadBot2.start();
        }
        repaint();
        if (autoBotRouteBlue.isEmpty()) {
            initAutoBotRoutes();
        }
    }
}
