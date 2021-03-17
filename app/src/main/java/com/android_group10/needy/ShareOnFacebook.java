package com.android_group10.needy;

import android.net.Uri;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;


public class ShareOnFacebook {


    public ShareLinkContent createShare() {
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.facebook.com/Needy-106825894801011"))
                .setQuote(
                        "Needy is a bridge between people to make the live more easy and help each other," +
                                " join us on Facebook ,and you can download owr application"
                                + "App id ..... ")
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#Needy")
                        .build())

                .build();

        return linkContent;

    }
}
