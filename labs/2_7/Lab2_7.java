/**

In this task you will implement the method histogramEqualization of the class Lab2_7 which will perform histogram equalization.  

The expected output is provided in the files solution1.png and solution2.png.

You may use the following command to check if your output is identical to ours. 

cmp solution1.png out.png

If this command has no output, it implies that your solution has produced the same file as ours.

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
	}
		
	public static void main(String[] args) {
		new Lab2_7();
	}
}
