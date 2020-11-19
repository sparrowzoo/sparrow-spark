package com.sparrow.spark;

import java.util.ArrayList;
import java.util.List;

public class getSameItems {
        public static void main(String[] args) {
            int []a=new int[]{1,2,3};
            int []b=new int[]{1,5,3};
            int []c=new int[]{1,2,4};
            System.out.print(getSameItems(a,b,c));
        }

        private static  boolean exist(int i,int[]array){
            for(int a:array){
                if(i==a){
                    return true;
                }
            }
            return false;
        }

        private static List<Integer> getSameItems(int [] first,int []seconds,int thired[]){
            List<Integer> result=new ArrayList();
            for(int i:first){
                if(!exist(i,seconds)){
                    continue;
                }
                if(exist(i,thired)){
                    result.add(i);
                }
            }
            return result;
        }
    }
