package winho.com.print.unit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by xuyating on 2017/3/23.
 */

public class Utils {

    //讀取檔案為 Bitmap
    public Bitmap getLocalBitmap(String path){
        return BitmapFactory.decodeFile(path, getBitmapOptions());
    }
    //把圖片設定為小圖片
    private BitmapFactory.Options getBitmapOptions(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 2;
        return options;
    }
}
