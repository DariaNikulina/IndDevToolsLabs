package org.example.lab1;

import java.util.Set;

public class WordFilter {
    private static final Set<String> EXCLUDED_WORDS = Set.of(
            "и", "в", "на", "с", "под", "по", "за", "без", "до", "от", "при", "к", "о", "об", "у",
            "а", "но", "или", "либо", "да", "же", "тоже", "также", "зато", "хотя", "поскольку",
            "из", "между", "около", "перед", "через", "для", "то", "тут", "он", "я", "ты", "мы", "вы", "они",
            "меня", "тебя", "нас", "вас", "его", "ее", "их", "мой", "твой", "наш", "ваш", "свой",
            "ах", "ох", "ой", "э", "ну", "давай", "вот", "это", "там", "где", "как"
    );

    public static boolean filterWord(String word) {
        return !word.isEmpty() && !EXCLUDED_WORDS.contains(word);
    }
}
