package Automata;

import java.util.ArrayList;
import java.util.List;

public final class NFA extends Automata
{
    //========================================================================= INITIALIZE
    public NFA(String... stateNames)
    {
        super(stateNames);
    }

    //========================================================================= FUNCTIONS
    public final void addTransition(String from, String to, char symbol)
    {
        addTransition(getState(from), new NFATransition(getState(to), symbol));
    }

    public final Trace getTrace(String input)
    {
       return getTrace(initialState(), input, new Trace(initialState().name, input), new ArrayList<State>());
    }
    private Trace getTrace(State currentState, String input, Trace trace, List<State> lambdaChain)
    {
        if (input.length() == 0 && currentState.isFinal)
        {
            trace.accept();
            return trace;
        }

        for (Transition t : currentState.transitions)
        {
            NFATransition transition = (NFATransition)t;

            if (transition.isLambda() || (input.length() > 0 && transition.symbol == input.charAt(0)))
            {
                String newInput = (transition.isLambda() ? input : input.substring(1));
                List<State> newLambdaChain = new ArrayList<State>(lambdaChain);

                if (transition.isLambda())
                {
                    if (newLambdaChain.contains(transition.target)) continue;
                    else newLambdaChain.add(transition.target);
                }
                else newLambdaChain.clear();

                Trace runTrace = getTrace(transition.target, newInput, trace.add(transition.target.name, transition.symbol), newLambdaChain);

                if (runTrace.isAccepted())
                    return runTrace;
            }
        }

        return trace;
    }

    //========================================================================= CLASSES
    protected static final class NFATransition extends Transition
    {
        protected final char symbol;

        public NFATransition(State target, char symbol)
        {
            super(target);
            this.symbol = symbol;
        }

        public final boolean equals(Object obj)
        {
            if (!(obj instanceof NFATransition))
                return false;

            NFATransition t = (NFATransition)obj;
            if (symbol != t.symbol) return false;
            else return super.equals(obj);
        }

        public final String toString()
        {
            return String.valueOf(symbol);
        }

        public final boolean isLambda()
        {
            return symbol == LAMBDA;
        }
    }
}
