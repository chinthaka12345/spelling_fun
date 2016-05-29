package com.rudroid.spellingfun;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, ImageButton.OnClickListener, TextWatcher, OnGestureListener {

    private TextToSpeech tts;
    static final String Tag = "Spelling Main";

    // Todo
    ArrayList<FlashCard> cardSet = new ArrayList<FlashCard>();
    FlashCard currentCard;
    int currentIdx = 0;

    ImageButton spellingSpkBtn;
    ImageButton descSpkBtn;
    TextView description;
    EditText spelling;
    GestureDetector detector;
    Button spellHint;
    Button descHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spellingSpkBtn = (ImageButton) findViewById(R.id.spellSpeakBtn);
        descSpkBtn = (ImageButton) findViewById(R.id.descSpeakBtn);
        description = (TextView) findViewById(R.id.description);
        spelling = (EditText) findViewById(R.id.spelling);
        spellHint = (Button) findViewById(R.id.spellHint);
        descHint = (Button) findViewById(R.id.descHint);

        spelling.setPrivateImeOptions("nm");
        spellingSpkBtn.setOnClickListener(this);
        descSpkBtn.setOnClickListener(this);
        spelling.setOnClickListener(this);
        spelling.addTextChangedListener(this);
        spellHint.setOnClickListener(this);
        descHint.setOnClickListener(this);

        View view = this.findViewById(android.R.id.content);
        detector=new GestureDetector(this, this);

        Utils.loadCards(cardSet);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tts = new TextToSpeech(this, this);
        nextCard(currentIdx);
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
        spelling.setText("");
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

            case R.id.descHint:
                break;

            case R.id.spellHint:
                spellHintReq();
                break;

            default:
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        //Registering TouchEvent with GestureDetector
        return detector.onTouchEvent(event);
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
            spelling.setTextColor(Color.rgb(76, 153, 0));
        } else {
            spelling.setTextColor(Color.rgb(204, 0, 0));
        }

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        float sensitivity = 100;
        // Left swipe
        if(e1.getX() - e2.getX() > sensitivity){
            if(currentIdx < cardSet.size()-1) {
                currentIdx++;
            } else {
                currentIdx = 0;
            }
            nextCard(currentIdx);
            return true;
        }
        // Right swipe
        else if(e2.getX() - e1.getX() > sensitivity){
            if(currentIdx > 0) {
                currentIdx--;
            } else {
                currentIdx = cardSet.size()-1;
            }
            nextCard(currentIdx);
            return true;
        }

        return false;
    }

    private void init() {


    }

    private void spellHintReq() {

        int loopLen = 0;
        String hintWord = "";

        Log.d(Tag, "count "  + " : "  + spelling.getText().toString()) ;
        String lowerSpell = currentCard.Spelling.toLowerCase();
        String lowerInput = spelling.getText().toString().toLowerCase();

        if(lowerInput.compareTo(lowerSpell) == 0) {
            spellDone();
            return;
        }

        if(lowerInput.length() <= lowerSpell.length()) {
            loopLen = lowerInput.length();
        } else {
            loopLen = lowerSpell.length();
        }

        int idx = 0;
        for(idx=0; idx<loopLen; idx++) {
            if((lowerSpell.charAt(idx)) != lowerInput.charAt(idx)) {
                break;
            }
        }

        hintWord = lowerSpell.substring(0, idx+1);
        spelling.setText(hintWord);

        spelling.setSelection(idx+1);

    }

    private void spellDone() {
        tts.speak("You have done.", TextToSpeech.QUEUE_FLUSH, null);
    }
}
