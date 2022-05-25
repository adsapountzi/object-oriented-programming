/*
Author: Sapountzi Athanasia Despoina 02624
 */
package ce326.hw2;

public class RGBPixel {
    public static final int MAXVALUE = 255;
    private int RGBColor;
    //constructors 
    public RGBPixel(short red, short green, short blue){ //Creates pixel based on red, green and blue values.
        RGBColor = ((((red<<8)+green)<<8)+blue);  
    }
    public RGBPixel(RGBPixel pixel){ //Creates an object that is an exact copy of that pixel.
         this(pixel.getRed(), pixel.getGreen(), pixel.getBlue()); //calls the above constructor with the pixel colors.
    }   
    public RGBPixel(YUVPixel pixel){ //Creates an RGBPixel from an YUVPixel.s
        int c = pixel.getY() - 16;
        int d = pixel.getU() - 128;
        int e = pixel.getV() - 128;
        
        RGBColor = ((((clip(( 298 * c + 409 * e + 128) >> 8)<<8) + clip(( 298 * c - 100 * d - 208 * e + 128) >> 8))<<8) + clip(( 298 * c + 516 * d + 128) >> 8)); 
        
    }
    public int clip(int value){
        if(value > MAXVALUE){
            value = MAXVALUE;
        }
        else if(value < 0) {
            value = 0;
        }
        
        return value;
    }
    
    public short getRed(){ //returns the value of the red color.
        return (short)(this.RGBColor>>16);
    }
    short  getGreen(){//returns the value of the green color.
        return (short)((this.RGBColor>>8) & 0xFF);
    } 
    short  getBlue(){ //returns the value of the blue color.
        return (short)(this.RGBColor & 0xFF);
    }
    
    void setRed(short red){ //sets the value of the red color.
        this.RGBColor = (this.RGBColor & 0xFFFF) + ((int)red<<16); //keep only the green and blue values and set the new red value.
    }
    
    void setGreen(short green){ //sets the value of the green color.
        this.RGBColor = (this.RGBColor & 0xFF00FF) + ((int)green<<8); //keep only the red and blue values and set the new green value.
    }
    
    void setBlue(short blue){ //sets the value of the blue color.
        this.RGBColor = (this.RGBColor & 0xFFFF00) + ((int)blue); //keep only the red and green values and set the new blue value.

    }
    
    int getRGB(){ //returns an integer that contains the 3 RGB colors
        return(this.RGBColor);
    }
    
    void setRGB(int value){ //sets the values of the 3 RGB colors based on the value of the integer.
        this.RGBColor = value;
    }    
    
    final void setRGB(short red, short green, short blue){//sets the values of the three colos based on the variables red, green and blue
        this.RGBColor = ((((red<<8)+green)<<8)+blue);
    }
    
    public String toString(){ //returns a string in the form "R G B", where R=red color value,G=green color value, B=blue color value.
        return getRed()+" "+getGreen()+" "+getBlue()+" ";
    } 
}
