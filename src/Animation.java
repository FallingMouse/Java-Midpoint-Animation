import java.util.Scanner;
import java.awt.*;
import javax.swing.*;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Animation extends JPanel implements Runnable {
    static Scanner fileInFace, fileInClockNumber;
    
    double halftime = 0.0f;
    double rotate1 = 0.0f, rotate2 = 0.0f;
    
    String fileInPathPerFrame;

    public static void main(String[] args) throws IOException {
		Animation m = new Animation();

		JFrame frame = new JFrame();
		frame.add(m);
		frame.setTitle("Assignment2_63050098_63050103");
		frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

        (new Thread(m)).start();
	}
    public void run() {
        int numberFileIn = 0;
        double currentTime, elapsedTime, keyFrameTime = 0;
        double lastTime = System.currentTimeMillis();

        while(true) {
            //control time
            currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - lastTime;
            lastTime = currentTime;
            
            //update
            rotate1 += 0.1 * elapsedTime / 1000.0;
            rotate2 += 1.127 * elapsedTime / 1000.0;

            keyFrameTime += elapsedTime / 1000.0;
            
            if(keyFrameTime == 0.0f) {
                fileInPathPerFrame = "src/xyPosition/xyOfImage" + numberFileIn + ".txt";
                halftime += 0.625f;
                numberFileIn++;
                System.out.printf("Assignment2 ver %.1f.1\n", keyFrameTime);
            } else if(keyFrameTime >= halftime) {
                if(numberFileIn <= 40) {
                    fileInPathPerFrame = "src/xyPosition/xyOfImage" + numberFileIn + ".txt";
                    halftime += 0.625f;
                    numberFileIn++;
                } else {
                    fileInPathPerFrame = "src/xyPosition/xyOfImage40.txt";
                    
                    //Stop Thread
                    System.out.printf("Za Warudo!!!\nThread Killed!\n");
                    return;
                }
            }

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
        
        //--Face--
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
            fileInClockNumber = new Scanner(new FileReader("src/xyPosition/xyOfClockNumber.txt"));
            while(fileInClockNumber.hasNextLine()) {
                int xc = fileInClockNumber.nextInt();
                int yc = fileInClockNumber.nextInt();
                int a = fileInClockNumber.nextInt();
                fillMidpointCircle(g2, xc, yc, a);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //--Clockwise--
        g2.rotate(rotate1, 300, 300);
        
        for (int i = 1; i < 35; i++) {      //small hand
            fillMidpointCircle(g2, 300, (302 - (i * 2)), 4);
        }
        g2.rotate(rotate2, 300, 300);
        
        for (int i = 1; i < 90; i++) {      //minute hand
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
