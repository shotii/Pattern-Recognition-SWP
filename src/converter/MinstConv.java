package converter;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import data.Example;

public class MinstConv extends AbstractConverter {

	@Override
	public Example[] importFile(String pathLabels, String pathImages,
			long from, long to) throws IOException {

		DataInputStream labels = new DataInputStream(new FileInputStream(
				pathLabels));
		DataInputStream images = new DataInputStream(new FileInputStream(
				pathImages));

		int magicNumber = labels.readInt();
		if (magicNumber != 2049) {
			System.err.println("Label file has wrong magic number: "
					+ magicNumber + " it should be 2049");
			System.exit(0);
		}
		magicNumber = images.readInt();
		if (magicNumber != 2051) {
			System.err.println("Image file has wrong magic number: "
					+ magicNumber + " it should be 2051");
			System.exit(0);
		}

		int numLabels = labels.readInt();
		int numImages = images.readInt();
		int numRows = images.readInt();
		int numCols = images.readInt();
		if (numLabels != numImages) {
			System.err
					.println("Image file and label file do not contain the same number of entries.");
			System.err.println("  Label file contains: " + numLabels);
			System.err.println("  Image file contains: " + numImages);
			System.exit(0);
		}

		Example[] exampleArray = new Example[(int) (to - from) + 1];

		images.skip(from * numRows * numCols);
		labels.skip(from);

		int numLabelsRead = 0;
		int numImagesRead = 0;
		while (labels.available() > 0 && numLabelsRead < numLabels
				&& numLabelsRead <= (to - from)) {
			int label = labels.readByte();
			numLabelsRead++;
			int[] image = new int[numCols * numRows];
			for (int i = 0; i < numCols * numRows; i++) {
				image[i] = images.readUnsignedByte();
				// System.out.print(image[i] + " ");
			}

			exampleArray[numImagesRead] = new Example(image, label);
			numImagesRead++;
		}

		return exampleArray;
	}

	@Override
	public void exportFile(String path, Example example) throws IOException {
		// TODO Auto-generated method stub

	}


	public void exportFile(String pathLabels, String pathImages,
			ArrayList<Example> examples) throws IOException {

		DataOutputStream labels = new DataOutputStream(new FileOutputStream(
				pathLabels));
		DataOutputStream images = new DataOutputStream(new FileOutputStream(
				pathImages));

		byte[] imageValues = new byte[examples.get(0).getImageValues().length
				* examples.size()];

		byte[] labelValues = new byte[examples.size()];

		int counter =0;
		for (int ex = 0; ex < examples.size(); ex++) {
			for (int i = 0; i < examples.get(ex).getImageValues().length; i++) {
				imageValues[counter] = (byte) examples.get(ex).getImageValues()[i];
				counter ++;
			}
			labelValues[ex] = (byte) examples.get(ex).getClassification();
		}

		labels.writeInt(2049);
		labels.writeInt(labelValues.length);

		images.writeInt(2051);
		images.writeInt(examples.size());
		images.writeInt(examples.get(0).getRows());
		images.writeInt(examples.get(0).getColumns());

		labels.write(labelValues);
		images.write(imageValues);
		labels.close();
		images.close();
	}

	@Override
	public Example importFile(String path) throws IOException {
		return null;
	}

}
