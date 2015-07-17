package algorithm;

import data.ImageValues;
import data.LearnData;

public abstract class AbstractAlgorithm {
	public abstract void doLearn(LearnData learnData, int k, boolean isEuklidDistance, boolean isManhattanDistance) throws Exception;
	public abstract int getClassification(ImageValues imageValues);
}
