package org.apache.log4j.helpers;

import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/helpers/DateTimeDateFormat.class */
public class DateTimeDateFormat extends AbsoluteTimeDateFormat {
    private static final long serialVersionUID = 5547637772208514971L;
    String[] shortMonths;

    public DateTimeDateFormat() {
        this.shortMonths = new DateFormatSymbols().getShortMonths();
    }

    public DateTimeDateFormat(TimeZone timeZone) {
        this();
        setCalendar(Calendar.getInstance(timeZone));
    }

    @Override // org.apache.log4j.helpers.AbsoluteTimeDateFormat, java.text.DateFormat
    public StringBuffer format(Date date, StringBuffer sbuf, FieldPosition fieldPosition) {
        this.calendar.setTime(date);
        int day = this.calendar.get(5);
        if (day < 10) {
            sbuf.append('0');
        }
        sbuf.append(day);
        sbuf.append(' ');
        sbuf.append(this.shortMonths[this.calendar.get(2)]);
        sbuf.append(' ');
        int year = this.calendar.get(1);
        sbuf.append(year);
        sbuf.append(' ');
        return super.format(date, sbuf, fieldPosition);
    }

    @Override // org.apache.log4j.helpers.AbsoluteTimeDateFormat, java.text.DateFormat
    public Date parse(String s, ParsePosition pos) {
        return null;
    }
}
