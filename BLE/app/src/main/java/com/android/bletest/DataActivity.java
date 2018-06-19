package com.android.bletest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.ByteBuffer;

/**
 * Created by 601042 on 2017/5/26.
 */
public class DataActivity extends AppCompatActivity {
    private static final String TAG = "DataActivity";
    Button data_btn_write;
    Button data_btn_notify;
    Button data_btn_send;
    EditText data_edt_write;
    TextView data_tv_show;
    Handler mHandler = new Handler();
    GetDataCallback callback = new GetDataCallback() {
        @Override
        public void onGetData(String characteristicUUid, final byte[] data) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (data != null) {
                        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                        String content = new String(byteBuffer.array());
                        data_tv_show.setText(data_tv_show.getText().toString() + content + "\n");
                    }
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        initView();
        AppContext.getInstance().getmBle().setDataCallback(callback);

    }

    private void initView() {
        data_btn_write = (Button) findViewById(R.id.data_btn_write);
        data_btn_notify = (Button) findViewById(R.id.data_btn_notify);
        data_btn_send = (Button) findViewById(R.id.data_btn_send);

        data_edt_write = (EditText) findViewById(R.id.data_edt_write);
        data_tv_show = (TextView) findViewById(R.id.data_tv_show);
        data_btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DataActivity.this, ServicesList.class);
                i.putExtra("type", 1);
                startActivity(i);
            }
        });
        data_btn_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DataActivity.this, ServicesList.class);
                i.putExtra("type", 0);
                startActivity(i);
            }
        });
        data_btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = data_edt_write.getText().toString();
                if (!TextUtils.isEmpty(text))
                    AppContext.getInstance().getmBle().sendByBLE(text);
            }
        });


    }
}
