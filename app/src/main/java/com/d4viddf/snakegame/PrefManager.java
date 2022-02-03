package com.d4viddf.snakegame;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    Context context;

    PrefManager(Context context) {
        this.context = context;
    }

    public void setAge(int age) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Age", age);
        editor.commit();
    }

    public boolean isAge() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        boolean isAgeNotConfigure = sharedPreferences.getInt("Age",0) > 0;
        return isAgeNotConfigure;
    }

    public boolean isUserChild() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        int age = sharedPreferences.getInt("Age", 0);
        if (age < 13) return true;
        else return false;
    }

    public void setMaxPoint(int i){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("UserPuntuation", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Puntuation", i);
        editor.commit();
    }

    public int getMaxPoint(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPuntuation", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("Puntuation", 0);
    }
}
