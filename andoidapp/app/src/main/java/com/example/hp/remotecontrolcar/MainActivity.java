package com.example.hp.remotecontrolcar;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initJoystick();
    }

    private TextView textResult;
    private SeekBar seekBarForward;
    private SeekBar seekBarAside;
    private Button btnConnect;
    private Bluetooth bluetooth = null;

    private void initJoystick(){
        textResult = findViewById(R.id.textResult);
        seekBarForward = findViewById(R.id.seekBarForward);
        seekBarAside = findViewById(R.id.seekBarAside);
        btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setText("Connect");
        seekBarForward.setRotation(270);
        seekBarAside.setRotation(270);
        seekBarForward.setProgress(50);
        seekBarAside.setProgress(50);
        listeners();

    }

    private void listeners(){
        seekBarForward.setOnSeekBarChangeListener(seekBarChangeListenerForward);
        seekBarAside.setOnSeekBarChangeListener(seekBarChangeListenerAside);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryConnect();
            }
        });
    }

    private void tryConnect(){
        if (bluetooth == null){
            bluetooth = new Bluetooth();
            try {
                bluetooth.conn();
            } catch (Exception e) {
                e.printStackTrace();
                bluetooth = null;
                textResult.setText("ErrorConnect");
                try {
                    bluetooth.discon();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return;
            }
            btnConnect.setText("Disconnect");
        }else {
            try {
                bluetooth.discon();
            } catch (Exception e) {
                textResult.setText("ErrorDisconnect");
                return;
            }
            bluetooth = null;
            btnConnect.setText("Connect");
        }
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListenerForward = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(bluetooth==null){
                try {
                    bluetooth.conn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            bluetooth.sendData(progress);
            setText("ForwardProgress", progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if(bluetooth==null){
                try {
                    bluetooth.conn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setText("ForwardStart", seekBar.getProgress());
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            bluetooth.sendData(50);
            seekBar.setProgress(50);
            setText("ForwardStop", seekBar.getProgress());
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListenerAside = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            bluetooth.sendData(progress+101);
            setText("AsideProgress", progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            setText("AsideStart", seekBar.getProgress());
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            bluetooth.sendData(150);
            seekBar.setProgress(50);
            setText("AsideStop", seekBar.getProgress());
        }
    };

    void setText(String tex, int i){
        textResult.setText(tex + " = " + i);
    }

}
