package tags;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.templates.FastTags;
import play.templates.JavaExtensions;
import play.templates.GroovyTemplate.ExecutableTemplate;


@FastTags.Namespace("input")
public class Checkboxes extends play.templates.FastTags {
	private static final String ATT_VALUES = "values";
	private static final String ATT_SELECTED = "selected";
	private static final String ATT_BASE_NAME = "basename";
	private static final String ATT_PROPERTY = "property";
	private static final String ATT_VALUE = "value";
	private static final String ATT_TEXT = "text";
	private static final String ATT_ALL_NONE = "all-none";
	
	private static final String INPUT_TAG_VALUE = "value";
	private static final String INPUT_TAG_TEXT = "text";
	private static final String INPUT_TAG_NAME = "name";
	private static final String INPUT_TAG_SELECTED = "selected";

	private static final String DEFAULT_BASE_NAME = "selected";
	/**
	 * 
	 * @param args
	 * @param body
	 * @param out
	 * @param template
	 * @param fromLine
	 */
	public static void _checkboxes(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		List<?> all = (List<?>) args.get(ATT_VALUES);
		List<?> selected = (List<?>) args.get(ATT_SELECTED);
		selected = (selected==null)?Collections.emptyList():selected;
		
		Boolean allNone = Boolean.TRUE;
		Object allNoneObject = args.get(ATT_ALL_NONE);
		if (allNoneObject instanceof Boolean) {
			allNone = (Boolean) allNoneObject;
		} else if (allNoneObject instanceof String) {
			allNone = Boolean.valueOf((String) allNoneObject);
		} else if (allNoneObject != null) {
			// neither a Boolean nor a String, we ommit it
			Logger.warn("cannot detect a boolean value for attribute[%s], given value is: %s", ATT_ALL_NONE, allNoneObject);
		}
		
		String baseName = (args.get(ATT_BASE_NAME)==null)?DEFAULT_BASE_NAME:(String)args.get(ATT_BASE_NAME);
		String property = (String) args.get(ATT_PROPERTY);
		String fieldValue = (String) args.get(ATT_VALUE);
		String fieldText = (String) args.get(ATT_TEXT);
		
		Map<String, Object> properties = new HashMap<String, Object>();

		int i = 0;
		out.println("<fieldset class=\"checkboxes\">");
		
		for (Object obj : all) {
			fillProperty(fieldValue, properties, INPUT_TAG_VALUE, obj);
			fillProperty(fieldText, properties, INPUT_TAG_TEXT, obj);
			properties.put(INPUT_TAG_NAME, baseName + "[" + i + "]" + ((property==null)?"":"."+property));
			properties.put(INPUT_TAG_SELECTED, Boolean.valueOf(selected.contains(obj)));
			
			writeCheckBox(out, properties);
			i++;
		}
		
		if (allNone) {
			writeAllNoneCheckboxes(out, selected.containsAll(all));
		}
		out.println("</fieldset>");
	}

	private static void writeAllNoneCheckboxes(PrintWriter out, boolean checked) {
		out.println(String.format("<input type=\"checkbox\" class=\"checkall\" %s>All/None</input>", checked?"checked=\"checked\"":""));
	}

	private static void writeCheckBox(PrintWriter out, Map<String, Object> properties) {
		Boolean selected = (Boolean) properties.get(INPUT_TAG_SELECTED);
		out.println(
			String.format("<input type=\"checkbox\" name=\"%s\" value=\"%s\" %s>%s</input>"
					, properties.get(INPUT_TAG_NAME)
					, properties.get(INPUT_TAG_VALUE)
					, selected.booleanValue()?"checked=\"checked\"":""
					, properties.get(INPUT_TAG_TEXT)
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
