package caceresenzo.apps.quickhour.codec.implementations;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import caceresenzo.apps.quickhour.codec.ReferenceFormatCodec;
import caceresenzo.apps.quickhour.models.ReferenceFormat;
import caceresenzo.libs.filesystem.FileChecker;
import caceresenzo.libs.json.JsonObject;
import caceresenzo.libs.json.parser.JsonParser;
import caceresenzo.libs.parse.ParseUtils;
import caceresenzo.libs.string.StringUtils;

public class JsonReferenceFormatCodec extends ReferenceFormatCodec {
	
	private static final String JSON_KEY_FORMATS = "formats";
	private static final String JSON_KEY_FORMATS_DISPLAY = "display";
	private static final String JSON_KEY_FORMATS_FORMAT = "format";
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ReferenceFormat> read(File file) throws Exception {
		List<ReferenceFormat> formats = new ArrayList<ReferenceFormat>();
		
		if (!FileChecker.checkFile(file, true, true)) {
			write(file, null);
			return formats;
		}
		
		JsonObject json = (JsonObject) new JsonParser().parse(StringUtils.fromFile(file));
		
		HashMap<String, Object> formatsMap = (HashMap<String, Object>) json.get(JSON_KEY_FORMATS);
		for (Entry<String, Object> formatsMapEntry : formatsMap.entrySet()) {
			HashMap<String, Object> userDataMap = (HashMap<String, Object>) formatsMapEntry.getValue();
			
			String name = formatsMapEntry.getKey();
			String display = ParseUtils.parseString(userDataMap.get(JSON_KEY_FORMATS_DISPLAY), name);
			String format = ParseUtils.parseString(userDataMap.get(JSON_KEY_FORMATS_FORMAT), "%s");
			
			formats.add(new ReferenceFormat(name, display, format));
		}
		
		return formats;
	}
	
}