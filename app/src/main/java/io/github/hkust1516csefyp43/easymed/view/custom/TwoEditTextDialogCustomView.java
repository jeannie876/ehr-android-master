package io.github.hkust1516csefyp43.easymed.view.custom;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.hkust1516csefyp43.easymed.R;


/**
 * Usage:
 * 1) Add/Edit Card
 * Created by Louis on 11/3/16.
 */
public class TwoEditTextDialogCustomView extends LinearLayout {
  AutoCompleteTextView actv;
  TextView tv;
  EditText et;

  /**
   *
   * @param context
   * @param suggestions i.e. keywords suggestions
   * @param title       of the
   */
  public TwoEditTextDialogCustomView(Context context, @Nullable ArrayList<String> suggestions, @Nullable String title) {
    super(context);
    this.setOrientation(VERTICAL);

    TextInputLayout til = new TextInputLayout(context);
    actv = new AutoCompleteTextView(context);
    actv.setDropDownBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_text_color)));
    actv.setHint(title);
    if (suggestions != null) {
      String[] list = new String[suggestions.size()];
      list = suggestions.toArray(list);
      ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_selectable_list_item, list);
      actv.setThreshold(1);
      actv.setAdapter(adapter);
    }

    TextInputLayout til2 = new TextInputLayout(context);

    et = new EditText(context);
    et.setLines(4);
    et.setHint("Description");

    actv.setSingleLine(true);
    actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        et.setFocusableInTouchMode(true);
        et.requestFocus();
      }
    });
//    et.setSingleLine(true);

    til.addView(actv);
    til2.addView(et);

    this.addView(til);
    this.addView(til2);
  }

  /**
   * @param context
   * @param suggestions
   * @param title
   * @param text1
   * @param text2
   * @param displaySwitch
   */
  public TwoEditTextDialogCustomView(Context context, @Nullable ArrayList<String> suggestions, @Nullable String title, @Nullable String text1, @Nullable String text2, boolean displaySwitch) {
    super(context);
    this.setOrientation(VERTICAL);

    if (!displaySwitch) {
      TextInputLayout til = new TextInputLayout(context);
      actv = new AutoCompleteTextView(context);
      actv.setHint(title);
      if (suggestions != null) {
        String[] list = new String[suggestions.size()];
        list = suggestions.toArray(list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_selectable_list_item, list);
        actv.setSingleLine(true);
        actv.setThreshold(1);
        actv.setAdapter(adapter);
      }
      if (text1 != null)
        actv.setText(text1);
      til.addView(actv);
      this.addView(til);
    } else {
      tv = new TextView(context);
      tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
      tv.setGravity(Gravity.CENTER_HORIZONTAL);
      tv.setPadding(0, 0, 0, 16);
      LayoutParams llp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
      tv.setLayoutParams(llp);
      if (text1 != null){
        actv = null;
        tv.setText(text1);
      }
      this.addView(tv);
    }

    TextInputLayout til2 = new TextInputLayout(context);

    et = new EditText(context);
    et.setLines(4);
    et.setHint("Description");
    if (text2 != null)
      et.setText(text2);

    til2.addView(et);

    this.addView(til2);
  }

  public TwoEditTextDialogCustomView(Context context, @Nullable ArrayList<String> suggestions, @Nullable String title, @Nullable String text1, @Nullable String text2, boolean displaySwitch, String[] shortcuts) {
    super(context);
    this.setOrientation(VERTICAL);

    if (!displaySwitch) {
      TextInputLayout til = new TextInputLayout(context);
      actv = new AutoCompleteTextView(context);
      actv.setHint(title);
      if (suggestions != null) {
        String[] list = new String[suggestions.size()];
        list = suggestions.toArray(list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_selectable_list_item, list);
        actv.setSingleLine(true);
        actv.setThreshold(1);
        actv.setAdapter(adapter);
      }
      if (text1 != null)
        actv.setText(text1);
      til.addView(actv);
      this.addView(til);
    } else {
      tv = new TextView(context);
      tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
      tv.setGravity(Gravity.CENTER_HORIZONTAL);
      tv.setPadding(0, 0, 0, 16);
      LayoutParams llp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
      tv.setLayoutParams(llp);
      if (text1 != null){
        actv = null;
        tv.setText(text1);
      }
      this.addView(tv);
    }

    // Description
    TextInputLayout til2 = new TextInputLayout(context);
    et = new EditText(context);
    et.setLines(4);
    et.setHint(R.string.Description);
    if (text2 != null)
      et.setText(text2);
    til2.addView(et);
    //TBC

    //Input shortcuts
    if (shortcuts != null && shortcuts.length > 0) {
      HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
      horizontalScrollView.setPadding(0, 0, 0, 16);
      LinearLayout linearLayout = new LinearLayout(context);
      linearLayout.setOrientation(HORIZONTAL);
      for (final String s:shortcuts) {
        Button button = new Button(context);
        button.setText(s);
        if (et != null) {
          button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              et.append(s + " ");
            }
          });
        }
        linearLayout.addView(button);
      }
      horizontalScrollView.addView(linearLayout);
      this.addView(horizontalScrollView);
    }

    this.addView(til2);
  }

  public ArrayList<String> getData() {
    ArrayList<String> output = new ArrayList<>();
    if (actv != null) {
      output.add(actv.getText().toString());
    } else {
      output.add(tv.getText().toString());
    }
    output.add(et.getText().toString());
    return output;
  }

  public void clearData() {
    if (actv != null)
      actv.setText("");
    if (et != null)
      et.setText("");
  }
}
