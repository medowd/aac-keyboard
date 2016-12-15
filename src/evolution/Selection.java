package evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import keyboard.Key;
import keyboard.Keyboard;
import keyboard.KeyboardComparator;
import wordlist.Word;

public class Selection extends Evolution {
	private static int NUM_KEYBOARDS = 6;
	
	private static int MIN_NUM_KEYS = 5;
	private static int MAX_NUM_KEYS = 12;
	
	private int keyboardSize;
	
	public Selection(Set<Word> words, int size) {
		super(words);
		keyboardSize = size;
		initializeKeyboards();
	}
	
	public Selection(Set<Word> words, List<Keyboard> keyboards) {
		super(words);
		Collections.sort(keyboards, new KeyboardComparator());
		myKeyboards = keyboards.subList(0, NUM_KEYBOARDS);
		System.out.println("Keyboards initialized.");
		printKeyboards();
	}
	
	private void initializeKeyboards() {
		System.out.println("Initializing keyboards....");

		for (int i=0; i < NUM_KEYBOARDS; i++) {
			myKeyboards.add(createRandomKeyboard(keyboardSize));
		}
		Collections.sort(myKeyboards, new KeyboardComparator());
		System.out.println("Keyboards initialized.");
		printKeyboards();
	}

	private void printKeyboards() {
		System.out.println();
		for (Keyboard kb : myKeyboards) {
			System.out.println("Cost: " + Float.toString(kb.getMyCost()));
			System.out.println(kb.toString());
		}
	}
	
	public Keyboard evolve(int n, EvolutionType e) {
		for (int i=0; i < n; i++) {
			switch (e) {
			case RANDOM:
				completelyRandomMutate();
				break;
			case RANDOM_MUTATION:
				randomMutateOneLevel();
				break;
			case BREED_ONE:
				breedOneLevel(1);
				break;
			case BREED_TWO:
				breedOneLevel(2);
				break;
			}
			
			System.out.println("---EVOLUTION LEVEL " + Integer.toString(i+1) + "---");
			printKeyboards();
		}
		return myKeyboards.get(0);
	}
	
	private void completelyRandomMutate() {
		for (int i=0; i < NUM_KEYBOARDS*NUM_KEYBOARDS; i++) {
			myKeyboards.add(createRandomKeyboard(keyboardSize));
		}
		Collections.sort(myKeyboards, new KeyboardComparator());
		myKeyboards = myKeyboards.subList(0, NUM_KEYBOARDS);
	}
	
	private void randomMutateOneLevel() {
		List<Keyboard> mutantKeyboards = myKeyboards.stream()
				.map(keyboard -> generateMutants(keyboard, NUM_KEYBOARDS))
				.reduce(new ArrayList<Keyboard>(), (a,b) -> flatten(a,b));
		mutantKeyboards.addAll(myKeyboards);
		Collections.sort(mutantKeyboards, new KeyboardComparator());
		
//		System.out.println("TOTAL MUTANT LIST");
//		for (Keyboard kb : mutantKeyboards) {
//			System.out.println(kb.getMyCost());
//			System.out.println(kb.toString());
//		}
		
		List<Keyboard> topKeyboards = mutantKeyboards.subList(0, NUM_KEYBOARDS);
		myKeyboards.clear();
		myKeyboards.addAll(topKeyboards);
	}
	
	private void breedOneLevel(int breedingType) {
		List<Keyboard> offspring = new ArrayList<Keyboard>();
		for (int i=0; i < NUM_KEYBOARDS-1; i++) {
			for (int j=i+1; j < NUM_KEYBOARDS; j++) {
				Keyboard keyboardToMutate = myKeyboards.get(j).copyKeyboard();
				Keyboard otherKeyboard = myKeyboards.get(i);
				Breeding b = breedingType == 1 ? new BreedingOne(keyboardToMutate, otherKeyboard)
						: new BreedingTwo(keyboardToMutate, otherKeyboard);
				offspring.add(b.breed());
				
				keyboardToMutate = myKeyboards.get(i).copyKeyboard();
				otherKeyboard = myKeyboards.get(j);
				b = breedingType == 1 ? new BreedingOne(keyboardToMutate, otherKeyboard)
						: new BreedingTwo(keyboardToMutate, otherKeyboard);
				offspring.add(b.breed());
			}
		}
		offspring.addAll(myKeyboards);
		offspring = removeDuplicates(offspring);
		Collections.sort(offspring, new KeyboardComparator());
		myKeyboards.clear();
		myKeyboards.addAll(offspring.subList(0, NUM_KEYBOARDS));
	}
	
	private List<Keyboard> flatten(List<Keyboard> a, List<Keyboard> b) {
		a.addAll(b);
		return a;
	}
	
	private List<Keyboard> generateMutants(Keyboard board, int n) {
		List<Keyboard> mutants = new ArrayList<Keyboard>();
		for (int i=0; i < n; i++) {
			Keyboard copy = copyKeyboard(board);
			RandomMutation mutant = new RandomMutation(copy);
			mutant.mutate(MIN_NUM_KEYS, MAX_NUM_KEYS);
			mutants.add(mutant.getMyKeyboard());
		}
		return mutants;
	}
	
	private List<Keyboard> removeDuplicates(List<Keyboard> keyboards) {
		List<Keyboard> uniqueKeyboards = new ArrayList<Keyboard>();
		for (Keyboard k : keyboards) {
			if (!containsKeyboard(uniqueKeyboards, k)) {
				uniqueKeyboards.add(k);
			}
		}
		return uniqueKeyboards;
	}
	
	private boolean containsKeyboard(List<Keyboard> keyboards, Keyboard kb) {
		for (Keyboard k : keyboards) {
			if (k.equals(kb)) {
				return true;
			}
		}
		return false;
	}
	
}
