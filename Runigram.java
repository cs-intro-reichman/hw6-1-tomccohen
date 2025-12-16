import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);

		System.out.println();
		System.out.println("Testing scaled to 3x5: ");
		image = scaled(tinypic, 3, 5);
		System.out.println();
		print(image);
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		Color[][] image = new Color[numRows][numCols];
		
			for (int i = 0; i < numRows; i++)
			{
				for (int j = 0; j < numCols; j++)
				{
					int r = in.readInt();
					int g = in.readInt();
					int b = in.readInt();
					image[i][j] = new Color(r, g, b);
				}
			}
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");	
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		int numRows = image.length;
		int numCols = image[0].length;

		for (int i = 0; i < numRows; i++)
		{
			for (int j = 0; j < numCols; j++)
			{
				print(image[i][j]); 
			}
			System.out.println();
		}
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		int rows = image.length;
		int cols = image[0].length;
		Color[][] flippedImage = new Color[rows][cols];

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				flippedImage[i][(cols - 1) - j] = image[i][j];
			}
		}

		return flippedImage;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		int rows = image.length;
		int cols = image[0].length;
		Color[][] flippedImage = new Color[rows][cols];

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				flippedImage[(rows - 1) - i][j] = image[i][j];
			}
		}
		return flippedImage;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		int r = pixel.getRed();
		int g = pixel.getGreen();
		int b = pixel.getBlue();
		int lumin = (int) (0.299 * r + 0.587 * g + 0.114 * b);

		Color lum = new Color(lumin, lumin, lumin);
		return lum;
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		int rows = image.length;
		int cols = image[0].length;
		Color[][] newImage = new Color[rows][cols];

		for(int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				newImage[i][j] = luminance(image[i][j]);
			}
		}
		return newImage;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		Color[][] newImage = new Color[height][width];
		double hScale = (double) image.length / height; 
		double wScale = (double) image[0].length / width;

		for (int i = 0; i < height; i++)
		{	
			int Xscale = (int) (i * hScale);

			for (int j = 0; j < width; j++)
			{
				int yScale = (int) (j * wScale);
				newImage[i][j] = image[Xscale][yScale];
			}
		}	
		return newImage;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int newR = (int) ((c1.getRed() * alpha) + (c2.getRed() * (1 - alpha)));
		int newG = (int) ((c1.getGreen() * alpha) + (c2.getGreen() * (1 - alpha)));
		int newB = (int) ((c1.getBlue() * alpha) + (c2.getBlue() * (1 - alpha)));

		Color mixColor = new Color(newR, newG, newB);

		return mixColor;
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		if (image1.length != image2.length || image1[0].length != image2[0].length)
		{
			image2 = scaled(image2, image1[0].length, image1.length); // make image 2 like the scales of image 1
		}

		int rows = image1.length;
		int cols = image1[0].length;
		Color blendImages[][] = new Color[rows][cols];

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				Color newColor = blend(image1[i][j], image2[i][j], alpha);
				blendImages[i][j] = newColor;
			}
		}
		return blendImages;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		if (source.length != target.length || source[0].length != target[0].length)
		{
			target = scaled(target, source[0].length, source.length); // make image target like the scales of source
		}
		
		int rows = source.length;
		int cols = source[0].length;
		Color[][] newImage = new Color[rows][cols];

		for (int i = 0; i <= n; i++)
		{
			double alpha = (double) (n - i) / n;
			newImage = blend(source, target, alpha);
			display(newImage);
			StdDraw.pause(500);
		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

