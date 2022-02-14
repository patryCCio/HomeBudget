package pl.patrickit.utils.conveters;

import pl.patrickit.controllers.MainController;
import pl.patrickit.controllers.PermanentController;
import pl.patrickit.database.dao.BalanceDao;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.Balance;
import pl.patrickit.database.models.Permanent;
import pl.patrickit.modelFx.PermanentFx;
import pl.patrickit.utils.FxmlUtils;
import pl.patrickit.utils.constants.ConstantMethod;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.util.ResourceBundle;

public class PermanentConveter {
    private ResourceBundle bundle = FxmlUtils.getResourceBundle();

    public static Permanent convertToPermanent(PermanentFx permanentFx){
        Permanent permanent = new Permanent();
        permanent.setId(permanentFx.getId());
        permanent.setFrequency(permanentFx.getFrequency());
        permanent.setDescription(permanentFx.getDescription());
        permanent.setPrice(Double.parseDouble(String.valueOf(permanentFx.getPrice())));
        return permanent;
    }

    public static PermanentFx convertToPermanentFxBalance(Permanent permanent, String option){
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        PermanentFx permanentFx = new PermanentFx();
        permanentFx.setId(permanent.getId());
        permanentFx.setFrequency(permanent.getFrequency());
        permanentFx.setDescription(permanent.getDescription());
        permanentFx.setPrice(String.valueOf(permanent.getPrice()));

        if(permanent.getFrequency().equals(option)&&option.equals(bundle.getString("day"))){
            int month = MainController.ACTUAL_MONTH;
            if((month==0)||(month==2)||(month==4)||(month==6)||(month==7)||(month==9)||(month==11)){
                double price = permanent.getPrice();
                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                price = price * 31;

                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                PermanentController.BALANCE = PermanentController.BALANCE + price;
                PermanentController.BALANCE_PER_MONTH = PermanentController.BALANCE_PER_MONTH + price;
                PermanentController.BALANCE_PER_ONE_MONTH = PermanentController.BALANCE_PER_DAY + price;
            }else if(month==1){
                double price = permanent.getPrice();
                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                price = price * 28;

                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                PermanentController.BALANCE = PermanentController.BALANCE + price;
                PermanentController.BALANCE_PER_MONTH = PermanentController.BALANCE_PER_MONTH + price;
                PermanentController.BALANCE_PER_ONE_MONTH = PermanentController.BALANCE_PER_DAY + price;
            }else{
                double price = permanent.getPrice();
                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                price = price * 30;

                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                PermanentController.BALANCE = PermanentController.BALANCE + price;
                PermanentController.BALANCE_PER_MONTH = PermanentController.BALANCE_PER_MONTH + price;
                PermanentController.BALANCE_PER_ONE_MONTH = PermanentController.BALANCE_PER_DAY + price;
            }
        }
        else if(permanent.getFrequency().equals(option)&&option.equals(bundle.getString("week"))){
            double price = permanent.getPrice();
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            price = price * 4;
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            PermanentController.BALANCE = PermanentController.BALANCE + price;
            PermanentController.BALANCE_PER_MONTH = PermanentController.BALANCE_PER_MONTH + price;
            PermanentController.BALANCE_PER_WEEK = PermanentController.BALANCE_PER_WEEK + price;
        }else if(permanent.getFrequency().equals(option)&&option.equals(bundle.getString("month"))){
            double price = permanent.getPrice();
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            PermanentController.BALANCE = PermanentController.BALANCE + price;
            PermanentController.BALANCE_PER_MONTH = PermanentController.BALANCE_PER_MONTH + price;
            PermanentController.BALANCE_PER_ONE_MONTH = PermanentController.BALANCE_PER_ONE_MONTH + price;
        }else if(permanent.getFrequency().equals(option)&&option.equals(bundle.getString("quarter"))){
            double price = permanent.getPrice();
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            price = price / 3;
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            PermanentController.BALANCE = PermanentController.BALANCE + price;
            PermanentController.BALANCE_PER_QUARTER = PermanentController.BALANCE_PER_QUARTER + price;
        }else if(permanent.getFrequency().equals(option)&&option.equals(bundle.getString("six.months"))) {
            double price = permanent.getPrice();
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            price = price / 6;
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            PermanentController.BALANCE = PermanentController.BALANCE + price;
            PermanentController.BALANCE_PER_SIX_MONTHS = PermanentController.BALANCE_PER_SIX_MONTHS + price;
        }else if(permanent.getFrequency().equals(option)&&option.equals(bundle.getString("year"))){
            double price = permanent.getPrice();
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            price = price / 12;
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            PermanentController.BALANCE = PermanentController.BALANCE + price;
            PermanentController.BALANCE_PER_YEAR = PermanentController.BALANCE_PER_YEAR + price;
        }

        return permanentFx;
    }


