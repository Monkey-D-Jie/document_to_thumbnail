package converter.controller;

import converter.PPTToPNGConverter;
import converter.WordToPNGConveter;
import converter.base.IConverter;

/***
 * ת���������������ļ���ʽ��׺�ж�
 * @author JadonYuen
 *
 */
public class ConverterController {

	private ConverterController() {
		
	}
	
	public static IConverter getConveter(String inputPath,String outputPath) {
		
		IConverter conveter = null;
		if(inputPath.endsWith(".ppt") || inputPath.endsWith(".pptx")) {
			
			conveter = new PPTToPNGConverter(inputPath, outputPath);
			
		}else if(inputPath.endsWith(".doc") || inputPath.endsWith(".docx")) {
			
			conveter = new WordToPNGConveter(inputPath, outputPath);
			
		}else {
			
		}
		return conveter;
	}
	
}
