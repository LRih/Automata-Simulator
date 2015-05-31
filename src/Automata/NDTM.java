package Automata;

import java.util.Arrays;

public final class NDTM extends Automata
{
    //========================================================================= INITIALIZE
    public NDTM(String... stateNames)
    {
        super(stateNames);
    }

    //========================================================================= FUNCTIONS
    public final void addTransition(String from, String to, char read, char write, Move move)
    {
        addTransition(getState(from), new TMTransition(getState(to), read, write, move));
    }

    protected final String getRunTrace(String input)
    {
        return getRunTrace(initialState(), (LAMBDA + input + LAMBDA).toCharArray(), 0, initialState().name);
    }
    private String getRunTrace(State currentState, char[] input, int index, String trace)
    {
        if (index < 0 || index == input.length)
            return "";

        boolean foundTransition = false;

        for (Transition t : currentState.transitions)
        {
            TMTransition transition = (TMTransition)t;

            if (transition.read == input[index])
            {
                foundTransition = true;

                char[] newInput = Arrays.copyOf(input, input.length);
                newInput[index] = transition.write;
                int newIndex = index + transition.move.value();
                String newTrace = trace + " > " + transition.target.name + "(" + createInputTrace(newInput, newIndex) + ")";

                String runTrace = getRunTrace(transition.target, newInput, newIndex, newTrace);

                if (runTrace.length() > 0)
                    return runTrace;
            }
        }

        return (currentState.isFinal && !foundTransition ? trace : "");
    }

    private String createInputTrace(char[] input, int index)
    {
        String str = new String(input);

        if (index < 0)
            return "_" + str;
        else if (index == str.length())
            return str + "_";
        else
            return str.substring(0, index) + "_" + str.substring(index);
    }

    //========================================================================= CLASSES
    private final class TMTransition extends Transition
    {
        protected final char read;
        protected final char write;
        protected final Move move;

        public TMTransition(State target, char read, char write, Move move)
        {
            super(target);
            this.read = read;
            this.write = write;
            this.move = move;
        }

        public final boolean equals(Object obj)
        {
            if (!(obj instanceof TMTransition))
                return false;

            TMTransition t = (TMTransition)obj;
            if (read != t.read || write != t.write || move != t.move) return false;
            else return super.equals(obj);
        }

        public final String toString()
        {
            return String.format("%s/%s, %s", read, write, move);
        }

        public final boolean isLambda()
        {
            return false;
        }
    }

    public enum Move
    {
        L, R, S;

        public int value()
        {
            switch (this)
            {
                case L: return -1;
                case R: return 1;
                case S: return 0;
                default: throw new RuntimeException("Unreachable code reached");
            }
        }
    }
}
