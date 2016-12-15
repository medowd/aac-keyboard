package wordlist;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PowerFileReader {
	private static int WORD_SELECTOR_SIZE = 3;

	private String myFilename;
	private Set<Word> myPowerWords;
	
	public PowerFileReader(String filename) {
		myFilename = filename;
		myPowerWords = new HashSet<Word>();
		readPowerWordsFile();
	}
	
	public void readPowerWordsFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/" + myFilename));
			String line = br.readLine();
			while (line != null) {
				String[] word = line.split(",");
				myPowerWords.add(new Word(word[1].substring(3).toLowerCase(), Integer.parseInt(word[3])));
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setWordPrefixes() {
		Iterator<Word> iter = myPowerWords.iterator();
		while (iter.hasNext()) {
			Word w = iter.next();
			String word = w.getWord();
			for (int i=1; i < word.length()+1; i++) {
				String prefix = word.substring(0, i);
				List<Word> filteredWords = myPowerWords.stream()
						.filter(powerWord -> powerWord.getWord().startsWith(prefix))
						.collect(Collectors.toList());
				Collections.sort(filteredWords, new WordComparator());
				if (filteredWords.subList(0, Math.min(WORD_SELECTOR_SIZE, filteredWords.size())).contains(w)) {
					w.setPrefix(prefix);
					break;
				}
			}
		}
	}
	
	public void addWordFrequencies() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/" + myFilename));
			BufferedWriter bw = new BufferedWriter(new FileWriter("data/freq_" + myFilename));
			String line = br.readLine();
			int i = 10000;
			while (line != null) {
				bw.write(line + "," + Integer.toString(i) + (i > 0 ? "\n" : ""));
				line = br.readLine();
				i--;
			}
			br.close();
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writePrefixWordList(String fileName) {
		readPowerWordsFile();
		setWordPrefixes();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("data/" + fileName));
			List<Word> powerWords = myPowerWords.stream().collect(Collectors.toList());
			Collections.sort(powerWords, new WordComparator());
			for (Word w : powerWords) {
				bw.write(w.getPrefix() + "," + Float.toString(w.getFreq()) + "," + w.getWord() + "\n");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void printUpdatedWordList() {
		Iterator<Word> iter = myPowerWords.iterator();
		while (iter.hasNext()) {
			Word w = iter.next();
			System.out.print(w.getWord() + ", ");
			System.out.print(w.getPrefix() + ", ");
			System.out.print(Float.toString(w.getFreq()) + "\n");
		}
	}
	
	public Set<Word> getMyPowerWords() {
		return myPowerWords;
	}

	public void setMyPowerWords(Set<Word> myPowerWords) {
		this.myPowerWords = myPowerWords;
	}
}
