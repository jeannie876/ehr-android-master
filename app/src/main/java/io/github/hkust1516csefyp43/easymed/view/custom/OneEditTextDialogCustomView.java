package io.github.hkust1516csefyp43.easymed.view.custom;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.Serializable;

/**
 * Usage:
 * 1) Add Medicine
 * Created by Louis on 28/6/2016.
 */

public class OneEditTextDialogCustomView extends LinearLayout {
  public String title;
  public EditText et;

  public OneEditTextDialogCustomView(Context context, String title) {
    super(context);
    this.setOrientation(VERTICAL);

    TextInputLayout til = new TextInputLayout(context);
    et = new EditText(context);
    et.setHint("Name of " + title);
    til.addView(et);

  }

  public Serializable getData() throws Throwable {
    if (et == null) {
      throw new Throwable("Null EditText");
    } else if (et.getText().length() <= 0) {
      throw new Throwable("Empty input");
    } else {
      return et.getText().toString();
    }
  }

}
