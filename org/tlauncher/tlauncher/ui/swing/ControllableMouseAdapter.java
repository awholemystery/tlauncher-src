package org.tlauncher.tlauncher.ui.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/ControllableMouseAdapter.class */
public class ControllableMouseAdapter implements MouseListener {
    private MouseEventHandler click;
    private MouseEventHandler press;
    private MouseEventHandler release;
    private MouseEventHandler enter;
    private MouseEventHandler exit;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/ControllableMouseAdapter$MouseEventHandler.class */
    public interface MouseEventHandler {
        void handleEvent(MouseEvent mouseEvent);
    }

    public MouseEventHandler getOnClick() {
        return this.click;
    }

    public ControllableMouseAdapter setOnClick(MouseEventHandler handler) {
        this.click = handler;
        return this;
    }

    public MouseEventHandler getOnPress() {
        return this.press;
    }

    public ControllableMouseAdapter setOnPress(MouseEventHandler handler) {
        this.press = handler;
        return this;
    }

    public MouseEventHandler getOnRelease() {
        return this.release;
    }

    public ControllableMouseAdapter setOnRelease(MouseEventHandler handler) {
        this.release = handler;
        return this;
    }

    public MouseEventHandler getOnEnter() {
        return this.enter;
    }

    public ControllableMouseAdapter setOnEnter(MouseEventHandler handler) {
        this.enter = handler;
        return this;
    }

    public MouseEventHandler getOnExit() {
        return this.exit;
    }

    public ControllableMouseAdapter setOnExit(MouseEventHandler handler) {
        this.exit = handler;
        return this;
    }

    public final void mouseClicked(MouseEvent e) {
        if (this.click != null) {
            this.click.handleEvent(e);
        }
    }

    public final void mousePressed(MouseEvent e) {
        if (this.press != null) {
            this.press.handleEvent(e);
        }
    }

    public final void mouseReleased(MouseEvent e) {
        if (this.release != null) {
            this.release.handleEvent(e);
        }
    }

    public final void mouseEntered(MouseEvent e) {
        if (this.enter != null) {
            this.enter.handleEvent(e);
        }
    }

    public final void mouseExited(MouseEvent e) {
        if (this.exit != null) {
            this.exit.handleEvent(e);
        }
    }
}
