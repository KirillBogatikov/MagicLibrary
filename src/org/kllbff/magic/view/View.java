package org.kllbff.magic.view;

import static org.kllbff.magic.styling.StateList.MASK_DISABLED;
import static org.kllbff.magic.styling.StateList.MASK_UNHOVERED;
import static org.kllbff.magic.styling.StateList.MASK_UNPRESSED;
import static org.kllbff.magic.styling.StateList.MASK_UNSELECTED;
import static org.kllbff.magic.styling.StateList.STATE_ENABLED;
import static org.kllbff.magic.styling.StateList.STATE_FOCUSED;
import static org.kllbff.magic.styling.StateList.STATE_HOVERED;
import static org.kllbff.magic.styling.StateList.STATE_PRESSED;
import static org.kllbff.magic.styling.StateList.STATE_SELECTED;

import java.io.IOException;
import java.util.Arrays;

import org.kllbff.magic.event.KeyEvent;
import org.kllbff.magic.event.MouseEvent;
import org.kllbff.magic.exceptions.ResourceNotFoundException;
import org.kllbff.magic.graphics.Canvas;
import org.kllbff.magic.graphics.Paint;
import org.kllbff.magic.graphics.drawable.ColorDrawable;
import org.kllbff.magic.graphics.drawable.Drawable;
import org.kllbff.magic.res.Resources;
import org.kllbff.magic.styling.StateList;

public abstract class View {
    public static final int WRAP_CONTENT = -1,
                            MATCH_PARENT = -2;
    
    public static final int VISIBLE   = 0,
                            INVISIBLE = 1,
                            GONE      = 2;
    
    public static enum Side {
        LEFT   (0),
        TOP    (1),
        RIGHT  (2),
        BOTTOM (3);
        
        protected int index;
        private Side(int index) {
            this.index = index;
        }
    }
    
    private Resources resources;
    private OnClickListener onClickListener;
    private OnHoverListener onHoverListener;
    private OnDoubleClickListener onDoubleClickListener;
    private KeyListener keyListener;
    private MouseListener mouseListener;
    
    private Drawable backgroundDrawable;
    private StateList<Drawable> backgroundDrawableList;
    private StateList<Long> backgroundColorList;

    protected int[] padding;
    protected int[] margin;
    protected int state;
    
    private long id;
    private int x, y, z;
    private int minWidth, minHeight;
    private int width, height;
    private boolean shown;
    private int visibility;
    
    private ViewGroup parent;

    public View(Resources resources) {
        this.resources = resources;
        padding = new int[4];
        margin = new int[4];
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    
    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }
    
    public void setOnHoverListener(OnHoverListener onHoverListener) {
        this.onHoverListener = onHoverListener;
    }
    
    public void setMouseListener(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }
    
    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(long id) {
        if(this.id == id) {
            return (T)this;
        }
        return null;
    }
 
    public void setPadding(int left, int top, int right, int bottom) {
        this.padding[Side.LEFT.index] = left;
        this.padding[Side.TOP.index] = top;
        this.padding[Side.RIGHT.index] = right;
        this.padding[Side.BOTTOM.index] = bottom;
    }
    
    public void setPadding(Side side, int value) {
        this.padding[side.index] = value;
    }
    
    public int[] getPadding() {
        return Arrays.copyOf(padding, 4);
    }
    
    public int getPadding(Side side) {
        return this.padding[side.index];
    }
    
    public void setMargin(int left, int top, int right, int bottom) {
        this.margin[Side.LEFT.index] = left;
        this.margin[Side.TOP.index] = top;
        this.margin[Side.RIGHT.index] = right;
        this.margin[Side.BOTTOM.index] = bottom;
    }
    
    public void setMargin(Side side, int value) {
        this.margin[side.index] = value;
    }
    
    public int[] getMargin() {
        return Arrays.copyOf(margin, 4);
    }
    
