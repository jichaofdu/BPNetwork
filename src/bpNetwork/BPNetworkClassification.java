package bpNetwork;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * 解释:BPNetwork的主要程序
 * @author 13302010019-冀超
 * 解释：对于每个数据样本的处理，应该分为以下步骤
 *       
 */
public class BPNetworkClassification {
	
	private int inputUnits;//解释：输入层的数量
	private int hiddenUnits;//解释：中间层的数量
	private int outputUnits;//解释：输出层的数量
	private int learningTimes;//解释：学习的总次数
	private double alpha;//解释：learning rate
	private double beta;//解释：learning rate
	private double errorLimit;//解释：误差小于改限制值就结束运算
	private double totalError;//解释：当前的误差总数
	private double[] biasHidden;//解释：中间层bias
	private double[] biasOutput;//解释：输出层bias
	private double[][] weightIH;//注意：weight[Hidden][Input]
	private double[][] weightHO;//注意：weight[Output][Hidden]
	private Random randomgen;//解释：用于初始化变量的随机数
	private double[] input;
    private double[] actualOutput;//解释：计算出的输出
	private double[] desiredOutput;//解释：实际应该的输出
	private double[] outputHidden;
	private NumberObject nowCase;
	private int numberGuess;//解释：神经网络猜的数字
	private int correct;//解释：当前猜测正确的数字的计数
	
	public BPNetworkClassification(){
		randomgen = new Random();
		inputUnits = ConstantParameter.INPUTNUMBER;
		hiddenUnits = ConstantParameter.HIDDENNUMBER;
		outputUnits = ConstantParameter.OUTPUTNUMBER;
		learningTimes = ConstantParameter.TRAINNINGSETNUMBER;
		alpha = ConstantParameter.ALPHA;     //超参数！
		beta = ConstantParameter.BETA;       //超参数！
		errorLimit = ConstantParameter.ERRORLIMIT;   //这个东西怎么用？
		totalError = 0;                              //这个东西怎么用？
		biasHidden = new double[ConstantParameter.HIDDENNUMBER];
		biasOutput = new double[ConstantParameter.OUTPUTNUMBER];
	    weightIH = new double[ConstantParameter.HIDDENNUMBER][ConstantParameter.INPUTNUMBER];
	    weightHO = new double[ConstantParameter.OUTPUTNUMBER][ConstantParameter.HIDDENNUMBER];
	    input = new double[ConstantParameter.INPUTNUMBER];
	    actualOutput = new double[ConstantParameter.OUTPUTNUMBER];
	    desiredOutput = new double[ConstantParameter.OUTPUTNUMBER];
	    outputHidden = new double[ConstantParameter.HIDDENNUMBER];
	    correct = 0;
	}
	
	/**
	 * 解释：训练一个数组的一个轮回
	 * @param i 当前进行训练的是第几个样本
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void learningProcedure() throws FileNotFoundException, IOException{
		LogRecord.logRecord("[Begin] 训练集运算开始 ");
		initPara();
		LogRecord.logRecord("[Tip] 参数初始化完毕 ");
		for(int i = 1;i < learningTimes;i++){
			NumberObject numberObj = new NumberObject(i);
			setNowCase(numberObj);
			calculateOutput();
			updateWeightAndBias();
			if((double)(i % 100) == 0 && i > 100){
				adjustWeight();
				LogRecord.logRecord("["+ i + "] 参数调整 ");
				System.out.println("运行中......");
			}
			if((double)(i % 1000) == 0 && i > 1000){
				saveParaToDisk();
				LogRecord.logRecord("[" + i + "] 完成一次往本地的参数存储 ");
			}
		}
		saveParaToDisk();
		LogRecord.logRecord("[Tip] 完成一次往本地的参数存储 ");
		LogRecord.logRecord("[End] 训练集运算结束 ");
	}
	
	/**
	 * 解释：本方法用于调整超参数
	 */
	public void developmentProcedure(){
		
	}
	
