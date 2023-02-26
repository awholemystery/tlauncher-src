package org.tlauncher.tlauncher.ui.modpack.form;

import ch.qos.logback.core.CoreConstants;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.inject.Inject;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tlauncher.modpack.domain.client.CommentDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.share.TopicType;
import org.tlauncher.modpack.domain.client.site.PatternValidator;
import org.tlauncher.tlauncher.controller.CommentModpackController;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
import org.tlauncher.tlauncher.ui.modpack.DiscussionPanel;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/form/CommentCreationForm.class */
public class CommentCreationForm extends ExtendedPanel {
    @Inject
    private CommentModpackController controller;
    @Inject
    private EventBus eventBus;
    @Inject
    private Gson gson;
    private volatile CommentDTO changedComment;
    private JTextArea textArea = new JTextArea();
    private JButton boldButton = new UpdaterButton(Color.gray, "B");
    private JButton inclineButton = new UpdaterButton(Color.gray, "I");
    private JButton underscoreButton = new UpdaterButton(Color.gray, "U");
    private JButton leaveComment = new UpdaterFullButton(new Color(54, 153, 208), ColorUtil.BACKGROUND_COMBO_BOX_POPUP_SELECTED, "modpack.comment.leave", "create-modpack.png");
    private JButton close = new ImageUdaterButton(ColorUtil.BLUE_COLOR, "close-modpack.png");
    private GameType type;
    private Long topicPage;
    private Long topicId;
    private DiscussionPanel.Comment parent;
    private TopicType topicType;
    private static final Logger log = LoggerFactory.getLogger(CommentCreationForm.class);
    private static final Dimension SIZE = new Dimension(500, (int) HttpStatus.SC_MULTIPLE_CHOICES);

    public void setChangedComment(CommentDTO changedComment) {
        this.changedComment = changedComment;
    }

