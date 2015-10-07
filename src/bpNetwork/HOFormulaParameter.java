package bpNetwork;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 解释：本类用于记录Output层在对Input层的数据进行计算时所使用的参数
 * @author 13302010019-冀超
 *
 */
public class HOFormulaParameter implements Serializable{
	/**
	 * 解释：系统自动产生的serialVersionUID,本人并不知道那是什么东西
	 */
	private static final long serialVersionUID = 4998927595055948190L;
	private double[] outputFormulaPara;
	private double outputFormulaBias;
	private int indexOfOutputLayer;
	
	/**
	 * 解释：本方法用于根据已有数据创建出来一个新的数据对象
	 * @param paraSet 要产生特定对象的参数集
	 * @param bias 对应式子的bias偏差
	 */
	public HOFormulaParameter(double[] paraSet,double bias,int index){
		outputFormulaBias = bias;
		indexOfOutputLayer = index;
		outputFormulaPara = new double[ConstantParameter.HIDDENNUMBER];
		if(paraSet != null){
			for(int i = 0;i < ConstantParameter.HIDDENNUMBER;i++){
				outputFormulaPara[i] = paraSet[i];
			}
		}

	}
	
	/**
	 * 解释：该方法用于将对象写入到指定地点
	 * @throws FileNotFoundException 抛出【无法找到文件错误】
	 * @throws IOException 抛出【IO错误】
	 */
	public void writeToDiskHO() throws FileNotFoundException, IOException{
		String fileName = "HO" + indexOfOutputLayer;
		fileName = ConstantParameter.DATASAVEPATH + fileName + ".obj";
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(this);
		out.close();
	}
	
	/**
	 * 解释:该方法用于读取指定的文件中的数据
	 * @throws IOException  抛出【IO错误】
	 * @throws FileNotFoundException 抛出【文件无法找到错误】
	 * @throws ClassNotFoundException 抛出【找不到对应类错误】
	 */
	public void readFromDiskHO() throws FileNotFoundException, IOException, ClassNotFoundException{
		String fileName = "HO" + indexOfOutputLayer;
		fileName = ConstantParameter.DATASAVEPATH + fileName + ".obj";
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		HOFormulaParameter newRead = (HOFormulaParameter)in.readObject();
		outputFormulaBias = newRead.getOutputBias();
		for(int i = 0;i < ConstantParameter.HIDDENNUMBER;i++){
			outputFormulaPara[i] = newRead.getOutputPara(i);
		}
		in.close();
	}
	
	/**
	 * 解释：返回对应的是哪一个hidden层的参数组
	 * @return 对应hidden层神经元的信号
	 */
	public int getOutputFormulaIndex(){
		return indexOfOutputLayer;
	}
	
	/**
	 * 解释：返回某个参数值
	 * @param paraIndex 要返回参数值的序号
	 * @return 返回指定的参数值
	 */
	public double getOutputPara(int paraIndex){
		return outputFormulaPara[paraIndex];
	}
	
	/**
	 * 解释：得到式子的bias
	 * @return 式子的bias
	 */
	public double getOutputBias(){
		return outputFormulaBias;
	}

}
