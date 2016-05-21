package com.rudroid.spellingfun;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, ImageButton.OnClickListener, TextWatcher{

    private TextToSpeech tts;
    static final String Tag = "Spelling Main";

    // Todo
    ArrayList<FlashCard> cardSet = new ArrayList<FlashCard>();
    FlashCard currentCard;

    ImageButton spellingSpkBtn;
    ImageButton descSpkBtn;
    TextView description;
    EditText spelling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spellingSpkBtn = (ImageButton) findViewById(R.id.spellSpeakBtn);
        descSpkBtn = (ImageButton) findViewById(R.id.descSpeakBtn);
        description = (TextView) findViewById(R.id.description);
        spelling = (EditText) findViewById(R.id.spelling);

        spellingSpkBtn.setOnClickListener(this);
        descSpkBtn.setOnClickListener(this);
        spelling.setOnClickListener(this);
        spelling.addTextChangedListener(this);

//        for(int i=0; i<10; i++) {
        FlashCard card = new FlashCard();
        card.Spelling = "January";
        card.Description = "First month of the year.";
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

        // // TODO: 16/05/22
        nextCard(0);
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
        description.setText(currentCard.Description);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.spellSpeakBtn:
                speakIt(currentCard.Spelling);
                break;

            case R.id.descSpeakBtn:
                speakIt(currentCard.Description);
                break;

            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(currentCard.Spelling.regionMatches(true, 0, spelling.getText().toString(), 0, spelling.getText().length())) {
            Log.d(Tag, "correct");
            spelling.setTextColor(Color.rgb(76, 153, 0));
        } else {
            spelling.setTextColor(Color.rgb(204, 0, 0));
        }

    }
}
