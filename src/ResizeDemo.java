import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class ResizeDemo {
    public static void main(String[] args) {
//        if (args.length != 3) {
//            StdOut.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");
//            return;
//        }

        Picture inputImg = new Picture("seam02.jpg");

        StdOut.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        SeamCarver sc = new SeamCarver(inputImg);
        //展示原图
        inputImg.show();

        //下面这两个参数就是去除多长的长和宽，直接赋值就可以了
        int removeColumns = 500;
        int removeRows = 80;//Integer.parseInt(args[2]);



        Stopwatch sw = new Stopwatch();


        for (int i = 0; i < removeRows; i++) {
            int[] horizontalSeam = sc.findHorizontalSeam();
            sc.removeHorizontalSeam(horizontalSeam);
        }


        for (int i = 0; i < removeColumns; i++) {
            int[] verticalSeam = sc.findVerticalSeam();
            sc.removeVerticalSeam(verticalSeam);
        }


        Picture outputImg = sc.picture();

        StdOut.printf("new image size is %d columns by %d rows\n", sc.getWidth(), sc.getHeight());

        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
        // inputImg.show();
        //裁剪过后的图
        outputImg.show();
        outputImg.save("test.jpg");
    }

    public String Resize(String filename,int width,int height) {
        Picture inputImg = new Picture(filename);

        //StdOut.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        SeamCarver sc = new SeamCarver(inputImg);
        //展示原图
        //inputImg.show();

        //下面这两个参数就是去除多长的长和宽，直接赋值就可以了
        int removeColumns = inputImg.width()-width;
        int removeRows = inputImg.height()-height;//Integer.parseInt(args[2]);

        Stopwatch sw = new Stopwatch();


        for (int i = 0; i < removeRows; i++) {
            int[] horizontalSeam = sc.findHorizontalSeam();
            sc.removeHorizontalSeam(horizontalSeam);
        }


        for (int i = 0; i < removeColumns; i++) {
            int[] verticalSeam = sc.findVerticalSeam();
            sc.removeVerticalSeam(verticalSeam);
        }


        Picture outputImg = sc.picture();

        StdOut.printf("new image size is %d columns by %d rows\n", sc.getWidth(), sc.getHeight());

        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
        // inputImg.show();
        //裁剪过后的图
        //outputImg.show();
        outputImg.save("test.jpg");

        return "test.jpg";
    }

}
