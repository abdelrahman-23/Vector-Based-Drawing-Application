public interface DrawingEngine
{
    public void addShape(Shape shape);
    public void deleteShape(Shape shape);
    // return the created shapes objects
    public Shape[] getShapes();
    // redraw all shapes on the canvas
    public void redraw(java.awt.Graphics canvas);
    /* return the classes' types of supported shapes that can
      be dynamically loaded at runtime */
    public java.util.List<Class<? extends Shape>> getSupportedShapes();
    /* add to the shapes the new shape class */
    public void installToShape (Class<? extends Shape> shapeClass);
    /* Only consider these actions: addShape, removeShape . Only 20 steps */
    public void undo();
    public void redo();
}