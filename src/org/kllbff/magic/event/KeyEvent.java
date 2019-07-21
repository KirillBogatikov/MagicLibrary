package org.kllbff.magic.event;

public class KeyEvent extends InputEvent {
    public static final int KEY_ENTER                = '\n'; 
    public static final int KEY_BACK_SPACE           = '\b';
    public static final int KEY_TAB                  = '\t';
    
    public static final int KEY_CANCEL               = 0x03; //unknown
    public static final int KEY_CLEAR                = 0x0C; //unknown
    
    public static final int KEY_SHIFT                = 0x10; //shift
    public static final int KEY_CTRL                 = 0x11; //ctrl
    public static final int KEY_ALT                  = 0x12; //alt
    public static final int KEY_WINDOWS              = 0x020C; //win
    public static final int KEY_CONTEXT_MENU         = 0x020D; //windows' context button
    
    public static final int KEY_CAPS_LOCK            = 0x14; //caps lock
    public static final int KEY_NUM_LOCK             = 0x90; //num lock
    public static final int KEY_SCROLL_LOCK          = 0x91; //scroll lock
    
    public static final int KEY_ESCAPE               = 0x1B; //esc
    public static final int KEY_SPACE                = 0x20; //space
    
    public static final int KEY_PAGE_UP              = 0x21; //page up
    public static final int KEY_PAGE_DOWN            = 0x22; //page down
    public static final int KEY_END                  = 0x23; //end
    public static final int KEY_HOME                 = 0x24; //home
    public static final int KEY_INSERT               = 0x9B; //insert
    public static final int KEY_DELETE               = 0x7F; //delete
    
    public static final int KEY_ARROW_LEFT           = 0x25; //arrow left 
    public static final int KEY_ARROW_UP             = 0x26; //arrow up
    public static final int KEY_ARROW_RIGHT          = 0x27; //arrow right
    public static final int KEY_ARROW_DOWN           = 0x28; //arrow down
    
    public static final int KEY_COMMA                = 0x2C; //comma ,
    public static final int KEY_POINT                = 0x2E; //point .
    public static final int KEY_SLASH                = 0x2F; //slash /
    
    public static final int KEY_0                    = 0x30;
    public static final int KEY_1                    = 0x31;
    public static final int KEY_2                    = 0x32;
    public static final int KEY_3                    = 0x33;
    public static final int KEY_4                    = 0x34;
    public static final int KEY_5                    = 0x35;
    public static final int KEY_6                    = 0x36;
    public static final int KEY_7                    = 0x37;
    public static final int KEY_8                    = 0x38;
    public static final int KEY_9                    = 0x39;
    
    public static final int KEY_SEMICOLON            = 0x3B; //semicolon ;
    public static final int KEY_EQUALS               = 0x3D; //equals =
    
    public static final int KEY_A                    = 0x41;
    public static final int KEY_B                    = 0x42;
    public static final int KEY_C                    = 0x43;
    public static final int KEY_D                    = 0x44;
    public static final int KEY_E                    = 0x45;
    public static final int KEY_F                    = 0x46;
    public static final int KEY_G                    = 0x47;
    public static final int KEY_H                    = 0x48;
    public static final int KEY_I                    = 0x49;
    public static final int KEY_J                    = 0x4A;
    public static final int KEY_K                    = 0x4B;
    public static final int KEY_L                    = 0x4C;
    public static final int KEY_M                    = 0x4D;
    public static final int KEY_N                    = 0x4E;
    public static final int KEY_O                    = 0x4F;
    public static final int KEY_P                    = 0x50;
    public static final int KEY_Q                    = 0x51;
    public static final int KEY_R                    = 0x52;
    public static final int KEY_S                    = 0x53;
    public static final int KEY_T                    = 0x54;
    public static final int KEY_U                    = 0x55;
    public static final int KEY_V                    = 0x56;
    public static final int KEY_W                    = 0x57;
    public static final int KEY_X                    = 0x58;
    public static final int KEY_Y                    = 0x59;
    public static final int KEY_Z                    = 0x5A;
    
    public static final int KEY_OPEN_BRACKET         = 0x5B; //bracket [
    public static final int KEY_BACK_SLASH           = 0x5C; //backslash \
    public static final int KEY_CLOSE_BRACKET        = 0x5D; //bracket ]
    
    public static final int KEY_NUMPAD_0             = 0x60;
    public static final int KEY_NUMPAD_1             = 0x61;
    public static final int KEY_NUMPAD_2             = 0x62;
    public static final int KEY_NUMPAD_3             = 0x63;
    public static final int KEY_NUMPAD_4             = 0x64;
    public static final int KEY_NUMPAD_5             = 0x65;
    public static final int KEY_NUMPAD_6             = 0x66;
    public static final int KEY_NUMPAD_7             = 0x67;
    public static final int KEY_NUMPAD_8             = 0x68;
    public static final int KEY_NUMPAD_9             = 0x69;
    
    public static final int KEY_NUMPAD_MULTIPLY      = 0x6A; //numpad *
    public static final int KEY_NUMPAD_ADD           = 0x6B; //numpad +
    public static final int KEY_NUMPAD_SUBTRACT      = 0x6D; //numpad -
    public static final int KEY_NUMPAD_DECIMAL_POINT = 0x6E; //numpad .
    public static final int KEY_NUMPAD_DIVIDE        = 0x6F; //numpad /
    
    public static final int KEY_F1                   = 0x70;
    public static final int KEY_F2                   = 0x71;
    public static final int KEY_F3                   = 0x72;
    public static final int KEY_F4                   = 0x73;
    public static final int KEY_F5                   = 0x74;
    public static final int KEY_F6                   = 0x75;
    public static final int KEY_F7                   = 0x76;
    public static final int KEY_F8                   = 0x77;
    public static final int KEY_F9                   = 0x78;
    public static final int KEY_F10                  = 0x79;
    public static final int KEY_F11                  = 0x7A;
    public static final int KEY_F12                  = 0x7B;    

    public static final int KEY_PRINTSCREEN          = 0x9A; //print screen
    public static final int KEY_PAUSE                = 0x13; //pause|break
    
    public static final int KEY_HELP                 = 0x9C; //unknown
    public static final int KEY_META                 = 0x9D; //unknown
    
    public static final int KEY_BACK_QUOTE           = 0xC0; //back quote `
    public static final int KEY_QUOTE                = 0xDE; //quote ' 
    
    private int code;
    private char character;

    public KeyEvent(int mask, int keyCode, char character) {
        super(mask);
        this.code = keyCode;
        this.character = character;
    }
    
    public char getChar() {
        return character;
    }
    
    public int getKeyCode() {
        return code;
    }
    
    
}
