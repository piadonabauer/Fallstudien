import lingologs.Script;
import lingologs.Texture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Preprocess {

    private static Script textAsScript;

    private static Texture<Script> keywordsHund = new Texture<>();
    private static Texture<Script> keywordsFressen = new Texture<>();
    private static Texture<Script> keywordsGesund = new Texture<>();


    public static Texture<Script> getTextAsTexture(String pathOfText) throws IOException {
        Path path = Paths.get(pathOfText);
        textAsScript = new Script(Files.readString(path));

        //separate by space
        Texture<Script> textAsTexture = new Texture<>(textAsScript.split());

        //remove everything but alphabetic letters
        for (Script word : textAsTexture) {
            Script filteredWord = word.toLower().replace("[^\\w]", "");
            textAsTexture = textAsTexture.replace(word, filteredWord);
        }

        //remove empty scripts
        textAsTexture = textAsTexture.filter(Script::isEmpty, true);

        //filter to remove stopwords
        Texture <Script> stopwords = new Texture<>(Ressources.stopwords.split());
        textAsTexture = textAsTexture.filter(stopwords.toSet(), true);

        return textAsTexture;
    }

    public static Map<Script, Integer> getEditedTally(String pathOfText) throws IOException {
        //every word to absolute frequency
        Map<Script, Integer> map = getTextAsTexture(pathOfText).tally();

        //Keywords
        int dogCount = 0;
        int foodCount = 0;
        int healthCount = 0;

        //checks for every word if it is a keywords - if yes, then it is added to the associated collection
        for (Script s : map.keySet().stream().toList()) {
            if (s.match(".*hund.*|.*welpe.*|vierbein.*")) {
                dogCount++;
                map.remove(s);
                keywordsHund = keywordsHund.add(s);
            } else if (s.match(".*fress.*|.*futter.*|.*fütter.*|.*nahrung.*|ernährung")) {
                foodCount++;
                map.remove(s);
                keywordsFressen = keywordsFressen.add(s);
            } else if (s.match(".*gesund.*|.*gewicht|.*krank.*|wohlbefinden|fett.*|dürr|körper.*|nährstoff.*")) {
                healthCount++;
                map.remove(s);
                keywordsGesund = keywordsGesund.add(s);
            }
        }

        //if keyword does not appear, it will not be added to the map (else 0 -> error)
        if (dogCount != 0) {
            //if it appears, it is saved as collection of synonyms
            map.put(Script.of("hund"), dogCount);
        }
        if (foodCount != 0) {
            map.put(Script.of("fressen"), foodCount);
        }
        if (healthCount != 0) {
            map.put(Script.of("gesund"), healthCount);
        }

        //now: collection of words and their frequencies
        return map;
    }

    public static Texture<Script> getKeywordsHund() {
        return keywordsHund;
    }

    public static Texture<Script> getKeywordsFressen() {
        return keywordsFressen;
    }

    public static Texture<Script> getKeywordsGesund() {
        return keywordsGesund;
    }

    public static Texture<Script> getKeywordsHundGesund() {
        Texture<Script> keywordsHundGesund = new Texture<>();
        keywordsHundGesund = keywordsHundGesund.add(keywordsHund);
        keywordsHundGesund = keywordsHundGesund.add(keywordsGesund);
        return keywordsHundGesund;
    }

    public static Texture<Script> getKeywordsFressenGesund() {
        Texture<Script> keywordsFressenGesund = new Texture<>();
        keywordsFressenGesund = keywordsFressenGesund.add(keywordsFressen);
        keywordsFressenGesund = keywordsFressenGesund.add(keywordsGesund);
        return keywordsFressenGesund;
    }

    public static Texture<Script> getKeywordsHundFressen() {
        Texture<Script> keywordsHundFressen = new Texture<>();
        keywordsHundFressen = keywordsHundFressen.add(keywordsFressen);
        keywordsHundFressen = keywordsHundFressen.add(keywordsHund);
        return keywordsHundFressen;
    }

    public static Texture<Script> getAllKeywords() {
        Texture<Script> allKeywords = new Texture<>();
        allKeywords = allKeywords.add(keywordsHund);
        allKeywords = allKeywords.add(keywordsFressen);
        allKeywords = allKeywords.add(keywordsGesund);
        return allKeywords;
    }
}