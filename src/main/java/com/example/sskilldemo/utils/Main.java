package com.example.sskilldemo.utils;


import java.util.Scanner;

/**
 * @author zhangkang88
 * @Description
 * @date 2022/7/23 18:54
 **/

public class Main {

    public static void main(String[] args){
        Scanner scan  = new Scanner(System.in);
        int n = scan.nextInt();
        int x= scan.nextInt();
        int y = scan.nextInt();
        int[] nums = new int[n];
        for(int i=0;i<n;i++){
            nums[i]=scan.nextInt();
        }
        sort(nums);
        if(n-y>x){
            System.out.println(nums[n-y-1]);
        }else{
            System.out.println(-1);
        }


    }
    public static void sort(int[] num){
        int len=num.length;
        for(int i=0;i<len;i++){
            for(int j=0;j<len-i-1;j++){
                if(num[j]>num[j+1]){
                    int temp = num[j];
                    num[j]=num[j+1];
                    num[j+1]=temp;
                }
            }
        }
    }
}
