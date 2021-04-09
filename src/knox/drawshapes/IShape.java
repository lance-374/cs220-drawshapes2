package knox.drawshapes;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Interface for a shape to be drawn
 * 
 * You can add methods to this class if you'd like, such as
 * a method to move the anchor point by a given X/Y amount. 
 * 
 * @author jspacco
 *
 */
/**
 * @author jaimespacco
 *
 */
public interface IShape extends IMoveableShape
{
    /**
     * Draw the shape using the given Graphics object
     * @param g
     */
    public void draw(Graphics g);
    /**
     * Does this shape intersect any part of the other shape?
     * 
     * @param other
     * @return
     */
    public boolean intersects(IShape other);
    /**
     * Does this shape contain the given point?
     * 
     * @param point
     * @return
     */
    public boolean contains(Point point);
    /**
     * Return the color of this shape.
     * 
     * @return
     */
    public Color getColor();
    /**
     * Set the color of this shape to the given color.
     * 
     * @param color
     */
    public void setColor(Color color);
    /**
     * Is this shape selected?
     * 
     * Some operations apply to all selected shapes.
     * 
     * @return
     */
    public boolean isSelected();
    /**
     * Set the selected status of this shape to the given value.
     * @param b
     */
    public void setSelected(boolean b);
    
    /**
     * Return the anchor point of this shape.
     * @return
     */
    public Point getAnchorPoint();
    /**
     * Set the anchor point of this shape to the given point.
     * @param p
     */
    public void setAnchorPoint(Point p);
}
