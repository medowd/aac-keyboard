package keyboard;

import java.util.Comparator;

public class KeyboardComparator implements Comparator<Keyboard>{

	@Override
	public int compare(Keyboard o1, Keyboard o2) {
		return Float.floatToIntBits(o1.getMyCost() - o2.getMyCost());
	}

}
