package org.tlauncher.tlauncher.ui.swing.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Dictionary;
import javax.imageio.ImageIO;
import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.GlyphView;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.InlineView;
import javax.swing.text.html.StyleSheet;
import javax.xml.bind.DatatypeConverter;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/editor/ExtendedImageView.class */
public class ExtendedImageView extends View {
    private static String base64s = "data:image/";
    private static String base64e = ";base64,";
    private static boolean sIsInc = false;
    private static int sIncRate = 100;
    private static final String PENDING_IMAGE = "html.pendingImage";
    private static final String MISSING_IMAGE = "html.missingImage";
    private static final String IMAGE_CACHE_PROPERTY = "imageCache";
    private static final int DEFAULT_WIDTH = 38;
    private static final int DEFAULT_HEIGHT = 38;
    private static final int LOADING_FLAG = 1;
    private static final int LINK_FLAG = 2;
    private static final int WIDTH_FLAG = 4;
    private static final int HEIGHT_FLAG = 8;
    private static final int RELOAD_FLAG = 16;
    private static final int RELOAD_IMAGE_FLAG = 32;
    private static final int SYNC_LOAD_FLAG = 64;
    private AttributeSet attr;
    private Image image;
    private Image disabledImage;
    private int width;
    private int height;
    private int state;
    private Container container;
    private Rectangle fBounds;
    private Color borderColor;
    private short borderSize;
    private short leftInset;
    private short rightInset;
    private short topInset;
    private short bottomInset;
    private ImageObserver imageObserver;
    private View altView;
    private float vAlign;

    public ExtendedImageView(Element elem) {
        super(elem);
        this.fBounds = new Rectangle();
        this.imageObserver = new ImageHandler();
        this.state = 48;
    }

    public String getAltText() {
        return (String) getElement().getAttributes().getAttribute(HTML.Attribute.ALT);
    }

    public String getImageSource() {
        return (String) getElement().getAttributes().getAttribute(HTML.Attribute.SRC);
    }

    public Icon getNoImageIcon() {
        return (Icon) UIManager.getLookAndFeelDefaults().get(MISSING_IMAGE);
    }

    public Icon getLoadingImageIcon() {
        return (Icon) UIManager.getLookAndFeelDefaults().get(PENDING_IMAGE);
    }

    public Image getImage() {
        sync();
        return this.image;
    }

    private Image getImage(boolean enabled) {
        Image img = getImage();
        if (!enabled) {
            if (this.disabledImage == null) {
                this.disabledImage = GrayFilter.createDisabledImage(img);
            }
            img = this.disabledImage;
        }
        return img;
    }

    public void setLoadsSynchronously(boolean newValue) {
        synchronized (this) {
            if (newValue) {
                this.state |= 64;
            } else {
                this.state = (this.state | 64) ^ 64;
            }
        }
    }

    public boolean getLoadsSynchronously() {
        return (this.state & 64) != 0;
    }

    protected StyleSheet getStyleSheet() {
        HTMLDocument doc = getDocument();
        return doc.getStyleSheet();
    }

    public AttributeSet getAttributes() {
        sync();
        return this.attr;
    }

    public String getToolTipText(float x, float y, Shape allocation) {
        return getAltText();
    }

    protected void setPropertiesFromAttributes() {
        StyleSheet sheet = getStyleSheet();
        this.attr = sheet.getViewAttributes(this);
        this.borderSize = (short) getIntAttr(HTML.Attribute.BORDER, 0);
        short intAttr = (short) (getIntAttr(HTML.Attribute.HSPACE, 0) + this.borderSize);
        this.rightInset = intAttr;
        this.leftInset = intAttr;
        short intAttr2 = (short) (getIntAttr(HTML.Attribute.VSPACE, 0) + this.borderSize);
        this.bottomInset = intAttr2;
        this.topInset = intAttr2;
        this.borderColor = getDocument().getForeground(getAttributes());
        AttributeSet attr = getElement().getAttributes();
        Object alignment = attr.getAttribute(HTML.Attribute.ALIGN);
        this.vAlign = 1.0f;
        if (alignment != null) {
            Object alignment2 = alignment.toString();
            if ("top".equals(alignment2)) {
                this.vAlign = 0.0f;
            } else if ("middle".equals(alignment2)) {
                this.vAlign = 0.5f;
            }
        }
        AttributeSet anchorAttr = (AttributeSet) attr.getAttribute(HTML.Tag.A);
        if (anchorAttr != null && anchorAttr.isDefined(HTML.Attribute.HREF)) {
            synchronized (this) {
                this.state |= 2;
            }
            return;
        }
        synchronized (this) {
            this.state = (this.state | 2) ^ 2;
        }
    }

