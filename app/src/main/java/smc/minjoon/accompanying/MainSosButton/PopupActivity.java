package smc.minjoon.accompanying.MainSosButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import smc.minjoon.accompanying.MainSosButton.Sos.AlwaysOnTopService;
import smc.minjoon.accompanying.R;

public class PopupActivity extends Activity {
    int datanum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);
        LinearLayout ll01 = (LinearLayout) findViewById(R.id.ll01);
        ll01.bringToFront();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);// 이게 없앤다기보단 잠금화면 위로 올리는 코드
        datanum = 0;
        //UI 객체생성
        TextView txtText = (TextView) findViewById(R.id.txtText);

        //데이터 가져오기

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(datanum != 1) {
                    Intent intent = new Intent(getApplicationContext(), AlwaysOnTopService.class);
                    intent.putExtra("result", "이런");
                    startService(intent);
                    finish();
                }
            }
        },3000);
    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        Intent intent = new Intent(getApplicationContext(), AlwaysOnTopService.class);
        intent.putExtra("result", "Close");
        startService(intent);
        datanum =1;
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }


}
