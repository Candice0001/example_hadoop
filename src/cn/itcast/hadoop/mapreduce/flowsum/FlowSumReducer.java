package cn.itcast.hadoop.mapreduce.flowsum;

import java.io.IOException;

import org.apache.hadoop.hdfs.protocol.RollingUpgradeInfo.Bean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


/**
 * 输入数据：<手机号1，bean> <手机号2，bean> <手机号1，bean> <手机号3，bean>
 *  排序
 *  然后调用reduce方法
 * 
 * Title: FlowSumReducer.java
 * Description: 
 * @author chengge
 * @date 2018年8月24日
 *
 */
public class FlowSumReducer extends Reducer<Text, FlowBean, Text, FlowBean>{

	FlowBean v = new FlowBean();
	@Override
	protected void reduce(Text key, Iterable<FlowBean> values, Reducer<Text, FlowBean, Text, FlowBean>.Context context)
			throws IOException, InterruptedException {
		
		long upFlowCount = 0; //总上行流量
		long downFlowCount = 0; //总下行流量
		
		//迭代
		for (FlowBean flowBean : values) {
			upFlowCount += flowBean.getUpFlow();
			downFlowCount += flowBean.getDownFlow();
		}
		
		v.set(upFlowCount, downFlowCount);
		
		context.write(key, v);
		
	}

	
	

}
