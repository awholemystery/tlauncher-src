package org.tlauncher.tlauncher.ui.modpack;

import ch.qos.logback.core.CoreConstants;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tlauncher.modpack.domain.client.CommentDTO;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.share.TopicType;
import org.tlauncher.modpack.domain.client.site.CommonPage;
import org.tlauncher.tlauncher.controller.CommentModpackController;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
import org.tlauncher.tlauncher.ui.model.CurrentUserPosition;
import org.tlauncher.tlauncher.ui.model.InsertCommentDTO;
import org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm;
import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
import org.tlauncher.util.ColorUtil;
import org.tlauncher.util.SwingUtil;
import org.tlauncher.util.swing.FontTL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/DiscussionPanel.class */
public class DiscussionPanel extends ExtendedPanel {
    private static final Logger log = LoggerFactory.getLogger(DiscussionPanel.class);
    private static final Object ob = new Object();
    private static final long serialVersionUID = -6938564413758462564L;
    private GameEntityDTO dto;
    private GameType type;
    private boolean first;
    @Inject
    private CommentModpackController controller;
    @Named("singleDownloadExecutor")
    @Inject
    private Executor singleDownloadExecutor;
    private CommentCreationForm commentCreationForm;
    private JScrollPane scrollPane;
    public final SimpleDateFormat format = new SimpleDateFormat(" dd.MM.YYYY HH:MM", Localizable.get().getSelected());
    private int oneGapeCommentWidth = 25;
    private int commentDescriptionWidth = 695;
    int v = 210;
    private JButton addedComment = new UpdaterButton(new Color(this.v, this.v, this.v), new Color(this.v - 30, this.v - 30, this.v - 30), "modpack.comment.leave");
    private Component gapeSpace;

