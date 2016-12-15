package keyboard;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import wordlist.Word;

public class KeyboardReader {
	
	private String myBaseFolder;
	private String myFilename;
	private Keyboard myKeyboard;
	
	public KeyboardReader(String baseFolder, String file, Set<Word> words) {
		myBaseFolder = baseFolder;
		myFilename = file;
		myKeyboard = new Keyboard(words);
		readKeyboardFile();
	}

	private void readKeyboardFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(myBaseFolder + "/keyboards/" + myFilename));
			String line = br.readLine();
			myKeyboard.setMyCost(Float.parseFloat(line));
			line = br.readLine();
			while (line != null) {
				String[] key = line.split(":");
				String[] letters = key[1].split(",");
				Key k = new Key(Integer.parseInt(key[0]));
				List<Character> characters = Arrays.asList(letters).stream()
						.map(ch -> Character.valueOf(ch.toCharArray()[0]))
						.collect(Collectors.toList());
				k.setMyLetters(new HashSet(characters));
				myKeyboard.getMyKeys().add(k);
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
	
	public Keyboard getKeyboard() {
		return myKeyboard;
	}

	public void setKeyboard(Keyboard myKeyboard) {
		this.myKeyboard = myKeyboard;
	}

}
