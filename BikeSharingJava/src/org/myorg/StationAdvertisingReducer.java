package org.myorg;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class StationAdvertisingReducer extends Reducer<Text, IntWritable, Text, NullWritable> {
	
	@Override
	public void reduce (Text key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException
	{
		int sum = 0;
		for (IntWritable value : values) {
			sum += value.get();
		}
		
		String stationCompleteData = key.toString() + "," + sum;
		
		context.write(new Text(stationCompleteData), NullWritable.get());
	}
}