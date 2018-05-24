package com.intuit.cg.backendtechassessment;

import com.intuit.cg.backendtechassessment.model.Person;
import com.intuit.cg.backendtechassessment.model.Price;
import com.intuit.cg.backendtechassessment.model.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BackendTechAssessmentApplicationTests {

    public static Project mockProject() {
        return new Project()
                .setDescription("First project on the marketplace")
                .setMaxBudget(new Price()
                        .setValue(new BigDecimal(200.00)))
                .setClosingDate(LocalDate.now().toString())
                .setSeller(new Person()
                        .setName("Peter")
                        .setEmail("peter.parker@avengers.com"));
    }

    @Test
	public void contextLoads() {

	}

}
