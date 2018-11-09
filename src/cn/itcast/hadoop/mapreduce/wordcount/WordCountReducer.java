package cn.itcast.hadoop.mapreduce.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * mr程序：Reducer阶段的类
 * KEYIN：reducer阶段输入的数据key的类型，对应mapper的输出key的类型， Text
 * VALUEIN:reducer阶段输入的数据value的类型，对应mapper的输出vaalue的类型， IntWritable
 * 
 * KEYOUT:reducer阶段输出的数据key的类型,在本例子中 就是单词Text
 * VALUEOUT:reducer阶段输出的数据value的类型,在本例子中 就是单词的总次数IntWrtiable
 * 
 * Title: WordCountReducer.java
 * Description: 
 * @author chengge
 * @date 2018年8月20日
 *
 */

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable>{

	/**
	 * 这里是reducer阶段具体业务类的实现方法
	 * 
	 * reduce接收所有来自map阶段处理的数据之后，按照key的字典序进行排序
	 * <hello,1> <hadoop,1> <spark,1> <hadoop,1>
	 * 排序后：
	 * <hadoop,1> <hadoop,1> <hello,1> <spark,1>
	 * 
	 * 按照key是否相同作为一组去调用reduce方法
	 * 本方法的key就是这一组相同kv对的共同key
	 * 把这一组所有的v作为一个迭代器传入我们的reduce方法
	 * 
	 * 
	 */
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,
			Reducer<Text, IntWritable, Text, IntWritable>.Context Context) throws IOException, InterruptedException {
			
		//定义一个计算器
		int count = 0;
		//遍历一组迭代器,把每一个数量1累加起来就构成了单词的总次数
		for(IntWritable value : values) {
			count +=value.get();
		}
		//把最终的结果输出
		Context.write(key, new IntWritable(count));
	}

}
