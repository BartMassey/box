/*
 * Copyright © 2013 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

/* Shut The Box solver */

import java.util.*;

public class Box {
    static final int allDigits = 0x1ff;

    static int bit(int v) {
        return 1 << (v - 1);
    }

    static ArrayList<Integer> nexts(int digits, int roll) {
        int t = 0;
        int digitBound = 0;
        for (int i = 1; i <= 9; i++) {
            if ((digits & bit(i)) > 0) {
                if (t + i > roll)
                    break;
                digitBound++;
                t += i;
            }
        }
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int i = 1; i <= digitBound; i++)
            appendNexts(result, digits, roll, 0, i, 1);
        return result;
    }

    static void appendNexts(ArrayList<Integer> a, int digits, int roll,
                            int cur, int len, int start) {
        if (len == 1) {
            if (roll >= start && (digits & ~cur & bit(roll)) > 0)
                a.add(cur | bit(roll));
            return;
        }
        for (int i = start; i < 9 && i < roll; i++) {
            if ((digits & ~cur & bit(i)) == 0)
                continue;
            appendNexts(a, digits,
                        roll - i, cur | bit(i), len - 1, i + 1);
        }
    }

    static void printChoice(int c) {
        for (int i = 1; i <= 9; i++)
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
        ArrayList<Integer> choices = nexts(digits, roll);
        for (int c : choices)
            printChoice(c);
    }
}