    public static PermanentFx convertToPermanentFx(Permanent permanent){
        PermanentFx permanentFx = new PermanentFx();
        permanentFx.setId(permanent.getId());
        permanentFx.setFrequency(permanent.getFrequency());
        permanentFx.setDescription(permanent.getDescription());
        permanentFx.setPrice(String.valueOf(permanent.getPrice()));
        return permanentFx;
    }

    public static PermanentFx convertToPermanentFxBalanceAll(Permanent permanent, String option) {
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        PermanentFx permanentFx = new PermanentFx();
        permanentFx.setId(permanent.getId());
        permanentFx.setFrequency(permanent.getFrequency());
        permanentFx.setDescription(permanent.getDescription());
        permanentFx.setPrice(String.valueOf(permanent.getPrice()));

        if(permanent.getFrequency().equals(bundle.getString("day"))){
            int month = MainController.ACTUAL_MONTH;
            if((month==0)||(month==2)||(month==4)||(month==6)||(month==7)||(month==9)||(month==11)){
                double price = permanent.getPrice();
                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                price = price * 31;

                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                PermanentController.BALANCE = PermanentController.BALANCE + price;
                PermanentController.BALANCE_PER_MONTH = PermanentController.BALANCE_PER_MONTH + price;
                PermanentController.BALANCE_PER_DAY = PermanentController.BALANCE_PER_DAY + price;
            }else if(month==1){
                double price = permanent.getPrice();
                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                price = price * 28;

                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                PermanentController.BALANCE = PermanentController.BALANCE + price;
                PermanentController.BALANCE_PER_MONTH = PermanentController.BALANCE_PER_MONTH + price;
                PermanentController.BALANCE_PER_DAY = PermanentController.BALANCE_PER_DAY + price;
            }else{
                double price = permanent.getPrice();
                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                price = price * 30;

                price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
                PermanentController.BALANCE = PermanentController.BALANCE + price;
                PermanentController.BALANCE_PER_MONTH = PermanentController.BALANCE_PER_MONTH + price;
                PermanentController.BALANCE_PER_DAY = PermanentController.BALANCE_PER_DAY + price;
            }
        }
        else if(permanent.getFrequency().equals(bundle.getString("week"))){
            double price = permanent.getPrice();
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            price = price * 4;
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            PermanentController.BALANCE = PermanentController.BALANCE + price;
            PermanentController.BALANCE_PER_MONTH = PermanentController.BALANCE_PER_MONTH + price;
            PermanentController.BALANCE_PER_WEEK = PermanentController.BALANCE_PER_WEEK + price;
        }else if(permanent.getFrequency().equals(bundle.getString("month"))){
            double price = permanent.getPrice();
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            PermanentController.BALANCE = PermanentController.BALANCE + price;
            PermanentController.BALANCE_PER_MONTH = PermanentController.BALANCE_PER_MONTH + price;
            PermanentController.BALANCE_PER_ONE_MONTH = PermanentController.BALANCE_PER_ONE_MONTH + price;
        }else if(permanent.getFrequency().equals(bundle.getString("quarter"))){
            double price = permanent.getPrice();
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            price = price / 3;
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            PermanentController.BALANCE = PermanentController.BALANCE + price;
            PermanentController.BALANCE_PER_QUARTER = PermanentController.BALANCE_PER_QUARTER + price;
        }else if(permanent.getFrequency().equals(bundle.getString("six.months"))) {
            double price = permanent.getPrice();
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            price = price / 6;
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            PermanentController.BALANCE = PermanentController.BALANCE + price;
            PermanentController.BALANCE_PER_SIX_MONTHS = PermanentController.BALANCE_PER_SIX_MONTHS + price;
        }else if(permanent.getFrequency().equals(bundle.getString("year"))){
            double price = permanent.getPrice();
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            price = price / 12;
            price = ConstantMethod.tryAndCatchValue(String.valueOf(price));
            PermanentController.BALANCE = PermanentController.BALANCE + price;
            PermanentController.BALANCE_PER_YEAR = PermanentController.BALANCE_PER_YEAR + price;
        }
        return permanentFx;
    }
}
