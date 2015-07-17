package data;

import org.garret.perst.Persistent;

public class ImageValues extends Persistent {
	private int[] value;
	public int intKey;

	public ImageValues(int length){
		this.value = new int[length];
		init();
	}
	
	public ImageValues(int[] value)
	{
		this.value = value;
	}
	
	private void init(){
		for(int i=0; i<this.value.length; i++){
		this.value[i] = 0;
		}
	}

	public int[] getValue() {
		return value;
	}

	public void setValue(int[] value) {
		this.value = value;
	}
	
	public int length() {
		return value.length;
	}

	public double[] toDoubleArray() {
		int valueLength =length();
		double[] doubleArray = new double[valueLength];

		for (int i = 0; i < valueLength; i++) {
			doubleArray[i] = (double) value[i];
		}

		return doubleArray;
	}
	
	
}
