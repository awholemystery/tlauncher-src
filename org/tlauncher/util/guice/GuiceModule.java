package org.tlauncher.util.guice;

import by.gdev.http.download.config.HttpClientConfig;
import by.gdev.http.download.impl.FileCacheServiceImpl;
import by.gdev.http.download.impl.GsonServiceImpl;
import by.gdev.http.download.impl.HttpServiceImpl;
import by.gdev.http.download.service.FileCacheService;
import by.gdev.http.download.service.GsonService;
import by.gdev.http.download.service.HttpService;
import by.gdev.util.DesktopUtil;
import by.gdev.utils.service.FileMapperService;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.json.DateTypeAdapter;
import net.minecraft.launcher.versions.json.FileTypeAdapter;
import net.minecraft.launcher.versions.json.LowerCaseEnumTypeAdapterFactory;
import net.minecraft.launcher.versions.json.PatternTypeAdapter;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.MapDTO;
import org.tlauncher.modpack.domain.client.ModDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.ResourcePackDTO;
import org.tlauncher.modpack.domain.client.share.NameIdDTO;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.InnerConfiguration;
import org.tlauncher.tlauncher.minecraft.auth.AuthenticatorDatabase;
import org.tlauncher.tlauncher.minecraft.auth.UUIDTypeAdapter;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.minecraft.launcher.assitent.AdditionalFileAssistance;
import org.tlauncher.tlauncher.minecraft.launcher.assitent.AdditionalFileAssistanceFactory;
import org.tlauncher.tlauncher.minecraft.launcher.assitent.LanguageAssistance;
import org.tlauncher.tlauncher.minecraft.launcher.assitent.SoundAssist;
import org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServer;
import org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServersImpl;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.console.Console;
import org.tlauncher.tlauncher.ui.explorer.FileChooser;
import org.tlauncher.tlauncher.ui.explorer.FileExplorer;
import org.tlauncher.tlauncher.ui.explorer.FileWrapper;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.progress.ProgressFrame;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.TlauncherUtil;
import org.tlauncher.util.U;
import org.tlauncher.util.gson.serializer.MapDTOTypeAdapter;
import org.tlauncher.util.gson.serializer.ModDTOTypeAdapter;
import org.tlauncher.util.gson.serializer.ModpackDTOTypeAdapter;
import org.tlauncher.util.gson.serializer.ResourcePackDTOTypeAdapter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/guice/GuiceModule.class */
public class GuiceModule extends AbstractModule {
    private final Configuration configuration;
    private final InnerConfiguration inner;
    private Injector injector;
    private boolean usedDefaultChooser;
    private EventBus eventBus = new EventBus();

    public Injector getInjector() {
        return this.injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    public GuiceModule(Configuration configuration, InnerConfiguration inner) {
        this.usedDefaultChooser = true;
        this.configuration = configuration;
        this.inner = inner;
        try {
            new FileExplorer();
        } catch (Throwable th) {
            U.log("problem with standard FileExplorer");
            this.usedDefaultChooser = false;
        }
    }

    protected void configure() {
        bind(Gson.class).annotatedWith(Names.named("GsonCompleteVersion")).toInstance(createGsonCompleteVersion());
        bind(Gson.class).annotatedWith(Names.named("GsonAdditionalFile")).toInstance(createAddittionalFileGson());
        bind(InnerMinecraftServer.class).to(InnerMinecraftServersImpl.class);
        bind(Console.class).annotatedWith(Names.named("console")).toInstance(new Console());
        bind(ExecutorService.class).annotatedWith(Names.named("modpackExecutorService")).toInstance(Executors.newSingleThreadExecutor());
        Class<? extends FileChooser> c = FileExplorer.class;
        if (!this.usedDefaultChooser) {
            c = FileWrapper.class;
        }
        bind(FileChooser.class).to(c);
        install(new FactoryModuleBuilder().implement(MinecraftLauncher.class, MinecraftLauncher.class).build(MinecraftLauncherFactory.class));
        install(new FactoryModuleBuilder().implement(TLauncher.class, TLauncher.class).build(TlauncherFactory.class));
        install(new FactoryModuleBuilder().implement(ProgressFrame.class, ProgressFrame.class).build(CustomBarFactory.class));
        install(new FactoryModuleBuilder().implement(SoundAssist.class, SoundAssist.class).build(SoundAssistFactory.class));
        install(new FactoryModuleBuilder().implement(AdditionalFileAssistance.class, AdditionalFileAssistance.class).build(AdditionalFileAssistanceFactory.class));
        install(new FactoryModuleBuilder().implement(LanguageAssistance.class, LanguageAssistance.class).build(LanguageAssistFactory.class));
        bind(EventBus.class).toInstance(this.eventBus);
        bindListener(Matchers.any(), new TypeListener() { // from class: org.tlauncher.util.guice.GuiceModule.1
            public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
                typeEncounter.register(new InjectionListener<I>() { // from class: org.tlauncher.util.guice.GuiceModule.1.1
                    public void afterInjection(I i) {
                        GuiceModule.this.eventBus.register(i);
                    }
                });
            }
        });
    }

