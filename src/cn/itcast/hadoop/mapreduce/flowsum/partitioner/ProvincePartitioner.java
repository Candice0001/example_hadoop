package cn.itcast.hadoop.mapreduce.flowsum.partitioner;

import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import cn.itcast.hadoop.mapreduce.flowsum.FlowBean;

/**
 * 自定义分区规则：
 * 		将流量汇总统计结果按照手机号归属地不同省份输出到不同的文件中
 * 
 * Title: ProvincePartitioner.java
 * Description: 
 * @author chengge
 * @date 2018年8月26日
 *
 */
public class ProvincePartitioner extends Partitioner<Text, FlowBean>{
	
	public static HashMap<String, Integer> provinceMap = new HashMap<>();
	
	//模拟分区:根据手机号的前三位确定分区
	static {
		provinceMap.put("136", 0);
		provinceMap.put("137", 1);
		provinceMap.put("138", 2);
		provinceMap.put("139", 3);
	}
	
	/**
	 * 这里就是实际分区方法，返回的就是分区编号，分区编号决定了数据到哪一个分区， part-r-00000?
	 */
	@Override
	public int getPartition(Text key, FlowBean value, int numPartitions) {
		
		Integer code = provinceMap.get(key.toString().substring(0, 3));
		System.out.println(key+"的code:"+code);
		
		if (code != null) {
			return code;
		}
		//其他手机号都归为4分区
		return 4;
	}

}
