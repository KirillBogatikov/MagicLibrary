package org.kllbff.magic.parsers.json;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class JsonLoggingParser extends JsonAbstractParser<Map<String, PrintStream>> {

    public JsonLoggingParser() {
        super(null);
        this.linksAllowed = false;
    }
    
    private PrintStream parseTarget(String target) throws FileNotFoundException {
        switch(target) {
            case "stdout": return System.out; 
            case "stderr": return System.err; 
            default: 
                return new PrintStream(target); 
        }
    }

    @Override
    public Map<String, PrintStream> parseResource(Reader reader) throws FileNotFoundException {
        HashMap<String, PrintStream> streams = new HashMap<String, PrintStream>();
        JsonObject jsonObject = jp.parse(reader).getAsJsonArray().get(0).getAsJsonObject();
        
        String target = getProp(jsonObject, "info", null);
        if(target != null) {
            streams.put("info", parseTarget(target));
        }
        
        target = getProp(jsonObject, "warn", null);
        if(target != null) {
            streams.put("warn", parseTarget(target));
        }
        
        target = getProp(jsonObject, "error", null);
        if(target != null) {
            streams.put("error", parseTarget(target));
        }
        
        target = getProp(jsonObject, "debug", null);
        if(target != null) {
            streams.put("debug", parseTarget(target));
        }
        
        return streams;
    }

}
