package bpNetwork;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 解释：本类用于将程序执行过程写入到日志中
 * @author 13302010019-冀超
 *
 */
public class LogRecord {
	public static void logRecord(String content){
		File file = new File(ConstantParameter.LOGPATH);
		BufferedWriter fw;
		try {
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			fw.append(content);
			fw.newLine();
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
