package org.tlauncher.tlauncher.ui.swing.extended;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import org.tlauncher.tlauncher.ui.swing.util.IntegerArrayGetter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/ExtendedComponentListener.class */
public abstract class ExtendedComponentListener implements ComponentListener {
    private final Component comp;
    private final QuickParameterListenerThread resizeListener;
    private final QuickParameterListenerThread moveListener;
    private ComponentEvent lastResizeEvent;
    private ComponentEvent lastMoveEvent;

    public abstract void onComponentResizing(ComponentEvent componentEvent);

    public abstract void onComponentResized(ComponentEvent componentEvent);

    public abstract void onComponentMoving(ComponentEvent componentEvent);

    public abstract void onComponentMoved(ComponentEvent componentEvent);

    public ExtendedComponentListener(Component component, int tick) {
        if (component == null) {
            throw new NullPointerException();
        }
        this.comp = component;
        this.resizeListener = new QuickParameterListenerThread(new IntegerArrayGetter() { // from class: org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentListener.1
            @Override // org.tlauncher.tlauncher.ui.swing.util.IntegerArrayGetter
            public int[] getIntegerArray() {
                return new int[]{ExtendedComponentListener.this.comp.getWidth(), ExtendedComponentListener.this.comp.getHeight()};
            }
        }, new Runnable() { // from class: org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentListener.2
            @Override // java.lang.Runnable
            public void run() {
                ExtendedComponentListener.this.onComponentResized(ExtendedComponentListener.this.lastResizeEvent);
            }
        }, tick);
        this.moveListener = new QuickParameterListenerThread(new IntegerArrayGetter() { // from class: org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentListener.3
            @Override // org.tlauncher.tlauncher.ui.swing.util.IntegerArrayGetter
            public int[] getIntegerArray() {
                Point location = ExtendedComponentListener.this.comp.getLocation();
                return new int[]{location.x, location.y};
            }
        }, new Runnable() { // from class: org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentListener.4
            @Override // java.lang.Runnable
            public void run() {
                ExtendedComponentListener.this.onComponentMoved(ExtendedComponentListener.this.lastMoveEvent);
            }
        }, tick);
    }

    public ExtendedComponentListener(Component component) {
        this(component, 500);
    }

    public final void componentResized(ComponentEvent e) {
        onComponentResizing(e);
        this.resizeListener.startListening();
    }

    public final void componentMoved(ComponentEvent e) {
        onComponentMoving(e);
        this.moveListener.startListening();
    }

    public boolean isListening() {
        return this.resizeListener.isIterating() || this.moveListener.isIterating();
    }
}
