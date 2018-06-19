package com.android.bletest;

import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 601042 on 2017/5/26.
 */
public class ServicesList extends AppCompatActivity {
    ListView list_services;
    ServicesAdapter adapter;
    List<BluetoothGattService> servicesList = new ArrayList<>();
    int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceslist);
        type = getIntent().getIntExtra("type",0);
        initListview();
    }

    private void initListview() {
        list_services = (ListView) findViewById(R.id.list_services);
        servicesList = AppContext.getInstance().getmBle().getServicesList();
        adapter = new ServicesAdapter(this,servicesList);
        list_services.setAdapter(adapter);
        list_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("fuck","----ServicesList----->type = " + type);
                switch (type){
                    case 0:
                        AppContext.getInstance().getmBle().characteristicNotification( servicesList.get(position).getUuid().toString());
                        break;
                    case 1:
                        Intent intent = new Intent(ServicesList.this,CharacteristicList.class);
                        intent.putExtra("service", servicesList.get(position).getUuid().toString());
                        intent.putExtra("type", type);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent intents = new Intent(ServicesList.this,CharacteristicList.class);
                        intents.putExtra("service", servicesList.get(position).getUuid().toString());
                        intents.putExtra("type", type);
                        startActivity(intents);
                        break;
                    case 3:
                        Intent intentss = new Intent(ServicesList.this,CharacteristicList.class);
                        intentss.putExtra("service", servicesList.get(position).getUuid().toString());
                        intentss.putExtra("type", 0);
                        startActivity(intentss);
                        break;

                }


            }
        });
    }
}
