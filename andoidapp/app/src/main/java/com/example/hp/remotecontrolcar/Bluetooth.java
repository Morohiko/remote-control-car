package com.example.hp.remotecontrolcar;

import android.app.Activity;
import android.bluetooth.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Timer;

class Bluetooth extends Activity implements Serializable{
    private BluetoothSocket socket;

    private String deviceaddress = "98:D3:31:FD:1E:61";
    private OutputStream outStream = null;
    private InputStream inStream = null;
    private TimerTask runn;

    void conn() throws Exception{
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = adapter.getRemoteDevice(deviceaddress);
        adapter.cancelDiscovery();

        Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});

        if (socket != null) {
            socket.close();
        }
        socket = (BluetoothSocket) m.invoke(device, 1);
        socket.connect();
        outStream = socket.getOutputStream();
        inStream = socket.getInputStream();
        initTimer();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(runn, 10, 100);
    }

    void initTimer(){
        runn = new TimerTask() {
            @Override
            public void run() {
                read();
            }
        };
    }

    void discon() throws Exception{
        outStream.close();
        socket.close();
    }

    void discon1(){
        try {
            outStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void read() {
        try {
            if (inStream.available() > 10000) inStream.skip(10000);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    boolean sendData(int i1) {
        Integer i = i1;
        byte msgBuffer = i.byteValue();
        try {
            outStream.write(msgBuffer);
            if (outStream != null) {
                outStream.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
