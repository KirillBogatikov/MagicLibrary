package org.kllbff.magic.view;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseMotionListener;

import org.kllbff.magic.app.Activity;
import org.kllbff.magic.event.KeyEvent;
import org.kllbff.magic.event.MouseEvent;
import org.kllbff.magic.graphics.Canvas;
import org.kllbff.magic.hardware.Display;

public class Window extends View {
    private Activity activity;
    private Frame frame;
    private View contentView;

    public Window(Activity activity) {
        super(activity.getResources());
        this.activity = activity;
        
        this.frame = new Frame() {
            private static final long serialVersionUID = 4062787460280149527L;
            @Override
            public void paint(Graphics g) {
                g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
                Graphics2D g2d = (Graphics2D)g;
                Canvas canvas = new Canvas(g2d, frame.getWidth(), frame.getHeight());
                draw(canvas);
            }
        };
        
        frame.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                //no drag'n'drop
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int mask = MouseEvent.MASK_SIMPLE;
                
                if((e.getModifiers() & Event.ALT_MASK) != 0) {
                    mask |= MouseEvent.MASK_ALT;
                }
                if((e.getModifiers() & Event.CTRL_MASK) != 0) {
                    mask |= MouseEvent.MASK_CTRL;
                }
                if((e.getModifiers() & Event.SHIFT_MASK) != 0) {
                    mask |= MouseEvent.MASK_SHIFT;
                }
                
                onMouseEvent(TYPE_MOVE, new MouseEvent(mask, MouseEvent.NO_BUTTON, e.getX(), e.getY()));
            }
        });
        
        frame.addMouseListener(new java.awt.event.MouseListener() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) { }

            @Override
            public void mousePressed(java.awt.event.MouseEvent event) {
                int mask = MouseEvent.MASK_SIMPLE;
                
                if((event.getModifiers() & Event.ALT_MASK) != 0) {
                    mask |= MouseEvent.MASK_ALT;
                }
                if((event.getModifiers() & Event.CTRL_MASK) != 0) {
                    mask |= MouseEvent.MASK_CTRL;
                }
                if((event.getModifiers() & Event.SHIFT_MASK) != 0) {
                    mask |= MouseEvent.MASK_SHIFT;
                }
                
                onMouseEvent(TYPE_DOWN, new MouseEvent(mask, event.getButton(), event.getX(), event.getY()));
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent event) {
                int mask = MouseEvent.MASK_SIMPLE;
                
                if((event.getModifiers() & Event.ALT_MASK) != 0) {
                    mask |= MouseEvent.MASK_ALT;
                }
                if((event.getModifiers() & Event.CTRL_MASK) != 0) {
                    mask |= MouseEvent.MASK_CTRL;
                }
                if((event.getModifiers() & Event.SHIFT_MASK) != 0) {
                    mask |= MouseEvent.MASK_SHIFT;
                }
                
                onMouseEvent(TYPE_UP, new MouseEvent(mask, event.getButton(), event.getX(), event.getY()));   
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                onHoverStateChanged(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                onHoverStateChanged(false);
            }

        });
        
        frame.addKeyListener(new java.awt.event.KeyListener() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent event) {
                int mask = MouseEvent.MASK_SIMPLE;
                
                if((event.getModifiers() & Event.ALT_MASK) != 0) {
                    mask |= MouseEvent.MASK_ALT;
                }
                if((event.getModifiers() & Event.CTRL_MASK) != 0) {
                    mask |= MouseEvent.MASK_CTRL;
                }
                if((event.getModifiers() & Event.SHIFT_MASK) != 0) {
                    mask |= MouseEvent.MASK_SHIFT;
                }
                
                onKeyEvent(TYPE_DOWN, new KeyEvent(mask, event.getKeyCode(), event.getKeyChar()));
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent event) {
                int mask = MouseEvent.MASK_SIMPLE;
                
                if((event.getModifiers() & Event.ALT_MASK) != 0) {
                    mask |= MouseEvent.MASK_ALT;
                }
                if((event.getModifiers() & Event.CTRL_MASK) != 0) {
                    mask |= MouseEvent.MASK_CTRL;
                }
                if((event.getModifiers() & Event.SHIFT_MASK) != 0) {
                    mask |= MouseEvent.MASK_SHIFT;
                }
                
                onKeyEvent(TYPE_UP, new KeyEvent(mask, event.getKeyCode(), event.getKeyChar()));
            }
        });
        
        frame.setUndecorated(true);
        frame.setBackground(new java.awt.Color(0xFF, 0xFF, 0xFF, 0xFF));
    }
    
    public Display getDisplay() {
        return activity.getDisplayManager().getDisplay(getX(), getY());
    }
    
    public boolean isExpanded() {
        return frame.getExtendedState() == Frame.MAXIMIZED_BOTH;
    }
    
    public void expand() {
        Display display = getDisplay();
        frame.setMaximumSize(new Dimension(display.getWidth() - display.getToolbarWidth(), display.getHeight() - display.getToolbarHeight()));
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        frame.setLocation(x, getY());
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        frame.setLocation(getX(), y);
    }        
    
    @Override
    public <T extends View> T findViewById(long id) {
        return contentView.findViewById(id);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.translate(16, 16);
        super.draw(canvas);
        if(contentView != null) {
            contentView.draw(canvas);
        }
    }
    
    public int getWidth() {
        return this.minWidth;
    }
    
    public void setWidth(int width) {
        this.minWidth = width;
        this.width = width - 32;
        frame.setSize(width, minHeight);
    }
    
    public int getHeight() {
        return this.minHeight;
    }
    
    public void setHeight(int height) {
        this.minHeight = height;
        this.height = height - 32;
        frame.setSize(minWidth, height);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    @Override
    public boolean isCheckable() {
        return false;
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public void redraw() {
        frame.repaint();
    }

    @Override
    public View getTooltipView() {
        return null;
    }
}
