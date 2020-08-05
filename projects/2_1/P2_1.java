/**
  * In this task you will implement the method gradientImage of the class P2_1 which will calculate the gradient image.
  *
  * To determine the gradient in images in x and y directions use the masks [-1 0 1]^T and [-1 0 1], respectively.
  *
  * Note that values should be scaled to [0, 255]. This can be done by multiplying with 1 / sqrt(2).
  *
  * The solution files are provided for qualitative comparison. Output could be different because of differences in floating point arithmetic.
  **/
public class P2_1 {
	public P2_1() {
		Img img = new Img("Fig0314a.png");
		gradientImage(img);
		img.save();
	}

	public void gradientImage(Img i) {
		//Your code here
		// 2 1-d gaussian smoothing
	}

	public static void main(String[] args) {
		new P2_1();
	}
}
