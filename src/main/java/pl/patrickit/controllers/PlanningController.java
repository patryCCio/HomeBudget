package pl.patrickit.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import pl.patrickit.database.dao.PlanningDao;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.Money;
import pl.patrickit.database.models.MoneyAside;
import pl.patrickit.database.models.Planning;
import pl.patrickit.database.models.Salary;
import pl.patrickit.modelFx.CategoryFx;
import pl.patrickit.modelFx.CategoryModel;
import pl.patrickit.modelFx.PlanningFx;
import pl.patrickit.modelFx.PlanningModel;
import pl.patrickit.utils.DialogUtils;
import pl.patrickit.utils.FxmlUtils;
import pl.patrickit.utils.constants.ConstantMethod;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PlanningController {

    @FXML
    private Button addPlanButton;

    @FXML
    private Label budgetLabel;

    @FXML
    private DatePicker calendarDatePicker;

    @FXML
    private ComboBox<CategoryFx> categoryComboBox;

    @FXML
    private TableColumn<PlanningFx, String> categoryTableColumn;

    @FXML
    private TableColumn<PlanningFx, String> dateTableColumn;

    @FXML
    private TableColumn<PlanningFx, String> descriptionTableColumn;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private ComboBox<Object> infoComboBox;

    @FXML
    private Label moneyAsideLabel;

    @FXML
    private TableView<PlanningFx> planningTableView;

    @FXML
    private TableColumn<PlanningFx, String> priceTableColumn;

    @FXML
    private TextField valueTextField;

    @FXML
    private MenuItem payOffMenuItem;

    @FXML
    private MenuItem deleteMenuItem;

    @FXML
    private StackPane addStackPane;

    @FXML
    private ToggleGroup toggleGroupChoice;

    @FXML
    private Label salaryLabel;


    Money actualMoney = new Money();
    MoneyAside moneyAside = new MoneyAside();
    Salary actualSalary = new Salary();

    private PlanningModel planningModel;
    ResourceBundle bundle = FxmlUtils.getResourceBundle();

    private CategoryModel categoryModel;
    private ObservableList<Object> List;


    public void initialize(){
        this.planningModel = new PlanningModel();
        this.categoryModel = new CategoryModel();
        this.categoryModel.init();
        this.categoryComboBox.setItems(this.categoryModel.getCategoryList());
        try {
            this.planningModel.init();
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        initBindings();
        bindingsTableView();
        showInfo();
        this.descriptionTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.dateTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.categoryTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.priceTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        this.planningTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.planningModel.setPlanningFxObjectPropertyEdit(newValue);
        });
    }

    private void showInfo(){
        ArrayList<String> arrayList = new ArrayList<>();
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        arrayList.add(bundle.getString("budget"));
        arrayList.add(bundle.getString("money.aside"));
        List = FXCollections.observableArrayList(arrayList);
        infoComboBox.setItems(List);

        this.actualMoney = ConstantMethod.showInfoBudget(this.actualMoney);
        ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);
        this.actualSalary = ConstantMethod.showInfoSalary(this.actualSalary);
        ConstantMethod.setLabel(this.actualSalary.getSalary(), salaryLabel);
        this.moneyAside = ConstantMethod.showInfoMoneyAside(this.moneyAside);
        ConstantMethod.setLabel(this.moneyAside.getMoneyAside(), moneyAsideLabel);
    }

    private void bindingsTableView() {
        this.planningTableView.setItems(this.planningModel.getPlanningFxObservableList());
        this.descriptionTableColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        this.dateTableColumn.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        this.categoryTableColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        this.priceTableColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());

    }

    private void initBindings(){
        if(toggleGroupChoice.getSelectedToggle()==null){
            this.addStackPane.setVisible(false);
        }

        this.addPlanButton.disableProperty().bind(valueTextField.textProperty().isEmpty().or(descriptionTextField.textProperty().isEmpty().or(this.categoryModel.categoryProperty().isNull().or(this.calendarDatePicker.getEditor().textProperty().isEmpty()))));
    }

    public void onActionComboBox() {
        this.categoryModel.setCategory(this.categoryComboBox.getSelectionModel().getSelectedItem());
    }

    public void infoSplitMenuButton(){
        String info = infoComboBox.getValue().toString();
        this.infoComboBox.getEditor().textProperty().set(info);
    }

    public void payOff() {
        int choice = 0;
        if(infoComboBox.getEditor().getText().isEmpty()){
            DialogUtils.dialogEmpty();
        }else if(infoComboBox.getValue().equals(bundle.getString("budget"))){
            choice=1;
            this.planningModel.payOffPlan(choice);
        }else{
            this.planningModel.payOffPlan(choice);
        }

        this.actualMoney = ConstantMethod.showInfoBudget(this.actualMoney);
        this.moneyAside = ConstantMethod.showInfoMoneyAside(this.moneyAside);

        ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);
        ConstantMethod.setLabel(this.moneyAside.getMoneyAside(), moneyAsideLabel);
    }

    public void addPlan() {
        String helper = valueTextField.getText();
        double price = ConstantMethod.getMoney(helper);

        PlanningDao planningDao = new PlanningDao(DbManager.getConnectionSource());

        if(price!=0){
            Planning planning = new Planning();
                planning.setId(planning.getId());
                planning.setPrice(price);
                planning.setData(calendarDatePicker.getEditor().getText());
                planning.setDescription(descriptionTextField.getText());
                planning.setCategory(this.categoryModel.categoryProperty().getValue().getName());
                try {
                    planningDao.creatOrUpdate(planning);
                } catch (ApplicationException e) {
                    e.printStackTrace();
                }
                try {
                    this.planningModel.init();
                } catch (ApplicationException e) {
                    e.printStackTrace();
                }

                DbManager.closeConnectionSource();
        }

        descriptionTextField.clear();
        valueTextField.clear();
    }

    public void deletePlan() {
        this.planningModel.deletePlanningInDatabase();
    }

    public void onEditCommitDate(TableColumn.CellEditEvent<PlanningFx, String> planningFxStringCellEditEvent) {
        this.planningModel.getPlanningFxObjectPropertyEdit().setData(planningFxStringCellEditEvent.getNewValue());
        this.planningModel.savePlanningEditInDatabase();
    }

    public void onEditCommitCategory(TableColumn.CellEditEvent<PlanningFx, String> planningFxStringCellEditEvent) {
        this.planningModel.getPlanningFxObjectPropertyEdit().setCategory(planningFxStringCellEditEvent.getNewValue());
        this.planningModel.savePlanningEditInDatabase();
    }

    public void onEditCommitDescription(TableColumn.CellEditEvent<PlanningFx, String> planningFxStringCellEditEvent) {
        this.planningModel.getPlanningFxObjectPropertyEdit().setDescription(planningFxStringCellEditEvent.getNewValue());
        this.planningModel.savePlanningEditInDatabase();
    }

    public void onEditCommitPrice(TableColumn.CellEditEvent<PlanningFx, String> planningFxStringCellEditEvent) {
        this.planningModel.getPlanningFxObjectPropertyEdit().setPrice(planningFxStringCellEditEvent.getNewValue());
        this.planningModel.savePlanningEditInDatabase();
    }

    public void addStack() {
        this.addStackPane.setVisible(true);
        initBindings();
    }

    public void setToday() {
        this.calendarDatePicker.setValue(LocalDate.now());
    }
}
