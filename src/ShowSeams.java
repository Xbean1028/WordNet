import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class ShowSeams {
    //用红色标出水平接缝
    private static void showHorizontalSeam(SeamCarver sc) {
        Picture picture = SCUtility.toEnergyPicture(sc);
        int[] horizontalSeam = sc.findHorizontalSeam();
        Picture overlay = SCUtility.seamOverlay(picture, true, horizontalSeam);
        overlay.show();
    }

    //用红色标出竖向接缝
    private static void showVerticalSeam(SeamCarver sc) {
        Picture picture = SCUtility.toEnergyPicture(sc);
        int[] verticalSeam = sc.findVerticalSeam();
        Picture overlay = SCUtility.seamOverlay(picture, false, verticalSeam);
        overlay.show();
    }

    public static void main(String[] args) {
        Picture picture = new Picture("seam02.jpg");
        StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());
        picture.show();
        SeamCarver sc = new SeamCarver(picture);

        //展示水平接缝
        StdOut.printf("Displaying horizontal seam calculated.\n");
        showHorizontalSeam(sc);

        //展示竖向接缝
        StdOut.printf("Displaying vertical seam calculated.\n");
        showVerticalSeam(sc);

    }
}
