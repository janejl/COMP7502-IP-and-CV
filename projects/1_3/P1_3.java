/**
 * In this task 3 of project 1 you will design an appropriate filter and apply it to the car image to attenuate
 * the impulse-like bursts in the image. You may reuse existing code from your tasks 1 and 2.
 * <p>
 * Hint: Note that the spatial resolution of the car.png is not a power of 2, i.e., it is not of the form 2^m x 2^n.
 * You should create a new suitable image with padded black pixels by changing code in the constructor of the class P1_3.
 * <p>
 * To avoid reverse engineering, we do not provide a sample solution for this task.
 **/

import java.time.Duration;
import java.time.Instant;

public class P1_3 {
    public P1_3() {
        //Change this code.
        Img img = new Img("car.png");
        Instant start = Instant.now();

        // add padding
        int W = img.width;
        int H = img.height;
        int size = Math.max(W, H);
        int p = 0;
        while (size > 0) {
            size = size >> 1;
            p++;
        }
        int new_size = (int) Math.pow(2, p);
        byte[] img_extend = new byte[new_size * new_size];
        for (int x = 0; x < new_size; x++) {
            for (int y = 0; y < new_size; y++) {
                img_extend[x * new_size + y] = (x < H && y < W) ? img.img[x * W + y] : (byte) 0;
            }
        }
        img.width = new_size;
        img.height = new_size;
        img.img = img_extend;

        filterImage(img);

        // remove padding
        byte[] img_shrink = new byte[H * W];
        for (int x = 0; x < H; x++) {
            for (int y = 0; y < W; y++) {
                img_shrink[x * W + y] = img.img[x * new_size + y];
            }
        }
        img.img = img_shrink;
        img.width = W;
        img.height = H;

        Instant stop = Instant.now();
        System.out.println("Elapsed time: " + Duration.between(start, stop).getSeconds() + "s");
        img.save();
    }

    public double butterWorthNorthFilter(int M, int N, int u, int v, int center_x, int center_y, double D0, int n) {
        double D = Math.sqrt(Math.pow(u - center_x, 2) + Math.pow(v - center_y, 2));
        double Dk = Math.sqrt(Math.pow(u - M / 2 - center_x, 2) + Math.pow(v - N / 2 - center_y, 2));
        double D_k = Math.sqrt(Math.pow(u - M / 2 + center_x, 2) + Math.pow(v - N / 2 + center_y, 2));
        double H = (1 + Math.pow(D0 / Dk, n)) * (1 + Math.pow(D0 / D_k, n));
        return 1 / H;
    }

    public void filterImage(Img i) {
        Complex[] F = fastFourierTransfrom(i);
        //Your code here
        int M = i.height;
        int N = i.width;
        // Notch Filter
        for (int u = 0; u < i.height; u++) {
            for (int v = 0; v < i.width; v++) {
                double H = butterWorthNorthFilter(M, N, u, v, 42, 45, 10, 5);
                H *= butterWorthNorthFilter(M, N, u, v, 42, -45, 10, 5);
                H *= butterWorthNorthFilter(M, N, u, v, 84, 42, 10, 4);
                H *= butterWorthNorthFilter(M, N, u, v, 84, -42, 10, 4);
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
        new P1_3();
    }
}
