/*
Author: Sapountzi Athanasia Despoina 02624
 */
package ce326.hw2;

public class RGBImage implements Image{
       public static final int MAX_COLORDEPTH = 255; //??\
       protected RGBPixel [][] pixelArray; //2D array of pixels
      // private int width;	//width of theimage
      // private int height;	// height of the image
       private int colordepth;	//colordepth of the image
    //constructors
    public RGBImage(){//deafult constructor , needed for constructor of inhereted class PPMImage
    }
    
    public RGBImage(int width, int height, int colordepth){ // creates an RGB image with dimensions: width and height and max brightness is colodepth  
        pixelArray = new RGBPixel[height][width];
        int i,j;
        
//        this.height = height;
//        this.width = width;
           this.colordepth = colordepth;
//        
        for(i = 0; i < this.getHeight(); i++){
            for(j = 0; j  < this.getWidth(); j++){
                pixelArray[i][j] = new RGBPixel((short) colordepth, (short) colordepth, (short)colordepth); 
            }
        }    
    }
    
    public RGBImage(RGBImage copyImg){ //creates an RGB image that is a copy of another RGB image. 
        pixelArray = new RGBPixel[copyImg.getHeight()][copyImg.getWidth()];
       
        for(int i = 0 ; i < copyImg.getHeight(); i++){
          for(int j = 0; j < copyImg.getWidth(); j++){
              pixelArray[i][j] = new RGBPixel(copyImg.getPixel(i, j));
          }
        }
    }
    
    public RGBImage(YUVImage YUVImg){ //creates an RGB image from an YUV image.
        this.colordepth = MAX_COLORDEPTH;
        this.pixelArray = new RGBPixel[YUVImg.getHeight()][YUVImg.getWidth()];
        for(int i = 0; i < YUVImg.getHeight(); i++){
            for(int j = 0; j < YUVImg.getWidth(); j++){
                this.pixelArray[i][j] = new RGBPixel(YUVImg.pixelArray[i][j]);
			}
		}
    }
    
    //methods
    int getWidth(){//Returns an integer that corresponds to the width value of that image.
        return (this.pixelArray[0].length);
    }
    
    int getHeight(){//Returns an integer that corresponds to the height value of that image.
        
        return (pixelArray.length);
    }
    
    int getColorDepth(){//Returns an integer that corresponds to the color depth value of that image.
        return(this.colordepth);
    }
    
    RGBPixel getPixel(int row, int col){//Returns an RGB object that corresponds to row and col of the array of that image.
        return(this.pixelArray[row][col]);
    }
    
    void  setPixel(int row, int col, RGBPixel pixel){//Sets the RGB object at row and col of the array of that image.
        pixelArray[row][col] = pixel;
    }
    
    /////interface methods\\\\\\
    public void grayscale(){ //alters the image to grayscale image.
        int i,j;
        short gray;
        
        for(i = 0; i < getHeight(); i++){
            for(j = 0; j < getWidth(); j++){
                gray = (short) (pixelArray[i][j].getRed() * 0.3 + pixelArray[i][j].getGreen() * 0.59 + pixelArray[i][j].getBlue() * 0.11);
                
                pixelArray[i][j].setRed(gray);
                pixelArray[i][j].setGreen(gray);
                pixelArray[i][j].setBlue(gray);
            }
        }
    }
    
    public void doublesize(){ // doubles image size.
        RGBPixel [][]doubleArray = new RGBPixel[getHeight()*2][getWidth()*2];
        int i,j;
        
        for(i = 0; i < getHeight(); i++){
            for(j = 0; j < getWidth(); j++){ 
                doubleArray[2*i][2*j] = new RGBPixel(pixelArray[i][j]);
                doubleArray[2*i+1][2*j] = new RGBPixel(pixelArray[i][j]);
                doubleArray[2*i][2*j+1] = new RGBPixel(pixelArray[i][j]);
                doubleArray[2*i+1][2*j+1] = new RGBPixel(pixelArray[i][j]);
                //pixels[i][j] = null;???
            }
        }
        
        pixelArray = doubleArray; 
    }

    public void halfsize(){//changes image size in half.
        RGBPixel [][]halfArray = new RGBPixel[getHeight()/2][getWidth()/2];
        int i,j;
        short avgRed, avgGreen, avgBlue;
        
        for(i = 0; i < (getHeight()/2); i++){
            for(j = 0; j < (getWidth()/2); j++){ 
                avgRed = (short) ((pixelArray[2*i+1][2*j].getRed() + pixelArray[2*i][2*j].getRed() + pixelArray[2*i][2*j+1].getRed() + pixelArray[2*i+1][2*j+1].getRed()) / 4);
                avgGreen = (short) ((pixelArray[2*i+1][2*j].getGreen() + pixelArray[2*i][2*j].getGreen() + pixelArray[2*i][2*j+1].getGreen() + pixelArray[2*i+1][2*j+1].getGreen()) / 4);
                avgBlue = (short) ((pixelArray[2*i+1][2*j].getBlue() + pixelArray[2*i][2*j].getBlue() + pixelArray[2*i][2*j+1].getBlue() + pixelArray[2*i+1][2*j+1].getBlue()) / 4);
                
                halfArray[i][j] = new RGBPixel(avgRed, avgGreen, avgBlue); 
                
            }
        }
        
        pixelArray = halfArray; 
    }
    
    public void rotateClockwise(){// rotates image.
        RGBPixel [][]rotatedArray = new RGBPixel[getWidth()][getHeight()]; //swap width with height dimension and vice versa. 
        int i,j;
        
        for(i = 0; i < getHeight(); i++){
            for(j = 0; j < getWidth(); j++){ 
                rotatedArray[j][getHeight()-1-i] = pixelArray[i][j];
            }
        }
        pixelArray = rotatedArray; 
    }
}
