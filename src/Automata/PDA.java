package Automata;

import java.util.ArrayList;
import java.util.List;

public final class PDA extends Automata
{
    //========================================================================= INITIALIZE
    public PDA(String... stateNames)
    {
        super(stateNames);
    }

    //========================================================================= FUNCTIONS
    public final void addTransition(String from, String to, char symbol, String subStack, String addStack)
    {
        addTransition(getState(from), new PDATransition(getState(to), symbol, subStack, addStack));
    }

    public final Trace getTrace(String input)
    {
        return getTrace(initialState(), input, "", new Trace(input, initialState().name), new ArrayList<State>());
    }
    private Trace getTrace(State currentState, String input, String stack, Trace trace, List<State> lambdaChain)
    {
        if (input.length() == 0 && stack.length() == 0 && currentState.isFinal)
        {
            trace.accept();
            return trace;
        }

        for (Transition t : currentState.transitions)
        {
            PDATransition transition = (PDATransition)t;

            if ((transition.symbol == LAMBDA || (input.length() > 0 && transition.symbol == input.charAt(0))) &&
                (transition.subStack.isEmpty() || (stack.startsWith(transition.subStack))))
            {
                String newInput = (transition.symbol == LAMBDA ? input : input.substring(1));
                String newStack = transition.addStack + stack.substring(transition.subStack.length());
                Trace newTrace = trace.add(transition.target.name, transition.symbol + "," + (newStack.isEmpty() ? LAMBDA : newStack));
                List<State> newLambdaChain = new ArrayList<State>(lambdaChain);

                if (transition.isLambda())
                {
                    if (newLambdaChain.contains(transition.target)) continue;
                    else newLambdaChain.add(transition.target);
                }
                else newLambdaChain.clear();

                Trace runTrace = getTrace(transition.target, newInput, newStack, newTrace, newLambdaChain);

                if (runTrace.isAccepted())
                    return runTrace;
            }
        }

        return trace;
    }

    //========================================================================= CLASSES
    private final class PDATransition extends Transition
    {
        protected final char symbol;
        protected final String subStack;
        protected final String addStack;

        public PDATransition(State target, char symbol, String subStack, String addStack)
        {
            super(target);
            this.symbol = symbol;
            this.subStack = subStack;
            this.addStack = addStack;
        }

        public final boolean equals(Object obj)
        {
            if (!(obj instanceof PDATransition))
                return false;

            PDATransition t = (PDATransition)obj;
            if (symbol != t.symbol || !subStack.equals(t.subStack) || !addStack.equals(t.addStack)) return false;
            else return super.equals(obj);
        }

        public final String toString()
        {
            String sub = (subStack.isEmpty() ? "" : " - " + subStack);
            String add = (addStack.isEmpty() ? "" : " + " + addStack);
            return symbol + sub + add;
        }

        public final boolean isLambda()
        {
            return symbol == LAMBDA && subStack.isEmpty() && addStack.isEmpty();
        }
    }
}
