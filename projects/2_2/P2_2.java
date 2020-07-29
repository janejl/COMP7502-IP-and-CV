/**
  * In this task you will implement the method gaussianSmooth of the class P2_2 which will apply 2D Gaussian smoothing to the image. 
  *
  * You should implement the 2D convolution using 1D masks (first x then y) for performance reasons. You should also output the size of the mask and the values used for the smoothing mask.
  * 
  * Note that you should cut off the Gaussian, as discussed in class. Consider the following input/output:
  *
  * Sigma: 0.5
  * Size: 3
  * Mask: [0.10650697891920077, 0.7869860421615985, 0.10650697891920077]
  *
  * Sigma: 1
  * Size: 7
  * Mask: [0.004433048175243746, 0.05400558262241449, 0.24203622937611433, 0.3990502796524549, 0.24203622937611433, 0.05400558262241449, 0.004433048175243746]
  *
  * Note that the mask is always symmetric and sums to one. 
  * Don't worry if you cannot generate the exact values. We will manually check the correctness of your solution. 
  * For simplicity, you should handle the boundary case simply by using the original intensities there.
  *
  * The solution files are provided for qualitative comparison. They have been generated with input 1 and 0.5. Output could be different because of differences in floating point arithmetic.
  **/
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
	}
	
		
	public static void main(String[] args) {
		new P2_2();
	}
}
