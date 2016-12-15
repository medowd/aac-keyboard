import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import evolution.BreedingTwo;
import evolution.EvolutionType;
import evolution.Selection;
import evolution.SimulatedAnnealing;
import keyboard.Key;
import keyboard.Keyboard;
import keyboard.KeyboardComparator;
import keyboard.KeyboardReader;
import multistart.Analysis;
import multistart.CostCalculator;
import multistart.FixedFiveKeyboard;
import multistart.MultiStartAlgo;
import wordlist.PowerFileReader;
import wordlist.Word;
import wordlist.WordComparator;

public class Main {
//	private static int NUM_GENERATIONS = 40;
	
	public static void main(String[] args) {	
		PowerFileReader freqFileReader = new PowerFileReader("COCA_top1000.csv");
		List<Word> words = freqFileReader.getMyPowerWords().stream().map(w -> w).collect(Collectors.toList());
		Collections.sort(words, new WordComparator());
		
		Analysis a = new Analysis("run1.txt", words);
		a.process();
		a.getTopClashesKeyboard().printKeyboard();
		System.out.println(a.getCheapestClashes());
		System.out.println();
		a.getTopTypingKeyboard().printKeyboard();
		System.out.println(a.getCheapestTyping());
		CostCalculator c = new CostCalculator(a.getTopTypingKeyboard(), words);
		System.out.println(c.getF_t());
		System.out.println(c.getF_c());
		
		
		
//		Selection ns = new Selection(freqFileReader.getMyPowerWords());
//		Keyboard winner = ns.evolve(NUM_GENERATIONS, EvolutionType.BREED_TWO);
//	
		
//		List<Keyboard> keyboards = initializeFiveKeyboards(freqFileReader);
//		KeyboardReader k = new KeyboardReader("seven/keyboard1.txt", freqFileReader.getMyPowerWords());
//		k.getKeyboard().writeKeyboardFunctionality("keyboard1b.txt");
//		List<Word> words = freqFileReader.getMyPowerWords()
//				.stream().map(w -> {return w;}).collect(Collectors.toList());
//		Collections.sort(words, new WordComparator());
//		System.out.println(k.getKeyboard().wordListToString(k.getKeyboard().filterWordsByPrefix(words, "the")));

//		List<Keyboard> keyboards = readAndInitializeKeyboards(freqFileReader, "COCA_wordlist", "five", 12);
//		Selection ns = new Selection(freqFileReader.getMyPowerWords(), keyboards);
//		Keyboard winner = ns.evolve(NUM_GENERATIONS, EvolutionType.BREED_TWO);
//		System.out.println("---TOP KEYBOARD---");
//		System.out.println("Cost: " + Float.toString(winner.getMyCost()));
//		System.out.print(winner.toString());
//		System.out.println();
//		Collections.sort(keyboards, new KeyboardComparator());
//		
//		for (int i=0; i < 5; i++) {
//			SimulatedAnnealing sa = new SimulatedAnnealing(freqFileReader.getMyPowerWords(), 1000.0, .003, keyboards.get(0));
//			Keyboard winner = sa.anneal();
//			
//			System.out.println("---TOP KEYBOARD---");
//			System.out.println("Cost: " + Float.toString(winner.getMyCost()));
//			System.out.print(winner.toString());
//			System.out.println();
//		}
//			
//		
		
	}
	
	private static List<Keyboard> readAndInitializeKeyboards(PowerFileReader freqFileReader, String baseFolder, String folderName, int numFiles) {
		List<Keyboard> keyboards = new ArrayList<Keyboard>();
		for (int i=1; i <= numFiles; i++) {
			String filename = folderName+"/keyboard"+Integer.toString(i)+".txt";
			KeyboardReader k = new KeyboardReader(baseFolder, filename, freqFileReader.getMyPowerWords());
			keyboards.add(k.getKeyboard());
		}
		return keyboards;
	}
	
	private static void initializeKeyboard(Keyboard k) {
		List<Key> keys = new ArrayList<Key>();
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		for (int i=0; i < alphabet.length(); i++) {
			Key key = new Key(i);
			key.addLetter(alphabet.charAt(i));
			keys.add(key);
		}
		k.setMyKeys(keys);
	}

}
