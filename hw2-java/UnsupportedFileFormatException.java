/*
Author: Sapountzi Athanasia Despoina 02624
 */
package ce326.hw2;

//UnsupportedFileFormatException is a descendant of the class java.lang.Exception
public class UnsupportedFileFormatException extends java.lang.Exception{ 
    static final long serialVersionUID = -4567891456L;
    
    public UnsupportedFileFormatException(){
        super();
    }
    
    public UnsupportedFileFormatException(String msg){
        super(msg);
                
    }
    
}
