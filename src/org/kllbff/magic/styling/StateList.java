package org.kllbff.magic.styling;

import java.util.ArrayList;

public class StateList<V> {
    public static final int STATE_DISABLED  = 0x00;
    public static final int STATE_ENABLED   = 0x01;
    public static final int STATE_HOVERED   = 0x02;
    public static final int STATE_FOCUSED   = 0x04;
    public static final int STATE_SELECTED  = 0x08;
    public static final int STATE_PRESSED   = 0x10;
    public static final int STATE_CHECKED   = 0x20;
    
    public static final int MASK_DISABLED   = 0b11111111111111111111111111111110;
    public static final int MASK_UNHOVERED  = 0b11111111111111111111111111111101;
    public static final int MASK_UNFOCUSED  = 0b11111111111111111111111111111011;
    public static final int MASK_UNSELECTED = 0b11111111111111111111111111110111;
    public static final int MASK_UNPRESSED  = 0b11111111111111111111111111101111;
    public static final int MASK_UNCHECKED  = 0b11111111111111111111111111011111;
    
    
    private ArrayList<Integer> states;
    private ArrayList<V> values;
    
    public StateList(int[] states, V[] values) {
        this.states = new ArrayList<>();
        this.values = new ArrayList<>();
        for(int i = 0; i < states.length; i++) {
            addState(states[i], values[i]);
        }
    }
    
    public void addState(int state, V value) {
        this.states.add(state);
        this.values.add(value);
    }
    
    private int bit(int n, int i) {
        return (n >> i) & 1;
    }
    
    private int similarity(int firstNumber, int secondNumber) {
        if(bit(firstNumber, 0) != bit(secondNumber, 0)) {
            return 0;
        }
        
        int similarity = 0;
        for(int bitOffset = 1; bitOffset < 32; bitOffset++) {
            similarity += bit(firstNumber, bitOffset) == bit(secondNumber, bitOffset) ? bitOffset : 0;
        }
        return similarity;
    }
    
    public V getMostSimilar(int state) {
        int index = -1;
        int maxSimilarity = 0;
        for(int i = 0; i < states.size(); i++) {
            int currentSimilarity = similarity(states.get(i), state);
            if(currentSimilarity > maxSimilarity) {
                index = i;
                maxSimilarity = currentSimilarity;
            }
        }
        if(index > -1) {
            return values.get(index);
        }
        return null;
    }
    
    public V get(int state) {
        return values.get(state);
    }
}
