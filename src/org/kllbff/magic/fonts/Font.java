package org.kllbff.magic.fonts;

import org.kllbff.magic.exceptions.FontException;

public class Font {
    public static final int STYLE_PLAIN = 0,
                            STYLE_ITALIC = 1,
                            STYLE_BOLD   = 2;
    
    public java.awt.Font plain, italic, bold, boldItalic;
    private java.awt.Font current;
    private int style;
    
    Font(java.awt.Font plain) {
        this.plain = plain;
        this.current = plain;
        this.style = STYLE_PLAIN;
    }
    
    public int setStyle(int style) {
        switch(style) {
            case STYLE_PLAIN: 
                if(plain == null) {
                    plain = current.deriveFont(java.awt.Font.PLAIN, getSize());
                } else if(plain.getSize() != getSize()) {
                    plain = plain.deriveFont((float)getSize());
                }
                
                current = plain;
            break;
            case STYLE_ITALIC: 
                if(italic == null) {
                    italic = current.deriveFont(java.awt.Font.ITALIC, getSize());
                } else if(italic.getSize() != getSize()) {
                    italic = italic.deriveFont((float)getSize());
                }
                
                current = italic;
            break;
            case STYLE_BOLD:
                if(bold == null) {
                    bold = current.deriveFont(java.awt.Font.BOLD, getSize());
                } else if(bold.getSize() != getSize()) {
                    bold = bold.deriveFont((float)getSize());
                }
                 
                current = bold;
            break;
            case STYLE_ITALIC | STYLE_BOLD: 
                if(boldItalic == null) {
                    boldItalic = current.deriveFont(java.awt.Font.BOLD | java.awt.Font.ITALIC, getSize());
                } else if(boldItalic.getSize() != getSize()) {
                    boldItalic = boldItalic.deriveFont((float)getSize());
                }
                
                current = boldItalic;
            default: 
                throw new FontException("Incorrect style flag: it can be only STYLE_PLAIN, STYLE_ITALIC, STYLE_BOLD or logical sum of two last");
        }
        this.style = style;
        return style;
    }
    
    public int getStyle() {
        return style;
    }
    
    public boolean isItalic() {
        return (style & STYLE_ITALIC) != 0;
    }
    
    public boolean isBold() {
        return (style & STYLE_BOLD) != 0;
    }
    
    public boolean isPlain() {
        return style == STYLE_PLAIN;
    }
    
    public boolean canDisplay(char c) {
        return current.canDisplay(c);
    }
    
    public String getFamily() {
        return current.getFamily();
    }
    
    public String getName() {
        return current.getFontName();
    }
    
    public int getSize() {
        return current.getSize();
    }
    
    public void setSize(int size) {
        if(size == getSize()) {
            return;
        }
        
        current = current.deriveFont((float)size);
    }
    
    public Font clone() {
        return new Font(plain.deriveFont(1.0f));
    }
    
    public java.awt.Font toAWT() {
        return current;
    }
}