    @Singleton
    @Provides
    public TLauncher getTlauncher() {
        try {
            return ((TlauncherFactory) TLauncher.getInjector().getInstance(TlauncherFactory.class)).create(this.configuration);
        } catch (Throwable e) {
            if (TlauncherUtil.getStringError(e).contains("Problem reading font data.")) {
                String text = Localizable.get("alert.error.font.problem").replaceAll("pageLang", TlauncherUtil.getPageLanguage());
                Alert.showErrorHtml(text, (int) HttpStatus.SC_BAD_REQUEST);
                TLauncher.kill();
            } else {
                U.log("can't create TLauncher instance", e);
            }
            throw new NullPointerException("can't create TLauncher instance");
        }
    }

    @Singleton
    @Provides
    public FileCacheService getFileCacheService() {
        return new FileCacheServiceImpl(getHttpService(), getGson(), StandardCharsets.UTF_8, MinecraftUtil.getTLauncherFile("cache").toPath(), this.inner.getInteger("file.cache.service.time.to.life"));
    }

    @Singleton
    @Provides
    public Gson getGson() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        return builder.create();
    }

    @Singleton
    @Provides
    public FileMapperService createFileMapperService() {
        return new FileMapperService(getGson(), StandardCharsets.UTF_8, MinecraftUtil.getWorkingDirectory().getAbsolutePath());
    }

    private Gson createGsonCompleteVersion() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new LowerCaseEnumTypeAdapterFactory());
        builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        builder.registerTypeAdapter(CompleteVersion.class, new CompleteVersion.CompleteVersionSerializer());
        builder.registerTypeAdapter(ModpackDTO.class, new ModpackDTOTypeAdapter());
        builder.registerTypeAdapter(ModDTO.class, new ModDTOTypeAdapter());
        builder.registerTypeAdapter(MapDTO.class, new MapDTOTypeAdapter());
        builder.registerTypeAdapter(ResourcePackDTO.class, new ResourcePackDTOTypeAdapter());
        builder.enableComplexMapKeySerialization();
        builder.setPrettyPrinting();
        builder.disableHtmlEscaping();
        return builder.create();
    }

    private Gson createAddittionalFileGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Pattern.class, new PatternTypeAdapter());
        builder.setPrettyPrinting();
        return builder.create();
    }

    private Gson createProfileGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new LowerCaseEnumTypeAdapterFactory());
        builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        builder.registerTypeAdapter(File.class, new FileTypeAdapter());
        builder.registerTypeAdapter(AuthenticatorDatabase.class, new AuthenticatorDatabase.Serializer());
        builder.registerTypeAdapter(UUIDTypeAdapter.class, new UUIDTypeAdapter());
        builder.setPrettyPrinting();
        return builder.create();
    }

    private int getMaxAttemts() {
        return DesktopUtil.numberOfAttempts(Lists.newArrayList(this.inner.getArray("net.check.urls")), this.inner.getInteger("max.attempts.per.request"), getRequestConfig(), getHttpClient());
    }

    @Singleton
    @Provides
    private RequestConfig getRequestConfig() {
        return RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(60000).build();
    }

    @Singleton
    @Provides
    CloseableHttpClient getHttpClient() {
        return HttpClientConfig.getInstanceHttpClient();
    }

    @Singleton
    @Provides
    public HttpService getHttpService() {
        return new HttpServiceImpl(null, getHttpClient(), getRequestConfig(), getMaxAttemts());
    }

    @Singleton
    @Provides
    public GsonService getGsonService() {
        return new GsonServiceImpl(createGsonCompleteVersion(), getFileCacheService(), getHttpService());
    }

    @Singleton
    @Provides
    public ExecutorService createExecutorService() {
        return Executors.newCachedThreadPool();
    }

    @Named("profileFileMapperService")
    @Singleton
    @Provides
    public FileMapperService createFileMapperService1() {
        return new FileMapperService(createProfileGson(), StandardCharsets.UTF_8, MinecraftUtil.getWorkingDirectory().getAbsolutePath());
    }

    @Named("anyVersionType")
    @Singleton
    @Provides
    public NameIdDTO createAnyVersionType() {
        return new NameIdDTO(0L, Languages.ANY);
    }

    @Named("anyGameVersion")
    @Singleton
    @Provides
    public GameVersionDTO createAnyGameVersion() {
        GameVersionDTO g = new GameVersionDTO();
        g.setId(0L);
        g.setName("modpack.version.any");
        return g;
    }

    @Named("singleDownloadExecutor")
    @Singleton
    @Provides
    public Executor createImagesThread() {
        return Executors.newSingleThreadExecutor();
    }
}
