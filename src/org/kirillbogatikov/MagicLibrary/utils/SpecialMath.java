package org.kirillbogatikov.MagicLibrary.utils;

public class SpecialMath {
    public static Number max(Number... numbers) {
        if(numbers.length == 1) return numbers[0];
        
        int max = 0;
        for(int i = 1; i < numbers.length; i++) {
            if(numbers[i].doubleValue() > numbers[max].doubleValue()) {
                max = i;
            }
        }
        
        return numbers[max];
    }
    
    public static Number min(Number... numbers) {
        if(numbers.length == 1) return numbers[0];
        
        int min = 0;
        for(int i = 1; i < numbers.length; i++) {
            if(numbers[i].doubleValue() < numbers[min].doubleValue()) {
                min = i;
            }
        }
        
        return numbers[min];
    }
}
