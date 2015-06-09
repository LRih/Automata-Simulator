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

    public final Trace getTrace(String input)
    {
        return getTrace(initialState(), input, new Trace(initialState().name, input));
    }
    private Trace getTrace(State currentState, String input, Trace trace)
    {
        if (input.length() == 0)
        {
            if (currentState.isFinal) trace.accept();
            return trace;
        }

        for (Transition t : currentState.transitions)
        {
            NFA.NFATransition transition = (NFA.NFATransition)t;

            if (input.length() > 0 && transition.symbol == input.charAt(0))
            {
                String newInput = input.substring(1);
                Trace runTrace = getTrace(transition.target, newInput, trace.add(transition.target.name, transition.symbol));

                if (runTrace.isAccepted())
                    return runTrace;
            }
        }

        return trace;
    }
}
