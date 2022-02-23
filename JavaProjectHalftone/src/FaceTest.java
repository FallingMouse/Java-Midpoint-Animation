import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import java.util.Scanner;
import java.io.FileReader;

public class FaceTest extends JPanel implements Runnable {
    public static BufferedImage buffer;
    static Scanner fileInFace, fileInClockNumber, scn = new Scanner(System.in);
    static String fileInPathPerFrame;

    int numberFileIn = 1;

    double halftime = 0.0f;
    double circleMove = 0.0f;
    double squareRotate = 0.0f;
    double rotate1 = 0.0f, rotate2 = 0.0f;
    double x = 0, y = 550;

    double velocity = 50;
    double angle = -60;
    public static void main(String[] args) throws IOException {
		FaceTest m = new FaceTest();

		JFrame frame = new JFrame();
		frame.add(m);
		frame.setTitle("Animation Hell Yeah");
		frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

        (new Thread(m)).start();
	}
    public void run() {
        double currentTime, elapsedTime, keyFrameTime = 0;
        double lastTime = System.currentTimeMillis();

        while(true) {
            //control time
            currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - lastTime;
            lastTime = currentTime;
            
            //update
            rotate1 += 0.05 * elapsedTime / 1000.0;
            rotate2 += 0.6 * elapsedTime / 1000.0;
            // ^---- keyFrameTime % 2.5 == 0
            /* rotate1 += 0.10 * elapsedTime / 1000.0; 
            rotate2 += 1.2 * elapsedTime / 1000.0; */

            keyFrameTime += elapsedTime / 1000.0;
            /* if(keyFrameTime == 0.0f) {
                fileInPathPerFrame = "src/halftone/xyPosition/xyOfImage1.txt";
            } else if(keyFrameTime >= 2.5f && keyFrameTime < 2.6f) {
                fileInPathPerFrame = "src/halftone/xyPosition/xyOfImage2.txt";
            } else if(keyFrameTime >= 5.0f && keyFrameTime < 5.1f) {
                fileInPathPerFrame = "src/halftone/xyPosition/xyOfImage3.txt";
            } else if(keyFrameTime >= 7.5f && keyFrameTime < 7.6f) {
                fileInPathPerFrame = "src/halftone/xyPosition/xyOfImage4.txt";
            } else if(keyFrameTime >= 10.0f && keyFrameTime < 10.1f) {
                fileInPathPerFrame = "src/halftone/xyPosition/xyOfImage5.txt";
            } */
            
            
            if(keyFrameTime == 0.0f) {
                fileInPathPerFrame = "src/halftone/xyPosition/xyOfImage1.txt";
                halftime += 2.50f;
                numberFileIn++;
                System.out.println("Hello World " + keyFrameTime);
            } else if(keyFrameTime >= halftime) {
                fileInPathPerFrame = "src/halftone/xyPosition/xyOfImage" + numberFileIn + ".txt";
                halftime += 2.50f;
                numberFileIn++;
                System.out.println("Hello World");
            }
            /* halftime += 2.5f;
            numberFileIn++; */
            
            
            // System.out.println(rotate2);
            // System.out.println(keyFrameTime);

            //display
            repaint();
        }
    }
	@Override
	public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 600, 600);
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        // g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        g2.setColor(Color.black);
        try {
            fileInFace = new Scanner(new FileReader(fileInPathPerFrame));
            while(fileInFace.hasNextLine()) {
                int xc = fileInFace.nextInt();
                int yc = fileInFace.nextInt();
                int a = fileInFace.nextInt();
                int b = fileInFace.nextInt();
                midpointEllipse(g2, xc, yc, a, b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        g2.setColor(Color.black);
        try {
            fileInClockNumber = new Scanner(new FileReader("src/halftone/xyPosition/xyOfClockNumber.txt"));
            while(fileInClockNumber.hasNextLine()) {
                int xc = fileInClockNumber.nextInt();
                int yc = fileInClockNumber.nextInt();
                int a = fileInClockNumber.nextInt();
                fillMidpointCircle(g2, xc, yc, a);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //--clockwise--
        g2.rotate(rotate1, 300, 300);
        //small hand
        for (int i = 1; i < 35; i++)
        {
            fillMidpointCircle(g2, 300, (302 - (i * 2)), 4);
        }
        g2.rotate(rotate2, 300, 300);
        //minute hand
        for (int i = 1; i < 90; i++)
        {
            fillMidpointCircle(g2, 300, (302 - (i * 2)), 4);
        }

        fillMidpointCircle(g2, 300, 300, 6);
        g2.setColor(Color.WHITE);
        fillMidpointCircle(g2, 300, 300, 3);

    }
    public void midpointCircle(Graphics g, int xc, int yc, int r) {
        int x = 0;
        int y = r;
        int d = 1 - r;
        int dx = 2 * x;
        int dy = 2 * y;

        while(x <= y) {
            plot(g, x + xc, y + yc, 1);
            plot(g, -x + xc, y + yc, 1);
            plot(g, x + xc, -y + yc, 1);
            plot(g, -x + xc, -y + yc, 1);
            plot(g, y + xc, x + yc, 1);
            plot(g, -y + xc, x + yc, 1);
            plot(g, y + xc, -x + yc, 1);
            plot(g, -y + xc, -x + yc, 1);
            x++;

            dx += 2;

            d = d + dx + 1;

            if(d >= 0) {
                y--;
                dy -= 2;
                d = d - dy;
            }
        }
    }
    public void midpointEllipse(Graphics g, int xc, int yc, int a, int b) { //a, b = radius
        //region 1
        int x, y, d;
        x = 0;
        y = b;
        d = Math.round(b * b - a * a * b + a * a / 4);

        while(b * b * x <= a * a * y) {
            plot(g, x + xc, y + yc, 1);
            plot(g, -x + xc, y + yc, 1);
            plot(g, x + xc, -y + yc, 1);
            plot(g, -x + xc, -y + yc, 1);

            x++;
            d = d + 2 * b * b * x + b * b;

            if(d >= 0) {
                y--;
                d = d - 2 * a * a * y;
            }
        }

        //region 2
        x = a;
        y = 0;
        d = Math.round(a * a - b * b * a + b * b / 4);

        while(b * b * x >= a * a * y) {
            plot(g, x + xc, y + yc, 1);
            plot(g, -x + xc, y + yc, 1);
            plot(g, x + xc, -y + yc, 1);
            plot(g, -x + xc, -y + yc, 1);

            y++;
            d = d + 2 * a * a * y + a * a;

            if(d >= 0) {
                x--;
                d = d - 2 * b * b * x;
            }
        }
    }
    public void fillMidpointCircle(Graphics g, int xc, int yc, int r) {
        for(int i = 0; i <= r; i++) {
            midpointCircle(g, xc, yc, i);
        }
    }
    public void plot(Graphics g, int x, int y, int size) {
		g.fillRect(x, y, size, size);
	}
}
