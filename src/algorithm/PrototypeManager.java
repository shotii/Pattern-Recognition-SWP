package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.Cluster;
import data.Example;
import data.ImageValues;

public class PrototypeManager {
	private HashMap<Integer, List<ImageValues>> prototypeAssociations;
	private Example[] oldPrototypes;
	private Example[] currentPrototypes;
	private ImageValues[] meanImageValues;
	private Cluster[] clusters;
	private static PrototypeManager instance;
	private int numberOfPrototypes;

	private PrototypeManager(int k) {
		this.numberOfPrototypes = k;
		int imageSize = 28 * 28;
		prototypeAssociations = new HashMap<Integer, List<ImageValues>>();
		ImageValues imgValueTemplate = new ImageValues(imageSize);
		meanImageValues = new ImageValues[numberOfPrototypes];

		oldPrototypes = new Example[numberOfPrototypes];
		clusters = new Cluster[numberOfPrototypes];

		for (int i = 0; i < numberOfPrototypes; i++) {
			prototypeAssociations.put(i, new ArrayList<ImageValues>());
			oldPrototypes[i] = new Example(imgValueTemplate.getValue(), 0);
			meanImageValues[i] = new ImageValues(imageSize);
		}
	}

	private boolean isClusterEmpty() {
		for (int i = 0; i < clusters.length; i++) {
			if (clusters[i] != null) {
				return false;
			}
		}
		return true;
	}

	public Cluster[] getNewClusters() {
		int numberOfClusters = prototypeAssociations.size();

		ArrayList<ArrayList<ImageValues>> currClusters = new ArrayList<ArrayList<ImageValues>>();

		for (int i = 0; i < numberOfClusters; i++) {
			currClusters.add((ArrayList<ImageValues>) prototypeAssociations
					.get(i));
		}

		for (int i = 0; i < currClusters.size(); i++) {
			ArrayList<ImageValues> listImgValues = currClusters.get(i);

			Example[] examples = new Example[listImgValues.size()];

			for (int j = 0; j < listImgValues.size(); j++) {
				examples[j] = new Example(listImgValues.get(j).getValue(), -1);
			}

			clusters[i] = new Cluster(examples, meanImageValues[i].getValue());
		}
		
		return clusters;
	}

	public Cluster[] getClusters() {
		if (isClusterEmpty()) {
			return getNewClusters();
		}

		return clusters;
	}

	public void setClusters(Cluster[] clusters) {
		this.clusters = clusters;
	}

	public void AddImageVector(ImageValues imageValues, int prototypeIndex) {
		prototypeAssociations.get(prototypeIndex).add(imageValues);
	}

	public ImageValues calcMeanVector(int prototypeIndex) {
		List<ImageValues> listImageValues = prototypeAssociations
				.get(prototypeIndex);
		int imageSize = 28 * 28;
		int numberOfImgVectors = listImageValues.size();

		meanImageValues[prototypeIndex] = currentPrototypes[prototypeIndex]
				.getImageValueObj();

		int[] tmpSumValue = new int[imageSize];

		for (int i = 0; i < numberOfImgVectors; i++) {
			ImageValues tmpImgValue = listImageValues.get(i);

			for (int j = 0; j < tmpImgValue.length(); j++) {
				tmpSumValue[j] += tmpImgValue.getValue()[j];
			}
		}

		if (numberOfImgVectors > 0) {
			for (int i = 0; i < imageSize; i++) {
				int value = Math.round((tmpSumValue[i] / numberOfImgVectors));
				meanImageValues[prototypeIndex].getValue()[i] = value;
			}
		}

		return meanImageValues[prototypeIndex];
	}

	public int prototypeDistance(boolean isEuclidDistance) throws Exception {
		if (currentPrototypes.length == 0)
			throw new Exception("Number of current prototypes must not be 0!");

		int numberOfPrototypes = oldPrototypes.length;
		int[] distanceValues = new int[numberOfPrototypes];

		for (int i = 0; i < numberOfPrototypes; i++) {
			if (isEuclidDistance)
				distanceValues[i] = CalcEuclidDistance(
						oldPrototypes[i].getImageValues(),
						currentPrototypes[i].getImageValues());
			else
				distanceValues[i] = CalcManhattanDistance(
						oldPrototypes[i].getImageValues(),
						currentPrototypes[i].getImageValues());

		}

		return maxValue(distanceValues);
	}

	private int maxValue(int[] values) {
		int tmp = values[0];

		for (int i = 1; i < values.length; i++) {
			if (tmp < values[i])
				tmp = values[i];
		}

		return tmp;
	}

	public Example[] getOldPrototypes() {
		return oldPrototypes;
	}

	public void setOldPrototypes(Example[] oldPrototypes) {
		this.oldPrototypes = oldPrototypes;
	}

	public Example[] getCurrentPrototypes() {
		return currentPrototypes;
	}

	public void setCurrentPrototypes(Example[] currentPrototypes) {
		this.currentPrototypes = currentPrototypes;
	}

	private int CalcManhattanDistance(int[] imageValuesTrain,
			int[] imageValuesPrototype) {
		int tmpSum = 0;

		for (int i = 0; i < imageValuesTrain.length; i++) {
			// always integer values as results, so no rounding is needed here!
			tmpSum += Math.abs((imageValuesTrain[i] - imageValuesPrototype[i]));
		}

		return tmpSum;
	}

	private int CalcEuclidDistance(int[] imageValuesTrain,
			int[] imageValuesPrototype) {
		int tmpSum = 0;

		for (int i = 0; i < imageValuesTrain.length; i++) {
			// always integer values as results, so no rounding is needed here!
			tmpSum += Math.pow((imageValuesTrain[i] - imageValuesPrototype[i]),
					2);
		}

		return (int) Math.round(Math.sqrt(tmpSum));
	}

	public static PrototypeManager getInstance(int k) {
		if (instance == null)
			instance = new PrototypeManager(k);
		return instance;
	}

	public int getK() {
		return this.numberOfPrototypes;
	}

	public void setK(int k) {
		this.numberOfPrototypes = k;
	}

}
