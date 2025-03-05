import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class EnhancedCalculatorGUI extends JFrame implements ActionListener, KeyListener {

    private JTextField inputField1;
    private JTextField inputField2;
    private JComboBox<String> operationComboBox;
    private JButton calculateButton, memoryPlusButton, memoryMinusButton, memoryClearButton, memoryRecallButton;
    private JLabel resultLabel;
    private double memoryValue = 0;
    private JTextArea historyArea;
    private List<String> history = new ArrayList<>();

    public EnhancedCalculatorGUI() {
        setTitle("Enhanced Calculator");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Input Fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(createLabel("Number 1:", Color.BLUE), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputField1 = createTextField();
        inputField1.addKeyListener(this);
        add(inputField1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(createLabel("Number 2:", Color.BLUE), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputField2 = createTextField();
        inputField2.addKeyListener(this);
        add(inputField2, gbc);

        // Operation ComboBox
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(createLabel("Operation:", Color.GREEN), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        String[] operations = {"+", "-", "*", "/", "sin", "cos", "tan", "sqrt", "^"};
        operationComboBox = new JComboBox<>(operations);
        operationComboBox.setBackground(Color.LIGHT_GRAY);
        add(operationComboBox, gbc);

        // Calculate Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        calculateButton = createButton("Calculate", Color.ORANGE);
        calculateButton.addActionListener(this);
        add(calculateButton, gbc);

        // Memory Buttons
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        memoryPlusButton = createButton("M+", Color.CYAN);
        memoryPlusButton.addActionListener(this);
        add(memoryPlusButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        memoryMinusButton = createButton("M-", Color.CYAN);
        memoryMinusButton.addActionListener(this);
        add(memoryMinusButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        memoryClearButton = createButton("MC", Color.CYAN);
        memoryClearButton.addActionListener(this);
        add(memoryClearButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        memoryRecallButton = createButton("MR", Color.CYAN);
        memoryRecallButton.addActionListener(this);
        add(memoryRecallButton, gbc);

        // Result Label
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        resultLabel = createLabel("Result: ", Color.RED);
        add(resultLabel, gbc);

        // History Area
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, gbc);

        getContentPane().setBackground(new Color(240, 248, 255));

        setVisible(true);
    }

    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.WHITE);
        return textField;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calculateButton) {
            calculateResult();
        } else if (e.getSource() == memoryPlusButton) {
            memoryValue += Double.parseDouble(resultLabel.getText().substring(8));
        } else if (e.getSource() == memoryMinusButton) {
            memoryValue -= Double.parseDouble(resultLabel.getText().substring(8));
        } else if (e.getSource() == memoryClearButton) {
            memoryValue = 0;
        } else if (e.getSource() == memoryRecallButton) {
            resultLabel.setText("Result: " + memoryValue);
        }
    }

    private void calculateResult() {
        try {
            double num1 = Double.parseDouble(inputField1.getText());
            double num2 = Double.parseDouble(inputField2.getText());
            String operation = (String) operationComboBox.getSelectedItem();
            double result = 0;

            switch (operation) {
                case "+": result = num1 + num2; break;
                case "-": result = num1 - num2; break;
                case "*": result = num1 * num2; break;
                case "/": if (num2 != 0) { result = num1 / num2; } else { resultLabel.setText("Result: Division by zero"); return; } break;
                case "sin": result = Math.sin(Math.toRadians(num1)); break;
                case "cos": result = Math.cos(Math.toRadians(num1)); break;
                case "tan": result = Math.tan(Math.toRadians(num1)); break;
                case "sqrt": result = Math.sqrt(num1); break;
                case "^": result = Math.pow(num1, num2); break;
            }
            resultLabel.setText("Result: " + result);
            addToHistory(num1, num2, operation, result);
        } catch (NumberFormatException ex) {
            resultLabel.setText("Result: Invalid input");
        }
    }

    private void addToHistory(double num1, double num2, String operation, double result) {
        String entry = num1 + " " + operation + " " + num2 + " = " + result;
        history.add(entry);
        StringBuilder historyText = new StringBuilder();
        for (String s : history) {
            historyText.append(s).append("\n");
        }
        historyArea.setText(historyText.toString());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            calculateResult();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EnhancedCalculatorGUI();
            }
        });
    }
}