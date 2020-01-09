package sample;

import java.util.HashMap;
import java.util.Map;


public class brailleConverter {

    private static Map<Character, Integer> braille;

    public static boolean numericIndicator = false;


    brailleConverter() {


        braille = new HashMap<Character, Integer>();
        braille.put(',', 0x2820);
        braille.put('@', 0x2808);
        braille.put(';', 0x2806);
        braille.put('/', 0x280c);
        braille.put('(', 0x2837);
        braille.put(')', 0x283E);
        braille.put(' ', 0x0020);
        braille.put('"', 0x2810);
        braille.put('!', 0x282E);
        braille.put('^', 0x2818);
        braille.put('>', 0x281C);
        braille.put('<', 0x2823);
        braille.put('*', 0x2821);
        braille.put('-', 0x2824);
        braille.put('.', 0x2828);
        braille.put('%', 0x2829);
        braille.put('[', 0x282A);
        braille.put('$', 0x281B);
        braille.put('+', 0x282C);
        braille.put('&', 0x282F);
        braille.put(';', 0x2830);
        braille.put(':', 0x2831);
        braille.put('_', 0x2838);
        braille.put('?', 0x2839);
        braille.put(']', 0x283B);
        braille.put('#', 0x283C);
        braille.put('=', 0x283F);
        braille.put('a', 0x2801);
        braille.put('b', 0x2803);
        braille.put('c', 0x2809);
        braille.put('d', 0x2819);
        braille.put('e', 0x2811);
        braille.put('f', 0x280B);
        braille.put('g', 0x281B);
        braille.put('h', 0x2813);
        braille.put('i', 0x280A);
        braille.put('j', 0x281A);
        braille.put('k', 0x2805);
        braille.put('l', 0x2807);
        braille.put('m', 0x280D);
        braille.put('n', 0x281D);
        braille.put('o', 0x2815);
        braille.put('p', 0x280F);
        braille.put('q', 0x281F);
        braille.put('r', 0x2817);
        braille.put('s', 0x280E);
        braille.put('t', 0x281E);
        braille.put('u', 0x2825);
        braille.put('v', 0x2827);
        braille.put('w', 0x283A);
        braille.put('x', 0x282D);
        braille.put('y', 0x283D);
        braille.put('z', 0x2835);
        braille.put('0', 0x2834);
        braille.put('1', 0x2802);
        braille.put('2', 0x2806);
        braille.put('3', 0x2812);
        braille.put('4', 0x2832);
        braille.put('5', 0x2822);
        braille.put('6', 0x2816);
        braille.put('7', 0x2836);
        braille.put('8', 0x2826);
        braille.put('9', 0x2814);
        braille.put('\n', 0x000A);

//        braille.put('\'',0x2804);

    }

    public String brailleKeyboardToChar(String str) {

        if (compare(str, "1")) {
            if (numericIndicator == true) {
                numericIndicator = false;
                return "1";
            }
            return "a";
        } else if (compare(str, "12")) {
            if (numericIndicator == true) {
                numericIndicator = false;
                return "2";
            }
            return "b";
        } else if (compare(str, "14")) {
            if (numericIndicator == true) {
                numericIndicator = false;
                return "3";
            }
            return "c";
        } else if (compare(str, "145")) {
            if (numericIndicator == true) {
                numericIndicator = false;

                return "4";
            }
            return "d";
        } else if (compare(str, "15")) {
            if (numericIndicator == true) {
                numericIndicator = false;
                return "5";
            }
            return "e";
        } else if (compare(str, "124")) {
            if (numericIndicator == true) {
                numericIndicator = false;
                return "6";
            }
            return "f";
        } else if (compare(str, "1245")) {
            if (numericIndicator == true) {
                numericIndicator = false;
                return "7";
            }
            return "g";
        } else if (compare(str, "125")) {
            if (numericIndicator == true) {
                numericIndicator = false;
                return "8";
            }
            return "h";
        } else if (compare(str, "24")) {
            if (numericIndicator == true) {
                numericIndicator = false;
                return "9";
            }
            return "i";
        } else if (compare(str, "245")) {
            if (numericIndicator == true) {
                numericIndicator = false;
                return "0";
            }
            return "j";
        } else if (compare(str, "13")) {
            return "k";
        } else if (compare(str, "123")) {
            return "l";
        } else if (compare(str, "134")) {
            return "m";
        } else if (compare(str, "1345")) {
            return "n";
        } else if (compare(str, "135")) {
            return "o";
        } else if (compare(str, "1234")) {
            return "p";
        } else if (compare(str, "12345")) {
            return "q";
        } else if (compare(str, "1235")) {
            return "r";
        } else if (compare(str, "234")) {
            return "s";
        } else if (compare(str, "2345")) {
            return "t";
        } else if (compare(str, "136")) {
            return "u";
        } else if (compare(str, "1236")) {
            return "v";
        } else if (compare(str, "2456")) {
            return "w";
        } else if (compare(str, "1346")) {
            return "x";
        } else if (compare(str, "13456")) {
            return "y";
        } else if (compare(str, "1356")) {
            return "z";
        } else if (compare(str, "3456")) {
            if (numericIndicator == true) {
                numericIndicator = false;
                return "#";
            }
            numericIndicator = true;
        } else if (str.equals("9")) {
            return " ";
        }

//        else if (str.equals("100010"))
        return "n";
    }

    public char convertChar(char s) {
        try {
            return (char) braille.get(Character.toLowerCase(s)).intValue();

        } catch (Exception ex) {
//            ex.printStackTrace();
            return 'n';
        }
    }

    public static boolean compare(String i, String j) {

        int len = i.length();
        if (j.length() != len) {
            return false;
        }
        int count = 0;
        for (int idx = 0; idx < len; idx++) {
            System.out.println(i.substring(idx, idx + 1));
            if (j.contains(i.substring(idx, idx + 1))) {
                count += 1;
            }
        }
        System.out.println(count);
        if (count == len) {
            return true;
        }
        return false;
    }


}
