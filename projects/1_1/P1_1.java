/**
 * In this task 1 of project 1 you will implement the fast Fourier transform and change the image to the Fourier spectrum in the method fourierSpectrum().
 * Your task is to implement the missing code in the method fastFourierTransform(). The implementation details of the FFT can be obtained in section 4.11 of our Textbook.
 * <p>
 * Use the log transformation and ensure that all values are in the range 0 ... 255.
 * <p>
 * You may use methods declared in the class Complex.java for your convenience and you may add new methods to that class if necessary.
 * <p>
 * The solution files are provided for qualitative comparison. Output could be different because of differences in floating point arithmetic and differences in the way the rescaling is performed.
 * <p>
 * For your reference, we are able generate the Fourier spectrum of the file rectangle1024.png in < 1 seconds.
 **/

import java.time.Duration;
import java.time.Instant;

public class P1_1 {
    public P1_1() {
        Img img = new Img("ic512.png");
        Instant start = Instant.now();
        fourierSpectrum(img);
        Instant stop = Instant.now();
        System.out.println("Elapsed time: " + Duration.between(start, stop).toMillis() + "ms");
        img.save();
    }

    public void fourierSpectrum(Img i) {
        Complex[] F = fastFourierTransfrom(i);
        double max = Double.NEGATIVE_INFINITY;
        for (int x = 0; x < F.length; x++)
            max = Math.max(F[x].getNorm(), max);
        for (int x = 0; x < i.img.length; x++)
            i.img[x] = (byte) (255 / Math.log(256) * Math.log(255 / max * F[x].getNorm() + 1));
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
        new P1_1();
    }
}
