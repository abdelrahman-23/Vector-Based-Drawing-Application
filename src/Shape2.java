import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Shape2 implements Shape{
    protected Point position;
    public Map<String, Double> properties;
    protected Color color;
    protected Color fillColor;

    public Shape2() {
        this.position = new Point();
        this.properties = new LinkedHashMap<>();
        this.color = new Color(255, 255, 255);
        this.fillColor = new Color(7, 56, 86);
    }
    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, Double> getProperties() {
        return properties;
    }

    @Override
    public void setColor(Color color)
    {
        this.color = color;
    }
    @Override
    public Color getColor()
    {
        return color;
    }
    @Override
    public void setFillColor(Color color) {
        this.fillColor = color;
    }
    @Override
    public Color getFillColor() {
        return fillColor;
    }
    // if I don't Override The Method here, It Gives Me an Error !!
    @Override
    public Object clone() throws CloneNotSupportedException{
        return null;
    }
}
