package caceresenzo.apps.quickhour.codec.implementations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import caceresenzo.apps.quickhour.codec.SortReferenceTemplateCodec;
import caceresenzo.apps.quickhour.codec.chartable.ValueCharTable;
import caceresenzo.apps.quickhour.config.Config;
import caceresenzo.apps.quickhour.models.SortTemplateReference;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.codec.chartable.SeparatorCharTable;
import caceresenzo.libs.filesystem.FileChecker;
import caceresenzo.libs.logger.Logger;
import caceresenzo.libs.parse.ParseUtils;
import caceresenzo.libs.string.StringUtils;

public class NegroExcelReferenceSortTemplateCodec extends SortReferenceTemplateCodec implements SeparatorCharTable, ValueCharTable {
	
	@Override
	public List<SortTemplateReference> read(File file) throws Exception {
		List<SortTemplateReference> sortReferences = new ArrayList<>();
		
		if (!FileChecker.checkFile(file, true, true) || Config.REFERENCE_SORT_CODEC_SHEET_PAGE == Config.NO_VALUE) {
			return null;
		}
		
		List<String> lines = new ArrayList<>();
		
		boolean completeBreak = false;
		for (String compressedLine : Utils.readExcel(file, Config.REFERENCE_SORT_CODEC_SHEET_PAGE)) {
			for (String uncompressedLine : compressedLine.split(DATA_SEPARATOR)) {
				if (uncompressedLine.equals(DATA_FILL)) {
					continue;
				}
				
				if (uncompressedLine.contains("Observations")) {
					completeBreak = true;
					break;
				}
				
				lines.add(uncompressedLine);
			}
			
			if (completeBreak) {
				break;
			}
		}
		
		boolean canStart = false;
		for (int index = 0; index < lines.size(); index++) {
			String line = lines.get(index).toUpperCase();
			
			if (!StringUtils.validate(line)) {
				continue;
			}
			
			if (line.contains("(EN ROUGE DIFFÉRENT DE 38H)")) {
				canStart = true;
				continue;
			}
			
			if (!canStart) {
				continue;
			}
			
			if (line.equals("Observations")) {
				break;
			}
			
			if (line.contains(" ")) {
				sortReferences.add(new SortTemplateReference(line, true));
				continue;
			} else { /* PD */
				if (line.startsWith("PD") || line.startsWith("BT")) {
					String fullReference = line;
					
					int subindex = 0;
					while (true) {
						String newLine = lines.get(index + subindex).toUpperCase();
						
						if (newLine.startsWith("PD") || newLine.startsWith("BT")) {
							break;
						}
						
						fullReference += " " + newLine.replace("\n", " ");
						subindex++;
					}
					
					index += subindex;
					
					sortReferences.add(new SortTemplateReference(fullReference.replace("\n", " "), true));
				} else {
					/* BT */
					int possibleBt = ParseUtils.parseInt(line, NO_VALUE);
					
					if (possibleBt != NO_VALUE) {
						String reference = "BT" + possibleBt;
						
						sortReferences.add(new SortTemplateReference(reference, true));
					} else {
						sortReferences.add(new SortTemplateReference(line, true));
					}
				}
			}
		}
		
		for (SortTemplateReference reference : sortReferences) {
			Logger.info("-- %s", reference.getString());
		}
		Logger.info("SIZE %s", sortReferences.size());
		
		return sortReferences;
	}
	
}