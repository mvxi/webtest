package org.example.webtest.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class SecurityInfo {
    @JsonProperty("important_version")
    private ImportantEdition importantEdition;

    @JsonProperty("detailed_version")
    private DetailedEdition detailedEdition;
    @JsonProperty("risk_count")
    private String riskCount;

    // Getters & Setters
    // ------------------------------

    // 重要版（固定字段）
    public static class ImportantEdition {
        @JsonProperty("安全隐患")
        private String potentialRisks;

        @JsonProperty("重要提醒")
        private String majorHazardReminder;

        @JsonProperty("法律依据")
        private String legalBasis;

        @JsonProperty("巡检建议")
        private String rectificationSuggestions;

        // Getters & Setters
    }

    // 详细版（动态键值对）
    public static class DetailedEdition {
        @JsonProperty("安全隐患")
        private Map<String, String> potentialRisks;  // 动态键值对

        @JsonProperty("重要提醒")
        private Map<String, String> majorHazardReminder;

        @JsonProperty("法律依据")
        private Map<String, String> legalBasis;

        @JsonProperty("巡检建议")
        private Map<String, String> rectificationSuggestions;

        // Getters & Setters
    }
}
