package pl.patrickit.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ACTUAL_SALARY")
public class Salary implements BaseModel{

    public Salary() {
    }

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(columnName = "SALARY", defaultValue = "0")
    private double salary;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
