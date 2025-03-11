package com.codetech.task4;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class RecommendationSystem {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Prevent file locking by using BufferedReader
            File tempFile = new File("tempo.csv");
            copyFile("recommendation.csv", tempFile);

            // Load data from copied file
            DataModel model = new FileDataModel(tempFile);
            System.out.println("Data model successfully loaded!");

            // Use Item-Based Filtering (Better for sparse data)
            
            ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
            Recommender recommender = new GenericItemBasedRecommender(model, similarity);

            // Get user input
            System.out.print("Enter User ID for recommendations: ");
            int userId = scanner.nextInt();

            System.out.print("Enter the number of recommendations to generate: ");
            int numRecommendations = scanner.nextInt();

            // Generate recommendations
            List<RecommendedItem> recommendations = recommender.recommend(userId, numRecommendations);

            // Display recommendations
            System.out.println("\nRecommendations for User " + userId + ":");
            if (recommendations.isEmpty()) {
                System.out.println(" No recommendations found. Try adding more data.");
            } else {
                for (RecommendedItem item : recommendations) {
                    System.out.println("Product ID: " + item.getItemID() + " | Score: " + item.getValue());
                }
            }
            
            // Delete temporary file after processing
            tempFile.delete();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to copy file to prevent access conflicts
    private static void copyFile(String sourcePath, File destFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourcePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(destFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}

