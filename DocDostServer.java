import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class DocDostServer {

    // 🛑 DATABASE CREDENTIALS
    static final String DB_URL = "jdbc:mysql://localhost:3306/docdost1_db";
    static final String USER = "root";
    static final String PASS = ""; // APNA PASSWORD YAHAN DALNA MAT BHOOLNA

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/appointment", new AppointmentHandler());
        server.createContext("/api/getAppointments", new AdminHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("🚀 Core Java + MySQL Server is running at: http://localhost:8080");
    }

    static class AppointmentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                InputStream is = exchange.getRequestBody();
                String requestBody = new String(is.readAllBytes());

                String pName = extract(requestBody, "patientName");
                String pEmail = extract(requestBody, "patientEmail");
                String pPhone = extract(requestBody, "patientPhone");
                String loc = extract(requestBody, "location"); // Nayi entry
                String dName = extract(requestBody, "doctorName");
                String date = extract(requestBody, "date");
                String time = extract(requestBody, "time");
                String problem = extract(requestBody, "problem");

                boolean isSaved = saveToDatabase(pName, pEmail, pPhone, loc, dName, date, time, problem);

                String jsonResponse = isSaved ? "{\"status\": \"success\", \"message\": \"✅ Appointment Confirmed Successfully! We will contact you soon.\"}" : "{\"status\": \"error\", \"message\": \"Database Error!\"}";

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(jsonResponse.getBytes());
                os.close();
            }
        }
    }

    static class AdminHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1); return;
            }
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                String jsonResponse = fetchFromDatabase();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                byte[] responseBytes = jsonResponse.getBytes("UTF-8");
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
            }
        }
    }

    private static String extract(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return "";
        startIndex += searchKey.length();
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }

    // Naya Save function with Location
    private static boolean saveToDatabase(String name, String email, String phone, String location, String doctor, String date, String time, String problem) {
        String query = "INSERT INTO appointments (patient_name, patient_email, patient_phone, location, doctor_name, appointment_date, appointment_time, problem) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name); pstmt.setString(2, email); pstmt.setString(3, phone);
            pstmt.setString(4, location); pstmt.setString(5, doctor); pstmt.setString(6, date); 
            pstmt.setString(7, time); pstmt.setString(8, problem);
            pstmt.executeUpdate();
            pstmt.close(); conn.close();
            return true;
        } catch (Exception e) { 
            System.out.println("❌ DB Error: " + e.getMessage());
            return false; 
        }
    }

    // Fetch Function mein Location add kar diya
    private static String fetchFromDatabase() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM appointments ORDER BY id DESC"); 

            boolean first = true;
            while (rs.next()) {
                if (!first) jsonBuilder.append(",");
                jsonBuilder.append("{");
                jsonBuilder.append("\"id\":").append(rs.getInt("id")).append(",");
                jsonBuilder.append("\"patientName\":\"").append(rs.getString("patient_name")).append("\",");
                jsonBuilder.append("\"phone\":\"").append(rs.getString("patient_phone")).append("\",");
                
                // Fetching Location safely
                String loc = rs.getString("location");
                if(loc == null) loc = "N/A";
                jsonBuilder.append("\"location\":\"").append(loc).append("\",");
                
                jsonBuilder.append("\"doctorName\":\"").append(rs.getString("doctor_name")).append("\",");
                jsonBuilder.append("\"date\":\"").append(rs.getString("appointment_date")).append("\",");
                jsonBuilder.append("\"time\":\"").append(rs.getString("appointment_time")).append("\",");
                jsonBuilder.append("\"problem\":\"").append(rs.getString("problem").replace("\"", "\\\"").replace("\n", " ")).append("\"");
                jsonBuilder.append("}");
                first = false;
            }
            rs.close(); stmt.close(); conn.close();
        } catch (Exception e) {
            System.out.println("❌ Fetch Error: " + e.getMessage());
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}