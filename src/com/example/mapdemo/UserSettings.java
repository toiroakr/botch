package com.example.mapdemo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettings {
    Context context;
    private SharedPreferences preferences;
    public UserSettings(Context context, String name){
        this.context = context;
        preferences = context.getSharedPreferences(name ,Context.MODE_PRIVATE);
    }
    //Stringの値を書き込み
    public void WriteKeyValue(String key, String value){
        SharedPreferences.Editor preference_editor = preferences.edit();
        preference_editor.putString(key, value);
        preference_editor.commit();
    }
    //Hashmapの値を書き込み
    public void WriteKeyValues(HashMap<String, String> hashmap){
        SharedPreferences.Editor preference_editor = preferences.edit();
        Set<String> key_set = hashmap.keySet();
        Iterator<String> key_iterator = key_set.iterator();
        for(int i=0; i<hashmap.size(); i++){
            String current_key = key_iterator.next().toString();
            String current_value = hashmap.get(current_key).toString();
            preference_editor.putString(current_key, current_value);
        }
        preference_editor.commit();
    }
    //Stringの値を取得
    public String ReadKeyValue(String key){
        return preferences.getString(key, "");
    }
    //Stringの値を取得
    public HashMap<String, String> ReadKeyValues(String keys){
        HashMap<String, String> hashmap = new HashMap<String,String>();
        for(int i=0; i<keys.length(); i++){
            hashmap.put(keys, preferences.getString(keys, ""));
        }
        return hashmap;
    }
}