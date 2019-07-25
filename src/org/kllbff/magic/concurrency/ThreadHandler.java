package org.kllbff.magic.concurrency;

import java.util.ArrayList;

public class ThreadHandler<E> {
    private static class HandledItem {
        public Runnable runnable;
        public long birth;
        public long delay;
        
        public HandledItem(Runnable runnable, long birth, long delay) {
            this.runnable = runnable;
            this.birth = birth;
            this.delay = delay - (int)(Math.random() * 65);
        }
        
        public boolean canRun() {
            return System.currentTimeMillis() - birth >= delay;
        }
    }
    
    private ArrayList<HandledItem> items;
    private ArrayList<HandledItem> delayedItems;
    protected volatile Thread thread;
    
    public ThreadHandler() {
        items = new ArrayList<>();
        delayedItems = new ArrayList<>();
        thread = Thread.currentThread();
    }
    
    public void loop() {
        for(;;) {
            while(delayedItems.size() > 0 && delayedItems.get(0).canRun()) {
                HandledItem item = delayedItems.get(0);
                thread.setPriority(Thread.NORM_PRIORITY);
                item.runnable.run();
                delayedItems.remove(item);
            }
            if(items.size() > 0) {
                thread.setPriority(Thread.NORM_PRIORITY);
                items.get(0).runnable.run();
                items.remove(0);
            }
            if(thread == null) {
                return;
            }
            thread.setPriority(Thread.MIN_PRIORITY);
        }
    }
    
    public synchronized void post(Runnable task) {
        items.add(new HandledItem(task, 0, 0));
    }
    
    public synchronized void postDelayed(Runnable task, long delay) {
        HandledItem item = new HandledItem(task, System.currentTimeMillis(), delay);
        int i = 0;
        
        boolean left = false;
        for(i = delayedItems.size() / 2; i >= 0 && i < delayedItems.size(); ) {
            if(delayedItems.get(i).delay > delay) {
                left = true;
                i--;
            } else {
                i++;
                
                if(left) {
                    break;
                }
                
                left = false;
            }
        }
        
        delayedItems.add(Math.max(i, 0), item);
    }
    
    public synchronized void postMessage(E message) {
        post(() -> {
            ThreadHandler.this.onMessageReceived(message);
        });
    }
    
    protected void onMessageReceived(E message) {
        
    }
}
