import Automata.DFA;
import Automata.Trace;
import Utils.Strings;

public final class Main
{
    public static void main(String[] args)
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
