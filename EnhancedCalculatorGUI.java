import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class EnhancedCalculatorGUI extends JFrame implements ActionListener {

    private JTextField inputField;
    private JButton[] numberButtons;
    private JButton[] operationButtons;
    private JButton deleteButton, clearButton;
    private List<String> history = new ArrayList<>();

    public EnhancedCalculatorGUI() {
        setTitle("Calculator");
        setSize(300, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Field
        inputField = new JTextField("3.141");
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        inputField.setFont(new Font("Arial", Font.PLAIN, 24));
        inputField.setEditable(false);
        add(inputField, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Number and Operation Buttons
        String[] buttonLabels = {
            "1", "2", "3", "+",
            "4", "5", "6", "-",
            "7", "8", "9", "*",
            ".", "0", "=", "/"
        };

        numberButtons = new JButton[10];
        operationButtons = new JButton[4];

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.setBackground(Color.WHITE);
            button.addActionListener(this);

            if (i % 4 == 3) {
                // Operation buttons
                operationButtons[i/4] = button;
                button.setBackground(new Color(240, 240, 240));
            } else {
                // Number buttons
                if (!buttonLabels[i].matches("[+\\-*/=.]")) {
                    numberButtons[Integer.parseInt(buttonLabels[i])] = button;
                }
            }
            buttonsPanel.add(button);
        }

        // Delete and Clear Buttons
        JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);

        deleteButton.setFont(new Font("Arial", Font.PLAIN, 16));
        clearButton.setFont(new Font("Arial", Font.PLAIN, 16));

        controlPanel.add(deleteButton);
        controlPanel.add(clearButton);

        // Add panels to frame
        add(buttonsPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        String currentText = inputField.getText();
        
        if (command.matches("[0-9.]")) {
            // Prevent multiple decimal points
            if (!(command.equals(".") && currentText.contains("."))) {
                inputField.setText(currentText + command);
            }
        } else if (command.matches("[+\\-*/]")) {
            // Prevent multiple consecutive operators
            if (!isOperator(currentText.charAt(currentText.length() - 1))) {
                inputField.setText(currentText + command);
            }
        } else if (command.equals("=")) {
            // Calculate result
            try {
                double result = evaluateExpression(currentText);
                inputField.setText(String.valueOf(result));
            } catch (Exception ex) {
                inputField.setText("Error");
            }
        } else if (command.equals("Delete")) {
            // Remove last character
            if (!currentText.isEmpty()) {
                inputField.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (command.equals("Clear")) {
            // Clear input field
            inputField.setText("");
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private double evaluateExpression(String expression) {
        // Remove any trailing operator
        while (!expression.isEmpty() && isOperator(expression.charAt(expression.length() - 1))) {
            expression = expression.substring(0, expression.length() - 1);
        }

        // Simple expression evaluation using two operands and one operator
        for (char op : new char[]{'+', '-', '*', '/'}) {
            int opIndex = expression.lastIndexOf(op);
            if (opIndex != -1) {
                try {
                    double left = Double.parseDouble(expression.substring(0, opIndex));
                    double right = Double.parseDouble(expression.substring(opIndex + 1));
                    
                    switch (op) {
                        case '+': return left + right;
                        case '-': return left - right;
                        case '*': return left * right;
                        case '/': 
                            if (right == 0) throw new ArithmeticException("Division by zero");
                            return left / right;
                    }
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Invalid expression");
                }
            }
        }
        
        // If no operator found, try to parse as a single number
        try {
            return Double.parseDouble(expression);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid expression");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EnhancedCalculatorGUI());
    }
}
