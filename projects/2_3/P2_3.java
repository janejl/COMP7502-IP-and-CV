/**
 * In this task you will implement the method cornerResponseImage of the class P2_3 which will change the image to the response map R of the Harris corner detector.
 * As usual, ignore the boundary.
 * Set pixels to 255 if R > threshold and otherwise set pixels to 0.
 * <p>
 * The solution files are provided for qualitative comparison. Output could be different because of differences in floating point arithmetic.
 **/

import java.util.Scanner;

public class P2_3 {
    public P2_3() {
        Img img = new Img("chessmat.png");
        System.out.print("Sigma: ");
        Scanner in = new Scanner(System.in);
        double s = in.nextDouble();
        System.out.print("Threshold: ");
        double t = in.nextDouble();
        cornerResponseImage(img, s, t);
        img.save();
    }

    public void cornerResponseImage(Img i, double sigma, double threshold) {
        //Your code here
        // 1. approximate fx and fy at each pixel using the mask and form three images: fx^2, fy^2, fxfy
        double[] fxfx = new double[i.img.length],
                fyfy = new double[i.img.length],
                fxfy = new double[i.img.length];
        int a = 1;
        int[] mask = new int[]{-1, 0, 1};
        for (int x = a; x < i.height - a; x++) {
            for (int y = a; y < i.width - a; y++) {
                double fx = 0, fy = 0;
                for (int s = -a; s <= a; s++) {
                    fx += (double) (i.img[(x - s) * i.width + y] & 0xFF) * mask[a - s];
                    fy += (double) (i.img[x * i.width + y - s] & 0xFF) * mask[a - s];
                }
                int idx = x * i.width + y;
                fxfx[idx] = fx * fx;
                fyfy[idx] = fy * fy;
                fxfy[idx] = fx * fy;
            }
        }

        // 2. apply Gaussian mask to fx^2, fy^2, fxfy and obtain smoothed derivatives <fx^2>, <fy^2>, <fxfy>
        gaussianSmooth(i.width, i.height, fxfx, sigma);
        gaussianSmooth(i.width, i.height, fyfy, sigma);
        gaussianSmooth(i.width, i.height, fxfy, sigma);

        // 3. form an image of R using the smoothed images of derivatives, R = det(A) - k * Trace(A) ^ 2
        double k = 0.04;
        for (int x = 0; x < i.height; ++x) {
            for (int y = 0; y < i.width; ++y) {
                int v = x * i.width + y;
                double R = (fxfx[v] * fyfy[v] - fxfy[v] * fxfy[v]) - k * Math.pow(fxfx[v] + fyfy[v], 2);
                i.img[x * i.width + y] = (R > threshold) ? (byte) 255 : (byte) 0;
            }
        }
    }

    public void gaussianSmooth(int width, int height, double[] img, double sigma) {
        // calculate the mask
        int half = (int) (sigma * Math.sqrt(6 * Math.log(10)));
        int size = 1 + 2 * half;
        double[] mask = new double[size];
        double mask_sum = 0;
        for (int k = 0; k <= half; ++k) {
            double value = Math.exp(-1 * k * k / (2 * sigma * sigma)) / (sigma * Math.sqrt(2 * Math.PI));
            mask_sum += (k == 0) ? value : value * 2;
            mask[half + k] = value;
            mask[half - k] = value;
        }
        // normalize the mask
        for (int k = 0; k < size; ++k) {
            mask[k] = mask[k] / mask_sum;
        }

        // 2D convolution using 1D mask in x
        double[] i_temp = new double[img.length];
        for (int x = half; x < height - half; ++x) {
            for (int y = half; y < width - half; ++y) {
                for (int s = -half; s <= half; s++) {
                    i_temp[x * width + y] += img[(x - s) * width + y] * mask[half - s];
                }
            }
        }

        // 2D convolution using 1D mask in y
        for (int x = half; x < height - half; ++x) {
            for (int y = half; y < width - half; ++y) {
                double f = 0;
                for (int s = -half; s <= half; s++) {
                    f += i_temp[x * width + y - s] * mask[half - s];
                }
                img[x * width + y] = f;
            }
        }
    }

    public static void main(String[] args) {
        new P2_3();
    }
}