    public int getMargin(Side side) {
        return margin[side.index];
    }
    
    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }
    
    public StateList<Drawable> getBackgroundDrawableList() {
        return backgroundDrawableList;
    }
    
    public StateList<Long> getBackgroundColorList() {
        return backgroundColorList;
    }
    
    public void setBackgroundDrawable(StateList<Drawable> backgroundList) {
        this.backgroundDrawableList = backgroundList;
    }
    
    public void setBackgroundDrawable(Drawable background) {
        this.backgroundDrawable = background;
    }
    
    public void setBackgroundDrawable(String resourceName) throws ResourceNotFoundException, IOException {
        this.setBackgroundDrawable(resources.getDrawable(resourceName));
    }
    
    public void setBackgroundColor(StateList<Long> backgroundList) {
        this.backgroundColorList = backgroundList;
    }
    
    public void setBackgroundColor(long background) {
        setBackgroundDrawable(new ColorDrawable(background));
    }
    
    public void setBackgroundColor(String resourceName) {
        setBackgroundColor(resources.getColor(resourceName));
    }
   
    public final int getLeft() {
        return x;
    }
    
    public final int getTop() {
        return y;
    }
    
    public final int getRight() {
        return x + width;
    }
    
    public final int getBottom() {
        return y + height;
    }
    
    public final long getId() {
        return id;
    }

    public final int getMinWidth() {
        return minWidth;
    }
    
    public final int getMinHeight() {
        return minHeight;
    }
    
    public final int getWidth() {
        return width;
    }
    
    public final int getHeight() {
        return height;
    }
    
    public void setWidth(int width) {
        if(width < MATCH_PARENT) {
            throw new RuntimeException("Width can not be less than 0");
        }
        minWidth = width;
    }
    
    public void setHeight(int height) {
        if(height < MATCH_PARENT) {
            throw new RuntimeException("Height can not be less than 0");
        }
        minWidth = height;
    }

    public final int getX() {
        return x;
    }
    
    public final int getY() {
        return y;
    }
    
    public final int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void setZ(int z) {
        this.z = z;
    }
        
    public final boolean isAttached() {
        return parent != null;
    }
    
    public final boolean isEnabled() {
        return (state & STATE_ENABLED) != 0;
    }
    
    public final void enable() {
        onStateChange(state | STATE_ENABLED);
    }
    
    public final void disable() {
        onStateChange(state & MASK_DISABLED); 
    }
    
    public final boolean isShown() {
        return shown;
    }
    
    public final int getVisibility() {
        return visibility;
    }
    
    public final void setVisibility(int visibility) {
        if(visibility < VISIBLE || visibility > GONE) {
            throw new RuntimeException("Cannot set visibility to " + visibility);
        }
        this.visibility = visibility;
    }
    
    public final boolean isVisible() {
        return visibility == VISIBLE;
    }
    
    public final boolean isPressed() {
        return (state & STATE_PRESSED) != 0;
    }
    
    public final boolean isFocused() {
        return (state & STATE_FOCUSED) != 0;
    }
    
    public final boolean isHovered() {
        return (state & STATE_HOVERED) != 0;
    }
    
    public final boolean isSelected() {
        return (state & STATE_SELECTED) != 0;
    }
    
    public final void setSelected(boolean selected) {
        if(selected) {
            onStateChange(state | STATE_SELECTED);
        } else {
            onStateChange(state & MASK_UNSELECTED);
        }
    }
    
    public abstract boolean isCheckable();
    
    public abstract boolean isClickable();
    
    public final ViewGroup getParent() {
        return parent;
    }
    
    public void draw(Canvas canvas) {
        if(backgroundDrawable != null) {
            backgroundDrawable.setBounds(x, y, width, height);
            backgroundDrawable.draw(canvas);
        } else if(backgroundDrawableList != null) {
            Drawable bg = backgroundDrawableList.getMostSimilar(state);
            bg.setBounds(x, y, width, height);
            bg.draw(canvas);
        } else if(backgroundColorList != null) {
            long color = backgroundColorList.getMostSimilar(state);
            
            Paint originPaint = canvas.getPaint();
            originPaint.setType(Paint.FILL);
            originPaint.setColor(color);
            
            canvas.drawRectangle(x, y, width, height);
        }
    }

    public abstract void redraw();
    
    public abstract View getTooltipView();
    
    public void onAttach(ViewGroup parent) {
        this.parent = parent;
    }
    
    public void onStateChange(int newState) {
        this.state = newState;
        redraw();
    }

    public void onDetach() {
        parent = null;
    }

    public void onShowStateChange(boolean shown) {
        this.shown = shown;
    }
    
    public void onHoverStateChanged(boolean nowHovered) {
        if(nowHovered) {
            onStateChange(state | STATE_HOVERED);
        } else {
            onStateChange(state & MASK_UNHOVERED);
        }
        
        if(onHoverListener != null) {
            if(nowHovered) {
                onHoverListener.onHoverStart(this);
            } else {
                onHoverListener.onHoverEnd(this);
            }
        }
    }
    
    public static final int TYPE_DOWN = 0;
    public static final int TYPE_UP   = 1;
    public static final int TYPE_MOVE = 2;
    private long mouseDownTime, mouseUpTime;
    private long lastMouseDownTime, lastMouseUpTime;
    
    public void onMouseEvent(int type, MouseEvent event) {
        switch(type) {
            case TYPE_DOWN: 
                mouseDownTime = System.currentTimeMillis();
                onStateChange(state | STATE_PRESSED); 
            break;
            case TYPE_UP:
                mouseUpTime = System.currentTimeMillis();
                onStateChange(state | MASK_UNPRESSED); 
            break;
            case TYPE_MOVE:
                if(event.getX() < getLeft() || event.getY() < getTop() ||
                   event.getX() > getRight() || event.getY() > getBottom()) {
                    if(isHovered()){
                        onHoverStateChanged(false);
                    }
                    return;
                }
                if(!isHovered()) {
                    onHoverStateChanged(true);
                }
            break;
        }
        
        if(mouseListener != null) {
            switch(type) {
                case TYPE_DOWN: mouseListener.onKeyDown(this, event); break;
                case TYPE_UP:   mouseListener.onKeyUp(this, event);   break;
                case TYPE_MOVE: mouseListener.onMove(this, event);    break;
            }
        }
        
        if(type == TYPE_UP) {
            if(onDoubleClickListener != null && 
               lastMouseUpTime - lastMouseDownTime < 50 &&
               lastMouseUpTime - mouseUpTime < 50) {
                onDoubleClickListener.onLongClick(this);
                return;
            }
            if(onClickListener != null && mouseUpTime - mouseDownTime < 50) {
                onClickListener.onClick(this, false);
            }
        }
    }
    
    public void onKeyEvent(int type, KeyEvent event) {
        switch(type) {
            case TYPE_UP:   keyListener.onKeyUp(this, event);   break;
            case TYPE_DOWN: keyListener.onKeyDown(this, event); break;
        }
    }
}
