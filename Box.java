/* Copyright © 2013 Bart Massey */
/* Shut The Box solver */

import java.util.*;

public class Box {
    static final int allDigits = 0x1ff;

    static HashSet<Integer> nexts(int digits, int roll) {
        HashSet<Integer> result = new HashSet<Integer>();
        for (int i = 1; i <= 9; i++) {
            int count = appendNexts(result, digits, roll, 0, i);
            if (i > 1 && count == 0)
                break;
        }
        return result;
    }

    static int bit(int v) {
        return 1 << (v - 1);
    }

    static int appendNexts(HashSet<Integer> a, int digits, int roll,
                    int cur, int len) {
        if (len == 1) {
            if (((digits & ~cur) & bit(roll)) > 0) {
                a.add(cur | bit(roll));
                return 1;
            }
            return 0;
        }
        int t = 0;
        for (int i = 1; i <= 9; i++) {
            if ((digits & ~cur & bit(i)) == 0)
                continue;
            t += appendNexts(a, digits, roll - i, cur | bit(i), len - 1);
        }
        return t;
    }

    static void printChoice(int c) {
        for (int i = 9; i >= 1; --i)
            if ((c & bit(i)) > 0)
                System.out.print(i);
        System.out.println();
    }

    public static void main(String[] args) {
        String digitStr = args[0];
        int roll = Integer.parseInt(args[1]);
        int digits = 0;
        for (int i = 0; i < digitStr.length(); i++) {
            int d = digitStr.charAt(i) - '0';
            digits |= bit(d);
        }
        HashSet<Integer> choices = nexts(digits, roll);
        for (int c : choices)
            printChoice(c);
    }
}
