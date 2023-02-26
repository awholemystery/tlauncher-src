package org.tlauncher.util.review;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/review/FolderEnum.class */
public enum FolderEnum {
    COMPLAINT { // from class: org.tlauncher.util.review.FolderEnum.1
        @Override // org.tlauncher.util.review.FolderEnum
        public String getName() {
            return "Complaint";
        }
    },
    THANK { // from class: org.tlauncher.util.review.FolderEnum.2
        @Override // org.tlauncher.util.review.FolderEnum
        public String getName() {
            return "Thank";
        }
    },
    BUG { // from class: org.tlauncher.util.review.FolderEnum.3
        @Override // org.tlauncher.util.review.FolderEnum
        public String getName() {
            return "Bug";
        }
    },
    LACK { // from class: org.tlauncher.util.review.FolderEnum.4
        @Override // org.tlauncher.util.review.FolderEnum
        public String getName() {
            return "Lack";
        }
    },
    WISH { // from class: org.tlauncher.util.review.FolderEnum.5
        @Override // org.tlauncher.util.review.FolderEnum
        public String getName() {
            return "Wish";
        }
    };

    public abstract String getName();
}
