package Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class Strings
{
    public static List<String> createRandomStrings(int count, char[] alphabet, int minLen, int maxLen)
    {
        List<String> strings = new ArrayList<String>();

        for (int i = 0; i < count; i++)
            strings.add(createRandomString(alphabet, minLen, maxLen));

        return strings;
    }
    public static String createRandomString(char[] alphabet, int minLen, int maxLen)
    {
        int length = rand(minLen, maxLen);
        String str = "";

        for (int i = 0; i < length; i++)
            str += alphabet[rand(0, alphabet.length - 1)];

        return str;
    }

    public static List<String> createAllStrings(char[] alphabet, int minLen, int maxLen)
    {
        List<String> strings = createAllStrings(new ArrayList<String>(), "", alphabet, minLen, maxLen);
        sort(strings);
        return strings;
    }
    private static List<String> createAllStrings(List<String> strings, String str, char[] alphabet, int minLen, int maxLen)
    {
        if (str.length() > maxLen)
            return strings;

        if (str.length() >= minLen)
            strings.add(str);

        for (char c : alphabet)
            createAllStrings(strings, str + c, alphabet, minLen, maxLen);

        return strings;
    }

    public static void sort(List<String> list)
    {
        Collections.sort(list, new Comparator<String>()
        {
            public final int compare(String s1, String s2)
            {
                if (s1.length() != s2.length())
                    return s1.length() - s2.length();
                else
                    return s1.compareTo(s2);
            }
        });
    }

    public static int countSymbols(String str, char symbol)
    {
        int count = 0;
        for (char c : str.toCharArray())
            if (c == symbol) count++;
        return count;
    }

    private static int rand(int min, int max)
    {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
