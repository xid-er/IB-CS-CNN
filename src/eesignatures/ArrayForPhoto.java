/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eesignatures;

/**
 *
 * @author User
 */
public class ArrayForPhoto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String path = "D:\\Karlis\\Documents\\Skola\\Extended Essay";
        EESignatures testPhoto = new EESignatures(path + "\\Real signatures\\sig-1.jpg");
        try{
            testPhoto.photoFromFile(testPhoto.getLoc());
            for(int i = 0; i < testPhoto.photo.length; i++){
                if(i%100 == 0) System.out.println("");
                System.out.print(testPhoto.photo[i]);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
}
