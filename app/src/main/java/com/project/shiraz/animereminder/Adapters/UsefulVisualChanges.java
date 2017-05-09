package com.project.shiraz.animereminder.Adapters;

import android.text.TextUtils;
import android.widget.TextView;

class UsefulVisualChanges {
    static void makeMarquee(TextView textView) {
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setMarqueeRepeatLimit(5);
        textView.setSelected(true);
    }
}
