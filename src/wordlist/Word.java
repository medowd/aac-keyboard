package wordlist;

public class Word {
	
	private String myWord;
	private float myFreq;
	private String myPrefix;	//prefix indicates how many characters have to be typed to know without ambiguity which word it is
	
	public Word(String w, int f) {
		myWord = w;
		myFreq = f;
	}

	public String getWord() {
		return myWord;
	}

	public void setWord(String word) {
		this.myWord = word;
	}

	public float getFreq() {
		return myFreq;
	}

	public void setFreq(float freq) {
		this.myFreq = freq;
	}
	
	public String getPrefix() {
		return myPrefix;
	}
	
	public void setPrefix(String pre) {
		this.myPrefix = pre;
	}
}
