package org.kirillbogatikov.MagicLibrary.graphics;

public class Color {
    public static int parse(String hex) {
        hex = hex.toLowerCase().trim().replaceAll("#", "");
        
        int[] argb = new int[4];
        
        if(hex.matches("[0-9a-f]{6}")) { //RRGGBB
            argb[0] = 255;
            for(int i = 0; i < 4;) {
                argb[i + 1] = Integer.parseInt(hex.substring(i * 2, (++i) * 2));
            }
        }
        
        if(hex.matches("[0-9a-f]{8}")) { //AARRGGBB
            for(int i = 0; i < 4;) {
                argb[i] = Integer.parseInt(hex.substring(i * 2, (++i) * 2));
            }
        }
        
        if(hex.matches("[0-9a-f]{3}")) { //RGB
            argb[0] = 255;
            for(int i = 0; i < 4;) {
                argb[i + 1] = Integer.parseInt(hex.substring(i, (++i)));
            }
        }
        
        if(hex.matches("[0-9a-f]{4}")) { //ARGB
            for(int i = 0; i < 4;) {
                argb[i] = Integer.parseInt(hex.substring(i, (++i)));
            }
        }
        
        return parse(argb);
    }
    
    public static int parse(int[] array) {
        int A, R, G, B;
        if(array.length == 3) {
            A = 0xFF;
            R = array[0];
            G = array[1];
            B = array[2];
        } else {
            A = array[0];
            R = array[1];
            G = array[2];
            B = array[3];
        }
        
        return A << 24 | R << 16 | G << 8 | B;
    }
    
    
}
