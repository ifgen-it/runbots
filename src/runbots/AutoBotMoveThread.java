
package runbots;

public class AutoBotMoveThread extends Thread {

    private BotsJFrame frame;
    
    public AutoBotMoveThread(BotsJFrame frame) {
        super("AutoBotMoveThread");
        this.frame = frame;
        
    }

    @Override
    public void run() {
        while (true){
            frame.animateAutoBot();
            try{
                Thread.sleep(40);
                
            } catch(InterruptedException e){
                //e.printStackTrace();
                System.out.println("AutoBotMoveThread killed");
                break;
            }
        }
    }
    
    
}
