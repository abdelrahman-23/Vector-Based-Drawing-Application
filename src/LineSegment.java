import java.awt.Graphics;
import java.util.Iterator;
import java.util.Map;
public class LineSegment extends Shape2 implements Shape
{

    //default constructor make line start from Point of origin
    public LineSegment(){
        super();
        this.properties.put("x2", 0.0);
        this.properties.put("y2", 0.0);
    }

    // draw line by take position of start x , y
    @Override
    public void draw(Graphics canvas)
    {
        canvas.setColor(this.color);
        canvas.drawLine((int)position.getX(), (int)position.getY(), (int)(properties.get("x2").doubleValue()),(int)(properties.get("y2").doubleValue()));
    }

    //clone another line by take value and moving direction
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        LineSegment LineSegment = new LineSegment();
        Iterator<Map.Entry<String, Double>> Iterator = properties.entrySet().iterator();
        while(Iterator.hasNext()) {
            Map.Entry<String, Double> Entry = Iterator.next();
            LineSegment.properties.put(Entry.getKey(),Entry.getValue());
        }
        LineSegment.setPosition(this.getPosition());
        LineSegment.setColor(this.getColor());
        LineSegment.setFillColor(this.getFillColor());
        return LineSegment;
    }

}
