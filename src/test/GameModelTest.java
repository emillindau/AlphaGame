package test;

import event.ChangeListener;
import model.GameModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Emil
 * Date: 2013-12-07
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
public class GameModelTest {

    private GameModel sutWithMock, sut;

    @Mock
    private ScheduledExecutorService mockService;

    @Mock
    private ChangeListener mMockListener;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sutWithMock = new GameModel(mockService);
        sut = new GameModel(Executors.newSingleThreadScheduledExecutor());
    }

    @Test
    public void testExists() {
        assertNotNull("The class should exist", sutWithMock);
    }

    @Test
    public void testDifficulty() {
        GameModel.Difficulty expected = GameModel.Difficulty.HARDER;
        GameModel sutTwo = new GameModel(expected, mockService);
        assertEquals("Should be the same", expected, sutTwo.getDifficulty());
    }

    @Test
    public void testAlphabet() {
        char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        assertArrayEquals("The class should contain the alphabet as a char array", alphabet, GameModel.ALPHABET);
    }

    @Test // Should be private
    public void testReturnRandom() {
        // 65
        // 91

        try {
            Method sutReflectM = sutWithMock.getClass().getDeclaredMethod("getRandomCharacter", new Class<?>[0]);
            sutReflectM.setAccessible(true);

            // Test it a couple of times - Yay! Found a bug with this one
            for(int i = 0; i < 100; i++) {
                Object val = sutReflectM.invoke(sutWithMock);
                char random = (char) val;
                assertTrue("Should be in range 65-91", random > 64 && random < 92);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStartStop() {
        boolean check = sutWithMock.start();
        assertTrue("Should be able to start", check);

        long timeStep = 0;
        try {
            Field reflectField = sutWithMock.getClass().getDeclaredField("DIFFICULTY_TIME_MAP");
            reflectField.setAccessible(true);
            Object val = reflectField.get(sutWithMock);
            timeStep = new Long((int)((HashMap) val).get(GameModel.Difficulty.HARD));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Mockito.verify(mockService).scheduleWithFixedDelay((Runnable)Mockito.anyObject(), Mockito.eq(0L), Mockito.eq(timeStep), Mockito.eq(TimeUnit.MILLISECONDS));
        // Mockito.verify(mockService).scheduleWithFixedDelay(mockRunnable, 0L, 1000L, TimeUnit.MILLISECONDS);

        sutWithMock.stop();
        Mockito.verify(mockService).shutdown();
    }

    @Test
    public void testInput() {
        sutWithMock.input("s");
        assertEquals("Should be", 'S', sutWithMock.getInputChar());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInputFaultTooLong() {
        sutWithMock.input("asd");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInputFaultNotAChar() {
        sutWithMock.input("1");
    }

    @Test
    public void testStartGeneration() {
        sut.start();

        Field reflectField = null;
        try {
            // Sleep, because it takes 1 sec before first number is generated
            sleep(2000);

            // Peek at the field
            reflectField = sut.getClass().getDeclaredField("mCurrent");
            reflectField.setAccessible(true);
            Object val = reflectField.get(sut);
            char generated = (char) val;

            // And check that a number has been generated
            assertTrue("Should be in range 65-91", generated > 64 && generated < 92);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            sut.stop();
        }
    }

    // Helper
    private void sleep(int amount) {
        try {
            System.out.println("> Thread sleep. Waiting " + (amount / 1000) + " sec...");
            Thread.sleep(amount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEventDelegateOnce() {
        sut.bind(mMockListener);
        sut.start();


        sleep(1000);

        Mockito.verify(mMockListener, Mockito.times(1)).change(Mockito.anyChar());
        sut.stop();
        sut.unbind(mMockListener);
    }

    @Test
    public void testEventDelegateTwice() {
        sut.bind(mMockListener);
        sut.start();

        sleep(2000);

        Mockito.verify(mMockListener, Mockito.times(2)).change(Mockito.anyChar());
        sut.stop();
        sut.unbind(mMockListener);

        Mockito.verifyNoMoreInteractions(mMockListener);
    }

    /**
     * To generate a faulty guess we need to peek at
     * the field that has been generated
     * @return hopefully a faulty String
     */
    private String getFaultyGuess() {
        Field reflectField = null;
        try {
            reflectField = sut.getClass().getDeclaredField("mCurrent");
            reflectField.setAccessible(true);
            Object val = reflectField.get(sut);
            char generated = (char) val;
            char myGuess = 'A';

            if(myGuess == generated) {
                myGuess++;
            }
            return ""+myGuess;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void testInputTowardsGeneratedFail() {
        sut.bind(mMockListener);
        sut.start();

        for(int i = 0; i < 4; i++) {
            sleep(1000);
            String faultyInput = getFaultyGuess();
            sut.input(faultyInput);
        }

        Mockito.verify(mMockListener, Mockito.atLeastOnce()).gameOver();

        sut.stop();
        sut.unbind(mMockListener);
    }

    @Test
    public void testNoInputGameOver() {
        sut.bind(mMockListener);
        sut.start();

        sleep(4100);
        // Has the value changed 3 times?
        Mockito.verify(mMockListener, Mockito.times(4)).change(Mockito.anyChar());
        // Then it should also generate gameOver
        Mockito.verify(mMockListener, Mockito.atLeastOnce()).gameOver();

        sut.stop();
        sut.unbind(mMockListener);
    }

    @Test
    public void testOneGuessPerChange() {
        sut.bind(mMockListener);
        sut.start();

        sleep(1000);
        for(int i = 0; i < 10; i++) {
            sut.input("k");
        }

        // Even though we guessed a couple of times
        // gameOver should not be called since
        // one guess per change
        Mockito.verify(mMockListener, Mockito.times(0)).gameOver();

        sut.stop();
        sut.unbind(mMockListener);
    }

    @After
    public void tearDown() throws Exception {
        sutWithMock = null;
        sut.stop();
        sut = null;
    }
}
