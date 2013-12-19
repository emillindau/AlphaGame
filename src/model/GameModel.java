package model;

import event.ChangeListener;
import event.ChangeObserver;

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

    private static final int MAX_NUMBER_OF_FAILS = 3;

    // Hashmap with difficulty and corresponding timestep
    private static final Map<Difficulty, Integer> DIFFICULTY_TIME_MAP = new HashMap<>();
    static {
        DIFFICULTY_TIME_MAP.put(Difficulty.HARD, 1000);
        DIFFICULTY_TIME_MAP.put(Difficulty.HARDER, 500);
        DIFFICULTY_TIME_MAP.put(Difficulty.HARDEST, 250);
    }

    // The 'game-loop', sort of
    private final ScheduledExecutorService mService;

    // The eventlistener/delegator
    private ChangeObserver mChangeObserver;

    // Game variables
    private Difficulty mDifficulty;

    private char mCurrent;
    private char mInput;
    private int mFails;

    // Boolean checks
    private boolean mHasInput;
    private boolean mFirstRun;
    private boolean mGameOver;

    public GameModel(ScheduledExecutorService service) {
        this(Difficulty.HARD, service);
    }

    public GameModel(Difficulty diff, ScheduledExecutorService service) {
        mDifficulty = diff;
        mService = service;
        mChangeObserver = new ChangeObserver();
        mFails = 0;
        mHasInput = false;
        mFirstRun = true;
        mGameOver = false;
    }

    private void reset() {
        stop();
        mFails = 0;
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
                // System.out.println(new Date());
                System.out.println("-- Debug --> " + "Fails: " + mFails + ", " + "Input: " + mHasInput + ", FirtRun: " + mFirstRun + ", CurrentChar: " + mCurrent + ", GameOver: " + mGameOver);

                // Before we change char, check if there's been input
                // (or rather no input)
                // also.. Should not take it into account first run
                if(!mFirstRun) {
                    if(!mHasInput) {
                        fail();
                    }
                } else {
                    mFirstRun = false;
                }

                mHasInput = false;

                if(!mGameOver) {
                    setChar(getRandomCharacter());
                    // Notify listeners about change
                    mChangeObserver.change(mCurrent);
                }

            }
        }, 0, timeStep, TimeUnit.MILLISECONDS);

        return true;
    }

    // Shutdown
    public void stop() {
        // mService.shutdownNow();
        mService.shutdown();
    }

    // Take input - String for more easiness in usage, casting happens here
    public void input(String key) {

        // Just one guess per change
        if(mHasInput) {
            return;
        }

        mHasInput = true;
        if(key.length() == 1) {

            key = key.toUpperCase();
            char in = key.charAt(0);

            if(in > 64 && in < 92) {
                mInput = in;

                // Check input against generated char
                if(mInput != mCurrent) {
                    // Failed
                    fail();
                }

            } else {
                fail();
                throw new IllegalArgumentException();
            }

        } else {
            fail();
            throw new IllegalArgumentException();
        }
    }

    /**
     * Called when input fails against
     * generated char. Also invokes
     * gameover
     */
    private void fail() {
        mFails++;
        if(mFails > MAX_NUMBER_OF_FAILS) {
            gameOver();
        }
    }

    /**
     * Called when game over
     * resets and tells listeners
     * about new state
     */
    private void gameOver() {
        System.out.println("GAME OVER!!");

        reset();
        mGameOver = true;
        mChangeObserver.gameOver();
    }

    /**
     * The view, or whichever class who wants to listen to events
     * from this model must bind, and also implement the ChangeListener
     * interface
     * @param listener
     */
    public void bind(ChangeListener listener) {
        mChangeObserver.add(listener);
    }

    public boolean unbind(ChangeListener listener) {
        return mChangeObserver.delete(listener);
    }

    // Generates a random char from the alphabet
    private char getRandomCharacter() {
        int min = 0;
        int max = ALPHABET.length-1;
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
    public char getInputChar() { return mInput; }

}

