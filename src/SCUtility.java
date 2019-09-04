import java.awt.Color;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdRandom;

public class SCUtility {
    public static Picture randomPicture(int width,int height) {
        Picture picture = new Picture(width,height);
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                int r = StdRandom.uniform(255);
                int g = StdRandom.uniform(255);
                int b = StdRandom.uniform(255);
                Color color = new Color(r, g, b);
                picture.set(col, row, color);
            }
        }
        return picture;
    }

    //将图片转化为double类型的像素矩阵，返回该矩阵，调用seamcarving类的energy函数
    public static double[][] toEnergyMatrix(SeamCarver sc) {
        double[][] returnDouble = new double[sc.getWidth()][sc.getHeight()];
        for (int col = 0; col < sc.getWidth(); col++)
            for (int row = 0; row < sc.getHeight(); row++)
                returnDouble[col][row] = sc.energy(col, row);

        return returnDouble;
    }

    //调用toEnergyPicture函数，将转化后的图片展示出来
    public static void showEnergy(SeamCarver sc) {
        doubleToPicture(toEnergyMatrix(sc)).show();
    }

    //将传入的sc再调用灰度函数，将图片转为灰度图
    public static Picture toEnergyPicture(SeamCarver sc) {
        double[][] energyMatrix = toEnergyMatrix(sc);
        return doubleToPicture(energyMatrix);
    }

    //将图片的每个像素点的值控制在0-1内，使其图片为灰度图片，并返回图片
    public static Picture doubleToPicture(double[][] grayValues) {

        // each 1D array in the matrix represents a single column, so number
        // of 1D arrays is the width, and length of each array is the height
        int width = grayValues.length;
        int height = grayValues[0].length;

        Picture picture = new Picture(width, height);

        // 忽略边界的最大灰度值maximum grayscale value (ignoring border pixels)
        double maxVal = 0;
        for (int col = 1; col < width-1; col++) {
            for (int row = 1; row < height-1; row++) {
                if (grayValues[col][row] > maxVal)
                    maxVal = grayValues[col][row];
            }
        }

        if (maxVal == 0)
            return picture; //return black picture

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                //定义一个标准的灰度值，遍历每个像素点，与最大像素值相除，得到的浮点数作为新的像素值
                float normalizedGrayValue = (float) grayValues[col][row] / (float) maxVal;
                if (normalizedGrayValue >= 1.0f) normalizedGrayValue = 1.0f;
                picture.set(col, row, new Color(normalizedGrayValue, normalizedGrayValue, normalizedGrayValue));
            }
        }

        return picture;
    }

    //将裁剪的行或列像素序列覆盖成红色
    public static Picture seamOverlay(Picture picture, boolean horizontal, int[] seamIndices) {
        Picture overlaid = new Picture(picture.width(), picture.height());
        int width = picture.width();
        int height = picture.height();

        for (int col = 0; col < width; col++)
            for (int row = 0; row < height; row++)
                overlaid.set(col, row, picture.get(col, row));


        //如果传入的是横向的需要裁剪的序列，则遍历图片的纵列，并将对应的横向的像素点的值设成红色
        // if horizontal seam, then set one pixel in every column
        if (horizontal) {
            for (int col = 0; col < width; col++)
                overlaid.set(col, seamIndices[col], Color.RED);
        }
        // 如果是竖直的需要裁剪的序列，则将对应的像素点设为红色
        else  { // if vertical, put one pixel in every row
            for (int row = 0; row < height; row++)
                overlaid.set(seamIndices[row], row, Color.RED);
        }
        //返回图片
        return overlaid;
    }

}
