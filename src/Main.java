import lingolava.Legacy;
import lingologs.Script;
import lingologs.Texture;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    private static HashMap<Script, Double> values = new HashMap<>();
    private static Texture<Script> textsOrderedByEntropy;
    private static Texture<Script> textsOrderedBySubjectivity;

    public static void main(String[] args) throws IOException {
        textsOrderedBySubjectivity = new Texture<>(new Script("luko rinti futalis agila hundund vdh bzga nabu br nabuLeipzig").split());
        entropy();
        similarity();
    }

    private static void entropy() throws IOException {
        //for all texts
        File[] files = new File("src/texts").listFiles();
        for (File file : files) {
            if (file.isFile()) {
                Script name = Script.of(file.getName().replace("Text ", ""));
                name = name.replace(".txt", "");
                //calculated the entropy and saves it in a hasmap with their names
                values.put(Script.of(name), Entropy.getEntropy(Preprocess.getEditedTally(file.getPath())));
            }
        }
        //ranking, ordered by entropy
        textsOrderedByEntropy = new Texture<>(sortByValue(values).keySet());
        printOutcome();
    }

    private static void printOutcome() {
        System.out.println("Ranking der Texte nach mathematischem Informationsgehalt:\n" + textsOrderedByEntropy + "\n" +
                "Ranking der Texte nach subjektivem Informationsgehalt:\n" + textsOrderedBySubjectivity);

        System.out.println("Für die Levenshtein-Distanz ergibt sich aus den Rankings der Subjektivität und der Entropie " +
                "folgender Wert: " + textsOrderedBySubjectivity.distLeven(textsOrderedByEntropy) + "\n");
    }

    //sorts a hashmap by value
    public static HashMap<Script, Double> sortByValue(HashMap<Script, Double> unsorted) {
        List<Map.Entry<Script, Double>> list = new LinkedList<>(unsorted.entrySet());

        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        HashMap<Script, Double> sorted = new LinkedHashMap<>();
        for (Map.Entry<Script, Double> aa : list) {
            sorted.put(aa.getKey(), aa.getValue());
        }
        return sorted;
    }

    //tests: for EVERY measure of similarity with ALL modifications, with ALL different keyword groups, ONE specific text (variable)
    private static void similarity() throws IOException {
        //all similarity measures
        Legacy.Similitude[] similarityMeasures = new Legacy.Similitude[]{Legacy.Similitude.Cosine, Legacy.Similitude.LCS,
                Legacy.Similitude.Overlap, Legacy.Similitude.Jaccard, Legacy.Similitude.SorenDice};

        for (Legacy.Similitude simi : similarityMeasures) {
            //all keywords
            Texture<Script>[] keywords = new Texture[]{Preprocess.getKeywordsHund(), Preprocess.getKeywordsGesund(),
                    Preprocess.getKeywordsFressen(), Preprocess.getKeywordsFressenGesund(),
                    Preprocess.getKeywordsHundGesund(), Preprocess.getKeywordsHundFressen(), Preprocess.getAllKeywords()};

            for (Texture<Script> key : keywords) {
                Similarity.compareDifferentModifications(Preprocess.getTextAsTexture("src/texts/Text agila.txt"), key, simi);
            }
        }

        //tests: calculates for EVERY text, for EVERY measure of similarity, the value in accordance with ALL keywords (one group)
        File[] files = new File("src/texts").listFiles();
        for (File file : files) {
            if (file.isFile()) {
                Script name = Script.of(file.getName().replace("Text ", ""));
                name = name.replace(".txt", "");
                //values are stored in list with their names
                Similarity.addTextToAllSimilarityMeasures(Preprocess.getTextAsTexture(file.getPath()), Script.of(name));
            }
        }
        printSimOutcome();
    }

    private static void printSimOutcome() {
        //Similarity of the texts with all keywords - similarity to subjectivity?
        Texture<Script> textsOrderedByCosine = new Texture<>(Similarity.getCosineSimilarities().keySet());
        forEach(Script.of("COSINUS"), textsOrderedByCosine);
        Texture<Script> textsOrderedByJaccard = new Texture<>(Similarity.getJaccardSimilarities().keySet());
        forEach(Script.of("JACCARD"), textsOrderedByJaccard);
        Texture<Script> textsOrderedByLcs = new Texture<>(Similarity.getLcsSimilarities().keySet());
        forEach(Script.of("LCS"), textsOrderedByLcs);
        Texture<Script> textsOrderedByOverlap = new Texture<>(Similarity.getOverlapSimilarities().keySet());
        forEach(Script.of("OVERLAP"), textsOrderedByOverlap);
        Texture<Script> textsOrderedBySorenDice = new Texture<>(Similarity.getSorenDiceSimilarities().keySet());
        forEach(Script.of("SOREN DICE"), textsOrderedBySorenDice);

        System.out.println("Die Levenshtein-Distanz der Rankings des SorenDice-Koeffizienten und des Jaccard-Koeffizienten beträgt " +
                textsOrderedBySorenDice.distLeven(textsOrderedByJaccard));
    }

    private static void forEach(Script simi, Texture<Script> textsOrderedBySimi) {
        System.out.println("Ranking der Texte bzgl. ihrer Ähnlichkeit zu allen Keywords nach der " +  simi  + " Similarity: \n" +
                textsOrderedBySimi);

        System.out.println("Für die Levenshtein-Distanz ergibt sich aus den Rankings der Subjektivität und der " +  simi  +
                " Similarity folgender Wert: " + textsOrderedBySubjectivity.distLeven(textsOrderedBySimi) + "\n");
    }
}
