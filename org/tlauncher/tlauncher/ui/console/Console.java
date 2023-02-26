package org.tlauncher.tlauncher.ui.console;

import com.google.inject.Singleton;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.SwingUtilities;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentAdapter;
import org.tlauncher.util.OS;
import org.tlauncher.util.TlauncherUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.async.AsyncThread;
import org.tlauncher.util.pastebin.Paste;
import org.tlauncher.util.pastebin.PasteResult;

@Singleton
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/Console.class */
public class Console extends WriterAppender {
    private ConsoleFrame frame;
    private Configuration global;
    private boolean killed;
    private volatile MinecraftLauncher launcher;

    public void init(final Configuration global, boolean show) {
        this.global = global;
        this.frame = new ConsoleFrame(this);
        update();
        updateLocale();
        this.frame.addWindowListener(new WindowAdapter() { // from class: org.tlauncher.tlauncher.ui.console.Console.1
            public void windowClosing(WindowEvent e) {
                Console.this.setShown(false);
                global.set("gui.console", ConsoleType.NONE);
            }

            public void windowClosed(WindowEvent e) {
                U.log("Console", Console.this.name, "has been disposed.");
            }
        });
        this.frame.addComponentListener(new ExtendedComponentAdapter(this.frame) { // from class: org.tlauncher.tlauncher.ui.console.Console.2
            @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentAdapter
            public void componentShown(ComponentEvent e) {
                Console.this.save(true);
            }

            @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentAdapter
            public void componentHidden(ComponentEvent e) {
                Console.this.save(true);
            }

            @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentAdapter, org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentListener
            public void onComponentResized(ComponentEvent e) {
                Console.this.save(true);
            }

            @Override // org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentAdapter, org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentListener
            public void onComponentMoved(ComponentEvent e) {
                Console.this.save(true);
            }
        });
        this.frame.addComponentListener(new ComponentListener() { // from class: org.tlauncher.tlauncher.ui.console.Console.3
            public void componentResized(ComponentEvent e) {
                Console.this.save(false);
            }

            public void componentMoved(ComponentEvent e) {
                Console.this.save(false);
            }

            public void componentShown(ComponentEvent e) {
                Console.this.save(true);
            }

            public void componentHidden(ComponentEvent e) {
                Console.this.save(true);
            }
        });
        activateOptions();
        if (show) {
            show();
        }
    }

    void update() {
        check();
        if (this.global == null) {
            return;
        }
        int width = this.global.getInteger("gui.console.width", ConsoleFrame.MIN_WIDTH);
        int height = this.global.getInteger("gui.console.height", 500);
        int x = this.global.getInteger("gui.console.x", 0);
        int y = this.global.getInteger("gui.console.y", 0);
        this.frame.setSize(width, height);
        this.frame.setLocation(x, y);
    }

    void save() {
        save(false);
    }

    void save(boolean flush) {
        check();
        if (this.global == null) {
            return;
        }
        int[] size = getSize();
        int[] position = getPosition();
        this.global.set("gui.console.width", Integer.valueOf(size[0]), false);
        this.global.set("gui.console.height", Integer.valueOf(size[1]), false);
        this.global.set("gui.console.x", Integer.valueOf(position[0]), false);
        this.global.set("gui.console.y", Integer.valueOf(position[1]), false);
    }

    private void check() {
        if (this.killed) {
            throw new IllegalStateException("Console is already killed!");
        }
    }

    public void setShown(boolean shown) {
        if (shown) {
            show();
        } else {
            hide();
        }
    }

    public void show() {
        show(true);
    }

    public void show(boolean toFront) {
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
              (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r3v0 'this' org.tlauncher.tlauncher.ui.console.Console A[D('this' org.tlauncher.tlauncher.ui.console.Console), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r4v0 'toFront' boolean A[D('toFront' boolean), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.console.Console), (r1 I:boolean) type: DIRECT call: org.tlauncher.tlauncher.ui.console.Console.lambda$show$0(boolean):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.console.Console.show(boolean):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/console/Console.class
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:377)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:306)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:272)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(Unknown Source)
            	at java.base/java.util.ArrayList.forEach(Unknown Source)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(Unknown Source)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Unknown Source)
            Caused by: java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
            	at java.base/jdk.internal.util.Preconditions.outOfBounds(Unknown Source)
            	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Unknown Source)
            	at java.base/jdk.internal.util.Preconditions.checkIndex(Unknown Source)
            	at java.base/java.util.Objects.checkIndex(Unknown Source)
            	at java.base/java.util.ArrayList.get(Unknown Source)
            	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:959)
            	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:864)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:792)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1036)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:837)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
            	... 15 more
            */
        /*
            this = this;
            r0 = r3
            r1 = r4
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$show$0(r1);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.console.Console.show(boolean):void");
    }

    public void hide() {
        check();
        this.frame.setVisible(false);
        Logger.getRootLogger().removeAppender(this);
        this.frame.clear();
    }

    public void clear() {
        check();
        this.frame.clear();
    }

    public void sendPaste() {
        AsyncThread.execute(new Runnable() { // from class: org.tlauncher.tlauncher.ui.console.Console.4
            @Override // java.lang.Runnable
            public void run() {
                Paste paste = new Paste();
                paste.addListener(Console.this.frame);
                paste.setTitle(Console.this.frame.getTitle());
                paste.setContent(U.readFileLog());
                PasteResult result = paste.paste();
                if (result instanceof PasteResult.PasteUploaded) {
                    PasteResult.PasteUploaded uploaded = (PasteResult.PasteUploaded) result;
                    if (Alert.showLocQuestion("console.pastebin.sent", uploaded.getURL())) {
                        OS.openLink(uploaded.getURL());
                    }
                } else if (result instanceof PasteResult.PasteFailed) {
                    Throwable error = ((PasteResult.PasteFailed) result).getError();
                    if (error instanceof RuntimeException) {
                        Alert.showLocError("console.pastebin.invalid", error);
                    } else if (error instanceof IOException) {
                        Alert.showLocError("console.pastebin.failed", error);
                    }
                }
            }
        });
    }

    Point getPositionPoint() {
        check();
        return this.frame.getLocation();
    }

    int[] getPosition() {
        check();
        Point p = getPositionPoint();
        return new int[]{p.x, p.y};
    }

    Dimension getDimension() {
        check();
        return this.frame.getSize();
    }

    int[] getSize() {
        check();
        Dimension d = getDimension();
        return new int[]{d.width, d.height};
    }

    public void updateLocale() {
        this.frame.updateLocale();
    }

    public void setLauncherToKillProcess(MinecraftLauncher launcher) {
        this.launcher = launcher;
        this.frame.bottom.kill.setEnabled(launcher != null);
    }

    public void killProcess() {
        this.launcher.killProcess();
        this.frame.show(true);
    }

    @Override // org.apache.log4j.WriterAppender, org.apache.log4j.AppenderSkeleton, org.apache.log4j.spi.OptionHandler
    public void activateOptions() {
        setName("tl console");
        setLayout(U.LOG_LAYOUT);
        setThreshold(Level.INFO);
        setEncoding(TlauncherUtil.LOG_CHARSET);
        setWriter(createWriter(new OutputStream() { // from class: org.tlauncher.tlauncher.ui.console.Console.5
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            @Override // java.io.OutputStream
            public void write(int b) throws IOException {
                char c = (char) b;
                if (c == '\n') {
                    Console.this.frame.println(this.out.toString(TlauncherUtil.LOG_CHARSET));
                    this.out.reset();
                    return;
                }
                this.out.write(b);
            }
        }));
    }
}
