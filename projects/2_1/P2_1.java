/**
 * In this task you will implement the method gradientImage of the class P2_1 which will calculate the gradient image.
 * <p>
 * To determine the gradient in images in x and y directions use the masks [-1 0 1]^T and [-1 0 1], respectively.
 * <p>
 * Note that values should be scaled to [0, 255]. This can be done by multiplying with 1 / sqrt(2).
 * <p>
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
        byte[] imgCopy = new byte[i.img.length];
        System.arraycopy(i.img, 0, imgCopy, 0, i.img.length);

        double scale = 1 / Math.sqrt(2);
        int a = 1;
        int[] mask = new int[]{-1, 0, 1};
        for (int x = a; x < i.height - a; x++) {
            for (int y = a; y < i.width - a; y++) {
                int fx = 0, fy = 0;
                for (int s = -a; s <= a; s++) {
                    fx += (int) (imgCopy[(x - s) * i.width + y] & 0xFF) * mask[a - s];
                    fy += (int) (imgCopy[x * i.width + y - s] & 0xFF) * mask[a - s];
                }
                i.img[x * i.width + y] = (byte) (Math.sqrt(fx * fx + fy * fy) * scale);
            }
        }
    }

    public static void main(String[] args) {
        new P2_1();
    }
}
