package data;


public class Cluster
{
	private Example[] cluster;
	private ImageValues averageImage;
	private int classification;
	
	public Cluster(Example[] clusteredExamples, int[] averageImage)
	{
		cluster = clusteredExamples;
		classification = -1;
		this.averageImage = new ImageValues(averageImage);
	}
	
	public void classifyCluster(int classification)
	{
		for(Example ex : cluster)
		{
			ex.setClassification(classification);
		}
		this.setClassification(classification);
	}

	
	public Example[] getCluster()
	{
		return cluster;
	}

	public int[] getAverageImage() {
		return averageImage.getValue();
	}

	public int getClassification() {
		return classification;
	}

	public void setClassification(int classification) {
		this.classification = classification;
	}
}
