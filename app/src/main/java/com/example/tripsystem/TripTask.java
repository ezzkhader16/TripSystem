package com.example.tripsystem;
public class TripTask {
    private String title;
    private String date;
    private double budget;
    private boolean isImportant;
    private String type;
    private boolean isPaid;
    private int cardImageResId;
    private int detailImageResId;

    public TripTask(String title, String date, double budget,
                    boolean isImportant, String type, boolean isPaid,
                    int cardImageResId, int detailImageResId) {
        this.title = title;
        this.date = date;
        this.budget = budget;
        this.isImportant = isImportant;
        this.type = type;
        this.isPaid = isPaid;
        this.cardImageResId = cardImageResId;
        this.detailImageResId = detailImageResId;
    }

    public TripTask(String title, String date, double budget,
                    boolean isImportant, String type, boolean isPaid) {
        this(title, date, budget, isImportant, type, isPaid, 0, 0);
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public boolean isImportant() { return isImportant; }
    public void setImportant(boolean important) { isImportant = important; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    public int getCardImageResId() { return cardImageResId; }
    public void setCardImageResId(int cardImageResId) { this.cardImageResId = cardImageResId; }

    public int getDetailImageResId() { return detailImageResId; }
    public void setDetailImageResId(int detailImageResId) { this.detailImageResId = detailImageResId; }
}