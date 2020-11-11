package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.final_project.controller.Controller;
import com.example.final_project.entity.Node;
import com.example.final_project.entity.Player;
import com.example.final_project.entity.Values;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocalGameActivity extends AppCompatActivity {

    Node[][] board;
    Controller controller;

    TableLayout layout;
    ImageView avatar1, avatar2, currentChess;
    ProgressBar HP1, HP2;
    TextView name1, name2, timer1, timer2;
    int defaultColor = Values.black_chess;
    int hpLost = 0;
    int buttonEffect = R.raw.choose_sound;
    int playSound = R.raw.play_sound;

    static int count = 1;
    Player player1, player2;

    int p = 5, s = 5;
    int time = p * 60 + s;
    Handler handler;
    AtomicBoolean isBlackTimeRunning = new AtomicBoolean(false),
            isWhiteTimeRunning = new AtomicBoolean(false);

    String timeBlack, timeWhite;
    Thread thb;
    Thread thw;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = time * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_local_game);
        setting();
        Init();
    }

    void setting() {
        board = new Node[Values.board_size][Values.board_size];
        controller = new Controller(board);
        Bundle bundle = getIntent().getExtras();
        player1 = (Player) bundle.get("player1");
        player2 = (Player) bundle.get("player2");

        layout = findViewById(R.id.table);
        currentChess = findViewById(R.id.curentChess);
        avatar1 = findViewById(R.id.avatar1);
        avatar2 = findViewById(R.id.avatar2);
        HP1 = findViewById(R.id.HP1);
        HP2 = findViewById(R.id.HP2);
        name1 = findViewById(R.id.playerName1);
        name2 = findViewById(R.id.playerName2);
        timer1 = findViewById(R.id.timer1);
        timer2 = findViewById(R.id.timer2);


        intent = new Intent(this, Winner.class);
        HP1.setProgress(100);
        HP2.setProgress(100);
        name1.setText(player1.getName());
        name2.setText(player2.getName());
        avatar1.setImageResource(player1.getImgId());
        avatar2.setImageResource(player2.getImgId());
        currentChess.setImageResource(defaultColor);
    }

    void Init() {
        int scale_button = Values.board_width / Values.board_size;
        for (int i = 0; i < board.length; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < board[0].length; j++) {
                final int x = i;
                final int y = j;
                final ImageButton button = new ImageButton(this);
                button.setImageResource(Values.chess_background_img);
                TableRow.LayoutParams layout = new TableRow.LayoutParams(scale_button, scale_button);
                button.setLayoutParams(layout);
                final Node node = new Node(i, j, button, Values.valueEmpty, false);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (node.getValue() == Values.valueEmpty || node.getValue() == 0 - defaultColor) {
                            MediaPlayer mPlayer = MediaPlayer.create(getApplication(), playSound);
                            mPlayer.start();
                            button.setImageResource(defaultColor);
                            node.setValues(defaultColor);
                            hpLost = controller.execute(x, y);
                            if (hpLost != 0) {
                                subHP();
                            }
                            defaultColor = defaultColor == Values.black_chess ? Values.white_chess : Values.black_chess;
                            currentChess.setImageResource(defaultColor);
                        }
                    }
                });
                board[i][j] = node;
                row.addView(button);
            }
            layout.addView(row);
        }
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer1.setText(timeLeftFormatted);
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();
        mTimerRunning = true;
    }


    void changeSizeHP(ProgressBar pb) {
        pb.setProgress(pb.getProgress() - hpLost * 2);
    }


    Intent intent;
    static final int REQUEST_CODE_WIN = 729;

    void subHP() {
        boolean is_win = false;
        if (defaultColor == Values.black_chess) {
            changeSizeHP(HP2);
            if (HP2.getProgress() <= 0) {
                intent.putExtra("winner", player1);
                intent.putExtra("loser", player2);
                is_win = true;
            }
        } else if (defaultColor == Values.white_chess) {
            changeSizeHP(HP1);
            if (HP1.getProgress() <= 0) {
                intent.putExtra("winner", player2);
                intent.putExtra("loser", player1);
                is_win = true;
            }
        }
        if (is_win) {
            startActivityForResult(intent, REQUEST_CODE_WIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_WIN) {
                String mess = data.getStringExtra("option");
                if (mess.equals("newgame")) {
                    setNewGame();
                } else if (mess.equals("quit")) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    public void newGameOnClick(View view) {
        MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
        mPlayer.start();

        setNewGame();
    }

    void setNewGame(){
        for (int i = 0; i < Values.board_size; i++) {
            for (int j = 0; j < Values.board_size; j++) {
                board[i][j].getButton().setImageResource(Values.chess_background_img);
                board[i][j].setValues(Values.valueEmpty);
            }
        }
        HP1.setProgress(100);
        HP2.setProgress(100);
        if(count%10==0){
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.dungnghien);
            mp.start();
        }
        count++;
    }

    public void quitGameOnc(View view) {
        MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
        mPlayer.start();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}