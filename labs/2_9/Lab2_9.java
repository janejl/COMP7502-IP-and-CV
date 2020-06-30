/**

In this task you will implement the method medianFilter of the class Lab2_9 which applies the median filter on the image. 

You should handle the boundary case by keeping the pixels unchanged. 

The expected output is provided in the files solution3.png and solution7.png, where the digit in the filename is the threshold. 

You may use the following command to check if your output is identical to ours. 

cmp solution7.png out.png

If this command has no output, it implies that your solution has produced the same file as ours.

**/

import java.util.Scanner;
import java.time.Instant;
import java.time.Duration;
public class Lab2_9 {
	public Lab2_9() {
		Img img = new Img("Fig0441.png");
		System.out.print("Size: ");
		Scanner in = new Scanner(System.in);
		int size = in.nextInt();
		Instant start = Instant.now();
		medianFilter(img, size);
		Instant stop = Instant.now();
		//System.out.println("Elapsed time: "+Duration.between(start, stop).toMillis()+"ms");
		img.save();
	}

	public void medianFilter(Img i, int size) {
		//Your code here
	}
		
	public static void main(String[] args) {
		new Lab2_9();
	}
}
