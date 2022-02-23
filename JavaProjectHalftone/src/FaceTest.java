import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FaceTest extends JPanel implements Runnable {
    public static BufferedImage buffer;
    static Scanner fileInFace, fileInClockNumber, scn = new Scanner(System.in);
    static String fileInPathPerFrame;

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

            keyFrameTime += elapsedTime / 1000.0;
            if(keyFrameTime == 0.0f) {
                fileInPathPerFrame = "src/halftone/xyOfImage1.txt";
            } else if(keyFrameTime >= 2.2f && keyFrameTime < 2.3f) {
                fileInPathPerFrame = "src/halftone/xyOfImage2.txt";
            } else if(keyFrameTime >= 4.2f) {
                fileInPathPerFrame = "src/halftone/xyOfImage4.txt";
            }
            
            //display
            repaint();
        }
    }
	@Override
	public void paintComponent(Graphics g) {
        /* BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics(); */
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 600, 600);
        /* g2.setColor(Color.BLACK);
        g2.drawOval((int)x, (int)y, 50, 50);
        // g2.drawOval((int)circleMove, 0, 100, 100);

        //change anchor point
        g2.rotate(squareRotate, 300, 300);
        g2.drawRect(200, 200, 200, 200); */
        
        // g.drawImage(buffer, 0, 0, null);
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
                // midpointCircle(g2, xc, yc, a);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        g2.setColor(Color.black);
        try {
            fileInClockNumber = new Scanner(new FileReader("src/halftone/xyOfClockNumber.txt"));
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
        for (int i = 1; i < 35; i++)
        {
            fillMidpointCircle(g2, (298 + (i * 2)), (302 - (i * 2)), 4);
        }

        g2.rotate(rotate2, 300, 300);
        for (int i = 1; i < 90; i++)
        {
            fillMidpointCircle(g2, 300, (302 - (i * 2)), 4);
        }

        fillMidpointCircle(g2, 300, 300, 6);
        g2.setColor(Color.WHITE);
        fillMidpointCircle(g2, 300, 300, 3);

        /* g2.setColor(Color.BLACK);
        for (int i = 1; i < 100; i++) {
            g2.rotate(rotate, 300, 300);
            midpointCircle(g2, (304 - (i * 4)), 300, (1 + (i/2)));
            // midpointCircle(g2, 300, (296 + (i * 4)), (1 + (i/2)));
            fillMidpointCircle(g2, (304 - (i * 4)), 300, (1 + (i/2)));
            // fillMidpointCircle(g2, 300, (296 + (i * 4)), (1 + (i/2)));
        } */
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
    public BufferedImage floodFill(BufferedImage m, int x, int y, Color targetColor, Color replacementColor) {
        Graphics2D g2 = m.createGraphics();
        
        Queue<Point> q = new LinkedList<Point>();

        g2.setColor(replacementColor);
        plot(g2, x, y, 1);
        q.add(new Point(x, y));

        while(!q.isEmpty()) {
            Point p = q.poll();

            //south
            if(p.y < 600 && new Color(m.getRGB(p.x, p.y + 1)).equals(targetColor)) {
                g2.setColor(replacementColor);
                plot(g2, p.x, p.y + 1, 1);
                q.add(new Point(p.x, p.y + 1));
            }
            //north
            if(p.y > 0 && new Color(m.getRGB(p.x, p.y - 1)).equals(targetColor)) {
                g2.setColor(replacementColor);
                plot(g2, p.x, p.y - 1, 1);
                q.add(new Point(p.x, p.y - 1));
            }
            //east
            if(p.x < 600 && new Color(m.getRGB(p.x + 1, p.y)).equals(targetColor)) {
                g2.setColor(replacementColor);
                plot(g2, p.x + 1, p.y , 1);
                q.add(new Point(p.x + 1, p.y));
            }
            //west
            if(p.x > 0 && new Color(m.getRGB(p.x - 1, p.y)).equals(targetColor)) {
                g2.setColor(replacementColor);
                plot(g2, p.x - 1, p.y , 1);
                q.add(new Point(p.x - 1, p.y));
            }
        } return m;
    }
    public void naiveLine(Graphics g, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        double m = dy / dx;
        double b = y1 - m * x1;

        for(int x = x1; x <= x2; x++) {
            int y = (int)Math.round(m * x + b);
            plot(g, x, y, 1);
        }
    }
    public void ddaLine(Graphics g, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        double m = dy / dx;

        double x, y;

        if(m <= 1 && m >= 0) {
            y = Math.min(y1, y2);
            for(x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
                y += m;
                plot(g, (int)Math.round(x), (int)Math.round(y), 1);
            }
        } else if(m >= -1 && m < 0) {
            y = Math.min(y1, y2);
            for(x = Math.max(x1, x2); x >= Math.min(x1, x2); x--) {
                y -= m;
                plot(g, (int)Math.round(x), (int)Math.round(y), 1);
            }
        }
        else if (m > 1) {
            x = Math.min(x1, x2);
            for(y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                x = x + 1 / m;
                plot(g, (int)Math.round(x), (int)Math.round(y), 1);
            }
        }
        else {
            x = Math.min(x1, x2);
            for(y = Math.max(y1, y2); y >= Math.min(y1, y2); y--) {
                x = x - 1 / m;
                plot(g, (int)Math.round(x), (int)Math.round(y), 1);
            }
        }
    }
    public void bresenhamLine(Graphics g, int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        boolean isSwap = false;

        if(dy > dx) {
            int tmp = dx;
            dx = dy;
            dy = tmp;
            isSwap = true;
        }

        int D = 2 * dy - dx;

        int x = x1;
        int y = y1;
        
        for(int i = 1; i <= dx; i++) {
            plot(g, x, y, 1);

            if(D >= 0) {
                if(isSwap) x += sx;
                else y += sy;

                D -= 2 * dx;
            }
            if(isSwap) y+= sy;
            else x += sx;

            D += 2 * dy;
        }
    }
    public void bezierCurve(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        for(int i = 0; i <= 1000; i++) {
            double t = i / 1000.0;

            int x = (int)(Math.pow((1 - t), 3) * x1 +
                    3 * t * Math.pow((1 - t), 2) * x2 +
                    3 * t * t * (1 - t) * x3 + 
                    t * t * t * x4);
            
            int y = (int)(Math.pow((1 - t), 3) * y1 +
                    3 * t * Math.pow((1 - t), 2) * y2 +
                    3 * t * t * (1 - t) * y3 + 
                    t * t * t * y4);

            plot(g, x, y, 3);
        }
    }
    public void plot(Graphics g, int x, int y, int size) {
		g.fillRect(x, y, size, size);
	}
}
