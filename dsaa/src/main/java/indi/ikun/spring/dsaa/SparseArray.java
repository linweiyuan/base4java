package indi.ikun.spring.dsaa;

import java.io.*;

/**
 * 稀疏数组压缩数组
 */
public class SparseArray {

    public static void main(String[] args) {

        //原始二维数组
        int[][] source=new int[11][7];
        source[1][2]=1;
        source[2][3]=2;
        source[3][3]=2;
        System.out.println("原始数组");
        display(source);
        System.out.println("压缩后的稀疏数组");
        display(compress(source));
        write(compress(source));
        System.out.println("解压缩后的原始数组");
        display(decompress(compress(source)));
    }

    public static void write(int[][] source){
        File file=new File("/disk/1tdisk/develop/java/base4java/bak.txt");

        try(FileWriter fileWriter=new FileWriter(file.getName()))
        {
            if(!file.exists()){
                file.createNewFile();
            }

            for (int i = 0; i < source.length; i++) {
                for (int j = 0; j <source[i].length ; j++) {
                    int tmp=source[i][j];
                    fileWriter.write(tmp);
                }
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static int[][] read(){
    return null;
    }




    public static void display(int[][] source){
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j <source[i].length ; j++) {
                System.out.printf("%d\t",source[i][j]);
            }
            System.out.println();
        }
    }

    public static int[][] compress(int[][] source){
        int val=0;
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                val+=source[i][j]==0?0:1;
            }
        }
        int[][] result=new int[val+1][3];

        result[0][0]=source.length;
        result[0][1]=source[0].length;
        result[0][2]=val;

        int row=1;

        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                if(source[i][j]!=0){
                    result[row][0]=i;
                    result[row][1]=j;
                    result[row][2]=source[i][j];
                    row++;
                }
            }
        }

        return result;
    }

    public static int[][] decompress(int[][] source){
        int[][] result=new int[source[0][0]][source[0][1]];
        for (int i = 1; i < source.length; i++) {
            result[source[i][0]][source[i][1]]=source[i][2];
        }

        return result;
    }
}