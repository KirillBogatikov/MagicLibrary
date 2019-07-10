package org.kllbff.magic.utils;

import org.kllbff.magic.exceptions.NotEnoughMemoryException;

/**
 * <h3>A helper class that allows you to more safely allocate memory to accommodate data</h3>
 * The main difference between the default Java allocation and this allocation is that attempts
 * to allocate memory by standard means lead to filling of free memory and at its shortage, 
 * {@link OutOfMemoryError} emission at which the application is forced to shut down. <br />
 * In the case of using this class, free memory is not filled, first checked for free memory and 
 * if it is not enough, there is a release NotEnoughMemoryException, processing that, the application
 * can continue it's work.
 * 
 * @author Kirill Bogatikov
 * @version 1.1
 * @since 1.0
 */
public class MemoryAllocator {
    private static final Runtime runtime = Runtime.getRuntime();
    
    /**
     * Returns amount of free memory
     * <p>Free memory is not all available to the application. Free memory is those areas that were previously 
     *    used by the application and are not currently used, but are "marked" for this process</p>
     * 
     * @return amount of free memory, in bytes
     */
    public static long freeMemory() {
        return runtime.freeMemory();
    }
    
    /**
     * Returns amount of occupied memory
     * <p>Occupied memory - the amount of data currently stored in RAM by the application</p>
     * 
     * @return amount of occupied memory, in bytes
     */
    public static long occupiedMemory() {
        return runtime.totalMemory();
    }
    
    /**
     * Returns maximum amount of memory, which process can use
     * <p>Max memory - full amount of RAM memory, given by JVM to this process, including already
     *    used (occupied) memory</p>
     * 
     * @return maximum amount of memory, in bytes
     */
    public static long maximumMemory() {
        return runtime.maxMemory();
    }

    /**
     * Returns amount of available for allocating memory
     * <p>Available memory amount can be calculated as MAX - OCCUPIED</p>
     * 
     * @return amount of available memory, in bytes
     */
    public static long availableMemory() {
        return runtime.maxMemory() - runtime.totalMemory();
    }
    
    /**
     * Checks amount of available memory for less or equal to specified count
     * 
     * @param bytesCount length of allocatable memory site, in bytes
     * @throws NotEnoughMemoryException if available space not enough to allocate specified memory space
     */
    public synchronized static void checkMemoryAvailability(int bytesCount) throws NotEnoughMemoryException {
        long available = availableMemory();
        if(available <= bytesCount) {
            throw new NotEnoughMemoryException(bytesCount, available);
        }
    }
    
    /**
     * Allocates memory site for byte array of specified length and returns new array associated
     * with allocated memory
     * 
     * @param length count of bytes in array 
     * @return new array associated with allocated memory
     * @throws NotEnoughMemoryException if available space not enough to allocate specified memory space
     */
    public synchronized static byte[] allocateByteArray(int length) throws NotEnoughMemoryException {
        checkMemoryAvailability(length);
        return new byte[length];
    }
    
    /**
     * Allocates memory site for int array of specified length and returns new array associated
     * with allocated memory
     * <p>In Java, Integer number has length of 32 bits or 4 bytes. Therefore, length of allocatable memory
     *    site can be calculated as length * 4</p>
     * 
     * @param length count of int in array 
     * @return new array associated with allocated memory
     * @throws NotEnoughMemoryException if available space not enough to allocate specified memory space
     */
    public synchronized static int[] allocateIntArray(int length) throws NotEnoughMemoryException {
        checkMemoryAvailability(length * 4);
        return new int[length];
    }
    
    /**
     * Allocates memory site for long array of specified length and returns new array associated
     * with allocated memory
     * <p>In Java, Long number has length of 64 bits or 8 bytes. Therefore, length of allocatable memory
     *    site can be calculated as length * 8</p>
     * 
     * @param length count of long in array 
     * @return new array associated with allocated memory
     * @throws NotEnoughMemoryException if available space not enough to allocate specified memory space
     */
    public synchronized static long[] allocateLongArray(int length) throws NotEnoughMemoryException {
        checkMemoryAvailability(length * 8);
        return new long[length];
    }

    /**
     * Allocates memory site for double array of specified length and returns new array associated
     * with allocated memory
     * <p>In Java, Double number has length of 64 bits or 8 bytes. Therefore, length of allocatable memory
     *    site can be calculated as length * 8</p>
     * 
     * @param length count of double in array 
     * @return new array associated with allocated memory
     * @throws NotEnoughMemoryException if available space not enough to allocate specified memory space
     */
    public synchronized static double[] allocateDoubleArray(int length) throws NotEnoughMemoryException {
        checkMemoryAvailability(length * 4);
        return new double[length];
    }

    /**
     * Allocates memory site for two-dimension byte array of specified width and height and returns new array associated
     * with allocated memory
     * <p>2D array can be presented as array of width * height length. Therefore, we can calculate size of target memory
     *    site: width * height bytes</p>
     * 
     * @param width width of 2D array
     * @param height height of 2D array 
     * @return new 2D array associated with allocated memory
     * @throws NotEnoughMemoryException if available space not enough to allocate specified memory space
     */
    public synchronized static byte[][] allocateByteMatrix(int width, int height) throws NotEnoughMemoryException {
        checkMemoryAvailability(width * height);
        return new byte[width][height];
    }
    
    /**
     * Allocates memory site for two-dimension int array of specified width and height and returns new array associated
     * with allocated memory
     * <p>2D array can be presented as array of width * height length. Also in Java, Integer number has length of 32 bits or 4 bytes.
     *    Therefore, we can calculate size of target memory site: width * height * 4 bytes</p>
     * 
     * @param width width of 2D array
     * @param height height of 2D array 
     * @return new 2D array associated with allocated memory
     * @throws NotEnoughMemoryException if available space not enough to allocate specified memory space
     */
    public synchronized static int[][] allocateIntMatrix(int width, int height) throws NotEnoughMemoryException {
        checkMemoryAvailability(width * height * 4);
        return new int[width][height];
    }
    
    /**
     * Allocates memory site for two-dimension long array of specified width and height and returns new array associated
     * with allocated memory
     * <p>2D array can be presented as array of width * height length. Also in Java, Long number has length of 64 bits or 8 bytes.
     *    Therefore, we can calculate size of target memory site: width * height * 8 bytes</p>
     * 
     * @param width width of 2D array
     * @param height height of 2D array 
     * @return new 2D array associated with allocated memory
     * @throws NotEnoughMemoryException if available space not enough to allocate specified memory space
     */
    public synchronized static long[][] allocateLongMatrix(int width, int height) throws NotEnoughMemoryException {
        checkMemoryAvailability(width * height * 8);
        return new long[width][height];
    }
    
    /**
     * Allocates memory site for two-dimension double array of specified width and height and returns new array associated
     * with allocated memory
     * <p>2D array can be presented as array of width * height length. Also in Java, Double number has length of 64 bit or 8 bytes.
     *    Therefore, we can calculate size of target memory site: width * height * 8 bytes</p>
     * 
     * @param width width of 2D array
     * @param height height of 2D array 
     * @return new 2D array associated with allocated memory
     * @throws NotEnoughMemoryException if available space not enough to allocate specified memory space
     */
    public synchronized static double[][] allocateDoubleMatrix(int width, int height) throws NotEnoughMemoryException {
        checkMemoryAvailability(width * height * 8);
        return new double[width][height];
    }
}
