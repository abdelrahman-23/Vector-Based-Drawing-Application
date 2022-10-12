import java.awt.Graphics;
import java.util.Iterator;
import java.util.Map;
public class Rectangle extends Shape2 implements Shape
{

    // default constructor make wight and height of rectangle = 0
    public Rectangle()
    {
        super();
        this.properties.put("Width", 0.0);
        this.properties.put("Height", 0.0);
    }

    //draw rectangle with take position of x and y and convert to double
    @Override
    public void draw(Graphics canvas)
    {
        canvas.setColor(this.color);
        canvas.drawRect((int) position.getX(), (int) position.getY(), (int) properties.get("Width").doubleValue(), (int) properties.get("Height").doubleValue());
        canvas.setColor(this.fillColor);
        canvas.fillRect((int) position.getX(), (int) position.getY(), (int) properties.get("Width").doubleValue(), (int) properties.get("Height").doubleValue());
    }

    // clone another rectangle by take value and moving direction
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Shape2 Rectangle = new Rectangle();
        Iterator<Map.Entry<String, Double>> Iterator = properties.entrySet().iterator();
        while (Iterator.hasNext()) {
            Map.Entry<String, Double> Entry = Iterator.next();
            Rectangle.properties.put(Entry.getKey(), Entry.getValue());
        }
        Rectangle.setPosition(this.getPosition());
        Rectangle.setColor(this.getColor());
        Rectangle.setFillColor(this.getFillColor());
        return Rectangle;
    }

}