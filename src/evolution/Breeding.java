package evolution;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import keyboard.Key;
import keyboard.Keyboard;

public abstract class Breeding {
	protected static String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	protected static int MINIMUM_MUTATIONS = 1;
	protected static int MAXIMUM_MUTATIONS = 3;
	
	protected Keyboard keyboardA;
	protected Keyboard keyboardB;
	protected Map<Character, Integer> aKeyMap;
	protected Map<Character, Integer> bKeyMap;
	protected int[][] similarityScores;
	
	protected Random myRandom;
	
	public Breeding(Keyboard a, Keyboard b) {
		keyboardA = a;
		keyboardB = b;
		aKeyMap = new HashMap<Character, Integer>();
		bKeyMap = new HashMap<Character, Integer>();
		similarityScores = new int[26][keyboardA.getMyKeys().size()];
		myRandom = new Random();
		buildKeyMaps();
		populateSimilarityScores();
	}
	
	private void buildKeyMaps() {
		buildKeyMap(keyboardA, aKeyMap);
		buildKeyMap(keyboardB, bKeyMap);
	}
	
	private void buildKeyMap(Keyboard board, Map<Character, Integer> map) {
		for (Key k : board.getMyKeys()) {
			Iterator<Character> iter = k.getMyLetters().iterator();
			while (iter.hasNext()) {
				map.put(iter.next(), k.getMyID());
			}
		}
	}
	
	public abstract Keyboard breed();
	
	protected void moveLetterToMutate(List<KeyUnit> mutations) {
		KeyUnit mutation = mutations.get(myRandom.nextInt(mutations.size()));
		removeLetterFromMutations(mutations, mutation.character);
		int startingKey = aKeyMap.get(mutation.character);
		List<Key> aKeys = keyboardA.getMyKeys();
		aKeys.get(startingKey).getMyLetters().remove(mutation.character);
		aKeys.get(mutation.destination).getMyLetters().add(mutation.character);
	}
	
	private void populateSimilarityScores() {
		for (Key k : keyboardA.getMyKeys()) {
			Iterator<Character> iter = k.getMyLetters().iterator();
			while (iter.hasNext()) {
				Character let = iter.next();
				int rowIdx = ALPHABET.indexOf(let);
				int bKeyNum = bKeyMap.get(let);
				Key bKey = keyboardB.getMyKeys().get(bKeyNum);
				Iterator<Character> bIter = bKey.getMyLetters().iterator();
				while (bIter.hasNext()) {
					Character commonLetter = bIter.next();
					int letterIdx = aKeyMap.get(commonLetter);
					similarityScores[rowIdx][letterIdx]++;
				}
			}
		}
	}
	
	protected List<KeyUnit> removeLetterFromMutations(List<KeyUnit> mutations, Character letter) {
		int i=0;
		while (i < mutations.size()) {
			if (mutations.get(i).character.equals(letter)) {
				mutations.remove(i);
			} else {
				i++;
			}
		}
		return mutations;
	}
	
	public void printSimilarityMatrix() {
		for (int i = 0; i < similarityScores.length; i++) {
			for (int j=0; j < similarityScores[0].length; j++) {
				System.out.print(Integer.toString(similarityScores[i][j]) + " ");
			}
			System.out.print("\n");
		}
	}

}
