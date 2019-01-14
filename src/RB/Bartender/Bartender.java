package RB.Bartender;

import java.net.Socket;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Bartender implements Runnable {
    
    private Socket socket;
    
    public Bartender(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        
        
    }
    
}
        

       