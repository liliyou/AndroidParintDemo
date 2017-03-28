package side.lili.com.androidparintdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import winho.com.print.Board;

public class MainActivity extends AppCompatActivity {

    LinearLayout viewDraw;

    Board board;

    File file2 = new File("/sdcard/im2ImageDir/MeasureData/SavePicture/P-2017326-001.jpg");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        viewDraw = (LinearLayout) findViewById(R.id.viewDraw);
        board = new Board(this, viewDraw, file2.getPath());
        viewDraw.addView(board);
    }


    //載入所有繪圖元件

    //點擊元件

    //移動一小點
}
