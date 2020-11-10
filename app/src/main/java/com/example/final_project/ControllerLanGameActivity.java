package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.final_project.controller.ControllerGame;
import com.example.final_project.entity.Message;
import com.example.final_project.entity.Node;
import com.example.final_project.entity.Values;
import com.example.final_project.lan.Client;
import com.example.final_project.lan.Server;

public class ControllerLanGameActivity extends AppCompatActivity {

    private Node[][] board;
    TableLayout table_layout;
    int current_chess;
    ControllerGame controller;
    public static boolean is_turn;
    int MODE;
    Server server;
    Client client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_lan_game);
        getSupportActionBar().hide();
        init();
    }

    void init() {
        board = new Node[Values.board_size][Values.board_size];
        table_layout = findViewById(R.id.board);
        controller = new ControllerGame(board);
        settingBoardGame();
        controller.createLittleMatrix();
        Bundle bundle = getIntent().getExtras();
        MODE = (int) bundle.get("mode");
        if(MODE == Values.HOST){
            current_chess = Values.black_chess;
            is_turn = true;
            server = new Server(board,controller);
            server.start();
        }else if(MODE == Values.GUEST){
            current_chess = Values.white_chess;
            is_turn = false;
            client = new Client(board, controller);
            client.start();
        }else if(MODE == Values.mode_local){
            current_chess = Values.black_chess;
        }

    }

    void initLan(){
        // Viết code config lan game
    }
    private void changeChessAfterRound() {
        current_chess = current_chess == Values.black_chess ? Values.white_chess : Values.black_chess;
    }

    private void settingBoardGame() {
        int scale_button = Values.board_width / Values.board_size - 10;
        for (int i = 0; i < board.length; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < board[0].length; j++) {
                final int x = i;
                final int y = j;
                final ImageButton imageButton = new ImageButton(this);
                imageButton.setImageResource(Values.chess_background_img);
                TableRow.LayoutParams layout = new TableRow.LayoutParams(scale_button, scale_button);
                imageButton.setLayoutParams(layout);
                final Node node = new Node(x, y, imageButton, Values.valueEmpty, false);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(MODE == Values.HOST || MODE == Values.GUEST){
                            handleClickChessLan(node, x, y);
                        }
                    }
                });
                board[i][j] = node;
                row.addView(imageButton);
            }
            table_layout.addView(row);
        }
    }

    void handleClickChessLocal(Node node, int x, int y){
        node.getButton().setImageResource(current_chess);
      //  int HP = controller.execute(x,y);
        changeChessAfterRound();
        // -----------
    }

    void handleClickChessLan(Node node, int x, int y){
     //   if(is_turn){
            node.getButton().setImageResource(current_chess);
            int HP = controller.exec(x,y, current_chess);
            if(MODE == Values.GUEST){
                client.setMessage(new Message("ok",x,y));
            }else{
                server.setMessage(new Message("ok",x,y));
            }
            is_turn = false;
    //    }
    }

}