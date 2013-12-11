package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Emil
 * Date: 2013-12-07
 * Time: 23:58
 * To change this template use File | Settings | File Templates.
 */
public class GameModel {

    public enum Difficulty { HARD, HARDER, HARDEST };

    public static final char[] ALPHABET = generateAlphabet();

    // Hashmap with difficulty and corresponding timestep
    private static final Map<Difficulty, Integer> DIFFICULTY_TIME_MAP = new HashMap<>();
    static {
        DIFFICULTY_TIME_MAP.put(Difficulty.HARD, 1000);
        DIFFICULTY_TIME_MAP.put(Difficulty.HARDER, 500);
        DIFFICULTY_TIME_MAP.put(Difficulty.HARDEST, 250);
    }

    // The 'game-loop', sort of
    private final ScheduledExecutorService mService = Executors.newSingleThreadScheduledExecutor();

    // Game variables
    private Difficulty mDifficulty;
    private char mCurrent;
    private char mInput;

    public GameModel() {
        this(Difficulty.HARD);
    }

    public GameModel(Difficulty diff) {
        mDifficulty = diff;
    }

    /**
     * Starts the ScheduledExecutorService
     * @return boolean, true on success
     */
    public boolean start() {
        int timeStep = DIFFICULTY_TIME_MAP.get(mDifficulty);

        mService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println(new Date());
                setChar(getRandomCharacter());
            }
        }, 0, timeStep, TimeUnit.MILLISECONDS);

        return true;
    }

    // Shutdown
    public void stop() {
        mService.shutdown();
    }

    // Generates a random char from the alphabet
    private char getRandomCharacter() {
        int min = 0;
        int max = ALPHABET.length;
        int randomPos;

        // Could possibly have used ASCII numbers 61-95
        randomPos = min + (int)(Math.random() * ((max - min) + 1));

        return ALPHABET[randomPos];
    }

    /**
     * Generate the english alphabet
     * @return char[] a-z
     */
    private static char[] generateAlphabet() {
        char[] bet = new char[26];

        int i = 0;
        for(char alpha = 'A'; alpha <= 'Z'; alpha++) {
            bet[i] = alpha;
            i++;
        }

        return bet;
    }

    /*                                     *
     * Getters and setters can reside here *
     *                                     */

    public Difficulty getDifficulty() {
        return mDifficulty;
    }
    private void setChar(char newChar) {
        mCurrent = newChar;
    }

}

