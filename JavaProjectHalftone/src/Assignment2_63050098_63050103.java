import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import javax.swing.*;

class Assignment2_63050098_63050103 {
    private JFrame frame;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Assignment2_63050098_63050103()::createAndShowGui); //We place our program on the EDT
    }
    
    private void createAndShowGui() {
        frame = new JFrame(getClass().getSimpleName());
        
        CirclePane circle = new CirclePane(5); //We want to create 5 circles, we may want to add more so we change it to 10, or whatever number we want
        
        frame.add(circle);
        frame.setTitle("Face From Young to Old");
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        (new Thread(circle)).start();
    } 
    
    //Data class
    class Circle {
        private Point coords;
        private int size;
        
        public Circle(Point coords, int size) {
            this.coords = coords;
            this.size = size;
        }
        
        public Point getCoords() {
            return coords;
        }
        public void setCoords(Point coords) {
            this.coords = coords;
        }
        public int getSize() {
            return size;
        }
        public void setSize(int size) {
            this.size = size;
        }
    }
    
    //The drawing class
    class CirclePane extends JPanel implements Runnable {
        private int numberOfCircles;
        private Circle[] circles;

        public CirclePane(int numberOfCircles) {
            this.numberOfCircles = numberOfCircles;
            
            circles = new Circle[numberOfCircles];
            
            for (int i = 0; i < numberOfCircles; i++) {
                Point coords = new Point((int) (Math.random() * 500) + 15, (int) (Math.random() * 450) + 15); //We generate random coords
                int size = (int) (Math.random() * 50) + 25; //And random sizes
                circles[i] = new Circle(coords, size); //Finally we create a new Circle with these properties
            }
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void paintComponent(Graphics g) {
            // super.paintComponent(g);     /* ******************** */
            Graphics2D g2d = (Graphics2D) g;
            
            for (int i = 0; i < numberOfCircles; i++) {
                g2d.draw(new Ellipse2D.Double(circles[i].getCoords().getX(), circles[i].getCoords().getY(), circles[i].getSize(), circles[i].getSize())); //We iterate over each circle in the array and paint it according to its coords and sizes
            }
        }
    }
}