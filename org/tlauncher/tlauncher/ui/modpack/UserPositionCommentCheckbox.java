package org.tlauncher.tlauncher.ui.modpack;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.SwingUtilities;
import org.tlauncher.modpack.domain.client.CommentDTO;
import org.tlauncher.tlauncher.controller.CommentModpackController;
import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.label.CheckBoxBlockAction;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.model.CurrentUserPosition;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/modpack/UserPositionCommentCheckbox.class */
public class UserPositionCommentCheckbox extends CheckBoxBlockAction {
    private ButtonGroup buttonGroup;
    private boolean position;
    private CurrentUserPosition pos;
    private static final long serialVersionUID = 1;
    private CommentModpackController controller;
    private CommentDTO comment;

    public boolean isPosition() {
        return this.position;
    }

    public void setPos(CurrentUserPosition pos) {
        this.pos = pos;
    }

    public UserPositionCommentCheckbox(String selectedIcon, String diselectedIcon, ButtonGroup buttonGroup, boolean position) {
        super(selectedIcon, diselectedIcon);
        this.buttonGroup = buttonGroup;
        this.position = position;
    }

    public void setController(CommentModpackController controller) {
        this.controller = controller;
    }

    public void setComment(CommentDTO comment) {
        this.comment = comment;
    }

    @Override // org.tlauncher.tlauncher.ui.label.CheckBoxBlockAction
    public void executeRequest() {
        try {
            if (isSelected()) {
                this.controller.deletePosition(this.comment.getId());
                this.buttonGroup.clearSelection();
                this.pos.update(this.position, false);
            } else {
                this.controller.setPosition(this.position, this.comment.getId());
                this.pos.update(this.position, true);
            }
            SwingUtilities.invokeLater(() -> {
                Enumeration<AbstractButton> en = this.buttonGroup.getElements();
                while (en.hasMoreElements()) {
                    UserPositionCommentCheckbox u = (UserPositionCommentCheckbox) en.nextElement();
                    u.initCounterPosition();
                }
            });
        } catch (IOException e) {
            Alert.showLocMessage("modpack.remote.not.found", "modpack.try.later", null);
        } catch (RequiredTLAccountException e2) {
            Alert.showLocError("modpack.right.panel.required.tl.account.title", Localizable.get("modpack.right.panel.required.tl.account", Localizable.get("loginform.button.settings.account")), null);
        } catch (SelectedAnyOneTLAccountException e3) {
            Alert.showLocError("modpack.right.panel.required.tl.account.title", "modpack.right.panel.select.account.tl", null);
        }
    }

    public void initCounterPosition() {
        Long value = this.position ? this.comment.getGoodPosition() : this.comment.getBadPosition();
        if (Objects.isNull(value)) {
            value = 0L;
        }
        if (Objects.nonNull(this.comment.getAuthorPosition())) {
            if (this.comment.getAuthorPosition().isPosition() && this.position) {
                value = Long.valueOf(value.longValue() - serialVersionUID);
            } else if (!this.comment.getAuthorPosition().isPosition() && !this.position) {
                value = Long.valueOf(value.longValue() - serialVersionUID);
            }
        }
        Long value2 = Long.valueOf(value.longValue() + this.pos.getByPosition(this.position));
        if (value2.longValue() >= 0) {
            setText(CoreConstants.EMPTY_STRING + value2);
        } else {
            setText("0");
        }
    }
}
