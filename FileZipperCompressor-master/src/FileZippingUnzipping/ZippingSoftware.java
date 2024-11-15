package FileZippingUnzipping;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
public class ZippingSoftware extends JFrame {
    public JTextField folderPathTextField, zipFilePathTextField;
    public JButton selectFolderButton, zipButton, selectZipFileButton, unzipButton;
    public JLabel statusLabel, headingLabel;
    public String selectedFolderPath = "";
    public String selectedZipFilePath = "";

    public ZippingSoftware() {
        GridBagConstraints gbc = new GridBagConstraints();
        getContentPane().setBackground(new Color(31, 40, 51));
        setTitle("Folder Zipping and Unzipping Software");
        setSize(500, 350);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new HeadingSlider());

        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components
        setLocationRelativeTo(null); // Center the window


        headingLabel = new JLabel("");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        headingLabel.setForeground(Color.WHITE);
        headingLabel.setFont(new Font("Serif", Font.BOLD, 17));
        gbc.anchor = GridBagConstraints.CENTER;
        add(headingLabel, gbc);


        JLabel folderPathLabel = new JLabel("Folder to Zip:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        folderPathLabel.setForeground(Color.WHITE);
        add(folderPathLabel, gbc);

        folderPathTextField = new JTextField();
        folderPathTextField.setEditable(false); // Not editable directly
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow horizontal stretching
        gbc.weightx = 1.0; // Allow this column to expand
        add(folderPathTextField, gbc);

        // Select folder button
        selectFolderButton = new JButton("Select Folder");
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(selectFolderButton, gbc);
        selectFolderButton.addActionListener(new SelectFolderButtonListener());




        // Zip button
        zipButton = new JButton("Zip Folder");
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;

        gbc.anchor = GridBagConstraints.CENTER;
        add(zipButton, gbc);
        zipButton.addActionListener(new ZipButtonListener());

        // ZIP file to unzip label
        JLabel zipFilePathLabel = new JLabel("ZIP File to Unzip:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        zipFilePathLabel.setForeground(Color.WHITE);
        gbc.anchor = GridBagConstraints.EAST;
        add(zipFilePathLabel, gbc);

        // ZIP file path text field
        zipFilePathTextField = new JTextField();
        zipFilePathTextField.setEditable(false); // Not editable directly
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow horizontal stretching
        gbc.weightx = 1.0; // Allow this column to expand
        add(zipFilePathTextField, gbc);

        // Select ZIP file button
        selectZipFileButton = new JButton("Select ZIP File");
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(selectZipFileButton, gbc);
        selectZipFileButton.addActionListener(new SelectZipFileButtonListener());

        // Unzip button
        unzipButton = new JButton("Unzip File");
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(unzipButton, gbc);
        unzipButton.addActionListener(new UnzipButtonListener());

        // Status label
        statusLabel = new JLabel("Status: Waiting for folder selection...");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        statusLabel.setForeground(Color.WHITE);
        gbc.anchor = GridBagConstraints.CENTER;
        add(statusLabel, gbc);
    }

    // Action listener for selecting a folder for zipping
    public class SelectFolderButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Select only directories
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFolder = fileChooser.getSelectedFile();
                selectedFolderPath = selectedFolder.getAbsolutePath();
                JOptionPane.showMessageDialog(null,selectedFolderPath);
                folderPathTextField.setText(selectedFolderPath);
                statusLabel.setText("Status: Folder selected for zipping.");
            } else {
                statusLabel.setText("Status: No folder selected.");
            }
        }
    }

    class HeadingSlider extends WindowAdapter {
        public void windowOpened(WindowEvent e) {
            ThreadedText T1 = new ThreadedText("File Zipping and UnZipping", headingLabel);
            T1.t.start();
        }
    }

    // Action listener for selecting a ZIP file for unzipping
    public class SelectZipFileButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Select only files
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("ZIP files", "zip"));
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedZipFile = fileChooser.getSelectedFile();
                selectedZipFilePath = selectedZipFile.getAbsolutePath();
                zipFilePathTextField.setText(selectedZipFilePath);
                statusLabel.setText("Status: ZIP file selected for unzipping.");
            } else {
                statusLabel.setText("Status: No ZIP file selected.");
            }
        }
    }

    // Action listener for zipping the selected folder
    public class ZipButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedFolderPath.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a folder first!");
                return;
            }

            String zipFileName = selectedFolderPath + ".zip";
            File zipFile = new File(zipFileName);

            // If the file exists, delete it to replace with the new one
            if (zipFile.exists()) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "A zip file with the same name already exists. Do you want to replace it?",
                        "Confirm Overwrite",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.NO_OPTION) {
                    return; // If the user doesn't want to replace the file, stop the operation
                } else {
                    zipFile.delete(); // Delete the existing zip file
                }
            }

            try {
                zipFolder(selectedFolderPath, zipFileName);
                statusLabel.setText("Status: Folder zipped successfully!");
                JOptionPane.showMessageDialog(null, "Folder zipped successfully!");
            } catch (IOException ioException) {
                statusLabel.setText("Status: Error occurred while zipping!");
                JOptionPane.showMessageDialog(null, "Error: " + ioException.getMessage());
            }
        }
    }


    // Action listener for unzipping the selected ZIP file
    public class UnzipButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedZipFilePath.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a ZIP file first!");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Select only directories
            int result = fileChooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File destinationFolder = fileChooser.getSelectedFile();
                try {
                    unzipFile(selectedZipFilePath, destinationFolder.getAbsolutePath());
                    statusLabel.setText("Status: ZIP file unzipped successfully!");
                    JOptionPane.showMessageDialog(null, "ZIP file unzipped successfully!");
                } catch (IOException ioException) {
                    statusLabel.setText("Status: Error occurred while unzipping!");
                    JOptionPane.showMessageDialog(null, "Error: " + ioException.getMessage());
                }
            }
        }
    }

    // Method to zip the selected folder
    public void zipFolder(String sourceDirPath, String zipFilePath) throws IOException {
        Path zipFile = Files.createFile(Paths.get(zipFilePath));

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            Path sourceDir = Paths.get(sourceDirPath);
            Files.walk(sourceDir)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDir.relativize(path).toString());
                        try {
                            zipOut.putNextEntry(zipEntry);
                            Files.copy(path, zipOut);
                            zipOut.closeEntry();
                        } catch (IOException e) {
                            System.err.println("Error while zipping: " + e);
                        }
                    });
        }
    }

    // Method to unzip the selected ZIP file
    public void unzipFile(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();

            while (entry != null) {
                String filePath = destDir + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dirEntry = new File(filePath);
                    dirEntry.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    // Method to extract individual file
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[4096];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ZippingSoftware zippingSoftware = new ZippingSoftware();
            zippingSoftware.setVisible(true);
        });
    }
}
