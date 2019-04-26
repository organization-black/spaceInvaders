package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite {

    private static BufferedImage sprite;
    private static final int TILE_SIZE=32;


    public static BufferedImage loadSprite(String file) {

        BufferedImage sprite = null;
        try{
            sprite = ImageIO.read(new File("res/" + file +".gif"));
        } catch(IOException e){
            e.printStackTrace();
        }
        return sprite;
    }
    public static BufferedImage getSprite(int xGrid, int yGrid) {
      if(sprite == null) {
          sprite = loadSprite("frame-1.gif");
      }
       return sprite.getSubimage(xGrid * TILE_SIZE, yGrid*TILE_SIZE,TILE_SIZE,TILE_SIZE);
    }








}
