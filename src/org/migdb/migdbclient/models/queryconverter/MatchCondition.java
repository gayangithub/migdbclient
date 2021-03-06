package org.migdb.migdbclient.models.queryconverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MatchCondition {
	
	private LinkedHashMap<String, Object> matchPair;

	public MatchCondition() {
		matchPair = new LinkedHashMap<String, Object>();
	}

	public LinkedHashMap<String, Object> getMatchPair() {
		return matchPair;
	}
	
	public void setMatchPairs(LinkedHashMap<String, Object> matchPair) {
		this.matchPair.putAll(matchPair);
	}

	public void addCondition(String expression, String key, Object value) {
		HashMap<String, Object> innerPair = new HashMap<String, Object>();

		if(expression.equals("=")) {
			innerPair.put("$eq", value);
		} else if(expression.equals("!=")) {
			innerPair.put("$ne", value);
		} else if(expression.equals("<")) {
			innerPair.put("$lt", value);
		} else if(expression.equals(">")) {
			innerPair.put("$gt", value);
		} else if(expression.equals("<=")) {
			innerPair.put("$lte", value);
		} else if(expression.equals(">=")) {
			innerPair.put("$gte", value);
		} else if(expression.equals("IN")) {
			List<Object> itemList = new ArrayList<Object>();
			StringTokenizer st = new StringTokenizer(value.toString(), ",");
			while (st.hasMoreElements()) {
				Object val = st.nextElement();
				itemList.add(val);
			}
			innerPair.put("$in", value);
		} else if(expression.equals("LIKE")) {
			if (!value.toString().startsWith("%")) {
				matchPair.put(key,"/^"+value.toString().replace("%", "/"));
			} else {
				matchPair.put(key,value.toString().replace("%", "/"));
			}
		}
		
		if(!innerPair.isEmpty()) {
			matchPair.put(key, innerPair);
		}
				
	}
	
	public void addOrCondition(HashMap<String, Object> pair) {
		List<HashMap<String, Object>> orList =  (List<HashMap<String, Object>>) matchPair.get("$or");
		if(orList == null) {
			List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			list.add(pair);
			this.matchPair.put("$or", list);		
		} else {
			orList.add(pair);
		}
	}
	
	

	@Override
	public String toString() {
		String str = "";
		if(matchPair.isEmpty()) {
			str = matchPair.toString().replace("=", ": ").replace("{}", "{ }");
		} else {
			str = matchPair.toString().replace("=", ": ").replace("{", "{ ").replace("}", " }");
		}
		return str;
	}
	
	

}
