package com.blogspot.rajbtc.onlineclass.dataclass;

import com.google.firebase.database.DatabaseReference;

public class SlideDataforAdapter {
    private DatabaseReference reference;
    private String slideName,givenBy,link;

    public SlideDataforAdapter(){}

    public SlideDataforAdapter(DatabaseReference reference, String slideName, String givenBy, String link) {
        this.reference=reference;
        this.slideName = slideName;
        this.givenBy = givenBy;
        this.link = link;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public String getSlideName() {
        return slideName;
    }

    public String getGivenBy() {
        return givenBy;
    }

    public String getLink() {
        return link;
    }
}
