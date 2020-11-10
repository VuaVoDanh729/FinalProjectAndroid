package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.final_project.entity.Player;

public class Winner extends AppCompatActivity {

    ImageView avatarWin, avatarLose;
    TextView winner, loser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_winner);
        getSupportActionBar().hide();
        init();

    }

    void init(){
        avatarWin = findViewById(R.id.winner_avatar);
        avatarLose = findViewById(R.id.loser_Avatar);
        winner = findViewById(R.id.winner_name);
        loser = findViewById(R.id.loser_name);

        Bundle bundle = getIntent().getExtras();
        Player winnerPlayer = (Player) bundle.get("winner");
        Player loserPlayer = (Player) bundle.get("loser");
        avatarWin.setImageResource(winnerPlayer.getImgId());
        winner.setText(winnerPlayer.getName());
        avatarLose.setImageResource(loserPlayer.getImgId());
        loser.setText(loserPlayer.getName());
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.iwonderhouse);
        mp.start();
    }

    public void quitOnC(View view) {
        Intent intent = new Intent();
        intent.putExtra("option", "quit");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void newGameOnC(View view) {
        Intent intent = new Intent();
        intent.putExtra("option", "newgame");
        setResult(RESULT_OK, intent);
        finish();
    }
}