    public void setDto(GameEntityDTO dto) {
        this.dto = dto;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public void setCommentCreationForm(CommentCreationForm commentCreationForm) {
        this.commentCreationForm = commentCreationForm;
    }

    public JScrollPane getScrollPane() {
        return this.scrollPane;
    }

    public DiscussionPanel() {
        setLayout(new BoxLayout(this, 3));
        this.scrollPane = ModpackScene.createScrollWrapper(this);
        this.scrollPane.addComponentListener(new ComponentAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.1
            public void componentShown(ComponentEvent e) {
                if (!DiscussionPanel.this.first) {
                    DiscussionPanel.this.preparingContent(DiscussionPanel.this.dto.getId(), 0, 1, TopicType.GAME_ENTITY, 0);
                }
            }
        });
        this.scrollPane.setPreferredSize(new Dimension(ModpackScene.SIZE.width, 500));
        JPanel ex = new ExtendedPanel((LayoutManager) new FlowLayout(2, 0, 0));
        ex.setBorder(BorderFactory.createEmptyBorder(3, 3, 15, 92));
        ex.add(this.addedComment);
        ex.setBackground(Color.GREEN);
        SwingUtil.changeFontFamily(this.addedComment, FontTL.ROBOTO_REGULAR, 15, Color.WHITE);
        add((Component) ex);
        this.addedComment.addActionListener(c -> {
            SwingUtilities.invokeLater(() -> {
                this.commentCreationForm.setChangedComment(null);
                this.commentCreationForm.preparedForNewComment();
                this.commentCreationForm.setTopicPage(this.dto.getId());
                this.commentCreationForm.setTopicId(this.dto.getId());
                this.commentCreationForm.setType(this.type);
                this.commentCreationForm.setTopicType(TopicType.GAME_ENTITY);
                this.commentCreationForm.setVisible(true);
            });
        });
        this.gapeSpace = Box.createVerticalStrut(1);
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/DiscussionPanel$Comment.class */
    public class Comment extends ExtendedPanel {
        private static final long serialVersionUID = 6313239430482145868L;
        private int gape;
        private volatile CommentDTO comment;
        private JPanel centerComment;
        private HtmlTextPane descriptionFull;

        public void setGape(int gape) {
            this.gape = gape;
        }

        public void setComment(CommentDTO comment) {
            this.comment = comment;
        }

        public void setCenterComment(JPanel centerComment) {
            this.centerComment = centerComment;
        }

        public void setDescriptionFull(HtmlTextPane descriptionFull) {
            this.descriptionFull = descriptionFull;
        }

        public String toString() {
            return "DiscussionPanel.Comment(gape=" + getGape() + ", comment=" + getComment() + ", centerComment=" + getCenterComment() + ", descriptionFull=" + getDescriptionFull() + ")";
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Comment) {
                Comment other = (Comment) o;
                if (other.canEqual(this) && super/*java.lang.Object*/.equals(o) && getGape() == other.getGape()) {
                    Object this$comment = getComment();
                    Object other$comment = other.getComment();
                    if (this$comment == null) {
                        if (other$comment != null) {
                            return false;
                        }
                    } else if (!this$comment.equals(other$comment)) {
                        return false;
                    }
                    Object this$centerComment = getCenterComment();
                    Object other$centerComment = other.getCenterComment();
                    if (this$centerComment == null) {
                        if (other$centerComment != null) {
                            return false;
                        }
                    } else if (!this$centerComment.equals(other$centerComment)) {
                        return false;
                    }
                    Object this$descriptionFull = getDescriptionFull();
                    Object other$descriptionFull = other.getDescriptionFull();
                    return this$descriptionFull == null ? other$descriptionFull == null : this$descriptionFull.equals(other$descriptionFull);
                }
                return false;
            }
            return false;
        }

        protected boolean canEqual(Object other) {
            return other instanceof Comment;
        }

        public int hashCode() {
            int result = super/*java.lang.Object*/.hashCode();
            int result2 = (result * 59) + getGape();
            Object $comment = getComment();
            int result3 = (result2 * 59) + ($comment == null ? 43 : $comment.hashCode());
            Object $centerComment = getCenterComment();
            int result4 = (result3 * 59) + ($centerComment == null ? 43 : $centerComment.hashCode());
            Object $descriptionFull = getDescriptionFull();
            return (result4 * 59) + ($descriptionFull == null ? 43 : $descriptionFull.hashCode());
        }

        public int getGape() {
            return this.gape;
        }

        public CommentDTO getComment() {
            return this.comment;
        }

        public JPanel getCenterComment() {
            return this.centerComment;
        }

        public HtmlTextPane getDescriptionFull() {
            return this.descriptionFull;
        }

        public Comment(final CommentDTO c, int gape, CommentDTO parent) {
            setBorder(BorderFactory.createEmptyBorder(0, 25 + (gape * DiscussionPanel.this.oneGapeCommentWidth), 5, 0));
            this.gape = gape;
            this.comment = c;
            setLayout(new BorderLayout());
            ButtonGroup bg = new ButtonGroup();
            JLabel authorImage = new JLabel();
            final JLabel response = new LocalizableLabel("modpack.response");
            JLabel author = new JLabel(c.getAuthor());
            JLabel commentDate = new JLabel(DiscussionPanel.this.format.format(new Date(c.getUpdated().longValue())));
            JPanel topCommentPanel = new ExtendedPanel((LayoutManager) new FlowLayout(0, 0, 0));
            this.centerComment = new ExtendedPanel((LayoutManager) new BorderLayout());
            JPanel buttons = new ExtendedPanel((LayoutManager) new FlowLayout(0, 0, 0));
            JPanel likes = new ExtendedPanel();
            BoxLayout bl = new BoxLayout(likes, 1);
            likes.setLayout(bl);
            UserPositionCommentCheckbox dislike = new UserPositionCommentCheckbox("modpack/icon-dislike-whole.png", "modpack/icon-dislike.png", bg, false);
            UserPositionCommentCheckbox like = new UserPositionCommentCheckbox("modpack/icon-like-whole.png", "modpack/icon-like.png", bg, true);
            CurrentUserPosition userPos = new CurrentUserPosition();
            if (Objects.nonNull(c.getAuthorPosition())) {
                if (c.getAuthorPosition().isPosition()) {
                    like.setSelected(true);
                    userPos.setGood((byte) 1);
                } else {
                    dislike.setSelected(true);
                    userPos.setBad((byte) 1);
                }
            }
            updateText();
            createUserPosition(c, dislike, userPos);
            createUserPosition(c, like, userPos);
            bg.add(dislike);
            bg.add(like);
            likes.add(Box.createVerticalStrut(10));
            likes.add(dislike);
            likes.add(like);
            likes.setPreferredSize(new Dimension(100, 50));
            JPanel userAvatar = new ExtendedPanel((LayoutManager) new BorderLayout());
            userAvatar.setPreferredSize(new Dimension(45, 45));
            authorImage.setPreferredSize(new Dimension(45, 30));
            userAvatar.add(authorImage, "North");
            buttons.add(response);
            if (c.isEdited()) {
                JLabel deleted = new LocalizableLabel("modpack.popup.delete");
                buttons.add(deleted);
                deleted.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                SwingUtil.changeFontFamily(deleted, FontTL.ROBOTO_REGULAR, 15, Color.RED);
                deleted.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.1
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            int res = Alert.showConfirmDialog(0, 2, CoreConstants.EMPTY_STRING, Localizable.get("modpack.comment.delete"), null, Localizable.get("ui.yes"), Localizable.get("ui.no"));
                            if (res != 0) {
                                return;
                            }
                            CommentDTO commentDTO = c;
                            CompletableFuture.runAsync(()
                            /*  JADX ERROR: Method code generation error
                                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x002e: INVOKE  
                                  (wrap: java.lang.Runnable : 0x0029: INVOKE_CUSTOM (r0v6 java.lang.Runnable A[REMOVE]) = 
                                  (r8v0 'this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel$Comment$1 A[D('this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel$Comment$1), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                                  (r1v2 'commentDTO' org.tlauncher.modpack.domain.client.CommentDTO A[DONT_INLINE])
                                
                                 handle type: INVOKE_DIRECT
                                 lambda: java.lang.Runnable.run():void
                                 call insn: ?: INVOKE  
                                  (r0 I:org.tlauncher.tlauncher.ui.modpack.DiscussionPanel$Comment$1)
                                  (r1 I:org.tlauncher.modpack.domain.client.CommentDTO)
                                 type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.1.lambda$mousePressed$1(org.tlauncher.modpack.domain.client.CommentDTO):void)
                                 type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.1.mousePressed(java.awt.event.MouseEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/DiscussionPanel$Comment$1.class
                                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
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
                                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                                	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
                                	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
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
                                	... 27 more
                                */
                            /*
                                this = this;
                                r0 = r9
                                boolean r0 = javax.swing.SwingUtilities.isLeftMouseButton(r0)
                                if (r0 == 0) goto L32
                                r0 = 0
                                r1 = 2
                                java.lang.String r2 = ""
                                java.lang.String r3 = "modpack.comment.delete"
                                java.lang.String r3 = org.tlauncher.tlauncher.ui.loc.Localizable.get(r3)
                                r4 = 0
                                java.lang.String r5 = "ui.yes"
                                java.lang.String r5 = org.tlauncher.tlauncher.ui.loc.Localizable.get(r5)
                                java.lang.String r6 = "ui.no"
                                java.lang.String r6 = org.tlauncher.tlauncher.ui.loc.Localizable.get(r6)
                                int r0 = org.tlauncher.tlauncher.ui.alert.Alert.showConfirmDialog(r0, r1, r2, r3, r4, r5, r6)
                                r10 = r0
                                r0 = r10
                                if (r0 == 0) goto L24
                                return
                            L24:
                                r0 = r8
                                r1 = r8
                                org.tlauncher.modpack.domain.client.CommentDTO r1 = r6
                                void r0 = () -> { // java.lang.Runnable.run():void
                                    r0.lambda$mousePressed$1(r1);
                                }
                                java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
                            L32:
                                return
                            */
                            throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.AnonymousClass1.mousePressed(java.awt.event.MouseEvent):void");
                        }
                    });
                }
                topCommentPanel.add(author);
                topCommentPanel.add(commentDate);
                if (c.isEdited()) {
                    JLabel changed = new LocalizableLabel("settings.change");
                    topCommentPanel.add(changed);
                    SwingUtil.changeFontFamily(changed, FontTL.ROBOTO_REGULAR, 15, Color.GRAY);
                    changed.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                    changed.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.2
                        public void mousePressed(MouseEvent e) {
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                SwingUtilities.invokeLater(() -> {
                                    DiscussionPanel.this.commentCreationForm.setChangedComment(Comment.this.comment);
                                    DiscussionPanel.this.commentCreationForm.preparedForNewComment();
                                    DiscussionPanel.this.commentCreationForm.setTopicPage(DiscussionPanel.this.dto.getId());
                                    DiscussionPanel.this.commentCreationForm.setTopicId(Comment.this.comment.getId());
                                    DiscussionPanel.this.commentCreationForm.setType(null);
                                    DiscussionPanel.this.commentCreationForm.setTopicType(null);
                                    DiscussionPanel.this.commentCreationForm.setVisible(true);
                                });
                            }
                        }
                    });
                }
                this.centerComment.add(topCommentPanel, "North");
                this.centerComment.add(this.descriptionFull, "Center");
                this.centerComment.add(buttons, "South");
                add((Component) userAvatar, "West");
                add((Component) this.centerComment, "Center");
                add((Component) likes, "East");
                authorImage.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
                like.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
                SwingUtil.changeFontFamily(author, FontTL.ROBOTO_REGULAR, 15, ColorUtil.COLOR_25);
                SwingUtil.changeFontFamily(commentDate, FontTL.ROBOTO_REGULAR, 15, ColorUtil.COLOR_25);
                SwingUtil.changeFontFamily(response, FontTL.ROBOTO_REGULAR, 15, Color.GRAY);
                SwingUtil.changeFontFamily(dislike, FontTL.ROBOTO_REGULAR, 15, Color.BLACK);
                SwingUtil.changeFontFamily(like, FontTL.ROBOTO_REGULAR, 15, Color.BLACK);
                response.addMouseListener(new MouseInputAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.3
                    public void mouseEntered(MouseEvent e) {
                        response.setForeground(ColorUtil.COLOR_25);
                    }

                    public void mouseExited(MouseEvent e) {
                        response.setForeground(Color.GRAY);
                    }
                });
                response.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.4
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e) && !c.isRemoved()) {
                            if (Comment.this.getGape() < TLauncher.getInnerSettings().getInteger("modpack.comment.max.branch")) {
                                DiscussionPanel.this.commentCreationForm.setChangedComment(null);
                                DiscussionPanel.this.commentCreationForm.preparedForNewComment();
                                DiscussionPanel.this.commentCreationForm.setTopicPage(DiscussionPanel.this.dto.getId());
                                DiscussionPanel.this.commentCreationForm.setTopicId(c.getId());
                                DiscussionPanel.this.commentCreationForm.setType(DiscussionPanel.this.type);
                                DiscussionPanel.this.commentCreationForm.setTopicType(TopicType.SUB_COMMENT);
                                DiscussionPanel.this.commentCreationForm.setParent(Comment.this);
                                DiscussionPanel.this.commentCreationForm.setVisible(true);
                                return;
                            }
                            Alert.showLocMessage("skin.notification.title", "modpack.comment.max.branch", null);
                        }
                    }
                });
                CompletableFuture.runAsync(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0316: INVOKE  
                      (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x030e: INVOKE  (r0v69 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                      (wrap: java.lang.Runnable : 0x0305: INVOKE_CUSTOM (r0v68 java.lang.Runnable A[REMOVE]) = 
                      (r8v0 'this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel$Comment A[D('this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel$Comment), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r10v0 'c' org.tlauncher.modpack.domain.client.CommentDTO A[D('c' org.tlauncher.modpack.domain.client.CommentDTO), DONT_INLINE])
                      (r0v7 'authorImage' javax.swing.JLabel A[D('authorImage' javax.swing.JLabel), DONT_INLINE])
                      (r0v35 'userAvatar' javax.swing.JPanel A[D('userAvatar' javax.swing.JPanel), DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  
                      (r0 I:org.tlauncher.tlauncher.ui.modpack.DiscussionPanel$Comment)
                      (r1 I:org.tlauncher.modpack.domain.client.CommentDTO)
                      (r2 I:javax.swing.JLabel)
                      (r3 I:javax.swing.JPanel)
                     type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.lambda$new$1(org.tlauncher.modpack.domain.client.CommentDTO, javax.swing.JLabel, javax.swing.JPanel):void)
                      (wrap: java.util.concurrent.Executor : 0x030b: IGET  (r1v57 java.util.concurrent.Executor A[REMOVE]) = 
                      (wrap: ?? : ?: IGET  
                      (r8v0 'this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel$Comment A[D('this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel$Comment), IMMUTABLE_TYPE, THIS])
                     org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.this$0 org.tlauncher.tlauncher.ui.modpack.DiscussionPanel)
                     org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.singleDownloadExecutor java.util.concurrent.Executor)
                     type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable, java.util.concurrent.Executor):java.util.concurrent.CompletableFuture)
                      (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x0311: INVOKE_CUSTOM (r1v58 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                     handle type: INVOKE_STATIC
                     lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                     call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.lambda$new$2(java.lang.Throwable):java.lang.Void)
                     type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.<init>(org.tlauncher.tlauncher.ui.modpack.DiscussionPanel, org.tlauncher.modpack.domain.client.CommentDTO, int, org.tlauncher.modpack.domain.client.CommentDTO):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/DiscussionPanel$Comment.class
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
                    Caused by: java.lang.IndexOutOfBoundsException: Index 3 out of bounds for length 3
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
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                    	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:805)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                    	... 15 more
                    */
                /*
                    Method dump skipped, instructions count: 814
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.Comment.<init>(org.tlauncher.tlauncher.ui.modpack.DiscussionPanel, org.tlauncher.modpack.domain.client.CommentDTO, int, org.tlauncher.modpack.domain.client.CommentDTO):void");
            }

            public void updateText() {
                String text;
                if (Objects.nonNull(this.descriptionFull)) {
                    this.centerComment.remove(this.descriptionFull);
                }
                if (this.comment.isRemoved()) {
                    text = Localizable.get("modpack.comment.deleted");
                } else {
                    text = this.comment.getDescription();
                }
                this.descriptionFull = HtmlTextPane.getWithWidth(text, DiscussionPanel.this.commentDescriptionWidth - (this.gape * DiscussionPanel.this.oneGapeCommentWidth));
                this.descriptionFull.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                this.centerComment.add(this.descriptionFull, "Center");
                this.centerComment.revalidate();
                this.centerComment.repaint();
            }

            private void createUserPosition(CommentDTO c, UserPositionCommentCheckbox dislike, CurrentUserPosition pos) {
                dislike.setObject(DiscussionPanel.ob);
                dislike.setController(DiscussionPanel.this.controller);
                dislike.setExecutor(DiscussionPanel.this.singleDownloadExecutor);
                dislike.setComment(c);
                dislike.setHorizontalTextPosition(4);
                dislike.setPos(pos);
                dislike.initCounterPosition();
            }
        }

        public void preparingContent(Long id, int page, int insertElement, TopicType type, int gape) {
            CompletableFuture.runAsync(()
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0015: INVOKE  
                  (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x000d: INVOKE  (r0v2 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                  (wrap: java.lang.Runnable : 0x0008: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
                  (r7v0 'this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel A[D('this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                  (r8v0 'id' java.lang.Long A[D('id' java.lang.Long), DONT_INLINE])
                  (r9v0 'page' int A[D('page' int), DONT_INLINE])
                  (r11v0 'type' org.tlauncher.modpack.domain.client.share.TopicType A[D('type' org.tlauncher.modpack.domain.client.share.TopicType), DONT_INLINE])
                  (r12v0 'gape' int A[D('gape' int), DONT_INLINE])
                  (r10v0 'insertElement' int A[D('insertElement' int), DONT_INLINE])
                
                 handle type: INVOKE_DIRECT
                 lambda: java.lang.Runnable.run():void
                 call insn: ?: INVOKE  
                  (r0 I:org.tlauncher.tlauncher.ui.modpack.DiscussionPanel)
                  (r1 I:java.lang.Long)
                  (r2 I:int)
                  (r3 I:org.tlauncher.modpack.domain.client.share.TopicType)
                  (r4 I:int)
                  (r5 I:int)
                 type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.lambda$preparingContent$4(java.lang.Long, int, org.tlauncher.modpack.domain.client.share.TopicType, int, int):void)
                 type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture)
                  (wrap: java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> : 0x0010: INVOKE_CUSTOM (r1v1 java.util.function.Function<java.lang.Throwable, ? extends java.lang.Void> A[REMOVE]) = 
                 handle type: INVOKE_STATIC
                 lambda: java.util.function.Function.apply(java.lang.Object):java.lang.Object
                 call insn: ?: INVOKE  (v0 java.lang.Throwable) type: STATIC call: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.lambda$preparingContent$5(java.lang.Throwable):java.lang.Void)
                 type: VIRTUAL call: java.util.concurrent.CompletableFuture.exceptionally(java.util.function.Function):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.preparingContent(java.lang.Long, int, int, org.tlauncher.modpack.domain.client.share.TopicType, int):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/DiscussionPanel.class
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
                Caused by: java.lang.IndexOutOfBoundsException: Index 5 out of bounds for length 5
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
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:805)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                	... 15 more
                */
            /*
                this = this;
                r0 = r7
                r1 = r8
                r2 = r9
                r3 = r11
                r4 = r12
                r5 = r10
                void r0 = () -> { // java.lang.Runnable.run():void
                    r0.lambda$preparingContent$4(r1, r2, r3, r4, r5);
                }
                java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
                void r1 = (v0) -> { // java.util.function.Function.apply(java.lang.Object):java.lang.Object
                    return lambda$preparingContent$5(v0);
                }
                java.util.concurrent.CompletableFuture r0 = r0.exceptionally(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.preparingContent(java.lang.Long, int, int, org.tlauncher.modpack.domain.client.share.TopicType, int):void");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void calculateGape() {
            remove(this.gapeSpace);
            int size = getComponents().length;
            if (size < 4 && size != 1) {
                this.gapeSpace.setPreferredSize(new Dimension(0, (4 - size) * 85));
                add(this.gapeSpace);
            } else if (findIndexElement(this.gapeSpace) != -1) {
                remove(this.gapeSpace);
            }
        }

        private void addedComment(List<? super Component> result, final CommonPage<CommentDTO> page, final int gape, final Long id, CommentDTO parent) {
            for (CommentDTO c : page.getContent()) {
                result.add(new Comment(c, gape, parent));
                if (Objects.nonNull(c.getSubComments())) {
                    addedComment(result, c.getSubComments(), gape + 1, c.getId(), c);
                }
            }
            if (page.isNext()) {
                JLabel l = new LocalizableLabel("modpack.comment.show.more");
                final JPanel p = new ExtendedPanel((LayoutManager) new BorderLayout(0, 0));
                l.setHorizontalAlignment(2);
                p.setBorder(BorderFactory.createEmptyBorder(0, 25 + (gape * this.oneGapeCommentWidth), 10, 0));
                SwingUtil.changeFontFamily(l, FontTL.ROBOTO_REGULAR, 16, ColorUtil.BLUE_COLOR);
                p.add(l, "West");
                result.add(p);
                l.addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.2
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            int j = DiscussionPanel.this.findIndexElement((Component) p);
                            JPanel jPanel = p;
                            SwingUtilities.invokeLater(()
                            /*  JADX ERROR: Method code generation error
                                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x001d: INVOKE  
                                  (wrap: java.lang.Runnable : 0x0018: INVOKE_CUSTOM (r0v6 java.lang.Runnable A[REMOVE]) = 
                                  (r7v0 'this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel$2 A[D('this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel$2), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                                  (r1v3 'jPanel' javax.swing.JPanel A[DONT_INLINE])
                                
                                 handle type: INVOKE_DIRECT
                                 lambda: java.lang.Runnable.run():void
                                 call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.modpack.DiscussionPanel$2), (r1 I:javax.swing.JPanel) type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.2.lambda$mousePressed$0(javax.swing.JPanel):void)
                                 type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.2.mousePressed(java.awt.event.MouseEvent):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/DiscussionPanel$2.class
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
                                	... 23 more
                                */
                            /*
                                this = this;
                                r0 = r8
                                boolean r0 = javax.swing.SwingUtilities.isLeftMouseButton(r0)
                                if (r0 == 0) goto L4c
                                r0 = r7
                                org.tlauncher.tlauncher.ui.modpack.DiscussionPanel r0 = org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.this
                                r1 = r7
                                javax.swing.JPanel r1 = r5
                                int r0 = org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.access$1200(r0, r1)
                                r9 = r0
                                r0 = r7
                                r1 = r7
                                javax.swing.JPanel r1 = r5
                                void r0 = () -> { // java.lang.Runnable.run():void
                                    r0.lambda$mousePressed$0(r1);
                                }
                                javax.swing.SwingUtilities.invokeLater(r0)
                                r0 = r7
                                org.tlauncher.tlauncher.ui.modpack.DiscussionPanel r0 = org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.this
                                r1 = r7
                                java.lang.Long r1 = r6
                                r2 = r7
                                org.tlauncher.modpack.domain.client.site.CommonPage r2 = r7
                                java.lang.Integer r2 = r2.getCurrent()
                                int r2 = r2.intValue()
                                r3 = 1
                                int r2 = r2 + r3
                                r3 = r9
                                r4 = r7
                                int r4 = r8
                                if (r4 != 0) goto L42
                                org.tlauncher.modpack.domain.client.share.TopicType r4 = org.tlauncher.modpack.domain.client.share.TopicType.GAME_ENTITY
                                goto L45
                            L42:
                                org.tlauncher.modpack.domain.client.share.TopicType r4 = org.tlauncher.modpack.domain.client.share.TopicType.SUB_COMMENT
                            L45:
                                r5 = r7
                                int r5 = r8
                                r0.preparingContent(r1, r2, r3, r4, r5)
                            L4c:
                                return
                            */
                            throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.AnonymousClass2.mousePressed(java.awt.event.MouseEvent):void");
                        }
                    });
                }
            }

            @Subscribe
            public void insertComment(InsertCommentDTO comment) {
                SwingUtilities.invokeLater(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
                      (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
                      (r3v0 'this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel A[D('this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r4v0 'comment' org.tlauncher.tlauncher.ui.model.InsertCommentDTO A[D('comment' org.tlauncher.tlauncher.ui.model.InsertCommentDTO), DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.modpack.DiscussionPanel), (r1 I:org.tlauncher.tlauncher.ui.model.InsertCommentDTO) type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.lambda$insertComment$6(org.tlauncher.tlauncher.ui.model.InsertCommentDTO):void)
                     type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.insertComment(org.tlauncher.tlauncher.ui.model.InsertCommentDTO):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/DiscussionPanel.class
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
                        r0.lambda$insertComment$6(r1);
                    }
                    javax.swing.SwingUtilities.invokeLater(r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.insertComment(org.tlauncher.tlauncher.ui.model.InsertCommentDTO):void");
            }

            @Subscribe
            public void insertComment(CommentDTO comment) {
                SwingUtilities.invokeLater(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0007: INVOKE  
                      (wrap: java.lang.Runnable : 0x0002: INVOKE_CUSTOM (r0v1 java.lang.Runnable A[REMOVE]) = 
                      (r3v0 'this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel A[D('this' org.tlauncher.tlauncher.ui.modpack.DiscussionPanel), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r4v0 'comment' org.tlauncher.modpack.domain.client.CommentDTO A[D('comment' org.tlauncher.modpack.domain.client.CommentDTO), DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.ui.modpack.DiscussionPanel), (r1 I:org.tlauncher.modpack.domain.client.CommentDTO) type: DIRECT call: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.lambda$insertComment$7(org.tlauncher.modpack.domain.client.CommentDTO):void)
                     type: STATIC call: javax.swing.SwingUtilities.invokeLater(java.lang.Runnable):void in method: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.insertComment(org.tlauncher.modpack.domain.client.CommentDTO):void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/DiscussionPanel.class
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
                        r0.lambda$insertComment$7(r1);
                    }
                    javax.swing.SwingUtilities.invokeLater(r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.ui.modpack.DiscussionPanel.insertComment(org.tlauncher.modpack.domain.client.CommentDTO):void");
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int findIndexElement(Component p) {
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= getComponents().length) {
                        break;
                    } else if (!getComponents()[i].equals(p)) {
                        i++;
                    } else {
                        j = i;
                        break;
                    }
                }
                return j;
            }

            private Comment findIndexElement(CommentDTO c) {
                for (int i = 0; i < getComponents().length; i++) {
                    Comment comment = getComponents()[i];
                    if ((comment instanceof Comment) && comment.getComment().getId().equals(c.getId())) {
                        return comment;
                    }
                }
                return null;
            }
        }
