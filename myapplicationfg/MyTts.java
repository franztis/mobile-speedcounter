package com.example.myapplicationfg;

import android.content.Context;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class MyTts {
    private TextToSpeech tts;
    TextToSpeech.OnInitListener  initListenerg= new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status==TextToSpeech.SUCCESS){
                tts.setLanguage(Locale.ENGLISH);
            }
        }
    };
    public MyTts(Context contextd){
        tts = new TextToSpeech(contextd,initListenerg);
    }

    public void speak(String message){
        tts.speak(message,TextToSpeech.QUEUE_ADD,null,null);
    }
}
