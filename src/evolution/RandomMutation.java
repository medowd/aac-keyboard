package evolution;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import keyboard.Key;
import keyboard.Keyboard;

public class RandomMutation {
	
	private Keyboard myKeyboard;
	private Random myRandom;
	
	public RandomMutation(Keyboard keyboard) {
		myKeyboard = keyboard;
		myRandom = new Random();
	}
	
	public Keyboard getMyKeyboard() {
		return myKeyboard;
	}

	public void setMyKeyboard(Keyboard myKeyboard) {
		this.myKeyboard = myKeyboard;
	}

	public void mutate(int minNumKeys, int maxNumKeys) {
		Runnable[] mutations = { () -> swapLetters(),
				() -> moveLetter(),
				() -> combineKeys(),
				() -> divideKeys()
		};
		String[] mutationLabels = { "swapLetters", "moveLetter", "combineKeys", "divideKeys"};
		int numMutations = myRandom.nextInt(3);
		for (int i=0; i <= numMutations; i++) {
			int mutationIdx = myRandom.nextInt(4);
			while (myKeyboard.getMyKeys().size() == minNumKeys && mutationIdx == 2) {
				mutationIdx = myRandom.nextInt(4);
			}
			while (myKeyboard.getMyKeys().size() == maxNumKeys && mutationIdx == 3) {
				mutationIdx = myRandom.nextInt(4);
			}
//			System.out.println("Applying mutation: " + mutationLabels[mutationIdx]);
			mutations[mutationIdx].run();
		}
		for (int i=0; i < myKeyboard.getMyKeys().size(); i++) {
			myKeyboard.getMyKeys().get(i).setMyID(i);
		}
		myKeyboard.calculateKeyboardCost();
	}
	
	private Key[] pickRandomKeys() {
		List<Key> keys = myKeyboard.getMyKeys();
		int keyboardSize = keys.size();
		int a = myRandom.nextInt(keyboardSize);
		int b = myRandom.nextInt(keyboardSize);
		while (b == a) {
			b = myRandom.nextInt(keyboardSize);
		}
		Key[] ab = {keys.get(a), keys.get(b)};
		return ab;
	}
	
	private Character getRandomLetter(Key key) {
		int idx = myRandom.nextInt(key.getMyLetters().size());
		Character c = key.getMyLetters().stream().collect(Collectors.toList()).get(idx);
		return c;
	}
	
	private void swapLetters() {
		Key[] keys = pickRandomKeys();
		Key aKey = keys[0];
		Key bKey = keys[1];
		Character aLetter = getRandomLetter(aKey);
		Character bLetter = getRandomLetter(bKey);
		aKey.getMyLetters().add(bLetter);
		aKey.getMyLetters().remove(aLetter);
		bKey.getMyLetters().add(aLetter);
		bKey.getMyLetters().remove(bLetter);
	}
	
	private void moveLetter() {
		Key[] keys = pickRandomKeys();
		Key aKey = keys[0];
		Key bKey = keys[1];
		Character aLetter = getRandomLetter(aKey);
		aKey.getMyLetters().remove(aLetter);
		if (aKey.getMyLetters().size() == 0) {
			myKeyboard.getMyKeys().remove(aKey);
		}
		bKey.getMyLetters().add(aLetter);
	}
	
	private void combineKeys() {
		Key[] keys = pickRandomKeys();
		Key aKey = keys[0];
		Key bKey = keys[1];
		aKey.getMyLetters().addAll(bKey.getMyLetters());
		myKeyboard.getMyKeys().remove(bKey);
	}
	
	private void divideKeys() {
		List<Key> keys = myKeyboard.getMyKeys();
		int keyboardSize = keys.size();
		
		int keyCount = 0;
		int idx = myRandom.nextInt(keyboardSize);
		Key key = myKeyboard.getMyKeys().get(idx);
		while (key.getMyLetters().size() == 1 && keyCount < myKeyboard.getMyKeys().size()) {
			keyCount++;
			idx = myRandom.nextInt(keyboardSize);
			key = myKeyboard.getMyKeys().get(idx);
		}
		if (keyCount >= myKeyboard.getMyKeys().size()) {
			return;
		}
		
		int keySize = key.getMyLetters().size() / 2;
		int i = 0;
		Key newKey = new Key(keyboardSize);
		while (i < keySize) {
			Character c = getRandomLetter(key);
			newKey.addLetter(c);
			key.getMyLetters().remove(c);
			i++;
		}
		myKeyboard.getMyKeys().add(newKey);
	}

}
