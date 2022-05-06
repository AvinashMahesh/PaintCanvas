
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * @author Avinash Mahesh Purdue CS
 * @version 11/17/2021
 */

public class Paint extends JComponent implements Runnable {

    private Image image;
    private Graphics2D graphics2D;
    private int curX; // current mouse x coordinate
    private int curY; // current mouse y coordinate
    private int oldX; // previous mouse x coordinate
    private int oldY; // previous mouse y coordinate
    private Paint paint;

    JButton clearButton;
    JButton fillButton;
    JButton eraseButton;
    JButton randomButton;

    JTextField hexText;
    JButton hexButton;
    JTextField rText;
    JTextField gText;
    JTextField bText;
    JButton rgbButton;

    private Color background;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == clearButton) {
                paint.clear();
                hexText.setText("#");
                rText.setText("");
                gText.setText("");
                bText.setText("");
            }
            if (e.getSource() == fillButton) {
                paint.fill();
                hexText.setText("#");
                rText.setText("");
                gText.setText("");
                bText.setText("");
            }
            if (e.getSource() == eraseButton) {
                paint.erase();
            }
            if (e.getSource() == randomButton) {
                Random random = new Random();
                int redColor = random.nextInt(256);
                int greenColor = random.nextInt(256);
                int blueColor = random.nextInt(256);
                Color color = new Color(redColor, greenColor, blueColor);
                paint.random(color);
                hexText.setText(String.format("#%02x%02x%02x", redColor, greenColor, blueColor));
                rText.setText(Integer.toString(redColor));
                gText.setText(Integer.toString(greenColor));
                bText.setText(Integer.toString(blueColor));
            }
            if (e.getSource() == hexButton) {
                try {
                    Color hexColor = Color.decode(hexText.getText());
                    rText.setText(String.valueOf(hexColor.getRed()));
                    gText.setText(String.valueOf(hexColor.getGreen()));
                    bText.setText(String.valueOf(hexColor.getBlue()));
                    paint.hex(hexColor);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Not a valid RGB Value", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (e.getSource() == rgbButton) {
                try {
                    int rC;
                    int gC;
                    int bC;
                    if (rText.getText() == null || rText.getText().equals("")) {
                        rC = 0;
                        rText.setText("0");
                    } else {
                        rC = Integer.parseInt(rText.getText());
                    }
                    if (gText.getText() == null || gText.getText().equals("")) {
                        gC = 0;
                        gText.setText("0");
                    } else {
                        gC = Integer.parseInt(gText.getText());
                    }
                    if (bText.getText() == null || bText.getText().equals("")) {
                        bC = 0;
                        bText.setText("0");
                    } else {
                        bC = Integer.parseInt(bText.getText());
                    }
                    Color rgbColor = new Color(rC, gC, bC);
                    hexText.setText(String.format("#%02x%02x%02x", rC, gC, bC));
                    paint.rgb(rgbColor);

                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "Not a valid RGB Value", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    };

    public void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        background = Color.white;
        repaint();
    }

    public void fill() {
        graphics2D.setPaint(graphics2D.getColor());
        background = graphics2D.getColor();
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    public void erase() {
        graphics2D.setPaint(background);
        repaint();
    }

    public void random(Color color) {
        graphics2D.setPaint(color);
    }

    public void hex(Color hexColor) {
        graphics2D.setPaint(hexColor);
    }

    public void rgb(Color rgbColor) {
        graphics2D.setPaint(rgbColor);
    }


    public Paint() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // set oldX and oldY coordinates to beginning mouse press
                oldX = e.getX();
                oldY = e.getY();
            }
        });


        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // set current coordinates to where mouse is being dragged
                curX = e.getX();
                curY = e.getY();

                // draw the line between old coordinates and new ones
                graphics2D.drawLine(oldX, oldY, curX, curY);

                // refresh frame and reset old coordinates
                repaint();
                oldX = curX;
                oldY = curY;

            }
        });

    }

    public void run() {
        JFrame frame = new JFrame("HW 12 Paint Challenge");

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        paint = new Paint();
        content.add(paint, BorderLayout.CENTER);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(actionListener);
        fillButton = new JButton("Fill");
        fillButton.addActionListener(actionListener);
        eraseButton = new JButton("Erase");
        eraseButton.addActionListener(actionListener);
        randomButton = new JButton("Random");
        randomButton.addActionListener(actionListener);

        hexText = new JTextField(10);
        hexText.setText("#");
        rText = new JTextField(5);
        gText = new JTextField(5);
        bText = new JTextField(5);
        hexButton = new JButton("Hex");
        hexButton.addActionListener(actionListener);
        rgbButton = new JButton("RGB");
        rgbButton.addActionListener(actionListener);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        JPanel topPanel = new JPanel();
        topPanel.add(clearButton);
        topPanel.add(fillButton);
        topPanel.add(eraseButton);
        topPanel.add(randomButton);
        content.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(hexText);
        bottomPanel.add(hexButton);
        bottomPanel.add(rText);
        bottomPanel.add(gText);
        bottomPanel.add(bText);
        bottomPanel.add(rgbButton);
        content.add(bottomPanel, BorderLayout.SOUTH);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Paint());
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);

            // this lets us draw on the image (ie. the canvas)
            //set brush size to 5
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setStroke(new BasicStroke(5));

            // gives us better rendering quality for the drawing lines
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // set canvas to white with default paint color
            graphics2D.setPaint(Color.white);
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            graphics2D.setPaint(Color.black);
            background = Color.white;
            repaint();
        }
        g.drawImage(image, 0, 0, null);
    }

}
