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
	
	//empty constructor, not gonna make this.
	private ConstantStore(){
		
	}
	
	/**
	 * initialize and build literal map used to hold all display literals in game
	 */
	public static final Map<String, String> LIT_MAP;
	static{
		Map<String,String> Literal_Map = new HashMap<String,String>(); 
		Literal_Map.put("MM_TITLE","Oregon Trail");
		Literal_Map.put("MM_PRESS_ENTER","Press Enter to Start");
		Literal_Map.put("PC_PROMPT","You want to select a profession, eh?");
		Literal_Map.put("OT_CONFIRM","Confirm");
		Literal_Map.put("OT_CANCEL","Cancel");
		Literal_Map.put("OT_PACE","Pace:");
		Literal_Map.put("OT_RATIONS","Rations:");
		Literal_Map.put("OT_SKILL","Skill ");
		Literal_Map.put("OT_NEW_PLAYER","New Player");
		Literal_Map.put("OT_PROFESSION","Profession");
		Literal_Map.put("OT_CHANGE_SKILL","Change Skill");
		Literal_Map.put("STS_STORE", "Store");
		Literal_Map.put("STS_PRESS_ESCAPE", "Press Escape to Leave");

		//add gui literals here following above syntax - "<string to call literal>". 
		LIT_MAP = Collections.unmodifiableMap(Literal_Map);
	}
	

}
