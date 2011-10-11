package controllers;

import models.Person;

import org.h2.engine.User;

import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {
    public void doJob() {
        // Check if the database is empty
        if(Person.count() == 0) {
            Fixtures.loadModels("initial-persons.yml");
        }
    }
 
}