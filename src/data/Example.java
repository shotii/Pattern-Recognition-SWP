package data;

import org.garret.perst.Persistent;

public class Example extends Persistent {
	private ImageValues imageValues;
	private int classification;
	private int rows = 28;
	private int columns = 28;
	public int intKey;

	public Example(int[] imageValues, int classification) {
		this.setClassification(classification);
		//this.setImageValues(imageValues);
		this.imageValues = new ImageValues(imageValues);
	}

	public int[] getImageValues() {
		return imageValues.getValue();
	}

	public void setImageValues(int[] imageValues) {
		this.imageValues.setValue(imageValues);
	}

	

	public int getClassification() {
		return classification;
	}

	public void setClassification(int classification) {
		this.classification = classification;
	}

	public int pixelCount() {
		return imageValues.length();
	}
	
	public int getRows(){
		return rows;
	}
	
	public void setRows(int rows){
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
	public ImageValues getImageValueObj(){
		return this.imageValues;
	}
}
