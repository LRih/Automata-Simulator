package Automata;

import java.util.ArrayList;
import java.util.List;

public final class Trace
{
    //========================================================================= VARIABLES
    private final String _start;
    private final String _input;
    private List<String> _states;
    private boolean _isAccepted;

    //========================================================================= INITIALIZE
    public Trace(String start, String input)
    {
        _start = start;
        _input = input;
        _states = new ArrayList<String>();
        _isAccepted = false;
    }

    //========================================================================= FUNCTIONS
    public final Trace add(String state, char trace)
    {
        return add(state, String.valueOf(trace));
    }
    public final Trace add(String state, String trace)
    {
        Trace copy = new Trace(_start, _input);
        copy._states = (List<String>)((ArrayList)_states).clone();
        copy._isAccepted = _isAccepted;

        copy._states.add(state + "(" + trace + ")");
        return copy;
    }

    public final void accept()
    {
        _isAccepted = true;
    }

    public final String toString()
    {
        StringBuilder builder = new StringBuilder();

        String input = (_input.isEmpty() ? String.valueOf(Automata.LAMBDA) : _input);
        if (isAccepted())
        {
            builder.append(String.format("Accept (%s): ", input));
            builder.append(_start);
            for (String state : _states)
                builder.append(" > ").append(state);
        }
        else
            builder.append(String.format("Reject (%s)", input));

        return builder.toString();
    }

    //========================================================================= PROPERTIES
    public final boolean isAccepted()
    {
        return _isAccepted;
    }
}
