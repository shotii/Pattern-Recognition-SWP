package data;

public class ClassificationDefinition {
	
	private static final int MIN_CLASS = 0;
	private static final int MAX_CLASS = 9;
	private static final int DEFAULT_VALUE = -1;

	public int checkClassification(Example example) {
		int classification = example.getClassification();
		if ((classification < MIN_CLASS || classification > MAX_CLASS)
				&& classification != DEFAULT_VALUE) {
			return -1;
		}
		return 0;
	}

}
