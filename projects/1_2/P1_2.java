/**

In this task 2 of project 1 you will implement the inverse fast Fourier transform and perform second order (n=2) ButterWorth low pass filtering in the frequency domain. You should reuse your implementation of the fast Fourier transform from the previous task of this project (P1_1).

In the filterImage() method add your code for the second order (n=2) ButterWorth low pass filtering.

Implement the inverse fast Fourier transform in the method inverseFourierTransfrom().

You may use methods declared in the class Complex.java for your convenience.

The solution file is provided for qualitative comparison. It was generated with d0=10, i.e., with the command

java P1_2 10

Output could be different because of differences in floating point arithmetic and differences in the way the rescaling is performed. 

**/

import java.time.Instant;
import java.time.Duration;
public class P1_2 {
	public P1_2(double d0) {
		Img img = new Img("ic512.png");
		Instant start = Instant.now();
		filterImage(img, d0);
		Instant stop = Instant.now();
		System.out.println("Elapsed time: "+Duration.between(start, stop).getSeconds()+"s");
		img.save();
	}

    public void filterImage(Img i, double d0) {
		Complex[] F = fastFourierTransfrom(i);
		//Your code here

		inverseFastFourierTransfrom(F, i);
    }

    public Complex[] fastFourierTransfrom(Img i) {
    	//Change this to your code from P1_1
    	Complex[] F = new Complex[i.width*i.height];
		for (int x=0;x<i.img.length;x++)
			F[x] = new Complex();
		return F;
	}

	private void inverseFastFourierTransfrom(Complex[] F, Img i) {
		//Your code here

	}

	public static void main(String[] args) {
		new P1_2(Double.parseDouble(args[0]));
	}
}
