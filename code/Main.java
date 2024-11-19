import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static JFrame frame;
    private static ImagePanel imagePanel;

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Main <file_name> <square_size> <mode>");
            System.out.println("Mode: S (single-threaded) or M (multi-threaded)");
            return;
        }

        String fileName = args[0];
        int squareSize = Integer.parseInt(args[1]);
        String mode = args[2].toUpperCase();

        try {
            BufferedImage image = ImageIO.read(new File(fileName));
            System.out.println("Processing image: " + fileName);

            initializeGUI(image);

            long startTime = System.currentTimeMillis();

            if (mode.equals("S")) {
                System.out.println("Running single-threaded processing...");
                singleThreadedProcessing(image, squareSize);
            } else if (mode.equals("M")) {
                System.out.println("Running multi-threaded processing...");
                multiThreadedProcessing(image, squareSize);
            } else {
                System.out.println("Invalid mode. Use 'S' for single-threaded or 'M' for multi-threaded.");
                return;
            }

            File output = new File("result.jpg");
            ImageIO.write(image, "jpg", output);
            System.out.println("Processing complete. Result saved as result.jpg.");

            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime) / 1000; 
            System.out.println("Processing took: " + duration + " seconds.");

        } catch (Exception e) {
            System.err.println("Error processing image: " + e.getMessage());
        }
    }

    public static void singleThreadedProcessing(BufferedImage image, int squareSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y += squareSize) {
            for (int x = 0; x < width; x += squareSize) {
                averageColorBlock(image, x, y, squareSize);
                updateImageDisplay(image); 
                sleep(50);
            }
        }
    }

    public static void multiThreadedProcessing(BufferedImage image, int squareSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        int numThreads = 8;
        int regionHeight = height / numThreads;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            int yStart = i * regionHeight;
            int yEnd = (i == numThreads - 1) ? height : (i + 1) * regionHeight;

            executor.execute(() -> {
                for (int y = yStart; y < yEnd; y += squareSize) {
                    for (int x = 0; x < width; x += squareSize) {
                        averageColorBlock(image, x, y, squareSize);
                        updateImageDisplay(image); 
                        sleep(50); 
                    }
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all threads to finish
        }
    }

    public static void averageColorBlock(BufferedImage image, int x, int y, int squareSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        int xEnd = Math.min(x + squareSize, width);
        int yEnd = Math.min(y + squareSize, height);

        long rSum = 0, gSum = 0, bSum = 0;
        int count = 0;

        for (int i = y; i < yEnd; i++) {
            for (int j = x; j < xEnd; j++) {
                Color color = new Color(image.getRGB(j, i));
                rSum += color.getRed();
                gSum += color.getGreen();
                bSum += color.getBlue();
                count++;
            }
        }

        int avgR = (int) (rSum / count);
        int avgG = (int) (gSum / count);
        int avgB = (int) (bSum / count);
        Color avgColor = new Color(avgR, avgG, avgB);

        for (int i = y; i < yEnd; i++) {
            for (int j = x; j < xEnd; j++) {
                image.setRGB(j, i, avgColor.getRGB());
            }
        }

        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.BLACK); 
        g2d.drawRect(x, y, squareSize, squareSize); 
        g2d.dispose();
    }

    private static void initializeGUI(BufferedImage image) {
        frame = new JFrame("Image Processing Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imagePanel = new ImagePanel(image);
        frame.add(imagePanel);
        frame.setSize(800, 600); // Set window size to 800x600
        frame.setResizable(true); // Make the window resizable
        frame.setVisible(true);
    }

    private static void updateImageDisplay(BufferedImage image) {
        imagePanel.setImage(image);
        imagePanel.repaint();
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static class ImagePanel extends JPanel {
        private BufferedImage image;

        public ImagePanel(BufferedImage image) {
            this.image = image;
        }

        public void setImage(BufferedImage image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        }
    }
}


