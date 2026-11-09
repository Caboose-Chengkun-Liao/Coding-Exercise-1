import java.util.*;

public class Solutions {
    private static int total = 0;
    private static int passed = 0;

    public static void main(String[] args) {
        Solutions s = new Solutions();

        System.out.println("===== 1. Third highest frequency =====");
        testFrequency(s, new char[]{'a', 'b', 'b', 'c', 'c', 'c'}, 'a');                 // example
        testFrequency(s, new char[]{'x', 'y', 'y', 'z', 'z', 'z', 'z', 'w', 'w', 'w'}, 'y'); // 4 levels
        testFrequency(s, "pqqrrrssssttttt".toCharArray(), 'r');                         // 5 levels
        testFrequency(s, new char[]{'c', 'a', 'c', 'b', 'c', 'a', 'd', 'd', 'd', 'd'}, 'a'); // shuffled
        testFrequency(s, new char[]{'A', 'a', 'a', 'B', 'B', 'B'}, 'A');                 // case-sensitive
        testFrequency(s, new char[]{'1', '2', '2', '3', '3', '3', '!', '!', '!', '!'}, '2'); // digits/symbols

        System.out.println();
        System.out.println("===== 2. Reverse a string =====");
        testReverse(s, "abc", "cba");                    // example
        testReverse(s, "a", "a");                        // single char
        testReverse(s, "", "");                          // empty string
        testReverse(s, "racecar", "racecar");            // palindrome
        testReverse(s, "ab", "ba");                      // even length
        testReverse(s, "hello world", "dlrow olleh");    // with space
        testReverse(s, "Java 8!", "!8 avaJ");            // mixed chars
        testReverse(s, null, null);                      // null input

        System.out.println();
        System.out.println("===== 3. Pairs that sum to target =====");
        testPairs(s, new int[]{1, 2, 3, 4}, 5, new int[][]{{1, 4}, {2, 3}});            // example
        testPairs(s, new int[]{4, 3, 2, 1}, 5, new int[][]{{1, 4}, {2, 3}});            // unsorted
        testPairs(s, new int[]{1, 1, 4, 4, 2}, 5, new int[][]{{1, 4}, {1, 4}});         // duplicates
        testPairs(s, new int[]{2, 2, 2, 2}, 4, new int[][]{{2, 2}, {2, 2}});            // all same
        testPairs(s, new int[]{3, 3, 3}, 6, new int[][]{{3, 3}});                       // each used once
        testPairs(s, new int[]{-1, 0, 1, 2, -2}, 0, new int[][]{{-2, 2}, {-1, 1}});     // negatives
        testPairs(s, new int[]{0, 0, 5, 5}, 5, new int[][]{{0, 5}, {0, 5}});            // zeros
        testPairs(s, new int[]{1, 2, 3}, 10, new int[0][]);                             // no pair
        testPairs(s, new int[]{5}, 5, new int[0][]);                                    // one element
        testPairs(s, new int[]{}, 5, new int[0][]);                                     // empty array

        // The method sorts a copy, so the caller's array must stay the same
        int[] original = {4, 3, 2, 1};
        s.pairsThatMatch(original, 5);
        report("original array after pairsThatMatch", "[4, 3, 2, 1]",
                Arrays.toString(original), Arrays.equals(original, new int[]{4, 3, 2, 1}));

        System.out.println();
        System.out.println("Passed " + passed + " / " + total + " tests");
    }

    private static void testFrequency(Solutions s, char[] input, char expected) {
        char actual = s.thirdHighestFrequency(input);
        report("highestFrequency(" + Arrays.toString(input) + ")",
                String.valueOf(expected), String.valueOf(actual), actual == expected);
    }

    private static void testReverse(Solutions s, String input, String expected) {
        String actual = s.reverse(input);
        report("reverse(" + quote(input) + ")",
                quote(expected), quote(actual), Objects.equals(actual, expected));
    }

    private static void testPairs(Solutions s, int[] nums, int target, int[][] expected) {
        int[][] actual = s.pairsThatMatch(nums, target);
        report("pairsThatMatch(" + Arrays.toString(nums) + ", " + target + ")",
                Arrays.deepToString(expected), Arrays.deepToString(actual),
                Arrays.deepEquals(actual, expected));
    }

    private static void report(String call, String expected, String actual, boolean ok) {
        total++;
        if (ok) {
            passed++;
        }
        System.out.println((ok ? "[PASS] " : "[FAIL] ") + call
                + " -> " + actual + "   (expected " + expected + ")");
    }

    private static String quote(String str) {
        return str == null ? "null" : "\"" + str + "\"";
    }

    public char thirdHighestFrequency(char[] input) {
        Map<Character, Integer> map = new HashMap<>();

        for (char c : input) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        int first = 0, second = 0, third = 0;
        char firstChar = ' ', secondChar = ' ', thirdChar = ' ';
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            char c = entry.getKey();
            int f = entry.getValue();
            if (f > first) {
                third = second;
                thirdChar = secondChar;
                second = first;
                secondChar = firstChar;
                first = f;
                firstChar = c;
            }
            else if (f > second) {
                third = second;
                thirdChar = secondChar;
                second = f;
                secondChar = c;
            }
            else if (f > third) {
                third = f;
                thirdChar = c;
            }
        }
        return thirdChar;
    }

    public String reverse(String input) {
        if (input == null) {
            return null;
        }

        char[] arr = input.toCharArray();
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            char temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }

        return new String(arr);
    }

    public int[][] pairsThatMatch(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return new int[0][];
        }

        int[] sorted = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sorted);

        List<int[]> pairs = new ArrayList<>();
        int left = 0;
        int right = sorted.length - 1;

        while (left < right) {
            long sum = (long) sorted[left] + sorted[right];
            if (sum == target) {
                pairs.add(new int[]{sorted[left], sorted[right]});
                left++;
                right--;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        return pairs.toArray(new int[0][]);
    }
}