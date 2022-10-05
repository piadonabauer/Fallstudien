import lingolava.Legacy;
import lingologs.Script;
import lingologs.Texture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Similarity {

    private static HashMap<Script, Double> cosineSimilarities = new HashMap<>();
    private static HashMap<Script, Double> jaccardSimilarities = new HashMap<>();
    private static HashMap<Script, Double> lcsSimilarities = new HashMap<>();
    private static HashMap<Script, Double> overlapSimilarities = new HashMap<>();
    private static HashMap<Script, Double> sorenDiceSimilarities = new HashMap<>();

    //compares the different modifications of the similares-method
    public static void compareDifferentModifications(Texture<Script> text, Texture<Script> keywords, Legacy.Similitude simi) {
        System.out.println(
                "---" + simi + "---" +  "\n" +
                        "n-Gram: 1, Multiset: true -> " + keywords.similares(text, simi, 1, true) + "\n" +
                        "n-Gram: 2, Multiset: true -> " + keywords.similares(text, simi, 2, true) + "\n" +
                        "n-Gram: 1, Multiset: false -> " + keywords.similares(text, simi, 1, false) + "\n" +
                        "n-Gram: 2, Multiset: false -> " + keywords.similares(text, simi, 2, false) + "\n");
    }

    //calculates the similarity of a text to all keywords, for each measure
    public static void addTextToAllSimilarityMeasures(Texture<Script> text, Script name) {
        cosineSimilarities.put(name, (Preprocess.getAllKeywords().similares(text, Legacy.Similitude.Cosine, 1, true)));
        jaccardSimilarities.put(name, (Preprocess.getAllKeywords().similares(text, Legacy.Similitude.Jaccard, 1, true)));
        lcsSimilarities.put(name, (Preprocess.getAllKeywords().similares(text, Legacy.Similitude.LCS, 1, true)));
        overlapSimilarities.put(name, (Preprocess.getAllKeywords().similares(text, Legacy.Similitude.Overlap, 1, true)));
        sorenDiceSimilarities.put(name, (Preprocess.getAllKeywords().similares(text, Legacy.Similitude.SorenDice, 1, true)));
    }

    public static HashMap<Script, Double> getCosineSimilarities() {
        return Main.sortByValue(cosineSimilarities);
    }

    public static HashMap<Script, Double> getJaccardSimilarities() {
        return Main.sortByValue(jaccardSimilarities);
    }

    public static HashMap<Script, Double> getLcsSimilarities() {
        return Main.sortByValue(lcsSimilarities);
    }

    public static HashMap<Script, Double> getOverlapSimilarities() {
        return Main.sortByValue(overlapSimilarities);
    }

    public static HashMap<Script, Double> getSorenDiceSimilarities() {
        return Main.sortByValue(sorenDiceSimilarities);
    }




}
