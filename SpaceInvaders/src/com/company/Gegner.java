package com.company;

import java.awt.*;

abstract class Gegner extends Graphics {

    int bewegung;
    int leben;
    int schaden;
    int x;
    int y;
    int width;
    int height;
    int projektilgeschw;
    Color color;

    public Gegner() {

    }

    public void paintComponent(Graphics g) {
       
    }
    public int getLeben() {
        return leben;
    }

    public void setLeben(int leben) {
        this.leben = leben;
    }

    public int getSchaden() {
        return schaden;
    }

    public void setSchaden(int schaden) {
        this.schaden = schaden;
    }

    public int getBewegung() {
        return bewegung;
    }

    public void setBewegung(int bewegung) {
        this.bewegung = bewegung;
    }

    public int getWidth() {return width;}

    public void setWidth(int width) {this.width = width;}

    public int getHeight() { return height; }

    public void setHeight(int height){ this.height = height;}

    public int getProjektilgeschw() {return projektilgeschw;}

    public void setProjektilgeschw(int projektilgeschw){this.projektilgeschw = projektilgeschw;};
}
