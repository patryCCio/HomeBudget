package pl.patrickit.database.dbutils;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import pl.patrickit.database.dao.BalanceDao;
import pl.patrickit.database.dao.MoneyAsideDao;
import pl.patrickit.database.dao.MoneyDao;
import pl.patrickit.database.dao.SalaryDao;
import pl.patrickit.database.models.*;
import pl.patrickit.utils.DialogUtils;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.io.IOException;
import java.sql.SQLException;

public class DbManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbManager.class);

    private static final String JDBC_DRIVER = "jdbc:sqlite:budgetDb";

    private static ConnectionSource connectionSource;

    public static void initDatabase(){
        createConnectionSource();
        //dropTable(); //zakomentuj, ?eby nie kasowa? za ka?ym razem tabel w bazie
        createTable();
        closeConnectionSource();
    }

    private static void createConnectionSource(){
        try {
            connectionSource = new JdbcConnectionSource(JDBC_DRIVER);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public static ConnectionSource getConnectionSource(){
        if(connectionSource == null){
            createConnectionSource();
        }
        return connectionSource;
    }

    public static void closeConnectionSource(){
        if(connectionSource!=null){
            try {
                connectionSource.close();
            } catch (IOException e) {
                LOGGER.warn(e.getMessage());
            }
        }
    }

    private static void moneyAdd(){
        MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());
        try {
                Money getMoney = new Money();
                getMoney = moneyDao.findById(Money.class, 1);
                if(getMoney==null){
                    Money money = new Money();
                    money.setId(1);
                    money.setMoney(0);
                    try {
                        moneyDao.creatOrUpdate(money);
                    } catch (ApplicationException applicationException) {
                        applicationException.printStackTrace();
                    }
                }
            } catch (ApplicationException e) {
                DialogUtils.errorDialog("MONEYADD");
            }
    }

    private static void moneyAsideAdd(){
        MoneyAsideDao moneyAsideDao = new MoneyAsideDao(DbManager.getConnectionSource());
        try {
                MoneyAside getMoneyAside = new MoneyAside();
                getMoneyAside = moneyAsideDao.findById(MoneyAside.class, 1);
                if(getMoneyAside==null){
                    MoneyAside moneyAside = new MoneyAside();
                    moneyAside.setId(1);
                    moneyAside.setMoneyAside(0);
                    try {
                        moneyAsideDao.creatOrUpdate(moneyAside);
                    } catch (ApplicationException applicationException) {
                        applicationException.printStackTrace();
                    }
                }
            } catch (ApplicationException e) {
                DialogUtils.errorDialog("MONEYASIDE");
            }
    }

    private static void salaryAdd(){
        SalaryDao salaryDao = new SalaryDao(DbManager.getConnectionSource());
        try {
                Salary salary = new Salary();
                salary = salaryDao.findById(Salary.class, 1);
                if(salary==null){
                    Salary setSalary = new Salary();
                    setSalary.setId(1);
                    setSalary.setSalary(0);
                    try {
                    salaryDao.creatOrUpdate(setSalary);
                    } catch (ApplicationException applicationException) {
                    applicationException.printStackTrace();
                    }
                }
            } catch (ApplicationException e) {
                DialogUtils.errorDialog("SALARYADD");
            }
    }

    private static void balanceAdd(){
        BalanceDao balanceDao = new BalanceDao(DbManager.getConnectionSource());
        try {
                Balance balance = new Balance();
                balance = balanceDao.findById(Balance.class, 1);
                if(balance==null){
                    Balance setBalance = new Balance();
                    setBalance.setId(1);
                    setBalance.setBalance(0);
                    try {
                    balanceDao.creatOrUpdate(setBalance);
                    } catch (ApplicationException applicationException) {
                    applicationException.printStackTrace();
                    }
                }
            } catch (ApplicationException e) {
                DialogUtils.errorDialog("BALANCEADD");
            }
    }

    private static void createTable(){
        try {
            TableUtils.createTableIfNotExists(connectionSource, Finance.class);
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
            TableUtils.createTableIfNotExists(connectionSource, MoneyAside.class);
            TableUtils.createTableIfNotExists(connectionSource, Permanent.class);
            TableUtils.createTableIfNotExists(connectionSource, Credit.class);
            TableUtils.createTableIfNotExists(connectionSource, Planning.class);
            TableUtils.createTableIfNotExists(connectionSource, Money.class);
            TableUtils.createTableIfNotExists(connectionSource, MoneyAside.class);
            TableUtils.createTableIfNotExists(connectionSource, Salary.class);
            TableUtils.createTableIfNotExists(connectionSource, Balance.class);
            moneyAdd();
            moneyAsideAdd();
            salaryAdd();
            balanceAdd();


        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }

    }

    private  static  void  dropTable(){
        try {
            TableUtils.dropTable(connectionSource, Finance.class, true);
            TableUtils.dropTable(connectionSource, Permanent.class, true);
            TableUtils.dropTable(connectionSource, Credit.class, true);
            TableUtils.dropTable(connectionSource, Planning.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, Money.class, true);
            TableUtils.dropTable(connectionSource, Balance.class, true);
            TableUtils.dropTable(connectionSource, Salary.class, true);
            TableUtils.dropTable(connectionSource, MoneyAside.class, true);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }

}
