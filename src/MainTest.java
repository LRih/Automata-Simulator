import Automata.*;
import Utils.Strings;

import org.junit.Test;

import static org.junit.Assert.*;

public final class MainTest
{
    @Test
    public void testPDA()
    {
        // 2x number of 'b' as 'a'
        PDA pda = new PDA("#q0");
        pda.addTransition("q0", "q0", 'a', "", "AA");
        pda.addTransition("q0", "q0", 'b', "A", "");
        pda.addTransition("q0", "q0", 'b', "", "B");
        pda.addTransition("q0", "q0", 'a', "BB", "");
        pda.addTransition("q0", "q0", 'a', "B", "A");

        for (String str : Strings.createAllStrings(new char[]{ 'a', 'b' }, 0, 6))
        {
            boolean test1 = pda.isAccept(str);
            boolean test2 = 2 * Strings.countSymbols(str, 'a') == Strings.countSymbols(str, 'b');

            assertTrue(test1 == test2);
        }
    }

    @Test
    public void testNDTM1()
    {
        // { a^i b^j | i >= 1, j >= 0, i != j }
        NDTM tm = new NDTM("q0", "q1", "q2", "#q3", "q4", "q5", "q6", "q7", "q8", "q9", "#q10");

        tm.addTransition("q0", "q1", '#', '#', NDTM.Move.R);

        tm.addTransition("q1", "q2", 'a', 'a', NDTM.Move.R);

        tm.addTransition("q2", "q3", '#', '#', NDTM.Move.S);
        tm.addTransition("q2", "q4", 'a', 'a', NDTM.Move.L);
        tm.addTransition("q2", "q4", 'b', 'b', NDTM.Move.L);

        tm.addTransition("q4", "q4", 'X', 'X', NDTM.Move.R);
        tm.addTransition("q4", "q5", 'a', 'X', NDTM.Move.R);
        tm.addTransition("q4", "q7", '#', '#', NDTM.Move.S);
        tm.addTransition("q4", "q8", 'Y', 'Y', NDTM.Move.R);

        tm.addTransition("q5", "q5", 'a', 'a', NDTM.Move.R);
        tm.addTransition("q5", "q5", 'Y', 'Y', NDTM.Move.R);
        tm.addTransition("q5", "q6", 'b', 'Y', NDTM.Move.L);
        tm.addTransition("q5", "q10", '#', '#', NDTM.Move.S);

        tm.addTransition("q6", "q6", 'a', 'a', NDTM.Move.L);
        tm.addTransition("q6", "q6", 'Y', 'Y', NDTM.Move.L);
        tm.addTransition("q6", "q4", 'X', 'X', NDTM.Move.R);

        tm.addTransition("q8", "q8", 'Y', 'Y', NDTM.Move.R);
        tm.addTransition("q8", "q9", 'b', 'b', NDTM.Move.R);

        tm.addTransition("q9", "q9", 'b', 'b', NDTM.Move.R);

        tm.addTransition("q9", "q10", '#', '#', NDTM.Move.S);

        for (String str : Strings.createAllStrings(new char[]{ 'a', 'b' }, 0, 6))
        {
            int aCount = Strings.countSymbols(str, 'a');
            int bCount = Strings.countSymbols(str, 'b');

            boolean test1 = tm.isAccept(str);
            boolean test2 = aCount >= 1 && bCount >= 0 && aCount != bCount && str.matches("\\Aa*b*\\Z");

            assertTrue(test1 == test2);
        }
    }

