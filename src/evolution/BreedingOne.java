package evolution;

import java.util.ArrayList;
import java.util.List;

import keyboard.Key;
import keyboard.Keyboard;

public class BreedingOne extends Breeding {

	public BreedingOne(Keyboard a, Keyboard b) {
		super(a, b);
	}
	
	@Override
	public Keyboard breed() {
		List<KeyUnit> mutations = createWeightedMutations();
		
		int numMutations = myRandom.nextInt(MAXIMUM_MUTATIONS-MINIMUM_MUTATIONS) + MINIMUM_MUTATIONS;
		for (int i=0; i < numMutations; i++) {
			moveLetterToMutate(mutations);
		}
		keyboardA.calculateKeyboardCost();
		return keyboardA;
	}
	
	private List<KeyUnit> createWeightedMutations() {
		List<KeyUnit> mutations = new ArrayList<KeyUnit>();
		for (int i=0; i < similarityScores.length; i++) {
			Character c = ALPHABET.charAt(i);
			for (int j=0; j < similarityScores[0].length; j++) {
				int weight = similarityScores[i][j] == 0 ? 1 : 2*similarityScores[i][j];
				for (int k=0; k < weight; k++) {
					mutations.add(new KeyUnit(c, j));
				}
			}
		}
		return mutations;
	}

}
