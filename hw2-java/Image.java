/*
Author: Sapountzi Athanasia Despoina 02624
 */
package ce326.hw2;

/**
 *
 * @author Sapountzi Athanasia Despoina
 */
public interface Image {
    public void grayscale(); //transforms image into black and white.
    
    public void doublesize(); //doubles the size of the image.
    
    public void halfsize(); //makes image's size half of its original size.
    
    public void rotateClockwise(); //rotates the image by 90 degrees.
}
