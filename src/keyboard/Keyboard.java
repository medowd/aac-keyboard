package keyboard;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import wordlist.Word;
import wordlist.WordComparator;

public class Keyboard {
	private static int WORD_SELECTOR_SIZE = 3;
	
	Set<Word> myWordSet;
	List<Key> myKeys;
	List<Word> myWords;
	float myCost;
	long totalWeight;
	
	public Keyboard(Set<Word> words) {
		myWordSet = words;
		myWords = words.stream().collect(Collectors.toList());
		Collections.sort(myWords, new WordComparator());
		sumTotalWeight();
		myKeys = new ArrayList<Key>();
	}
	
	private void sumTotalWeight() {
		long total = 0;
		for (Word w : myWords) {
			total += w.getFreq();
		}
		totalWeight = total;
	}
	
	public void calculateKeyboardCost() {
		float totalCost = 0;
		for (Word w : myWords) {
			totalCost += calculateWordCost(w.getWord())*(w.getFreq() / totalWeight);
		}
		myCost = totalCost;
	}
	
	private int calculateWordCost(String word) {
		StringBuffer prefix = new StringBuffer();
		List<Word> filteredWordList = myWords.stream().map(w -> new Word(w.getWord().toLowerCase(), (int)w.getFreq())).collect(Collectors.toList());
		int minCost = 1;
		int i = 0;
		while (prefix.length() < word.length() && filteredWordList.size() > WORD_SELECTOR_SIZE){
			if (wordInWordSelector(filteredWordList, word)) {
				break;
			}
			prefix.append(word.charAt(i));
			filteredWordList = filterWordsByPrefix(filteredWordList, prefix.toString());
			i++;
		}
		if (filteredWordList.size() > WORD_SELECTOR_SIZE && !wordInWordSelector(filteredWordList, word)) {
			minCost += 1;
			filteredWordList = filteredWordList.stream()
					.filter(w -> w.getWord().length() == word.length())
					.collect(Collectors.toList());
			minCost += word.length() + numClicksShiftWordSelector(filteredWordList, word);
		} else {
			minCost += prefix.length();
		}
		return minCost;
	}
	
	public void writeKeyboardFunctionality(String filename) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("typing/" + filename));
			for (Word w : myWords) {
				bw.write(showWordKeyPresses(w.getWord()) + "\n");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private String showWordKeyPresses(String word) {
		StringBuffer sb = new StringBuffer();
		sb.append(word);
		sb.append(": ");
		
		StringBuffer prefix = new StringBuffer();
		List<Word> filteredWordList = myWords.stream().map(w -> w).collect(Collectors.toList());
		int i = 0;
		while (prefix.length() < word.length() && filteredWordList.size() > WORD_SELECTOR_SIZE){
			if (wordInWordSelector(filteredWordList, word)) {
				break;
			}
			prefix.append(word.charAt(i));
			filteredWordList = filterWordsByPrefix(filteredWordList, prefix.toString());
			i++;
		}
		if (filteredWordList.size() > WORD_SELECTOR_SIZE && !wordInWordSelector(filteredWordList, word)) {
			filteredWordList = filteredWordList.stream()
					.filter(w -> w.getWord().length() == word.length())
					.collect(Collectors.toList());
			sb.append(prefixToString(word)+"next-"+clicksToString(numClicksShiftWordSelector(filteredWordList, word)));
		} else {
			sb.append(prefixToString(prefix.toString())+clicksToString(numClicksShiftWordSelector(filteredWordList, word)));
		}
		
		sb.append("select");
		return sb.toString();
	}
	
	public String wordListToString(List<Word> words) {
		return words.stream().map(w -> w.getWord()).collect(Collectors.toList()).toString();
	}
	
	private String clicksToString(int numClicks) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < numClicks; i++) {
			sb.append("next-");
		}
		return sb.toString();
	}
	
	private String prefixToString(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < s.length(); i++) {
			sb.append(s.charAt(i));
			sb.append('-');
		}
		return sb.toString();
	}
	
	public List<Word> filterWordsByPrefix(List<Word> matchingWords, String prefix) {
		for (int i = 0; i < prefix.length(); i++) {
			char c = prefix.charAt(i);
			for (Key k : myKeys) {
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
	
	private List<Word> showTopWords() {
		return myWords.subList(0, Math.min(WORD_SELECTOR_SIZE, myWords.size()));
	}
	
	private boolean wordInWordSelector(List<Word> wordSelector, String word) {
		for (int i=0; i < WORD_SELECTOR_SIZE; i++) {
			if (wordSelector.get(i).getWord().equals(word)) {
				return true;
			}
		}
		return false;
	}
	
	private int numClicksShiftWordSelector(List<Word> wordSelector, String word) {
		for (int i=0; i < wordSelector.size(); i++) {
			if (wordSelector.get(i).getWord().equals(word)) {
				return i / WORD_SELECTOR_SIZE;
			}
		}
		return -1;
	}

	public List<Key> getMyKeys() {
		return myKeys;
	}

	public void setMyKeys(List<Key> myKeyboard) {
		this.myKeys = myKeyboard;
	}

	public List<Word> getMyWords() {
		return myWords;
	}

	public void setMyWords(Set<Word> myWords) {
		this.myWords = myWords.stream().collect(Collectors.toList());
		Collections.sort(this.myWords, new WordComparator());
	}
	
	public void setMyWords(List<Word> myWords) {
		this.myWords = myWords;
		Collections.sort(this.myWords, new WordComparator());
	}
	
	public float getMyCost() {
		return myCost;
	}

	public void setMyCost(float myCost) {
		this.myCost = myCost;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Key key : myKeys) {
			sb.append(Integer.toString(key.getMyID()) + ": " + key.getMyLetters().toString() + "\n");
		}
		return sb.toString();
	}
	
	public Keyboard copyKeyboard() {
		Keyboard copy = new Keyboard(myWordSet);
		for (int i=0; i < myKeys.size(); i++) {
			Key orig = myKeys.get(i);
			Key keyCopy = new Key(i);
			Iterator<Character> iter = orig.getMyLetters().iterator();
			while (iter.hasNext()) {
				keyCopy.getMyLetters().add(iter.next());
			}
			copy.getMyKeys().add(keyCopy);
		}
		copy.setMyCost(myCost);
		return copy;
	}
	
	public boolean equals(Keyboard B) {
		int count = 0;
		for (Key k : this.myKeys) {
			for (Key bk : B.getMyKeys()) {
				if (k.getMyLetters().equals(bk.getMyLetters())) {
					count++;
				}
			}
		}
		return count == myKeys.size();
	}

}
