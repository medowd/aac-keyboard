package keyboard;

import java.util.HashSet;
import java.util.Set;

public class Key {
	
	private int myID;
	private Set<Character> myLetters;
	
	public Key(int id) {
		myID = id;
		myLetters = new HashSet<Character>();
	}
	
	public int getMyID() {
		return myID;
	}

	public void setMyID(int myID) {
		this.myID = myID;
	}
	
	public void addLetter(Character letter) {
		myLetters.add(letter);
	}

	public Set<Character> getMyLetters() {
		return myLetters;
	}

	public void setMyLetters(Set<Character> myLetters) {
		this.myLetters = myLetters;
	}
}
