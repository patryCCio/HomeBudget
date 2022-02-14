package pl.patrickit.utils.conveters;

import pl.patrickit.database.models.Planning;
import pl.patrickit.modelFx.PlanningFx;

public class PlanningConveter {
    public static Planning convertToPlanning(PlanningFx planningFx){
        Planning planning = new Planning();
        planning.setId(planningFx.getId());
        planning.setCategory(planningFx.getCategory());
        planning.setData(planningFx.getData());
        planning.setDescription(planningFx.getDescription());
        planning.setPrice(Double.parseDouble(String.valueOf(planningFx.getPrice())));
        return planning;
    }

    public static PlanningFx convertToPlanningFx(Planning planning){
        PlanningFx planningFx = new PlanningFx();
        planningFx.setId(planning.getId());
        planningFx.setCategory(planning.getCategory());
        planningFx.setData(planning.getData());
        planningFx.setDescription(planning.getDescription());
        planningFx.setPrice(String.valueOf(planning.getPrice()));
        return planningFx;
    }
}
