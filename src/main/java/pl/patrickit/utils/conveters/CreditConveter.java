package pl.patrickit.utils.conveters;

import pl.patrickit.database.models.Credit;
import pl.patrickit.modelFx.CreditFx;

public class CreditConveter {
    public static Credit convertToCredit(CreditFx creditFx){
        Credit credit = new Credit();
        credit.setId(creditFx.getId());
        credit.setDataTo(creditFx.getDateTo());
        credit.setDataFrom(creditFx.getDateFrom());
        credit.setDescription(creditFx.getDescription());
        credit.setPrice(Double.parseDouble(String.valueOf(creditFx.getPrice())));
        return credit;
    }

    public static Credit convertToCreditMath(CreditFx creditFx, double price){
        Credit credit = new Credit();
        credit.setId(creditFx.getId());
        credit.setDataTo(creditFx.getDateTo());
        credit.setDataFrom(creditFx.getDateFrom());
        credit.setDescription(creditFx.getDescription());
        double cred = credit.getPrice();
        cred = cred - price;
        credit.setPrice(Double.parseDouble(String.valueOf(creditFx.getPrice())));
        return credit;
    }

    public static CreditFx convertToCreditFx(Credit credit){
        CreditFx creditFx = new CreditFx();
        creditFx.setId(credit.getId());
        creditFx.setDateFrom(credit.getDataFrom());
        creditFx.setDateTo(credit.getDataTo());
        creditFx.setDescription(credit.getDescription());
        creditFx.setPrice(String.valueOf(credit.getPrice()));
        return creditFx;
    }
}
