import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class implementCanvas extends JPanel implements DrawingEngine
{
     private final Stack Undo;
     private final Stack Redo;
     private final Stack Temp;
     private int UndoSize;
     private int RedoSize;
     private ArrayList<Shape> shapes;
     private final ArrayList<Class<? extends Shape>> SupportedShapes;
    //default constructor to make array list to save undo and redo on ir
    public implementCanvas()
    {
        shapes = new ArrayList<>();
        SupportedShapes = new ArrayList<>();
        Undo = new Stack();
        Redo = new Stack();
        Temp = new Stack();
    }
    //set a shape by number of index and type of shape
    public void setShape(Shape Shape, int index)
    {
        this.shapes.set(index, Shape);
    }

    @Override
    public void paintComponent(Graphics graph)
    {
        super.paintComponent(graph);
        shapes.forEach((s) -> {s.draw(graph);});
    }

    // add a shape to canvas
    @Override
    public void addShape(Shape shape)
    {
        shapes.add(shape);
    }

    // remove a shape from canvas
    public void deleteShape(Shape shape)
    {
        shapes.remove(shape);
    }

    // get shape from array
    @Override
    public Shape[] getShapes()
    {
        return shapes.toArray(new Shape[shapes.size()]);

    }

    // draw shape another time
    @Override
    public void redraw(Graphics canvas)
    {
        this.repaint();
    }

    @Override
    public List<Class<? extends Shape>> getSupportedShapes()
    {
        return SupportedShapes;
    }

    @Override
    public void installToShape(Class<? extends Shape> shapeClass)
    {
        SupportedShapes.add(shapeClass);
    }

    // undo operation by push every operation on stack and pop FIFO for 100 time
    @Override
    public void undo()
    {
        if (!Undo.isEmpty())
        {
            if (RedoSize<100)
            {
                Redo.push(shapes.clone());
                RedoSize++;
                UndoSize--;
                shapes = (ArrayList<Shape>) Undo.pop();
            }
            else
            {
                removeOldest(Redo);
                RedoSize--;
                undo();
            }
        }
        else
            System.out.println("Nothing to Undo");
    }

    // redo operation by push every operation on stack and pop FIFO for 100 time
    @Override
    public void redo()
    {
        if (!Redo.isEmpty())
        {
            if (UndoSize<100)
            {
                Undo.push(shapes.clone());
                UndoSize++;
                RedoSize--;
                shapes.clear();
                shapes = (ArrayList<Shape>) Redo.pop();
            }
            else
            {
                removeOldest(Undo);
                UndoSize--;
                redo();
            }
        }
        else
            System.out.println("Nothing to redo");
    }

    // save operations on array
    public void saveArray()
    {
        while (!Redo.empty())
        {
            Redo.pop();
            RedoSize=0;
        }

        if (UndoSize<100)
        {
            Undo.push(shapes.clone());
            UndoSize++;
        }
        else{
            removeOldest(Undo);
            UndoSize--;
            saveArray();
        }
    }

    // format stack
    private void removeOldest(Stack stack)
    {
        while (!stack.empty())
            Temp.push(stack.pop());
        Temp.pop();
        while (!Temp.empty())
            stack.push(Temp.pop());
    }

    //number of undo time
    public int getUndoSize()
    {
        return UndoSize;
    }

    //number of redo time
    public int getRedoSize()
    {
        return RedoSize;
    }

}
