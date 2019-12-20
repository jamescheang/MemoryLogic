package com.example.memorylogic;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MemoryGameActivity extends AppCompatActivity /*implements MyAsyncTask.ICallback*/{

    /*    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMilliseconds = 180000;*/
    TextView countDownTimer;
    int clicked = 0;
    boolean faceUp = false;
    int lastClicked = -1;
    int matched = 0;
    //Number of seconds displayed on the stopwatch.
    private int seconds = 0;
    //Is the stopwatch running?
    private boolean running=true;//once the activity starts, the timer will start
    private boolean wasRunning;

    ArrayList<Integer> images=null;
    ImageButton[] buttons=null;
    ArrayList<String> files = null;
    TextView picMatch = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();
        //countDownTimer = (TextView)findViewById(R.id.countDownTimer);


        initUI();

//        for (int i = 0; i < images.size(); i++) {
//            System.out.println("Button: " + (i+1) + ", Tag: " + images.get(i));
//        }

        picMatch = findViewById(R.id.picmatches);

        memoryLogic();



        Button reset = findViewById(R.id.resetBtn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (int i = 0; i < buttons.length; i++) {
//                    buttons[i].setTag("cardBack");
//                    buttons[i].setImageResource(R.drawable.code);
//                    buttons[i].setClickable(true);
//                }
//                faceUp = false;
//                clicked = 0;
//                Collections.shuffle(images);
//                mTimeLeftInMilliseconds = 180000;
//                startTimer();
//                updateCountDownText();

                finish();
                startActivity(getIntent());
            }
        });

    }

    /*protected void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMilliseconds,1000) {
            @Override
            public void onTick(long l) {
                mTimeLeftInMilliseconds = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    void updateCountDownText(){
        int minutes = (int)(mTimeLeftInMilliseconds / 1000) / 60;
        int seconds = (int)(mTimeLeftInMilliseconds / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        countDownTimer.setText(timeLeft);
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }
    //Start the stopwatch running when the resume button is clicked.
    public void onClickStart(View view) {
        running = true;
        if(buttons!=null){
            for(ImageButton button:buttons){
                button.setClickable(true);
            }
        }
    }
    //Stop the stopwatch running when the pause button is clicked.
    public void onClickStop(View view) {
        running = false;
        //need to freeze the users from clicking on the imagebuttons
        if(buttons!=null){
            for(ImageButton button:buttons){
                button.setClickable(false);
            }
        }
        //grey out the pause button
        //enable to the resume button
    }
    //Sets the number of seconds on the timer.
    private void runTimer() {
        final TextView timeView = (TextView)findViewById(R.id.stopwatch);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds/3600;
                int minutes = (seconds%3600)/60;
                int secs = seconds%60;
                String time = String.format("%d:%02d:%02d",
                        hours, minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    protected void initUI(){
        buttons = new ImageButton[]{
                findViewById(R.id.Image1),
                findViewById(R.id.Image2),
                findViewById(R.id.Image3),
                findViewById(R.id.Image4),
                findViewById(R.id.Image5),
                findViewById(R.id.Image6),
                findViewById(R.id.Image7),
                findViewById(R.id.Image8),
                findViewById(R.id.Image9),
                findViewById(R.id.Image10),
                findViewById(R.id.Image11),
                findViewById(R.id.Image12)
        };

//        images = new ArrayList<>();
//        images.add(R.drawable.camel);
//        images.add(R.drawable.fox);
//        images.add(R.drawable.koala);
//        images.add(R.drawable.lion);
//        images.add(R.drawable.monkey);
//        images.add(R.drawable.wolf);
//        images.add(R.drawable.camel);
//        images.add(R.drawable.fox);
//        images.add(R.drawable.koala);
//        images.add(R.drawable.lion);
//        images.add(R.drawable.monkey);
//        images.add(R.drawable.wolf);
//
//        Collections.shuffle(images);

        files = new ArrayList<>();
        files.add("image1.jpg");
        files.add("image2.jpg");
        files.add("image3.jpg");
        files.add("image4.jpg");
        files.add("image5.jpg");
        files.add("image6.jpg");
        files.add("image1.jpg");
        files.add("image2.jpg");
        files.add("image3.jpg");
        files.add("image4.jpg");
        files.add("image5.jpg");
        files.add("image6.jpg");

        Collections.shuffle(files);

    }

    protected void memoryLogic(){
        for (int i=0;i<buttons.length;i++){
            final int finalI = i;
            buttons[i].setTag("cardBack");
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Tag: " + buttons[finalI].getTag());
                    String imageName = (String) buttons[finalI].getTag();
                    if (imageName.equals("cardBack") && !faceUp){
//                        buttons[finalI].setImageResource(images.get(finalI));
                        buttons[finalI].setImageBitmap(BitmapFactory.decodeFile(getFilesDir()+"/"+files.get(finalI)));
//                        buttons[finalI].setTag(images.get(finalI).toString());
                        buttons[finalI].setTag(files.get(finalI));
                        if (clicked == 0) {
                            lastClicked = finalI;
                            System.out.println("Lastclicked tag: " + lastClicked + ", i: " + finalI);
                        }
                        System.out.println("Flipped the card, setting tag to: " + buttons[finalI].getTag().toString());
                        clicked++;
                    }

                    //The second criteria is to ensure only 2 cards are clicked and flipped. Any further clicks on other cards won't mistakenly trigger the handler
                    if (clicked == 2 && !buttons[finalI].getTag().toString().equals("cardBack")){
                        buttons[finalI].setClickable(false);
                        faceUp = true;
                        if (buttons[finalI].getTag().toString().equalsIgnoreCase(buttons[lastClicked].getTag().toString())){
                            buttons[finalI].setClickable(false);
                            buttons[lastClicked].setClickable(false);
                            matched++;
                            faceUp = false;
                            clicked = 0;
                            picMatch.setText(matched + "/6 matches");
                            //if the matches equals 6
                            //onClickPause();
                            if(matched == 6){
                                System.out.println("so smart, you matched 6 pairs in " + seconds + " seconds!");
                                running = false;
                            }
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    faceUp = false;
                                    clicked = 0;
                                    buttons[finalI].setImageResource(R.drawable.code);
                                    buttons[finalI].setTag("cardBack");
                                    buttons[lastClicked].setImageResource(R.drawable.code);
                                    buttons[lastClicked].setTag("cardBack");
                                    buttons[finalI].setClickable(true);
                                    buttons[lastClicked].setClickable(true);
                                }
                            }, 1000);
                        }
                    } else if (clicked == 0){
                        faceUp = false;
                    } else if (clicked == 1){
                        buttons[lastClicked].setClickable(false);
                    }
                }
            });
        }
    }

    //uncomment the async related tasks below and in this's implement class above to download all 6 images
    /*@Override
    protected void onStart() {
        super.onStart();
        String[] urls = { "https://webneel.com/wallpaper/sites/default/files/images/08-2018/3-nature-wallpaper-mountain.jpg",
                "https://s.ftcdn.net/v2013/pics/all/curated/RKyaEDwp8J7JKeZWQPuOVWvkUjGQfpCx_cover_580.jpg?r=1a0fc22192d0c808b8bb2b9bcfbf4a45b1793687",
                "https://images.pexels.com/photos/459225/pexels-photo-459225.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
                "https://images.unsplash.com/photo-1541233349642-6e425fe6190e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80",
                "https://image.shutterstock.com/image-photo/spring-blossom-background-beautiful-nature-260nw-1033292395.jpg",
                "https://image.shutterstock.com/image-photo/large-beautiful-drops-transparent-rain-260nw-668593321.jpg"};

        for ( int i = 0 ; i < 6 ; i++)
        {
            int j = i+1;
            new MyAsyncTask(this).execute(urls[i], getFilesDir() + "/image"+j+".jpg");
        }
    }
    public void onAsyncTaskProgress(int progress) {
//        if (progBar != null)
//            progBar.setProgress(Math.round(progress));
    }

    public void onAsyncTaskDone(Bitmap bitmap) {

//        if (imgView != null)
//            imgView.setImageBitmap(bitmap);
//          imgView2.setImageBitmap(bitmap);



//        if (progBar != null)
//            progBar.setVisibility(View.GONE);
    }*/

}
