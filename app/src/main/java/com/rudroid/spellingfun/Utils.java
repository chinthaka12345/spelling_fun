package com.rudroid.spellingfun;

import android.os.Environment;
import android.util.Log;

import com.rudroid.utils.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

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

    // Get random number between min & max
    public static int randomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    // Implementing Fisher–Yates shuffle
    public static ArrayList<Integer> shuffleArray(int size)
    {
        ArrayList<Integer> shuffled = new ArrayList<>();
        for(int i=0; i<size; i++) {
            shuffled.add(i);
        }

        Random rnd = new Random();
        for (int i = size - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = shuffled.get(index);
            shuffled.set(index, shuffled.get(i));
            shuffled.set(i, a);
        }

        return shuffled;
    }
}
