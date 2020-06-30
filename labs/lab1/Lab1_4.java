/**

In this you task will implement the method getMostFrequentIntensityValue that will return the most frequent intensity value in the image, i.e., the intensity that appears most often. 

Your program should output the following.

Most frequent intensity value is 194

**/
import java.util.HashMap;
import java.util.Scanner;
public class Lab1_4 {
	public Lab1_4() {
		Img img = new Img("Fig0314a.png");
		int i = getMostFrequentIntensityValue(img);
		System.out.println("Most frequent intensity value is "+i);
	}
	/**
	 * Retrieve the intensity value that occurs most often in the image
	 * @param img 
	 * @return the intensity value that occurs most often in the image
	 */
	public int getMostFrequentIntensityValue(Img i) {
		//Your code here
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (byte b : i.img) {
			int k = (int)(b & 0xFF);
			Integer count = map.get(k);
			map.put(k, count == null ? 1 : count+1);
		}
		int max = 0;
		int res = 0;
		for (int k : map.keySet()) {
			if (map.get(k) > max) {
				max = map.get(k);
				res = k;
			}
		}
		return res;
	}
	public static void main(String[] args) {
		new Lab1_4();
	}
}
