package converter;

import java.io.IOException;

import data.Example;
import data.ImageValues;

public abstract class AbstractConverter {
	
	private ImageValues imageValue;
	private int numberOfRows, numberOfColumns;
	
	
	public abstract Example importFile(String path) throws IOException, Exception;
	public abstract Example[] importFile(String path, String path2, long from, long to) throws IOException;
	
	public Example convertToExample(int[] imageValues, int classification, int[] labels){
		Example example = new Example(imageValues, -1);
		 return example;
	}
	
	public abstract void exportFile(String path, Example example) throws IOException;
	
}
