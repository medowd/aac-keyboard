package wordlist;
import java.util.Comparator;

public class WordComparator implements Comparator<Word>{

	@Override
	public int compare(Word o1, Word o2) {
		if (o1.getFreq() != o2.getFreq()) {
			return Float.floatToIntBits(o2.getFreq() - o1.getFreq());
		}
		return o1.getWord().length() - o2.getWord().length();
	}

}
