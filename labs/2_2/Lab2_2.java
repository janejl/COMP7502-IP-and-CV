/**

In this task you will implement the method negativeTransformation of the class Lab2_2 which will apply the negative transformation on the image.  

The expected output is provided in the file solution.png.

You may use the following command to check if your output is identical to ours. 

cmp solution.png out.png

If this command has no output, it implies that your solution has produced the same file as ours.

**/

import java.util.Scanner;
public class Lab2_2 {
	public Lab2_2() {
		Img img = new Img("Fig0304a.png");
		negativeTransformation(img);
		img.save();
	}
	/**
     * Applies the negative transformation. 
     */
	public void negativeTransformation(Img i) {
		//Your code here
		for (int x = 0; x < i.img.length; ++x) {
			i.img[x] = (byte)(255 - (i.img[x] & 0xFF));
		}
	}
		
	public static void main(String[] args) {
		new Lab2_2();
	}
}