    @Test
    public void testNDTM2()
    {
        // { a^i b^j a^(i+j) | i,j >= 0 }
        NDTM tm = new NDTM("q0", "q1", "#q2", "q3", "q4", "q5", "q6", "q7", "q8", "q9", "#q10");

        tm.addTransition("q0", "q1", '#', '#', NDTM.Move.R);

        tm.addTransition("q1", "q1", 'a', 'X', NDTM.Move.R);
        tm.addTransition("q1", "q2", '#', '#', NDTM.Move.L);
        tm.addTransition("q1", "q4", 'b', 'X', NDTM.Move.R);

        tm.addTransition("q2", "q3", 'X', 'X', NDTM.Move.L);

        tm.addTransition("q3", "q2", 'X', 'X', NDTM.Move.L);

        tm.addTransition("q4", "q4", 'b', 'X', NDTM.Move.R);
        tm.addTransition("q4", "q5", 'a', 'a', NDTM.Move.R);

        tm.addTransition("q5", "q5", 'a', 'a', NDTM.Move.R);
        tm.addTransition("q5", "q6", '#', '#', NDTM.Move.L);

        tm.addTransition("q6", "q7", 'a', 'Y', NDTM.Move.L);
        tm.addTransition("q6", "q10", 'Y', 'Y', NDTM.Move.R);

        tm.addTransition("q7", "q7", 'a', 'a', NDTM.Move.L);
        tm.addTransition("q7", "q7", 'X', 'X', NDTM.Move.L);

        tm.addTransition("q7", "q8", '#', '#', NDTM.Move.R);
        tm.addTransition("q7", "q8", 'Y', 'Y', NDTM.Move.R);

        tm.addTransition("q8", "q9", 'X', 'Y', NDTM.Move.R);

        tm.addTransition("q9", "q9", 'a', 'a', NDTM.Move.R);
        tm.addTransition("q9", "q9", 'X', 'X', NDTM.Move.R);
        tm.addTransition("q9", "q6", 'Y', 'Y', NDTM.Move.L);

        for (String str : Strings.createAllStrings(new char[]{ 'a', 'b' }, 0, 10))
        {
            int bCount = Strings.countSymbols(str, 'b');
            int startingA = 0, endingA = 0;
            for (int i = 0; i < str.length() && str.charAt(i) != 'b'; i++) startingA++;
            for (int i = str.length() - 1; i >= 0 && str.charAt(i) != 'b'; i--) endingA++;

            boolean test1 = tm.isAccept(str);
            boolean test2;

            if (bCount == 0) test2 = startingA % 2 == 0;
            else test2 = endingA == startingA + bCount && str.matches("\\Aa*b*a*\\Z");

            assertTrue(test1 == test2);
        }
    }

    @Test
    public void testNDTM3()
    {
        // number of a = number of b
        NDTM tm = new NDTM("q0", "#q1", "q2", "#q3", "q4", "q5", "#q6");

        tm.addTransition("q0", "q1", '#', '#', NDTM.Move.R);

        tm.addTransition("q1", "q1", 'Y', 'Y', NDTM.Move.R);
        tm.addTransition("q1", "q1", 'X', 'X', NDTM.Move.R);
        tm.addTransition("q1", "q1", 'c', 'c', NDTM.Move.R);
        tm.addTransition("q1", "q2", 'a', 'X', NDTM.Move.R);
        tm.addTransition("q1", "q4", 'b', 'Y', NDTM.Move.R);

        tm.addTransition("q2", "q2", 'a', 'a', NDTM.Move.R);
        tm.addTransition("q2", "q2", 'c', 'c', NDTM.Move.R);
        tm.addTransition("q2", "q2", 'Y', 'Y', NDTM.Move.R);
        tm.addTransition("q2", "q3", 'b', 'Y', NDTM.Move.L);

        tm.addTransition("q3", "q3", 'a', 'a', NDTM.Move.L);
        tm.addTransition("q3", "q3", 'c', 'c', NDTM.Move.L);
        tm.addTransition("q3", "q3", 'Y', 'Y', NDTM.Move.L);
        tm.addTransition("q3", "q1", 'X', 'X', NDTM.Move.R);

        tm.addTransition("q4", "q4", 'b', 'b', NDTM.Move.R);
        tm.addTransition("q4", "q4", 'c', 'c', NDTM.Move.R);
        tm.addTransition("q4", "q4", 'X', 'X', NDTM.Move.R);
        tm.addTransition("q4", "q5", 'a', 'X', NDTM.Move.L);

        tm.addTransition("q5", "q5", 'b', 'b', NDTM.Move.L);
        tm.addTransition("q5", "q5", 'c', 'c', NDTM.Move.L);
        tm.addTransition("q5", "q5", 'X', 'X', NDTM.Move.L);
        tm.addTransition("q5", "q1", 'Y', 'Y', NDTM.Move.R);

        for (String str : Strings.createAllStrings(new char[]{ 'a', 'b' }, 0, 6))
        {
            boolean test1 = tm.isAccept(str);
            boolean test2 = Strings.countSymbols(str, 'a') == Strings.countSymbols(str, 'b');

            assertTrue(test1 == test2);
        }
    }

