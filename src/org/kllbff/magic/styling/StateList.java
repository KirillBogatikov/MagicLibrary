package org.kllbff.magic.styling;

public class StateList<V> {
    private V disabledChecked, disabledUnchecked;
    private V pressedChecked, pressedUnchecked;
    private V focusedChecked, focusedUnchecked;
    private V checked, unchecked;
    
    public StateList(V dc, V du, V pc, V pu, V fc, V fu, V c, V u) {
        this.disabledChecked = dc;
        this.disabledUnchecked = du;
        this.pressedChecked = pc;
        this.pressedUnchecked = pu;
        this.focusedChecked = fc;
        this.focusedUnchecked = fu;
        this.checked = c;
        this.unchecked = u;
    }
    
    public V getDisabled(boolean checked) {
        if(checked && disabledChecked != null) {
            return disabledChecked;
        }
        return disabledUnchecked;
    }
    
    public V getPressed(boolean checked) {
        if(checked && pressedChecked != null) {
            return pressedChecked;
        }
        return pressedUnchecked;
    }
    
    public V getFocused(boolean checked) {
        if(checked && focusedChecked != null) {
            return focusedChecked;
        }
        return focusedUnchecked;
    }
    
    public V getDefault(boolean checked) {
        if(checked && this.checked != null) {
            return this.checked;
        }
        return unchecked;
    }
}
