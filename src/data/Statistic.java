package data;

public class Statistic {
	
	private final int CLASS_COUNTER = 10;
	private int[] testObjectCounter;
 	private String usedAlgorithm;
	private int wrongClassifiedObjects; 
	private int learnObjectCounter;
	private static Statistic instance;
	private String distFunction;
	
	private Statistic(String usedAlgorithm)
	{
		testObjectCounter = new int[CLASS_COUNTER];
		setLearnObjectCounter(0);
		wrongClassifiedObjects = 0;
		this.usedAlgorithm = usedAlgorithm;
		this.distFunction = "n/a";
	}
	
	public void reset () {
		instance = new Statistic("n/a");
	}
	
	public int getTestObjectCount()
	{
		int tmpSum = 0;
		
		for(int testObjectValue : testObjectCounter)
		{
			tmpSum += testObjectValue;
		}
		
		return tmpSum;
	}
	
	public void wrongIncrement() {
		wrongClassifiedObjects++;
	}
	
	public void testObjectIncrement(int classNo) {
		testObjectCounter[classNo]++;
	}
	
	public void testObjectDecrement(int classNo) {
		testObjectCounter[classNo]--;
	}
	
	public int getTestObjectCount(int ClassCounter)
	{
		return testObjectCounter[ClassCounter];
	}

	public String getUsedAlgorithm() {
		return usedAlgorithm;
	}

	public void setUsedAlgorithm(String usedAlgorithm) {
		this.usedAlgorithm = usedAlgorithm;
	}

	public int getWrongClassifiedObjects() {
		return wrongClassifiedObjects;
	}

	public void setWrongClassifiedObjects(int wrongClassifiedObjects) {
		this.wrongClassifiedObjects = wrongClassifiedObjects;
	}
	
	public static Statistic getInstance()
	{
		if(instance == null)
			instance = new Statistic("n/a");
		
		return instance;
	}

	public int getLearnObjectCounter() {
		return learnObjectCounter;
	}

	public void setLearnObjectCounter(int learnObjectCounter) {
		this.learnObjectCounter = learnObjectCounter;
	}
	
	public double getMeanSquaredError()
	{
		return Math.pow(wrongClassifiedObjects, 2) / getTestObjectCount();
	}
	
	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Benutzter Algorithmus: " + usedAlgorithm + "\n");
		builder.append("Benutzte Distanzmessfunktion: " + distFunction + "\n\n");
		
		builder.append("Anzahl der Testobjekte pro Klasse ..\n");
		builder.append(padLeft("Klasse:", 10));
		for(int i=0;i<testObjectCounter.length;i++){
			builder.append("|  " + i);
		}
		builder.append("\n----------");
		for(int i=0;i<testObjectCounter.length;i++){
			builder.append("\u253C---");
		}
		builder.append("--\n" + padLeft("Anzahl:", 10));
		for(int i=0;i<testObjectCounter.length;i++)
		{
			builder.append("|"+padLeft(Integer.toString(testObjectCounter[i]), 3));
		}
		builder.append("\nAnzahl der Testobjekte insgesamt: " + getTestObjectCount() + "\n\n");
		builder.append("Anzahl der falsch klassifizierten Objekte: " + wrongClassifiedObjects + "\n");
		builder.append("Anzahl der genutzten Lerndaten: " + learnObjectCounter + "\n");
		builder.append("Mean square error: " + getMeanSquaredError());
		
		return builder.toString();
	}

	public String getDistFunction() {
		return distFunction;
	}

	public void setDistFunction(String distFunction) {
		this.distFunction = distFunction;
	}
}
