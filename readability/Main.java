package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
//        String[] arr = "caene".split("[aeiouy]{1,}", -1);
//        System.out.println(arr.length);
//        for (int i = 0; i < arr.length; i++) {
//            System.out.println(arr[i]);
//        }

        File file = new File(args[0]);
        Scanner scanner = new Scanner(file);
        int cntSentences = 0;
        int cntWords = 0;
        int cntSyllables = 0;
        int cntPolysyllables = 0;
        int cntCharacters = 0;
        double score;
        String lastWord = "";

        System.out.println("The text is:");
        while (scanner.hasNextLine()) {
            String row = scanner.nextLine();
            System.out.println(row);
            String[] words = row.split("\\s+");
            cntWords += words.length;
            for (String word : words) {
                int tmpCountSyllables = calcSyllables(word);
                cntSyllables += tmpCountSyllables == 0 ? 1 : tmpCountSyllables;
                if (tmpCountSyllables > 2) {
                    cntPolysyllables++;
                }
                lastWord = word;
                cntCharacters += word.length();
                if (word.matches(".+[!?.]")) {
                    cntSentences++;
                }
            }
        }

        if (!lastWord.matches(".+[!?.]")) {
            cntSentences++;
        }


        System.out.println();
        System.out.printf("Words: %d\n" +
                "Sentences: %d\n" +
                "Characters: %d\n" +
                "Syllables: %d\n" +
                "Polysyllables: %d\n", cntWords, cntSentences, cntCharacters, cntSyllables, cntPolysyllables);

        scanner.close();

        scanner = new Scanner(System.in);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        System.out.println();
        switch (scanner.nextLine()) {
            case "ARI":
                score = calcARI(cntSentences, cntWords, cntCharacters);
                System.out.printf("Automated Readability Index: %.2f (about %s-year-olds).\n",
                        score, getAges(score));
                break;
            case "FK":
                score = calcFK(cntSentences, cntWords, cntSyllables);
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s-year-olds).\n",
                        score, getAges(score));
                break;
            case "SMOG":
                score = calcSMOG(cntSentences, cntPolysyllables);
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s-year-olds).\n",
                        score, getAges(score));
                break;
            case "CL":
                score = calcCL(cntSentences, cntWords, cntCharacters);
                System.out.printf("Coleman–Liau index: %.2f (about %s-year-olds).\n",
                        score, getAges(score));
                break;
            default:
                int sum = 0;

                score = calcARI(cntSentences, cntWords, cntCharacters);
                System.out.printf("Automated Readability Index: %.2f (about %s-year-olds).\n",
                        score, getAges(score));
                //sum += Integer.valueOf(getAges(score));

                score = calcFK(cntSentences, cntWords, cntSyllables);
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s-year-olds).\n",
                        score, getAges(score));
                //sum += Integer.valueOf(getAges(score));

                score = calcSMOG(cntSentences, cntPolysyllables);
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s-year-olds).\n",
                        score, getAges(score));
                //sum += Integer.valueOf(getAges(score));

                score = calcCL(cntSentences, cntWords, cntCharacters);
                System.out.printf("Coleman–Liau index: %.2f (about %s-year-olds).\n",
                        score, getAges(score));
                //sum += Integer.valueOf(getAges(score));

                System.out.println();
                System.out.printf("This text should be understood in average by %.2f-year-olds.",
                        1.0 * sum / 4);
        }
        scanner.close();
    }

    private static int calcSyllables(String word) {
        String tmpWord = word.replaceAll("[!?.]", "");
        if (tmpWord.endsWith("e")) {
            tmpWord = tmpWord.substring(0, tmpWord.length() - 1);
        }
        return tmpWord.split("[AEIOUYaeiouy]{1,}", -1).length - 1;
    }

    private static double calcARI(int cntSentences, int cntWords, int cntCharacters) {
        return 4.71 * cntCharacters / cntWords + 0.5 * cntWords / cntSentences - 21.43;
    }

    private static double calcFK(int cntSentences, int cntWords, int cntSyllables) {
        return 0.39 * cntWords / cntSentences + 11.8 * cntSyllables / cntWords - 15.59;
    }

    private static double calcSMOG(int cntSentences, int cntPolySyllables) {
        return 1.043 * Math.sqrt(cntPolySyllables * 30.0 / cntSentences) + 3.1291;
    }

    private static double calcCL(int cntSentences, int cntWords, int cntCharacters) {
        double L = 1.0 * cntCharacters / cntWords * 100;
        double S = 1.0 * cntSentences / cntWords * 100;
        return 0.0588 * L - 0.296 * S - 15.8;
    }

    private static String getAges(double score) {
        String ages;
        score = Math.round(score);

        switch ((int) score) {
            case 1:
                ages = "6";
                break;
            case 2:
                ages = "7";
                break;
            case 3:
                ages = "9";
                break;
            case 13:
                ages = "24";
                break;
            case 14:
                ages = "24+";
                break;
            default:
                ages = String.valueOf((int) score + 6);
        }
        return ages;
    }
}
