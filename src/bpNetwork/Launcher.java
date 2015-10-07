package bpNetwork;
import java.io.IOException;

/**
 * 解释：本类用于启动BP网的运算
 * @author 13302010019-冀超
 *
 */
public class Launcher {
	public static void main(String args[]){
		BPNetworkClassification classification = new BPNetworkClassification();
		try {
			classification.learningProcedure();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			classification.testingProcedure();
		} catch ( ClassNotFoundException
				| IOException e) {
			e.printStackTrace();
		}
	}
}
