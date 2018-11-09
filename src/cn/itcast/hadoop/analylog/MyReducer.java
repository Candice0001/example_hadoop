package cn.itcast.hadoop.analylog;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 
 * NullWritable 是Writable的一个特殊类，实现方法为空，即不从数据流写数据，也不从数据流中读数据，只充当占位符
 * 该处设置reducer的输出为<key,空>
 *
 */
public class MyReducer extends Reducer<LongWritable, Text, Text, NullWritable>{

	@Override
	protected void reduce(LongWritable key, Iterable<Text> vlaue,
			Reducer<LongWritable, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
		
		for (Text vv : vlaue) {
			context.write(vv, NullWritable.get());
		}
	}

	

}
