/*
Simple example of a Bluetooth communications activity

Provides a seek-bar (slider) to send values to the server, and a text widget to display the server's reply

Copyright 2018  Gunnar Bowman, Emily Boyes, Trip Calihan, Simon D. Levy, Shepherd Sims

MIT License
*/


package levy.cs.wlu.edu.bluetoothclient;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyCommunicationsActivity extends CommunicationsActivity {

    private String mMessageFromServer = "";

    private TextView mMessageTextView;
    private SeekBar mSpeedSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessageTextView = (TextView)findViewById(R.id.serverReplyText);
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        try {
                            Thread.sleep(100); // Waits for 1 second (1000 milliseconds)
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        char c = (char) mBluetoothConnection.read();
                        if (c == '+') {
                            if (mMessageFromServer.length() > 0) {
                                final String updateWords = mMessageFromServer;
                                Log.e("TAG", "run: " +updateWords);
                                mMessageTextView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //do something
                                        Context context = getApplicationContext();
                                        CharSequence text = updateWords;
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();

                                        //mMessageTextView.setText(updateWords);
                                    }

                                    ;
                                });
                                mMessageFromServer = "";

                            }
                        } else {
                            mMessageFromServer += c;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "run: " + "exception");
                }
            }
        };

        Thread myThread = new Thread(myRunnable);
        myThread.start();


//        mSpeedSeekBar = (SeekBar)findViewById(R.id.seekBar);
//
//        mSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                if (fromUser==true) {
//
//                    for (byte b : String.valueOf(progress).getBytes()) {
//                        mBluetoothConnection.write(b);
//                    }
//                    mBluetoothConnection.write((byte)'.');
//
//                    while (mBluetoothConnection.available() > 0) {
//
//                        char c = (char)mBluetoothConnection.read();
//
//                        if (c == '.') {
//
//                            if (mMessageFromServer.length() > 0) {
//                                mMessageTextView.setText(mMessageFromServer);
//                                mMessageFromServer = "";
//                            }
//                        }
//                        else {
//                            mMessageFromServer += c;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
