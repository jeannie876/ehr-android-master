package io.github.hkust1516csefyp43.easymed.view.custom;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Louis on 26/5/2016.
 */
public class TableRenderer {

  public static View renderString(final String value, Context context) {
    final TextView textView = new TextView(context);
    textView.setText(value);
    textView.setPadding(20, 10, 20, 10);
    textView.setTextSize(14);
    return textView;
  }

}
