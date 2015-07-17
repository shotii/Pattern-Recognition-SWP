package data;

public class Schema {

	ImageDefinition imageDefinition;
	ClassificationDefinition classificDefinition;

	public Schema() {
		imageDefinition = new ImageDefinition();
		classificDefinition = new ClassificationDefinition();
	}

	public int checkExample(Example example) {
		if (imageDefinition.checkImageDefinition(example) == -1)
			return -1;
		else if (classificDefinition.checkClassification(example) == -1)
			return -1;

		return 0;
	}
}
