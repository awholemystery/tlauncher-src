package org.tlauncher.tlauncher.site.play;

import ch.qos.logback.core.net.ssl.SSL;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.security.KeyStore;
import java.util.concurrent.CompletableFuture;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import net.minecraft.launcher.Http;
import org.apache.http.client.methods.HttpOptions;
import org.tlauncher.tlauncher.entity.ServerCommandEntity;
import org.tlauncher.tlauncher.entity.server.SiteServer;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.managers.VersionManagerListener;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.U;

@Singleton
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/site/play/SitePlay.class */
public class SitePlay implements VersionManagerListener {
    private boolean status = false;
    @Inject
    private TLauncher tLauncher;
    @Inject
    private ModpackManager manager;

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshing(VersionManager manager) {
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshingFailed(VersionManager manager) {
        init();
    }

    @Override // org.tlauncher.tlauncher.managers.VersionManagerListener
    public void onVersionsRefreshed(VersionManager manager) {
        init();
    }

    private synchronized void init() {
        if (this.status) {
            return;
        }
        this.status = true;
        Thread th = new Thread(new Runnable() { // from class: org.tlauncher.tlauncher.site.play.SitePlay.1
            @Override // java.lang.Runnable
            public void run() {
                String[] ports = TLauncher.getInnerSettings().getArray("local.ports.client.play");
                for (String port : ports) {
                    try {
                        SSLContext sc = SitePlay.this.createSSLContext();
                        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
                        SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(Integer.valueOf(port).intValue());
                        s.setEnabledCipherSuites(sc.getServerSocketFactory().getSupportedCipherSuites());
                        U.log("run server on ", port);
                        while (true) {
                            try {
                                SSLSocket socket = (SSLSocket) s.accept();
                                ServerCommandEntity res = Http.readRequestInfo(socket);
                                if (!res.getRequestType().equals(HttpOptions.METHOD_NAME)) {
                                    if (res.getUrn().equals("/")) {
                                        SitePlay.this.runGameWithServer(res);
                                    } else if (res.getUrn().startsWith("/open/modpack/element")) {
                                        CompletableFuture.runAsync(()
                                        /*  JADX ERROR: Method code generation error
                                            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x00a9: INVOKE  
                                              (wrap: java.lang.Runnable : 0x00a4: INVOKE_CUSTOM (r0v39 java.lang.Runnable A[REMOVE]) = 
                                              (r5v0 'this' org.tlauncher.tlauncher.site.play.SitePlay$1 A[D('this' org.tlauncher.tlauncher.site.play.SitePlay$1), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                                              (r0v28 'res' org.tlauncher.tlauncher.entity.ServerCommandEntity A[D('res' org.tlauncher.tlauncher.entity.ServerCommandEntity), DONT_INLINE])
                                            
                                             handle type: INVOKE_DIRECT
                                             lambda: java.lang.Runnable.run():void
                                             call insn: ?: INVOKE  (r0 I:org.tlauncher.tlauncher.site.play.SitePlay$1), (r1 I:org.tlauncher.tlauncher.entity.ServerCommandEntity) type: DIRECT call: org.tlauncher.tlauncher.site.play.SitePlay.1.lambda$run$0(org.tlauncher.tlauncher.entity.ServerCommandEntity):void)
                                             type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture in method: org.tlauncher.tlauncher.site.play.SitePlay.1.run():void, file: TLauncher-2.876.jar:org/tlauncher/tlauncher/site/play/SitePlay$1.class
                                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                                            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                                            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                            	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                                            	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
                                            	at jadx.core.codegen.RegionGen.connectElseIf(RegionGen.java:156)
                                            	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:133)
                                            	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
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
                                            	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                                            	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:175)
                                            	at jadx.core.dex.regions.loops.LoopRegion.generate(LoopRegion.java:171)
                                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                            	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                                            	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:302)
                                            	at jadx.core.dex.regions.TryCatchRegion.generate(TryCatchRegion.java:85)
                                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                                            	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                                            	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:207)
                                            	at jadx.core.dex.regions.loops.LoopRegion.generate(LoopRegion.java:171)
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
                                            	... 53 more
                                            */
                                        /*
                                            this = this;
                                            org.tlauncher.tlauncher.configuration.InnerConfiguration r0 = org.tlauncher.tlauncher.rmo.TLauncher.getInnerSettings()
                                            java.lang.String r1 = "local.ports.client.play"
                                            java.lang.String[] r0 = r0.getArray(r1)
                                            r6 = r0
                                            r0 = r6
                                            r7 = r0
                                            r0 = r7
                                            int r0 = r0.length
                                            r8 = r0
                                            r0 = 0
                                            r9 = r0
                                        L11:
                                            r0 = r9
                                            r1 = r8
                                            if (r0 >= r1) goto Ld5
                                            r0 = r7
                                            r1 = r9
                                            r0 = r0[r1]
                                            r10 = r0
                                            r0 = r5
                                            org.tlauncher.tlauncher.site.play.SitePlay r0 = org.tlauncher.tlauncher.site.play.SitePlay.this     // Catch: java.lang.Exception -> Lc1
                                            javax.net.ssl.SSLContext r0 = org.tlauncher.tlauncher.site.play.SitePlay.access$000(r0)     // Catch: java.lang.Exception -> Lc1
                                            r11 = r0
                                            r0 = r11
                                            javax.net.ssl.SSLServerSocketFactory r0 = r0.getServerSocketFactory()     // Catch: java.lang.Exception -> Lc1
                                            r12 = r0
                                            r0 = r12
                                            r1 = r10
                                            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Exception -> Lc1
                                            int r1 = r1.intValue()     // Catch: java.lang.Exception -> Lc1
                                            java.net.ServerSocket r0 = r0.createServerSocket(r1)     // Catch: java.lang.Exception -> Lc1
                                            javax.net.ssl.SSLServerSocket r0 = (javax.net.ssl.SSLServerSocket) r0     // Catch: java.lang.Exception -> Lc1
                                            r13 = r0
                                            r0 = r13
                                            r1 = r11
                                            javax.net.ssl.SSLServerSocketFactory r1 = r1.getServerSocketFactory()     // Catch: java.lang.Exception -> Lc1
                                            java.lang.String[] r1 = r1.getSupportedCipherSuites()     // Catch: java.lang.Exception -> Lc1
                                            r0.setEnabledCipherSuites(r1)     // Catch: java.lang.Exception -> Lc1
                                            r0 = 2
                                            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch: java.lang.Exception -> Lc1
                                            r1 = r0
                                            r2 = 0
                                            java.lang.String r3 = "run server on "
                                            r1[r2] = r3     // Catch: java.lang.Exception -> Lc1
                                            r1 = r0
                                            r2 = 1
                                            r3 = r10
                                            r1[r2] = r3     // Catch: java.lang.Exception -> Lc1
                                            org.tlauncher.util.U.log(r0)     // Catch: java.lang.Exception -> Lc1
                                        L5d:
                                            r0 = r13
                                            java.net.Socket r0 = r0.accept()     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            javax.net.ssl.SSLSocket r0 = (javax.net.ssl.SSLSocket) r0     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            r14 = r0
                                            r0 = r14
                                            org.tlauncher.tlauncher.entity.ServerCommandEntity r0 = net.minecraft.launcher.Http.readRequestInfo(r0)     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            r15 = r0
                                            r0 = r15
                                            java.lang.String r0 = r0.getRequestType()     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            java.lang.String r1 = "OPTIONS"
                                            boolean r0 = r0.equals(r1)     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            if (r0 != 0) goto Lad
                                            r0 = r15
                                            java.lang.String r0 = r0.getUrn()     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            java.lang.String r1 = "/"
                                            boolean r0 = r0.equals(r1)     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            if (r0 == 0) goto L94
                                            r0 = r5
                                            org.tlauncher.tlauncher.site.play.SitePlay r0 = org.tlauncher.tlauncher.site.play.SitePlay.this     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            r1 = r15
                                            org.tlauncher.tlauncher.site.play.SitePlay.access$100(r0, r1)     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            goto Lad
                                        L94:
                                            r0 = r15
                                            java.lang.String r0 = r0.getUrn()     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            java.lang.String r1 = "/open/modpack/element"
                                            boolean r0 = r0.startsWith(r1)     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            if (r0 == 0) goto Lad
                                            r0 = r5
                                            r1 = r15
                                            void r0 = () -> { // java.lang.Runnable.run():void
                                                r0.lambda$run$0(r1);
                                            }     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                            java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)     // Catch: java.io.IOException -> Lb0 java.lang.Exception -> Lc1
                                        Lad:
                                            goto L5d
                                        Lb0:
                                            r14 = move-exception
                                            r0 = 1
                                            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch: java.lang.Exception -> Lc1
                                            r1 = r0
                                            r2 = 0
                                            r3 = r14
                                            r1[r2] = r3     // Catch: java.lang.Exception -> Lc1
                                            org.tlauncher.util.U.log(r0)     // Catch: java.lang.Exception -> Lc1
                                            goto L5d
                                        Lc1:
                                            r11 = move-exception
                                            r0 = 1
                                            java.lang.Object[] r0 = new java.lang.Object[r0]
                                            r1 = r0
                                            r2 = 0
                                            r3 = r11
                                            r1[r2] = r3
                                            org.tlauncher.util.U.log(r0)
                                            int r9 = r9 + 1
                                            goto L11
                                        Ld5:
                                            return
                                        */
                                        throw new UnsupportedOperationException("Method not decompiled: org.tlauncher.tlauncher.site.play.SitePlay.AnonymousClass1.run():void");
                                    }
                                });
                                th.setDaemon(true);
                                th.start();
                            }

                            /* JADX INFO: Access modifiers changed from: private */
                            public SSLContext createSSLContext() throws Exception {
                                char[] ksPass = "test123123".toCharArray();
                                char[] ctPass = "test123123".toCharArray();
                                KeyStore ks = KeyStore.getInstance(SSL.DEFAULT_KEYSTORE_TYPE);
                                ks.load(TLauncher.class.getResource("/play_game_server").openStream(), ksPass);
                                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                                kmf.init(ks, ctPass);
                                SSLContext sc = SSLContext.getInstance("TLS");
                                sc.init(kmf.getKeyManagers(), null, null);
                                return sc;
                            }

                            /* JADX INFO: Access modifiers changed from: private */
                            public void runGameWithServer(ServerCommandEntity res) {
                                SiteServer siteServer = (SiteServer) new Gson().fromJson(res.getBody(), (Class<Object>) SiteServer.class);
                                this.tLauncher.getFrame().setAlwaysOnTop(true);
                                this.tLauncher.getFrame().setAlwaysOnTop(false);
                                this.tLauncher.getFrame().mp.defaultScene.loginForm.startLauncher(siteServer);
                            }
                        }
