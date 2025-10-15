package model;


import java.time.LocalDateTime;


public class Calculation {
    private int id;
    private String name;
    private String inputData; // e.g., JSON/CSV string or parameters
    private String resultData;
    private LocalDateTime createdAt;


    public Calculation() {
        createdAt = LocalDateTime.now();
    }


// getters/setters omitted for brevity


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getInputData() { return inputData; }
    public void setInputData(String inputData) { this.inputData = inputData; }
    public String getResultData() { return resultData; }
    public void setResultData(String resultData) { this.resultData = resultData; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}