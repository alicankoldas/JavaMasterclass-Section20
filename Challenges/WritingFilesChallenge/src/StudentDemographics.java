public record StudentDemographics(String countryCode, int enrolledMonth,
                                  int enrolledYear, int ageAtEnrollment, String gender,
                                  boolean previousProgrammingExperience) {

    @Override
    public String toString() {
        //return "%s,%d,%d,%d,%s,%b".formatted(countryCode, enrolledMonth, enrolledYear,
        //        ageAtEnrollment, gender, previousProgrammingExperience);

        return """
                {
                    \t\t"countryCode": \"%s\",
                    \t\t"enrolledMonth": %d,
                    \t\t"enrolledYear": %d,
                    \t\t"ageAtEnrollment": %d,
                    \t\t"gender": \"%s\",
                    \t\t"previousProgrammingExperience": %b
                \t\t}
                """.formatted(
                        countryCode, enrolledMonth, enrolledYear,
                                ageAtEnrollment, gender, previousProgrammingExperience);
    }
}
