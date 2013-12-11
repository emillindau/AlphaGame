package test;

import event.ChangeListener;
import model.GameModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
            System.out.println("> Thread sleep. Waiting 2 sec...");
            Thread.sleep(2000);

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEventDelegate() {
        sut.bind(mMockListener);
        Mockito.verify(mMockListener).change();
    }

    @After
    public void tearDown() throws Exception {
        sutWithMock = null;
        sut.stop();
        sut = null;
    }
}
