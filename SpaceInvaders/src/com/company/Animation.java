package com.company;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {

    private BufferedImage[] explosions = {Sprite.getSprite(0,1), Sprite.getSprite(2,1)};
    private Animation explosion = new Animation(explosions, 10);

    private int frameCount;
    private int frameDelay;
    private int currentFrame;
    private int animationDirection;
    private int totalFrames;


    private boolean stopped;

    private List<Frame> frm =  new ArrayList<Frame>();


    public Animation(BufferedImage[] frm, int frameDelay) {
        this.frameDelay = frameDelay;
        this.stopped = true;

        for(int i=0; i<frm.length; i++){
            addFrame(frm[i], frameDelay);
        }
        this.frameCount=0;
        this.frameDelay=frameDelay;
        this.currentFrame =0;
        this.animationDirection=1;
        this.totalFrames=this.frm.size();
    }
    public void start() {
        if(!stopped) {
            return;
        }
        stopped=false;
    }
    public void stop() {
        if(frm.size() == 0){
            return;
        }
        stopped=false;
        currentFrame=0;
    }
    public void restart() {
        if(frm.size() == 0){
            return;
        }
    }
    public void reset() {
        this.stopped=true;
        this.frameCount = 0;
        this.currentFrame=0;
    }

    private void addFrame(BufferedImage frame, int duration) {
        if (duration <= 0) {
            System.err.println("Ungueltige Dauer: " + duration);
            throw new RuntimeException("Ungueltige Dauer:  " + duration);
        }

        frm.add(new Frame(frame, duration));
        currentFrame = 0;
    }
    public BufferedImage getSprite() {
        return frm.get(currentFrame).getFrame();
    }

    public void update() {
        if(!stopped) {
            frameCount++;
            if (frameCount > frameDelay) {
                frameCount=0;
                currentFrame += animationDirection;

                if(currentFrame > totalFrames-1) {
                    currentFrame=0;
                }
                else if(currentFrame < 0) {
                    currentFrame = totalFrames -1;
                }
            }
        }
    }


}
