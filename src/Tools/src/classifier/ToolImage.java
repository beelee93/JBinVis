package classifier;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ToolImage {
    private BufferedImage image;
    
	public ToolImage(int i, int j) {
		image = new BufferedImage(i, j, BufferedImage.TYPE_INT_RGB);
	}

	public void put(byte[] digraph) {
		int w = image.getWidth();
		int length = image.getWidth() * image.getHeight();
		length = digraph.length < length ? digraph.length : length;
		
		int value;
		
		for(int i=0;i<length;i++) {
			value = (digraph[i] & 0xFF)<<8;
		    image.setRGB(i % w, i/w, value);
		}
	}

	public void save(String name) {
		File file = new File(name);
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}