/*
Author: Sapountzi Athanasia Despoina 02624
 */
package ce326.hw2;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class YUVImage {
    protected YUVPixel [][] pixelArray;
    
    int height;
    int width;
    
    //constructors
    public YUVImage(int width, int height){
        this.height  =  height;
        this.width = width;
        
        this.pixelArray = new YUVPixel[height][width];
         
        for(int i = 0; i < this.height; i++){//creates an array of YUVPixel pixels.
            for(int j = 0; j  < this.width ; j++){
               pixelArray[i][j] = new YUVPixel((short) 16, (short) 128, (short)128);
            }
        }    
    }
    
    public YUVImage(YUVImage copyImg){//creates a YUVImage that is an copy of YUVImage copyImg.
        this.height = copyImg.height;
        this.width = copyImg.width;
        
        this.pixelArray = new YUVPixel[this.height][this.width];
        
        for(int i = 0 ; i < this.height; i++){//sets array pixel's values to the values of the image we want to copy.
            for(int j = 0; j < this.width; j++){
                 this.pixelArray[i][j] = new YUVPixel(copyImg.pixelArray[i][j]);
            }
        }
    }  
//    Creates a YUVImage type object from an RGBImage type object
    public YUVImage(RGBImage RGBImg){
        this.height = RGBImg.getHeight();
        this.width = RGBImg.getWidth();
        this.pixelArray = new YUVPixel[this.height][this.width];
        
        for(int i = 0 ; i < this.height; i++){
            for(int j = 0; j < this.width; j++){
                 this.pixelArray[i][j] = new YUVPixel(RGBImg.pixelArray[i][j]);
            }
        }
        
    }
    //Creates a YUVImage type object whose information it reads from the file
    public YUVImage(java.io.File file)  throws UnsupportedFileFormatException, java.io.FileNotFoundException{
        short Y, U, V;
    
        try{
            Scanner YUVfile = new Scanner(file);
            if(!(YUVfile.next().equals("YUV3"))){
                throw new UnsupportedFileFormatException("String in first line does not match YUV3!");
            }
            
            //check if the width values is acceptable.
            if(YUVfile.hasNextInt()){
                width = YUVfile.nextInt();
                if(width <= 0){
                    throw new UnsupportedFileFormatException("Width value is not a positive integer");
                }
////                    System.out.println("WIDTH IS "+width);
            }
            else{
                throw new UnsupportedFileFormatException("Width value is not an acceptable value");
            }
            
            //check if  height value is acceptable.
            if(YUVfile.hasNextInt()){
                height = YUVfile.nextInt();
                if(height <= 0){
                    throw new UnsupportedFileFormatException("Height value is not a positive integer");
                }
//                    System.out.println("HEIGHT IS "+height)
            }
            else{
                throw new UnsupportedFileFormatException("Height value is not an acceptable value");
            }
            
            pixelArray = new YUVPixel[height][width];
            
            for(int i = 0; i < height; i++){
                for(int j =0 ; j < width; j++){
                    if(YUVfile.hasNextInt()){//check if Y value is acceptable
                        Y = (short) YUVfile.nextInt();
                    }
                    else{
                        throw new UnsupportedFileFormatException("invalid Y");
                    }
                    if(YUVfile.hasNextInt()){//check if U value is acceptable
                         U = (short) YUVfile.nextInt();
                    }
                    else{
                        throw new UnsupportedFileFormatException("invalid U");
                    }
                    if(YUVfile.hasNextInt()){//check if V value is acceptable
                         V = (short) YUVfile.nextInt();
                    }
                    else{
                        throw new UnsupportedFileFormatException("invalid V");
                    }
                    pixelArray[i][j] = new YUVPixel(Y, U, V);
                }
            }
        }
        catch(java.io.FileNotFoundException ex){
            throw new FileNotFoundException("File was not found!");
        }
    }
//methods
    
    public String toString(){ //creates string that consists of contents of YUV file.
        StringBuilder str = new StringBuilder(("YUV3\n" + width + " " + height + "\n" ));
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                str.append(pixelArray[i][j].toString());
            }
            str.append("\n");
        }
        return str.toString();
    }
    
    public void toFile(java.io.File file){
        try {
            FileWriter myWriter = new FileWriter(file, false); //If file already exists, overwrite it
            myWriter.write(toString());
            myWriter.close();
        }
        catch (IOException e) {
            System.out.println("ERROR: in YUVImage toFile");
        }
    }    
    
    public void equalize(){
        Histogram histogram = new Histogram(this);
        histogram.equalize();
        
        for(int i=0;i<height;i++){ //sets equalized values to pixels.
            for(int j=0;j<width;j++){
                pixelArray[i][j].setY(histogram.getEqualizedLuminocity(pixelArray[i][j].getY()));
            }
        }      
    }
    
    int getWidth(){//Returns an integer that corresponds to the width value of that image.
       return (this.pixelArray[0].length);
    }

    int getHeight(){//Returns an integer that corresponds to the height value of that image.
        return (pixelArray.length);
    }
}