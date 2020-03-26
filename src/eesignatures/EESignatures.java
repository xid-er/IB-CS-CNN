/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eesignatures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Karlis
 */
public class EESignatures {
    
//    First signature - OG mine
//    int width = 100;
//    int height = 75;
//    Second signature - Luse
    int width = 190;
    int height = 75;
    int[] photo = new int[(width*height)];
    private String loc;
    // public final int[] netLayerSizes;
    
    public EESignatures(String location){
        loc = location;
    }
    String getLoc(){
        return loc;
    }

    /**
     * @param args the command line arguments
     */
    
    public void photoFromFile(String fileName) throws FileNotFoundException, 
                                                      IOException{
        BufferedImage img = ImageIO.read(new File(fileName));
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
               int p = img.getRGB(i, j);
               int r = (p >> 16) & 0xff;
               int g = (p >> 8) & 0xff;
               int b = p & 0xff;
               int digit;
               if(r + g + b < 650) digit = 1;
               else digit = 0;
               photo[i * height + j] = digit;
            }
        }
    }
    
    public double sigmoid(double x){
        return 1d / (1 + Math.pow(Math.E, -x));
    }
    
}
