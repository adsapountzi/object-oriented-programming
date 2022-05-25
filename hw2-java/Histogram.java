/*
Author: Sapountzi Athanasia Despoina 02624
 */
package ce326.hw2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Histogram {//Creates the histogram of a YUV image.
    private int arrayHistogram[],  equalized[];
    private double PMF[], CDF[];
    int totalPixels;
    public static final int MAX_LUMINOCITY = 235;
    //constructor
    public Histogram(YUVImage img){
        arrayHistogram = new int[MAX_LUMINOCITY+1];
        PMF = new double[MAX_LUMINOCITY+1];
        CDF = new double[MAX_LUMINOCITY+1];
        totalPixels = img.getHeight()*img.getWidth();
        equalized = new int[MAX_LUMINOCITY+1];
        //initialize arrays 
        Arrays.fill(arrayHistogram,0);
        Arrays.fill(equalized,0);
        Arrays.fill(PMF,0);
        Arrays.fill(CDF,0);
        
        for(int i = 0; i < img.getHeight(); i++){//calculates the histogram based on the parameter Y which is its measure image brightness.
            for(int j = 0; j < img.getWidth(); j++){
                arrayHistogram[img.pixelArray[i][j].getY()]++;
            }
        }  
    }
    
    //methods       
    public String toString(){ 
        int counterNumberSign = 0, counterDollar = 0, counterAt = 0, counterAsterisks = 0; 
        StringBuilder str = new StringBuilder("\n");
//        String numberSign ="#", dollarSign="$" ;
        for(int i = 0; i < 236; i++){
            str.append(String.format("%3d",i)).append(".(").append(String.format("%4d",arrayHistogram[i])).append(")\t");
           
            counterNumberSign = (arrayHistogram[i]%10000)/1000;//compute 
            counterDollar = (arrayHistogram[i]%1000)/100 ;
            counterAt = (arrayHistogram[i]%100)/10;
            counterAsterisks = (arrayHistogram[i]%10);
            //Unfortunately repeat() doesn't get recognized in autolab
//            str.append(numberSign.repeat(counterNumberSign)).append(dollarSign.repeat(counterDollar)).append("@".repeat(counterAt)).append("*".repeat(counterAsterisks)).append("\n");
   
            str.append(new String(new char[counterNumberSign]).replace("\0", "#")).append(new String(new char[counterDollar]).replace("\0", "$")).append(new String(new char[counterAt]).replace("\0", "@")).append(new String(new char[counterAsterisks]).replace("\0", "*")).append("\n");
                    
        } 
        return str.toString();
    }
        
    public void toFile(File file){//prints the String of the toString () method to a file.
        try {
            FileWriter myWriter = new FileWriter(file, false); //If file already exists, overwrite it
            myWriter.write(toString());
            myWriter.close();
        }
        catch (IOException e) {
            System.out.println("ERROR: in Histogram toFile");
        }
    }    
    
    public void equalize(){//equalizes the histogram.
        int maxLuminocity = 0;
        for(int i = 0; i < (MAX_LUMINOCITY+1); i++){
            if(arrayHistogram[i] > 0){
                maxLuminocity = i;	
            }
        }
        for(int i = 0; i < (MAX_LUMINOCITY+1); i++){ //calculates the probability mass function of brightness of pixels.
            PMF[i] = (double)arrayHistogram[i] / (double)totalPixels;
            for(int j = 0; (j <= i); j++){ //calculates the cumulative distribution function of brightness of pixels.
                CDF[i] += PMF[j];
            }
            equalized[i] = (int)((double)maxLuminocity * CDF[i]); //calculates the final equalized pixels
        }   		
    }
    
    public short getEqualizedLuminocity(int luminocity){
        return (short)equalized[luminocity];
    }
    
}
