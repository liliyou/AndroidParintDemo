package side.lili.com.androidparintdemo;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout parentPanel;
    Board board;


    String imageName="TEST_NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView()
    {
        parentPanel = (ConstraintLayout) findViewById(R.id.parentPanel);
        board = new Board(this,"設定檔的位置");
        parentPanel.addView(board);
    }
    //載入所有繪圖元件

    //點擊元件

    //移動一小點
}
