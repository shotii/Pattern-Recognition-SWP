package algorithm;

import java.util.ArrayList;
import java.util.Random;

import data.Cluster;
import data.Example;
import data.ImageValues;
import data.LearnData;

public class KMeanAlgorithm extends AbstractAlgorithm {

	private ArrayList<Example> examples;
	private int k;
	private int maxIterationSteps;
	private int converganceFactor;
	private Cluster[] clusters;
	private boolean isEuclidDistance, isManhattanDistance;
	private PrototypeManager prototypeManager;
	
	public KMeanAlgorithm(int maxIterations, int converganceFactor)
	{
		maxIterationSteps = maxIterations;
		this.converganceFactor = converganceFactor;
	}
	
	//Example[] is equivalent to cluster!
	public void doLearn(LearnData learnData, int k, boolean isEuclidDistance, boolean isManhattanDistance) throws Exception 
	{	
		this.k = k;
		this.isEuclidDistance = isEuclidDistance;
		this.isManhattanDistance = isManhattanDistance;
		prototypeManager = PrototypeManager.getInstance(k);
		prototypeManager.setK(k);
		
		if(!(isEuclidDistance ^ isManhattanDistance))
			throw new Exception("You have to choose exactly one distance measure function!");
		
		int iterationSteps = 0;
		this.setExamples(learnData.getExamples());
		
		Example[] protoTypes = CreateInitialPrototypes();
		
		prototypeManager.setCurrentPrototypes(protoTypes);
		
		if(isEuclidDistance)
		{
			while(iterationSteps < maxIterationSteps && 
					prototypeManager.prototypeDistance(true) > converganceFactor)
			{
				//Expectation step
				clusterImageValues(protoTypes, examples);
		
				//Maximization step
				protoTypes = CalculateMeanPrototypes();
				
				prototypeManager.setOldPrototypes(prototypeManager.getCurrentPrototypes());
				prototypeManager.setCurrentPrototypes(protoTypes);
				
				iterationSteps++;
			}
		}
		else if(isManhattanDistance)
		{
			while(iterationSteps < maxIterationSteps &&
					prototypeManager.prototypeDistance(false) > converganceFactor)
			{
				//Expectation step
				clusterImageValues(protoTypes, examples);
		
				//Maximization step
				protoTypes = CalculateMeanPrototypes();
		
				prototypeManager.setOldPrototypes(prototypeManager.getCurrentPrototypes());
				prototypeManager.setCurrentPrototypes(protoTypes);
				
				iterationSteps++;
			}
		}
		
		clusters = prototypeManager.getNewClusters();
	}

	
	private Example[] CalculateMeanPrototypes() 
	{
		Example[] tmpExamples = new Example[k];
		
		for(int i=0;i<k;i++)
		{
			tmpExamples[i] = new Example(prototypeManager.calcMeanVector(i).getValue(),-1);
		}
		
		return tmpExamples;
	}

	private void clusterImageValues(Example[] protoTypes, ArrayList<Example> trainData)
	{
		if(isEuclidDistance)
		{
			for(int i=0;i<trainData.size();i++)
			{
				int indexOfBestPrototype = 0;
				int minEuclidDistance = Integer.MAX_VALUE;
				
				for(int j=0;j<protoTypes.length;j++)
				{
					int euclidDistance = 
							CalcEuclidDistance(trainData.get(i).getImageValues(), 
							protoTypes[j].getImageValues()); 
					
					if(euclidDistance < minEuclidDistance)
					{
						indexOfBestPrototype = j;
						minEuclidDistance = euclidDistance;
					}
				}
				
				prototypeManager.AddImageVector(trainData.get(i).getImageValueObj(), indexOfBestPrototype);
			}
		}
		else if(isManhattanDistance)
		{
			for(int i=0;i<trainData.size();i++)
			{
				int indexOfBestPrototype = 0;
				int minManhattanDistance = Integer.MAX_VALUE;
				
				for(int j=0;j<protoTypes.length;j++)
				{
					int manhattanDistance = 
							CalcManhattanDistance(trainData.get(i).getImageValues(), 
							protoTypes[j].getImageValues()); 
					
					if(manhattanDistance < minManhattanDistance)
					{
						indexOfBestPrototype = j;
						minManhattanDistance = manhattanDistance;
					}
				}
				
				prototypeManager.AddImageVector(trainData.get(i).getImageValueObj(), indexOfBestPrototype);
			}
		}
	}

	private int CalcManhattanDistance(int[] imageValuesTrain,
			int[] imageValuesPrototype) 
	{
		int tmpSum = 0;
		
		for(int i=0;i<imageValuesTrain.length;i++)
		{
			//always integer values as results, so no rounding is needed here!
			tmpSum += Math.abs((imageValuesTrain[i] - imageValuesPrototype[i]));
		}
		
		return tmpSum;
	}

	private int CalcEuclidDistance(int[] imageValuesTrain,
			int[] imageValuesPrototype) 
	{
		int tmpSum = 0;
		
		for(int i=0;i<imageValuesTrain.length;i++)
		{
			//always integer values as results, so no rounding is needed here!
			tmpSum += Math.pow((imageValuesTrain[i] - imageValuesPrototype[i]),2);
		}
		
		return (int) Math.round(Math.sqrt(tmpSum));
	}

	private Example[] CreateInitialPrototypes()
	{
		int imageSize = 28 * 28;
		Random random = new Random();
		Example[] protoTypes = new Example[k];
		
		for(int i=0;i<k;i++)
		{
			ImageValues randomValues = new ImageValues(imageSize);
			
			for(int j=0;j<imageSize;j++)
			{
				int value = random.nextInt(Byte.MAX_VALUE);
			
				randomValues.getValue()[j] = value;
			}
			
			protoTypes[i] = new Example(randomValues.getValue(),-1);
		}
		
		return protoTypes;
	}

	public ArrayList<Example> getExamples() {
		return examples;
	}

	public void setExamples(ArrayList<Example> examples) {
		this.examples = examples;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public int getIterationSteps() {
		return maxIterationSteps;
	}

	public void setIterationSteps(int iterationSteps) {
		this.maxIterationSteps = iterationSteps;
	}

	@Override
	public int getClassification(ImageValues imageValues) {
		int minDistance = Integer.MAX_VALUE;
		int classification = -1;
		
		for(Cluster cluster : clusters)
		{
			if(isEuclidDistance)
			{
				int euclidDistance = CalcEuclidDistance(imageValues.getValue(), cluster.getAverageImage());
				
				if(euclidDistance < minDistance)
				{
					minDistance = euclidDistance;
					classification = cluster.getClassification();
				}
			}
			else if(isManhattanDistance)
			{
				int manhattanDistance = CalcManhattanDistance(imageValues.getValue(), cluster.getAverageImage());
				
				if(manhattanDistance < minDistance)
				{
					minDistance = manhattanDistance;
					classification = cluster.getClassification();
				}
			}
		}
		
		return classification;
	}
}
