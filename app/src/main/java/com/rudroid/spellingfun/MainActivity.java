package com.rudroid.spellingfun;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;

    // Todo
    ArrayList<FlashCard> cardSet = new ArrayList<FlashCard>();
    FlashCard currentCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        for(int i=0; i<10; i++) {
        FlashCard card = new FlashCard();
        card.Spelling = "January";
        card.Description = "first month";
        cardSet.add(card);

        FlashCard card2 = new FlashCard();
        card2.Spelling = "February";
        card2.Description = "Second month";
        cardSet.add(card2);

        FlashCard card3 = new FlashCard();
        card3.Spelling = "March";
        card3.Description = "Third month";
        cardSet.add(card3);

//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tts = new TextToSpeech(this, this);
    }

    @Override
    protected void onPause() {

        tts.stop();
        tts.shutdown();
        Log.d("SF", "shutdown done...");
        super.onPause();
    }

    private void speakIt(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }



    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.UK);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
//                btnSpeak.setEnabled(true);
//                speakIt("Hello");
            }

        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }

    private void nextCard(int index) {
        currentCard = cardSet.get(index);

    }
}
