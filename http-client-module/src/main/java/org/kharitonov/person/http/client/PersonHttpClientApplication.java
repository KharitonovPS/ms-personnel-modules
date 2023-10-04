package org.kharitonov.person.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.kharitonov.person.http.client.client.PersonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PersonHttpClientApplication {


    public static void main(String[] args) throws JsonProcessingException {

        final Logger log = LoggerFactory.getLogger(PersonHttpClientApplication.class);
        PersonClient personClient = new PersonClient();


        log.info("-------------findAllPerson----------------");
        log.info(personClient.findAllPerson().toString());
        log.info("------------------------------------------");

        log.info("--------------getPerson-------------------");
        log.info(personClient.getPerson(1).toString());
        log.info("------------------------------------------");

        log.info("-------------filterMultiplyEven-----------");
        log.info(personClient.filterMultiplyEven().toString());
        log.info("------------------------------------------");

        log.info("-------------sortByAge-----------");
        log.info(personClient.sortByAge().toString());
        log.info("------------------------------------------");

        log.info("-----------sortByFirstCharOfName----------");
        log.info(personClient.sortByFirstCharOfName().toString());
        log.info("------------------------------------------");

        log.info("-----------------findMaxAge----------------");
        log.info(personClient.findMaxAge().toString());
        log.info("------------------------------------------");

        log.info("--------------skipAndLimitation-----------");
        log.info(personClient.skipAndLimitation().toString());
        log.info("------------------------------------------");

        log.info("------------personNamesAsString-----------");
        log.info(personClient.personNamesAsString());
        log.info("------------------------------------------");

        log.info("----------------isUnderage----------------");
        log.info(personClient.isUnderage());
        log.info("------------------------------------------");

        log.info("---------increaseAgeByTenPercent----------");
        log.info(personClient.increaseAgeByTenPercent().toString());
        log.info("------------------------------------------");

        log.info("-------------deleteDuplicate-------------");
        log.info("Status code: " + personClient.deleteDuplicate());
        log.info("------------------------------------------");

        log.info("-------------getStatistic-------------");
        log.info(personClient.getStatistic().toString());
        log.info("------------------------------------------");

        log.info("-------------addPerson-------------");
        log.info(personClient.addPerson(12, "Test1"));
        log.info("------------------------------------------");

        log.info("-------------deletePerson-------------");
        log.info(personClient.deletePerson(2));
        log.info("------------------------------------------");

        log.info("-------------updatePerson-------------");
        log.info(personClient.updatePerson(12, "Will", 3));
        log.info("------------------------------------------");

        log.info("-------------findAllPerson----------------");
        log.info(personClient.findAllPerson().toString());
        log.info("------------------------------------------");
    }
}
