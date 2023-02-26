package org.tlauncher.tlauncher.ui.review;

import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/review/ReviewJTextArea.class */
public class ReviewJTextArea extends JTextArea {
    public ReviewJTextArea() {
    }

    public ReviewJTextArea(int rows, int column) {
        super(rows, column);
    }

    protected final Document createDefaultModel() {
        return new PlainDocument() { // from class: org.tlauncher.tlauncher.ui.review.ReviewJTextArea.1
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (ReviewJTextArea.this.getText().length() < 1500) {
                    super.insertString(offs, str, a);
                }
            }
        };
    }
}