    public void setParent(View parent) {
        View oldParent = getParent();
        super.setParent(parent);
        this.container = parent != null ? getContainer() : null;
        if (oldParent != parent) {
            synchronized (this) {
                this.state |= 16;
            }
        }
    }

    public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f) {
        super.changedUpdate(e, a, f);
        synchronized (this) {
            this.state |= 48;
        }
        preferenceChanged(null, true, true);
    }

    public void paint(Graphics g, Shape a) {
        sync();
        Rectangle rect = a instanceof Rectangle ? (Rectangle) a : a.getBounds();
        Rectangle clip = g.getClipBounds();
        this.fBounds.setBounds(rect);
        paintHighlights(g, a);
        paintBorder(g, rect);
        if (clip != null) {
            g.clipRect(rect.x + this.leftInset, rect.y + this.topInset, (rect.width - this.leftInset) - this.rightInset, (rect.height - this.topInset) - this.bottomInset);
        }
        Container host = getContainer();
        Image img = getImage(host == null || host.isEnabled());
        if (img != null) {
            if (!hasPixels(img)) {
                Icon icon = getLoadingImageIcon();
                if (icon != null) {
                    icon.paintIcon(host, g, rect.x + this.leftInset, rect.y + this.topInset);
                }
            } else {
                g.drawImage(img, rect.x + this.leftInset, rect.y + this.topInset, this.width, this.height, this.imageObserver);
            }
        } else {
            Icon icon2 = getNoImageIcon();
            if (icon2 != null) {
                icon2.paintIcon(host, g, rect.x + this.leftInset, rect.y + this.topInset);
            }
            View view = getAltView();
            if (view != null && ((this.state & 4) == 0 || this.width > 38)) {
                Rectangle altRect = new Rectangle(rect.x + this.leftInset + 38, rect.y + this.topInset, ((rect.width - this.leftInset) - this.rightInset) - 38, (rect.height - this.topInset) - this.bottomInset);
                view.paint(g, altRect);
            }
        }
        if (clip != null) {
            g.setClip(clip.x, clip.y, clip.width, clip.height);
        }
    }

    private void paintHighlights(Graphics g, Shape shape) {
        if (this.container instanceof JTextComponent) {
            JTextComponent tc = this.container;
            LayeredHighlighter highlighter = tc.getHighlighter();
            if (highlighter instanceof LayeredHighlighter) {
                highlighter.paintLayeredHighlights(g, getStartOffset(), getEndOffset(), shape, tc, this);
            }
        }
    }

    private void paintBorder(Graphics g, Rectangle rect) {
        Color color = this.borderColor;
        if ((this.borderSize > 0 || this.image == null) && color != null) {
            int xOffset = this.leftInset - this.borderSize;
            int yOffset = this.topInset - this.borderSize;
            g.setColor(color);
            int n = this.image == null ? (short) 1 : this.borderSize;
            for (int counter = 0; counter < n; counter++) {
                g.drawRect(rect.x + xOffset + counter, rect.y + yOffset + counter, ((((rect.width - counter) - counter) - xOffset) - xOffset) - 1, ((((rect.height - counter) - counter) - yOffset) - yOffset) - 1);
            }
        }
    }

    public float getPreferredSpan(int axis) {
        sync();
        if (axis == 0 && (this.state & 4) == 4) {
            getPreferredSpanFromAltView(axis);
            return this.width + this.leftInset + this.rightInset;
        } else if (axis == 1 && (this.state & 8) == 8) {
            getPreferredSpanFromAltView(axis);
            return this.height + this.topInset + this.bottomInset;
        } else {
            Image image = getImage();
            if (image != null) {
                switch (axis) {
                    case 0:
                        return this.width + this.leftInset + this.rightInset;
                    case 1:
                        return this.height + this.topInset + this.bottomInset;
                    default:
                        throw new IllegalArgumentException("Invalid axis: " + axis);
                }
            }
            View view = getAltView();
            float retValue = 0.0f;
            if (view != null) {
                retValue = view.getPreferredSpan(axis);
            }
            switch (axis) {
                case 0:
                    return retValue + this.width + this.leftInset + this.rightInset;
                case 1:
                    return retValue + this.height + this.topInset + this.bottomInset;
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }
    }

    public float getAlignment(int axis) {
        switch (axis) {
            case 1:
                return this.vAlign;
            default:
                return super.getAlignment(axis);
        }
    }

    public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
        int p0 = getStartOffset();
        int p1 = getEndOffset();
        if (pos >= p0 && pos <= p1) {
            Rectangle r = a.getBounds();
            if (pos == p1) {
                r.x += r.width;
            }
            r.width = 0;
            return r;
        }
        return null;
    }

    public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
        Rectangle alloc = (Rectangle) a;
        if (x < alloc.x + alloc.width) {
            bias[0] = Position.Bias.Forward;
            return getStartOffset();
        }
        bias[0] = Position.Bias.Backward;
        return getEndOffset();
    }

    public void setSize(float width, float height) {
        View view;
        sync();
        if (getImage() == null && (view = getAltView()) != null) {
            view.setSize(Math.max(0.0f, width - ((38 + this.leftInset) + this.rightInset)), Math.max(0.0f, height - (this.topInset + this.bottomInset)));
        }
    }

    private boolean hasPixels(Image image) {
        return image != null && image.getHeight(this.imageObserver) > 0 && image.getWidth(this.imageObserver) > 0;
    }

    private float getPreferredSpanFromAltView(int axis) {
        View view;
        if (getImage() == null && (view = getAltView()) != null) {
            return view.getPreferredSpan(axis);
        }
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void repaint(long delay) {
        if (this.container != null && this.fBounds != null) {
            this.container.repaint(delay, this.fBounds.x, this.fBounds.y, this.fBounds.width, this.fBounds.height);
        }
    }

    private int getIntAttr(HTML.Attribute name, int deflt) {
        int i;
        AttributeSet attr = getElement().getAttributes();
        if (attr.isDefined(name)) {
            String val = (String) attr.getAttribute(name);
            if (val == null) {
                i = deflt;
            } else {
                try {
                    i = Math.max(0, Integer.parseInt(val));
                } catch (NumberFormatException e) {
                    i = deflt;
                }
            }
            return i;
        }
        return deflt;
    }

    private void sync() {
        int s = this.state;
        if ((s & 32) != 0) {
            refreshImage();
        }
        int s2 = this.state;
        if ((s2 & 16) != 0) {
            synchronized (this) {
                this.state = (this.state | 16) ^ 16;
            }
            setPropertiesFromAttributes();
        }
    }

    private void refreshImage() {
        synchronized (this) {
            this.state = ((((this.state | 1) | 32) | 4) | 8) ^ 44;
            this.image = null;
            this.height = 0;
            this.width = 0;
        }
        try {
            loadImage();
            updateImageSize();
            synchronized (this) {
                this.state = (this.state | 1) ^ 1;
            }
        } catch (Throwable th) {
            synchronized (this) {
                this.state = (this.state | 1) ^ 1;
                throw th;
            }
        }
    }

    private void loadImage() {
        try {
            this.image = loadNewImage();
        } catch (Exception e) {
            this.image = null;
            e.printStackTrace();
        }
    }

    private Image loadNewImage() throws Exception {
        Image newImage;
        int startPoint;
        String source = getImageSource();
        if (source == null) {
            return null;
        }
        if (source.startsWith(base64s)) {
            int startPoint2 = base64s.length();
            String imageType = source.substring(startPoint2, startPoint2 + 4);
            if (imageType.startsWith("png") || imageType.startsWith("jpg")) {
                startPoint = startPoint2 + 3;
            } else if (imageType.equals("jpeg")) {
                startPoint = startPoint2 + 4;
            } else {
                return null;
            }
            if (!source.substring(startPoint, startPoint + base64e.length()).equals(base64e)) {
                return null;
            }
            byte[] bytes = DatatypeConverter.parseBase64Binary(source.substring(startPoint + base64e.length()));
            return ImageIO.read(new ByteArrayInputStream(bytes));
        }
        URL src = U.makeURL(source);
        if (src == null) {
            return null;
        }
        Dictionary<?, ?> cache = (Dictionary) getDocument().getProperty(IMAGE_CACHE_PROPERTY);
        if (cache != null) {
            newImage = (Image) cache.get(src);
        } else {
            newImage = Toolkit.getDefaultToolkit().createImage(src);
            if (newImage != null && getLoadsSynchronously()) {
                ImageIcon ii = new ImageIcon();
                ii.setImage(newImage);
            }
        }
        return newImage;
    }

    private void updateImageSize() {
        int newState = 0;
        Image newImage = getImage();
        if (newImage != null) {
            int newWidth = getIntAttr(HTML.Attribute.WIDTH, -1);
            if (newWidth > 0) {
                newState = 0 | 4;
            }
            int newHeight = getIntAttr(HTML.Attribute.HEIGHT, -1);
            if (newHeight > 0) {
                newState |= 8;
            }
            if (newWidth <= 0) {
                newWidth = newImage.getWidth(this.imageObserver);
                if (newWidth <= 0) {
                    newWidth = 38;
                }
            }
            if (newHeight <= 0) {
                newHeight = newImage.getHeight(this.imageObserver);
                if (newHeight <= 0) {
                    newHeight = 38;
                }
            }
            if ((newState & 12) != 0) {
                Toolkit.getDefaultToolkit().prepareImage(newImage, newWidth, newHeight, this.imageObserver);
            } else {
                Toolkit.getDefaultToolkit().prepareImage(newImage, -1, -1, this.imageObserver);
            }
            boolean createText = false;
            synchronized (this) {
                if (this.image != null) {
                    if ((newState & 4) == 4 || this.width == 0) {
                        this.width = newWidth;
                    }
                    if ((newState & 8) == 8 || this.height == 0) {
                        this.height = newHeight;
                    }
                } else {
                    createText = true;
                    if ((newState & 4) == 4) {
                        this.width = newWidth;
                    }
                    if ((newState & 8) == 8) {
                        this.height = newHeight;
                    }
                }
                this.state |= newState;
                this.state = (this.state | 1) ^ 1;
            }
            if (createText) {
                updateAltTextView();
                return;
            }
            return;
        }
        this.height = 38;
        this.width = 38;
        updateAltTextView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAltTextView() {
        String text = getAltText();
        if (text != null) {
            ImageLabelView newView = new ImageLabelView(getElement(), text);
            synchronized (this) {
                this.altView = newView;
            }
        }
    }

    private View getAltView() {
        View view;
        synchronized (this) {
            view = this.altView;
        }
        if (view != null && view.getParent() == null) {
            view.setParent(getParent());
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void safePreferenceChanged() {
        if (SwingUtilities.isEventDispatchThread()) {
            AbstractDocument document = getDocument();
            if (document instanceof AbstractDocument) {
                document.readLock();
            }
            preferenceChanged(null, true, true);
            if (document instanceof AbstractDocument) {
                document.readUnlock();
                return;
            }
            return;
        }
        SwingUtilities.invokeLater(new Runnable() { // from class: org.tlauncher.tlauncher.ui.swing.editor.ExtendedImageView.1
            @Override // java.lang.Runnable
            public void run() {
                ExtendedImageView.this.safePreferenceChanged();
            }
        });
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/editor/ExtendedImageView$ImageHandler.class */
    private class ImageHandler implements ImageObserver {
        private ImageHandler() {
        }

        public boolean imageUpdate(Image img, int flags, int x, int y, int newWidth, int newHeight) {
            if ((img != ExtendedImageView.this.image && img != ExtendedImageView.this.disabledImage) || ExtendedImageView.this.image == null || ExtendedImageView.this.getParent() == null) {
                return false;
            }
            if ((flags & 192) != 0) {
                ExtendedImageView.this.repaint(0L);
                synchronized (ExtendedImageView.this) {
                    if (ExtendedImageView.this.image == img) {
                        ExtendedImageView.this.image = null;
                        if ((ExtendedImageView.this.state & 4) != 4) {
                            ExtendedImageView.this.width = 38;
                        }
                        if ((ExtendedImageView.this.state & 8) != 8) {
                            ExtendedImageView.this.height = 38;
                        }
                    } else {
                        ExtendedImageView.this.disabledImage = null;
                    }
                    if ((ExtendedImageView.this.state & 1) != 1) {
                        ExtendedImageView.this.updateAltTextView();
                        ExtendedImageView.this.safePreferenceChanged();
                        return false;
                    }
                    return false;
                }
            }
            if (ExtendedImageView.this.image == img) {
                short changed = 0;
                if ((flags & 2) != 0 && !ExtendedImageView.this.getElement().getAttributes().isDefined(HTML.Attribute.HEIGHT)) {
                    changed = (short) (0 | 1);
                }
                if ((flags & 1) != 0 && !ExtendedImageView.this.getElement().getAttributes().isDefined(HTML.Attribute.WIDTH)) {
                    changed = (short) (changed | 2);
                }
                synchronized (ExtendedImageView.this) {
                    if ((changed & 1) == 1 && (ExtendedImageView.this.state & 4) == 0) {
                        ExtendedImageView.this.width = newWidth;
                    }
                    if ((changed & 2) == 2 && (ExtendedImageView.this.state & 8) == 0) {
                        ExtendedImageView.this.height = newHeight;
                    }
                    if ((ExtendedImageView.this.state & 1) == 1) {
                        return true;
                    }
                    if (changed != 0) {
                        ExtendedImageView.this.safePreferenceChanged();
                        return true;
                    }
                }
            }
            if ((flags & 48) != 0) {
                ExtendedImageView.this.repaint(0L);
            } else if ((flags & 8) != 0 && ExtendedImageView.sIsInc) {
                ExtendedImageView.this.repaint(ExtendedImageView.sIncRate);
            }
            return (flags & 32) == 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/editor/ExtendedImageView$ImageLabelView.class */
    public class ImageLabelView extends InlineView {
        private Segment segment;
        private Color fg;

        ImageLabelView(Element e, String text) {
            super(e);
            reset(text);
        }

        public void reset(String text) {
            this.segment = new Segment(text.toCharArray(), 0, text.length());
        }

        public void paint(Graphics g, Shape a) {
            GlyphView.GlyphPainter painter = getGlyphPainter();
            if (painter != null) {
                g.setColor(getForeground());
                painter.paint(this, g, a, getStartOffset(), getEndOffset());
            }
        }

        public Segment getText(int p0, int p1) {
            if (p0 < 0 || p1 > this.segment.array.length) {
                throw new RuntimeException("ImageLabelView: Stale view");
            }
            this.segment.offset = p0;
            this.segment.count = p1 - p0;
            return this.segment;
        }

        public int getStartOffset() {
            return 0;
        }

        public int getEndOffset() {
            return this.segment.array.length;
        }

        public View breakView(int axis, int p0, float pos, float len) {
            return this;
        }

        public Color getForeground() {
            View parent;
            if (this.fg == null && (parent = getParent()) != null) {
                StyledDocument document = getDocument();
                AttributeSet attr = parent.getAttributes();
                if (attr != null && (document instanceof StyledDocument)) {
                    this.fg = document.getForeground(attr);
                }
            }
            return this.fg;
        }
    }
}
