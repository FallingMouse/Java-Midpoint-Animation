import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

//63050103 นายแก้วเพชร หนูร่วง
public class GraphicSwing extends JPanel {
	public static void main(String[] args) {
		GraphicSwing m = new GraphicSwing();

		JFrame frame = new JFrame();
		frame.add(m);
		frame.setTitle("First Swing");
		frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	@Override
	public void paintComponent(Graphics g) {
        BufferedImage buffer = new BufferedImage(601, 601, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 600, 600);
        
        g2.setColor(Color.black);
        midpointCircle(g2, 300, 300, 100);
        fillMidpointCircle(g2, 300, 300, 100);

        midpointEllipse(g2, 300, 300, 200, 100);
        
        g.drawImage(buffer, 0, 0, null);
    }
    public void midpointCircle(Graphics g, int xc, int yc, int r) {
        int x = 0;
        int y = r;
        int d = 1 - r;
        int dx = 2 * x;
        int dy = 2 * y;

        while(x <= y) {
            plot(g, x + xc, y + yc, 3);
            plot(g, -x + xc, y + yc, 3);
            plot(g, x + xc, -y + yc, 3);
            plot(g, -x + xc, -y + yc, 3);
            plot(g, y + xc, x + yc, 3);
            plot(g, -y + xc, x + yc, 3);
            plot(g, y + xc, -x + yc, 3);
            plot(g, -y + xc, -x + yc, 3);
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
            plot(g, x + xc, y + yc, 3);
            plot(g, -x + xc, y + yc, 3);
            plot(g, x + xc, -y + yc, 3);
            plot(g, -x + xc, -y + yc, 3);

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
            plot(g, x + xc, y + yc, 3);
            plot(g, -x + xc, y + yc, 3);
            plot(g, x + xc, -y + yc, 3);
            plot(g, -x + xc, -y + yc, 3);

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
