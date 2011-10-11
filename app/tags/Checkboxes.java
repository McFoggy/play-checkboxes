package tags;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.templates.FastTags;
import play.templates.JavaExtensions;
import play.templates.GroovyTemplate.ExecutableTemplate;


@FastTags.Namespace("input")
public class Checkboxes extends play.templates.FastTags {
	/**
	 * 
	 * @param args
	 * @param body
	 * @param out
	 * @param template
	 * @param fromLine
	 */
	public static void _checkboxes(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		List<?> all = (List<?>) args.get("all");
		List<?> selected = (List<?>) args.get("selected");
		selected = (selected==null)?Collections.emptyList():selected;
		
		String baseName = (args.get("basename")==null)?"selected":(String)args.get("basename");
		String property = (String) args.get("property");
		
		String fieldValue = (String) args.get("value");
		String fieldText = (String) args.get("text");
		
		Map<String, Object> properties = new HashMap<String, Object>();

		int i = 0;
		String keyForObjectValue = "value";
		String keyForObjectText = "text";
		out.println("<fieldset class=\"checkboxes\">");
		for (Object obj : all) {
			fillProperty(fieldValue, properties, keyForObjectValue, obj);
			fillProperty(fieldText, properties, keyForObjectText, obj);
			properties.put("name", baseName + "[" + i + "]" + ((property==null)?"":"."+property));
			properties.put("selected", Boolean.valueOf(selected.contains(obj)));
			
			writeCheckBox(out, properties);
			i++;
		}
		writeAllNoneCheckboxes(out, selected.containsAll(all));
		out.println("</fieldset>");
	}

	private static void writeAllNoneCheckboxes(PrintWriter out, boolean checked) {
		out.println(String.format("<input type=\"checkbox\" class=\"checkall\" %s>All/None</input>", checked?"checked=\"checked\"":""));
	}

	private static void writeCheckBox(PrintWriter out, Map<String, Object> properties) {
		Boolean selected = (Boolean) properties.get("selected");
		out.println(
			String.format("<input type=\"checkbox\" name=\"%s\" value=\"%s\" %s>%s</input>"
					, properties.get("name")
					, properties.get("value")
					, selected.booleanValue()?"checked=\"checked\"":""
					, properties.get("text")
			)
		);
	}

	private static void fillProperty(String fieldValue,
			Map<String, Object> properties, String keyForObjectValue, Object obj) {
		try {
			Field f = obj.getClass().getField(fieldValue);
		    try{
		        Method getter = obj.getClass().getMethod("get"+JavaExtensions.capFirst(f.getName()));
		        properties.put(keyForObjectValue, getter.invoke(obj, new Object[0]));
		    }catch(NoSuchMethodException e){
		    	properties.put(keyForObjectValue,f.get(obj));
		    }
		} catch (Exception e) {
		    try{
		        Method getter = obj.getClass().getMethod("get"+JavaExtensions.capFirst(fieldValue));
		        properties.put(keyForObjectValue, getter.invoke(obj, new Object[0]));
		    } catch(Exception ex){
			    properties.put(keyForObjectValue, obj.toString());
		    }
		}
	}
}
