import java.awt.Graphics;
import java.util.Iterator;
import java.util.Map;
public class Circle extends Shape2 implements Shape
{

    // default constructor make radius = 0 on Point of origin
    public Circle()
    {
        super();
        this.properties.put("Radius", 0.0);
    }

    //draw circle with take radius and position of x and y
    @Override
    public void draw(Graphics canvas)
    {
        canvas.setColor(this.color);
        canvas.drawOval((int)position.getX(),(int) position.getY(),(properties.get("Radius")).intValue()*2,(properties.get("Radius")).intValue()*2);
        canvas.setColor(this.fillColor);
        canvas.fillOval((int)position.getX(),(int) position.getY(),(properties.get("Radius")).intValue()*2 ,(properties.get("Radius")).intValue()*2);
    }

    // clone circle by take value and moving direction
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Shape2 Circle = new Circle();
        Iterator<Map.Entry<String, Double>> Iterator = properties.entrySet().iterator();
        while(Iterator.hasNext()) {
            Map.Entry<String, Double> Entry = Iterator.next();
            Circle.properties.put(Entry.getKey(),Entry.getValue());
        }
        Circle.setPosition(this.position);
        Circle.setColor(this.color);
        Circle.setFillColor(this.fillColor);
        return Circle;
    }

}