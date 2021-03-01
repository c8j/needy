package com.android_group10.needy;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Report implements Serializable {
    private String postUID;
    private boolean reacted;
    private String reportAuthorUID;
    private String blamedUserUID;
    private String description;

    public Report(){
    }

    public Report(String postUID, String reportAuthorUID, String blamedUserUID, String description){
        this.postUID = postUID;
        this.reportAuthorUID = reportAuthorUID;
        this.blamedUserUID = blamedUserUID;
        this.description = description;
        reacted = false;
    }

    public void setBlamedUserUID(String blamedUserUID) {
        this.blamedUserUID = blamedUserUID;
    }

    public void setPostUID(String postUID) {
        this.postUID = postUID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReportAuthorUID(String reportAuthorUID) {
        this.reportAuthorUID = reportAuthorUID;
    }

    public void setReacted(boolean reacted) {
        this.reacted = reacted;
    }

    public String getBlamedUserUID() {
        return blamedUserUID;
    }

    public String getDescription() {
        return description;
    }

    public String getPostUID() {
        return postUID;
    }

    public String getReportAuthorUID() {
        return reportAuthorUID;
    }

    public boolean isReacted() {
        return reacted;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("postUID", postUID);
        result.put("reacted", reacted);
        result.put("description", description);
        result.put("reportAuthorUID", reportAuthorUID);
        result.put("blamedUserUID", blamedUserUID);
        return result;
    }
}
