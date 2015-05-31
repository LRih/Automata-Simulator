package Automata;

public final class DFA extends Automata
{
    //========================================================================= INITIALIZE
    public DFA(String... stateNames)
    {
        super(stateNames);
    }

    //========================================================================= FUNCTIONS
    public final void addTransition(String from, String to, char symbol)
    {
        NFA.NFATransition transition = new NFA.NFATransition(getState(to), symbol);

        if (transition.isLambda())
            throw new RuntimeException("DFA cannot have empty transition");
        else if (getState(from).transitions.contains(transition))
            throw new RuntimeException(getState(from).name + " already has '" + transition.toString() + "' transition");

        addTransition(getState(from), transition);
    }

    protected final String getRunTrace(String input)
    {
        return getRunTrace(initialState(), input, initialState().name);
    }
    private String getRunTrace(State currentState, String input, String trace)
    {
        if (input.length() == 0)
            return (currentState.isFinal ? trace : "");

        for (Transition t : currentState.transitions)
        {
            NFA.NFATransition transition = (NFA.NFATransition)t;

            if (input.length() > 0 && transition.symbol == input.charAt(0))
            {
                String newInput = input.substring(1);
                String newTrace = trace + " > " + transition.target.name + "(" + transition.symbol + ")";

                String runTrace = getRunTrace(transition.target, newInput, newTrace);

                if (runTrace.length() > 0)
                    return runTrace;
            }
        }

        return "";
    }
}
