package org.tlauncher.tlauncher.ui.listener.mods;

import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/mods/DeferredDocumentListener.class */
public class DeferredDocumentListener implements DocumentListener {
    private final Timer timer;

    public DeferredDocumentListener(int timeOut, ActionListener listener, boolean repeats) {
        this.timer = new Timer(timeOut, listener);
        this.timer.setRepeats(repeats);
    }

    public void start() {
        this.timer.start();
    }

    public void stop() {
        this.timer.stop();
    }

    public void insertUpdate(DocumentEvent e) {
        this.timer.restart();
    }

    public void removeUpdate(DocumentEvent e) {
        this.timer.restart();
    }

    public void changedUpdate(DocumentEvent e) {
        this.timer.restart();
    }
}