    @Test
    public void testAssignment2Q1()
    {
        NDTM tm = new NDTM("q0", "q1", "q2", "q3", "q4", "#q5");

        tm.addTransition("q0", "q1", '#', '#', NDTM.Move.R);

        tm.addTransition("q1", "q2", '9', '1', NDTM.Move.R);
        tm.addTransition("q1", "q5", '7', '7', NDTM.Move.R);

        tm.addTransition("q2", "q2", '9', '9', NDTM.Move.R);
        tm.addTransition("q2", "q2", '7', '7', NDTM.Move.R);
        tm.addTransition("q2", "q2", '2', '2', NDTM.Move.R);

        tm.addTransition("q2", "q3", '8', '2', NDTM.Move.R);

        tm.addTransition("q3", "q4", '8', '2', NDTM.Move.L);

        tm.addTransition("q4", "q4", '9', '9', NDTM.Move.L);
        tm.addTransition("q4", "q4", '7', '7', NDTM.Move.L);
        tm.addTransition("q4", "q4", '2', '2', NDTM.Move.L);

        tm.addTransition("q4", "q1", '1', '1', NDTM.Move.R);

        tm.addTransition("q5", "q5", '2', '2', NDTM.Move.R);
        tm.addTransition("q5", "q5", '7', '7', NDTM.Move.R);

        assertTrue(tm.isAccept("979887"));
        assertFalse(tm.isAccept("9798787"));
        assertTrue(tm.isAccept("99798878898"));
        assertFalse(tm.isAccept("997988787887"));
    }

    @Test
    public void testAssignment2Q3()
    {
        NFA original = createOriginalNFA();
        DFA aMachine = create3aDFA();
        NFA bMachine = create3bNFA();

        for (String str : Strings.createRandomStrings(100, new char[] { 'a', 'b', 'c' }, 1, 10))
        {
            boolean test1 = original.isAccept(str);
            boolean test2 = aMachine.isAccept(str);
            boolean test3 = bMachine.isAccept(str);

            assertTrue(test1 == test2 && test2 == test3);
        }
    }
    private NFA createOriginalNFA()
    {
        NFA original = new NFA("s0", "s1", "s2", "#s3");

        original.addTransition("s0", "s0", 'b');

        original.addTransition("s0", "s1", 'b');
        original.addTransition("s0", "s1", '#');

        original.addTransition("s1", "s1", 'a');
        original.addTransition("s1", "s1", 'c');

        original.addTransition("s1", "s2", 'a');

        original.addTransition("s2", "s2", 'c');

        original.addTransition("s2", "s3", 'c');
        original.addTransition("s2", "s3", '#');

        original.addTransition("s3", "s3", 'a');

        return original;
    }
    private DFA create3aDFA()
    {
        DFA a = new DFA("s0", "s0s1", "s1", "s1s2", "#s1s2s3");

        a.addTransition("s0", "s1s2s3", 'a');
        a.addTransition("s0", "s0s1", 'b');
        a.addTransition("s0", "s1", 'c');

        a.addTransition("s1", "s1s2s3", 'a');
        a.addTransition("s1", "s1", 'c');

        a.addTransition("s0s1", "s1s2s3", 'a');
        a.addTransition("s0s1", "s0s1", 'b');
        a.addTransition("s0s1", "s1", 'c');

        a.addTransition("s1s2", "s1s2s3", 'a');
        a.addTransition("s1s2", "s1s2s3", 'c');

        a.addTransition("s1s2s3", "s1s2s3", 'a');
        a.addTransition("s1s2s3", "s1s2s3", 'c');

        return a;
    }
    private NFA create3bNFA()
    {
        NFA b = new NFA("s0", "s1", "#s2");

        b.addTransition("s0", "s2", 'a');
        b.addTransition("s0", "s0", 'b');
        b.addTransition("s0", "s1", 'c');

        b.addTransition("s1", "s1", 'a');
        b.addTransition("s1", "s2", 'a');
        b.addTransition("s1", "s1", 'c');

        b.addTransition("s2", "s2", 'a');
        b.addTransition("s2", "s2", 'c');


        return b;
    }
}