package multistart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import keyboard.Key;
import keyboard.Keyboard;
import wordlist.Word;

public class FixedFiveKeyboard {
	private static String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	
	private static int[] LETTER_KEYS = {3, 4, 5, 7, 8};

	private List<Key> myKeys;
	private Map<Character, Integer> characterKeyMap;
	private double[][] distanceMatrix;
	
	public FixedFiveKeyboard() {
		myKeys = new ArrayList<Key>();
		for (int i=0; i < 5; i++) {
			myKeys.add(new Key(i));
		}
		characterKeyMap = new HashMap<Character, Integer>();
		initializeDistanceMatrix();
		
	}
	
	private void initializeStandardKeyboard() {
		Key k0 = new Key(0);
		Key k1 = new Key(0);
		Key k2 = new Key(0);
		Key k3 = new Key(0);
		Key k4 = new Key(0);
		Key[] keys = {k0, k1, k2, k3, k4};
		String[] alpha = {"abcde", "fghij", "klmno", "pqrstu", "vwxyz"};
		
		for (int i=0; i < keys.length; i++) {
			Set<Character> letters = keys[i].getMyLetters();
			for (int j=0; j < alpha[i].length(); j++) {
				letters.add(alpha[i].charAt(j));
				characterKeyMap.put(alpha[i].charAt(j), i);
			}
			myKeys.add(keys[i]);
		}

	}
	
	private void initializeDistanceMatrix() {
		distanceMatrix = new double[10][10];
		List<double[]> coords = initializeKeyCoordinates();
		for (int i=0; i < distanceMatrix.length; i++) {
			for (int j=i; j < distanceMatrix[0].length; j++) {
				if (i == j) {
					distanceMatrix[i][j] = 1.0;
				} else {
					double[] a = coords.get(i);
					double[] b = coords.get(j);
					double dist = distance(a[0], a[1], b[0], b[1]) / 2;
					distanceMatrix[i][j] = dist;
					distanceMatrix[j][i] = dist;
				}
			}
		}
	}
	
	private List<double[]> initializeKeyCoordinates() {
		List<double[]> coords = new ArrayList<double[]>();
		double[] k0 = {6, 17.5};
		double[] k1 = {12, 17.5};
		double[] k2 = {24, 17.5};
		double[] k3 = {7, 10.5};
		double[] k4 = {21, 10.5};
		double[] k5 = {35, 10.5};
		double[] k6 = {3.5, 3.5};
		double[] k7 = {14, 3.5};
		double[] k8 = {28, 3.5};
		double[] k9 = {38.5, 3.5};
		coords.add(k0);
		coords.add(k1);
		coords.add(k2);
		coords.add(k3);
		coords.add(k4);
		coords.add(k5);
		coords.add(k6);
		coords.add(k7);
		coords.add(k8);
		coords.add(k9);
		return coords;
	}
	
	private double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1-y2, 2));
	}
	
	private int keyIdx(Character b) {
		return b == ' ' ? 9 : (ALPHABET.indexOf(b) == -1 ? 6 : LETTER_KEYS[characterKeyMap.get(b)]);
	}
	
	/**
	 * cost of moving from character a to character b
	 */
	public double characterDistance(Character a, Character b) {
		int aKey = keyIdx(a);
		int bKey = keyIdx(b);
		return distanceMatrix[aKey][bKey];
	}
	
	public double distanceToWordSelector(Character a, int wordSelectorIdx) {
		int aKey = keyIdx(a);
		return distanceMatrix[aKey][wordSelectorIdx];
	}
	
	public double distanceBetweenKeys(int i, int j) {
		return distanceMatrix[i][j];
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < 3; i++) {
			sb.append("| word" + Integer.toString(i) + " ");
		}
		sb.append("| n -> |");
		sb.append("\n");
		for (int i=0; i < 3; i++) {
			sb.append("|" + myKeys.get(i).getMyLetters().toString());
		}
		sb.append("|");
		sb.append("\n");
		sb.append("| ' ");
		for (int i=3; i < 5; i++) {
			sb.append("|" + myKeys.get(i).getMyLetters().toString());
		}
		sb.append("|");
		sb.append(" _ |");
		sb.append("\n");
		return sb.toString();
	}
	
	public void printKeyboard() {
		for (int i=0; i < 3; i++) {
			System.out.print("| word" + Integer.toString(i) + " ");
		}
		System.out.print("| n -> |");
		System.out.print("\n");
		for (int i=0; i < 3; i++) {
			System.out.print("|" + myKeys.get(i).getMyLetters().toString());
		}
		System.out.print("|");
		System.out.print("\n");
		System.out.print("|  '  ");
		for (int i=3; i < 5; i++) {
			System.out.print("|" + myKeys.get(i).getMyLetters().toString());
		}
		System.out.print("|");
		System.out.print(" _ |");
		System.out.println();
	}
	
	private void printDistanceMatrix() {
		for (int i=0; i < distanceMatrix.length; i++) {
			for (int j=0; j < distanceMatrix[0].length; j++) {
				String dist = Double.toString(distanceMatrix[i][j]);
				System.out.print(dist.substring(0, Math.min(dist.length(), 4)) + "\t");
			}
			System.out.print("\n");
		}
	}
	
	public List<Key> getMyKeys() {
		return myKeys;
	}

	public void setMyKeys(List<Key> myKeys) {
		this.myKeys = myKeys;
	}

	public Map<Character, Integer> getCharacterKeyMap() {
		return characterKeyMap;
	}

	public void setCharacterKeyMap(Map<Character, Integer> characterKeyMap) {
		this.characterKeyMap = characterKeyMap;
	}

	public double[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	public void setDistanceMatrix(double[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}
	
	public String wordToKeyCode(String w) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < w.length(); i++) {
			if (ALPHABET.indexOf(w.charAt(i)) == -1) {
				sb.append(w.charAt(i));
			} else {
				sb.append(Integer.toString(characterKeyMap.get(w.charAt(i))));
			}
		}
		return sb.toString();
	}
	
	public int getCharacterKey(Character a) {
		return characterKeyMap.get(a);
	}
	
	public void moveCharacterToKey(Character a, int keyIdx) {
		int aKey = characterKeyMap.get(a);
		myKeys.get(aKey).getMyLetters().remove(a);
		myKeys.get(keyIdx).getMyLetters().add(a);
		characterKeyMap.put(a, keyIdx);
	}
	
	private void createRandomKeyboard() {
		Random r = new Random();
		List<Character> alpha = new ArrayList<Character>();
		for (int i=0; i < ALPHABET.length(); i++) {
			alpha.add(ALPHABET.charAt(i));
		}
		while (alpha.size() > 0) {
			int keyIdx = r.nextInt(5);
			int alphaIdx = r.nextInt(alpha.size());
			myKeys.get(keyIdx).getMyLetters().add(alpha.get(alphaIdx));
			characterKeyMap.put(alpha.get(alphaIdx), keyIdx);
			alpha.remove(alphaIdx);
		}
	}
	

}
