package org.apache.log4j.chainsaw;

import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: TLauncher-2.876.jar:org/apache/log4j/chainsaw/EventDetails.class */
public class EventDetails {
    private final long mTimeStamp;
    private final Priority mPriority;
    private final String mCategoryName;
    private final String mNDC;
    private final String mThreadName;
    private final String mMessage;
    private final String[] mThrowableStrRep;
    private final String mLocationDetails;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EventDetails(long aTimeStamp, Priority aPriority, String aCategoryName, String aNDC, String aThreadName, String aMessage, String[] aThrowableStrRep, String aLocationDetails) {
        this.mTimeStamp = aTimeStamp;
        this.mPriority = aPriority;
        this.mCategoryName = aCategoryName;
        this.mNDC = aNDC;
        this.mThreadName = aThreadName;
        this.mMessage = aMessage;
        this.mThrowableStrRep = aThrowableStrRep;
        this.mLocationDetails = aLocationDetails;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EventDetails(LoggingEvent aEvent) {
        this(aEvent.timeStamp, aEvent.getLevel(), aEvent.getLoggerName(), aEvent.getNDC(), aEvent.getThreadName(), aEvent.getRenderedMessage(), aEvent.getThrowableStrRep(), aEvent.getLocationInformation() == null ? null : aEvent.getLocationInformation().fullInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Priority getPriority() {
        return this.mPriority;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getCategoryName() {
        return this.mCategoryName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getNDC() {
        return this.mNDC;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getThreadName() {
        return this.mThreadName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getMessage() {
        return this.mMessage;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getLocationDetails() {
        return this.mLocationDetails;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String[] getThrowableStrRep() {
        return this.mThrowableStrRep;
    }
}