	/**
	 * 解释：测试步骤。输出测试结果
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	public void testingProcedure() throws FileNotFoundException, ClassNotFoundException, IOException{
		LogRecord.logRecord("[Begin] 测试集运算开始 ");
		readParaFromDisk();
		LogRecord.logRecord("[Tip] 完成一次从本地的参数读取 ");
		for(int i = 65000;i < 65000 + ConstantParameter.TESTSETNUMBER;i++){
			NumberObject numberObj = new NumberObject(i);
			setNowCase(numberObj);
			calculateOutput();
			try {
				guessNumberAndSaveAnswer();
				//注意：在最终提交版本中，关于统计正确率的部分必须被删除
				if(numberObj.getActualNumber() == numberGuess){
					correct++;
				}
				LogRecord.logRecord("[Test " + i + "] 猜测数:" + numberGuess + " 实际值：" + numberObj.getActualNumber());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LogRecord.logRecord("[Tip] 测试集运算结束");
		LogRecord.logRecord("[End] 正确率：" + correct + " / " + 1000);
	}
	
	/**
	 * 解释：本方法用于随机初始化式子中的各参数。首先对两组bias进行设置，然后再初始化其它参数,即weight
	 * 注意：初始化公式不知道是否正确
	 */
	private void initPara(){
		for(int i = 0;i < hiddenUnits;i++){
			biasHidden[i] = randomgen.nextDouble() - 1.0d;
		}
		for(int i = 0;i < outputUnits;i++){
			biasOutput[i] = (randomgen.nextDouble() - 0.5) * 2 * 0.2;
		}
		for(int i = 0;i < hiddenUnits;i++){
			for(int j = 0;j < inputUnits;j++){
				weightIH[i][j] = (randomgen.nextDouble() - 0.5) * 2 / Math.sqrt(inputUnits);
			}
		}
		for(int i = 0;i < outputUnits;i++){
			for(int j = 0;j < hiddenUnits;j++){
				weightHO[i][j] = (randomgen.nextDouble() - 0.5) * 2 / Math.sqrt(hiddenUnits);
			}
		}
	}
	
	/**
	 * 解释：本方法用于更新当前正在处理的数字案例
	 */
	private void setNowCase(NumberObject newObj){
		nowCase = newObj;
		for(int i = 0;i < outputUnits;i++){
			desiredOutput[i] = 0;
		}
		desiredOutput[nowCase.getActualNumber()] = 1;
		for(int i = 0;i < ConstantParameter.MATRIXHIGH;i++){
			for(int j = 0;j < ConstantParameter.MATRIXWIDTH;j++){
				input[i * ConstantParameter.MATRIXWIDTH + j] = nowCase.getValue(i, j);
			}
		}
	}
	
	/**
	 * 解释：本方法用于计算output。先由input层计算出hidden层，再由hidden层计算出output层
	 */
	private void calculateOutput(){
		for(int i = 0;i < hiddenUnits;i++){
			double temp = 0;
			for(int j = 0;j < inputUnits;j++){
				temp += weightIH[i][j] * input[j];
			}
			//注意：公式可能修改处
			outputHidden[i] = sigmod(temp + biasHidden[i]);
		}
		for(int i = 0;i < outputUnits;i++){
			double temp = 0;
			for(int j = 0;j < hiddenUnits;j++){
				temp += weightHO[i][j] * outputHidden[j];	
			}
			//注意：公式可能修改处
			actualOutput[i] = sigmod(temp + biasOutput[i]);
		}
	}

	/**
	 * 解释：本方法用于刷新现有的所有weight与bias参数.
	 */
	private void updateWeightAndBias(){
		for(int j = 0;j < hiddenUnits;j++){
			for(int k = 0;k < inputUnits;k++){
				//注意：公式可能修改处
				weightIH[j][k] += alpha * outputHidden[j] * (1 - outputHidden[j]) * input[k] * deviationAmount(j);
			}
			//注意：公式可能修改处
			biasHidden[j] += alpha * outputHidden[j] * (1 - outputHidden[j]) * deviationAmount(j);
		}
		for(int i = 0;i < outputUnits;i++){
			for(int j = 0;j < hiddenUnits;j++){
				//注意：公式可能修改处
				weightHO[i][j] += beta * (desiredOutput[i] - actualOutput[i]) * actualOutput[i] * (1 - actualOutput[i]) * outputHidden[j];
			}
			//注意：公式可能修改处
			biasOutput[i] = beta * (desiredOutput[i] - actualOutput[i]) * actualOutput[i] * (1 - actualOutput[i]);
		}	
	}
	
