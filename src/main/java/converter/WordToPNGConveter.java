package converter;

import java.io.File;

import converter.base.BaseConveter;

/***
 * word�ĵ�����Ԥ��ͼpng
 * @author JadonYuen
 */
public class WordToPNGConveter extends BaseConveter {

	public WordToPNGConveter(String inputPath,String outputPath) {
		super(inputPath,outputPath);
	}
	
	private WordToPDFConverter wordToPdf = null;
	
	/***
	 * ��תpdf��ʽ��תpng��20180719Ŀǰ�汾ֻ֧�����ֱ�׾��ʽ��������һ����λ�����ٵ�����
	 */
	public void startConvert() throws Exception {
		super.startConvert();
		
		String[] extArray = this.inputPath.split("\\.");
		String ext = extArray[extArray.length - 1];
		ext = "." + ext.toLowerCase();
		String pdfPath =  this.inputPath.replace(ext, ".pdf");
		
		File file = new File(this.outputPath);
		if(!file.exists())file.mkdirs();
		
		wordToPdf = new WordToPDFConverter(this.inputPath, pdfPath);
		wordToPdf.startConvert();
		
		PDFToPNGConverter pdfConverter = new PDFToPNGConverter(pdfPath, this.outputPath);
		pdfConverter.startConvert();
		
		//�Ƴ�pdf�ļ�
		wordToPdf.deletePDF();
		wordToPdf = null;
		pdfConverter = null;
		file = null;
		System.gc();
	}

	public void cancelConvert() throws Exception {
		super.cancelConvert();
		File dirFile = new File(this.outputPath);
		if(dirFile.exists()) {
			for (File childFile : dirFile.listFiles()) {
				childFile.delete();
			}
			dirFile.delete();
		}
		dirFile = null;
		wordToPdf.deletePDF();
		
		System.gc();
	}
	
}
