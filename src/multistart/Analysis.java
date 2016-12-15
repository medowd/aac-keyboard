package multistart;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import wordlist.Word;

public class Analysis {
	
	private BufferedReader myReader;
	private List<Word> myWords;

	private double cheapestTyping = Double.MAX_VALUE;
	private double cheapestClashes = Double.MAX_VALUE;
	private int effKeyboardCount = 0;
	
	private FixedFiveKeyboard topTypingKeyboard;
	private FixedFiveKeyboard topClashesKeyboard;
	
	
	public Analysis(String filename, List<Word> words) {
		try {
			myReader = new BufferedReader(new FileReader("COCA_wordlist/output/five/msa/" + filename));
			myWords = words;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void process() {
		try {
			String line = myReader.readLine();
			while (line != null) {
				if (line.equals("Top Keyboard")) {
					myReader.readLine();
					String line1 = myReader.readLine().substring(1);
					line1 = line1.substring(0, line1.length()-1);
					String line2 = myReader.readLine().substring(1);
					line2 = line2.substring(0, line2.length()-1);
					FixedFiveKeyboard k = new FixedFiveKeyboard();
					String[] l1 = line1.split("\\|");
					for (int i=0; i < l1.length; i++) {
						String[] letters = l1[i].substring(1, l1[i].length()-1).split(", ");
						for (String l : letters) {
							k.getMyKeys().get(i).addLetter(l.toCharArray()[0]);
							k.getCharacterKeyMap().put(l.toCharArray()[0], i);
						}
					}
					String[] l2 = line2.split("\\|");
					for (int i=1; i < 3; i++) {
						String[] letters = l2[i].substring(1, l2[i].length()-1).split(", ");
						for (String l : letters) {
							k.getMyKeys().get(i+2).addLetter(l.toCharArray()[0]);
							k.getCharacterKeyMap().put(l.toCharArray()[0], i+2);
						}
					}
					CostCalculator c = new CostCalculator(k, myWords);
					if (c.getF_c() < cheapestClashes) {
						cheapestClashes = c.getF_c();
						topClashesKeyboard = k;
					}
					if (c.getF_c() < 25000000L) {
						if (c.getF_t() < cheapestTyping) {
							cheapestTyping = c.getF_t();
							topTypingKeyboard = k;
						}
						effKeyboardCount++;
					}
					
				}
				line = myReader.readLine();
			}
			System.out.println("Num Efficient Keyboards:");
			System.out.println(effKeyboardCount);
			myReader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FixedFiveKeyboard getTopTypingKeyboard() {
		return topTypingKeyboard;
	}

	public void setTopTypingKeyboard(FixedFiveKeyboard topTypingKeyboard) {
		this.topTypingKeyboard = topTypingKeyboard;
	}

	public FixedFiveKeyboard getTopClashesKeyboard() {
		return topClashesKeyboard;
	}

	public void setTopClashesKeyboard(FixedFiveKeyboard topClashesKeyboard) {
		this.topClashesKeyboard = topClashesKeyboard;
	}

	public double getCheapestTyping() {
		return cheapestTyping;
	}

	public void setCheapestTyping(double cheapestTyping) {
		this.cheapestTyping = cheapestTyping;
	}

	public double getCheapestClashes() {
		return cheapestClashes;
	}

	public void setCheapestClashes(double cheapestClashes) {
		this.cheapestClashes = cheapestClashes;
	}

}
