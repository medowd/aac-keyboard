package evolution;

import java.util.ArrayList;
import java.util.List;

import keyboard.Keyboard;

public class BreedingTwo extends Breeding {
	
	private float[] probToMove;

	public BreedingTwo(Keyboard a, Keyboard b) {
		super(a, b);
		probToMove = new float[26];
	}

	@Override
	public Keyboard breed() {
		calculateProbabilityToMove();
		List<Character> weightedLetters = createWeightedLetterToMoveList();
		
		int numMutations = MAXIMUM_MUTATIONS-MINIMUM_MUTATIONS == 0 ? MINIMUM_MUTATIONS : 
			myRandom.nextInt(MAXIMUM_MUTATIONS-MINIMUM_MUTATIONS) + MINIMUM_MUTATIONS;
		for (int i=0; i < numMutations; i++) {
			Character letterToMove = weightedLetters.get(myRandom.nextInt(weightedLetters.size()));
			removeLetterFromCharacterList(weightedLetters, letterToMove);
			List<KeyUnit> mutations = generateWeightedMutationsForLetter(letterToMove);
			moveLetterToMutate(mutations);
		}	
		
		keyboardA.calculateKeyboardCost();
		return keyboardA;	
	}
	
	private void calculateProbabilityToMove() {
		for (int i=0; i < similarityScores.length; i++) {
			float sum = 0;
			for (int j=0; j < similarityScores[0].length; j++) {
				sum += similarityScores[i][j];
			}
			probToMove[i] = 1 - similarityScores[i][aKeyMap.get(ALPHABET.charAt(i))] / sum;
		}
		
		//normalize these scores
		float arraySum = 0;
		for (float n : probToMove) {
			arraySum += n;
		}
		for (int i=0; i < probToMove.length; i++) {
			probToMove[i] = probToMove[i] / arraySum;
		}
	}
	
	private List<Character> removeLetterFromCharacterList(List<Character> mutations, Character letter) {
		int i=0;
		while (i < mutations.size()) {
			if (mutations.get(i).equals(letter)) {
				mutations.remove(i);
			} else {
				i++;
			}
		}
		return mutations;
	}
	
	private List<Character> createWeightedLetterToMoveList() {
		List<Character> weightedCharacters = new ArrayList<Character>();
		for (int i=0; i < probToMove.length; i++) {
			for (int j=0; j < (int)(probToMove[i]*100); j++) {
				weightedCharacters.add(ALPHABET.charAt(i));
			}
		}
		return weightedCharacters;
	}
	
	private List<KeyUnit> generateWeightedMutationsForLetter(Character letter) {
		List<KeyUnit> mutations = new ArrayList<KeyUnit>();
		int[] simScores = similarityScores[ALPHABET.indexOf(letter)];
		int letterKeyIdx = aKeyMap.get(letter);
		float sum = 0;
		for (int i=0; i < simScores.length; i++) {
			if (i != letterKeyIdx) {
				sum += simScores[i]+1;
			}
		}
		for (int i=0; i < simScores.length; i++) {
			if (i != letterKeyIdx) {
				for (int j=0; j < (int)100*(simScores[i]+1)/sum; j++) {
					mutations.add(new KeyUnit(letter, i));
				}
			}
		}
		return mutations;
	}

}
