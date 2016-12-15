package evolution;

import java.util.Set;
import java.util.stream.Collectors;

import keyboard.Key;
import keyboard.Keyboard;
import wordlist.Word;

public class SimulatedAnnealing extends Evolution {
	
	private Keyboard topKeyboard;
	private Keyboard curKeyboard;
	private double temp;
	private double coolingRate;

	public SimulatedAnnealing(Set<Word> words, double temperature, double cooling, int keyboardSize) {
		super(words);
		temp = temperature;
		coolingRate = cooling;
		Keyboard k = createRandomKeyboard(keyboardSize);
		topKeyboard = k;
		curKeyboard = k;
		printCurState();
		System.out.println();
	}
	
	public SimulatedAnnealing(Set<Word> words, double temperature, double cooling, Keyboard k) {
		super(words);
		temp = temperature;
		coolingRate = cooling;
		topKeyboard = k;
		curKeyboard = k;
		printCurState();
		System.out.println();
	}
	
	private void printCurState() {
		System.out.println("Temp = " + Double.toString(temp));
		System.out.println("Starting keyboard: " + Float.toString(topKeyboard.getMyCost()));
		System.out.print(topKeyboard.toString());
	}
	
	private double acceptanceProbability(Keyboard newKeyboard) {
		if (newKeyboard.getMyCost() < curKeyboard.getMyCost()) {
			return 1;
		}
		return Math.exp((curKeyboard.getMyCost() - newKeyboard.getMyCost())*5000 / temp);
	}
	
	public Keyboard anneal() {
		
		while (temp > 1) {
			
			Keyboard newKeyboard = copyKeyboard(curKeyboard);
			mutateKeyboard(newKeyboard);
		
			double rand = Math.random();
			if (acceptanceProbability(newKeyboard) > rand) {
				curKeyboard = copyKeyboard(newKeyboard);
			}
			
			if (newKeyboard.getMyCost() < topKeyboard.getMyCost()) {
				topKeyboard = copyKeyboard(newKeyboard);
			}
			
			temp *= (1 - coolingRate);
		}
		
		return topKeyboard;
	}

	private void mutateKeyboard(Keyboard newKeyboard) {
		int randKeyIdx = myRandom.nextInt(newKeyboard.getMyKeys().size());
		Key randKey = newKeyboard.getMyKeys().get(randKeyIdx);
		while (randKey.getMyLetters().size() == 0) {
			randKeyIdx = myRandom.nextInt(newKeyboard.getMyKeys().size());
			randKey = newKeyboard.getMyKeys().get(randKeyIdx);
		}
		Character randCharacter = getRandomLetter(randKey);
		int randDestIdx = myRandom.nextInt(newKeyboard.getMyKeys().size()); 
		while (randDestIdx == randKeyIdx) {
			randDestIdx = myRandom.nextInt(newKeyboard.getMyKeys().size()); 
		}
		Key destKey = newKeyboard.getMyKeys().get(randDestIdx);
		randKey.getMyLetters().remove(randCharacter);
		destKey.getMyLetters().add(randCharacter);
		newKeyboard.calculateKeyboardCost();
	}
	
	private Character getRandomLetter(Key key) {
		int idx = myRandom.nextInt(key.getMyLetters().size());
		Character c = key.getMyLetters().stream().collect(Collectors.toList()).get(idx);
		return c;
	}

}
