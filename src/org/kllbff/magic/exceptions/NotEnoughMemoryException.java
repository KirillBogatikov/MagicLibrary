package org.kllbff.magic.exceptions;

public class NotEnoughMemoryException extends RuntimeException {
    private static final long serialVersionUID = 745641407191720389L;

    private long available, necessary;
    
    public NotEnoughMemoryException(String message, long acailable, long necessary) {
        super(message);
        this.available = acailable;
        this.necessary = necessary;
    }
    
    public NotEnoughMemoryException(String message) {
        this(message, -1, -1);
    }
    
    public NotEnoughMemoryException(long available, long necessary) {
        this("Requested " + necessary + " bytes of memory, but available only " + available);
    }
    
    public long getAvailableMemory() {
        return available;
    }
    
    public long getNecessaryMemory() {
        return necessary;
    }
}
