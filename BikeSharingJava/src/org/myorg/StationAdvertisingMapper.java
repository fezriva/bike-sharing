package org.myorg;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class StationAdvertisingMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	private Text startStationInfo = new Text();
	private final static IntWritable one = new IntWritable(1);
	
	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException
	{
		Configuration conf = context.getConfiguration();
		Pattern patternStationNumber = Pattern.compile(conf.get("StationNumber"));
		String confAgeTarget = conf.get("AgeTarget");
		String confGenderTarget = conf.get("GenderTarget");
		
		String[] line = value.toString().split(",");
		String ID = line[7];
		
		Matcher matcherStationNumber = patternStationNumber.matcher(ID);	
		
		if (matcherStationNumber.find()) {
			
			String ageTarget = "-1", genderTarget;
			
			if (!confGenderTarget.equals("None")) {
				try {
					
					if (line[14].equals("Male")) {
						genderTarget = "Male";
					} else if (line[14].equals("Female")) {
						genderTarget = "Female";
					} else {
						genderTarget = "Other";
					}
					
			    } catch(ArrayIndexOutOfBoundsException | NumberFormatException e) {
			    	genderTarget = "None";
			    }
			} else {
				genderTarget = "None";
			}
			
			if (!confAgeTarget.equals("-1")) {
				try {
					
					int userAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(line[13]);
					
					if (userAge < 18) {
						ageTarget = "0";
					} else if (userAge >= 18 && userAge < 25) {
						ageTarget = "1";
					} else if (userAge >= 25 && userAge < 35) {
						ageTarget = "2";
					} else if (userAge >= 35 && userAge < 45) {
						ageTarget = "3";
					} else if (userAge >= 45 && userAge < 55) {
						ageTarget = "4";
					} else if (userAge >= 55 && userAge < 65) {
						ageTarget = "5";
					} else if (userAge >= 65) {
						ageTarget = "6";
					}
					
			    } catch(ArrayIndexOutOfBoundsException | NumberFormatException e) {
			    	ageTarget = "-1";
			    }
			} else {
				ageTarget = "-1";
			}
			
			if (confGenderTarget.equals(genderTarget) && confAgeTarget.equals(ageTarget)) {
				
				String startStationString = line[3] + "," + line[4] + "," + line[5] + "," + line[6] + "," + ageTarget + "," + genderTarget;
				startStationInfo.set(startStationString);
				
				context.write(startStationInfo,one);
			}
		}
	}
}