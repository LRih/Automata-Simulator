package Automata;

import Utils.Strings;

import java.util.ArrayList;
import java.util.List;

public final class UnrestrictedGrammar
{
    //========================================================================= CONSTANTS
    private static final String NON_TERMINAL = "[A-Z]";
    private static final String TERMINAL = "[a-z0-9]";

    //========================================================================= VARIABLES
    private final List<Rule> _rules = new ArrayList<Rule>();

    //========================================================================= FUNCTIONS
    public final void addRule(String left, String right)
    {
        if (!left.matches(".*" + NON_TERMINAL + ".*"))
            throw new RuntimeException("Left side '" + left + "' must contain at least one non-terminal");

        for (String splitRight : right.replace(" ", "").split("\\|", -1))
        {
            Rule rule = new Rule(left, splitRight);
            if (_rules.contains(rule))
                throw new RuntimeException("'" + rule.toString() + "' already exists");
            _rules.add(rule);
        }
    }

    public final List<String> generate(int maxLength)
    {
        List<String> strings = generate(new ArrayList<String>(), "S", maxLength);
        Strings.sort(strings);
        return strings;
    }
    private List<String> generate(List<String> strings, String str, int maxLength)
    {
        for (Rule rule : _rules)
        {
            String newStr = str.replaceFirst(rule.left, rule.right);
            if (!newStr.equals(str) && newStr.length() <= maxLength)
            {
                if (!strings.contains(newStr) && newStr.matches("\\A" + TERMINAL + "*\\Z"))
                    strings.add(newStr);
                else
                    generate(strings, newStr, maxLength);
            }
        }
        return strings;
    }

    //========================================================================= PROPERTIES
    public final String toString()
    {
        String result = "";

        for (Rule rule : _rules)
            result += rule.toString() + "\n";

        return result;
    }

    //========================================================================= CLASSES
    private final class Rule
    {
        public final String left;
        public final String right;

        public Rule(String left, String right)
        {
            this.left = left;
            this.right = right;
        }

        public final boolean equals(Object obj)
        {
            if (!(obj instanceof Rule))
                return false;

            Rule rule = (Rule)obj;
            return left.equals(rule.left) && right.equals(rule.right);
        }

        public final String toString()
        {
            return left + " -> " + right;
        }
    }
}
