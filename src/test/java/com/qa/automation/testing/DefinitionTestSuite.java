package com.qa.automation.testing;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features = "src/test/resources/features/",
        tags = {"@AmazonFeature and @Positive"}
)
public class DefinitionTestSuite {
}
