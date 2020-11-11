package com.narrowstudio.wonski.linguastic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;


public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    private WordManager mWM;
    private TextView spanishTV, englishTV, spanishExp, englishExp;
    private int  seconds, secSet=5;
    private long startTime = 0, millis;
    private boolean running = false;
    private ImageView playButton;
    private boolean stateDarkModeSwitch, stateDoubleTimeSwitch;
    private SharedPreferences preferences;
    private int stateTime;
    private static final String SHARED_PREFS = "PREFS";
    final WindowManager.LayoutParams params = show();
    NotificationCompat.Builder builder;
    Notification notification;
    NotificationManager notificationManager;
    Intent notificationHomeIntent;
    PendingIntent pendingHomeIntent;

    int notificationID = 1; //ID of notification

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            millis = System.currentTimeMillis() - startTime;
            seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            if(seconds>secSet-1){
                String lineS = (String) mWM.getRandomLine();
                if(lineS != null) {
                    diviningString(lineS);
                    timerHandler.removeCallbacks(timerRunnable);
                    startTime = System.currentTimeMillis();
                }
                else{
                    timerHandler.removeCallbacks(timerRunnable);
                }
            }

            timerHandler.postDelayed(this, 50);
        }
    };




    public FloatingViewService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand (Intent intent, int flags, int startId) {
        createNotificationChannel();
        notificationHomeIntent = new Intent(this, MainActivity.class);
        pendingHomeIntent = PendingIntent.getActivity(this,
                0, notificationHomeIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //play / pause action Intent
        Intent playBroadcastIntent = new Intent(this, ServiceReceiver.class).setAction("play");
        PendingIntent playActionIntent = PendingIntent.getBroadcast(this,
                0, playBroadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //kill action Intent
        Intent killBroadcastIntent = new Intent(this, ServiceReceiver.class).setAction("kill");
        PendingIntent killActionIntent = PendingIntent.getBroadcast(this,
                0, killBroadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //expand action Intent
        Intent expandBroadcastIntent = new Intent(this, ServiceReceiver.class).setAction("expand");
        PendingIntent expandActionIntent = PendingIntent.getBroadcast(this,
                0, expandBroadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //audio action Intent
        Intent audioBroadcastIntent = new Intent(this, ServiceReceiver.class).setAction("audio");
        PendingIntent audioActionIntent = PendingIntent.getBroadcast(this,
                0, audioBroadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        notification = new NotificationCompat.Builder(this, getResources().getString(R.string.service_channel))
                .setContentText(getString(R.string.service_context))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setNotificationSilent()
                .setContentIntent(playActionIntent)
                .addAction(R.mipmap.ic_launcher, getString(R.string.service_button11), audioActionIntent)
                .addAction(R.mipmap.ic_launcher, getString(R.string.service_button21), expandActionIntent)
                .addAction(R.mipmap.ic_launcher, getString(R.string.service_button31), killActionIntent)
                .setShowWhen(false)
                .build();

        startForeground(notificationID, notification);


        mWM = (WordManager) intent.getExtras().get("list");
        mWM.setMax(mWM.getSize());
        int posit = (Integer) intent.getExtras().get("position");
        mWM.setCurrentPosition(posit);

        String lineS = (String) mWM.getRandomLine();
        diviningString(lineS);
        return Service.START_STICKY;
    }




    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the floating view layout we created
        preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        stateDarkModeSwitch = preferences.getBoolean("dark_mode", false);
        stateDoubleTimeSwitch = preferences.getBoolean("double_time", false);
        stateTime = preferences.getInt("time", 0);
        IntentFilter filter = new IntentFilter("com.narrowstudio.wonski.linguastic.SERVICE_ACTION");
        registerReceiver(mBroadcastReceiver, filter);



        if (stateDarkModeSwitch) {
            setTheme(R.style.AppDarkTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }

        if (stateTime == 0) {
            secSet = 5;
        }
        else if (stateTime == 1) {
            secSet = 10;
        }
        else if (stateTime == 2) {
            secSet = 15;
        }

        if (stateDoubleTimeSwitch){
            secSet = secSet * 2;
        }


        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);


        spanishTV = (TextView) mFloatingView.findViewById(R.id.spanishTV);
        englishTV = (TextView) mFloatingView.findViewById(R.id.englishTV);
        spanishExp = (TextView) mFloatingView.findViewById(R.id.spanishExp);
        englishExp = (TextView) mFloatingView.findViewById(R.id.englishExp);

        //mWM = (WordManager) getIntent().getSerializableExtra("list");

        //final WindowManager.LayoutParams params = show();


        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //mFloatingView.setSystemUiVisibility(getUIFlag());
        mWindowManager.addView(mFloatingView, params);





        //The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);

        //Set the close button
/*       ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopSelf();
            }
        });


*/



        //Set the view while floating view is expanded.
        //Set the play button.
        playButton = (ImageView) mFloatingView.findViewById(R.id.play_btn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSwitch();

            }
        });


        //Set the next button.
        ImageView nextButton = (ImageView) mFloatingView.findViewById(R.id.next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHandler.removeCallbacks(timerRunnable);
                String lineS = (String) mWM.getRandomLine();
                if(lineS != null) {
                    diviningString(lineS);
                }
                else{
                    //ArrayList<String> conv = mWM.getWordArray();
                    //mWM = new WordManager(conv);
                }
                seconds = 0;
                if(running == true){
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                }
            }
        });


        //Set the pause button.
        ImageView prevButton = (ImageView) mFloatingView.findViewById(R.id.prev_btn);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHandler.removeCallbacks(timerRunnable);
                String lineS = (String) mWM.getPrevLine();
                if(lineS != null) {
                    diviningString(lineS);
                }
                seconds = 0;
                if(running == true){
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                }

            }
        });


        //Set the close button
        ImageView closeButton = (ImageView) mFloatingView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopSelf();


            }
        });


        //Open the application on thi button click
        ImageView openButton = (ImageView) mFloatingView.findViewById(R.id.open_button);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open the application  click.

                goFull();

            }
        });

        //Drag and move floating view using user's touch action.
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 5 && Ydiff < 5 && Xdiff > -5 && Ydiff > -5) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                            else{
                                collapsedView.setVisibility(View.VISIBLE);
                                expandedView.setVisibility(View.GONE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;

                }
                return false;
            }


        });







    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    getResources().getString(R.string.service_channel),
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_MIN
            );
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
    }

    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    private int getUIFlag(){
        int mFlag;
        mFlag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LOW_PROFILE;
        return mFlag;
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Display display = mWindowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int tx, ty;
        int nxp, nyp;
        float nxpf, nypf;
        float xper, yper;

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            tx = params.y;
            ty = params.x;
            xper = (float) tx / screenWidth;
            yper = (float) ty / screenHeight;
            nxpf = xper * screenHeight;
            nypf = yper * screenWidth;
            nxp = (int) nxpf;
            nyp = (int) nypf;
            params.x = nyp;
            params.y = nxp;
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            tx = params.x;
            ty = params.y;
            xper = (float) tx / screenHeight;
            yper = (float) ty / screenWidth;
            nxpf = xper * screenWidth;
            nypf = yper * screenHeight;
            nxp = (int) nxpf;
            nyp = (int) nypf;
            params.x = nxp;
            params.y = nyp;
        }
        mWindowManager.updateViewLayout(mFloatingView, params);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }



    private WindowManager.LayoutParams show(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_FULLSCREEN
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            | WindowManager.LayoutParams.SCREEN_ORIENTATION_CHANGED,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.START | Gravity.TOP;
            params.x = 0;
            params.y = 100;
            return params;


        } else {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_FULLSCREEN
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            | WindowManager.LayoutParams.SCREEN_ORIENTATION_CHANGED,
                    PixelFormat.TRANSLUCENT);


            params.gravity = Gravity.START | Gravity.TOP;
            params.x = 0;
            params.y = 100;
            return params;
        }
    }





    private void goFull(){
        int currP = mWM.getCurrentPosition();
        Intent intent = new Intent(FloatingViewService.this, WordsLister.class);
        intent.putExtra("list", mWM);
        intent.putExtra("position",currP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        stopSelf();
    }



    private void diviningString(String input){
        String[] parts = input.split("\\|");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        spanishTV.setText(part1);
        englishTV.setText(part2);
        spanishExp.setText(part1);
        englishExp.setText(part2);
    }

    private void killService(){
        stopSelf();
    }

    private void playSwitch(){
        if(running == false) {
            running = true;
            playButton.setImageResource(R.drawable.ic_pause);
            //String lineS = (String) mWM.getRandomLine();
            //diviningString(lineS);
            timerHandler.removeCallbacks(timerRunnable);
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);

        }
        else{
            running = false;
            timerHandler.removeCallbacks(timerRunnable);
            playButton.setImageResource(R.drawable.ic_play);
        }
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");
            switch (action) {
                case "expand":
                    goFull();
                    killService();
                    break;
                case "play":
                    playSwitch();
                    break;
                case "kill":
                    killService();
                    break;
                case "audio":
                    break;
                default:
                    break;
            }
        }
    };

}