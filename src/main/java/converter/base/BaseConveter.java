package converter.base;

/***
 * ת�������� �д���չ......
 * @author JadonYuen
 */
public abstract class BaseConveter implements IConverter{

	protected String inputPath;
	protected String outputPath;
	
	protected boolean isConverting = false;

	public BaseConveter(String inputPath,String outputPath) {
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}
	
	public void startConvert() throws Exception {
		this.isConverting = true;
	}
	
	public void cancelConvert() throws Exception{
		this.isConverting = false;
	}

}
