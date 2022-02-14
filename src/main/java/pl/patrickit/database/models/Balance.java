package pl.patrickit.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ACTUAL_BALANCE")
public class Balance implements BaseModel{
    public Balance() {
    }

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(columnName = "BALANCE", defaultValue = "0")
    private double balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
