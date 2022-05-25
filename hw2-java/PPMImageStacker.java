/*
Author: Sapountzi Athanasia Despoina 02624
 */
package ce326.hw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class PPMImageStacker  {
    List <PPMImage> imageList;  //create list that consists of images
    PPMImage finalImage;
    
    //constructor
    public PPMImageStacker(java.io.File dir) throws FileNotFoundException, UnsupportedFileFormatException {
        try{
            if(!(dir.exists())){ //check if dir exists!
                throw new FileNotFoundException("[ERROR]Directory<"+dir.getName()+">does not exist!");
            }
            else if(!(dir.isDirectory())) {//check if dir is a directory!
                throw new FileNotFoundException("[ERROR]Directory<"+dir.getName()+">is not a directory!");
            }

            File [] filesInDir;
            PPMImage currentImage;
            filesInDir = dir.listFiles(); //an array of files pathnames in directory

            imageList = new LinkedList<PPMImage>(); //Create list 
           
            for (int i = 0; i < filesInDir.length; i++) { 
               currentImage = new PPMImage(filesInDir[i]);
               imageList.add(currentImage); //add image to the list
            }
            finalImage = new PPMImage(filesInDir[0]);  
        }
        catch(UnsupportedFileFormatException e){
            System.out.println(" UnsupportedFileFormatException!"+e.getMessage());     
        }
    }
    //computes average of red,average of green and average of blue pixels of the corresponding pixel, of all the images involved 
    //and sets the final PPMImage's pixels to those average values.
    public void stack(){
        short avgRed = 0, avgGreen = 0, avgBlue = 0;
        int width, height, listSize;
        
        width = imageList.get(0).getWidth();
        height = imageList.get(0).getHeight();
        listSize = imageList.size();
     
        for(int i = 0; i < height; i++){ 
            for(int j = 0; j < width ; j++){
                avgRed = 0;
                avgGreen = 0;
                avgBlue = 0;
		for(int k = 0; k < listSize; k++){
                    avgRed +=  imageList.get(k).pixelArray[i][j].getRed();
                    avgGreen +=  imageList.get(k).pixelArray[i][j].getGreen();
                    avgBlue +=  imageList.get(k).pixelArray[i][j].getBlue();
                }
                avgRed = (short) (avgRed / listSize);
                avgGreen = (short) (avgGreen / listSize);
                avgBlue = (short) (avgBlue / listSize);
                
                finalImage.pixelArray[i][j] = new RGBPixel(avgRed, avgGreen, avgBlue);
            }
        }         
    }
    
    public PPMImage getStackedImage(){
        return finalImage;
    }
}
