import Automata.DFA;
import Automata.NFA;
import Automata.PDA;
import Automata.Trace;
import Utils.Strings;

public final class Main
{
    public static void main(String[] args)
    {
        testExamPDA();
    }

    public static void testExamNFA()
    {
        NFA nfa = new NFA("p", "q", "#r");

        nfa.addTransition("p", "p", 'a');
        nfa.addTransition("p", "q", 'b');
        nfa.addTransition("p", "r", 'c');

        nfa.addTransition("q", "q", 'a');
        nfa.addTransition("q", "p", '#');
        nfa.addTransition("q", "r", 'b');

        nfa.addTransition("r", "r", 'a');
        nfa.addTransition("r", "p", 'c');
        nfa.addTransition("r", "q", '#');

        for (String str : Strings.createAllStrings(new char[] { 'a', 'b', 'c' }, 0, 6))
        {
            Trace trace = nfa.getTrace(str);
            System.out.println(trace.toString());
        }
    }

    public static void testExamPDA()
    {
        PDA pda = new PDA("q0", "q1", "q2", "#q3", "q4");

        pda.addTransition("q0", "q0", 'a', "", "A");
        pda.addTransition("q0", "q1", 'b', "", "B");

        pda.addTransition("q1", "q1", 'b', "", "B");
        pda.addTransition("q1", "q2", 'b', "", "B");

        pda.addTransition("q2", "q2", 'c', "B", "");
        pda.addTransition("q2", "q3", 'c', "B", "");

        pda.addTransition("q3", "q4", 'c', "A", "");
        pda.addTransition("q4", "q3", '#', "A", "");

        System.out.println(pda.getTrace("aabbccc"));
    }

    public static void testEvenNumberOf1s()
    {
        // even number of 1s
        DFA dfa = new DFA("#q0", "q1");

        dfa.addTransition("q0", "q0", '0');
        dfa.addTransition("q0", "q1", '1');
        dfa.addTransition("q1", "q1", '0');
        dfa.addTransition("q1", "q0", '1');

        for (String str : Strings.createAllStrings(new char[] { '0', '1' }, 0, 6))
        {
            Trace trace = dfa.getTrace(str);
            System.out.println(trace.toString());

            boolean test1 = trace.isAccepted();
            boolean test2 = Strings.countSymbols(str, '1') % 2 == 0;

            if (test1 != test2)
            {
                System.out.println("error");
                break;
            }
        }
    }
}
