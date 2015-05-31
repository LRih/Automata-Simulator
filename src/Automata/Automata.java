package Automata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Automata
{
    //========================================================================= CONSTANTS
    protected static final char LAMBDA = '#';
    private static final char FINAL_MARKER = LAMBDA;

    //========================================================================= VARIABLES
    private final HashMap<String, State> _states = new HashMap<String, State>();

    //========================================================================= INITIALIZE
    public Automata(String... stateNames)
    {
        if (stateNames.length == 0)
            throw new RuntimeException("Automata requires at least one state");

        for (int i = 0; i < stateNames.length; i++)
        {
            String name = stateNames[i].replace(String.valueOf(FINAL_MARKER), "");
            boolean isFinal = stateNames[i].startsWith(String.valueOf(FINAL_MARKER));
            _states.put(name, new State(name, i == 0, isFinal));
        }
    }

    //========================================================================= FUNCTIONS
    public final void addTransition(State from, Transition transition)
    {
        from.addTransition(transition);
    }

    public final boolean isAccept(String input)
    {
        String trace = getRunTrace(input);
        if (input.isEmpty()) input = String.valueOf(LAMBDA);
        if (trace.length() > 0)
        {
            System.out.println(String.format("Accept (%s): %s", input, trace));
            return true;
        }
        else
        {
            System.out.println(String.format("Reject (%s)", input));
            return false;
        }
    }
    protected abstract String getRunTrace(String input);

    protected final State getState(String name)
    {
        return _states.get(name);
    }

    //========================================================================= PROPERTIES
    protected final State initialState()
    {
        for (State state : _states.values())
            if (state.isInitial) return state;
        throw new RuntimeException("No initial state exists");
    }

    public final String toString()
    {
        String result = "";

        for (State state : _states.values())
            for (Transition transition : state.transitions)
                result += (state.isFinal ? FINAL_MARKER : "") + state.name + ", " +
                    (transition.target.isFinal ? FINAL_MARKER : "") + transition.target.name + ", " +
                    transition.toString() + "\n";

        return result;
    }

    //========================================================================= CLASSES
    protected final class State
    {
        public final List<Transition> transitions = new ArrayList<Transition>();
        public final String name;
        public final boolean isInitial;
        public final boolean isFinal;

        public State(String name, boolean isInitial, boolean isFinal)
        {
            this.name = name;
            this.isInitial = isInitial;
            this.isFinal = isFinal;
        }

        public final void addTransition(Transition transition)
        {
            if (transitions.contains(transition))
                throw new RuntimeException(name + " already has '" + transition.toString() + "' transition to " + transition.target.name);

            transitions.add(transition);
        }
    }

    protected static abstract class Transition
    {
        protected final State target;

        public Transition(State target)
        {
            this.target = target;
        }

        public boolean equals(Object obj)
        {
            if (!(obj instanceof Transition))
                return false;

            Transition t = (Transition)obj;
            return target == t.target;
        }

        public String toString()
        {
            return "generic";
        }

        public abstract boolean isLambda();
    }
}
