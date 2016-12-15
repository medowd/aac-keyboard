package multistart;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import keyboard.Key;
import wordlist.Word;

public class MultiStartAlgo {
	private static String FREQUENCY_ALPHABET = "eitarnoslcupdmhgyfbvwkxqjz";

	private CostCalculator myCalculator;
	private FixedFiveKeyboard myKeyboard;
	private List<Word> myWords;
	private BufferedWriter myWriter;
	private String myFilename;
	
	public MultiStartAlgo(List<Word> words, String filename) {
		myWords = words;
		myFilename = filename;
	}
	
	public void msa(double nMax) {
		for (int i=0; i< nMax; i++) {
			System.out.println(i);
			myKeyboard = new FixedFiveKeyboard();
			try {
				myWriter = new BufferedWriter(new FileWriter("COCA_wordlist/output/five/msa/" + myFilename, true));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				myWriter.write(myKeyboard.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myCalculator = new CostCalculator(myKeyboard, myWords);
			try {
				myWriter.write("Cost: " + Double.toString(myCalculator.calcTotalCost(i/nMax))+"\n");
				myWriter.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			findKeyboardLocalMinimum(i/nMax);
			int alphaSum = 0;
			for (Key k : myKeyboard.getMyKeys()) {
				alphaSum += k.getMyLetters().size();
			}
			if (alphaSum != 26) {
				System.out.println("BROKEN!!!!!!!!!!!");
				break;
			}
			try {
				myWriter.write("Top Keyboard\n");
				myWriter.write(myKeyboard.toString());
				myWriter.write("Cost: " + Double.toString(myCalculator.calcTotalCost(i/nMax))+"\n");
				myWriter.newLine();
				myWriter.flush();
				myWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void findKeyboardLocalMinimum(double alpha) {
		int alphabetIdx = 0;
		while (alphabetIdx < 26) {
			Character toMove = FREQUENCY_ALPHABET.charAt(alphabetIdx);
			int keyIdx = myKeyboard.getCharacterKey(toMove);
			double leastCost = myCalculator.calcTotalCost(alpha);
			boolean foundCheaper = false;
			for (int i=0; i < 5; i++) {
				myKeyboard.moveCharacterToKey(toMove, i);
				myCalculator = new CostCalculator(myKeyboard, myWords);
				double newCost = myCalculator.calcTotalCost(alpha);
				if (newCost < leastCost) {
					leastCost = newCost;
					alphabetIdx = 0;
					keyIdx = i;
					foundCheaper = true;
				}
			}
			myKeyboard.moveCharacterToKey(toMove, keyIdx);
			myCalculator = new CostCalculator(myKeyboard, myWords);
			if (!foundCheaper) {
				alphabetIdx++;
			} else {
				System.out.println(myCalculator.calcTotalCost(alpha));
				myKeyboard.printKeyboard();
			}
		}
	}

	public FixedFiveKeyboard getMyKeyboard() {
		return myKeyboard;
	}

	public void setMyKeyboard(FixedFiveKeyboard myKeyboard) {
		this.myKeyboard = myKeyboard;
	}
}
