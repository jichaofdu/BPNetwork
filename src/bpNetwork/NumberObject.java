package bpNetwork;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 解释：本class用于将一个数据读入矩阵，存储其信息
 * @author 13302010019-冀超
 * 
 */
public class NumberObject {
	private double[][] numberMatrix;
	private int actualNumber;
    private double meanValue;
    private double standardDeviation;
	
	/**
	 * 解释：读取indexOfFile中的矩阵信息然后将其写入到对象之中
	 * @param indexOfFile 要读入文件的文件序号
	 */
	public NumberObject(int indexOfFile){
		try {
			readRawNumber(indexOfFile);
			calculateMeanAndStandardDeviation();		
			normalizeNumber();  		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解释：从文件中读出初始的数据
	 * @param indexOfFile 读取哪一个样本的数据，该样本的序号
	 * @throws IOException 抛出【IO】错误
	 */
	private void readRawNumber(int indexOfFile) throws IOException{
		numberMatrix = new double[ConstantParameter.MATRIXHIGH][ConstantParameter.MATRIXWIDTH];
		File file = new File(ConstantParameter.DATASETPATH + indexOfFile + ".txt");
		if(file.isFile() && file.exists()){
             InputStreamReader read = new InputStreamReader(new FileInputStream(file));
             BufferedReader bufferedReader = new BufferedReader(read);
             String lineTxt = null;
             for(int i = 0;i < ConstantParameter.MATRIXHIGH;i++){
            	 lineTxt = bufferedReader.readLine();
            	 String[] tempList = lineTxt.split(" ");
            	 for(int j = 0;j < ConstantParameter.MATRIXWIDTH;j++){
            		 numberMatrix[i][j] = Integer.parseInt(tempList[j]);
            	 }
             }
             try{
            	 actualNumber = Integer.parseInt(bufferedReader.readLine());
             }catch(Exception e){
            	 actualNumber = -1;
             }
             
            
     
             read.close();
		}
	}
	
	/**
	 * 解释：计算向量的方差和标准差
	 */
	private void calculateMeanAndStandardDeviation(){
		double amount = 0;
        for(int i = 0;i < ConstantParameter.MATRIXHIGH;i++){
        	for(int j = 0;j < ConstantParameter.MATRIXWIDTH;j++){
       			amount += numberMatrix[i][j];
       	 	}
        }
        meanValue = amount / ConstantParameter.VECTORLENGTH;
        amount = 0;
        for(int i = 0;i < ConstantParameter.MATRIXHIGH;i++){
        	for(int j = 0;j < ConstantParameter.MATRIXWIDTH;j++){
       			amount += Math.pow(numberMatrix[i][j] - meanValue, 2);
       	 	}
        }
        standardDeviation = Math.sqrt(amount / ConstantParameter.VECTORLENGTH);
	}
	
	
	/**
	 * 解释：将读入的灰度值归一化
	 * @param number 准备被处理的数字
	 * @return 处理结束以后的数字
	 */
	private void normalizeNumber(){
		for(int i = 0;i < ConstantParameter.MATRIXHIGH;i++){
			for(int j = 0;j < ConstantParameter.MATRIXWIDTH;j++){
				numberMatrix[i][j] = (numberMatrix[i][j] - meanValue) / standardDeviation;
			}
		}
	}
	
	/**
	 * 解释：获取向量中的值
	 * @param y 这个值位于向量的从上到下第y行
	 * @param x 这个值位于向量的从左到右的第x列
  	 * @return 返回所要求的值
	 */
	public double getValue(int y,int x){
		return numberMatrix[y][x];
	}
	
	/**
	 * 解释：返回这个矩阵对应的真实数字
	 * @return
	 */
	public int getActualNumber() {
		return actualNumber;
	}	
}
