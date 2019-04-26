package com.company;

import java.util.ArrayList;
import java.util.List;

public class Items {

    List<Geschoss> item = new ArrayList<>();


    public Items() {
        for (int i = 0; i < 5; i++) {
            item.add(Spielfeld.explosivGeschossItem);
        }
        for(int j=0; j<2; j++) {
            item.add(Spielfeld.lahmesGeschossItem);
        }
        for(int w=0; w<100; w++) {
            item.add(Spielfeld.standardGeschossItem);
        }
        for(int p=0; p<2; p++){
            item.add(Spielfeld.portalGeschossItem);
        }
        item.add(Spielfeld.laserGeschossItem);
    }


}
