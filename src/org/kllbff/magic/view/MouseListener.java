package org.kllbff.magic.view;

import org.kllbff.magic.event.MouseEvent;

public interface MouseListener {
    public void onKeyDown(View view, MouseEvent event);
    public void onMove(View view, MouseEvent event);
    public void onKeyUp(View view, MouseEvent event);
}
