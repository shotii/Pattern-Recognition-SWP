package data;

public class ImageDefinition {

	private static final int MAX_IMAGEVALUE = 255;
	private static final int MIN_IMAGEVALUE = 0;

	public int checkImageDefinition(Example ex){
		ImageValues imageValues = ex.getImageValueObj();
		for (int i = 0; i < imageValues.getValue().length; i++) {
			if (imageValues.getValue()[i] < MIN_IMAGEVALUE
					|| imageValues.getValue()[i] > MAX_IMAGEVALUE) {
				return -1;
			}
		}
		return 0;
	}
}
