package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.final_project.entity.Values;

import static com.example.final_project.MainActivity.musicBackgroundService;

public class SettingActivity extends AppCompatActivity {
    Spinner spinner;
    String[] sizeBoard;
    int buttonEffect = R.raw.choose_sound;
    EditText textSizeHP;

    // Sound cho bạn cần nài : https://stackoverflow.com/questions/9008770/media-player-called-in-state-0-error-38-0
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.board_size_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        textSizeHP = findViewById(R.id.editTextNumberDecimal);
        sizeBoard = getResources().getStringArray(R.array.board_size_array);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Values.board_size = Integer.parseInt(sizeBoard[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicBackgroundService != null) {
            musicBackgroundService.resumeMusic();
        }
    }

    public void saveOnClick(View view) {
        MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
        mPlayer.start();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void backOnClick(View view) {
        MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
        mPlayer.start();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void save(){
        Values.HP = Integer.parseInt(textSizeHP.getText().toString());
    }
}