package converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import data.Example;

public class CsvConv extends AbstractConverter {

	@Override
	public Example importFile(String path) throws Exception {

		BufferedReader in = new BufferedReader(new FileReader(path));
		int[] imageValues = null;
		int classification = -1;
		String str;
		

		if (path.endsWith(".csv") == false) {
			throw new Exception("The provided path must have extension .csv");
		}

		
		while ((str = in.readLine()) != null) {
				String[] s = str.split(",");
				
				int rows = Integer.parseInt(s[0]);
				int cols = Integer.parseInt(s[1]);
				classification = Integer.parseInt(s[2]);
				
				imageValues = new int[rows*cols];
				for (int i = 3; i < s.length; i++) {
					imageValues[i-3] = Integer.parseInt(s[i]);
				}
		}
		in.close();
		return new Example(imageValues, classification);
	}

	private int getSize(String path, int choice) throws IOException {
		// choice 0 = width
		// choice 1 = height
		BufferedReader in = new BufferedReader(new FileReader(path));
		String[] str = in.readLine().split(" ");

		if (choice == 0) {
			in.close();
			return Integer.parseInt(str[0]);
		} else {
			in.close();
			return Integer.parseInt(str[1]);
		}
	}

	@Override
	public void exportFile(String path, Example example)
			throws IOException {

		File file = new File(path);
		if (!file.exists())
			file.createNewFile();

		FileWriter fw = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(fw);

		StringBuilder s = new StringBuilder();
		writer.write(example.getRows() + "," + example.getColumns() + "," + example.getClassification());
		for (int i = 0; i < example.getImageValues().length; i++) {
			
			s.append("," + example.getImageValues()[i]);
		}
		writer.write(s.toString());
		writer.close();
		fw.close();
	}

	@Override
	public Example[] importFile(String path, String path2, long from, long to)
			throws IOException {
		System.out.println("This function does nothing in this Converter!");
		return null;
	}

}
