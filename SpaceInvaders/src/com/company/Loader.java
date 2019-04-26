package com.company;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Loader{


    public  void loader(){
        URL url = this.getClass().getResource("res/explo.gif");
        Icon icon = new ImageIcon(url);
        JLabel label = new JLabel(icon);
}
}
