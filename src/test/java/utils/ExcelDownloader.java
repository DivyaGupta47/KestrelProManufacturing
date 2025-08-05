package utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExcelDownloader {

    public static File downloadExcelFromAPI(String apiUrl) {
        try {
            String token = Config.getSessionToken();
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("Session token not found. Please login first.");
            }

            File folder = new File(System.getProperty("user.dir") + "/reportassertion");
            if (!folder.exists()) {
                folder.mkdirs();
            } else {
                // Clean old files before downloading
                for (File f : folder.listFiles()) {
                    if (f.isFile() && (f.getName().endsWith(".xlsx") || f.getName().endsWith(".csv"))) {
                        f.delete();
                    }
                }
            }

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Set headers
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Accept", "*/*");

            int responseCode = connection.getResponseCode();
            String contentType = connection.getContentType();
            System.out.println("DEBUG: API Response Code = " + responseCode);
            System.out.println("DEBUG: API Content-Type = " + contentType);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Detect file type
                String fileExtension = ".xlsx"; // default
                if (contentType != null && contentType.toLowerCase().contains("csv")) {
                    fileExtension = ".csv";
                }

                String fileName = "Report_" + System.currentTimeMillis() + fileExtension;
                File file = new File(folder, fileName);

                try (InputStream inputStream = connection.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(file)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }

                System.out.println("File downloaded to: " + file.getAbsolutePath());
                return file;
            } else {
                throw new RuntimeException("Failed with HTTP code: " + responseCode);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
