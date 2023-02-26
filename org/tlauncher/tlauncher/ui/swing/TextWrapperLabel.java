package org.tlauncher.tlauncher.ui.swing;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/TextWrapperLabel.class */
public class TextWrapperLabel extends JTextArea {
    public TextWrapperLabel(String text) {
        super(text);
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
        setHighlighter(null);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
    }
}
