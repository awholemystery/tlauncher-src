package org.tlauncher.tlauncher.managers;

import by.gdev.utils.service.FileMapperService;
import ch.qos.logback.core.CoreConstants;
import com.google.inject.Key;
import com.google.inject.name.Names;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.tlauncher.component.RefreshableComponent;
import org.tlauncher.tlauncher.entity.profile.ClientProfile;
import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
import org.tlauncher.tlauncher.minecraft.auth.Account;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.browser.BrowserHolder;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.advertising.AdvertisingStatusObservable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/ProfileManager.class */
public class ProfileManager extends RefreshableComponent {
    private static final String DEFAULT_PROFILE_FILENAME = "TlauncherProfiles.json";
    private static final String LAUNCHER_PROFILE_FILENAME = "launcher_profiles.json";
    private final List<ProfileManagerListener> listeners;
    private final FileMapperService fileMapperService;
    private volatile ClientProfile clientProfile;

    public ProfileManager(ComponentManager manager) throws Exception {
        super(manager);
        this.listeners = Collections.synchronizedList(new ArrayList());
        this.fileMapperService = (FileMapperService) TLauncher.getInjector().getInstance(Key.get(FileMapperService.class, Names.named("profileFileMapperService")));
        addListener(new ProfileManagerAdapter() { // from class: org.tlauncher.tlauncher.managers.ProfileManager.1
            private boolean init;

            @Override // org.tlauncher.tlauncher.managers.ProfileManagerAdapter, org.tlauncher.tlauncher.managers.ProfileManagerListener
            public void fireRefreshed(ClientProfile clientProfile) {
                if (this.init) {
                    return;
                }
                this.init = true;
                TLauncher.getInstance().getTLauncherManager().asyncRefresh();
                AdvertisingStatusObservable adStatus = new AdvertisingStatusObservable(clientProfile, ProfileManager.this);
                if (BrowserHolder.getInstance().getBrowser() != null) {
                    adStatus.addListeners(BrowserHolder.getInstance().getBrowser());
                }
                adStatus.run();
            }
        });
    }

