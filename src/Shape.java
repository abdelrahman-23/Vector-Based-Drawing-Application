public interface Shape
{
    /* set position */
    public void setPosition(java.awt.Point position);
    public java.awt.Point getPosition();
    /* update shape specific properties */
    public void setProperties(java.util.Map<String, Double> properties);
    public java.util.Map<String, Double> getProperties();
    /* colorize */
    public void setColor(java.awt.Color color);
    public java.awt.Color getColor();
    public void setFillColor(java.awt.Color color);
    public java.awt.Color getFillColor();
    /* redraw the shape on the canvas */
    public void draw(java.awt.Graphics canvas);
    /* create a clone */
    public Object clone() throws CloneNotSupportedException;
}