package by.gdev.http.download.impl;

import by.gdev.http.download.exeption.StatusExeption;
import by.gdev.http.download.service.Downloader;
import by.gdev.http.upload.download.downloader.DownloadElement;
import by.gdev.http.upload.download.downloader.DownloaderContainer;
import by.gdev.http.upload.download.downloader.DownloaderStatus;
import by.gdev.http.upload.download.downloader.DownloaderStatusEnum;
import com.google.common.eventbus.EventBus;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/download/impl/DownloaderImpl.class */
public class DownloaderImpl implements Downloader {
    private static final Logger log = LoggerFactory.getLogger(DownloaderImpl.class);
    private String pathToDownload;
    private EventBus eventBus;
    private CloseableHttpClient httpclient;
    private RequestConfig requestConfig;
    private Queue<DownloadElement> downloadElements;
    private List<DownloadElement> processedElements;
    private List<Long> allConteinerSize;
    private volatile DownloaderStatusEnum status;
    private DownloadRunnableImpl runnable;
    private volatile Integer allCountElement;
    private long fullDownloadSize;
    private long downloadBytesNow1;
    private LocalTime start;

    public void setPathToDownload(String pathToDownload) {
        this.pathToDownload = pathToDownload;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setHttpclient(CloseableHttpClient httpclient) {
        this.httpclient = httpclient;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public void setDownloadElements(Queue<DownloadElement> downloadElements) {
        this.downloadElements = downloadElements;
    }

    public void setProcessedElements(List<DownloadElement> processedElements) {
        this.processedElements = processedElements;
    }

    public void setAllConteinerSize(List<Long> allConteinerSize) {
        this.allConteinerSize = allConteinerSize;
    }

    public void setStatus(DownloaderStatusEnum status) {
        this.status = status;
    }

    public void setRunnable(DownloadRunnableImpl runnable) {
        this.runnable = runnable;
    }

    public void setAllCountElement(Integer allCountElement) {
        this.allCountElement = allCountElement;
    }

    public void setFullDownloadSize(long fullDownloadSize) {
        this.fullDownloadSize = fullDownloadSize;
    }

    public void setDownloadBytesNow1(long downloadBytesNow1) {
        this.downloadBytesNow1 = downloadBytesNow1;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DownloaderImpl) {
            DownloaderImpl other = (DownloaderImpl) o;
            if (other.canEqual(this)) {
                Object this$pathToDownload = getPathToDownload();
                Object other$pathToDownload = other.getPathToDownload();
                if (this$pathToDownload == null) {
                    if (other$pathToDownload != null) {
                        return false;
                    }
                } else if (!this$pathToDownload.equals(other$pathToDownload)) {
                    return false;
                }
                Object this$eventBus = getEventBus();
                Object other$eventBus = other.getEventBus();
                if (this$eventBus == null) {
                    if (other$eventBus != null) {
                        return false;
                    }
                } else if (!this$eventBus.equals(other$eventBus)) {
                    return false;
                }
                Object this$httpclient = getHttpclient();
                Object other$httpclient = other.getHttpclient();
                if (this$httpclient == null) {
                    if (other$httpclient != null) {
                        return false;
                    }
                } else if (!this$httpclient.equals(other$httpclient)) {
                    return false;
                }
                Object this$requestConfig = getRequestConfig();
                Object other$requestConfig = other.getRequestConfig();
                if (this$requestConfig == null) {
                    if (other$requestConfig != null) {
                        return false;
                    }
                } else if (!this$requestConfig.equals(other$requestConfig)) {
                    return false;
                }
                Object this$downloadElements = getDownloadElements();
                Object other$downloadElements = other.getDownloadElements();
                if (this$downloadElements == null) {
                    if (other$downloadElements != null) {
                        return false;
                    }
                } else if (!this$downloadElements.equals(other$downloadElements)) {
                    return false;
                }
                Object this$processedElements = getProcessedElements();
                Object other$processedElements = other.getProcessedElements();
                if (this$processedElements == null) {
                    if (other$processedElements != null) {
                        return false;
                    }
                } else if (!this$processedElements.equals(other$processedElements)) {
                    return false;
                }
                Object this$allConteinerSize = getAllConteinerSize();
                Object other$allConteinerSize = other.getAllConteinerSize();
                if (this$allConteinerSize == null) {
                    if (other$allConteinerSize != null) {
                        return false;
                    }
                } else if (!this$allConteinerSize.equals(other$allConteinerSize)) {
                    return false;
                }
                Object this$status = getStatus();
                Object other$status = other.getStatus();
                if (this$status == null) {
                    if (other$status != null) {
                        return false;
                    }
                } else if (!this$status.equals(other$status)) {
                    return false;
                }
                Object this$runnable = getRunnable();
                Object other$runnable = other.getRunnable();
                if (this$runnable == null) {
                    if (other$runnable != null) {
                        return false;
                    }
                } else if (!this$runnable.equals(other$runnable)) {
                    return false;
                }
                Object this$allCountElement = getAllCountElement();
                Object other$allCountElement = other.getAllCountElement();
                if (this$allCountElement == null) {
                    if (other$allCountElement != null) {
                        return false;
                    }
                } else if (!this$allCountElement.equals(other$allCountElement)) {
                    return false;
                }
                if (getFullDownloadSize() == other.getFullDownloadSize() && getDownloadBytesNow1() == other.getDownloadBytesNow1()) {
                    Object this$start = getStart();
                    Object other$start = other.getStart();
                    return this$start == null ? other$start == null : this$start.equals(other$start);
                }
                return false;
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DownloaderImpl;
    }

    public int hashCode() {
        Object $pathToDownload = getPathToDownload();
        int result = (1 * 59) + ($pathToDownload == null ? 43 : $pathToDownload.hashCode());
        Object $eventBus = getEventBus();
        int result2 = (result * 59) + ($eventBus == null ? 43 : $eventBus.hashCode());
        Object $httpclient = getHttpclient();
        int result3 = (result2 * 59) + ($httpclient == null ? 43 : $httpclient.hashCode());
        Object $requestConfig = getRequestConfig();
        int result4 = (result3 * 59) + ($requestConfig == null ? 43 : $requestConfig.hashCode());
        Object $downloadElements = getDownloadElements();
        int result5 = (result4 * 59) + ($downloadElements == null ? 43 : $downloadElements.hashCode());
        Object $processedElements = getProcessedElements();
        int result6 = (result5 * 59) + ($processedElements == null ? 43 : $processedElements.hashCode());
        Object $allConteinerSize = getAllConteinerSize();
        int result7 = (result6 * 59) + ($allConteinerSize == null ? 43 : $allConteinerSize.hashCode());
        Object $status = getStatus();
        int result8 = (result7 * 59) + ($status == null ? 43 : $status.hashCode());
        Object $runnable = getRunnable();
        int result9 = (result8 * 59) + ($runnable == null ? 43 : $runnable.hashCode());
        Object $allCountElement = getAllCountElement();
        int result10 = (result9 * 59) + ($allCountElement == null ? 43 : $allCountElement.hashCode());
        long $fullDownloadSize = getFullDownloadSize();
        int result11 = (result10 * 59) + ((int) (($fullDownloadSize >>> 32) ^ $fullDownloadSize));
        long $downloadBytesNow1 = getDownloadBytesNow1();
        int result12 = (result11 * 59) + ((int) (($downloadBytesNow1 >>> 32) ^ $downloadBytesNow1));
        Object $start = getStart();
        return (result12 * 59) + ($start == null ? 43 : $start.hashCode());
    }

    public String toString() {
        return "DownloaderImpl(pathToDownload=" + getPathToDownload() + ", eventBus=" + getEventBus() + ", httpclient=" + getHttpclient() + ", requestConfig=" + getRequestConfig() + ", downloadElements=" + getDownloadElements() + ", processedElements=" + getProcessedElements() + ", allConteinerSize=" + getAllConteinerSize() + ", status=" + getStatus() + ", runnable=" + getRunnable() + ", allCountElement=" + getAllCountElement() + ", fullDownloadSize=" + getFullDownloadSize() + ", downloadBytesNow1=" + getDownloadBytesNow1() + ", start=" + getStart() + ")";
    }

    public DownloaderImpl(String pathToDownload, EventBus eventBus, CloseableHttpClient httpclient, RequestConfig requestConfig, Queue<DownloadElement> downloadElements, List<DownloadElement> processedElements, List<Long> allConteinerSize, DownloaderStatusEnum status, DownloadRunnableImpl runnable, Integer allCountElement, long fullDownloadSize, long downloadBytesNow1, LocalTime start) {
        this.downloadElements = new ConcurrentLinkedQueue();
        this.processedElements = Collections.synchronizedList(new ArrayList());
        this.allConteinerSize = new ArrayList();
        this.pathToDownload = pathToDownload;
        this.eventBus = eventBus;
        this.httpclient = httpclient;
        this.requestConfig = requestConfig;
        this.downloadElements = downloadElements;
        this.processedElements = processedElements;
        this.allConteinerSize = allConteinerSize;
        this.status = status;
        this.runnable = runnable;
        this.allCountElement = allCountElement;
        this.fullDownloadSize = fullDownloadSize;
        this.downloadBytesNow1 = downloadBytesNow1;
        this.start = start;
    }

    public String getPathToDownload() {
        return this.pathToDownload;
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public CloseableHttpClient getHttpclient() {
        return this.httpclient;
    }

    public RequestConfig getRequestConfig() {
        return this.requestConfig;
    }

    public Queue<DownloadElement> getDownloadElements() {
        return this.downloadElements;
    }

    public List<DownloadElement> getProcessedElements() {
        return this.processedElements;
    }

    public List<Long> getAllConteinerSize() {
        return this.allConteinerSize;
    }

    public DownloaderStatusEnum getStatus() {
        return this.status;
    }

    public DownloadRunnableImpl getRunnable() {
        return this.runnable;
    }

    public Integer getAllCountElement() {
        return this.allCountElement;
    }

    public long getFullDownloadSize() {
        return this.fullDownloadSize;
    }

    public long getDownloadBytesNow1() {
        return this.downloadBytesNow1;
    }

    public LocalTime getStart() {
        return this.start;
    }

    public DownloaderImpl(EventBus eventBus, CloseableHttpClient httpclient, RequestConfig requestConfig) {
        this.downloadElements = new ConcurrentLinkedQueue();
        this.processedElements = Collections.synchronizedList(new ArrayList());
        this.allConteinerSize = new ArrayList();
        this.eventBus = eventBus;
        this.httpclient = httpclient;
        this.requestConfig = requestConfig;
        this.status = DownloaderStatusEnum.IDLE;
        this.runnable = new DownloadRunnableImpl(this.downloadElements, this.processedElements, httpclient, requestConfig, eventBus);
    }

    @Override // by.gdev.http.download.service.Downloader
    public void addContainer(DownloaderContainer container) {
        if (Objects.nonNull(container.getRepo().getResources())) {
            container.getRepo().getResources().forEach(metadata -> {
                DownloadElement element = new DownloadElement();
                element.setMetadata(container);
                element.setRepo(container.getRepo());
                element.setPathToDownload(container.getDestinationRepositories());
                element.setHandlers(container.getHandlers());
                this.downloadElements.add(element);
            });
        }
        this.pathToDownload = container.getDestinationRepositories();
        this.allConteinerSize.add(Long.valueOf(container.getContainerSize()));
    }

    @Override // by.gdev.http.download.service.Downloader
    public void startDownload(boolean sync) throws InterruptedException, ExecutionException, StatusExeption, IOException {
        this.fullDownloadSize = totalDownloadSize(this.allConteinerSize);
        this.start = LocalTime.now();
        if (this.status.equals(DownloaderStatusEnum.IDLE)) {
            this.status = DownloaderStatusEnum.WORK;
            this.runnable.setStatus(this.status);
            this.allCountElement = Integer.valueOf(this.downloadElements.size());
            List<CompletableFuture<Void>> listThread = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                listThread.add(CompletableFuture.runAsync(this.runnable));
            }
            if (sync) {
                waitThreadDone(listThread);
                return;
            } else {
                CompletableFuture.runAsync(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x007b: INVOKE  
                      (wrap: java.util.concurrent.CompletableFuture<java.lang.Void> : 0x0078: INVOKE  (r0v16 java.util.concurrent.CompletableFuture<java.lang.Void> A[REMOVE]) = 
                      (wrap: java.lang.Runnable : 0x0073: INVOKE_CUSTOM (r0v15 java.lang.Runnable A[REMOVE]) = 
                      (r4v0 'this' by.gdev.http.download.impl.DownloaderImpl A[D('this' by.gdev.http.download.impl.DownloaderImpl), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r0v10 'listThread' java.util.List<java.util.concurrent.CompletableFuture<java.lang.Void>> A[D('listThread' java.util.List<java.util.concurrent.CompletableFuture<java.lang.Void>>), DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: java.lang.Runnable.run():void
                     call insn: ?: INVOKE  (r0 I:by.gdev.http.download.impl.DownloaderImpl), (r1 I:java.util.List) type: DIRECT call: by.gdev.http.download.impl.DownloaderImpl.lambda$startDownload$1(java.util.List):void)
                     type: STATIC call: java.util.concurrent.CompletableFuture.runAsync(java.lang.Runnable):java.util.concurrent.CompletableFuture)
                     type: VIRTUAL call: java.util.concurrent.CompletableFuture.get():java.lang.Object in method: by.gdev.http.download.impl.DownloaderImpl.startDownload(boolean):void, file: TLauncher-2.876.jar:by/gdev/http/download/impl/DownloaderImpl.class
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:287)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
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
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:117)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                    	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                    	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:805)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:399)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:280)
                    	... 29 more
                    */
                /*
                    this = this;
                    r0 = r4
                    r1 = r4
                    r2 = r4
                    java.util.List<java.lang.Long> r2 = r2.allConteinerSize
                    long r1 = r1.totalDownloadSize(r2)
                    r0.fullDownloadSize = r1
                    r0 = r4
                    java.time.LocalTime r1 = java.time.LocalTime.now()
                    r0.start = r1
                    r0 = r4
                    by.gdev.http.upload.download.downloader.DownloaderStatusEnum r0 = r0.status
                    by.gdev.http.upload.download.downloader.DownloaderStatusEnum r1 = by.gdev.http.upload.download.downloader.DownloaderStatusEnum.IDLE
                    boolean r0 = r0.equals(r1)
                    if (r0 == 0) goto L82
                    r0 = r4
                    by.gdev.http.upload.download.downloader.DownloaderStatusEnum r1 = by.gdev.http.upload.download.downloader.DownloaderStatusEnum.WORK
                    r0.status = r1
                    r0 = r4
                    by.gdev.http.download.impl.DownloadRunnableImpl r0 = r0.runnable
                    r1 = r4
                    by.gdev.http.upload.download.downloader.DownloaderStatusEnum r1 = r1.status
                    r0.setStatus(r1)
                    r0 = r4
                    r1 = r4
                    java.util.Queue<by.gdev.http.upload.download.downloader.DownloadElement> r1 = r1.downloadElements
                    int r1 = r1.size()
                    java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
                    r0.allCountElement = r1
                    java.util.ArrayList r0 = new java.util.ArrayList
                    r1 = r0
                    r1.<init>()
                    r6 = r0
                    r0 = 0
                    r7 = r0
                L4c:
                    r0 = r7
                    r1 = 3
                    if (r0 >= r1) goto L65
                    r0 = r6
                    r1 = r4
                    by.gdev.http.download.impl.DownloadRunnableImpl r1 = r1.runnable
                    java.util.concurrent.CompletableFuture r1 = java.util.concurrent.CompletableFuture.runAsync(r1)
                    boolean r0 = r0.add(r1)
                    int r7 = r7 + 1
                    goto L4c
                L65:
                    r0 = r5
                    if (r0 == 0) goto L71
                    r0 = r4
                    r1 = r6
                    r0.waitThreadDone(r1)
                    goto L7f
                L71:
                    r0 = r4
                    r1 = r6
                    void r0 = () -> { // java.lang.Runnable.run():void
                        r0.lambda$startDownload$1(r1);
                    }
                    java.util.concurrent.CompletableFuture r0 = java.util.concurrent.CompletableFuture.runAsync(r0)
                    java.lang.Object r0 = r0.get()
                L7f:
                    goto L91
                L82:
                    by.gdev.http.download.exeption.StatusExeption r0 = new by.gdev.http.download.exeption.StatusExeption
                    r1 = r0
                    r2 = r4
                    by.gdev.http.upload.download.downloader.DownloaderStatusEnum r2 = r2.status
                    java.lang.String r2 = r2.toString()
                    r1.<init>(r2)
                    throw r0
                L91:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: by.gdev.http.download.impl.DownloaderImpl.startDownload(boolean):void");
            }

            @Override // by.gdev.http.download.service.Downloader
            public void cancelDownload() {
                this.status = DownloaderStatusEnum.CANCEL;
                this.runnable.setStatus(DownloaderStatusEnum.CANCEL);
            }

            private DownloaderStatus buildDownloaderStatus() throws IOException {
                DownloaderStatus statusDownload = new DownloaderStatus();
                long downloadBytesNow = 0;
                List<DownloadElement> list = new ArrayList<>(this.processedElements);
                List<Throwable> errorList = new ArrayList<>();
                double thirty = Duration.between(this.start, LocalTime.now()).getSeconds();
                for (DownloadElement elem : list) {
                    downloadBytesNow += elem.getDownloadBytes();
                    if (Objects.nonNull(elem.getError())) {
                        errorList.add(elem.getError());
                    }
                }
                statusDownload.setThrowables(errorList);
                statusDownload.setDownloadSize(sizeDownloadNow());
                statusDownload.setSpeed((downloadBytesNow / 1048576) / thirty);
                statusDownload.setDownloaderStatusEnum(this.status);
                statusDownload.setAllDownloadSize(this.fullDownloadSize);
                statusDownload.setLeftFiles(Integer.valueOf(this.processedElements.size()));
                statusDownload.setAllFiles(this.allCountElement);
                return statusDownload;
            }

            private void waitThreadDone(List<CompletableFuture<Void>> listThread) throws InterruptedException, IOException {
                LocalTime start = LocalTime.now();
                boolean workedAnyThread = true;
                while (workedAnyThread) {
                    Thread.sleep(50L);
                    workedAnyThread = listThread.stream().anyMatch(e -> {
                        return !e.isDone();
                    });
                    if (start.isBefore(LocalTime.now())) {
                        start = start.plusSeconds(1L);
                        if (this.allCountElement.intValue() != 0 && start.getSecond() != start.plusSeconds(1L).getSecond()) {
                            this.eventBus.post(buildDownloaderStatus());
                        }
                    }
                }
                this.status = DownloaderStatusEnum.DONE;
                this.eventBus.post(buildDownloaderStatus());
            }

            private long totalDownloadSize(List<Long> containerSize) {
                long sum = 0;
                for (Long l : containerSize) {
                    long l2 = l.longValue();
                    sum += l2;
                }
                return sum;
            }

            private long sizeDownloadNow() throws IOException {
                return Files.walk(Paths.get(this.pathToDownload, new String[0]), new FileVisitOption[0]).filter(p -> {
                    return p.toFile().isFile();
                }).mapToLong(p2 -> {
                    return p2.toFile().length();
                }).sum();
            }
        }
