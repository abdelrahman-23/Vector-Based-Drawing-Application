import java.awt.Point;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main extends javax.swing.JFrame
{

    // Variables declaration
    private javax.swing.JDialog AddDialog;
    private javax.swing.JColorChooser ColorChooser;
    private javax.swing.JFrame ColorsFrame;
    private javax.swing.JCheckBox FillCheckBox;
    private javax.swing.JTextField MoveByTextField;
    private javax.swing.JDialog MoveDialog;
    private javax.swing.JCheckBox OutlineCheckBox;
    private javax.swing.JPanel PaintingCanvas;
    private javax.swing.JLabel PropertiesLabel1;
    private javax.swing.JLabel PropertiesLabel2;
    private javax.swing.JLabel PropertiesLabel3;
    private javax.swing.JLabel PropertiesLabel4;
    private javax.swing.JTextField PropertiesTextField1;
    private javax.swing.JTextField PropertiesTextField2;
    private javax.swing.JTextField PropertiesTextField3;
    private javax.swing.JTextField PropertiesTextField4;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JComboBox<String> PluginsList;
    private javax.swing.JButton RedoButton;
    private javax.swing.JComboBox<String> ShapesList;
    private javax.swing.JButton UndoButton;
    private javax.swing.JTextField XTextField;
    private javax.swing.JTextField YTextField;
    private javax.swing.JLabel xLabel;
    private javax.swing.JLabel yLabel;
    private static Shape CurrentShape;
    private static Map<String, Double> CurrentShapeProperties;
    private static implementCanvas Canvas ;
    private static final JTextField[] TextFields = new JTextField[6];
    private static final JLabel[] Labels = new JLabel[6];
    private static char Choice;

    // shape's labels and text
    public Main()
    {
        initComponents();
        Canvas = (implementCanvas) PaintingCanvas;
        TextFields[0] = XTextField;
        TextFields[1] = YTextField;
        TextFields[2] = PropertiesTextField1;
        TextFields[3] = PropertiesTextField2;
        TextFields[4] = PropertiesTextField3;
        TextFields[5] = PropertiesTextField4;
        Labels[0] = xLabel;
        Labels[1] = yLabel;
        Labels[2] = PropertiesLabel1;
        Labels[3] = PropertiesLabel2;
        Labels[4] = PropertiesLabel3;
        Labels[5] = PropertiesLabel4;
    }

    //unable the useless fields
    private void CleanDialogBox()
    {
        Labels[0].setText("x:");
        Labels[1].setText("y:");
        for (int i = 0; i<2; i++)
            TextFields[i].setText("");
        for (int i = 2; i<6; i++)
        {
            TextFields[i].setText("");
            TextFields[i].setEnabled(false);
            Labels[i].setEnabled(false);
            Labels[i].setText("N/A");
        }
    }

    //position (x,y)
    private void prepareAddDialogBox()
    {
        Choice = 'A';
        CurrentShapeProperties = CurrentShape.getProperties();
        CleanDialogBox();
        for (int i = 0; i<2; i++)
        {
            TextFields[i].setEnabled(true);
            Labels[i].setEnabled(true);
        }
        setPropertiesFields();
        AddDialog.setVisible(true);
    }

    //edit by size ( don't edit x,y)
    private void prepareEditDialogBox()
    {
        CurrentShapeProperties = CurrentShape.getProperties();
        CleanDialogBox();
        for (int i = 0; i<2; i++)
        {
            TextFields[i].setEnabled(false);
            Labels[i].setEnabled(false);
        }
        setPropertiesFields();
        AddDialog.setVisible(true);
    }

    //set size of the tab of shapes
    private void setPropertiesFields()
    {
        Iterator<Map.Entry<String, Double>> Iterator = CurrentShapeProperties.entrySet().iterator();
        for (int i = 2; Iterator.hasNext();i++)
        {
            Map.Entry<String, Double> Entry = Iterator.next();
            if (Entry.getKey().equals("x2"))
            {
                for (int j = 0; j<2; j++)
                {
                    TextFields[j].setEnabled(true);
                    Labels[j].setEnabled(true);
                }
                Labels[0].setText("x1: ");
                Labels[1].setText("y1: ");
            }
            Labels[i].setText(Entry.getKey() + ": ");
            Labels[i].setEnabled(true); TextFields[i].setEnabled(true);
            AddDialog.setSize(410, (142+35*(i-2)));
        }
    }

    //check error
    private void readProperties()
    {
        Iterator<Map.Entry<String, Double>> Iterator = CurrentShapeProperties.entrySet().iterator();
        for (int i = 2; Iterator.hasNext();i++) {
            Map.Entry<String, Double> Entry = Iterator.next();
            if (TextFields[i].getText().isEmpty() && Choice == 'A')
                JOptionPane.showConfirmDialog(null,"Fill All Fields"  ,
                        "There's no Input ! ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            else if (!TextFields[i].getText().matches("\\d*"))
                JOptionPane.showConfirmDialog(null,"\"" + TextFields[i].getText() + "\" Is Not A Number "
                        , "Invalid Input !!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            else
                CurrentShapeProperties.put(Entry.getKey(),Double.parseDouble(TextFields[i].getText()));
        }
    }

    //move up
    private void MoveUpBy(int x){
        CurrentShapeProperties = CurrentShape.getProperties();
        CurrentShape.setPosition(new Point( (int) CurrentShape.getPosition().getX(), (int) CurrentShape.getPosition().getY() - x ));
        for (Map.Entry<String, Double> Entry : CurrentShapeProperties.entrySet()) {
            if (Entry.getKey().equals("y2"))
                Entry.setValue(Entry.getValue() - x);
            if (Entry.getKey().equals("y3"))
                Entry.setValue(Entry.getValue() - x);
        }
        Canvas.redraw(PaintingCanvas.getGraphics());
    }

    //move down
    private void MoveDownBy(int x){
        CurrentShapeProperties = CurrentShape.getProperties();
        CurrentShape.setPosition(new Point ((int) CurrentShape.getPosition().getX(), (int) CurrentShape.getPosition().getY() + x ));
        for (Map.Entry<String, Double> Entry : CurrentShapeProperties.entrySet()) {
            if (Entry.getKey().equals("y2"))
                Entry.setValue(Entry.getValue() + x);
            if (Entry.getKey().equals("y3"))
                Entry.setValue(Entry.getValue() + x);
        }
        Canvas.redraw(PaintingCanvas.getGraphics());
    }

    //move right
    private void MoveRightBy(int x){
        CurrentShapeProperties = CurrentShape.getProperties();
        CurrentShape.setPosition(new Point( (int) CurrentShape.getPosition().getX() + x, (int) CurrentShape.getPosition().getY()));
        for (Map.Entry<String, Double> Entry : CurrentShapeProperties.entrySet()) {
            if (Entry.getKey().equals("x2"))
                Entry.setValue(Entry.getValue() + x);
            if (Entry.getKey().equals("x3"))
                Entry.setValue(Entry.getValue() + x);
        }
        Canvas.redraw(PaintingCanvas.getGraphics());
    }

    //move left
    private void MoveLeftBy(int x){
        CurrentShapeProperties = CurrentShape.getProperties();
        CurrentShape.setPosition(new Point( (int) CurrentShape.getPosition().getX() - x, (int) CurrentShape.getPosition().getY()));
        for (Map.Entry<String, Double> Entry : CurrentShapeProperties.entrySet()) {
            if (Entry.getKey().equals("x2"))
                Entry.setValue(Entry.getValue() - x);
            if (Entry.getKey().equals("x3"))
                Entry.setValue(Entry.getValue() - x);
        }
        Canvas.redraw(PaintingCanvas.getGraphics());
    }

    //bydy el rqm el by3ml beh move
    private Point getNewPosition(){
        AddDialog.setVisible(false);
        int x = (int) CurrentShape.getPosition().getX();
        int y = (int) CurrentShape.getPosition().getY();
        if (!XTextField.getText().equals(""))
            x = Integer.parseInt(XTextField.getText());
        if (!YTextField.getText().equals(""))
            y = Integer.parseInt(YTextField.getText());
        return new Point(x,y);
    }

    // add new shape
    private void addNewShape(){
        CurrentShape.setPosition(getNewPosition());
        readProperties();
        CurrentShape.setProperties(CurrentShapeProperties);
        Canvas.addShape(CurrentShape);
        UndoButton.setEnabled(true);
        RedoButton.setEnabled(false);
        AddDialog.setVisible(false);
    }

    // Edit properties
    private Map<String, Double> getNewProperties(){
        readProperties();
        AddDialog.setVisible(false);
        return CurrentShapeProperties;
    }

    //list Contains all Shapes
    private void UpdateList(){
        ShapesList.removeAllItems();
        Shape[] Shapes = Canvas.getShapes();
        int counter = 0;
        for (int i = 0; i<Shapes.length; i++) {
            int currentShapeCounter = 1;
            for (int j = 0; j<i; j++){
                if (Shapes[i].getClass().getSimpleName().equals(Shapes[j].getClass().getSimpleName()))
                    currentShapeCounter++;
            }
            ShapesList.addItem(++counter + ". " + Shapes[i].getClass().getSimpleName() + " #" + currentShapeCounter);
        }
        ShapesList.setSelectedIndex(ShapesList.getItemCount()-1);
        if (ShapesList.getItemCount() == 0)
            ShapesList.addItem("Choose Shape");
    }

    //colorize button
    private Boolean changeColor(){
        if (FillCheckBox.isSelected())
            CurrentShape.setFillColor(ColorChooser.getColor());
        if (OutlineCheckBox.isSelected())
            CurrentShape.setColor(ColorChooser.getColor());
        Canvas.redraw(PaintingCanvas.getGraphics());
        if (!FillCheckBox.isSelected() && !OutlineCheckBox.isSelected()){
            JOptionPane.showConfirmDialog(null,"Select a Color to Change "  , "No Selection !!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }


    //Circle
    private void CircleButtonActionPerformed(java.awt.event.ActionEvent evt) {
        CurrentShape = new Circle();
        prepareAddDialogBox();
    }

    //Line
    private void LineSegmentButtonActionPerformed(java.awt.event.ActionEvent evt) {
        CurrentShape = new LineSegment();
        prepareAddDialogBox();

    }

    //Square
    private void SquareButtonActionPerformed(java.awt.event.ActionEvent evt) {
        CurrentShape = new Square();
        prepareAddDialogBox();
    }

    //Ellipse
    private void EllipseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        CurrentShape = new Ellipse();
        prepareAddDialogBox();
    }

    //Triangle
    private void TriangleButtonActionPerformed(java.awt.event.ActionEvent evt) {
        CurrentShape = new Triangle();
        prepareAddDialogBox();
    }

    //Rectangle
    private void RectangleButtonActionPerformed(java.awt.event.ActionEvent evt) {
        CurrentShape = new Rectangle();
        prepareAddDialogBox();

    }

    //Enter in properties
    private void EnterButtonActionPerformed(java.awt.event.ActionEvent evt) {
        boolean flag = false;
        for (JTextField TextField : TextFields)
            if (!TextField.getText().matches("\\d*")){
                flag = true;
                JOptionPane.showConfirmDialog(null,"\"" + TextField.getText() + "\" Is Not A Number "
                        , "Invalid Input !!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                break;
            }
        if (!flag){
            switch (Choice) {
                case 'A' -> {
                    Canvas.saveArray();
                    addNewShape();
                    UpdateList();
                }
                case 'E' -> {
                    CurrentShape.setPosition(getNewPosition());
                    CurrentShape.setProperties(getNewProperties());
                }
            }
            Canvas.redraw(PaintingCanvas.getGraphics());
        }
    }

    //Edit
    private void EditButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (Canvas.getShapes().length != 0){
            Choice = 'E';
            CurrentShape = Canvas.getShapes()[ShapesList.getSelectedIndex()];
            prepareEditDialogBox();
        }
        else
            JOptionPane.showConfirmDialog(null, "Please Create A New Shape First !!  ",
                    "No Shapes Found!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    //Move
    private void MoveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (Canvas.getShapes().length != 0){
            CurrentShape = Canvas.getShapes()[ShapesList.getSelectedIndex()];
            MoveDialog.setVisible(true);
            MoveDialog.setSize(290, 260);
        }
        else
            JOptionPane.showConfirmDialog(null, "Create a New Shape First !!  ",
                    "No Shapes Found!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    //Copy
    private void CopyButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (Canvas.getShapes().length != 0){
            CurrentShape = Canvas.getShapes()[ShapesList.getSelectedIndex()];
            CurrentShapeProperties = CurrentShape.getProperties();
            try {
                CurrentShape = (Shape) CurrentShape.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            Canvas.saveArray();
            Canvas.addShape(CurrentShape);
            CurrentShape = Canvas.getShapes()[Canvas.getShapes().length-1];
            MoveDownBy(50);
            MoveRightBy(50);
            UpdateList();
            Canvas.redraw(PaintingCanvas.getGraphics());

        }
        else
            JOptionPane.showConfirmDialog(null, "Create a New Shape First !!  "
                    , "No Shapes Found!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    //Colorize
    private void ColorizeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (Canvas.getShapes().length != 0){
            CurrentShape = Canvas.getShapes()[ShapesList.getSelectedIndex()];
            ColorsFrame.setVisible(true);
            ColorsFrame.setSize(663, 460);
        }
        else
            JOptionPane.showConfirmDialog(null, "Please Create A New Shape First !!  ",
                    "No Shapes Found!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    //Ok in Colorize
    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (changeColor())
            ColorsFrame.setVisible(false);
    }

    //Apply in Colorize
    private void ApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {
        changeColor();
    }

    //Cancel in Colorize
    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ColorsFrame.setVisible(false);
    }

    //Move right button
    private void MoveRightButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (MoveByTextField.getText().matches("\\d+"))
            MoveRightBy(Integer.parseInt(MoveByTextField.getText()));
        else if (MoveByTextField.getText().equals(""))
            JOptionPane.showConfirmDialog(null,"Enter Any Value"  , "No Input !!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        else
            JOptionPane.showConfirmDialog(null,"\"" + MoveByTextField.getText() + "\" Is Not A Number "  , "Invalid Input !!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    //Move up button
    private void MoveUpButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (MoveByTextField.getText().matches("\\d+"))
            MoveUpBy(Integer.parseInt(MoveByTextField.getText()));
        else if (MoveByTextField.getText().equals(""))
            JOptionPane.showConfirmDialog(null,"Enter Any Value"  , "No Input !!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        else
            JOptionPane.showConfirmDialog(null,"\"" + MoveByTextField.getText() + "\" Is Not A Number "  , "Invalid Input !!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    //Move left button
    private void MoveLeftButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (MoveByTextField.getText().matches("\\d+"))
            MoveLeftBy(Integer.parseInt(MoveByTextField.getText()));
        else if (MoveByTextField.getText().equals(""))
            JOptionPane.showConfirmDialog(null,"Enter Any Value"  , "No Input !!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        else
            JOptionPane.showConfirmDialog(null,"\"" + MoveByTextField.getText() + "\" Is Not A Number "  , "Invalid Input !!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    //Move down button
    private void MoveDownButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (MoveByTextField.getText().matches("\\d+"))
            MoveDownBy(Integer.parseInt(MoveByTextField.getText()));
        else if (MoveByTextField.getText().equals(""))
            JOptionPane.showConfirmDialog(null,"Enter Any Value"  , "No Input !!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        else
            JOptionPane.showConfirmDialog(null,"\"" + MoveByTextField.getText() + "\" Is Not A Number "  , "Invalid Input !!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    //Done in move
    private void DoneButtonActionPerformed(java.awt.event.ActionEvent evt) {
        MoveDialog.setVisible(false);
    }

    //Delete
    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (Canvas.getShapes().length != 0){
            Canvas.saveArray();
            Canvas.deleteShape(Canvas.getShapes()[ShapesList.getSelectedIndex()]);
            Canvas.redraw(PaintingCanvas.getGraphics());
            UpdateList();
            RedoButton.setEnabled(false);
            UndoButton.setEnabled(true);
        }
        else
            JOptionPane.showConfirmDialog(null, "Create A New Shape First !!  ",
                    "No Shapes Found!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    //Undo
    private void UndoButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Canvas.undo();
        Canvas.redraw(PaintingCanvas.getGraphics());
        UpdateList();
        RedoButton.setEnabled(true);
        if (Canvas.getUndoSize() == 0)
            UndoButton.setEnabled(false);
    }

    //Redo
    private void RedoButtonActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println(Canvas.getRedoSize());
        Canvas.redo();
        Canvas.redraw(PaintingCanvas.getGraphics());
        UpdateList();
        UndoButton.setEnabled(true);
        if (Canvas.getRedoSize() == 0)
            RedoButton.setEnabled(false);
    }

    //Draw
    private void DrawButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(!Canvas.getSupportedShapes().isEmpty()){
            try {
                Constructor<? extends Shape> constructor = Canvas.getSupportedShapes()
                        .get(PluginsList.getSelectedIndex()).getDeclaredConstructor();
                constructor.setAccessible(true);
                CurrentShape = constructor.newInstance();
                CurrentShapeProperties = CurrentShape.getProperties();
                prepareAddDialogBox();
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                     | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
            JOptionPane.showConfirmDialog(null, "Load A Plugin First !!  ", "No plugins Found!",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

    }

    //Load
    private void LoadButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            FileNameExtensionFilter jarFilter = new FileNameExtensionFilter("JAR files", "jar");
            jFileChooser1.setFileFilter(jarFilter);
            jFileChooser1.showOpenDialog(null);
            File pluginFile = jFileChooser1.getSelectedFile();
            String qualifiedClassName = JOptionPane.showInputDialog("Enter Qualified Class Name: ",
                    "Final.prog2.src");
            URL url = pluginFile.toURL();
            URL[] URLs = new URL[] {url};
            ClassLoader loader = new URLClassLoader(URLs);
            Class  myClass = Class.forName(qualifiedClassName, true, loader);

            Canvas.installToShape(myClass);
            PluginsList.removeAllItems();
            Canvas.getSupportedShapes().forEach((supportedShape) -> {
                PluginsList.addItem(supportedShape.getSimpleName());
            });

        } catch (MalformedURLException | ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initComponents() {

        //Variables declaration
        AddDialog = new javax.swing.JDialog();
        javax.swing.JPanel propertiesPanel = new javax.swing.JPanel();
        xLabel = new javax.swing.JLabel();
        yLabel = new javax.swing.JLabel();
        PropertiesLabel1 = new javax.swing.JLabel();
        PropertiesLabel2 = new javax.swing.JLabel();
        PropertiesLabel3 = new javax.swing.JLabel();
        PropertiesLabel4 = new javax.swing.JLabel();
        XTextField = new javax.swing.JTextField();
        YTextField = new javax.swing.JTextField();
        PropertiesTextField1 = new javax.swing.JTextField();
        PropertiesTextField2 = new javax.swing.JTextField();
        PropertiesTextField3 = new javax.swing.JTextField();
        PropertiesTextField4 = new javax.swing.JTextField();
        PluginsList = new javax.swing.JComboBox<>();
        javax.swing.JButton enterButton = new javax.swing.JButton();
        ColorsFrame = new javax.swing.JFrame();
        ColorChooser = new javax.swing.JColorChooser();
        javax.swing.JButton OKButton = new javax.swing.JButton();
        javax.swing.JButton cancelButton = new javax.swing.JButton();
        OutlineCheckBox = new javax.swing.JCheckBox();
        FillCheckBox = new javax.swing.JCheckBox();
        javax.swing.JButton applyButton = new javax.swing.JButton();
        MoveDialog = new javax.swing.JDialog();
        javax.swing.JButton moveRightButton = new javax.swing.JButton();
        MoveByTextField = new javax.swing.JTextField();
        javax.swing.JButton moveUpButton = new javax.swing.JButton();
        javax.swing.JButton moveLeftButton = new javax.swing.JButton();
        javax.swing.JButton moveDownButton = new javax.swing.JButton();
        JLabel moveByLabel = new JLabel();
        javax.swing.JButton doneButton = new javax.swing.JButton();
        PaintingCanvas = new implementCanvas();
        javax.swing.JButton circleButton = new javax.swing.JButton();
        ShapesList = new javax.swing.JComboBox<>();
        javax.swing.JButton editButton = new javax.swing.JButton();
        javax.swing.JButton moveButton = new javax.swing.JButton();
        UndoButton = new javax.swing.JButton();
        RedoButton = new javax.swing.JButton();
        javax.swing.JButton drawButton = new javax.swing.JButton();
        javax.swing.JButton loadButton = new javax.swing.JButton();
        jFileChooser1 = new javax.swing.JFileChooser();
        javax.swing.JButton copyButton = new javax.swing.JButton();
        javax.swing.JButton colorizeButton = new javax.swing.JButton();
        javax.swing.JButton deleteButton = new javax.swing.JButton();
        javax.swing.JButton lineSegmentButton = new javax.swing.JButton();
        javax.swing.JButton squareButton = new javax.swing.JButton();
        javax.swing.JButton ellipseButton = new javax.swing.JButton();
        javax.swing.JButton triangleButton = new javax.swing.JButton();
        javax.swing.JButton rectangleButton = new javax.swing.JButton();
        JLabel selectShapeLabel = new JLabel();
        javax.swing.JSeparator jSeparator1 = new javax.swing.JSeparator();

        //Properties panel
        AddDialog.setTitle("Please Enter Your Values:");
        propertiesPanel.setPreferredSize(new java.awt.Dimension(400, 150));
        xLabel.setText("x: ");
        yLabel.setText("y: ");
        PropertiesLabel1.setText("jLabel4");
        PropertiesLabel2.setText("jLabel4");
        PropertiesLabel3.setText("jLabel4");
        PropertiesLabel4.setText("jLabel4");

        //Panel text field in properties & Actions
        XTextField.setNextFocusableComponent(YTextField);
        XTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        YTextField.setNextFocusableComponent(PropertiesTextField1);
        YTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        PropertiesTextField1.setToolTipText("");
        PropertiesTextField1.setNextFocusableComponent(PropertiesTextField2);
        PropertiesTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        PropertiesTextField2.setToolTipText("");
        PropertiesTextField2.setNextFocusableComponent(PropertiesTextField3);
        PropertiesTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        PropertiesTextField3.setToolTipText("");
        PropertiesTextField3.setNextFocusableComponent(PropertiesTextField4);
        PropertiesTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });
        PropertiesTextField4.setToolTipText("");
        PropertiesTextField4.setNextFocusableComponent(enterButton);
        PropertiesTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        //Draw
        drawButton.setText("Draw");
        drawButton.setToolTipText("Draw Selected Shape");
        drawButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DrawButtonActionPerformed(evt);
            }
        });

        //Load
        loadButton.setText("Load");
        loadButton.setToolTipText("Load A New Shape");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadButtonActionPerformed(evt);
            }
        });

        //Plugins
        PluginsList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Plugin" }));
        PluginsList.setToolTipText("Choose A Loaded Plugin");
        enterButton.setText("Enter");
        enterButton.setToolTipText("Confirm Your Values");
        enterButton.setNextFocusableComponent(XTextField);
        enterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnterButtonActionPerformed(evt);
            }
        });
        enterButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {

            }
        });

        //Properties Panel Layout Control
        javax.swing.GroupLayout PropertiesPanelLayout = new javax.swing.GroupLayout(propertiesPanel);
        propertiesPanel.setLayout(PropertiesPanelLayout);
        PropertiesPanelLayout.setHorizontalGroup(PropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(PropertiesPanelLayout.createSequentialGroup()
        .addGap(26, 26, 26).addGroup(PropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(PropertiesLabel1).addComponent(PropertiesLabel2).addComponent(PropertiesLabel3).addComponent(PropertiesLabel4)
        .addComponent(yLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(xLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(PropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
        .addComponent(XTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
        .addComponent(YTextField, javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(PropertiesTextField1, javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(PropertiesTextField2, javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(PropertiesTextField3, javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(PropertiesTextField4, javax.swing.GroupLayout.Alignment.LEADING))
        .addGap(28, 28, 28).addComponent(enterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(40, Short.MAX_VALUE)));
         PropertiesPanelLayout.setVerticalGroup(PropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(PropertiesPanelLayout.createSequentialGroup().addContainerGap()
        .addGroup(PropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(XTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(xLabel)).addGroup(PropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(PropertiesPanelLayout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(PropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(YTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(yLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(PropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(PropertiesTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(PropertiesLabel1)))
        .addGroup(PropertiesPanelLayout.createSequentialGroup()
        .addGap(11, 11, 11).addComponent(enterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(PropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(PropertiesTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(PropertiesLabel2))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(PropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(PropertiesTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(PropertiesLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(PropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(PropertiesTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(PropertiesLabel4)).addGap(0, 0, Short.MAX_VALUE)));

        //Add Dialog Layout Control
        javax.swing.GroupLayout AddDialogLayout = new javax.swing.GroupLayout(AddDialog.getContentPane());
        AddDialog.getContentPane().setLayout(AddDialogLayout);
        AddDialogLayout.setHorizontalGroup(AddDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(AddDialogLayout.createSequentialGroup().addContainerGap()
        .addComponent(propertiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        AddDialogLayout.setVerticalGroup(AddDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(AddDialogLayout.createSequentialGroup().addContainerGap()
        .addComponent(propertiesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE).addContainerGap()));

        //Color Panel
        ColorsFrame.setTitle("Coloring");
        ColorsFrame.setResizable(false);
        OKButton.setText("Ok");
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });
        OutlineCheckBox.setText("Outline Color");
        FillCheckBox.setText("Fill Color");
        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApplyButtonActionPerformed(evt);
            }
        });

        //Colors Frame Layout Control
        javax.swing.GroupLayout ColorsFrameLayout = new javax.swing.GroupLayout(ColorsFrame.getContentPane());
        ColorsFrame.getContentPane().setLayout(ColorsFrameLayout);
        ColorsFrameLayout.setHorizontalGroup(ColorsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ColorsFrameLayout.createSequentialGroup()
        .addComponent(ColorChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, Short.MAX_VALUE)).addGroup(ColorsFrameLayout.createSequentialGroup().addContainerGap()
        .addComponent(OKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(applyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(OutlineCheckBox).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(FillCheckBox).addGap(73, 73, 73)));
        ColorsFrameLayout.setVerticalGroup(ColorsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(ColorsFrameLayout.createSequentialGroup()
        .addComponent(ColorChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(ColorsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(OKButton)
        .addGroup(ColorsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelButton)
        .addComponent(applyButton)).addGroup(ColorsFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(OutlineCheckBox)
        .addComponent(FillCheckBox))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        //Move Panel
        moveRightButton.setText("►");
        moveRightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoveRightButtonActionPerformed(evt);
            }
        });
        MoveByTextField.setFocusTraversalPolicyProvider(true);
        MoveByTextField.setNextFocusableComponent(doneButton);
        moveUpButton.setText("▲");
        moveUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoveUpButtonActionPerformed(evt);
            }
        });
        moveLeftButton.setText("◄");
        moveLeftButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoveLeftButtonActionPerformed(evt);
            }
        });
        moveDownButton.setText("▼");
        moveDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoveDownButtonActionPerformed(evt);
            }
        });
        moveByLabel.setText("Move By:");
        doneButton.setText("Done");
        doneButton.setFocusTraversalPolicyProvider(true);
        doneButton.setNextFocusableComponent(MoveByTextField);
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DoneButtonActionPerformed(evt);
            }
        });

        //Move Dialog Layout Control
        javax.swing.GroupLayout MoveDialogLayout = new javax.swing.GroupLayout(MoveDialog.getContentPane());
        MoveDialog.getContentPane().setLayout(MoveDialogLayout);
        MoveDialogLayout.setHorizontalGroup(MoveDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(MoveDialogLayout.createSequentialGroup().addGap(13, 13, 13)
        .addComponent(moveLeftButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(MoveDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(moveUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(moveDownButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGroup(MoveDialogLayout.createSequentialGroup()
        .addGroup(MoveDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(MoveByTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(moveByLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(moveRightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(doneButton).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
         MoveDialogLayout.setVerticalGroup(MoveDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(MoveDialogLayout.createSequentialGroup().addContainerGap()
        .addGroup(MoveDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
        .addComponent(doneButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(MoveDialogLayout.createSequentialGroup()
        .addComponent(moveUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(MoveDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(MoveDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(moveRightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(moveLeftButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MoveDialogLayout.createSequentialGroup().addComponent(moveByLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(MoveByTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(moveDownButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        //Main Panel
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Vector Based Drawing Application - FCDS - 2022 - RSAA");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(800, 400));
        setSize(new java.awt.Dimension(800, 400));
        PaintingCanvas.setBackground(new java.awt.Color(7, 56, 86));
        PaintingCanvas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(219, 227, 7)));
        PaintingCanvas.setForeground(new java.awt.Color(121, 9, 84));
        javax.swing.GroupLayout PaintingCanvasLayout = new javax.swing.GroupLayout(PaintingCanvas);
        PaintingCanvas.setLayout(PaintingCanvasLayout);
        //Weight
        PaintingCanvasLayout.setHorizontalGroup(PaintingCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 700, Short.MAX_VALUE));
        //Height
        PaintingCanvasLayout.setVerticalGroup(PaintingCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 500, Short.MAX_VALUE));

        //Buttons Panel
        moveButton.setText("Move");
        moveButton.setToolTipText("Adjust Selected Shape's Position");
        moveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoveButtonActionPerformed(evt);
            }
        });

        UndoButton.setText("Undo");
        UndoButton.setToolTipText("Undo Last Action");
        UndoButton.setEnabled(false);
        UndoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UndoButtonActionPerformed(evt);
            }
        });

        RedoButton.setText("Redo");
        RedoButton.setToolTipText("Redo Last Undo");
        RedoButton.setEnabled(false);
        RedoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RedoButtonActionPerformed(evt);
            }
        });

        copyButton.setText("Copy");
        copyButton.setToolTipText("Clone Selected Shape");
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CopyButtonActionPerformed(evt);
            }
        });

        colorizeButton.setText("Colorize");
        colorizeButton.setToolTipText("Colorize selected Shape");
        colorizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ColorizeButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.setToolTipText("Remove Selected Shape");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });

        //Shapes button
        ShapesList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose Shape" }));
        ShapesList.setToolTipText("Choose A Drawn shape");
        editButton.setText("Edit");
        editButton.setToolTipText("Adjust Selected Shape's Properties");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButtonActionPerformed(evt);
            }
        });

        circleButton.setText("     Circle     ");
        circleButton.setToolTipText("Create A New Circle");
        circleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CircleButtonActionPerformed(evt);
            }
        });

        lineSegmentButton.setText("     Line Segment     ");
        lineSegmentButton.setToolTipText("Create A New Line Segment");
        lineSegmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LineSegmentButtonActionPerformed(evt);
            }
        });

        squareButton.setText("     Square     ");
        squareButton.setToolTipText("Create A New Square");
        squareButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SquareButtonActionPerformed(evt);
            }
        });

        ellipseButton.setText("     Ellipse     ");
        ellipseButton.setToolTipText("Create A New Ellipse");
        ellipseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EllipseButtonActionPerformed(evt);
            }
        });

        triangleButton.setText("     Triangle     ");
        triangleButton.setToolTipText("Create A New Triangle");
        triangleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TriangleButtonActionPerformed(evt);
            }
        });

        rectangleButton.setText("     Rectangle     ");
        rectangleButton.setToolTipText("Create A New Rectangle");
        rectangleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RectangleButtonActionPerformed(evt);
            }
        });

        selectShapeLabel.setText("Select Shape");
        selectShapeLabel.setToolTipText("Why Are You Pointing Here :)");

        //Buttons Layout Control
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
        .addComponent(ShapesList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
        .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(moveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
        .addComponent(copyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(colorizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addComponent(selectShapeLabel).addGroup(layout.createSequentialGroup()
        .addComponent(UndoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(RedoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addComponent(PluginsList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jSeparator1))
        .addGroup(layout.createSequentialGroup()
        .addComponent(loadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(drawButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
        .addGroup(layout.createSequentialGroup().addComponent(circleButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(lineSegmentButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(squareButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(ellipseButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(triangleButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(rectangleButton))
        .addComponent(PaintingCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(circleButton).addComponent(lineSegmentButton)
        .addComponent(squareButton).addComponent(ellipseButton).addComponent(triangleButton)
        .addComponent(rectangleButton)).addGap(18, 18, 18))
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(loadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(drawButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(9, 9, 9)))
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
        .addComponent(PluginsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(10, 10, 10).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(UndoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(RedoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(selectShapeLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(ShapesList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(moveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(copyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addComponent(colorizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addComponent(PaintingCanvas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    //Main
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}