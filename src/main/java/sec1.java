import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class sec1 {
    public static void main(String[] args) {

    }

    public static void exercise1(int number) {
        double invert = (double) 1 / number;
        String binary = Integer.toBinaryString(number);
        System.out.printf("%s\n%o\n%x\n%a\n", binary, number, number, invert);

    }

    public static void exercise2(int number) {
        System.out.printf("%d\n", Math.floorMod(number, 360));
    }

    public static void exercise3(int... number) {
        System.out.printf("%d\n", Math.max(Math.max(number[0], number[1]),
                number[2]));
    }

    public static void exercise4(int number) {
        System.out.println(Math.nextUp(0));
        System.out.println((Double.MAX_VALUE));
    }

    public static void exercise5() {
        int x = (int) 99900000000.99999;
        System.out.println((9999999999999.99999));
        System.out.println(x);

    }

    public static void exercise6() {
        BigInteger n = BigInteger.valueOf(1);
        for (int i = 1; i <= 1000; i++) {
            n = n.multiply(BigInteger.valueOf(i));
        }
        System.out.println(n);
    }

    public static void exercise7() {

        int a = 999999999;
        int b = 65238841;
        System.out.printf("%s\n%s\n%s\n%s-%s\n", Integer.toUnsignedString(a + b),
                Integer.toUnsignedString(a - b), Integer.toUnsignedString(a * b),
                Integer.toUnsignedString(a / b), Integer.toUnsignedString(a % b));

    }

    public static void exercise8() {
        String string = "4654dsfdsf865";
        for (int i = 0; i < string.length(); i++) {
            for (int j = i + 1; j <= string.length(); j++) {
                System.out.println(string.substring(i, j));
            }
        }
    }

    public static void exercise9() {
        String string = "4654dsfdsf865";
        String s = string + "";
        System.out.printf("%b\n%b\n", string.equals(s), string == s);
    }

    public static void exercise10() {
        Random generator = new Random();
        long number = generator.nextLong();
        long f = number % 36;
        char x = (char) (f + 'a');
        System.out.println(x + "" + number);
    }

    public static void exercise11() {
        String string = "地方4546fsdf我啊葙♂";
        int length = string.length();
        int i = 0;
        int n = 0;
        while (i < length) {
            int j = string.offsetByCodePoints(i, 1);
            int codepoint = string.codePointAt(n);
            String s = string.substring(i, j);
            i = j;
            n++;
            if (codepoint >= 128) {
                System.out.printf("%s \\u%x\n", s, codepoint);
            }
        }
    }

    public static void exercise12() {
/*      Java SE 12 src.zip AccessControlContext.java 681th Row

        for (int i = 0; i < slen; i++) {
            ProtectionDomain sd = current[i];
            if (sd != null) {
                Boolean f = false;
                for (int j = 0; j < n; j++) {
                    if (sd == pd[j]) {
                        f = true;
                        break;
                    }
                }
            if (!f) pd[n++] = sd;
            }
        }
 */
    }

    public static void exercise13() {
        ArrayList<Integer> n = new ArrayList<>();
        for (int i = 0; i < 49; i++) {
            n.add(i + 1);
        }
        Random random = new Random();
        int index = random.nextInt(48);
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            result.add(n.get(index));
            n.remove(index);
            index = random.nextInt(48 - i - 1);
        }
        System.out.println(result);
    }

    public static void exercise14() {
        int[][] block = new int[4][4];
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int number = in.nextInt();
                block[i][j] = number;
            }
        }
        int rowSum = -1;
        int colSum = -1;
        boolean sc = true;
        for (int i = 0; i < 4; i++) {
            int rowTemp = 0;
            int colTemp = 0;
            for (int j = 0; j < 4; j++) {
                rowTemp += block[i][j];
                colTemp += block[j][i];
            }
            if (rowTemp != colTemp) {
                sc = false;
                break;
            } else if (rowSum == -1) {
                rowSum = rowTemp;
                colSum = colTemp;
            } else if (rowSum != rowTemp) {
                sc = false;
                break;
            }
        }
        int lcroTemp = 0;
        int rcroTemp = 0;
        for (int i = 0; i < 4; i++) {
            lcroTemp += block[i][i];
            rcroTemp += block[i][3 - i];
        }
        if (lcroTemp != rcroTemp) sc = false;
        else if (lcroTemp != colSum) sc = false;

        System.out.println(sc);
    }

    public static void exercise15() {
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> t = new ArrayList<>();
            for (int j = 0; j < i + 1; j++) t.add(0);
            t.set(0, 1);
            t.set(i, 1);
            if (i > 1) {
                ArrayList<Integer> last = list.get(i - 1);
                for (int j = 1; j < i; j++) {
                    t.set(j, last.get(j - 1) + last.get(j));
                }
            }
            list.add(t);
        }

        for (ArrayList<Integer> list1 : list) {
            for (Integer x : list1) {
                System.out.printf("%d ", x);
            }
            System.out.print('\n');
        }
    }

    // exercise16
    public static double average(double first, double... values) {
        double sum = first;
        for (double v : values) sum += v;
        return sum / (values.length + 1);
    }
}
