/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runbots;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author evgen
 */
public class FrameCloser extends WindowAdapter{

    private BotsJFrame frame;
    
    public FrameCloser(BotsJFrame frame){
        this.frame = frame;
    }
    
    @Override
    public void windowClosed(WindowEvent e) {
        System.out.println("Window closed");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        e.getWindow().dispose();
        frame.killBots();
    }
    
}
