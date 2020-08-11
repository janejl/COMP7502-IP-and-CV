/**
 * In this task you will implement the method gaussianSmooth of the class P2_2 which will apply 2D Gaussian smoothing to the image.
 * <p>
 * You should implement the 2D convolution using 1D masks (first x then y) for performance reasons.
 * You should also output the size of the mask and the values used for the smoothing mask.
 * <p>
 * Note that you should cut off the Gaussian, as discussed in class. Consider the following input/output:
 * <p>
 * Sigma: 0.5
 * Size: 3
 * Mask: [0.10650697891920077, 0.7869860421615985, 0.10650697891920077]
 * <p>
 * Sigma: 1
 * Size: 7
 * Mask: [0.004433048175243746, 0.05400558262241449, 0.24203622937611433, 0.3990502796524549, 0.24203622937611433, 0.05400558262241449, 0.004433048175243746]
 * <p>
 * Note that the mask is always symmetric and sums to one.
 * Don't worry if you cannot generate the exact values. We will manually check the correctness of your solution.
 * For simplicity, you should handle the boundary case simply by using the original intensities there.
 * <p>
 * The solution files are provided for qualitative comparison. They have been generated with input 1 and 0.5. Output could be different because of differences in floating point arithmetic.
 **/

import java.util.Arrays;
import java.util.Scanner;

public class P2_2 {
    public P2_2() {
        Img img = new Img("Fig0457.png");
        System.out.print("Sigma: ");
        Scanner in = new Scanner(System.in);
        double s = in.nextDouble();
        gaussianSmooth(img, s);
        img.save();
    }

    public void gaussianSmooth(Img i, double sigma) {
        //Your code here
        // calculate the mask
        int half = (int) (sigma * Math.sqrt(6 * Math.log(10)));
        int size = 1 + 2 * half;
        System.out.printf("Size: %d\n", size);
        double[] mask = new double[size];
        double mask_sum = 0;
        for (int k = 0; k <= half; k++) {
            double value = Math.exp(-k * k / (2 * sigma * sigma)) / (sigma * Math.sqrt(2 * Math.PI));
            mask_sum += (k == 0) ? value : value * 2;
            mask[half + k] = value;
            mask[half - k] = value;
        }
        // normalize the mask
        for (int k = 0; k < size; k++) {
            mask[k] = mask[k] / mask_sum;
        }
        System.out.printf("Mask: %s\n", Arrays.toString(mask));

        // 2D convolution using 1D mask in x
        double[] i_temp = new double[i.img.length];
        for (int x = 0; x < i.img.length; x++) {
            i_temp[x] = (double) (i.img[x] & 0xFF);
        }
        for (int x = half; x < i.height - half; x++) {
            for (int y = half; y < i.width - half; y++) {
                i_temp[x * i.width + y] = 0;
                for (int s = -half; s <= half; s++) {
                    i_temp[x * i.width + y] += (double) (i.img[(x + s) * i.width + y] & 0xFF) * mask[half - s];
                }
            }
        }

        // 2D convolution using 1D mask in y
        for (int x = half; x < i.height - half; x++) {
            for (int y = half; y < i.width - half; y++) {
                double f = 0;
                for (int s = -half; s <= half; s++) {
                    f += i_temp[x * i.width + y + s] * mask[half - s];
                }
                i.img[x * i.width + y] = (byte) f;
            }
        }
    }

    public static void main(String[] args) {
        new P2_2();
    }
}
