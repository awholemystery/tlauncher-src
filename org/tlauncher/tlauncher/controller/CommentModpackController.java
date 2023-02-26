package org.tlauncher.tlauncher.controller;

import by.gdev.http.download.service.GsonService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.swing.ImageIcon;
import net.minecraft.launcher.Http;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.tlauncher.modpack.domain.client.CommentDTO;
import org.tlauncher.modpack.domain.client.share.GameEntitySort;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.share.TopicType;
import org.tlauncher.modpack.domain.client.site.CommonPage;
import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.images.ImageCache;
import org.tlauncher.tlauncher.ui.model.GameEntityComment;

@Singleton
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/controller/CommentModpackController.class */
public class CommentModpackController {
    @Inject
    private GsonService gsonService;
    private String modpackApiURL = TLauncher.getInnerSettings().get("modpack.operation.url");
    @Inject
    private Gson gson;
    @Inject
    private ModpackManager modpackManager;

    public CommonPage<CommentDTO> getComments(Long id, Integer page, GameEntitySort sort, TopicType topicType) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("sort", sort.name().toString());
        map.put("type", topicType.name().toString());
        TLauncher tl = TLauncher.getInstance();
        Map<String, String> map1 = new HashMap<>();
        try {
            Account ac = TLauncher.getInstance().getProfileManager().findUniqueTlauncherAccount();
            map1.put("uuid", tl.getProfileManager().getClientToken().toString());
            map1.put("accessToken", ac.getAccessToken());
        } catch (RequiredTLAccountException | SelectedAnyOneTLAccountException e) {
        }
        return (CommonPage) this.gsonService.getObjectWithoutSaving(Http.get(this.modpackApiURL + "comments/page/" + id, map), new TypeToken<CommonPage<CommentDTO>>() { // from class: org.tlauncher.tlauncher.controller.CommentModpackController.1
        }.getType(), map1);
    }

    public ImageIcon loadIcon(String author) {
        BufferedImage b = ImageCache.loadImage(Http.constantURL(this.modpackApiURL + "user/picture?author=" + author), false);
        if (Objects.isNull(b)) {
            return null;
        }
        return new ImageIcon(b.getScaledInstance(35, 35, 1));
    }

    public void deletePosition(Long id) throws ClientProtocolException, IOException, SelectedAnyOneTLAccountException, RequiredTLAccountException {
        this.modpackManager.sendRequest(new HttpDelete(), null, String.format("%scomments/%s/position", this.modpackApiURL, id), null);
    }

    public void setPosition(boolean position, Long commentId) throws SelectedAnyOneTLAccountException, RequiredTLAccountException, ClientProtocolException, IOException {
        this.modpackManager.sendRequest(new HttpPut(), null, String.format("%scomments/%s/position?position=%s", this.modpackApiURL, commentId, Boolean.valueOf(position)), null);
    }

    public String saveComment(String message, TopicType topicType, String lang, GameType gameType, Long topicPage, Long topicId) throws RequiredTLAccountException, SelectedAnyOneTLAccountException, ClientProtocolException, IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("topic_type", topicType.toString().toUpperCase(Locale.ROOT));
        map.put("lang", lang);
        map.put("game_type", gameType);
        map.put("topic_page", topicPage);
        GameEntityComment g = new GameEntityComment();
        g.setDescription(message);
        g.setTopicId(topicId);
        return this.modpackManager.sendRequest(new HttpPost(), g, String.format("%scomments", this.modpackApiURL), map);
    }

    public void delete(Long id, Long topicPage) throws ClientProtocolException, IOException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
        Map<String, Object> map = new HashMap<>();
        map.put("topic_page", topicPage);
        this.modpackManager.sendRequest(new HttpDelete(), null, String.format("%scomments/%s", this.modpackApiURL, id), map);
    }

    public CommentDTO update(Long id, GameEntityComment g) throws ClientProtocolException, IOException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
        return (CommentDTO) this.gson.fromJson(this.modpackManager.sendRequest(new HttpPatch(), g, String.format("%scomments/%s", this.modpackApiURL, id), null), (Class<Object>) CommentDTO.class);
    }
}
