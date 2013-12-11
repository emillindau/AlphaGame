package test;

import model.GameModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: Emil
 * Date: 2013-12-07
 * Time: 23:59
 * To change this template use File | Settings | File Templates.
 */
public class GameModelTest {

    private GameModel sut;

    @Mock
    private Object t;

    @Before
    public void setUp() throws Exception {
        sut = new GameModel();
    }

    @Test
    public void testExists() {
        assertNotNull("The class should exist", sut);
    }

    @Test
    public void testDifficulty() {
        GameModel.Difficulty expected = GameModel.Difficulty.HARDER;
        GameModel sutTwo = new GameModel(expected);
        assertEquals("Should be the same", expected, sutTwo.getDifficulty());
    }

    @Test
    public void testAlphabet() {
        char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        assertArrayEquals("The class should contain the alphabet as a char array", alphabet, GameModel.ALPHABET);
    }

    @Ignore // Should be private
    public void testReturnRandom() {
        // 65
        // 91
        // char random = sut.getRandomCharacter();
        // assertTrue("Should be in range 65-91", random > 64 && random < 92);
    }

    @Test
    public void testStartStop() {
        assertTrue("Should be able to start", sut.start());
        sut.stop();
    }

    @After
    public void tearDown() throws Exception {
        sut = null;
    }
}
