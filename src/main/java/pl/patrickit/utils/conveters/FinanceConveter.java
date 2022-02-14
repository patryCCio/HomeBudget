package pl.patrickit.utils.conveters;

import pl.patrickit.database.models.Finance;
import pl.patrickit.modelFx.FinanceFx;
import pl.patrickit.utils.constants.ConstantMethod;

public class FinanceConveter {

    public static Finance convertToFinance(FinanceFx financeFx){
        Finance finance = new Finance();
        finance.setId(financeFx.getId());
        finance.setData(financeFx.getData());
        finance.setCategory(financeFx.getCategory());
        finance.setDescription(financeFx.getDescription());
        finance.setPrice(Double.parseDouble(String.valueOf(financeFx.getPrice())));
        finance.setType(financeFx.getType());
        return finance;
    }

    public static FinanceFx convertToFinanceFx(Finance finance){
        FinanceFx financeFx = new FinanceFx();
        financeFx.setId(finance.getId());
        financeFx.setData(finance.getData());
        financeFx.setCategory(finance.getCategory());
        financeFx.setDescription(finance.getDescription());
        String helper = String.valueOf(finance.getPrice());
        financeFx.setPrice(String.valueOf(ConstantMethod.getMoney(helper)));
        financeFx.setType(finance.getType());
        return financeFx;
    }

}
