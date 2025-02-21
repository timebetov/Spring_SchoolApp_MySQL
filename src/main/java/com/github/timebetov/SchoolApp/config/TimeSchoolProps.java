package com.github.timebetov.SchoolApp.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Component("timeSchoolProps")
@Data
@ConfigurationProperties(prefix = "timeschool")
@Validated
public class TimeSchoolProps {

    @Min(value = 5, message = "Must be between 5 and 25")
    @Max(value = 25, message = "Must be between 5 and 25")
    private int pageSize;

    private Map<String, String> contact;

    private List<String> branches;
}
