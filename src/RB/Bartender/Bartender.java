package RB.Bartender;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @authors Anthony Spiteri
 *          Cristian Nuosci
 *          Shahezad Kassam
 */

public class Bartender implements Runnable {
    private Socket socket;
    private ObjectOutputStream output;
    private static ObjectInputStream input;
    private final int bartenderNumber = 1;
    public Thread updateThread;
    private SynchronousQueue queue = new SynchronousQueue();
    
    public Bartender(Socket socket) throws IOException {
        this.socket = socket;
        this.output = new ObjectOutputStream(getSocket().getOutputStream());
        this.input = new ObjectInputStream(getSocket().getInputStream());
    }

    @Override
    public void run() {
        try {
            System.out.println("Bartender Connected");
            sendObject("Bartender");    // notifies the server that a bartender is connected
            createThread();
            
        } catch (IOException ex) {
            Logger.getLogger(Bartender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Socket getSocket() {
        return socket;
    }

    public void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
    }
    
    public Object receiveObject() throws IOException, ClassNotFoundException, InterruptedException {
        return queue.take();
    }  
    
    public void createThread() {
        updateThread = new Thread(() -> {
            try {
                while(true) {
                    Object message = input.readObject();

                    if(message.equals("Update")) {
                        System.out.println("there is an new drinkMenu...");
                        // see if drinks can still be made
                    }
                    else if(message.equals("Notify Manager")) {
                        receiveObject();
                    }                        
                    else {
                        //System.out.println("in thread else.....");
                        queue.put(message); 
                    }
                }
            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                Logger.getLogger(Kiosk.class.getName()).log(Level.SEVERE, null, ex);
            }   
        });
        updateThread.start();
    }


    
}
        

       