/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photoconversion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author User
 */
public class PhotoConversion {

    /**
     * @param args the command line arguments
     */
    private String loc;
    // public final int[] netLayerSizes;
    
    public PhotoConversion(String location){
        loc = location;
    }
    String getLoc(){
        return loc;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        // TODO code application logic here
        int width = 190;
        int height = 75;
        int[] photo = new int[(width*height)];
        String fileName = "D:\\Karlis\\Documents\\Skola\\Extended Essay\\Luse real\\Luse-real-32.jpg";
        
        BufferedImage img = ImageIO.read(new File(fileName));
        int count = 0;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
               int p = img.getRGB(i, j);
               int r = (p >> 16) & 0xff;
               int g = (p >> 8) & 0xff;
               int b = p & 0xff;
               int digit;
               if(r + g + b > 650) digit = 1;
               else digit = 0;
               photo[i * height + j] = digit;
               System.out.print(digit);
               count++;
            }
            System.out.println("");
        }
    }
    
}
