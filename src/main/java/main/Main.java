package main;

import java.io.File;
import java.util.Date;

import converter.PDFToPNGConverter;
import converter.PPTToPNGConverter;
import converter.WordToPDFConverter;

public class Main {

	public static void main(String[] args) {
		try {
			//String documentPath = "H:\\DTPlatform\\git_project\\temp_test\\platform\\preparelesson\\592246f8778739294f000114\\document\\5b4d4e631340a5dc23000002\\src\\5b4d4e631340a5dc23000002.pptx";
			String documentPath = args[0];

			String[] extArray = documentPath.split("\\.");
			String ext = extArray[extArray.length - 1];
			ext = "." + ext.toLowerCase();
			String pdfPath = documentPath.replace(ext, ".pdf");

			String dirPath = args[1];
			//String dirPath = "H:\\DTPlatform\\git_project\\temp_test\\platform\\preparelesson\\592246f8778739294f000114\\document\\5b4d4e631340a5dc23000002\\thumb";

			int msgNum = 3;// ��ͼ�����ﵽ3��֪ͨ

			// Ϊ��� libreoffice �� ppt��ʽ�ļ�����Ԥ��ͼ�Ĳ���  ʹ��poi

			File thumbDir = new File(dirPath);
			thumbDir.mkdirs();
			System.out.println("start convert");
			Date begin = new Date();
			if (documentPath.endsWith("pptx") || documentPath.endsWith("ppt")) {
				new PPTToPNGConverter().startConvert(documentPath, dirPath);
			} else if (documentPath.endsWith("docx") || documentPath.endsWith("doc")) {
				System.out.println("start convert pdf");
				new WordToPDFConverter().startConvert(documentPath, pdfPath);
				new PDFToPNGConverter().startConvert(pdfPath, dirPath, msgNum);
			}
			Date end = new Date();
			System.out.println(end.getTime() - begin.getTime());
			System.out.println("finish");
			System.exit(0);
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}

	}

}
