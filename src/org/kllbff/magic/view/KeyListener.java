package org.kllbff.magic.view;

import org.kllbff.magic.event.KeyEvent;

public interface KeyListener {
    public boolean onKeyUp(View view, KeyEvent keyEvent);
    public boolean onKeyDown(View view, KeyEvent keyEvent);
}
