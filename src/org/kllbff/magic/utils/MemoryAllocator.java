package org.kllbff.magic.utils;

import org.kllbff.magic.exceptions.NotEnoughMemoryException;

public class MemoryAllocator {
    private static final Runtime runtime = Runtime.getRuntime();
    
    public static long freeMemory() {
        return runtime.freeMemory();
    }
    
    public static long busyMemory() {
        return runtime.totalMemory();
    }
    
    public static long maximumMemory() {
        return runtime.maxMemory();
    }

    public static long availableMemory() {
        return runtime.maxMemory() - runtime.totalMemory();
    }
    
    public static void checkMemoryAvailability(int bytesCount) throws NotEnoughMemoryException {
        long available = availableMemory();
        if(available <= bytesCount) {
            throw new NotEnoughMemoryException(bytesCount, available);
        }
    }
    
    public static byte[] allocateByteArray(int length) throws NotEnoughMemoryException {
        checkMemoryAvailability(length);
        return new byte[length];
    }
    
    public static int[] allocateIntArray(int length) throws NotEnoughMemoryException {
        checkMemoryAvailability(length * 4);
        return new int[length];
    }
    
    public static long[] allocateLongArray(int length) throws NotEnoughMemoryException {
        checkMemoryAvailability(length * 8);
        return new long[length];
    }
    
    public static double[] allocateDoubleArray(int length) throws NotEnoughMemoryException {
        checkMemoryAvailability(length * 4);
        return new double[length];
    }
    
    public static byte[][] allocateByteMatrix(int width, int height) throws NotEnoughMemoryException {
        checkMemoryAvailability(width * height);
        return new byte[width][height];
    }
    
    public static int[][] allocateIntMatrix(int width, int height) throws NotEnoughMemoryException {
        checkMemoryAvailability(width * height * 4);
        return new int[width][height];
    }
    
    public static long[][] allocateLongMatrix(int width, int height) throws NotEnoughMemoryException {
        checkMemoryAvailability(width * height * 8);
        return new long[width][height];
    }
    
    public static double[][] allocateDoubleMatrix(int width, int height) throws NotEnoughMemoryException {
        checkMemoryAvailability(width * height * 8);
        return new double[width][height];
    }
}
