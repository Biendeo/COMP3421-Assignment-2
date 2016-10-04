package heightmap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A nice program to turn a heightmap image into the JSON format for assignment 2.
 */
public class HeightmapToAltitude {
	public static void main(String[] args) {
		if (args.length != 5) {
			System.out.println("Usage: [heightmap file] [output file] [max height] [width] [depth]");
			return;
		}

		BufferedImage bitmap;

		try {
			bitmap = ImageIO.read(new File(args[0]));
		} catch (IOException e) {
			System.out.println("Heightmap could not be found.");
			return;
		}

		FileWriter outputFile;

		try {
			outputFile = new FileWriter(args[1]);
		} catch (IOException e) {
			System.out.println("Couldn't establish output file.");
			return;
		}

		float maxHeight;
		int width, depth;

		try {
			maxHeight = Float.parseFloat(args[2]);
			width = Integer.parseInt(args[3]);
			depth = Integer.parseInt(args[4]);
		} catch (NumberFormatException e) {
			System.out.println("Numbers were not able to be parsed.");
			return;
		}

		try {
			outputFile.write("{\n  \"width\" : " + args[3] + ",\n  \"depth\" : " + args[4] + ",\n\n  \"sunlight\" : [ -1, 1, 0 ],\n\n  \"altitude\" : [\n");

			int heightmapWidth = bitmap.getWidth();
			int heightmapHeight = bitmap.getHeight();

			for (int outputX = 0; outputX < width; ++outputX) {
				outputFile.write("    ");
				int pixelX = (int)((outputX + 0.5) * (heightmapWidth * 1.0 / width));
				for (int outputZ = 0; outputZ < depth; ++outputZ) {
					int pixelY = (int)((outputZ + 0.5) * (heightmapHeight * 1.0 / depth));

					float value = new Color(bitmap.getRGB(pixelX, pixelY)).getRed() / 255.0f * maxHeight;

					outputFile.write(Float.toString(value));

					if (outputX != width - 1 || outputZ != depth - 1) {
						if (outputZ == depth - 1) {
							outputFile.write(",\n");
						} else {
							outputFile.write(", ");
						}
					}

				}
			}

			outputFile.write("\n  ]\n}");

			outputFile.close();
		} catch (IOException e) {
			System.out.println("The output file unexpectedly errored.");
			return;
		}


		System.out.println("Program successful: check out " + args[1]);
	}
}
