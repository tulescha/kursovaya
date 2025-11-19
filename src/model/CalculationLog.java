package model;

public class CalculationLog {
    private int id;
    private int userId;
    private String calcType;
    private String inputData;
    private String resultData;
    private String createdAt;

    public CalculationLog(int id, int userId, String calcType, String inputData, String resultData, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.calcType = calcType;
        this.inputData = inputData;
        this.resultData = resultData;
        this.createdAt = createdAt;
    }

    public CalculationLog(int userId, String calcType, String inputData, String resultData, String createdAt) {
        this.userId = userId;
        this.calcType = calcType;
        this.inputData = inputData;
        this.resultData = resultData;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getCalcType() { return calcType; }
    public String getInputData() { return inputData; }
    public String getResultData() { return resultData; }
    public String getCreatedAt() { return createdAt; }
}
