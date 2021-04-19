package knox.drawshapes;



import java.awt.Color;
import java.awt.Point;

public class Square extends Rectangle
{
    public Square(Color color, int centerX, int centerY, int length) {
        super(new Point(centerX, centerY), length, length, color);
    }
    
    public Square(Color color, Point center, int length) {
    	super(center, length, length, color);
    }
    
    public String toString() {
        return String.format("SQUARE %d %d %d %d %d %d %d %s", 
                getAnchorPoint().x,
                getAnchorPoint().y,
                width,
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha(),
                selected);
    }
    
    
}
