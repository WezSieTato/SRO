package rso.generator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rso.core.model.Group;
import rso.core.model.Person;
import rso.core.service.GroupService;
import rso.core.service.PersonService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by kometa on 27.04.2015.
 */
@Component
public class Generator {

    private final String firstNamesSource = System.getProperty("user.dir") + "/etc/imiona.csv";
    private final String lastNamesSource = System.getProperty("user.dir") + "/etc/nazwiska.txt";
    @Autowired
    GroupService groupService;
    @Autowired
    PersonService personService;
    Collection<Person> generatedPersons;

    public void generate(int numberOfUsersToGenerate) {

        generatedPersons = new ArrayList<Person>();


        ArrayList<String> firstNames = this.loadFirstNames(firstNamesSource);
        ArrayList<String> lastNames = this.loadLastNames(lastNamesSource);


        Random rand = new Random(System.nanoTime());

        List<Group> classes = new ArrayList<Group>(groupService.findAll());
        for (int i = 0; i < numberOfUsersToGenerate; ++i) {

            Person newPerson = new Person();
            newPerson.setFirstName(firstNames.get(rand.nextInt(firstNames.size())));
            newPerson.setLastName(lastNames.get(rand.nextInt(lastNames.size())));


            Collections.shuffle(classes);
            newPerson.setupGroups(classes.subList(0, rand.nextInt(classes.size())));

            newPerson.setDateOfBirth(this.generateDateOfBirth());
            newPerson.setTimestamp(new Date());
            generatedPersons.add(newPerson);
        }

        personService.save(generatedPersons);

    }


    public ArrayList<String> loadFirstNames(String file){

        ArrayList<String> names= new ArrayList<String>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {


                String[] name = line.split(cvsSplitBy);
                names.add(name[0].replace("\"", ""));

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return names;
    }

    public ArrayList<String> loadLastNames(String file){

        ArrayList<String> names= new ArrayList<String>();
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {



                names.add(line);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return names;
    }


    private Date generateDateOfBirth(){
        GregorianCalendar gc = new GregorianCalendar();

        int year = randBetween(1985, 1995);

        gc.set(Calendar.YEAR, year);

        int dayOfYear = randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));

        gc.set(Calendar.DAY_OF_YEAR, dayOfYear);


        return gc.getTime();
    }


    private int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
}
