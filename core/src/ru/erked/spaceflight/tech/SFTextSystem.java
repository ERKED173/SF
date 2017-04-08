package ru.erked.spaceflight.tech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class SFTextSystem {

    private static String text;

    public SFTextSystem(boolean lang){
        if(lang){
            FileHandle textFile = Gdx.files.internal("lang/RU.txt");
            text = textFile.readString();
        }else{
            FileHandle textFile = Gdx.files.internal("lang/EN.txt");
            text = textFile.readString();
        }


    }

    public static String get(String key){
        String result = "null";
        for(int i=0;i<text.length()-key.length();i++){
            if(text.substring(i, i + key.length()).equals(key)){
                int j = i + key.length() + 1;
                while(!text.substring(j, j+1).equals("☺")){
                    j++;
                }
                result = text.substring(i + key.length() + 1, j);
            }
        }
        return result;
    }

}
