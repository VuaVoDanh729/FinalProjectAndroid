package com.example.final_project.controller;

import com.example.final_project.entity.Node;
import com.example.final_project.entity.Values;

import java.util.ArrayList;

public class Controller {

    Node[][] table;

    ArrayList<Node> blockList;
    public Controller(Node[][] table) {
        this.table = table;
    }

    public int execute(int x, int y){
        Node node = table[x][y];
        ArrayList<Node> listNode = allOppositeNode(node);
        for (int i = 0; i < listNode.size(); i++) {
            ArrayList<Node> tempList = new ArrayList<>();
            getGraphs(listNode.get(i), tempList);
            boolean isDeath = checkGraphsDeath(tempList);
            if (isDeath) {
                changeValue(tempList, 0 - node.getValue());
                if(listNode.size() == 3 && tempList.size() ==1){
                    blockList = tempList;
                    blockList.get(0).setValues(Values.death_value);
                }
                return tempList.size();
            }
        }
        ArrayList<Node> selfKill = new ArrayList<>();
        getGraphs(node, selfKill);
        boolean isSelfKill = checkGraphsDeath(selfKill);
        if(isSelfKill){
            int value = node.getValue() == Values.black_chess?Values.white_chess:Values.black_chess;
            changeValue(selfKill, value);
            return 0- selfKill.size();
        }

        return 0;

    }

    // Lấy các Node xung quanh và có giá trị đảo nghịch
    ArrayList<Node> allOppositeNode(Node node) {
        ArrayList<Node> result = new ArrayList<>();
        ArrayList<Node> list = allNodeAround(node);
        int value = node.getValue();
        for (int i = 0; i < list.size(); i++) {
            Node thisNode = list.get(i);
            if (thisNode.getValue()!= Values.valueEmpty && thisNode.getValue() != value) {
                result.add(thisNode);
            }
        }
        return result;
    }

    //Lấy 4 Node xung quanh 1 Node
    ArrayList<Node> allNodeAround(Node node) {
        ArrayList<Node> nodes = new ArrayList<>();
        int x = node.getX();
        int y = node.getY();
        if (x - 1 >= 0) {
            nodes.add(table[x - 1][y]);
        }
        if (x + 1 <= table.length - 1) {
            nodes.add(table[x + 1][y]);
        }
        if (y - 1 >= 0) {
            nodes.add(table[x][y - 1]);
        }
        if (y + 1 <= table.length - 1) {
            nodes.add(table[x][y + 1]);
        }
        return nodes;
    }

    /*
     * Check xem một chuỗi liền kề nhau có tiếp xúc với ô có giá trị 0 hay không, nếu có, Graphs được coi là sống
     * Nếu không có Node nào tiếp xúc với 0, Graphs chết.
     * */
    boolean checkGraphsDeath(ArrayList<Node> graphs) {
        for (int i = 0; i < graphs.size(); i++) {
            ArrayList<Node> aroundNode = allNodeAround(graphs.get(i));
            for (int j = 0; j < aroundNode.size(); j++) {
                if (aroundNode.get(j).getValue() == Values.valueEmpty) {
                    return false;
                }
            }
        }
        return true;
    }

    //   Lấy ra 1 chuỗi các Node gần nhau
    void getGraphs(Node node, ArrayList<Node> result) {
        result.add(node);
        node.setVisited(true);
        ArrayList<Node> arround = allNodeAround(node);
        for (int i = 0; i < arround.size(); i++) {
            Node thisNode = arround.get(i);
            if (!thisNode.isVisited() && thisNode.getValue() == node.getValue()) {
                getGraphs(thisNode, result);
            }
        }
        node.setVisited(false);
    }

    // Đổi giá trị
    void changeValue(ArrayList<Node> graphs, int value) {
        for (int i = 0; i < graphs.size(); i++) {
            graphs.get(i).getButton().setImageResource(Values.chess_background_img);
            graphs.get(i).setValues(Values.valueEmpty);
        }
    }


}

