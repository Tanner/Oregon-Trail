package core;

import java.util.Collections;
import java.util.HashMap; 
import java.util.Map; 

/**
 * Class to hold constants used by OT game
 * 
 * @author john 
 */

public final class ConstantStore {
	
	private ConstantStore(){
		
	}
	
	/**
	 * initialize and build literal map used to hold all display literals in game
	 */
	public static final Map<String, String> LIT_MAP;
	static{
		Map<String,String> Literal_Map = new HashMap<String,String>(); 
		Literal_Map.put("OT_TITLE","Oregon Trail");
		//add gui literals here following above syntax - "<string to call literal>","<string to display>"
		LIT_MAP = Collections.unmodifiableMap(Literal_Map);
	}
	

}
