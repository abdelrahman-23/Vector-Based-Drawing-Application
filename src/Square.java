import java.awt.Graphics;
import java.util.Iterator;
import java.util.Map;

public class Square extends Shape2 implements Shape
{

    //default constructor make each side of square = 0
    public Square()
    {
        super();
        this.properties.put("Side", 0.0);
    }

    //draw rectangle with take position of x and y and convert to double
    @Override
    public void draw(Graphics canvas)
    {
        canvas.setColor(this.color);
        canvas.drawRect((int)position.getX(), (int)position.getY(), (int)properties.get("Side").doubleValue(), (int)properties.get("Side").doubleValue());
        canvas.setColor(this.fillColor);
        canvas.fillRect((int)position.getX(), (int)position.getY(), (int)properties.get("Side").doubleValue(), (int)properties.get("Side").doubleValue());
    }

    // clone another square by take value and moving direction
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Shape2 Square = new Square();
        Iterator<Map.Entry<String, Double>> Iterator = properties.entrySet().iterator();
        while(Iterator.hasNext()) {
            Map.Entry<String, Double> Entry = Iterator.next();
            Square.properties.put(Entry.getKey(),Entry.getValue());
        }
        Square.setPosition(this.getPosition());
        Square.setColor(this.getColor());
        Square.setFillColor(this.getFillColor());
        return Square;
    }

}