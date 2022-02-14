package pl.patrickit.utils.constants;

import javafx.scene.control.Label;
import pl.patrickit.controllers.PermanentController;
import pl.patrickit.database.dao.BalanceDao;
import pl.patrickit.database.dao.MoneyAsideDao;
import pl.patrickit.database.dao.MoneyDao;
import pl.patrickit.database.dao.SalaryDao;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.Balance;
import pl.patrickit.database.models.Money;
import pl.patrickit.database.models.MoneyAside;
import pl.patrickit.database.models.Salary;
import pl.patrickit.utils.DialogUtils;
import pl.patrickit.utils.FxmlUtils;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConstantMethod {

    public static Money showInfoBudget(Money actualMoney){
        MoneyDao moneyDao = new MoneyDao(DbManager.getConnectionSource());

        try {
            actualMoney = moneyDao.findById(Money.class, 1);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        DbManager.closeConnectionSource();
        return actualMoney;
    }

    public static MoneyAside showInfoMoneyAside(MoneyAside moneyAside) {
        MoneyAsideDao moneyAsideDao = new MoneyAsideDao(DbManager.getConnectionSource());

        try {
            moneyAside = moneyAsideDao.findById(MoneyAside.class, 1);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        DbManager.closeConnectionSource();
        return moneyAside;
    }

    public static Salary showInfoSalary(Salary actualSalary) {
        SalaryDao salaryDao = new SalaryDao(DbManager.getConnectionSource());

        try {
            actualSalary = salaryDao.findById(Salary.class, 1);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        DbManager.closeConnectionSource();
        return actualSalary;
    }

    public static Balance showInfoBalance(Balance balance) {
        Salary salary = new Salary();
        salary = showInfoSalary(salary);
        BalanceDao balanceDao = new BalanceDao(DbManager.getConnectionSource());
        try {
            balance = balanceDao.findById(Balance.class, 1);
            double actualBalance = salary.getSalary();
            actualBalance = actualBalance - PermanentController.BALANCE;
            balance.setBalance(actualBalance);
            balance.setId(balance.getId());
        } catch (ApplicationException e) {
            e.printStackTrace();
        }

        DbManager.closeConnectionSource();
        return balance;
    }

    public static void setLabel(double moneyAdd, Label label){
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        DecimalFormat plnFormat = new DecimalFormat("###,###,###.00");
        label.setText(plnFormat.format(moneyAdd) + " " + bundle.getString("pln"));
    }

    public static void setLabelText(String text, Label label){
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        label.setText(text);
    }

    public static double getMoney(String helper){
        boolean isGood = checkNumber(helper);
        double price = 0;
        if(isGood){
            price = tryAndCatchValue(helper);
            if(price<0){
                DialogUtils.onlyPlusDialog();
                price = 0;
            }
        }
        return price;
    }

    public static String getData(){
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.YYYY"));
        return data;
    }

    public static boolean checkNumber(String helper){
        double price = 0;
        boolean isGoodNumber = true;
        try{
            price = Double.parseDouble(helper);
        }catch(NumberFormatException e){
            isGoodNumber = false;
            DialogUtils.wrongFormat();
        }
        return isGoodNumber;
    }

    public static double tryAndCatchValue(String helper){
        int x=helper.length()-1;
        boolean isDot=false;
        for(int y=0; y<=x; y++){
            if(helper.charAt(y)=='.')isDot=true;
        }

        if(!isDot){
            helper = helper + ".00";
        }else{
            x=0;
            String helper2 = "";
            while(helper.charAt(x)!='.'){
                x++;
            }
            if((x+2)==helper.length()-1){
                for(x=0; x<helper.length(); x++){
                    helper2 = helper2 + helper.charAt(x);
                }
            }else if((x+1)==helper.length()-1){
                for(x=0; x<helper.length(); x++){
                    helper2 = helper2 + helper.charAt(x);
                }
                helper2 = helper2 + "0";
            }else{
                int y=x;
                for(x=0; x<=y+2; x++){
                    helper2 = helper2 + helper.charAt(x);
                }
            }
            helper = helper2;
        }

        double money = Double.parseDouble(helper);
        return money;
    }

}

