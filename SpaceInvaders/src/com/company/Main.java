package com.company;

import javax.swing.*;
import java.awt.*;


public class Main {

    public static void main(String[] args) {
        JFrame frm = new JFrame();
        frm.setSize(1280, 720);
        frm.setTitle("Space Invaders");
        frm.setDefaultCloseOperation(3);
        frm.setResizable(false);
        frm.setIgnoreRepaint(true);
        Spielfeld sf = new Spielfeld();

        frm.add(sf);

        frm.setVisible(true);
    }
}
