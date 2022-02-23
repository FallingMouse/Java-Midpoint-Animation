package halftone;

import java.net.URL;

import java.util.Timer;
import java.util.TimerTask;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;

import javax.swing.JFrame;
import javax.swing.JComponent;

import javax.imageio.ImageIO;

import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;

public class Halftone extends JComponent {
    private static final long serialVersionUID = 1l;

    private BufferedImage image;
    private static final double SQ2 = Math.sqrt(2);
    private static final double SCALE = 3.0;
    private static final double DISP_SCALE = 1.2;

    public static Scanner scn = new Scanner(System.in);
    public static PrintWriter fileOut;

    public static void main(String[] args) throws IOException {
        fileOut = new PrintWriter("src/halftone/xyPosition/xyOfImage19.txt");
        
        JFrame frame = new JFrame();
        frame.add(new Halftone());
        // frame.pack();
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
    }

    public Halftone() {
        setDefaultImage();
        new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    repaint();
                }
            }, 1000l, 1000l);
    }

    private void setDefaultImage()  {
        try {
            URL res = Halftone.class.getResource("/halftone/ImageHT/Image5_3.png");
            // URL res = Halftone.class.getResource("/halftone/1.png");
            // URL res = Halftone.class.getResource("/halftone/lenna.png");
            setImage(ImageIO.read(res));
        } catch (java.io.IOException e) {
            System.out.println("Failed to fetch lenna.");
        }
    }
    public void setImage(BufferedImage display) {
        image = display;
        Dimension size = new Dimension((int) (image.getWidth() * DISP_SCALE),
                                       (int) (image.getHeight() * DISP_SCALE));
        AffineTransform scale
            = AffineTransform.getScaleInstance(1 / SCALE, 1 / SCALE);
        AffineTransformOp op
            = new AffineTransformOp(scale, AffineTransformOp.TYPE_BICUBIC);
        image = op.filter(image, null);
        // setPreferredSize(size);
        // setMinimumSize(size);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setColor(Color.WHITE);
        // g.setColor(Color.decode("#C1C1C1"));
        g.fillRect(0, 0, getWidth(), getHeight());

        /* Adjust the display. */
        /* g.scale(getWidth() / (2.0 * image.getWidth()),
                getHeight() / (2.0 * image.getHeight()));
        g.translate(1.0, 1.0); */

        /* Tune drawing parameters. */
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                           RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                           RenderingHints.VALUE_RENDER_QUALITY);

        /* Draw image. */
        g.setColor(Color.BLACK);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int c = image.getRGB(x, y);
                /* Use the luma model. */
                double sum = (+ 0.30 * (c >> 16 & 0xff)
                              + 0.59 * (c >>  8 & 0xff)
                              + 0.11 * (c >>  0 & 0xff)) / 255.0;
                sum = 1.0 - sum;
                double size = sum * SQ2;
                if(size * 3.5 != 0.0f) {
                   /*  g.fill(new Ellipse2D.Double((double) x * 3.5 - size,
                                                (double) y * 3.6 - size,
                                                size * 3.5, size * 3.5)); */
                    // g.drawOval((int)(Math.ceil(x * 3.5 - size)), (int)(Math.ceil(y * 3.6 - size)), (int)(Math.ceil(size * 3.5)), (int)(Math.ceil(size * 3.5)));
                    fillMidpointCircle(g, (int)(Math.ceil(x * 3.5 - size)), (int)(Math.ceil(y * 3.6 - size)), (int)(Math.ceil(size * 3.5)));
                // if(size * 3.5 != 0.0f) {
                    // fileOut.printf("midpointEllipse(g2, %d, %d, %d, %d);\n", (int)(Math.ceil(x * 3.5 - size)), (int)(Math.ceil(y * 3.6 - size)), (int)(Math.ceil(size * 3.5)), (int)(Math.ceil(size * 3.5)));
                    fileOut.printf("%d %d %d %d\n", (int)(Math.ceil(x * 3.5 - size)), (int)(Math.ceil(y * 3.6 - size)), (int)(Math.ceil(size * 3.5)), (int)(Math.ceil(size * 3.5)));
                    // fileOut.printf("%d %d %d\n", (int)(Math.ceil(x * 3.5 - size)), (int)(Math.ceil(y * 3.6 - size)), (int)(Math.ceil(size * 3.5)));
                }
            }
        }
        fileOut.close();
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
