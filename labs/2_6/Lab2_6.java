/**
 * In this task you will implement the method cumulativeHistogram of the class Lab2_6 which will return a histogram of the image.
 * <p>
 * The expected output is provided in the files solution1.png to solution4.png.
 * <p>
 * You may use the following command to check if your output is identical to ours.
 * <p>
 * cmp solution1.png out.png
 * <p>
 * If this command has no output, it implies that your solution has produced the same file as ours.
 **/

import java.util.Scanner;

public class Lab2_6 {
    public Lab2_6() {
        Img img = new Img("Fig03161.png");
        int[] h = cumulativeHistogram(img);
        img.saveHistogram(h);
    }

    public int[] cumulativeHistogram(Img i) {
        //Your code here
        int L = 256;
        int[] histogram = new int[L];
        for (int x = 0; x < i.img.length; ++x) {
            int v = (int) (i.img[x] & 0xFF);
            ++histogram[v];
        }
        for (int k = 1; k < L; ++k) {
            histogram[k] = histogram[k - 1] + histogram[k];
        }
        return histogram;
    }

    public static void main(String[] args) {
        new Lab2_6();
    }
}
