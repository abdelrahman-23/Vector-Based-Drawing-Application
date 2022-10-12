import java.awt.*;
import java.util.Iterator;
import java.util.Map;
public class Ellipse extends Shape2 implements Shape
{

    // Ellipse should have wight and height . default constructor to make them 0,0
    public Ellipse(){
        super();
        this.properties.put("Width", 0.0);
        this.properties.put("Height", 0.0);
    }

    //draw Ellipse with take position of x and y and convert to integer
    @Override
    public void draw(Graphics canvas)
    {
        canvas.setColor(this.color);
        canvas.drawOval((int)position.getX(),(int) position.getY(),(properties.get("Width")).intValue(),(properties.get("Height")).intValue());
        canvas.setColor(this.fillColor);
        canvas.fillOval((int)position.getX(),(int) position.getY(),(properties.get("Width")).intValue(),(properties.get("Height")).intValue());
    }

    // clone another Ellipse by take value and moving direction
    @Override
    public Object clone() throws CloneNotSupportedException{
        Shape2 Ellipse = new Ellipse();
        Iterator<Map.Entry<String, Double>> Iterator = properties.entrySet().iterator();
        while(Iterator.hasNext()) {
            Map.Entry<String, Double> Entry = Iterator.next();
            Ellipse.properties.put(Entry.getKey(),Entry.getValue());
        }
        Ellipse.setPosition(this.getPosition());
        Ellipse.setColor(this.getColor());
        Ellipse.setFillColor(this.getFillColor());
        return Ellipse;
    }

}