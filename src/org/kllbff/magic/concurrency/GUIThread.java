package org.kllbff.magic.concurrency;

import org.kllbff.magic.app.Log;

public class GUIThread extends Thread {
    private static ThreadHandler<String> handlerInstance;
    
    public static synchronized ThreadHandler<String> getHandlerInstance() {
        if(handlerInstance == null) {
            Thread gui = new GUIThread();
            gui.start();
            
            while(handlerInstance == null) {
                //wait initialization
            }
        }
        return handlerInstance;
    }
    
    private GUIThread() {
    }

    public void run() {
        setName("Magic application GUI thread");
        
        handlerInstance = new ThreadHandler<String>() {
            @Override
            protected void onMessageReceived(String message) {
                if(message.equals("kill")) {
                    Thread.yield();
                    this.thread = null;
                }
            }
            
            public synchronized void post(Runnable task) {
                Log.logger().i("GUI", "Running task in GUI thread");
                super.post(task);
            }
            
            public synchronized void postDelayed(Runnable task, long delay) {
                Log.logger().i("GUI", "Running task in GUI thread through " + delay + " ms");
                super.postDelayed(task, delay);
            }
            
            public synchronized void postMessage(String message) {
                Log.logger().i("GUI", "Posted new message into GUI thread: " + message);
                super.postMessage(message);
            }
        };

        handlerInstance.loop();
        Log.logger().i("GUI", "GUI thread stoppped");
    }    
}
