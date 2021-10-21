package com.sinnia.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sinnia.R;
import com.sinnia.preferences.SiniiaPreferences;

public class CongratulationsActiviity extends AppCompatActivity {

    private Context mContext;
    private SiniiaPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);

        mContext=getApplicationContext();
        prefs=new SiniiaPreferences(mContext);

        TextView mTxtName=(TextView)findViewById(R.id.txt_name);
        Button mBtnLetStart=(Button)findViewById(R.id.btn_let_start);

        String name=prefs.getUserName();
        mTxtName.setText("Hi, "+name);

        mBtnLetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,SelectTypeActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

    }
}
