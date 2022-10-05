import lingolava.Mathx;
import lingologs.Script;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Entropy {

    private static Map<Script, Integer> map;
    private static Collection<BigDecimal> collectionOfProbabilities;

    private static void calculateProbabilities() {
        collectionOfProbabilities = new ArrayList<>();

        //count total number, changed because of manipulated keywords
        double numberOfAllWords = 0;
        for (Integer i : map.values()) {
            numberOfAllWords = numberOfAllWords + i;
        }

        //calculate for every word the relative frequency
        for (Integer i : map.values()) {
            //calculation with BigDecimal is more precise
            BigDecimal occurence = new BigDecimal(i);
            BigDecimal allWords = new BigDecimal(numberOfAllWords);
            BigDecimal result = occurence.divide(allWords, 100, RoundingMode.HALF_UP);
            collectionOfProbabilities.add(result);
        }
    }

    public static double getEntropy(Map<Script, Integer> givenTally) {
        map = givenTally;
        calculateProbabilities();
        return Mathx.info(collectionOfProbabilities);
    }
}
