import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CacheDriverWithCombiner {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: CacheDriverWithCombiner <input path> <output path> <cache file path>");
            System.exit(1);
        }
        
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "URL Category Analysis WITH Combiner");
        
        job.setJarByClass(CacheDriverWithCombiner.class);
        job.setMapperClass(CacheMapper.class);
        job.setCombinerClass(CacheReducer.class);
        job.setReducerClass(CacheReducer.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        job.setNumReduceTasks(1);
        
        job.addCacheFile(new Path(args[2]).toUri());
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}