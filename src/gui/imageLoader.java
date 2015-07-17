package gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import data.Example;

public class imageLoader {

	public static ImageIcon createImageIcon(String path) {
		ImageIcon icon = null;
		try {
			icon = new ImageIcon(ImageIO.read(new File(path)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return icon;
    }
	
	public static ImageIcon createImageIcon(BufferedImage image){
		return new ImageIcon(image);
	}
    
	/**
	 * Reads path to BufferedImage
	 * @param path String element with the absolute path to image
	 * @return BufferedImage element
	 */
    public static BufferedImage createBufferedImage(String path) {
    	BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
        return image;
    }
    
    public static ImageIcon exportImageIcon(Example example){
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
		return new ImageIcon(image);
	}
    
    public static BufferedImage importFileToBuffer(String path) throws IOException {
		BufferedImage image = ImageIO.read(new File(path));
		return image;
	}

}
