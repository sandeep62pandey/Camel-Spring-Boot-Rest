package com.sample.service;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.report.ValidationReport;
import com.atlassian.oai.validator.whitelist.ValidationErrorsWhitelist;
import com.atlassian.oai.validator.whitelist.rule.WhitelistRules;
import com.sample.model.Failures;
import org.apache.camel.Exchange;
import org.apache.camel.component.bean.validator.BeanValidationException;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.atlassian.oai.validator.whitelist.rule.WhitelistRules.allOf;

public class SalesService {

    public Failures frameValidationException(BeanValidationException exception){

        Set<ConstraintViolation<Object>> constraintViolations = exception.getConstraintViolations();
        Failures failures = new Failures();
        List<Failures.Failure> failuresList = new ArrayList<>();
        for(ConstraintViolation constraintViolation : constraintViolations){
            Failures.Failure failure = new Failures.Failure();
            failure.setCode(String.valueOf(constraintViolation.getPropertyPath()));
            failure.setReason(String.valueOf(constraintViolation.getMessage()));
            failuresList.add(failure);
        }
        failures.setFailures(failuresList);
        return failures;
    }


    public void validateJson(Exchange ex){
        String body = ex.getIn().getBody(String.class);
        String entityId = ex.getIn().getHeader("entityId", String.class);
        String year = ex.getIn().getHeader("year", String.class);
        final OpenApiInteractionValidator validator = OpenApiInteractionValidator
                .createFor("/type.json").withWhitelist(ValidationErrorsWhitelist.create()
                .withRule(
                        "Ignore missing security when getting store inventory",
                        allOf(
                                WhitelistRules.messageHasKey("validation.request.security.missing"))))
                .build();
        Request request = SimpleRequest.Builder.post("/sales-details/customer/"+entityId+"/"+year).withBody(body).withContentType("application/json").build();
        final ValidationReport report = validator.validateRequest(request);
        if(report.hasErrors()){
            List<ValidationReport.Message> messages = report.getMessages();
            for (ValidationReport.Message message:messages){
                System.out.println(message.getKey());
                if(message.getContext().isPresent()){
                    message.getContext().get().getApiResponseDefinition();
                }
                System.out.println(message.getMessage());
            }
        }
    }
}
