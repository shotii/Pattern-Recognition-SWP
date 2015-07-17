package converter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import data.*;

public class PngConv extends AbstractConverter {

	@Override
	public void exportFile(String path, Example example)
			throws IOException {
		BufferedImage image = new BufferedImage(example.getColumns(),
				example.getRows(), BufferedImage.TYPE_BYTE_GRAY);

		int[] values = example.getImageValues();
		int counter = 0;
		for (int i = 0; i < example.getRows(); i++) {
			for (int j = 0; j < example.getColumns(); j++) {
				int pixel = values[counter];
				int color = new Color(pixel, pixel, pixel).getRGB();
				image.setRGB(j, i, color);

				counter++;
			}
		}
		ImageIO.write(image, "png", new File(path));
	}
	

	@Override
	public Example importFile(String path) throws IOException {

		BufferedImage image = ImageIO.read(new File(path));

		int width = image.getWidth();
		int height = image.getHeight();

		int resArray[] = new int[height*width];

		int counter = 0;
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				resArray[counter] =  printPixelARGB(image.getRGB(col, row));
				counter++;
			}
		}
		return new Example(resArray, -1);
	}

	private int printPixelARGB(int pixel) {
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
//		System.out.print((red + green + blue) / 3);
//		System.out.print(" ");
		return ( red + green + blue) /3;
	}

	@Override
	public Example[] importFile(String path, String path2, long from, long to)
			throws IOException {
		System.out.println("This function does nothing in this Converter!");
		return null;
	}
}
