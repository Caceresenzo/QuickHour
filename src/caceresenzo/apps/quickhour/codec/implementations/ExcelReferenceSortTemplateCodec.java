package caceresenzo.apps.quickhour.codec.implementations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import caceresenzo.apps.quickhour.codec.SortReferenceTemplateCodec;
import caceresenzo.apps.quickhour.codec.chartable.EmptyCharTable;
import caceresenzo.apps.quickhour.models.SortTemplateReference;
import caceresenzo.apps.quickhour.utils.Utils;
import caceresenzo.libs.codec.chartable.SeparatorCharTable;
import caceresenzo.libs.filesystem.FileChecker;

public class ExcelReferenceSortTemplateCodec extends SortReferenceTemplateCodec implements SeparatorCharTable, EmptyCharTable {
	
	private static final String FILE_TABLE_HEADER = "IDENTIFIEUR,COMMENTAIRE";
	
	@Override
	public List<SortTemplateReference> read(File file) throws Exception {
		List<SortTemplateReference> sortReferences = new ArrayList<>();
		
		if (!FileChecker.checkFile(file, true, true)) {
			return null;
		}
		
		for (String line : Utils.readExcel(file)) {
			if (line.startsWith(FILE_TABLE_HEADER)) {
				continue;
			}
			
			String[] reference = line.split(DATA_SEPARATOR);
			
			if (reference.length == 0) {
				continue;
			}
			
			boolean displayable = !reference[0].equals(EMPTY);
			
			sortReferences.add(new SortTemplateReference(reference[0], displayable));
		}
		
		return sortReferences;
	}
	
}