    public CommentDTO getChangedComment() {
        return this.changedComment;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public void setTopicPage(Long topicPage) {
        this.topicPage = topicPage;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public void setParent(DiscussionPanel.Comment parent) {
        this.parent = parent;
    }

    public void setTopicType(TopicType topicType) {
        this.topicType = topicType;
    }

    public CommentCreationForm() {
        setLayout(new FlowLayout(1, 0, 0));
        setVisible(false);
        JPanel pp = new JPanel(new FlowLayout(1, 0, 140));
        LocalizableLabel title = new LocalizableLabel("modpack.comment.creation");
        pp.setBackground(new Color(1, 1, 1, 100));
        pp.setOpaque(true);
        pp.setPreferredSize(ModpackScene.SIZE);
        setSize(ModpackScene.SIZE);
        add((Component) pp);
        JPanel panel = new JPanel();
        JPanel upPanel = new JPanel(new FlowLayout(1, 0, 0));
        JPanel upPanel1 = new ExtendedPanel((LayoutManager) new BorderLayout(0, 0));
        upPanel.setBackground(ColorUtil.BLUE_COLOR);
        upPanel1.setPreferredSize(new Dimension(500, 45));
        upPanel.setPreferredSize(new Dimension(500, 45));
        this.close.setPreferredSize(new Dimension(41, 45));
        title.setPreferredSize(new Dimension((int) HttpStatus.SC_OK, 45));
        title.setHorizontalAlignment(0);
        panel.setBackground(ColorUtil.BLUE_COLOR);
        BoxLayout l = new BoxLayout(panel, 1);
        panel.setOpaque(true);
        panel.setBackground(new Color(213, 213, 213));
        panel.setLayout(l);
        JPanel buttons = new JPanel(new FlowLayout(0));
        buttons.setOpaque(false);
        buttons.add(this.boldButton);
        buttons.add(this.inclineButton);
        buttons.add(this.underscoreButton);
        upPanel1.add(title, "Center");
        upPanel1.add(this.close, "East");
        upPanel.add(upPanel1);
        panel.add(upPanel);
        panel.add(buttons);
        panel.add(ModpackScene.createScrollWrapper(this.textArea));
        this.close.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.1
            public void mouseEntered(MouseEvent e) {
                CommentCreationForm.this.close.setBackground(new Color(60, 145, 193));
            }

            public void mouseExited(MouseEvent e) {
                CommentCreationForm.this.close.setBackground(ColorUtil.BLUE_COLOR_UNDER);
            }
        });
        this.close.addActionListener(e -> {
            setVisible(false);
        });
        JPanel p1 = new JPanel();
        p1.setOpaque(false);
        p1.add(this.leaveComment);
        panel.add(p1);
        this.leaveComment.setPreferredSize(new Dimension((int) HttpStatus.SC_OK, 50));
        panel.setPreferredSize(SIZE);
        pp.add(panel);
        this.boldButton.addActionListener(e2 -> {
            wrapText("b");
        });
        this.inclineButton.addActionListener(e3 -> {
            wrapText("em");
        });
        this.underscoreButton.addActionListener(e4 -> {
            wrapText("u");
        });
        this.textArea.setWrapStyleWord(true);
        this.textArea.setLineWrap(true);
        SwingUtil.changeFontFamily(this.leaveComment, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        SwingUtil.changeFontFamily(this.boldButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        SwingUtil.changeFontFamily(this.inclineButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        SwingUtil.changeFontFamily(this.underscoreButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        SwingUtil.changeFontFamily(title, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
        this.leaveComment.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.2
            public void mousePressed(MouseEvent e5) {
                if (!Pattern.matches(PatternValidator.POST_COMMENT, CommentCreationForm.this.textArea.getText())) {
                    CommentCreationForm.this.textArea.setBorder(BorderFactory.createLineBorder(Color.red, 1));
                    return;
                }
                CommentCreationForm.this.leaveComment.setEnabled(false);
                CompletableFuture.runAsync(() -> {
                    try {
                        if (Objects.isNull(CommentCreationForm.this.changedComment)) {
                            String res = CommentCreationForm.this.controller.saveComment(CommentCreationForm.this.textArea.getText(), CommentCreationForm.this.topicType, TLauncher.getInstance().getLang().isUSSRLocale() ? "ru" : "en", CommentCreationForm.this.type, CommentCreationForm.this.topicPage, CommentCreationForm.this.topicId);
                            SwingUtilities.invokeLater(()
                            /*  JADX ERROR: Method code generation error
                                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0058: INVOKE  
                                  (wrap: java.lang.Runnable : 0x0053: INVOKE_CUSTOM (r0v24 java.lang.Runnable A[REMOVE]) = 
                                  (r8v0 'this' org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm$2 A[D('this' org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm$2), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                                  (r0v22 'res' java.lang.String A[D('res' java.lang.String), DONT_INLINE])
                                
                                 handle type: INVOKE_DIRECT
                                 lambda: java.lang.Runnable.run():void
                                 call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm$2), (r1 I:java.lang.String) type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.2.lambda$null$0(java.lang.String):void)
                                 type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.2.lambda$mousePressed$2():void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/form/CommentCreationForm$2.class
                                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                                	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                                	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
                                	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                                	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:302)
                                	at jadx.core.dex.regions.TryCatchRegion.generate(TryCatchRegion.java:85)
                                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
                                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
                                	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:964)
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
                                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                                	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                	at jadx.core.dex.regions.Region.generate(Region.java:35)
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
                                	... 50 more
                                */
                            /*
                                this = this;
                                r0 = r8
                                org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm r0 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.this     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                org.tlauncher.modpack.domain.client.CommentDTO r0 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$300(r0)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                boolean r0 = java.util.Objects.isNull(r0)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                if (r0 == 0) goto L5e
                                r0 = r8
                                org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm r0 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.this     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                org.tlauncher.tlauncher.controller.CommentModpackController r0 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$800(r0)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r1 = r8
                                org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm r1 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.this     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                javax.swing.JTextArea r1 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$100(r1)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                java.lang.String r1 = r1.getText()     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r2 = r8
                                org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm r2 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.this     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                org.tlauncher.modpack.domain.client.share.TopicType r2 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$400(r2)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                org.tlauncher.tlauncher.rmo.TLauncher r3 = org.tlauncher.tlauncher.rmo.TLauncher.getInstance()     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                org.tlauncher.tlauncher.configuration.LangConfiguration r3 = r3.getLang()     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                boolean r3 = r3.isUSSRLocale()     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                if (r3 == 0) goto L36
                                java.lang.String r3 = "ru"
                                goto L38
                            L36:
                                java.lang.String r3 = "en"
                            L38:
                                r4 = r8
                                org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm r4 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.this     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                org.tlauncher.modpack.domain.client.share.GameType r4 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$500(r4)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r5 = r8
                                org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm r5 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.this     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                java.lang.Long r5 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$600(r5)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r6 = r8
                                org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm r6 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.this     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                java.lang.Long r6 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$700(r6)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                java.lang.String r0 = r0.saveComment(r1, r2, r3, r4, r5, r6)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r9 = r0
                                r0 = r8
                                r1 = r9
                                void r0 = () -> { // java.lang.Runnable.run():void
                                    r0.lambda$null$0(r1);
                                }     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                javax.swing.SwingUtilities.invokeLater(r0)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                goto L95
                            L5e:
                                org.tlauncher.tlauncher.ui.model.GameEntityComment r0 = new org.tlauncher.tlauncher.ui.model.GameEntityComment     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r1 = r0
                                r1.<init>()     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r9 = r0
                                r0 = r9
                                r1 = r8
                                org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm r1 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.this     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                javax.swing.JTextArea r1 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$100(r1)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                java.lang.String r1 = r1.getText()     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r0.setDescription(r1)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r0 = r8
                                org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm r0 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.this     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                org.tlauncher.tlauncher.controller.CommentModpackController r0 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$800(r0)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r1 = r8
                                org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm r1 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.this     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                org.tlauncher.modpack.domain.client.CommentDTO r1 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$300(r1)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                java.lang.Long r1 = r1.getId()     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r2 = r9
                                org.tlauncher.modpack.domain.client.CommentDTO r0 = r0.update(r1, r2)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r10 = r0
                                r0 = r8
                                org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm r0 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.this     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                com.google.common.eventbus.EventBus r0 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$900(r0)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                                r1 = r10
                                r0.post(r1)     // Catch: org.tlauncher.tlauncher.exceptions.RequiredTLAccountException -> L98 org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException -> Lb3 java.lang.Throwable -> Lbf
                            L95:
                                goto Ld3
                            L98:
                                r9 = move-exception
                                java.lang.String r0 = "modpack.right.panel.required.tl.account.title"
                                java.lang.String r1 = "modpack.right.panel.required.tl.account"
                                r2 = 1
                                java.lang.Object[] r2 = new java.lang.Object[r2]
                                r3 = r2
                                r4 = 0
                                java.lang.String r5 = "loginform.button.settings.account"
                                java.lang.String r5 = org.tlauncher.tlauncher.ui.loc.Localizable.get(r5)
                                r3[r4] = r5
                                java.lang.String r1 = org.tlauncher.tlauncher.ui.loc.Localizable.get(r1, r2)
                                r2 = 0
                                org.tlauncher.tlauncher.ui.alert.Alert.showLocError(r0, r1, r2)
                                goto Ld3
                            Lb3:
                                r9 = move-exception
                                java.lang.String r0 = "modpack.right.panel.required.tl.account.title"
                                java.lang.String r1 = "modpack.right.panel.select.account.tl"
                                r2 = 0
                                org.tlauncher.tlauncher.ui.alert.Alert.showLocError(r0, r1, r2)
                                goto Ld3
                            Lbf:
                                r9 = move-exception
                                org.slf4j.Logger r0 = org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.access$1000()
                                java.lang.String r1 = "error"
                                r2 = r9
                                r0.warn(r1, r2)
                                java.lang.String r0 = "modpack.remote.not.found"
                                java.lang.String r1 = "modpack.try.later"
                                r2 = 0
                                org.tlauncher.tlauncher.ui.alert.Alert.showLocMessage(r0, r1, r2)
                            Ld3:
                                r0 = r8
                                void r0 = () -> { // java.lang.Runnable.run():void
                                    r0.lambda$null$1();
                                }
                                javax.swing.SwingUtilities.invokeLater(r0)
                                return
                            */
                            throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.AnonymousClass2.lambda$mousePressed$2():void");
                        });
                    }
                });
                pp.addMouseMotionListener(new MouseMotionAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.3
                });
                this.textArea.addFocusListener(new FocusAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.4
                    public void focusGained(FocusEvent e5) {
                        CommentCreationForm.this.textArea.setBorder(BorderFactory.createEmptyBorder());
                    }
                });
                addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm.5
                    public void componentShown(ComponentEvent e5) {
                        CommentCreationForm.this.textArea.setBorder(BorderFactory.createEmptyBorder());
                    }
                });
            }

            private void wrapText(String tag) {
                if (this.textArea.getSelectionStart() == this.textArea.getSelectionEnd()) {
                    int index = this.textArea.getCaretPosition();
                    this.textArea.insert(String.format("<%s>%s</%s>", tag, CoreConstants.EMPTY_STRING, tag), index);
                    return;
                }
                this.textArea.insert(String.format("<%s>", tag), this.textArea.getSelectionStart());
                this.textArea.insert(String.format("</%s>", tag), this.textArea.getSelectionEnd());
            }

            public void preparedForNewComment() {
                this.parent = null;
                this.textArea.setText(CoreConstants.EMPTY_STRING);
                if (Objects.nonNull(this.changedComment)) {
                    this.textArea.setText(this.changedComment.getDescription());
                }
            }
        }
