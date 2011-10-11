package controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import models.Person;
import play.db.jpa.JPA;
import play.mvc.Controller;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

public class Application extends Controller {
	private final static List<String> ITEMS = Arrays.asList(
			"Item 1"
			, "Item 2"
			, "Item 3"
			, "Item 4"
			, "Item 5"
	);
	
    public static void index() {
        render();
    }
    
    public static void jquery(List<String> selected) {
    	List<String> items = ITEMS;
    	selected = removeNull(selected);
    	Date date = new Date();
        render(items, selected, date);
    }
    
    public static void dojo(List<String> selected) {
    	List<String> items = ITEMS;
    	selected = removeNull(selected);
    	Date date = new Date();
        render(items, selected, date);
    }
    
    public static void standard(List<String> selected) {
    	List<String> items = ITEMS;
    	selected = removeNull(selected);
    	Date date = new Date();
    	render(items, selected, date);
    }
    
    public static void persons(List<Integer> selectedPersons) {
    	List<Person> items = Person.findAll();
    	selectedPersons = removeNull(selectedPersons);
    	List<Person> selected = Collections.emptyList();
    	if (selectedPersons.size()>0) {
    		selected = JPA.em().createQuery("from Person where id in (?1)")
    		.setParameter(1, selectedPersons)
    		.getResultList();
    	}
    	Date date = new Date();
        render(items, selected, date);
    }
    
    private static <T> List<T> removeNull(List<T> src) {
    	if (src == null) {
    		return Collections.emptyList();
    	}
    	return new LinkedList<T>(Collections2.filter(src, Predicates.notNull()));
    }
}