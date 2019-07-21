package org.kllbff.magic.view;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.kllbff.magic.app.Activity;
import org.kllbff.magic.app.Log;
import org.kllbff.magic.event.KeyEvent;
import org.kllbff.magic.event.MouseEvent;
import org.kllbff.magic.graphics.Canvas;
import org.kllbff.magic.graphics.drawable.Drawable;
import org.kllbff.magic.hardware.Display;
import org.kllbff.magic.hardware.WindowManager;

public class Window extends View {
    private WindowManager windowManager = WindowManager.getInstance();
    private Frame frame;
    private View contentView;
    private Drawable icon;

    public Window(Activity activity) {
        super(activity);
        windowManager.register(this);
        
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
            public void mouseDragged(java.awt.event.MouseEvent e) {}

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
        
        frame.setUndecorated(false);
        frame.setBackground(new java.awt.Color(0xFF, 0xFF, 0xFF, 0xFF));
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                
            }

            @Override
            public void windowClosing(WindowEvent e) {
                activity.onStop();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                
            }

            @Override
            public void windowIconified(WindowEvent e) {
                activity.onPause(null);   
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                activity.onResume(null);
            }

            @Override
            public void windowActivated(WindowEvent e) {
                
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                
            }
        });
        
        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                onResize(frame.getWidth(), frame.getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                onLayout(frame.getX(), frame.getY());
            }

            @Override
            public void componentShown(ComponentEvent e) { }

            @Override
            public void componentHidden(ComponentEvent e) { }
        });
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

    @Override
    public int getWidth() {
        return this.minWidth;
    }

    @Override
    public void setWidth(int width) {
        this.minWidth = width;
        this.width = width - windowManager.getBorderWidth() * 2;
        frame.setSize(width, minHeight);
    }

    @Override
    public int getHeight() {
        return this.minHeight;
    }

    @Override
    public void setHeight(int height) {
        this.minHeight = height;
        this.height = height - windowManager.getTitleBarHeight() - windowManager.getBorderWidth();
        frame.setSize(minWidth, height);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    public void dismiss() {
        frame.dispose();
    }
    
    public void setResizable(boolean resizable) {
        frame.setResizable(resizable);
    }
    
    public boolean isResizable() {
        return frame.isResizable();
    }
    
    public boolean isFullscreen() {
        Window window = getDisplay().getFullscreenWindow();
        if(window == null) {
            return false;
        }
        
        return window.equals(this);
    }
    
    public boolean requestFullscreen() {
        return getDisplay().setFullscreenWindow(this);
    }
    
    public void unlockFullscreen() {
        getDisplay().setFullscreenWindow(null);
    }
    
    public String getTitle() {
        return frame.getTitle();
    }
    
    public void setTitle(String title) {
        frame.setTitle(title);
    }
    
    public boolean isFolded() {
        return frame.getState() == Frame.ICONIFIED;
    }
    
    public void fold() {
        frame.setState(Frame.ICONIFIED);
    }
    
    public void unfold() {
        frame.setState(Frame.NORMAL);
    }
    
    public Drawable getIcon() {
        return icon;
    }
    
    public void setIcon(Drawable icon) {
        this.icon = icon;
        Canvas canvas = new Canvas(48, 48);
        icon.setBounds(0, 0, 48, 48);
        icon.draw(canvas);
        frame.setIconImage(canvas.toAWT());
    }
    
    public void toFront() {
        frame.toFront();
    }
    
    public void toBack() {
        frame.toBack();
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
    
    @Override
    public void onResize(int width, int height) {
        Log.logger().i("Window", "Window resized to " + width + "x" + height);
        this.minWidth = width;
        this.minHeight = height;
        this.width = width - windowManager.getBorderWidth() * 2;
        this.height = height - windowManager.getTitleBarHeight() - windowManager.getBorderWidth();
    }
    
    @Override
    public void onLayout(int x, int y) {
        Log.logger().i("Window", "Window located at (" + x + ", " + y + ")");
        super.onLayout(x, y);
    }
    
    public Frame toAWT() {
        return frame;
    }
}
