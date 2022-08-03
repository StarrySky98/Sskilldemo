package com.example.sskilldemo.utils;

import java.util.Scanner;

import static java.lang.Math.*;

/**
 * @author zhangkang88
 * @Description
 * @date 2022/7/23 18:54
 **/

public class Main {

    public static void main(String[] args) {
        int n;

        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for(int i=0;i<n;i++){
            a[i]=sc.nextInt();
            b[i]=sc.nextInt();
        }
        int ans = Count(a,b);
        System.out.println(ans);

    }

    private static int Count(int[] a, int[] b) {


        int res=0;
        int len=a.length;
        for(int i=0;i<len-2;i++){
            for(int j=i+1;j<len;j++){
                for(int k=j+1;k<len;k++){
                    if(gongxian(a[i],b[i],a[j],b[j],a[k],b[k])){
                        continue;
                    }
                    double b1=bianchang(a[i],b[i],a[j],b[j]);;
                    double b2=bianchang(a[i],b[i],a[k],b[k]);;
                    double b3=bianchang(a[k],b[k],a[j],b[j]);;
                    res=res+jd(b1,b2,b3);

                }
            }
        }
        return res;
    }

    private static boolean gongxian(int x1, int x2, int y1, int y2,int x3, int y3) {
        if((x2-x1)==0 || (x3-x1)==0)
            return false;
        if((y2-y1)/(x2-x1)==(y3-y1)/(x3-x1))
            return true;
        return false;
    }



    private static int bianchang(int x1, int x2, int y1, int y2) {
        return (int)Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    }

    private static int jd(double b1, double b2, double b3) {

        if(b1*b1+b2*b2==b3*b3){
            return 1;
        } else if (b1*b1 + b3*b3==b2*b2) {
            return 1;

        } else if (b2*b2 + b3*b3==b1*b1) {
            return 1;

        }
        return 0;
    }

    private static int[] chuang(int n, int[] nums) {
        int[] ans = new int[n];
        for(int i=0;i<n;i++){
//            ans[i]  = (int) Math.ceil(nums[i]*0.5*(1-Math.cos(2*Math.PI*(i)/n)));
            double x= nums[i]*0.5*(1- cos(2*Math.PI*(i)/n));
            int y;
            if(x>0){
                y=(int)(x+0.5);
            }else{
                y=(int)(x-0.5);
            }
            ans[i]=y;
        }
        return ans;
    }

}
