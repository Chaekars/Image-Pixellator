import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

public class imagePixellator 
{	
	private static int _pixelIntensity;
	private static BufferedImage _imageFile;
	private static String _outputFile;

	private static int _imageWidth;
	private static int _imageHeight;

	public imagePixellator(int pixelIntensity, String inputFile)
	{
		_pixelIntensity = pixelIntensity;
		_getInputFile(inputFile);
		_getOutputFile(inputFile);
		_pixellateImage();
	}

	private static void _getInputFile(String inputFile)
	{

		File file = new File(inputFile);
		try {
			_imageFile = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		_imageWidth = _imageFile.getWidth();
		_imageHeight = _imageFile.getHeight();
	}

	private static void _getOutputFile(String inputFile)
	{
		int extensionIndex = inputFile.lastIndexOf(".");
		_outputFile = (inputFile.substring(0,extensionIndex) + "Output.jpg");
	}

	private static void _outputPixelImage()
	{
		File file = new File(_outputFile);
		try
		{
			ImageIO.write(_imageFile, "jpg", file);
		}
		catch(Exception e)
		{
			System.out.println("Critical error: " + e);
		}
	}

	private static void _pixellateImage()
	{
		int pixelArray[][] = new int[_imageWidth][_imageHeight];
		

		_getPixelRGBValues(pixelArray);
		_calculateAveragePixelValue(pixelArray);
		_setPixelRGBValues(pixelArray);
		_outputPixelImage();
	}

	private static void _getPixelRGBValues(int pixelArray[][])
	{
		for(int width = 0; width < _imageWidth; width++)
		{
			for(int height = 0; height < _imageHeight; height++)
			{
				pixelArray[width][height] = _imageFile.getRGB(width,height);
			}
		}
	}

	private static void _setPixelRGBValues(int pixelArray[][])
	{
		for(int width = 0; width < _imageWidth; width++)
		{
			for(int height = 0; height < _imageHeight; height++)
			{
				_imageFile.setRGB(width, height, pixelArray[width][height]);
			}
		}
	}

	private static void _calculateAveragePixelValue(int pixelArray[][])
	{
		for(int width = 0; width < (_imageWidth - _pixelIntensity); width+=_pixelIntensity)
		{
			for(int height = 0; height < (_imageHeight - _pixelIntensity); height+=_pixelIntensity)
			{
				_calculateAveragePixelValue(pixelArray,width,height);
			}
		}
	}
	
	private static void _calculateAveragePixelValue(int pixelArray[][], int width, int height)
	{
		String pixelValues[] = new String[_pixelIntensity*_pixelIntensity];
		int incArray = 0;
		for(int i = height; i < _pixelIntensity+height; i++)
		{
			for(int j = width; j < _pixelIntensity+width; j++)
			{
				pixelValues[incArray++] = Integer.toHexString(pixelArray[j][i]);
			}
		}
		String averagePixelValue = _averageHexidecimalValue(pixelValues);
		for(int i = height; i < _pixelIntensity+height; i++)
		{
			for(int j = width; j < _pixelIntensity+width; j++)
			{
				pixelArray[j][i] = Integer.parseUnsignedInt(averagePixelValue, 16);
			}
		}
	}
	
	private static String _averageHexidecimalValue(String pixelValues[])
	{
		int alpha = 0,
			red = 0,
			green = 0,
			blue = 0;

		int arrayLength = pixelValues.length;
		
		for(int i = 0; i < arrayLength; i++)
		{
			alpha += Integer.parseInt(pixelValues[i].substring(0, 2),16);
			red += Integer.parseInt(pixelValues[i].substring(2, 4),16);
			green += Integer.parseInt(pixelValues[i].substring(4, 6),16);
			blue += Integer.parseInt(pixelValues[i].substring(6, 8),16);
		}

		alpha/=arrayLength;
		red/=arrayLength;
		green/=arrayLength;
		blue/=arrayLength;

		String averagePixelValue = "";

		averagePixelValue+=(alpha < 16?"0":"") + Integer.toHexString(alpha);
		averagePixelValue+=(red < 16?"0":"") + Integer.toHexString(red);
		averagePixelValue+=(green < 16?"0":"") + Integer.toHexString(green);
		averagePixelValue+=(blue < 16?"0":"") + Integer.toHexString(blue);

		System.out.printf("%d %d %d %d%n",alpha,red,green,blue);
		System.out.println(averagePixelValue);

		return averagePixelValue;
	}
}
