package com.rudroid.spellingfun;

import android.os.Environment;
import android.util.Log;

import com.rudroid.utils.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by chaminda on 16/05/25.
 */
public class Utils {

    static final String TAG = "Utils";

    public static void loadCards(ArrayList<FlashCard> cardList) {

        File dir = Environment.getExternalStorageDirectory();
        File f = new File(dir, "/sdcard/Download/SpellingFun_0527.csv");

        try {
            File yourFile = new File("/sdcard/Download/", "SpellFun_0527.csv");
            String next[] = {};

            try {
                CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(yourFile)));
                while(true) {
                    next = reader.readNext();
                    if(next == null) {
                        break;
                    }
                    Log.d(TAG, next[0] + " : " + next[1]);
                    FlashCard card = new FlashCard();
                    card.Spelling = next[1];
                    card.Description = next[0];
                    cardList.add(card);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
