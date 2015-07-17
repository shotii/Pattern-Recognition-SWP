package algorithm;

import java.util.ArrayList;
import java.util.List;

import algorithm.KdTree.Entry;
import algorithm.KdTree.Manhattan;
import algorithm.KdTree.SqrEuclid;
import data.Cluster;
import data.Example;
import data.ImageValues;
import data.LearnData;

public class KnnAlgorithm extends AbstractAlgorithm {

	private int k;
	private boolean isEuclidDistance, isManhattanDistance;
	private SqrEuclid<Integer> sqrEuclid;
	private Manhattan<Integer> manhattan;
	
	public KnnAlgorithm()
	{
	}
	
	public void doLearn(LearnData learnData, int k, boolean isEuclidDistance, boolean isManhattanDistance) throws Exception 
	{	
		if(!(isEuclidDistance ^ isManhattanDistance))
			throw new Exception("You have to choose exactly one distance measure function!");
		
		this.k = k;
		this.isEuclidDistance = isEuclidDistance;
		this.isManhattanDistance = isManhattanDistance;
		ArrayList<Example> examples =  learnData.getExamples();
		
		if(isEuclidDistance)
		{
			sqrEuclid = null;
			//all examples should have the same size
			sqrEuclid = new SqrEuclid<Integer>(examples.get(0).pixelCount(), Integer.MAX_VALUE);
			for(Example example : examples)
			{
				sqrEuclid.addPoint(example.getImageValueObj().toDoubleArray(), example.getClassification());
			}
		}
		else if(isManhattanDistance)
		{
			manhattan = null;
			manhattan = new Manhattan<Integer>(examples.get(0).pixelCount(), Integer.MAX_VALUE);
			for(Example example : examples)
			{
				manhattan.addPoint(example.getImageValueObj().toDoubleArray(), example.getClassification());
			}
		}
	}
	
	public int getClassification(ImageValues imageValues)
	{
		Example tmpExample = new Example(imageValues.getValue(), -1);
		double[] doubleArray = tmpExample.getImageValueObj().toDoubleArray();
		List<Entry<Integer>> resultList = null;
		
		if(isEuclidDistance)
		{
			if(sqrEuclid == null) {
				//throw new Exception("No learn data was given!");
			}
		
			resultList = sqrEuclid.nearestNeighbor(doubleArray, k, true);
		}
		
		if(isManhattanDistance)
		{
			if(manhattan == null) {
				//throw new Exception("No learn data was given!");
			}
				
			resultList = manhattan.nearestNeighbor(doubleArray, k, true);
		}
		
		return resultList.get(0).value;
	}
	

//	public int getClassification(ImageValues imageValues)
//	{
//		List<Entry<Integer>> resultList = null;
//		
//		if(isEuclidDistance)
//		{
//			resultList = sqrEuclid.nearestNeighbor(imageValues, k, true);
//		}
//		
//		if(isManhattanDistance)
//		{
//			resultList = manhattan.nearestNeighbor(imageValues, k, true);
//		}
//		
//		return resultList.get(0).value;
//	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}
}