    @Override // org.tlauncher.tlauncher.component.RefreshableComponent
    public boolean refresh() {
        loadProfiles();
        fireProfileRefreshed();
        try {
            saveProfiles();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void fireProfileRefreshed() {
        for (ProfileManagerListener listener : this.listeners) {
            listener.fireRefreshed(this.clientProfile);
        }
    }

    public UUID getClientToken() {
        return this.clientProfile.getClientToken();
    }

    public void addListener(ProfileManagerListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public boolean isNotPremium() {
        return this.clientProfile.getAccounts().values().stream().noneMatch((v0) -> {
            return v0.isPremiumAccount();
        });
    }

    public boolean remove(Account account) throws IOException {
        Account ac = this.clientProfile.getAccounts().get(account.getShortUUID());
        if (Objects.nonNull(ac)) {
            this.clientProfile.getAccounts().remove(account.getShortUUID());
            saveProfiles();
            fireProfileChanged();
            return true;
        }
        fireProfileChanged();
        return false;
    }

    public void save(Account account) throws IOException {
        U.log("saved account " + account.toString());
        this.clientProfile.getAccounts().put(account.getShortUUID(), account);
        this.clientProfile.setSelectedAccountUUID(account.getShortUUID());
        saveProfiles();
        fireProfileChanged();
    }

    public Account getSelectedAccount() {
        if (Objects.isNull(this.clientProfile.getSelectedAccountUUID())) {
            return null;
        }
        return this.clientProfile.getAccounts().get(this.clientProfile.getSelectedAccountUUID());
    }

    public boolean hasSelectedAccount() {
        return Objects.nonNull(getSelectedAccount());
    }

    public void selectAccount(Account account) {
        if (!account.getShortUUID().equals(this.clientProfile.getSelectedAccountUUID())) {
            this.clientProfile.setSelectedAccountUUID(account.getShortUUID());
            saveProfiles();
        }
    }

    public void updateFreeAccountField(String username) {
        Account ac;
        Optional<Account> op = this.clientProfile.getAccounts().values().stream().filter(e -> {
            return Objects.equals(e.getShortUUID(), this.clientProfile.getFreeAccountUUID());
        }).findFirst();
        if (op.isPresent() && !Objects.equals(op.get().getUsername(), this.clientProfile.getFreeAccountUUID())) {
            ac = op.get();
            ac.setUsername(username);
            ac.setUserID(username);
            ac.setDisplayName(username);
        } else {
            ac = Account.createFreeAccountByUsername(username);
            this.clientProfile.setFreeAccountUUID(ac.getShortUUID());
        }
        if (StringUtils.isBlank(username)) {
            remove(ac);
        } else {
            save(ac);
        }
        TLauncher.getInstance().getConfiguration().set("login.account", (Object) null);
    }

    private File getProfileFile() {
        return new File(MinecraftUtil.getWorkingDirectory(), DEFAULT_PROFILE_FILENAME);
    }

    private void fireProfileChanged() {
        for (ProfileManagerListener listener : this.listeners) {
            listener.fireClientProfileChanged(this.clientProfile);
        }
    }

    private void loadProfiles() {
        log("Refreshing profiles from:", getProfileFile());
        try {
            this.clientProfile = (ClientProfile) this.fileMapperService.read(DEFAULT_PROFILE_FILENAME, ClientProfile.class);
            if (Objects.isNull(this.clientProfile)) {
                this.clientProfile = new ClientProfile();
            }
            setOldField();
            removedNotValidAccounts();
        } catch (Exception e) {
            log("Cannot read from", DEFAULT_PROFILE_FILENAME, e);
            this.clientProfile = new ClientProfile();
        }
    }

    private void removedNotValidAccounts() {
        ((List) this.clientProfile.getAccounts().entrySet().stream().filter(e -> {
            return Objects.isNull(((Account) e.getValue()).getShortUUID()) || StringUtils.isBlank(((Account) e.getValue()).getUsername()) || ((String) e.getKey()).contains("-") || Objects.isNull(e.getKey()) || !((String) e.getKey()).equals(((Account) e.getValue()).getShortUUID());
        }).collect(Collectors.toList())).forEach(e2 -> {
            U.log("removed account with null uuid or username " + ((Account) e2.getValue()).toString());
            this.clientProfile.getAccounts().remove(e2.getKey());
        });
        if (StringUtils.contains(this.clientProfile.getFreeAccountUUID(), "-")) {
            this.clientProfile.setFreeAccountUUID(null);
        }
        if (StringUtils.contains(this.clientProfile.getSelectedAccountUUID(), "-")) {
            this.clientProfile.setSelectedAccountUUID(null);
        }
        createStandardOfficialProfileFile();
    }

    private void setOldField() {
        if (Objects.nonNull(this.clientProfile.getAuthenticationDatabase())) {
            this.clientProfile.setAccounts(new HashMap());
            for (Account c : this.clientProfile.getAuthenticationDatabase().getAccounts()) {
                this.clientProfile.getAccounts().put(c.getShortUUID(), c);
            }
            this.clientProfile.setAuthenticationDatabase(null);
        }
    }

    private void createStandardOfficialProfileFile() {
        File launcherProfile = new File(MinecraftUtil.getWorkingDirectory(), LAUNCHER_PROFILE_FILENAME);
        if (Files.notExists(launcherProfile.toPath(), new LinkOption[0])) {
            try {
                FileUtil.writeFile(launcherProfile, "{\n\"clientToken\": \"" + this.clientProfile.getClientToken() + "\"\n,\"profiles\": {}}");
            } catch (IOException e) {
                log(e);
            }
        }
    }

    private void saveProfiles() throws IOException {
        U.log("save profiles");
        this.fileMapperService.write(this.clientProfile, DEFAULT_PROFILE_FILENAME);
    }

    public void print() {
        U.log("accounts");
        this.clientProfile.getAccounts().entrySet().forEach(e -> {
            U.log(CoreConstants.EMPTY_STRING + ((String) e.getKey()) + " " + ((Account) e.getValue()).getUsername() + " " + ((Account) e.getValue()).getShortUUID() + " " + ((Account) e.getValue()).getType());
        });
        U.log("selected " + this.clientProfile.getSelectedAccountUUID());
        U.log("selected free" + this.clientProfile.getFreeAccountUUID());
    }

    public void print1() {
        File f = getProfileFile();
        FileOwnerAttributeView ownerInfo = (FileOwnerAttributeView) Files.getFileAttributeView(f.toPath(), FileOwnerAttributeView.class, new LinkOption[0]);
        try {
            UserPrincipal fileOwner = ownerInfo.getOwner();
            U.log("File Owned by: " + fileOwner.getName());
            U.log(String.format("read %s, write %s, execute %s", Boolean.valueOf(f.canRead()), Boolean.valueOf(f.canWrite()), Boolean.valueOf(f.canExecute())));
        } catch (IOException e) {
            U.log(e);
        }
    }

    public Account findUniqueTlauncherAccount() throws SelectedAnyOneTLAccountException, RequiredTLAccountException {
        Account ac = getSelectedAccount();
        if (Objects.nonNull(ac) && Account.AccountType.TLAUNCHER.equals(ac.getType())) {
            return ac;
        }
        List<Account> list = (List) this.clientProfile.getAccounts().values().stream().filter(a -> {
            return Account.AccountType.TLAUNCHER.equals(a.getType());
        }).collect(Collectors.toList());
        if (list.size() > 1) {
            throw new SelectedAnyOneTLAccountException();
        }
        if (list.isEmpty()) {
            throw new RequiredTLAccountException();
        }
        return list.get(0);
    }

    public List<Account> findAllTLAccount() {
        return (List) this.clientProfile.getAccounts().values().stream().filter(a -> {
            return Account.AccountType.TLAUNCHER.equals(a.getType());
        }).collect(Collectors.toList());
    }
}
