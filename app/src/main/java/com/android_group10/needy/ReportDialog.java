package com.android_group10.needy;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReportDialog extends Dialog {
    protected ReportDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

   /* public ReportDialog(@NonNull Context context, @Nullable OnCancelListener cancelListener, String postUID, String blamedUserUID){
        ReportDialog(context, true, cancelListener);
    }*/
}
