package com.viamhealth.android.model.trackgrowth;

import java.util.Date;

/**
 * Created by kunal on 10/3/14.
 */
public class UserTrackGrowthData extends TrackGrowth {
    private Date entryDate;
    private Long userId = 0L;

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entry_date) {
        this.entryDate = entry_date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
