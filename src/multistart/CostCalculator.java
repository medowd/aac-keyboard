package multistart;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import keyboard.Key;
import wordlist.Word;

public class CostCalculator {
	
	private static long STANDARD_TYPING_COST = 11056887366L;	//for COCA 1000 words
	private static long STANDARD_CLASH_COST = 36304857L; 		//for COCA 1000 words
	private static int WORD_SELECTOR_SIZE = 3;
	
	private FixedFiveKeyboard myKeyboard;
	private List<Word> myWords;
	private Set<String> codedWords;
	
	private long f_t;
	private double f_c;
	
	public CostCalculator(FixedFiveKeyboard keyboard, List<Word> words) {
		myKeyboard = keyboard;
		myWords = words;
		codedWords = new HashSet<String>();
		
		calculateTypingCost();
		calculateClashesCost();

	}
	
	public double calcTotalCost(double alpha) {
		return alpha*(f_t / STANDARD_TYPING_COST) + (1 - alpha)*(f_c / STANDARD_CLASH_COST);
	}
	
	private void calculateTypingCost() {
		for (Word w : myWords) {
			f_t += wordCost(w)*w.getFreq();
		}
	}
	
	private double wordCost(Word word) {
		List<Word> filteredWords = myWords.stream().map(w -> w).collect(Collectors.toList());
		StringBuffer prefix = new StringBuffer();
		int wordSelectorIndex = wordInSelector(filteredWords, word);
		double cost = 0;
		char prevChar = ' '; 	//start on the space key
		int wordIdx = 0;
		while (wordSelectorIndex == -1 && prefix.length() < word.getWord().length()) {
			char nextChar = word.getWord().charAt(wordIdx);
			prefix.append(nextChar);
			cost += myKeyboard.characterDistance(prevChar, nextChar);
			prevChar = nextChar;
			wordIdx++;
			filteredWords = filterWordsByPrefix(filteredWords, prefix.toString());
			wordSelectorIndex = wordInSelector(filteredWords, word);
		}
		if (wordSelectorIndex == -1) {
			cost += myKeyboard.characterDistance(prevChar, ' ');
		} else {
			cost += myKeyboard.distanceToWordSelector(prevChar, wordSelectorIndex);
			cost += myKeyboard.distanceBetweenKeys(wordSelectorIndex, 9);
		}
		return cost;
	}
	
	public List<Word> filterWordsByPrefix(List<Word> matchingWords, String prefix) {
		for (int i = 0; i < prefix.length(); i++) {
			char c = prefix.charAt(i);
			for (Key k : myKeyboard.getMyKeys()) {
				if (k.getMyLetters().contains(c)) {
					int j = i;
					matchingWords = matchingWords.stream()
							.filter(w -> w.getWord().length() > j)
							.filter(w -> k.getMyLetters().contains(w.getWord().charAt(j)))
							.collect(Collectors.toList());
				}
			}
		}
		return matchingWords;
	}
	
	private int wordInSelector(List<Word> wordlist, Word w) {
		for (int i=0; i < Math.min(WORD_SELECTOR_SIZE, wordlist.size()); i++) {
			if (wordlist.get(i).getWord().equals(w.getWord())) {
				return i;
			}
		}
		return -1;
	}
	
	private void calculateClashesCost() {
		for (Word w : myWords) {
			String code = myKeyboard.wordToKeyCode(w.getWord());
			if (codedWords.contains(code)) {
				f_c += w.getFreq();
			} else {
				codedWords.add(code);
			}
		}
	}
	
	public long getF_t() {
		return f_t;
	}

	public void setF_t(long f_t) {
		this.f_t = f_t;
	}

	public double getF_c() {
		return f_c;
	}

	public void setF_c(double f_c) {
		this.f_c = f_c;
	}

}
