package org.myorg;

import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class BikeSharing {
	
	public static void main(String[] args)
			throws Exception
	{
		Configuration conf = new Configuration();
		
		if(args.length != 4) {
			System.err.println("Usage: BikeSharing <1\\2\\3> <input path> <output path>");
			System.exit(-1);
		}
		
		int UT = Integer.parseInt(args[1]);
		
		if (UT == 2) {
			
			Scanner scanner = new Scanner(System.in);
			
			System.out.println("<Station Number>");
			String stationNumber = scanner.next();
			scanner.nextLine();
			conf.set("StationNumber", stationNumber);
			
			System.out.println("<AgeTarget: -1 (none) \\ 0 (under 18) \\ 1 (18-24) \\ 2 (25-34) \\ 3 (35-44) \\ 4 (45-54) \\ 5 (55-64) \\ 6 (over 65)>");
			String ageTarget = scanner.next();
			scanner.nextLine();
			conf.set("AgeTarget", ageTarget);
			
			System.out.println("<Male\\Female\\Other\\None>");
			String genderTarget = scanner.next();
			scanner.nextLine();
			conf.set("GenderTarget", genderTarget);
			
			scanner.close();
			
		}
		
		Job job;
		job=Job.getInstance(conf, "Bike Sharing");
		job.setJarByClass(BikeSharing.class);
		
		FileInputFormat.addInputPath(job, new Path(args[2]));
		FileOutputFormat.setOutputPath(job, new Path(args[3]));
		
		if (UT == 1) {
			
			job.setMapperClass(EndStationsInvertedIndexMapper.class);
			job.setReducerClass(EndStationsInvertedIndexReducer.class);
			job.setCombinerClass(EndStationsInvertedIndexReducer.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			
		} else if (UT == 2){
			
			job.setMapperClass(StationAdvertisingMapper.class);
			job.setReducerClass(StationAdvertisingReducer.class);
			job.setCombinerClass(StationAdvertisingCombiner.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(NullWritable.class);
			job.setMapOutputValueClass(IntWritable.class);

		} else if (UT == 3) {
			
			job.setMapperClass(AverageTripDurationNDistanceMapper.class);
			job.setReducerClass(AverageTripDurationNDistanceReducer.class);
			job.setCombinerClass(AverageTripDurationNDistanceReducer.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			
		} else {
			System.err.println("Usage: BikeSharing <1/2> <input path> <output path>");
			System.exit(-1);
		}
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
