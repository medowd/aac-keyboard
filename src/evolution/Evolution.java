package evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import keyboard.Key;
import keyboard.Keyboard;
import wordlist.Word;

public abstract class Evolution {
	protected static String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	
	protected Random myRandom;
	protected List<Keyboard> myKeyboards;
	protected Set<Word> myWords;
	
	public Evolution(Set<Word> words) {
		myRandom = new Random();
		myKeyboards = new ArrayList<Keyboard>();
		myWords = words;
	}
	
	protected Keyboard copyKeyboard(Keyboard board) {
		return board.copyKeyboard();
	}
	
	protected Keyboard createRandomKeyboard(int keyboardSize) {
		Keyboard kb = new Keyboard(myWords);
		List<Character> alphabet = createAlphabetList();
		for (int i=0; i < keyboardSize; i++) {
			kb.getMyKeys().add(new Key(i));
			kb.getMyKeys().get(i).addLetter(pickRandomLetter(alphabet));
		}
		while (alphabet.size() > 0) {
			int randomKey = myRandom.nextInt(keyboardSize);
			kb.getMyKeys().get(randomKey).addLetter(pickRandomLetter(alphabet));
		}
		kb.calculateKeyboardCost();
		return kb;
	}
	
	protected List<Character> createAlphabetList() {
		List<Character> alphaSet = new ArrayList<Character>();
		for (int i=0; i < ALPHABET.length(); i++) {
			alphaSet.add(ALPHABET.charAt(i));
		}
		return alphaSet;
	}
	
	protected Character pickRandomLetter(List<Character> alphabet) {
		int idx = myRandom.nextInt(alphabet.size());
		Character c = alphabet.get(idx);
		alphabet.remove(idx);
		return c;
	}

}
