package knox.drawshapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Rectangle extends AbstractShape
{
    protected int width;
    protected int height;
    
    public Rectangle(Point center, int width, int height, Color color){
    	super(new Point(center.x, center.y), color);
        boundingBox = new BoundingBox(center.x - width/2, center.x + width/2, center.y - height/2, center.y + height/2);
        this.width = width;
        this.height = height;
    }
    
    public Rectangle(int left, int right, int top, int bottom) {
    	this(new Point(right - (right-left)/2, bottom - (bottom-top)/2),
    			right - left, bottom - top, Color.BLUE);
    }

    /* (non-Javadoc)
     * @see drawshapes.sol.Shape#draw(java.awt.Graphics)
     */
    @Override
    public void draw(Graphics g) {
        if (isSelected()){
            g.setColor(color.darker());
        } else {
            g.setColor(getColor());
        }
        g.fillRect(getAnchorPoint().x - width/2, 
        		getAnchorPoint().y - height/2, 
        		width, height);
    }

    public String toString() {
        return String.format("RECTANGLE %d %d %d %d %d %d %d %s", 
                getAnchorPoint().x,
                getAnchorPoint().y,
                width,
                height,
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                selected);
    }
    
    public String encode() {
    	return String.format("RECTANGLE %d %d %d %d %d %d %d %d %s", 
                getAnchorPoint().x,
                getAnchorPoint().y,
                width,
                height,
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha(),
                selected);
    }
    
	@Override
	public void scale(double factor) {
		this.width = (int)(this.width * factor);
		this.height = (int)(this.height * factor);
	}
    

}
