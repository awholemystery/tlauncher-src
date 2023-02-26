package org.tlauncher.tlauncher.ui.button;

import ch.qos.logback.core.CoreConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.SwingUtilities;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.SubModpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.share.StateGameElement;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/button/StateModpackElementButton.class */
public class StateModpackElementButton extends ImageUdaterButton {
    private StateGameElement state;
    private ModpackManager manager;

    public StateModpackElementButton(final SubModpackDTO entity, final GameType type) {
        super(buildImage(entity.getStateGameElement()));
        this.manager = (ModpackManager) TLauncher.getInjector().getInstance(ModpackManager.class);
        if (this.state != StateGameElement.BLOCK) {
            addMouseListener(new MouseAdapter() { // from class: org.tlauncher.tlauncher.ui.button.StateModpackElementButton.1
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        List<GameEntityDTO> list = StateModpackElementButton.this.manager.findDependenciesFromGameEntityDTO(entity);
                        StringBuilder b = ModpackUtil.buildMessage(list);
                        if (list.isEmpty()) {
                            StateModpackElementButton.this.manager.changeModpackElementState(entity, type);
                        } else if (Alert.showQuestion(CoreConstants.EMPTY_STRING, Localizable.get("modpack.left.element.remove.question", entity.getName(), b.toString()))) {
                            StateModpackElementButton.this.manager.changeModpackElementState(entity, type);
                        }
                    }
                }
            });
        }
    }

    public void setState(StateGameElement state) {
        setImage(ImageCache.getImage(buildImage(state)));
        this.state = state;
    }

    private static String buildImage(StateGameElement state) {
        return state == null ? StateGameElement.ACTIVE + "-element-left.png" : state + "-element-left.png";
    }
}
