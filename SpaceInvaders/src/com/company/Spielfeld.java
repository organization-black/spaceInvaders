package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Spielfeld extends JPanel implements KeyListener {

    Spieler s;
    ArrayList<Geschoss> geschossArrayList;
    int geschossCounter;
    Robot robot;
    ArrayList<Timer> t;
    boolean timerStopped;
    int count;
    int counterCount;
    int getT;
    boolean durch;
    int anotherCount;
    boolean activateLaser;
    boolean einMal;
    Items items;
    int cases;
    boolean[] downKeys;


    ArrayList<Gegner> gegnerArrayList;


    Spielfeld() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.setBackground(Color.blue);
        s = new Spieler(0, 250, 200, 180);
        geschossArrayList = new ArrayList<>();
        geschossCounter = 0;
        gegnerArrayList = new ArrayList<>();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        timerStopped = true;
        t = new ArrayList<>();
        count = -1;
        activateLaser = true;
        generateGegner();
        items = new Items();
        scoreBoard();
        cases = 1;
        downKeys = new boolean[5];
        runner.start();
        runner2.start();
    }

    private Thread runner = new Thread() {
        {
            setPriority(MIN_PRIORITY);
        }

        public void run() {

            while (true) {

                if(downKeys[0]) {
                    s.y -= 1;
                }
                if(downKeys[1]) {
                    s.y += 1;
                }

                if (s.y <= -s.height / 4) {
                    s.y = -s.height / 4;
                }
                if (s.y >= Spielfeld.super.getHeight() - s.height) {
                    s.y = (Spielfeld.super.getHeight() - s.height);
                }
                repaint();

                try {
                    sleep(2L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private Thread runner2 = new Thread() {
        {
            setPriority(MIN_PRIORITY);
        }

        public void run() {

            while (true) {

                if(downKeys[2]) {
                    boolean isOk = true;
                    switch (cases) {
                        case 1:
                            if (items.item.contains(standardGeschossItem)) {
                                geschossArrayList.add(new StandardGeschoss());
                                isOk = true;
                            } else {
                                isOk = false;
                            }
                            break;
                        case 2:
                            if (items.item.contains(explosivGeschossItem)) {
                                geschossArrayList.add(new ExplosivGeschoss());
                                isOk = true;
                            } else {
                                isOk = false;
                            }
                            break;
                        case 3:
                            if(activateLaser) {
                                if (items.item.contains(laserGeschossItem)) {
                                    geschossArrayList.add(new LaserGeschoss());
                                    isOk = true;
                                } else {
                                    isOk = false;
                                }
                            }
                            break;
                        case 4:
                            if (items.item.contains(lahmesGeschossItem)) {
                                geschossArrayList.add(new LahmesGeschoss());
                                isOk = true;
                            } else {
                                isOk = false;
                            }
                            break;
                        case 5:
                            if (items.item.contains(portalGeschossItem)) {
                                geschossArrayList.add(new PortalGeschoss());
                                isOk = true;
                            } else {
                                isOk = false;
                            }
                            break;
                    }
                    if (!items.item.isEmpty() && isOk) {
                        if (cases == 1 && geschossArrayList.get(geschossCounter) instanceof StandardGeschoss) {
                            items.item.remove(items.item.get(items.item.indexOf(standardGeschossItem)));
                            doStandard(geschossArrayList.get(geschossCounter));
                            geschossCounter++;

                        } else if (cases == 2 && geschossArrayList.get(geschossCounter) instanceof ExplosivGeschoss) {
                            items.item.remove(items.item.get(items.item.indexOf(explosivGeschossItem)));
                            doExplosion(geschossArrayList.get(geschossCounter));
                            geschossCounter++;

                        } else if (cases == 3 && activateLaser && geschossArrayList.get(geschossCounter) instanceof LaserGeschoss) {
                            items.item.remove(items.item.get(items.item.indexOf(laserGeschossItem)));
                            doLaser((LaserGeschoss) geschossArrayList.get(geschossCounter));
                            geschossCounter++;

                        } else if (cases == 4 && geschossArrayList.get(geschossCounter) instanceof LahmesGeschoss) {
                            items.item.remove(items.item.get(items.item.indexOf(lahmesGeschossItem)));
                            doLahmes(geschossArrayList.get(geschossCounter));
                            geschossCounter++;

                        } else if (cases == 5 && geschossArrayList.get(geschossCounter) instanceof PortalGeschoss) {
                            items.item.remove(items.item.get(items.item.indexOf(portalGeschossItem)));
                            doPortal(geschossArrayList.get(geschossCounter));
                            geschossCounter++;
                        }
                    }
                }

                repaint();

                try {
                    sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    JLabel standardGeschoss;
    JLabel explosivGeschoss;
    JLabel laserGeschoss;
    JLabel lahmesGeschoss;
    JLabel portalGeschoss;
    JLabel punkte;


    int punktestand=0;
    public void scoreBoard() {
        JPanel t = new JPanel();
        t.setLayout(new GridBagLayout());
        t.setBackground(Color.blue);
        t.setBorder(BorderFactory.createLineBorder(Color.red));
        GridBagConstraints c = new GridBagConstraints();
        this.add(t);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        standardGeschoss = new JLabel("Standard: " + getGeschossCount(0));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        t.add(standardGeschoss,c);
        explosivGeschoss = new JLabel( " Explosiv: " + getGeschossCount(1));
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        t.add(explosivGeschoss,c);
        laserGeschoss = new JLabel(" Laser: " + getGeschossCount(2));
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        t.add(laserGeschoss,c);
        lahmesGeschoss = new JLabel(" Lahmes: " + getGeschossCount(3));
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        t.add(lahmesGeschoss,c);
        portalGeschoss = new JLabel(" Portal: " + getGeschossCount(4));
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;
        t.add(portalGeschoss,c);
        punkte = new JLabel("Punkte: " + punktestand);
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        t.add(punkte,c);
        standardGeschoss.setForeground(Color.green);
        explosivGeschoss.setForeground(Color.black);
        laserGeschoss.setForeground(Color.black);
        lahmesGeschoss.setForeground(Color.black);
        portalGeschoss.setForeground(Color.black);
        punkte.setForeground(Color.yellow);
    }



    public void refreshBoard() {
        standardGeschoss.setText("Standard: " + getGeschossCount(0));
        explosivGeschoss.setText(" Explosiv: " + getGeschossCount(1));
        laserGeschoss.setText(" Laser: " + getGeschossCount(2));
        lahmesGeschoss.setText(" Lahmes: " + getGeschossCount(3));
        portalGeschoss.setText(" Portal: " + getGeschossCount(4));
        punkte.setText("Punkte: " + punktestand);
        repaint();
    }

    static StandardGeschoss standardGeschossItem = new StandardGeschoss();
    static ExplosivGeschoss explosivGeschossItem = new ExplosivGeschoss();
    static LahmesGeschoss lahmesGeschossItem = new LahmesGeschoss();
    static LaserGeschoss laserGeschossItem = new LaserGeschoss();
    static PortalGeschoss portalGeschossItem = new PortalGeschoss();
    static Random rnd1 = new Random();

    public void checkGegner(int i) {
        System.out.println(gegnerArrayList.get(i).getClass().toString());
        if(gegnerArrayList.get(i) instanceof MinionGegner) {
            for(int j = 0; j < 5; j++) {
                items.item.add(standardGeschossItem);
            }
            for (int j=0; j<rnd1.nextInt(10)+1; j++) {
                if(j == 5) {
                    items.item.add(portalGeschossItem);
                }
            }
            for(int j = 0; j<1; j++) {
                items.item.add(explosivGeschossItem);

            }
        } else if (gegnerArrayList.get(i) instanceof MittelGegner) {
           for(int j = 0; j<rnd1.nextInt(5)+1;  j++) {
               if(j == 7){
                    items.item.add(lahmesGeschossItem);
                    items.item.add(lahmesGeschossItem);
                    items.item.add(lahmesGeschossItem);
               }
           }
            for(int j = 0; j < 10; j++) {
                items.item.add(standardGeschossItem);
            }
            for(int j = 0; j<3; j++) {
                items.item.add(explosivGeschossItem);
            }
        } else if(gegnerArrayList.get(i) instanceof BossGegner) {
            for(int j=0; j<rnd1.nextInt(2)+1; j++) {
                if(j==1){
                    items.item.add(laserGeschossItem);
                }
            }
            for(int j=0; j<50; j++) {
                items.item.add(standardGeschossItem);
            }
            for (int j=0; j<5; j++) {
                items.item.add(explosivGeschossItem);
            }
            for (int j=0; j<5; j++){
                items.item.add(lahmesGeschossItem);
            }
        } else if(gegnerArrayList.get(i) instanceof SpiegelGegner) {
            items.item.add(explosivGeschossItem);
            items.item.add(explosivGeschossItem);
            items.item.add(explosivGeschossItem);

            for(int j=0; j<10; j++) {
                items.item.add(standardGeschossItem);
            }
        }
        punktestand += gegnerArrayList.get(i).punkte;
        refreshBoard();
    }

    public int getGeschossCount(int number) {
        int geschosse = 0;

        for(int i = 0; i < items.item.size(); i++) {
            if(items.item.get(i) instanceof StandardGeschoss && number == 0) {
                geschosse++;
            } else if(items.item.get(i) instanceof ExplosivGeschoss && number == 1) {
                geschosse++;
            } else if(items.item.get(i) instanceof LaserGeschoss && number == 2) {
                geschosse++;
            } else if(items.item.get(i) instanceof LahmesGeschoss && number == 3) {
                geschosse++;
            } else if(items.item.get(i) instanceof PortalGeschoss && number == 4) {
                geschosse++;
            }
        }

        return geschosse;
    }

    public void paintComponent(Graphics g) {
        refreshBoard();
        super.paintComponent(g);
        for (Gegner h : gegnerArrayList) {
            h.paintComponent(g);
        }
        for (Geschoss h : geschossArrayList) {
            h.paintComponent(g);
        }
        s.paintComponent(g);
    }

    int newCounter = -1;

    void generateGegner() {
        Timer time = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fixLag();
                Random rnd = new Random();
                int rndG = rnd.nextInt(50) + 1;
                    if (rndG >= 1 && rndG <= 30) {
                        gegnerArrayList.add(new MinionGegner());
                        newCounter++;
                    } else if (rndG == 32 || rndG == 31) {
                        gegnerArrayList.add(new MittelGegner());
                        newCounter++;
                    } else if (rndG == 40) {
                        gegnerArrayList.add(new BossGegner());
                        newCounter++;
                    } else if (rndG == 35 || rndG == 36 || rndG == 37) {
                        gegnerArrayList.add(new SpiegelGegner());
                        newCounter++;
                    }
                if (!gegnerArrayList.isEmpty()) {
                    gegnerArrayList.get(newCounter).x = Spielfeld.super.getWidth();
                    gegnerArrayList.get(newCounter).y = rnd.nextInt(Spielfeld.super.getHeight()-200)+50;
                    for (Gegner a : gegnerArrayList) {
                        a.x -= 10;
                    }
                }
                repaint();
            }
        });
        time.start();
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        doKeyActions(key, true);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        doKeyActions(key, false);
        switch(key) {
            case KeyEvent.VK_LEFT:
                if(cases>1) {
                    cases--;
                    casers();
                } else {
                    cases = 5;
                    casers();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(cases<5) {
                    cases++;
                    casers();
                } else {
                    cases = 1;
                    casers();
                }
                break;
        }
    }

    public void doKeyActions(int code, boolean isDown) {

        switch (code) {
            case KeyEvent.VK_W:
                downKeys[0] = isDown;
                break;
            case KeyEvent.VK_S:
                downKeys[1] = isDown;
                break;

            case KeyEvent.VK_SPACE:
                downKeys[2] = isDown;
                break;
        }

    }

    void doStandard(Geschoss sg) {
        sg.y = s.y + (s.height / 2 + 19);
        sg.x = s.width;
        doAnything(sg);
    }
    void doExplosion(Geschoss sg) {
        sg.y = s.y + (s.height / 2 + 11);
        sg.x = s.width;
        doAnything(sg);
    }
    void doLahmes(Geschoss sg) {
        sg.y = s.y + (s.height / 2);
        sg.x = s.width;
        doAnything(sg);
    }
    void doPortal(Geschoss sg) {
        sg.y = s.y + (s.height / 2 - 25);
        sg.x = s.width;
        doAnything(sg);
    }

    public void casers() {
        switch (cases) {
            case 1:
                standardGeschoss.setForeground(Color.green);
                explosivGeschoss.setForeground(Color.black);
                laserGeschoss.setForeground(Color.black);
                lahmesGeschoss.setForeground(Color.black);
                portalGeschoss.setForeground(Color.black);
                break;
            case 2:
                standardGeschoss.setForeground(Color.black);
                explosivGeschoss.setForeground(Color.green);
                laserGeschoss.setForeground(Color.black);
                lahmesGeschoss.setForeground(Color.black);
                portalGeschoss.setForeground(Color.black);
                break;
            case 3:
                standardGeschoss.setForeground(Color.black);
                explosivGeschoss.setForeground(Color.black);
                laserGeschoss.setForeground(Color.green);
                lahmesGeschoss.setForeground(Color.black);
                portalGeschoss.setForeground(Color.black);
                break;
            case 4:
                standardGeschoss.setForeground(Color.black);
                explosivGeschoss.setForeground(Color.black);
                laserGeschoss.setForeground(Color.black);
                lahmesGeschoss.setForeground(Color.green);
                portalGeschoss.setForeground(Color.black);
                break;
            case 5:
                standardGeschoss.setForeground(Color.black);
                explosivGeschoss.setForeground(Color.black);
                laserGeschoss.setForeground(Color.black);
                lahmesGeschoss.setForeground(Color.black);
                portalGeschoss.setForeground(Color.green);
                break;
        }
        repaint();
    }

    Timer timer;

    public void doAnything(Geschoss sg) {


        count++;
        int savenumber = count;
        timer = new Timer(125, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fixLag();
                for (int i = 0; i < newCounter; i++) {
                    if (sg.x < gegnerArrayList.get(i).x + gegnerArrayList.get(i).width && sg.x + sg.width > gegnerArrayList.get(i).x && sg.y < gegnerArrayList.get(i).y + gegnerArrayList.get(i).height && sg.y + sg.height > gegnerArrayList.get(i).y) {
                        gegnerArrayList.get(i).leben = gegnerArrayList.get(i).leben - sg.dmg;
                        sg.dmg = 0;
                        if(geschossArrayList.contains(sg)) {
                            geschossArrayList.remove(sg);
                            geschossCounter--;
                            if (gegnerArrayList.get(i).leben <= 0) {
                                checkGegner(i);
                                gegnerArrayList.remove(gegnerArrayList.get(i));
                                newCounter--;
                            }
                        }
                        stopTimer(t.get(savenumber));
                    }
                }
                if(sg.x >= Spielfeld.super.getWidth()) {
                    if(geschossArrayList.contains(sg)) {
                        geschossArrayList.remove(sg);
                        geschossCounter--;
                    }
                    stopTimer(t.get(savenumber));
                }
                sg.x += 10;
                repaint();

            }
        });
        t.add(timer);
        t.get(count).start();
    }


    public void doLaser(LaserGeschoss lg) {
        durch = false;
        anotherCount = 0;
        lg.y = s.y + (s.height / 2 + 19);
        lg.x = s.width;
        counterCount = 0;

        if (activateLaser) {

            t.add(new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fixLag();
                    activateLaser = false;
                    lg.width += 10;
                    lg.y = s.y + (s.height / 2 + 19);
                    int max = (Spielfeld.super.getWidth() / 10) + 10;
                    if (counterCount >= max && !durch) {
                        durch = true;
                    } else if (durch) {
                        anotherCount++;
                        if (anotherCount >= 5000 / 20) {
                            lg.color = new Color(0, 0, 0, 0);
                            stopTimer(t.get(getT));
                            activateLaser = true;
                        }
                    }
                    for(int i = 0; i < gegnerArrayList.size(); i++) {

                        if(lg.x < gegnerArrayList.get(i).x + gegnerArrayList.get(i).width && lg.x + lg.width > gegnerArrayList.get(i).x && lg.y < gegnerArrayList.get(i).y + gegnerArrayList.get(i).height && lg.y + lg.height > gegnerArrayList.get(i).y) {
                            gegnerArrayList.get(i).leben = gegnerArrayList.get(i).leben - lg.dmg;
                            if (gegnerArrayList.get(i).leben <= 0) {
                                checkGegner(i);
                                gegnerArrayList.remove(gegnerArrayList.get(gegnerArrayList.indexOf(gegnerArrayList.get(i))));
                                newCounter--;
                            }
                        }
                    }
                    repaint();
                    counterCount++;

                }
            }));
            count++;
            t.get(count).start();
            getT = count;
        }
    }
    public void fixLag() {
        PointerInfo pi = MouseInfo.getPointerInfo();
        Point p = pi.getLocation();
        robot.mouseMove((int) p.getX(), (int) p.getY());
    }

    public void stopTimer(Timer timer) {
        timer.stop();
        timerStopped = false;
    }

}
