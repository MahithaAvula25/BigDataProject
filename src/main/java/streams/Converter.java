package streams;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Converter {
	
    public static void main(String[] args) {
        String csvFile = "onlineshopping.csv"; // Replace with your CSV file path
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> rows = reader.readAll();
            boolean skipHeader = true; // Skip the header row

            ArrayList<Details> detailsList = new ArrayList<Details>();
            
            for (String[] row : rows) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                Details details = new Details();
                
                details.setCust_id(Long.parseLong(row[0]));
                details.setGender(row[1].charAt(0));
                details.setLocation(row[2]);
                details.setTenureMonths(Integer.parseInt(row[3]));
                
                detailsList.add(details);
                
                // Now you can use the 'person' object as needed
                //System.out.println(details.getCust_id() + ", " + details.getGender() + ", " + details.getLocation()+","+details.getTenureMonths());
                
            }
            long startTime, endTime;
         // Goal: Calculate the average tenure months for the customers by gender using streams
            // Sequential execution
            startTime = System.currentTimeMillis();

            Map<Character, Double> averageTenureByGenderSequential = detailsList.stream()
                    .collect(Collectors.groupingBy(Details::getGender,
                            Collectors.averagingInt(Details::getTenureMonths)));

            endTime = System.currentTimeMillis();
            System.out.println("Sequential execution time is " + (endTime - startTime) + " milliseconds");

            // Parallel execution
            startTime = System.currentTimeMillis();

            Map<Character, Double> averageTenureByGenderParallel = detailsList.parallelStream()
                    .collect(Collectors.groupingBy(Details::getGender,
                            Collectors.averagingInt(Details::getTenureMonths)));

            endTime = System.currentTimeMillis();
            System.out.println("Parallel execution time is " + (endTime - startTime) + " milliseconds");

            // Display the results
            averageTenureByGenderSequential.forEach((gender, averageTenure) ->
                    System.out.println("Average tenure for " + gender + ": " + averageTenure));

//            averageTenureByGenderParallel.forEach((gender, averageTenure) ->
//                    System.out.println("Parallel: Average tenure for " + gender + ": " + averageTenure));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
//        Set<Details> set = new HashSet<Details>();
//        detailsList.add(details);
//        //Stream<Details> detailsset = set.stream();
//     // Goal: Calculate the average tenure months for customers by gender
//        Map<Character, Double> averageTenureByGender = detailsset.stream()
//                .collect(Collectors.groupingBy(Details::getGender,
//                        Collectors.averagingInt(Details::getTenureMonths)));

    }
}
