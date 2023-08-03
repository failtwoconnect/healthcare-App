import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GUI extends Component {

    private final static int columnsLength = 15;
    private final static int logRows = 10;
    private final static int logColumns = 30;
    private final static String newline = "\n";

    private static int arrayListIndex;
    private Path load_File_Path;
    private static ArrayList<Patient> patientArrayList;
    private JTextField fname;
    private JTextField lastname;
    private JTextField addressLine1;
    private JTextField addressLine2;
    private JTextField city;
    private JTextField state;
    private JTextField zip;
    private JTextArea log;
    private JLabel errorLabel;
    private JTextField searchField;
    private JScrollPane scroll;
    private JButton prevButton;
    private JButton nextButton;
    public GUI() {
    }

    public void startGUI() {
        JFrame frame = new JFrame();
        patientArrayList = new ArrayList<>();
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Patient Screen", patientPanel());
        tabbedPane.add("Admin Screen", adminPanel());
        tabbedPane.add("Search Screen",searchPanel());
        frame.add(tabbedPane);


        frame.setSize(new Dimension(950, 600));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private JPanel adminPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        log = new JTextArea();
        log.setEditable(false);
        log.setRows(logRows);
        log.setColumns(logColumns);
        c.gridy = 0;
        scroll = new JScrollPane(log);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scroll, c);

        c.gridy = 1;
        panel.add(btnPanel(), c);



        return panel;
    }

    private JComponent btnPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JButton loadButton = new JButton("Load");
        c.gridy = 1;
        panel.add(loadButton, c);
        loadButton.addActionListener(this::adminLoadButtonActionPerformed);

        prevButton = new JButton("Previous");
        c.gridx = 1;
        panel.add(prevButton, c);
        prevButton.addActionListener(this::previousButtonActionPerformed);
        prevButton.addActionListener(this::cycleActionListenerPerformed);
        prevButton.setEnabled(false);


        nextButton = new JButton("Next");
        c.gridx = 2;
        panel.add(nextButton, c);
        nextButton.addActionListener(this::nextButtonActionPerformed);
        nextButton.addActionListener(this::cycleActionListenerPerformed);
        nextButton.setEnabled(false);

        return panel;
    }



    private JPanel patientPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel fNameLabel = new JLabel("First Name");
        c.gridx = 0;
        c.gridy = 0;
        panel.add(fNameLabel, c);

        fname = new JTextField();
        fname.setColumns(columnsLength);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(fname, c);

        JLabel lastNameLabel = new JLabel("Last Name");
        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(0, 5, 0, 0);
        panel.add(lastNameLabel, c);

        lastname = new JTextField();
        lastname.setColumns(columnsLength);
        c.gridx = 3;
        c.gridy = 0;
        panel.add(lastname, c);

        JLabel addressLine1Label = new JLabel("Address Line 1");
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(5, 0, 0, 5);
        panel.add(addressLine1Label, c);

        addressLine1 = new JTextField();
        addressLine1.setColumns(columnsLength);
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(5, 0, 0, 0);
        panel.add(addressLine1, c);

        JLabel addressLine2Label = new JLabel("Address Line 2");
        c.gridx = 2;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 0, 5);
        panel.add(addressLine2Label, c);

        addressLine2 = new JTextField();
        addressLine2.setColumns(columnsLength);
        c.gridx = 3;
        c.gridy = 1;
        c.insets = new Insets(5, 5, 0, 0);
        panel.add(addressLine2, c);

        JLabel cityLabel = new JLabel("City");
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5, 0, 0, 0);
        panel.add(cityLabel, c);

        city = new JTextField();
        city.setColumns(columnsLength);
        c.gridx = 1;
        c.gridy = 2;
        panel.add(city, c);

        JLabel stateLabel = new JLabel("State");
        c.gridx = 2;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 0, 5);
        panel.add(stateLabel, c);

        state = new JTextField();
        state.setColumns(columnsLength);
        c.gridx = 3;
        c.gridy = 2;
        c.insets = new Insets(5, 5, 0, 0);
        panel.add(state, c);

        JLabel zipcodeLabel = new JLabel("Zip Code");
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(5, 0, 0, 0);
        panel.add(zipcodeLabel, c);

        zip = new JTextField();
        zip.setColumns(columnsLength);
        c.gridx = 1;
        c.gridy = 3;
        panel.add(zip, c);

        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 2;
        panel.add(savePanel(), c);


        return panel;
    }

    private JPanel savePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        errorLabel = new JLabel("");
        c.gridx = 0;
        c.gridy = 0;
        panel.add(errorLabel, c);

        JButton saveButton = new JButton("Save");
        c.gridx = 0;
        c.gridy = 1;
        panel.add(saveButton, c);
        saveButton.addActionListener(this::saveButtonActionPerformed);


        return panel;
    }


    private void saveButtonActionPerformed(ActionEvent event) {
        try {
            Patient patient = new Patient();
            if (saveButtonBlankValidation() && saveButtonCharacterValidation()) {

                errorLabel.setText("");
                patient.setFirstName(fname.getText());
                patient.setLastName(lastname.getText());
                patient.setAddressLine1(addressLine1.getText());
                patient.setAddressLine2(addressLine2.getText());
                patient.setCity(city.getText());
                patient.setState(state.getText());
                patient.setZip(zip.getText());
                log.append(patient.toString());
                log.append(newline);

            } else {
                if(saveButtonBlankValidation() == false) {
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText("One or more fields are blank");
                }
                else{
                    errorLabel.setForeground(Color.RED);
                    errorLabel.setText("fields aren't in the right format");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean saveButtonBlankValidation(){
        boolean validation = !fname.getText().isBlank() &&
                !lastname.getText().isBlank() &&
                !addressLine1.getText().isBlank() &&
                !city.getText().isBlank() &&
                !state.getText().isBlank() &&
                !zip.getText().isBlank();


        return validation;
    }

    private boolean saveButtonCharacterValidation(){
        boolean validation = zip.getText().matches("^[0-9]") &&
                (fname.getText().matches("^[a-zA-Z]+\\s[a-zA-Z]") ||
                        fname.getText().matches("^[a-zA-Z]"))&&
                (lastname.getText().matches("^[a-zA-Z]+\\s[a-zA-Z]") ||
                        lastname.getText().matches("^[a-zA-Z]")) &&
                (city.getText().matches("^[a-zA-Z]") ||
                        city.getText().matches("^[a-zA-Z]+\\s[a-zA-Z]"))&&
                (state.getText().matches("^[a-zA-Z]") ||
                        state.getText().matches("^[a-zA-Z]+\\s[a-zA-Z]"))&&
                addressLine1.getText().matches("^[0-9]+\\s[A-Za-z]+\\s[A-Za-z]+") &&
                addressLine2.getText().isBlank();
        return validation;
    }

    private JComponent searchPanel(){
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel searchLabel = new JLabel("Search");
        c.gridx = 0;
        c.gridy = 0;
        pane.add(searchLabel, c);

        searchField = new JTextField();
        searchField.setColumns(columnsLength);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(0, 5, 0, 0);
        pane.add(searchField, c);



        return pane;
    }


    private void adminLoadButtonActionPerformed(ActionEvent event) {
        log.append("loading file...");
        log.append(newline);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File("C:/Users/Emily/IdeaProjects"));
        int returnVal = fileChooser.showOpenDialog(GUI.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            load_File_Path = Path.of(file.toURI());
            log.append(file.getAbsolutePath());
            log.append(newline);

        }
        String content;
        try (BufferedReader br = Files.newBufferedReader(load_File_Path)) {
            while ((content = br.readLine()) != null) {
                Patient patient = new Patient();
                String[] s = content.split(",");
                patient.setFirstName(s[0]);
                patient.setLastName(s[1]);
                patient.setAddressLine1(s[2]);
                patient.setAddressLine2(s[3]);
                patient.setCity(s[4]);
                patient.setState(s[5]);
                patient.setZip(s[6]);
                patientArrayList.add(patient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        nextButton.setEnabled(true);
    }

    private void patientFill(int patient){
        fname.setText(patientArrayList.get(patient).getFirstName());
        lastname.setText(patientArrayList.get(patient).getLastName());
        addressLine1.setText(patientArrayList.get(patient).getAddressLine1());
        addressLine2.setText(patientArrayList.get(patient).getAddressLine2());
        city.setText(patientArrayList.get(patient).getCity());
        state.setText(patientArrayList.get(patient).getState());
        zip.setText(patientArrayList.get(patient).getZip());
    }

    private void previousButtonActionPerformed(ActionEvent event){
        log.append("previous button clicked...");
        log.append(newline);
        arrayListIndex--;
        log.append(Integer.toString(arrayListIndex));
        log.append(newline);

    }

    private void nextButtonActionPerformed(ActionEvent event){
        log.append("Next Patient...");
        log.append(newline);
        arrayListIndex++;
        log.append(Integer.toString(arrayListIndex));
        log.append(newline);

    }

    private void cycleActionListenerPerformed(ActionEvent event){
        if(arrayListIndex == 0) {
            prevButton.setEnabled(false);
            nextButton.setEnabled(true);
        }
        else if(arrayListIndex < patientArrayList.size()-1 && arrayListIndex >= 1) {
            nextButton.setEnabled(true);
            prevButton.setEnabled(true);
        }
        else {
            nextButton.setEnabled(false);
            prevButton.setEnabled(true);
        }

    }


}
