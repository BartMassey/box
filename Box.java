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
    static double[] value = new double[512];

    static {
        for (int i = 0; i < 512; i++)
            value[i] = -1.0;
    }

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

    static double prob(int i) {
        return (6.0 - Math.abs(7 - i)) / 36.0;
    }

    static int valueLabel(int i) {
        int v = 0;
        for (int j = 1; j <= 9; j++)
            if ((i & bit(j)) > 0)
                v = 10 * v + j;
        return v;
    }

    static boolean backupPosition(int i) {
        double t = 0;
        for (int j = 2; j <= 12; j++) {
            ArrayList<Integer> moves = nexts(i, j);
            double minVal = valueLabel(i);
            for (int m : moves) {
                int n = i & ~m;
                if (value[n] < -0.5)
                    return false;
                if (value[n] < minVal)
                    minVal = value[n];
            }
            t += prob(j) * minVal;
        }
        value[i] = t;
        return true;
    }

    static void backup() {
        value[0] = 0.0;
        boolean done;
        do {
            done = true;
            for (int i = 0; i < 512; i++) {
                if (value[i] > -0.5)
                    continue;
                boolean advanced = backupPosition(i);
                if (advanced)
                    done = false;
            }
        } while (!done);
    }

    static String choiceString(int c) {
        String r = "";
        for (int i = 1; i <= 9; i++)
            if ((c & bit(i)) > 0)
                r += i;
        return r;
    }

    static void printChoice(int c) {
        System.out.println(choiceString(c));
    }

    public static void testNexts(String[] args) {
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

    public static void main(String[] args) {
        backup();
        for (int i = 0; i < 512; i++)
            System.out.println(choiceString(i) + " " + value[i]);
    }
}
