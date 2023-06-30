package com.example.ezralendingapi.utils;


import org.springframework.stereotype.Service;

@Service
public class NamingSeries {


        public  String documentNamingSeries() {
            int n;
            n = 100;
            int sum = 0,num = 1;
            for(int i = 1 ;i <= n ;i++){
                sum = sum + num;
                num = num + 1;
            }
           return  "#"+sum;
        }

}
