package org.tlauncher.tlauncher.updater.bootstrapper.view;

import ch.qos.logback.core.CoreConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import org.tlauncher.tlauncher.configuration.LangConfiguration;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.Downloader;
import org.tlauncher.tlauncher.downloader.DownloaderListener;
import org.tlauncher.tlauncher.ui.TLauncherFrame;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/view/DownloadingFrameElement.class */
public class DownloadingFrameElement extends JFrame implements DownloaderListener {
    private final JProgressBar bar = new JProgressBar();
    private final JLabel label;
    private final LangConfiguration langConfiguration;

    public DownloadingFrameElement(LangConfiguration langConfiguration) {
        this.langConfiguration = langConfiguration;
        JPanel pan = new UpdaterPanelProgressBar();
        pan.setLayout(new BorderLayout());
        pan.setOpaque(false);
        this.label = new JLabel(langConfiguration.get("updater.frame.name"));
        this.label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        this.label.setFont(this.label.getFont().deriveFont(TLauncherFrame.fontSize));
        pan.add(this.label, "North");
        pan.add(this.bar, "Center");
        pan.add(new JLabel(CoreConstants.EMPTY_STRING), "South");
        setSize(new Dimension(350, 60));
        setResizable(false);
        setUndecorated(true);
        setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        add(pan);
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderStart(Downloader d, int files) {
        setVisible(true);
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderAbort(Downloader d) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderProgress(Downloader d, double progress, double speed, double alreadyDownloaded) {
        SwingUtilities.invokeLater(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0009: INVOKE  
              (wrap: java.lang.Runnable : 0x0004: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
              (r6v0 'this' org.tlauncher.tlauncher.updater.bootstrapper.view.DownloadingFrameElement A[D('this' org.tlauncher.tlauncher.updater.bootstrapper.view.DownloadingFrameElement), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r8v0 'progress' double A[D('progress' double), DONT_INLINE])
              (r12v0 'alreadyDownloaded' double A[D('alreadyDownloaded' double), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: java.lang.Runnable.run():void
             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.updater.bootstrapper.view.DownloadingFrameElement), (r1 I:double), (r2 I:double) type: DIRECT call: org.tlauncher.tlauncher.updater.bootstrapper.view.DownloadingFrameElement.lambda$onDownloaderProgress$0(double, double):void)
             type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.updater.bootstrapper.view.DownloadingFrameElement.onDownloaderProgress(org.tlauncher.tlauncher.downloader.Downloader, double, double, double):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/updater/bootstrapper/view/DownloadingFrameElement.class
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
            Caused by: java.lang.IndexOutOfBoundsException: Index 2 out of bounds for length 2
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
            r0 = r6
            r1 = r8
            r2 = r12
            void r0 = () -> { // java.lang.Runnable.run():void
                r0.lambda$onDownloaderProgress$0(r1, r2);
            }
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.updater.bootstrapper.view.DownloadingFrameElement.onDownloaderProgress(org.tlauncher.tlauncher.downloader.Downloader, double, double, double):void");
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderFileComplete(Downloader d, Downloadable file) {
    }

    @Override // org.tlauncher.tlauncher.downloader.DownloaderListener
    public void onDownloaderComplete(Downloader d) {
        setVisible(false);
        dispose();
    }
}
