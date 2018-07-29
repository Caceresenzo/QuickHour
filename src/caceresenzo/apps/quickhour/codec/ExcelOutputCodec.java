package caceresenzo.apps.quickhour.codec;

import java.util.HashMap;
import java.util.List;

import caceresenzo.apps.quickhour.models.QuickHourReference;
import caceresenzo.apps.quickhour.models.QuickHourUser;
import caceresenzo.libs.codec.JsonReadWriteCodec;

public class ExcelOutputCodec extends JsonReadWriteCodec<HashMap<QuickHourUser, List<QuickHourReference>>> {
	
}