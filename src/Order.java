import org.jdatepicker.JDateComponent;
import org.jdatepicker.JDatePanel;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class Order extends JFrame {
    private JDatePickerImpl datePicker;
    private JLabel labelDate, labelName, labelFood, labelPrice, labelQty, labelFoodPrice;
    private JTextField nameInput, qtyInput;
    private JComboBox<String> cbFood;
    private JRadioButton rbDeli, rbTakeAway, rbDineIn;
    private JButton btnAdd, btnFinish, btnClear;
    private JTable tableOrder;
    private DefaultTableModel tableModel;

    private double price = 0.0, totalPrice = 0.0, surcharge = 0.0;
    private List<String> salesData = new ArrayList<>();

    public Order() {
        super.setTitle("Ordering System");
        super.setSize(600,900);
        super.setLocationRelativeTo(null);
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container cp = super.getContentPane(); //content pane
        cp.setLayout(new BorderLayout()); //main layout

        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new GridLayout(4, 1)); //element in center region
        panelCenter.setBackground(new Color(231, 245, 236));

        JPanel paneldnf = new JPanel(); //panel for date, name, food section
        paneldnf.setLayout(new GridLayout(3, 2));
        paneldnf.setBackground(new Color(231, 245, 236));

        labelDate = new JLabel("Date: "); //date label
        labelDate.setBackground(new Color(248, 248, 215));
        labelDate.setHorizontalAlignment(JLabel.CENTER);

        UtilDateModel model = new UtilDateModel(); //date component
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePanel.setBackground(new Color(231, 245, 236));
        this.datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
        datePicker.setBackground(new Color(231, 245, 236));

        labelName = new JLabel("Name: "); //name label
        labelName.setHorizontalAlignment(JLabel.CENTER);

        nameInput = new JTextField(); // name text field
        nameInput.setSize(new Dimension(100, 30));

        labelFood = new JLabel("Food: "); //food label
        labelFood.setBackground(new Color(231, 245, 236));
        labelFood.setHorizontalAlignment(JLabel.CENTER);

        cbFood = new JComboBox<>(); //food dropdown component
        cbFood.addItem("Cendol");
        cbFood.addItem("Cendol Kacang");
        cbFood.addItem("Cendol Jagung");
        cbFood.addItem("Cendol Pulut");

        cbFood.addActionListener(e -> updatePrice());

        JPanel panelpq = new JPanel(); //panel for price and quantity
        panelpq.setLayout(new GridLayout(2,2));
        panelpq.setBackground(new Color(231, 245, 236));

        labelPrice = new JLabel("Price"); //label for price
        labelPrice.setBackground(new Color(231, 245, 236));
        labelPrice.setHorizontalAlignment(JLabel.CENTER);

        labelQty = new JLabel("Quantity"); //label for quantity
        labelQty.setHorizontalAlignment(JLabel.CENTER);

        labelFoodPrice = new JLabel("RM3.00"); //label for the price of food
        labelFoodPrice.setBackground(new Color(231, 245, 236));
        labelFoodPrice.setHorizontalAlignment(JLabel.CENTER);

        qtyInput= new JTextField(); //text field for quantity
        qtyInput.setBackground(new Color(255, 255, 255));

        JPanel panelrb = new JPanel(); //panel for radio button
        panelrb.setLayout(new FlowLayout());
        panelrb.setBackground(new Color(231, 245, 236));

        rbDeli=new JRadioButton(); //radio button for delivery
        rbDineIn=new JRadioButton(); //radio button for dine in
        rbTakeAway=new JRadioButton(); //radio button for takeaway

        rbDeli.setBackground(new Color(248, 248, 215));
        rbDineIn.setBackground(new Color(248, 248, 215));
        rbTakeAway.setBackground(new Color(248, 248, 215));

        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(rbDeli);
        btnGroup.add(rbDineIn);
        btnGroup.add(rbTakeAway);

        rbDeli.setText("Delivery(+RM4.00)"); //caption for rbDeli
        rbDineIn.setText("Dine In"); //caption for rbDineIn
        rbTakeAway.setText("Take Away(+RM1.00)"); //caption for rbTakeAway

        JPanel panelbtn = new JPanel(); //panel for button
        panelbtn.setLayout(new FlowLayout());
        panelbtn.setBackground(new Color(231, 245, 236));

        btnAdd = new JButton("Add"); //button for adding new menu
        btnAdd.addActionListener(e -> addToTable());
        btnAdd.addActionListener(e -> saveToFile());
        btnFinish = new JButton("Finish"); //button for creating the order
        btnFinish.addActionListener(e -> calculateTotal());
        btnClear = new JButton("Clear"); //button for clearing the form
        btnClear.addActionListener(e -> clearForm());

        JPanel panelSouth = new JPanel();
        panelSouth.setBackground(new Color(231, 245, 236));
        panelSouth.setLayout(new FlowLayout());

        String[] column = {"Description", "Food", "Quantity", "Price(RM)", "Surcharge(RM)"};

        tableModel = new DefaultTableModel(column, 0);
        tableOrder = new JTable(tableModel); //table for orders
        tableOrder.setBounds(40, 50, 400, 100);
        tableOrder.setBackground(new Color(248, 248, 215));


        JScrollPane sp = new JScrollPane(tableOrder);

        paneldnf.add(labelDate); //add date label to panel dnf
        paneldnf.add(datePicker); //add date component to panel dnf
        paneldnf.add(labelName); //add name label to panel dnf
        paneldnf.add(nameInput); //add name text field to panel dnf
        paneldnf.add(labelFood); //add food label to panel dnf
        paneldnf.add(cbFood); //add food dropdown to panel dnf

        panelpq.add(labelPrice); //add price label to panel pq
        panelpq.add(labelQty); //add quantity label to panel pq
        panelpq.add(labelFoodPrice); //add food price label to panel pq
        panelpq.add(qtyInput); //add quantity text field to panel pq

        panelrb.add(rbDeli); //add delivery radio button to panel rb
        panelrb.add(rbDineIn); //add dine in radio button to panel rb
        panelrb.add(rbTakeAway); //add take away radio button to panel rb

        panelbtn.add(btnAdd); //add button add to panel button
        panelbtn.add(btnFinish); // add finish button to panel button
        panelbtn.add(btnClear); //add button clear to panel button

        panelCenter.add(paneldnf); //add panel dnf to panel center
        panelCenter.add(panelpq); //add panel pq to panel center
        panelCenter.add(panelrb); //add panel rb to panel center
        panelCenter.add(panelbtn); //add panel btn to panel center

        panelSouth.add(sp);

        cp.add(panelCenter, BorderLayout.CENTER); //add panel center to content panel
        cp.add(panelSouth, BorderLayout.SOUTH); //add panel south to content panel

        super.setVisible(true);
    }

    private void updatePrice(){
        String foodSelected = (String) cbFood.getSelectedItem();

        switch (foodSelected){
            case "Cendol":
                labelFoodPrice.setText("RM3.00");
                break;
            case "Cendol Kacang":
                labelFoodPrice.setText("RM5.00");
                break;
            case "Cendol Jagung":
                labelFoodPrice.setText("RM5.00");
                break;
            case "Cendol Pulut":
                labelFoodPrice.setText("RM6.00");
                break;
            default:
                labelFoodPrice.setText("");
        }
    }

    private double returnPrice(){
        String foodSelected = (String) cbFood.getSelectedItem();

        switch (foodSelected){
            case "Cendol":
                price = 3.00;
                break;
            case "Cendol Kacang":
                price = 5.00;
                break;
            case "Cendol Jagung":
                price = 5.00;
                break;
            case "Cendol Pulut":
                price = 6.00;
                break;
            default:
                price = 0.0;
        }
        return price;
    }

    private String rbSelected(){
        String rbSelected = "";
        if (rbDeli.isSelected()){
            rbSelected = "Delivery";
        } else if (rbDineIn.isSelected()) {
            rbSelected = "Dine In";
        } else {
            rbSelected = "Take Away";
        }
        return rbSelected;
    }

    private double calcSurcharge(){
        if (rbDeli.isSelected()){
            surcharge = 4.00;
        } else if (rbTakeAway.isSelected()) {
            surcharge = 1.00;
        } else {
            surcharge = 0.0;
        }
        return surcharge;
    }

    private void addToTable(){
        String method = rbSelected();
        String foodName = (String) cbFood.getSelectedItem();
        int qty = Integer.parseInt(qtyInput.getText());
        double surcharge = calcSurcharge();
        tableModel.addRow(new Object[]{method,foodName, qty, returnPrice(), surcharge});
        clearCenter();
    }

    private void calculateTotal(){
        initTotalPrice();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int quantity = (int) tableModel.getValueAt(i, 2);
            double price = (double) tableModel.getValueAt(i, 3);
            double surcharge = (double) tableModel.getValueAt(i,4);
            totalPrice += quantity * price + surcharge;
        }
        tableModel.addRow(new Object[]{"Total Price(RM)", "", "", "", totalPrice});
    }

    private void initTotalPrice(){
        totalPrice = 0.0;
    }

    private void clearCenter(){
        cbFood.setSelectedIndex(0);
        qtyInput.setText("");
        rbDeli.setSelected(false);
        rbDineIn.setSelected(false);
        rbTakeAway.setSelected(false);
    }

    private void clearForm(){
        initTotalPrice();
        nameInput.setText("");
        cbFood.setSelectedIndex(0);
        qtyInput.setText("");
        rbDeli.setSelected(false);
        rbDineIn.setSelected(false);
        rbTakeAway.setSelected(false);
        tableModel.setRowCount(0);
    }

    private void saveToFile(){
        Date date = (Date) this.datePicker.getModel().getValue();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        String name = nameInput.getText();
        String food = (String) cbFood.getSelectedItem();
        int qty = Integer.parseInt(qtyInput.getText());
        String method = rbSelected();

        String salesEntry = (String.format("%-20s%-20s%-20s%-20s%-20s\n", formattedDate, name, food, qty, method));
        //String salesEntry = formattedDate + "\t" + name + "\t" + food + "\t" + qty + "\t" + method + "\n";
        salesData.add(salesEntry);

        try {
            FileWriter fw = new FileWriter("Sales.txt");

            fw.write(String.format("%-20s%-20s%-20s%-20s%-20s\n", "Date", "Name", "Food", "Quantity", "Service Choice"));
            //fw.write("Date\tName\tFood\tQuantity\tService Choice\n");
            fw.write("==============================================================================================\n");

            for (String entry : salesData){
                fw.write(entry);
            }

            fw.close();
        }
        catch (FileNotFoundException fnf){
            System.out.println(fnf);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
