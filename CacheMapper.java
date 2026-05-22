import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CacheMapper extends Mapper<LongWritable, Text, Text, Text> {
    
    private Map<String, String> urlToCategory = new HashMap<>();
    private Text outputKey = new Text();
    private Text outputValue = new Text();
    private int skippedLines = 0;
    
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        urlToCategory.put("172.19.1.46", "CLIENT");
        urlToCategory.put("10.200.7.7", "PROXY");
        urlToCategory.put("192.168.1.1", "INTERNAL");
        urlToCategory.put("8.8.8.8", "DNS");
        urlToCategory.put("1.1.1.1", "CLOUDFLARE");
    }
    
    @Override
    protected void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {
        
        String line = value.toString();
        
        if (line.startsWith("Flow.ID")) {
            return;
        }
        
        String[] fields = line.split(",");
        if (fields.length < 80) {
            skippedLines++;
            return;
        }
        
        try {
            String urlPath = fields[3].trim();
            
            // Use long instead of int to avoid overflow
            long responseTimeMs = (long) Double.parseDouble(fields[7].trim());
            
            // Normalize: convert to milliseconds if too large
            if (responseTimeMs > 1000000) {
                responseTimeMs = responseTimeMs / 1000000; // convert to seconds
            }
            
            String label = fields[78].trim();
            int statusCode = label.equalsIgnoreCase("BENIGN") ? 200 : 404;
            
            String category = urlToCategory.get(urlPath);
            if (category == null) {
                category = "OTHER";
            }
            
            outputKey.set(category);
            outputValue.set(responseTimeMs + "|" + statusCode);
            context.write(outputKey, outputValue);
            
        } catch (Exception e) {
            skippedLines++;
        }
    }
    
    @Override
    protected void cleanup(Context context) {
        System.out.println("Mapper skipped lines: " + skippedLines);
    }
}