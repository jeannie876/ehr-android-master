package io.github.hkust1516csefyp43.easymed.utility;

import android.os.Environment;
import android.util.Log;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by Louis on 6/5/16.
 */
public class ExcelGenerator {

  public static final String TAG = ExcelGenerator.class.getSimpleName();

  /**
   *
   * @param fileName - the name to give the new workbook file
   * @return - a new WritableWorkbook with the given fileName
   */
  public static void createWorkbook(String fileName){

    //get the sdcard's directory
    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Easymed");
    Log.d(TAG, "dir: " + dir.getAbsolutePath());
    if (!dir.exists()) {
      boolean yes = dir.mkdirs();
      Log.d(TAG, "made folder: " + String.valueOf(yes));
    }
    File wbfile = new File(dir,fileName);
    WritableWorkbook m_workbook;

    if(!wbfile.exists())
    {
      try
      {
        m_workbook = Workbook.createWorkbook(new File(dir, fileName));

        // this will create new new sheet in workbook
        WritableSheet sheet = m_workbook.createSheet("hobbies", 0);

        // this will add label in excel sheet
        Label label = new Label(0, 0, "id");
        sheet.addCell(label);

        Label label2 = new Label(1, 0, "hobbies");
        sheet.addCell(label2);

        Label m_idValue = new Label(0,1,"1");
        sheet.addCell(m_idValue);

      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }


}
