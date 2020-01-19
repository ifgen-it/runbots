
package runbots;

public class BotMoveThread extends Thread {

    private BotsJFrame frame;
    
    public BotMoveThread(BotsJFrame frame) {
        super("BotMoveThread");
        this.frame = frame;
        
    }

    @Override
    public void run() {
        while (true){
            frame.animateBot();
            try{
                Thread.sleep(40);
                
            } catch(InterruptedException e){
                //e.printStackTrace();
                System.out.println("BotMoveThread killed");
                break;
            }
        }
    }
    
    
}
