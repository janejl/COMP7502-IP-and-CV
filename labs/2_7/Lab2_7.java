/**
 * In this task you will implement the method histogramEqualization of the class Lab2_7 which will perform histogram equalization.
 * <p>
 * The expected output is provided in the files solution1.png and solution2.png.
 * <p>
 * You may use the following command to check if your output is identical to ours.
 * <p>
 * cmp solution1.png out.png
 * <p>
 * If this command has no output, it implies that your solution has produced the same file as ours.
 **/

import java.util.Scanner;

public class Lab2_7 {
    public Lab2_7() {
        Img img = new Img("Fig03161.png");
        histogramEqualization(img);
        img.save("out1.png");
        img = new Img("HawkesBay.png");
        histogramEqualization(img);
        img.save("out2.png");
    }

    public void histogramEqualization(Img i) {
        //Your code here
        // get cumulative histogram
        int[] T = cumulativeHistogram(i);
        // update color
        for (int x = 0; x < i.img.length; ++x) {
            i.img[x] = (byte) (T[i.img[x] & 0xFF] * 255.0 / i.img.length);
        }
    }

    // 2_6
    public int[] cumulativeHistogram(Img i) {
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
        new Lab2_7();
    }
}
