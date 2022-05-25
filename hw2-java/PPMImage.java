/*
Author: Sapountzi Athanasia Despoina 
 */
package ce326.hw2;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PPMImage extends RGBImage{
    //constructors
    public PPMImage(java.io.File file) throws UnsupportedFileFormatException { 
        int width, height, colordepth, i, j;
        short red, green, blue;
        try{
            Scanner PPMfile = new Scanner(file);
            //check if the file has the right PPM form
            if(!(PPMfile.next().equals("P3"))){
                throw new UnsupportedFileFormatException("String in first line does not match P3!");
            }
            //check if the width and height values are acceptable.
            if(PPMfile.hasNextInt()){
                width = PPMfile.nextInt();
                if(width <= 0){
                    
                    throw new UnsupportedFileFormatException("Width value is not a positive integer");
                }
//                    System.out.println("WIDTH IS "+width);
            }
            else{
                throw new UnsupportedFileFormatException("Width value is not an acceptable value");
            }
            
            //check if  height value is acceptable.
            if(PPMfile.hasNextInt()){
                height = PPMfile.nextInt();
                if(height <= 0){
                    throw new UnsupportedFileFormatException("Height value is not a positive integer");
                }
//                    System.out.println("HEIGHT IS "+height);
            }
            else{
                throw new UnsupportedFileFormatException("Height value is not an acceptable value");
            }
            if(PPMfile.hasNextInt()){
                colordepth = PPMfile.nextInt();
                if((colordepth <= 0) || (colordepth > MAX_COLORDEPTH)){
                    throw new UnsupportedFileFormatException("Colordepth value is not a positive integer or exceeds max colordepth value of 255.");
                }
//                    System.out.println("colordepth IS "+colordepth);
            }
            else{
                throw new UnsupportedFileFormatException("Colordepth value is not an acceptable value");
            }
            
            super.pixelArray = new RGBPixel[height][width];
             //check the RGB red , green, blue color values. If valid set the color values of the pixelArray.
            for(i = 0; i < height; i++){
                for(j = 0; j < width; j++){
                    if(PPMfile.hasNextInt()){
                         red = (short) PPMfile.nextInt();
                        if(red < 0 || red > MAX_COLORDEPTH){
                            throw new UnsupportedFileFormatException("red value is negative or it exceeds max colordepth value");
                        }
//                        System.out.println("RED  at i:"+i+" j:"+j+" is "+red);
//                        super.pixelArray[i][j].setRed(red);
                    }
                    else{
                        throw new UnsupportedFileFormatException("invalid red");
                    }
                    if(PPMfile.hasNextInt()){
                         green = (short) PPMfile.nextInt();
                        if(green < 0 || green > MAX_COLORDEPTH){
                            throw new UnsupportedFileFormatException("green value is negative or it exceeds max colordepth value");
                        }
//                        super.pixelArray[i][j].setGreen(green);
//                        System.out.println("GREEN  at i:"+i+" j:"+j+" is "+green);
                    }
                    else{
                        throw new UnsupportedFileFormatException("invalid green");
                    }
                    if(PPMfile.hasNextInt()){
                         blue = (short) PPMfile.nextInt();
                        if(blue < 0 || blue > MAX_COLORDEPTH){
                            throw new UnsupportedFileFormatException("blue value is negative or it exceeds max colordepth value");
                        }
//                        super.pixelArray[i][j].setBlue(blue);
//                        System.out.println("BLUE  at i:"+i+" j:"+j+" is "+blue);
                    }
                    else{
                        throw new UnsupportedFileFormatException("invalid blue");
                    }
                    super.pixelArray[i][j] = new RGBPixel(red, green, blue);
                }
            }        
        }
        catch(FileNotFoundException e) { //Exeption activates when file does not exist or file cannot be read!
             System.out.println("File does not exist or  cannot be read"+e.toString());
        }
        
    }
    
    public PPMImage(RGBImage img){
        super(img);
    }
   
    public PPMImage(YUVImage img){
        super(img);
    }


    public String toString(){ //Creates string that includes contents of PPM file.
        StringBuffer str = new StringBuffer(("P3\n" + getWidth() + " " + getHeight() + "\n" + 255 + "\n"));
        for(int i = 0; i < getHeight(); i++){
            for(int j = 0; j < getWidth(); j++){
                str.append(super.pixelArray[i][j].toString());
            }
            str.append("\n");
        }
        return str.toString();
    } 
      
    public void toFile(java.io.File file){
        try {
            FileWriter PPMFile = new FileWriter(file, false); //If file already exists, overwrite it
            PPMFile.write(toString());
            PPMFile.close();
        }
        catch (IOException e) {
            System.out.println("ERROR: toFile");
        }
    }    
}
