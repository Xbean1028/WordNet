import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private Picture pic;
    private int width;
    private int height;

    //构造函数,初始化传入的图片以及图片的长宽
    public SeamCarver(Picture picture){
        if(picture == null){
            throw new IllegalArgumentException("No picture found in argument\n");
        }

        //初始化图片的宽度和高度并初始化一个picture对象
        width = picture.width();
        height = picture.height();
        pic = new Picture(picture);
    }

    //返回传入的图片
    public Picture picture() {
        return pic;
        //return new Picture(pic);
    }

    //返回图片的宽
    public int getWidth() {
        return width;
    }

    //返回图片的高
    public int getHeight(){
        return height;
    }

    //计算梯度值，像素点1和2的rgb值作为参数传入
    private double getGradient(int rgb1, int rgb2){
        //24位的像素值先右移得到对应的像素点1的r，g，b的值，
        // 再按位与，使其值在0-255的范围内
        int r1 = (rgb1>>16) & 0xFF; int r2 = (rgb2>>16) & 0xFF;
        int g1 = (rgb1>>8)  & 0xFF; int g2 = (rgb2>>8)  & 0xFF;
        int b1 = (rgb1>>0)  & 0xFF; int b2 = (rgb2>>0)  & 0xFF;

        double gradient = Math.pow(r1-r2,2)+Math.pow(g1-g2,2)+Math.pow(b1-b2,2);
        //返回梯度值
        return gradient;
    }

    //计算每个像素点的能量值并返回
    public double energy(int x,int y){
        if(x == 0 || x == width-1 || y == 0 || y == height-1){
            return 1000;
        }
        //找出传入像素点的上下左右的RGB值
        int up,down,right,left;
        up = pic.getRGB(x,y-1);
        down = pic.getRGB(x,y+1);
        right = pic.getRGB(x+1,y);
        left = pic.getRGB(x-1,y);
        //算出x，y坐标上对应的梯度值
        double gradientY = getGradient(up,down);
        double gradientX = getGradient(left,right);
        //返回该像素点的能量值，x轴上下像素点RGB差值的平方与Y轴上上下像素点差值平方的和的开方
        return Math.sqrt(gradientX+gradientY);
    }

    //此方法返回一个长为H的数组，是一列从图片中去除竖直方向的列序号
    public int[] findVerticalSeam() {
        double[][] energy = new double[width][height];
        //用二维矩阵存储每个像素点的能量值
        for(int row = 0;row<height;row++){
            for(int col = 0;col < width;col++){
                energy[col][row] = energy(col,row);
            }
        }

        //与该点相连的边的起点的坐标
        int [][]edgeTo = new int[width][height];
        //从最开始的起点到该点的边长
        double[][] distTo = new double[width][height];

        //遍历各个像素点，将距离矩阵的初始化为无穷大
        for(int row = 0;row<height;row++){
            for(int col = 0;col<width;col++){
                distTo[col][row] = Double.POSITIVE_INFINITY;
                if(row == 0) distTo[col][row] = energy[col][row];
            }
        }

        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width; col++) {
                if (distTo[col][row + 1] > distTo[col][row] + energy[col][row + 1]) {
                    distTo[col][row + 1] = distTo[col][row] + energy[col][row + 1];
                    edgeTo[col][row + 1] = col;
                }
                if (col - 1 > 0) {
                    if (distTo[col - 1][row + 1] > distTo[col][row] + energy[col - 1][row + 1]) {
                        distTo[col - 1][row + 1] = distTo[col][row] + energy[col - 1][row + 1];
                        edgeTo[col - 1][row + 1] = col;
                    }
                }
                if (col + 1 < width) {
                    if (distTo[col + 1][row + 1] > distTo[col][row] + energy[col + 1][row + 1]) {
                        distTo[col + 1][row + 1] = distTo[col][row] + energy[col + 1][row + 1];
                        edgeTo[col + 1][row + 1] = col;
                    }
                }
            }
        }

        int minCol = 0;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int col = 0; col < width; col++) {
            if (minEnergy > distTo[col][height - 1]) {
                minEnergy = distTo[col][height - 1];
                minCol = col;
            }
        }

        // construct VerticalSeam
        int[] vSeam = new int[height];
        int minRow = height - 1;
        while (minRow >= 0) {
            vSeam[minRow] = minCol;
            minCol = edgeTo[minCol][minRow--];
        }

        return vSeam;

    }

    //找出要去除的图片中的横向序列
    public int[] findHorizontalSeam() {
        transpose();
        int[] hSeam = findVerticalSeam();
        transpose();
        return hSeam;
    }

    //转置图形矩阵
    private void transpose() {
        Picture tmpPicture = new Picture(height, width);
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                tmpPicture.setRGB(col, row, pic.getRGB(row, col));
            }
        }
        pic = tmpPicture;
        int tmp = height;
        height = width;
        width = tmp;
    }

    //移除水平方向和竖直方向上的接缝
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("the argument to removeHorizontalSeam() is null\n");
        }
        if (seam.length != width) {
            throw new IllegalArgumentException("the length of seam not equal width\n");
        }
        validateSeam(seam);
        if (height <= 1) {
            throw new IllegalArgumentException("the height of the picture is less than or equal to 1\n");
        }

        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("the argument to removeVerticalSeam() is null\n");
        }
        if (seam.length != height) {
            throw new IllegalArgumentException("the length of seam not equal height\n");
        }
        validateSeam(seam);
        if (width <= 1) {
            throw new IllegalArgumentException("the width of the picture is less than or equal to 1\n");
        }

        Picture tmpPicture = new Picture(width - 1, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width - 1; col++) {
                validateColumnIndex(seam[row]);
                if (col < seam[row]) {
                    tmpPicture.setRGB(col, row, pic.getRGB(col, row));
                } else {
                    tmpPicture.setRGB(col, row, pic.getRGB(col + 1, row));
                }
            }
        }
        pic = tmpPicture;
        width--;
        //pic.show();
    }

    // 确保传入的竖直方向的索引在指定范围内
    private void validateColumnIndex(int col) {
        if (col < 0 || col > width -1) {
            throw new IllegalArgumentException("column index is out of range\n");
        }
    }

    private void validateRowIndex(int row) {
        if (row < 0 || row > height -1) {
            throw new IllegalArgumentException("row index is out of range\n");
        }
    }

    private void validateSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException("two adjacent entries differ by more than 1 in seam\n");
            }
        }
    }

    public static void main(String args[]){
        Picture pic = new Picture("seam02.jpg");
        SeamCarver s = new SeamCarver(pic);
        pic.show();
        s.removeVerticalSeam(s.findVerticalSeam());
        s.removeHorizontalSeam(s.findHorizontalSeam());
    }
}
