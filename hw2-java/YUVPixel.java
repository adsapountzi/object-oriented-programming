/*
Author: Sapountzi Athanasia Despoina 02624
 */
package ce326.hw2;


public class YUVPixel {
    private int YUVColor;
    
    public YUVPixel(short Y, short U, short V){//Creates a YUVPixel based on Y,U,V values.
        YUVColor = ((((Y<<8)+U)<<8)+V);
    }
    public YUVPixel(YUVPixel pixel){//Creates a copy of that YUVPixel.
        this(pixel.getY(), pixel.getU(), pixel.getV()); 
    }
    
    public YUVPixel(RGBPixel pixel){//creates YUVPixel from RGBPixel pixel.
        short Y, U, V;
        Y = (short) ((( 66 * pixel.getRed() + 129 * pixel.getGreen() + 25 * pixel.getBlue() + 128) >> 8) + 16);
        U = (short) ((( -38 * pixel.getRed() - 74 * pixel.getGreen() + 112 * pixel.getBlue() + 128) >> 8) + 128);
        V = (short) ((( 112 * pixel.getRed() - 94 * pixel.getGreen() - 18 * pixel.getBlue() + 128) >> 8) + 128);
        
        YUVColor = ((((Y<<8)+U)<<8)+V);
    }
    
    short getY(){//returns the value of Y.
        return (short)(this.YUVColor>>16);
    }
    
    short getU(){//returns the value of U.
        return (short)((this.YUVColor>>8) & 0xFF);
    }
    
    short getV(){//returns the value of V.
        return (short)(this.YUVColor & 0xFF);
    }
    
    void setY(short Y){//sets the value of Y.
        this.YUVColor = (this.YUVColor & 0xFFFF) + ((int)Y<<16);
    }
    
    void setU(short U){//sets the value of U.
        this.YUVColor = (this.YUVColor & 0xFF00FF) + ((int)U<<8);
    }
    
    void setV(short V){//sets the value of V.
         this.YUVColor = (this.YUVColor & 0xFFFF00) + ((int)V); 
    }
    public String toString(){
        return getY()+" "+getU()+" "+getV()+" ";
    }
        
}
