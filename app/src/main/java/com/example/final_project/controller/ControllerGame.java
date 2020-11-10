package com.example.final_project.controller;

import com.example.final_project.entity.Node;
import com.example.final_project.entity.Values;

import java.util.ArrayList;

public class ControllerGame {
    Node[][] board;
    int[][] table;
    static boolean is_continue = true;
    static int count = 0;
    ArrayList<Node> listDeath;


    public ControllerGame(Node[][] board) {
        this.board = board;
    }

    public void createLittleMatrix() {
        table = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                table[i][j] = board[i][j].getValue();
            }
        }
    }

    public int exec(int x, int y, int valueChess) {
        System.out.println("Execute : "+x+":"+ y);
        count = 0;
        is_continue = true;
        table[x][y] = valueChess;
        Node node = board[x][y];
        int value = table[x][y];
        int defaultValue = value==Values.valueBlack?Values.valueWhite:Values.valueBlack;
        listDeath = new ArrayList<>();
        findGtaphAndDelete(x,y,defaultValue);
        return count;
    }


    void doSth(int x, int y, int value){
        is_continue = true;
        core(x, y,value);
        count = is_continue == false ? count : 0;
        if (count > 0) {
            changeDeath();
        }
    }
    void core(int x, int y, int value) {
        count++;
        listDeath.add(board[x][y]);
        if (checkaliveNode(x, y)) {
            is_continue = false;
            count = 0;
            return;
        }
        table[x][y] = 0 - value;
        if (x - 1 >= 0 && is_continue) {
            subFunction(x - 1, y, value);
        }
        if (x + 1 <= table.length && is_continue) {
            subFunction(x + 1, y, value);
        }
        if (y - 1 >= 0 && is_continue) {
            subFunction(x, y - 1, value);
        }
        if (y + 1 <= table.length && is_continue) {
            subFunction(x, y + 1, value);
        }
        table[x][y] = 0 - table[x][y];
        is_continue = false;
    }

    void subFunction(int x, int y, int value) {
        if (table[x][y] == value) {
            core(x, y, value);
            count++;
        }
    }

    boolean checkaliveNode(int x, int y) {
        if (x - 1 >= 0) {
            if (table[x - 1][y] == 0) {
                return true;
            }
        }
        if (x + 1 <= table.length) {
            if (table[x + 1][y] == 0) {
                return true;
            }
        }
        if (y - 1 >= 0) {
            if (table[x][y - 1] == 0) {
                return true;
            }
        }
        if (y + 1 <= table.length) {
            if (table[x][y + 1] == 0) {
                return true;
            }
        }
        return false;
    }

    void changeDeath() {
        for (int i = 0; i < listDeath.size(); i++) {
            Node node = listDeath.get(i);
            node.getButton().setImageResource(Values.chess_background_img);
            table[node.getX()][node.getY()] = Values.death_value;
        }
        listDeath.clear();
    }

    void findGtaphAndDelete(int x, int y, int valueNode) {
        if (x - 1 >= 0) {
            if (table[x - 1][y] == valueNode) {
                doSth(x-1, y, valueNode);
            }
        }
        if (x + 1 <= table.length-1) {
            if (table[x + 1][y] == valueNode) {
                doSth(x+1, y, valueNode);
            }
        }
        if (y - 1 >= 0) {
            if (table[x][y - 1] == valueNode) {
                doSth(x, y-1, valueNode);
            }
        }
        if (y + 1 <= table.length-1) {
            if (table[x][y + 1] == valueNode) {
                doSth(x, y+1, valueNode);
            }
        }
    }
}
