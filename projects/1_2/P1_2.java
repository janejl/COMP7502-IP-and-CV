/**
 * In this task 2 of project 1 you will implement the inverse fast Fourier transform
 * and perform second order (n=2) ButterWorth low pass filtering in the frequency domain.
 * You should reuse your implementation of the fast Fourier transform from the previous task of this project (P1_1).
 * <p>
 * In the filterImage() method add your code for the second order (n=2) ButterWorth low pass filtering.
 * <p>
 * Implement the inverse fast Fourier transform in the method inverseFourierTransfrom().
 * <p>
 * You may use methods declared in the class Complex.java for your convenience.
 * <p>
 * The solution file is provided for qualitative comparison. It was generated with d0=10, i.e., with the command
 * <p>
 * java P1_2 10
 * <p>
 * Output could be different because of differences in floating point arithmetic and differences in the way the rescaling is performed.
 **/

import java.time.Duration;
import java.time.Instant;

public class P1_2 {
    public P1_2(double d0) {
        Img img = new Img("ic512.png");
        Instant start = Instant.now();
        filterImage(img, d0);
        Instant stop = Instant.now();
        System.out.println("Elapsed time: " + Duration.between(start, stop).toMillis() + "ms");
        img.save();
    }

    public void filterImage(Img i, double d0) {
        Complex[] F = fastFourierTransfrom(i);
        //Your code here
        int origin_x = i.height / 2;
        int origin_y = i.width / 2;
        for (int u = 0; u < i.height; u++) {
            for (int v = 0; v < i.width; v++) {
                double D = Math.sqrt(Math.pow(u - origin_x, 2) + Math.pow(v - origin_y, 2));
                double H = 1 / (1 + Math.pow(D / d0, 4));
                F[u * i.width + v].mul(H);
            }
        }
        inverseFastFourierTransfrom(F, i);
    }

    private void inverseFastFourierTransfrom(Complex[] F, Img i) {
        //Your code here
        // conjugate F
        for (int u = 0; u < i.height; u++) {
            for (int v = 0; v < i.width; v++) {
                F[u * i.width + v].i = -F[u * i.width + v].i;
            }
        }

        // apply FFT to rows
        for (int u = 0; u < i.height; u++) {
            Complex[] row = new Complex[i.height];
            for (int v = 0; v < i.width; v++) {
                row[v] = F[u * i.width + v];
            }
            Complex[] ifft = FFT(row);
            for (int v = 0; v < i.width; v++) {
                F[u * i.width + v] = ifft[v];
            }
        }

        // apply FFT to columns
        for (int v = 0; v < i.width; v++) {
            Complex[] row = new Complex[i.width];
            for (int u = 0; u < i.height; u++) {
                row[u] = F[u * i.width + v];
            }
            Complex[] ifft = FFT(row);
            for (int u = 0; u < i.height; u++) {
                F[u * i.width + v] = ifft[u];
            }
        }

        // apply real value to the image
        for (int x = 0; x < i.height; x++) {
            for (int y = 0; y < i.width; y++) {
                Complex f = F[x * i.width + y];
                f.div(i.width * i.height);
                f.mul(Math.pow(-1, x + y));
                if (f.r < 0) f.r = 0;
                if (f.r > 255) f.r = 255;
                i.img[x * i.width + y] = (byte) f.r;
            }
        }
    }

    public Complex[] FFT(Complex[] input) {
        // The length of input should always be M = 2^p
        // For M = 8 points transform, K = M / 2 = 4:
        // - It split to 2 4-point transform, {F(0),F(2),F(4),F(6)} and {F(1),F(3),F(5),F(7)}.
        // - Each 4-point transform can be split to 2 2-point transform, {F(0),F(4)}, {F(2),F(6)}, {F(1),F(5)}, {F(3),F(7)}
        int M = input.length;
        Complex[] F_fft = new Complex[M];
        if (M == 1) {
            F_fft[0] = new Complex(input[0].r, input[0].i);
        } else {
            int K = M / 2;
            // get points with even or odd position separately to do FFT
            Complex[] evens = new Complex[K];
            Complex[] odds = new Complex[K];
            for (int x = 0; x < K; x++) {
                evens[x] = input[2 * x];
                odds[x] = input[2 * x + 1];
            }
            Complex[] F_even = FFT(evens);
            Complex[] F_odd = FFT(odds);

            // F_fft(u) = F_even(u) + F_odd(u) * W(u, 2K), F_fft(u+K) = F_even(u) - F_odd(u) * W(u, 2K)
            for (int u = 0; u < K; u++) {
                double theta = -2 * Math.PI * u / (double) M;
                Complex W = new Complex(Math.cos(theta), Math.sin(theta));
                W.mul(F_odd[u]);
                F_fft[u] = new Complex();
                F_fft[u].plus(F_even[u]);
                F_fft[u].plus(W);
                F_fft[u + K] = new Complex();
                F_fft[u + K].plus(F_even[u]);
                F_fft[u + K].minus(W);
            }
        }
        return F_fft;
    }

    public Complex[] fastFourierTransfrom(Img i) {
        //Change this code
        int M = i.width;
        int K = M / 2;
        Complex[] F = new Complex[i.width * i.height];
        Complex[] F_fft = new Complex[i.width * i.height]; // temporary store FFT result for every rows

        // Apply FFT to each row of image
        // Given one value of x, and v in [0, width-1], F_fft(x,v) is 1D-DFT of row x of f(x,y) using FFT
        for (int x = 0; x < i.height; x++) {
            Complex[] row = new Complex[i.width];
            for (int y = 0; y < i.width; y++) {
                row[y] = new Complex((double) (i.img[x * i.width + y] & 0xFF) * Math.pow(-1, x + y), 0);
            }
            Complex[] fft = FFT(row);
            for (int y = 0; y < i.width; y++) {
                F_fft[x * i.width + y] = fft[y];
            }
        }

        // Apply FFT for each column of temporary FFT
        for (int v = 0; v < i.width; v++) {
            Complex[] column = new Complex[i.height];
            for (int u = 0; u < i.height; u++) {
                column[u] = F_fft[u * i.width + v];
            }
            Complex[] fft = FFT(column);
            for (int u = 0; u < i.height; u++) {
                F[u * i.width + v] = fft[u];
            }
        }
        return F;
    }

    public static void main(String[] args) {
        new P1_2(Double.parseDouble(args[0]));
    }
}
