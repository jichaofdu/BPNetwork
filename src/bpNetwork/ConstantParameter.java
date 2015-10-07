package bpNetwork;

/**
 * 解释：本类用于设定整个BP Network中的各个需要手动设定的参数值
 * @author 13302010019-冀超
 *
 */
public class ConstantParameter {
	//解释：该参数为矩阵的上下行数
	public static final int MATRIXHIGH = 28;
	//解释：该参数为矩阵的左右列数
	public static final int MATRIXWIDTH = 28;
	//解释：该参数为输入端的向量的长度
	public static final int VECTORLENGTH = MATRIXHIGH * MATRIXWIDTH;
	//解释：该参数为输入端的个数
	public static final int INPUTNUMBER = VECTORLENGTH;
	//解释：该参数为中间层的个数,推荐为输入端的1.5倍
	public static final int HIDDENNUMBER = (int) (INPUTNUMBER * 1.5);
	//解释：该参数为输出层的个数
	public static final int OUTPUTNUMBER = 10;
	//解释：训练集中数据的个数
	public static final int TRAINNINGSETNUMBER = 65000;
	//解释：修正集中数据的个数
	public static final int DEVELOPMENTSETNUMBER = 70000 - TRAINNINGSETNUMBER;
	//解释：测试集合个数
	public static final int TESTSETNUMBER = 5000;
	//解释：学习速率Alpha
	public static final double ALPHA = 0.05;
	//解释：学习速率Beta
	public static final double BETA = 0.05;
	//解释：错误限制，小于此值停止算法
	public static final double ERRORLIMIT = 0.1;
	//解释：PART 1存储临时数据的指定路径
	public static final String DATASAVEPATH = "d:\\PJ1\\part1\\data\\";
	//解释：PART 1数据集的存储位置
	public static final String DATASETPATH = "d:\\dataset\\";
	//解释：测试集合结果输出路径
	public static final String TESTSETRESULTPATH = "d:\\TestSetResult.txt";
	//解释：执行日志输出路径
	public static final String LOGPATH = "d:\\Log_Record.txt";
	//解释：每隔固定的轮回weight需要缩小的乘数
	public static final double WEIGHTADJUST = 0.9999;
}

