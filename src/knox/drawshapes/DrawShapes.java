//main is at bottom of this class

package knox.drawshapes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import javax.swing.UIManager;


@SuppressWarnings("serial")
public class DrawShapes extends JFrame
{
    private enum ShapeType {
        SQUARE,
        CIRCLE,
        RECTANGLE
    }
    
    private DrawShapesPanel shapePanel;
    private Scene scene;
    private ShapeType shapeType = ShapeType.SQUARE;
    private Color color = Color.RED;
	//private Point startDrag;
    private JFrame dimensionsPanel;
    private int shapeWidth;
    private int shapeHeight;
    private static int numWindowsOpen=0;


    public DrawShapes(int width, int height) {
        setTitle("Draw Shapes!");
        scene=new Scene();
        shapeWidth=100;
        shapeHeight=200;
        
        // create our canvas, add to this frame's content pane
        shapePanel = new DrawShapesPanel(width,height,scene);
        this.getContentPane().add(shapePanel, BorderLayout.CENTER);
        this.setResizable(true);
        this.pack();
        this.setLocation(100,100);
        
        // Add key and mouse listeners to our canvas
        initializeMouseListener();
        initializeKeyListener();
        
        // initialize the menu options
        initializeMenu();

        // Handle closing the window.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                numWindowsOpen--;
                if(numWindowsOpen==0) {
                	System.exit(0);
                }
            }
        });
        
        //create dimensions window
        dimensionsPanel=new JFrame("Change Dimensions");
        dimensionsPanel.setVisible(false);
        dimensionsPanel.setLayout(new GridLayout(4,1));
        JPanel textPanel=new JPanel(new FlowLayout());
        textPanel.add(new JLabel("Height only affects rectangles. Width will be used as all four side lengths of a square and diameter of a circle."));
        dimensionsPanel.add(textPanel);
        JPanel labelPanel = new JPanel(new FlowLayout());
        JTextField widthField=new JTextField(10);
        JTextField heightField=new JTextField(10);
        labelPanel.add(new JLabel("Width"));
        labelPanel.add(new JLabel("Height"));
		dimensionsPanel.add(labelPanel);
		JPanel textBoxesPanel = new JPanel(new FlowLayout());
		textBoxesPanel.add(widthField);
		textBoxesPanel.add(heightField);
		dimensionsPanel.add(textBoxesPanel);
		JPanel okDimPanel = new JPanel(new FlowLayout());
		JButton okButton=new JButton("OK");
		okDimPanel.add(okButton);
		dimensionsPanel.add(okDimPanel);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Integer.parseInt(widthField.getText());
					Integer.parseInt(heightField.getText());
					if(Integer.parseInt(widthField.getText())<=0 || Integer.parseInt(heightField.getText())<=0)
						throw new NumberFormatException();
				} catch (NumberFormatException ex) {
					System.err.println("width and height fields must be positive ints");
					return;
				}
				shapeWidth=Integer.parseInt(widthField.getText());
				shapeHeight=Integer.parseInt(heightField.getText());
				dimensionsPanel.setVisible(false);
			}
			
		});
		
		dimensionsPanel.pack();
        
     // Handle closing the dimensions window.
        dimensionsPanel.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	dimensionsPanel.setVisible(false);
            }
        });
    }
    
    private void initializeMouseListener()
    {
        MouseAdapter a = new MouseAdapter() {
            
            public void mouseClicked(MouseEvent e)
            {
                System.out.printf("Mouse cliked at (%d, %d)\n", e.getX(), e.getY());
                
                if (e.getButton()==MouseEvent.BUTTON1) { 
                    if (shapeType == ShapeType.SQUARE) {
                        scene.addShape(new Square(color, 
                                e.getX(), 
                                e.getY(),
                                shapeWidth));
                    } else if (shapeType == ShapeType.CIRCLE){
                        scene.addShape(new Circle(color,
                                e.getPoint(),
                                shapeWidth));
                    } else if (shapeType == ShapeType.RECTANGLE) {
                        scene.addShape(new Rectangle(e.getPoint(),shapeWidth,shapeHeight,color));
                    }
                    
                } else if (e.getButton()==MouseEvent.BUTTON2) {
                    // apparently this is middle click
                } else if (e.getButton()==MouseEvent.BUTTON3){
                    // right right-click
                    Point p = e.getPoint();
                    System.out.printf("Right click is (%d, %d)\n", p.x, p.y);
                    List<IShape> selected = scene.select(p);
                    if (selected.size() > 0){
                        for (IShape s : selected){
                            s.setSelected(true);
                        }
                    } else {
                        for (IShape s : scene){
                            s.setSelected(false);
                        }
                    }
                    System.out.printf("Select %d shapes\n", selected.size());
                }
                repaint();
            }
            
            /* (non-Javadoc)
             * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
             */
            public void mousePressed(MouseEvent e)
            {
                System.out.printf("mouse pressed at (%d, %d)\n", e.getX(), e.getY());
                scene.startDrag(e.getPoint());
                
            }

            /* (non-Javadoc)
             * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
             */
            public void mouseReleased(MouseEvent e)
            {
                System.out.printf("mouse released at (%d, %d)\n", e.getX(), e.getY());
                scene.stopDrag();
                repaint();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.printf("mouse drag! (%d, %d)\n", e.getX(), e.getY());
                scene.updateSelectRect(e.getPoint());
                repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // TODO use this to grow/shrink shapes
            }
            
        };
        shapePanel.addMouseMotionListener(a);
        shapePanel.addMouseListener(a);
    }
    
    /**
     * Initialize the menu options
     */
    private void initializeMenu()
    {
        // menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // file menu
        JMenu fileMenu=new JMenu("File");
        menuBar.add(fileMenu);
        // load
        JMenuItem loadItem = new JMenuItem("Load");
        fileMenu.add(loadItem);
        loadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.out.println(e.getActionCommand());
                JFileChooser jfc = new JFileChooser(".");

                int returnValue = jfc.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    System.out.println(selectedFile.getAbsolutePath());
                    try {
                        scene.loadFromFile(selectedFile.getAbsolutePath());
                        repaint();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        // save
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.out.println(e.getActionCommand());
                JFileChooser jfc = new JFileChooser(".");

                // int returnValue = jfc.showOpenDialog(null);
                int returnValue = jfc.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    System.out.println(selectedFile.getAbsolutePath());
                    try {
                        scene.saveToFile(selectedFile.getAbsolutePath());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        // new
        JMenuItem itemNew = new JMenuItem ("Open new window");
        fileMenu.add(itemNew);
        itemNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                System.out.println(text);
                newWindow();
            }
        });
        fileMenu.addSeparator();
        // close all windows
        JMenuItem itemExit = new JMenuItem ("Close all windows");
        fileMenu.add(itemExit);
        itemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                System.out.println(text);
                System.exit(0);
            }
        });

        
        // OK, it's annoying to create menu items this way,
        // so let's use a helper method
        
        // color menu
        JMenu colorMenu = new JMenu("Color");
        menuBar.add(colorMenu);

        
        // red color
        addToMenu(colorMenu, "Red", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text=e.getActionCommand();
                System.out.println(text);
                // change the color instance variable to red
                color = new Color(255,0,0);
			}
		});
        
        // green color
        addToMenu(colorMenu, "Green", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text=e.getActionCommand();
                System.out.println(text);
                // change the color instance variable to green
                color = Color.GREEN;
			}
		});
        
        // blue color
        addToMenu(colorMenu, "Blue", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                System.out.println(text);
                // change the color instance variable to blue
                color = Color.BLUE;
            }
        });
        
        // custom color
        addToMenu(colorMenu, "Custom", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text=e.getActionCommand();
                System.out.println(text);
                // open custom color chooser
                Color prevColor=color;
                color=JColorChooser.showDialog(
                        DrawShapes.this,
                        "Choose Color",
                        color);
                if(color==null) {
                	color=prevColor;
                }
			}
		});
        
        // shape menu
        JMenu shapeMenu = new JMenu("Shape");
        menuBar.add(shapeMenu);
        
        // square
        addToMenu(shapeMenu, "Square", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Square");
                shapeType = ShapeType.SQUARE;
            }
        });
        
        // circle
        addToMenu(shapeMenu, "Circle", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Circle");
                shapeType = ShapeType.CIRCLE;
            }
        });
        
        // rectangle
        addToMenu(shapeMenu, "Rectangle", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Rectangle");
                shapeType = ShapeType.RECTANGLE;
            }
        });
        
        
        // operation mode menu
        JMenu operationModeMenu=new JMenu("Operation");
        menuBar.add(operationModeMenu);
        
        // scale up
        addToMenu(operationModeMenu, "Scale Up", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                System.out.println(text);
                scene.scale(1.5);
                repaint();
            }
        });
        
        // scale down
        addToMenu(operationModeMenu, "Scale Down", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                System.out.println(text);
                scene.scale(0.75);
                repaint();
            }
        });
        
        // move option
        addToMenu(operationModeMenu, "Move", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                // currently this just prints
                System.out.println(text);
            }
        });
        
        // change dimensions option
        addToMenu(operationModeMenu, "Change Dimensions", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                System.out.println(text);
                dimensionsPanel.setVisible(true);
            }
        });

        // set the menu bar for this frame
        this.setJMenuBar(menuBar);
    }
    
    // Awesome helper method!
    private void addToMenu(JMenu menu, String title, ActionListener listener) {
    	JMenuItem menuItem = new JMenuItem(title);
    	menu.add(menuItem);
    	menuItem.addActionListener(listener);
    }
    
    /**
     * Initialize the keyboard listener.
     */
    private void initializeKeyListener()
    {
        shapePanel.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
            	// Called when you push a key down
            	System.out.println("key pressed: " + e.getKeyChar());
            	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            		scene.moveSelected(-50, 0);
            	} else if (e.getKeyCode() == KeyEvent.VK_UP) {
            		scene.moveSelected(0, -50);
            	}
            	repaint();
            }
            public void keyReleased(KeyEvent e){
            	// Called when you release a key and it goes up
            	System.out.println("key released: " + e.getKeyChar());
            }
            public void keyTyped(KeyEvent e) {
            	// Gets called when you push a key down and then release it,
            	// without pushing any other keys in between
            	System.out.println("key typed: " + e.getKeyChar());
            	if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
            		scene.removeSelected();
            	}
            	repaint();
            }
        });
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        DrawShapes shapes=new DrawShapes(700, 600);
        shapes.setVisible(true);
        numWindowsOpen++;
    }
    
    public static void newWindow() {
    	DrawShapes shapes=new DrawShapes(700, 600);
        shapes.setVisible(true);
        numWindowsOpen++;
    }
}
