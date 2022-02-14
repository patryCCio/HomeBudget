package pl.patrickit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import pl.patrickit.database.dao.*;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.database.models.*;
import pl.patrickit.modelFx.PermanentFx;
import pl.patrickit.modelFx.PermanentModel;
import pl.patrickit.utils.DialogUtils;
import pl.patrickit.utils.FxmlUtils;
import pl.patrickit.utils.constants.ConstantMethod;
import pl.patrickit.utils.conveters.PermanentConveter;
import pl.patrickit.utils.exceptions.ApplicationException;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class PermanentController {

    @FXML
    private Button addPermanentButton;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField valueTextField;
    @FXML
    private Label salaryLabel;
    @FXML
    private Label budgetLabel;
    @FXML
    private Label moneyAsideLabel;
    @FXML
    private TableView<PermanentFx> permanentTableView;
    @FXML
    private TableColumn<PermanentFx, String> frequencyTableColumn;
    @FXML
    private TableColumn<PermanentFx, String> priceTableColumn;
    @FXML
    private TableColumn<PermanentFx, String> descriptionTableColumn;
    @FXML
    private MenuItem deleteMenuItem;
    @FXML
    private ComboBox<String> frequencyComboBox;
    @FXML
    private ComboBox<String> searchComboBox;
    @FXML
    private StackPane addStackPane;
    @FXML
    private StackPane infoStackPane;
    @FXML
    private StackPane tabStackPane;
    @FXML
    private ToggleGroup toggleGroupChoice;
    @FXML
    private VBox tabVbox;
    @FXML
    private Label averagePerMonthLabel;
    @FXML
    private Label balance2Label;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label monthlyExpensesLabel;
    @FXML
    private Label frequencyLabel;
    @FXML
    private Label frequencyValueLabel;

    private PermanentModel permanentModel;

    ResourceBundle bundle = FxmlUtils.getResourceBundle();

    public static double BALANCE=0;
    public static double BALANCE_PER_MONTH=0;
    public static double BALANCE_PER_DAY=0;
    public static double BALANCE_PER_WEEK=0;
    public static double BALANCE_PER_ONE_MONTH=0;
    public static double BALANCE_PER_QUARTER=0;
    public static double BALANCE_PER_SIX_MONTHS=0;
    public static double BALANCE_PER_YEAR=0;

    private Money actualMoney = new Money();
    private Salary actualSalary = new Salary();
    private MoneyAside moneyAside = new MoneyAside();
    private Balance actualBalance = new Balance();
    MainController mainController = new MainController();
    private ObservableList<String> List;

    public void initialize(){
        showInfo();
        searchComboBox.setValue(this.bundle.getString("all"));
        this.permanentModel = new PermanentModel();
        this.permanentModel.getBalanceAll(this.bundle.getString("all"));
        setActualBalance();
        createComboBox();
        createComboBoxSearch();
        initBindings();
        bindingsTableView();
        this.descriptionTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.frequencyTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.priceTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.permanentTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.permanentModel.setPermanentFxObjectPropertyEdit(newValue);
        });
    }

    private void createComboBox() {
        ArrayList<String> arrayList = new ArrayList<>();
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        arrayList.add(bundle.getString("day"));
        arrayList.add(bundle.getString("week"));
        arrayList.add(bundle.getString("month"));
        arrayList.add(bundle.getString("quarter"));
        arrayList.add(bundle.getString("six.months"));
        arrayList.add(bundle.getString("year"));
        List = FXCollections.observableArrayList(arrayList);
        frequencyComboBox.setItems(List);
    }

    private void createComboBoxSearch(){
        ArrayList<String> arrayList = new ArrayList<>();
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        arrayList.add(bundle.getString("day"));
        arrayList.add(bundle.getString("week"));
        arrayList.add(bundle.getString("month"));
        arrayList.add(bundle.getString("quarter"));
        arrayList.add(bundle.getString("six.months"));
        arrayList.add(bundle.getString("year"));
        arrayList.add(bundle.getString("all"));
        List = FXCollections.observableArrayList(arrayList);
        searchComboBox.setItems(List);
    }

    @FXML
    void onActionComboBox(){
        this.frequencyComboBox.getSelectionModel().getSelectedItem();
        String helper = this.frequencyComboBox.getValue();
        this.frequencyComboBox.getEditor().textProperty().set(helper);
    }

    @FXML
    void onActionComboBoxSearch(){
        String option = searchComboBox.getValue();

        if(option.equals(bundle.getString("all"))){
            this.permanentModel.getBalanceAll(option);
        }else{
            this.permanentModel.getBalance(option);
        }
        setActualBalance();
    }

    private void checkHighestValue(){
        this.permanentModel.getBalanceAllInfo(bundle.getString("all"));
        System.out.println(BALANCE_PER_DAY);
        System.out.println(BALANCE_PER_WEEK);
        System.out.println(BALANCE_PER_ONE_MONTH);
        System.out.println(BALANCE_PER_QUARTER);
        System.out.println(BALANCE_PER_SIX_MONTHS);
        System.out.println(BALANCE_PER_YEAR);
        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(BALANCE_PER_DAY);
        arrayList.add(BALANCE_PER_WEEK);
        arrayList.add(BALANCE_PER_ONE_MONTH);
        arrayList.add(BALANCE_PER_QUARTER);
        arrayList.add(BALANCE_PER_SIX_MONTHS);
        arrayList.add(BALANCE_PER_YEAR);

        double helper = 0;
        int y=0;

        for(int x=0; x<arrayList.size(); x++){
            if(helper< arrayList.get(x)){
                helper = arrayList.get(x);
                y = x;
            }
        }


        if(y==0){
            ConstantMethod.setLabelText(bundle.getString("day"), frequencyLabel);
            ConstantMethod.setLabel(helper, frequencyValueLabel);
        }else if(y==1){
            ConstantMethod.setLabelText(bundle.getString("week"), frequencyLabel);
            ConstantMethod.setLabel(helper, frequencyValueLabel);
        }else if(y==2){
            ConstantMethod.setLabelText(bundle.getString("month"), frequencyLabel);
            ConstantMethod.setLabel(helper, frequencyValueLabel);
        }else if(y==3){
            ConstantMethod.setLabelText(bundle.getString("quarter"), frequencyLabel);
            ConstantMethod.setLabel(helper, frequencyValueLabel);
        }else if(y==4){
            ConstantMethod.setLabelText(bundle.getString("six.months"), frequencyLabel);
            ConstantMethod.setLabel(helper, frequencyValueLabel);
        }else if(y==5){
            ConstantMethod.setLabelText(bundle.getString("year"), frequencyLabel);
            ConstantMethod.setLabel(helper, frequencyValueLabel);
        }

    }

    private void initBindings(){
        if(toggleGroupChoice.getSelectedToggle()==null){
            this.addStackPane.setVisible(false);
            this.infoStackPane.setVisible(false);
            this.tabStackPane.setVisible(false);
            this.tabVbox.setVisible(false);
        }
        this.addPermanentButton.disableProperty().bind(this.descriptionTextField.textProperty().isEmpty().or(this.valueTextField.textProperty().isEmpty().or(this.frequencyComboBox.getEditor().textProperty().isEmpty())));
    }

    public void addStack(){
        this.addStackPane.setVisible(true);
        this.infoStackPane.setVisible(false);
        this.tabStackPane.setVisible(false);
        this.tabVbox.setVisible(true);
        initBindings();
    }

    private void bindingsTableView() {
        this.permanentTableView.setItems(this.permanentModel.getPermanentFxObservableList());
        this.descriptionTableColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        this.frequencyTableColumn.setCellValueFactory(cellData -> cellData.getValue().frequencyProperty());
        this.priceTableColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
    }


    public void informationStack(){
        this.addStackPane.setVisible(false);
        this.infoStackPane.setVisible(true);
        this.tabStackPane.setVisible(false);
        this.tabVbox.setVisible(false);
        initBindings();
        ConstantMethod.setLabel(BALANCE, averagePerMonthLabel);
        ConstantMethod.setLabel((this.actualSalary.getSalary()-BALANCE), balance2Label);
    }

    public void tabStack(){
        this.addStackPane.setVisible(false);
        this.infoStackPane.setVisible(false);
        this.tabStackPane.setVisible(true);
        this.tabVbox.setVisible(true);
        initBindings();
    }

    @FXML
    void deleteBill() {

        this.permanentModel.deletePermanentInDatabase(this.searchComboBox.getValue());

        String option = searchComboBox.getValue();

        if(option.equals(bundle.getString("all"))){
            this.permanentModel.getBalanceAll(option);
        }else{
            this.permanentModel.getBalance(option);
        }

        setActualBalance();
    }

    public void onEditCommitDescription(TableColumn.CellEditEvent<PermanentFx, String> permanentFxStringCellEditEvent) {
        this.permanentModel.getPermanentFxObjectPropertyEdit().setDescription(permanentFxStringCellEditEvent.getNewValue());
        this.permanentModel.savePermanentEditInDatabase(this.searchComboBox.getValue());
    }

    private void showInfo(){
        this.actualMoney = ConstantMethod.showInfoBudget(actualMoney);
        ConstantMethod.setLabel(this.actualMoney.getMoney(), budgetLabel);
        this.moneyAside = ConstantMethod.showInfoMoneyAside(moneyAside);
        ConstantMethod.setLabel(this.moneyAside.getMoneyAside(), moneyAsideLabel);
        this.actualSalary = ConstantMethod.showInfoSalary(actualSalary);
        ConstantMethod.setLabel(this.actualSalary.getSalary(), salaryLabel);
    }

    private void setActualBalance(){
        ConstantMethod.setLabel((this.actualSalary.getSalary()-BALANCE), balance2Label);
        ConstantMethod.setLabel(BALANCE, averagePerMonthLabel);
        ConstantMethod.setLabel(this.actualSalary.getSalary()-BALANCE_PER_MONTH, balanceLabel);
        ConstantMethod.setLabel(BALANCE_PER_MONTH, monthlyExpensesLabel);

        checkHighestValue();

    }

    @FXML
    void addPermanent() {
        String helper = valueTextField.getText();
        double price = ConstantMethod.getMoney(helper);
        if(price!=0){
            PermanentDao permanentDao = new PermanentDao(DbManager.getConnectionSource());
            Permanent permanent = new Permanent();
            permanent.setDescription(descriptionTextField.getText());
            permanent.setFrequency(frequencyComboBox.getValue());
            permanent.setPrice(price);
            permanent.setId(permanent.getId());
            try{
                permanentDao.creatOrUpdate(permanent);
            }catch (ApplicationException e){
                e.printStackTrace();
            }

            DbManager.closeConnectionSource();
            descriptionTextField.clear();
            valueTextField.clear();

            String option = searchComboBox.getValue();
            if(option.equals(bundle.getString("all"))){
                this.permanentModel.getBalanceAll(option);
            }else{
                this.permanentModel.getBalance(option);
            }

            bindingsTableView();
            ConstantMethod.setLabel((this.actualSalary.getSalary()-BALANCE), balance2Label);
            ConstantMethod.setLabel(BALANCE, averagePerMonthLabel);
            setActualBalance();

        }


        /*

        if((balance.getBalance()-price)<0){
            DialogUtils.dialogErrorMoneyAside();
        }else{
            if(price<0){
            DialogUtils.onlyPlusDialog();
        }else{
            if((this.actualBalance.getBalance()-price)<0){
                DialogUtils.dialogErrorBudget();
            }else{
                PermanentDao permanentDao = new PermanentDao(DbManager.getConnectionSource());
                Permanent permanent = new Permanent();
                permanent.setDescription(descriptionTextField.getText());
                permanent.setFrequency(frequencyComboBox.getValue());
                permanent.setPrice(price);
                permanent.setId(permanent.getId());
                try {
                    permanentDao.creatOrUpdate(permanent);
                } catch (ApplicationException e) {
                    e.printStackTrace();
                }
            }
        }
        }


        DbManager.closeConnectionSource();
        descriptionTextField.clear();
        valueTextField.clear();
        try {
            String option = this.searchComboBox.getValue();
            this.permanentModel.init(option);
            double helperBalance = mainController.tryAndCatchValue(String.valueOf(BALANCE));
            ConstantMethod.setLabel(helperBalance, totalBillsLabel);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        this.actualBalance = ConstantMethod.showInfoBalance(actualBalance);
        ConstantMethod.setLabel(this.actualBalance.getBalance(), balanceLabel);
         */
    }

}