	/**
	 * 解释：该方法用于每隔一定的运算次数之后，对参数打折，防止参数暴增出现剧烈抖动
	 */
	private void adjustWeight(){
		for(int j = 0;j < hiddenUnits;j++){
			for(int k = 0;k < inputUnits;k++){
				//注意：公式可能修改处
				weightIH[j][k] *= ConstantParameter.WEIGHTADJUST;
			}
			//注意：公式可能修改处
			biasHidden[j] *= ConstantParameter.WEIGHTADJUST;
		}
		for(int i = 0;i < outputUnits;i++){
			for(int j = 0;j < hiddenUnits;j++){
				//注意：公式可能修改处
				weightHO[i][j] *= ConstantParameter.WEIGHTADJUST;
			}
			//注意：公式可能修改处
			biasOutput[i] *= ConstantParameter.WEIGHTADJUST;
		}
	}
	
	/**
	 * 解释：调整超变量
	 */
	private void updateSuperValue(){
		
	}
	
	/**
	 * 解释：是上边函数的辅助计算式
	 * @param j
	 * @return
	 */
	private double deviationAmount(int j){
		double temp = 0;
		for(int i = 0;i < outputUnits;i++){
			temp += weightHO[i][j] * actualOutput[i] * (1 - actualOutput[i]) * (desiredOutput[i] - actualOutput[i]);
		}
		return temp;
	}
	
	/**
	 * 解释：该方法实现sigmod函数
	 * @param number 传入的自变量
	 * @return 根据自变量算出的应变量值 
	 */
	private double sigmod(double number){
		return (1 / (1 + Math.exp(-number)));
	}
	
	/**
	 * 解释：本方法用于猜测数字,然后将猜测结果输出到本地
	 * @throws IOException 抛出文件IO错误
	 */
	private void guessNumberAndSaveAnswer() throws IOException{
		double max = 0;
		for(int i = 0;i < outputUnits;i++){
			if(actualOutput[i] > max){
				max = actualOutput[i];
				numberGuess = i;
			}
		}
		File file = new File(ConstantParameter.TESTSETRESULTPATH);
		BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
		fw.append("" + numberGuess);
		fw.newLine();
		fw.flush();
		fw.close();
	}

	/**
	 * 解释：该方法用于从本地读取相应的参数bias以及各个weight
	 * @throws IOException 抛出【IO】错误
	 * @throws ClassNotFoundException 抛出【类无法找到】错误 
	 * @throws FileNotFoundException  抛出【无法找到文件】错误
	 */
	private void readParaFromDisk() throws FileNotFoundException, ClassNotFoundException, IOException{
		for(int i = 0;i < hiddenUnits;i++){
			IHFormulaParameter tempReadIH = new IHFormulaParameter(null,0,i);
			tempReadIH.readFromDiskIH();
			biasHidden[i] = tempReadIH.getHiddenBias();
			for(int j = 0;j < inputUnits;j++){
				weightIH[i][j] = tempReadIH.getHiddenPara(j);
			}
		}
		for(int i = 0;i < outputUnits;i++){
			HOFormulaParameter tempReadHO = new HOFormulaParameter(null,0,i);
			tempReadHO.readFromDiskHO();
			biasOutput[i] = tempReadHO.getOutputBias();
			for(int j = 0;j < hiddenUnits;j++){
				weightHO[i][j] = tempReadHO.getOutputPara(j);
			}
		}
	}
	
	/**
	 * 解释：本方法用于将当前所有的参数暂存在磁盘中
	 * @throws FileNotFoundException 抛出【文件无法找到错误】
	 * @throws IOException 抛出【IO】错误
	 */
	private void saveParaToDisk() throws FileNotFoundException, IOException{
		for(int i = 0;i < hiddenUnits;i++){
			IHFormulaParameter tempWriteIH = new IHFormulaParameter(weightIH[i],biasHidden[i],i);
			tempWriteIH.writeToDiskIH();
		}
		for(int i = 0;i < outputUnits;i++){
			HOFormulaParameter tempWriteHO = new HOFormulaParameter(weightHO[i],biasOutput[i],i);
			tempWriteHO.writeToDiskHO();
		}
	}
}
