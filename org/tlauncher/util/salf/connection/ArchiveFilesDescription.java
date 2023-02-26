package org.tlauncher.util.salf.connection;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/salf/connection/ArchiveFilesDescription.class */
public class ArchiveFilesDescription {
    private String nameFileArchive;
    private int countFiles;
    private int[] sizeFiles;

    public ArchiveFilesDescription() {
    }

    public ArchiveFilesDescription(String nameFileArchive, int countFiles, int[] sizeFiles) {
        this.nameFileArchive = nameFileArchive;
        this.countFiles = countFiles;
        this.sizeFiles = sizeFiles;
    }

    public String getNameFileArchive() {
        return this.nameFileArchive;
    }

    public void setNameFileArchive(String nameFileArchive) {
        this.nameFileArchive = nameFileArchive;
    }

    public int getCountFiles() {
        return this.countFiles;
    }

    public void setCountFiles(int countFiles) {
        this.countFiles = countFiles;
    }

    public int[] getSizeFiles() {
        return this.sizeFiles;
    }

    public void setSizeFiles(int[] sizeFiles) {
        this.sizeFiles = sizeFiles;
    }
}
