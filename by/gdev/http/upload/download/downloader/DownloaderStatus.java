package by.gdev.http.upload.download.downloader;

import java.time.Duration;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:by/gdev/http/upload/download/downloader/DownloaderStatus.class */
public class DownloaderStatus {
    private Duration duration;
    private double speed;
    private long downloadSize;
    private long allDownloadSize;
    private Integer leftFiles;
    private Integer allFiles;
    private List<Throwable> throwables;
    private DownloaderStatusEnum downloaderStatusEnum;

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public void setAllDownloadSize(long allDownloadSize) {
        this.allDownloadSize = allDownloadSize;
    }

    public void setLeftFiles(Integer leftFiles) {
        this.leftFiles = leftFiles;
    }

    public void setAllFiles(Integer allFiles) {
        this.allFiles = allFiles;
    }

    public void setThrowables(List<Throwable> throwables) {
        this.throwables = throwables;
    }

    public void setDownloaderStatusEnum(DownloaderStatusEnum downloaderStatusEnum) {
        this.downloaderStatusEnum = downloaderStatusEnum;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DownloaderStatus) {
            DownloaderStatus other = (DownloaderStatus) o;
            if (other.canEqual(this)) {
                Object this$duration = getDuration();
                Object other$duration = other.getDuration();
                if (this$duration == null) {
                    if (other$duration != null) {
                        return false;
                    }
                } else if (!this$duration.equals(other$duration)) {
                    return false;
                }
                if (Double.compare(getSpeed(), other.getSpeed()) == 0 && getDownloadSize() == other.getDownloadSize() && getAllDownloadSize() == other.getAllDownloadSize()) {
                    Object this$leftFiles = getLeftFiles();
                    Object other$leftFiles = other.getLeftFiles();
                    if (this$leftFiles == null) {
                        if (other$leftFiles != null) {
                            return false;
                        }
                    } else if (!this$leftFiles.equals(other$leftFiles)) {
                        return false;
                    }
                    Object this$allFiles = getAllFiles();
                    Object other$allFiles = other.getAllFiles();
                    if (this$allFiles == null) {
                        if (other$allFiles != null) {
                            return false;
                        }
                    } else if (!this$allFiles.equals(other$allFiles)) {
                        return false;
                    }
                    Object this$throwables = getThrowables();
                    Object other$throwables = other.getThrowables();
                    if (this$throwables == null) {
                        if (other$throwables != null) {
                            return false;
                        }
                    } else if (!this$throwables.equals(other$throwables)) {
                        return false;
                    }
                    Object this$downloaderStatusEnum = getDownloaderStatusEnum();
                    Object other$downloaderStatusEnum = other.getDownloaderStatusEnum();
                    return this$downloaderStatusEnum == null ? other$downloaderStatusEnum == null : this$downloaderStatusEnum.equals(other$downloaderStatusEnum);
                }
                return false;
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DownloaderStatus;
    }

    public int hashCode() {
        Object $duration = getDuration();
        int result = (1 * 59) + ($duration == null ? 43 : $duration.hashCode());
        long $speed = Double.doubleToLongBits(getSpeed());
        int result2 = (result * 59) + ((int) (($speed >>> 32) ^ $speed));
        long $downloadSize = getDownloadSize();
        int result3 = (result2 * 59) + ((int) (($downloadSize >>> 32) ^ $downloadSize));
        long $allDownloadSize = getAllDownloadSize();
        int result4 = (result3 * 59) + ((int) (($allDownloadSize >>> 32) ^ $allDownloadSize));
        Object $leftFiles = getLeftFiles();
        int result5 = (result4 * 59) + ($leftFiles == null ? 43 : $leftFiles.hashCode());
        Object $allFiles = getAllFiles();
        int result6 = (result5 * 59) + ($allFiles == null ? 43 : $allFiles.hashCode());
        Object $throwables = getThrowables();
        int result7 = (result6 * 59) + ($throwables == null ? 43 : $throwables.hashCode());
        Object $downloaderStatusEnum = getDownloaderStatusEnum();
        return (result7 * 59) + ($downloaderStatusEnum == null ? 43 : $downloaderStatusEnum.hashCode());
    }

    public String toString() {
        return "DownloaderStatus(duration=" + getDuration() + ", speed=" + getSpeed() + ", downloadSize=" + getDownloadSize() + ", allDownloadSize=" + getAllDownloadSize() + ", leftFiles=" + getLeftFiles() + ", allFiles=" + getAllFiles() + ", throwables=" + getThrowables() + ", downloaderStatusEnum=" + getDownloaderStatusEnum() + ")";
    }

    public Duration getDuration() {
        return this.duration;
    }

    public double getSpeed() {
        return this.speed;
    }

    public long getDownloadSize() {
        return this.downloadSize;
    }

    public long getAllDownloadSize() {
        return this.allDownloadSize;
    }

    public Integer getLeftFiles() {
        return this.leftFiles;
    }

    public Integer getAllFiles() {
        return this.allFiles;
    }

    public List<Throwable> getThrowables() {
        return this.throwables;
    }

    public DownloaderStatusEnum getDownloaderStatusEnum() {
        return this.downloaderStatusEnum;
    }
}
