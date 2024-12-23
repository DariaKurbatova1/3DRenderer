package com.daria;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import com.daria.Triangle;
import com.daria.Tetrahedron;
import com.daria.ShapeFactory;

//import Path2D;
public class DemoViewer {

    private static JPanel previousPanel; 
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());

        // slider to control horizontal rotation
        JSlider headingSlider = new JSlider(0, 360, 180);
        pane.add(headingSlider, BorderLayout.SOUTH);

        // slider to control vertical rotation
        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
        pane.add(pitchSlider, BorderLayout.EAST);

        //display welcome message
        JPanel welcomePanel = showIntro(pane, frame, headingSlider, pitchSlider);
        // panel to display render results
        
        
        pane.add(welcomePanel, BorderLayout.CENTER);

        frame.setSize(400, 400);
        frame.setVisible(true);

    }
    public static Color getShade(Color color, double shade) {
        double redLinear = Math.pow(color.getRed(), 2.4) * shade;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;
    
        int red = (int) Math.pow(redLinear, 1/2.4);
        int green = (int) Math.pow(greenLinear, 1/2.4);
        int blue = (int) Math.pow(blueLinear, 1/2.4);
    
        return new Color(red, green, blue);
    }
    public static JPanel showIntro(Container pane, JFrame frame, JSlider headingSlider, JSlider pitchSlider){
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the 3D Renderer!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        //start button to show renderer
        JButton startButton = new JButton("Start");
        welcomePanel.add(startButton, BorderLayout.SOUTH);

        startButton.addActionListener(e -> {
            //remove panel after start is clicked
            pane.remove(welcomePanel);
            previousPanel = welcomePanel;
            
            //show shape choices to user
            JPanel shapeChoicesPanel = showShapeChoices(pane, headingSlider, pitchSlider);
            pane.add(shapeChoicesPanel, BorderLayout.CENTER);

            pane.revalidate();
            pane.repaint();
        });

        return welcomePanel;
    }
    public static JPanel showShapeChoices(Container pane, JSlider headingSlider, JSlider pitchSlider) {
        JPanel shapeSelectionPanel = new JPanel();
        shapeSelectionPanel.setLayout(new BorderLayout());

        //create message
        JTextArea choiceLabel = new JTextArea("Please choose the shape you would like to render");
        choiceLabel.setLineWrap(true);
        choiceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        choiceLabel.setWrapStyleWord(true);
        
        shapeSelectionPanel.add(choiceLabel, BorderLayout.NORTH);

        //create buttons for shape choices
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        JButton tetrahedronButton = new JButton("Tetrahedron");
        JButton cubeButton = new JButton("Cube");
        JButton sphereButton = new JButton("Sphere");
        JButton backButton = new JButton("Back");

        buttonPanel.add(tetrahedronButton);
        buttonPanel.add(cubeButton);
        buttonPanel.add(sphereButton);
        shapeSelectionPanel.add(backButton, BorderLayout.SOUTH);

        //add button pannel to shape selection pannel
        shapeSelectionPanel.add(buttonPanel, BorderLayout.CENTER);

        backButton.addActionListener(e -> {
            pane.remove(shapeSelectionPanel);
            pane.add(previousPanel, BorderLayout.CENTER);

            pane.revalidate();
            pane.repaint();
        });

        tetrahedronButton.addActionListener(e -> {
            //remove choices and show tetrahedron
            pane.remove(shapeSelectionPanel);
            Tetrahedron tetrahedron = ShapeFactory.createTetrahedron();
            JPanel renderPanel = showTetrahedron(pane, shapeSelectionPanel, headingSlider, pitchSlider, tetrahedron);
            pane.add(renderPanel, BorderLayout.CENTER);

            headingSlider.addChangeListener(evt -> renderPanel.repaint());
            pitchSlider.addChangeListener(evt -> renderPanel.repaint());

            pane.revalidate();
            pane.repaint();
        });

        cubeButton.addActionListener(e -> {
            //remove choices and show cube
            pane.remove(shapeSelectionPanel);
            Cube cube = ShapeFactory.createCube();
            JPanel renderPanel = showCube(pane, shapeSelectionPanel, headingSlider, pitchSlider, cube);
            pane.add(renderPanel, BorderLayout.CENTER);

            headingSlider.addChangeListener(evt -> renderPanel.repaint());
            pitchSlider.addChangeListener(evt -> renderPanel.repaint());

            pane.revalidate();
            pane.repaint();
        });

        sphereButton.addActionListener(e -> {
            //add code to show sphere later
        });

        return shapeSelectionPanel;
    }


    public static JPanel showTetrahedron(Container pane, JPanel previous, JSlider headingSlider, JSlider pitchSlider, Tetrahedron tetrahedron){
        JPanel renderPanel = new JPanel() {
        public void paintComponent(Graphics graphics) {
            Graphics2D graphics2d = (Graphics2D) graphics;
            graphics2d.setColor(Color.BLACK);
            graphics2d.fillRect(0, 0, getWidth(), getHeight());

            //create a tetrahedron
            
            //heading for left-right rotation
            double heading = Math.toRadians(headingSlider.getValue());

            //pitch for up-down rotation
            double pitch = Math.toRadians(pitchSlider.getValue());

            //combining the matrixes by multipliying them
            Matrix3 transform = createTransformedMatrix(heading, pitch);

            BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

            //add z-buffer
            double[] zBuffer = createZBuffer(img);

            for (Triangle t : tetrahedron.triangles) {
                Vertex v1 = transform.transform(t.v1);
                Vertex v2 = transform.transform(t.v2);
                Vertex v3 = transform.transform(t.v3);

                //move vertices to the center of screen
                v1.x += getWidth() / 2;
                v1.y += getHeight() / 2;
                v2.x += getWidth() / 2;
                v2.y += getHeight() / 2;
                v3.x += getWidth() / 2;
                v3.y += getHeight() / 2;

                //shading
                //calculate edges
                Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
                Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);

                //callculate normal
                Vertex norm = new Vertex(
                    ab.y * ac.z - ab.z * ac.y,
                    ab.z * ac.x - ab.x * ac.z,
                    ab.x * ac.y - ab.y * ac.x
                );

                double normalLength = norm.getNormalLenght();
                //calculate cosine between triangle normal and light direction
                double angleCos = Math.abs(norm.z);

                //get shade
                Color shadedColor = getShade(t.color, angleCos);
            
                // compute rectangular bounds for triangle
                int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
                int maxX = (int) Math.min(img.getWidth() - 1,  Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
                int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
                int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
            
                double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
                //color pixels and shade
                for (int y = minY; y <= maxY; y++) {
                    for (int x = minX; x <= maxX; x++) {
                        double b1 = 
                          ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                        double b2 =
                          ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                        double b3 =
                          ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
                        if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {

                            double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                            int zIndex = y * img.getWidth() + x;
                            if (zBuffer[zIndex] < depth) {
                                img.setRGB(x, y, shadedColor.getRGB());
                                zBuffer[zIndex] = depth;
                            }
                        }
                    }
                }
            
            }
            
            graphics2d.drawImage(img, 0, 0, null);
            
            
            
        }
        
    };
    JButton backButton = new JButton("Back");
    renderPanel.add(backButton, BorderLayout.SOUTH);
    backButton.addActionListener(e -> {
        pane.remove(renderPanel);
        pane.add(previous, BorderLayout.CENTER);
        pane.revalidate();
        pane.repaint();
    });
    return renderPanel;
    }
    public static JPanel showCube(Container pane, JPanel previous, JSlider headingSlider, JSlider pitchSlider, Cube cube){
        JPanel renderPanel = new JPanel(){
            public void paintComponent(Graphics graphics){
                Graphics2D graphics2d = (Graphics2D) graphics;
                graphics2d.setColor(Color.BLACK);
                graphics2d.fillRect(0, 0, getWidth(), getHeight());

                //left-right rotation
                double heading = Math.toRadians(headingSlider.getValue());

                //up-down rotation
                double pitch = Math.toRadians(pitchSlider.getValue());

                //get transformed matrix
                Matrix3 transformed = createTransformedMatrix(heading, pitch);
                
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                double [] zBuffer = createZBuffer(img);

                for (Square s : cube.squares){
                    //corners of cube
                    Vertex v1 = transformed.transform(s.v1);
                    Vertex v2 = transformed.transform(s.v2);
                    Vertex v3 = transformed.transform(s.v3);
                    Vertex v4 = transformed.transform(s.v4);

                    //move vertices to the center of screen
                    v1.x += getWidth() / 2;
                    v1.y += getHeight() / 2;
                    v2.x += getWidth() / 2;
                    v2.y += getHeight() / 2;
                    v3.x += getWidth() / 2;
                    v3.y += getHeight() / 2;
                    v4.x += getWidth() / 2;
                    v4.y += getHeight() / 2;

                    //calculate edges for shading
                    Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
                    Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
                    //callculate normal
                    Vertex norm = new Vertex(
                        ab.y * ac.z - ab.z * ac.y,
                        ab.z * ac.x - ab.x * ac.z,
                        ab.x * ac.y - ab.y * ac.x
                    );

                    double normalLength = norm.getNormalLenght();
                    //calculate cosine between triangle normal and light direction
                    double angleCos = Math.abs(norm.z);

                    //get shade
                    Color shadedColor = getShade(s.color, angleCos);

                    //calculate bounds for square
                    int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, Math.min(v3.x, v4.x)))));
                    int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, Math.max(v3.x, v4.x)))));
                    int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, Math.min(v3.y, v4.y)))));
                    int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, Math.max(v3.y, v4.y)))));
                    
                    //square area
                    double squareArea = s.area();
                    //color pixels and shade
                    for (int y = minY; y <= maxY; y++) {
                        for (int x = minX; x <= maxX; x++) {
                            double b1 = 
                            ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / squareArea;
                            double b2 =
                            ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / squareArea;
                            double b3 =
                            ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / squareArea;
                            if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {

                                double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                                int zIndex = y * img.getWidth() + x;
                                if (zBuffer[zIndex] < depth) {
                                    img.setRGB(x, y, shadedColor.getRGB());
                                    zBuffer[zIndex] = depth;
                                }
                            }
                        }
                    }

                    }
                    graphics2d.drawImage(img, 0, 0, null);

            }
            
        };
        JButton backButton = new JButton("Back");
        renderPanel.add(backButton, BorderLayout.SOUTH);
        backButton.addActionListener(e -> {
            pane.remove(renderPanel);
            pane.add(previous, BorderLayout.CENTER);
            pane.revalidate();
            pane.repaint();
        });
        return renderPanel;
    }
    public static Matrix3 createTransformedMatrix(double heading, double pitch){
        //matrix for left-right rotation
        Matrix3 headingTransform = new Matrix3(new double[] {
            Math.cos(heading), 0, Math.sin(heading),
            0, 1, 0,
            -Math.sin(heading), 0, Math.cos(heading)
        });
        //matrix for up-down rotation
        Matrix3 pitchTransform = new Matrix3(new double[] {
            1, 0, 0,
            0, Math.cos(pitch), Math.sin(pitch),
            0, -Math.sin(pitch), Math.cos(pitch)
        });
        //multiply matrices
        Matrix3 transformed = headingTransform.multiply(pitchTransform);
        return transformed;
    }
    public static double[] createZBuffer(BufferedImage img){
        double[] zBuffer = new double[img.getWidth() * img.getHeight()];
        // initialize array with extremely far away depths
        for (int q = 0; q < zBuffer.length; q++) {
            zBuffer[q] = Double.NEGATIVE_INFINITY;
        }
        return zBuffer;
    }
    
}
