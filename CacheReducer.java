import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CacheReducer extends Reducer<Text, Text, Text, Text> {
    
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        
        long requestCount = 0;
        long sumResponseTime = 0;
        long errorCount = 0;
        
        for (Text value : values) {
            String[] parts = value.toString().split("\\|");
            if (parts.length == 2) {
                try {
                    long responseTimeMs = Long.parseLong(parts[0]);
                    int statusCode = Integer.parseInt(parts[1]);
                    
                    requestCount++;
                    sumResponseTime += responseTimeMs;
                    if (statusCode >= 400) errorCount++;
                } catch (NumberFormatException e) {}
            }
        }
        
        if (requestCount > 0) {
            long avgResponseTime = sumResponseTime / requestCount;
            context.write(key, new Text(requestCount + "\t" + avgResponseTime + "\t" + errorCount));
        }
    }
}