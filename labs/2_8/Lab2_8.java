/**
 * In this task you will implement the method boxFilter of the class Lab2_8 which applies the box smooth filter on the image i.
 * <p>
 * To pass the test case you should handle the boundary case by keeping the pixels unchanged.
 * <p>
 * The expected output is provided in the files solution3.png and solution7.png, where the digit in the filename is the threshold.
 * <p>
 * The solution files are provided for qualitative comparison. Output could be slightly different because of differences in floating point arithmetic.
 **/

import java.util.Scanner;
import java.time.Instant;
import java.time.Duration;

public class Lab2_8 {
    public Lab2_8() {
        Img img = new Img("Fig0441.png");
        System.out.print("Size: ");
        Scanner in = new Scanner(System.in);
        int size = in.nextInt();
        Instant start = Instant.now();
        boxFilter(img, size);
        Instant stop = Instant.now();
        System.out.println("Elapsed time: " + Duration.between(start, stop).toMillis() + "ms");
        img.save();
    }

    private void boxFilter(Img i, int size) {
        //Your code here
        int a = size / 2;
        double w = 1.0 / (size * size);
        byte[] imgCopy = new byte[i.img.length];
        System.arraycopy(i.img, 0, imgCopy, 0, i.img.length);
        for (int x = a; x < i.height - a; x++) {
            for (int y = a; y < i.width - a; y++) {
                double sum = 0;
                for (int s = -a; s <= a; s++) {
                    for (int t = -a; t <= a; t++) {
                        sum += w * (imgCopy[(x + s) * i.width + y + t] & 0xFF);
                    }
                }
                i.img[x * i.width + y] = (byte) Math.max(0, Math.min(255, sum));
            }
        }
    }

    public static void main(String[] args) {
        new Lab2_8();
    }
}
