package data;

import java.util.ArrayList;
import org.garret.perst.*;

public class LearnData extends Persistent {

	private ArrayList<Example> examples;
	public FieldIndex<Example> intKeyIndex;
	Schema schema;

	public LearnData() {
		schema = new Schema();
	}

	public LearnData(Storage db) {
		super(db);
		schema = new Schema();
		this.examples = new ArrayList<Example>();

		intKeyIndex = db.<Example> createFieldIndex(Example.class, "intKey",
				true);
	}

	public ArrayList<Example> getExamples() {
		return examples;
	}

	public Example getSpecificExample(int index) {
		return examples.get(index);
	}

	public int examplesCount() {
		return examples.size();
	}

	public void addExample(Example example) {

		if (schema.checkExample(example) == 0) {
			examples.add(example);
			intKeyIndex.put(example);
			modify();
		}
	}

	public void addExmaples(Example[] examples) {
		for (Example ex : examples) {
			addExample(ex);
		}
	}

	public void clearExamples() {
		examples.clear();
	}
}
