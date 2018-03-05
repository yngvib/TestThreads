package com.example.testthreads;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView mText;

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doWorkAndUpdateCounter();
        }
    };

    Runnable mRunnableInSeparateThread = new Runnable() {
        @Override
        public void run() {
            new Thread( mRunnable ).start();
        }
    };

    Runnable mRunnableUpdateCounter = new Runnable() {
        @Override
        public void run() {
            updateCounter();
        }
    };

    Handler mHandler = new Handler();

    class WorkTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for ( int n=0; n<10; ++n ) {
                work(1);
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void ...params) {
            updateCounter();
        }
    }

    // Simulate some computing.extensive work.
    void work( int sec ) {
        try {
            Thread.sleep( 1000 * sec );  // blocks current thread.
        }
        catch ( Exception e ) {}
    }

    void updateCounter() {
        String numberStr = mText.getText().toString();
        Integer number = Integer.parseInt(numberStr);
        number += 1;
        mText.setText( number.toString() );

    }

    void doWorkAndUpdateCounter() {
        for ( int n=0; n<10; ++n ) {
            work(1);
            //updateCounter();
            //runOnUiThread(mRunnableUpdateCounter);
            //mHandler.post(mRunnableUpdateCounter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = (TextView) findViewById(R.id.disp_text);
        mText.setText( "0" );

        // Take 1
        //doWorkAndUpdateCounter();

        // Take 2
        //mRunnable.run();

        // Take 2
        //mText.post( mRunnable );

        // Take 3
        //mText.postDelayed( mRunnable, 5000 );

        // Take 4
        //mText.postDelayed( mRunnableInSeparateThread, 5000 );

        new WorkTask().execute();
    }
}
