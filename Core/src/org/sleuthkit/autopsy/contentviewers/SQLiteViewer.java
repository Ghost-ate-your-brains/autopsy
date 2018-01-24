/*
 * Autopsy Forensic Browser
 *
 * Copyright 2018 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.contentviewers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.JComboBox;
import javax.swing.SwingWorker;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.datamodel.ContentUtils;
import org.sleuthkit.datamodel.AbstractFile;

public class SQLiteViewer extends javax.swing.JPanel implements FileTypeViewer {

    public static final String[] SUPPORTED_MIMETYPES = new String[]{"application/x-sqlite3"};
    private static final Logger LOGGER = Logger.getLogger(FileViewer.class.getName());
    private Connection connection = null;

    private String tmpDBPathName = null;
    private File tmpDBFile = null;

    private final Map<String, String> dbTablesMap = new TreeMap<>();

    private static final int ROWS_PER_PAGE = 100;
    private int numRows;    // num of rows in the selected table
    private int currPage = 0; // curr page of rows being displayed

    SQLiteTableView selectedTableView = new SQLiteTableView();

    private SwingWorker<? extends Object, ? extends Object> worker;

    /**
     * Creates new form SQLiteViewer
     */
    public SQLiteViewer() {
        initComponents();
        jTableDataPanel.add(selectedTableView, BorderLayout.CENTER);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jHdrPanel = new javax.swing.JPanel();
        tablesDropdownList = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        numEntriesField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        currPageLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        numPagesLabel = new javax.swing.JLabel();
        prevPageButton = new javax.swing.JButton();
        nextPageButton = new javax.swing.JButton();
        jTableDataPanel = new javax.swing.JPanel();

        jHdrPanel.setPreferredSize(new java.awt.Dimension(536, 40));

        tablesDropdownList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        tablesDropdownList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tablesDropdownListActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(SQLiteViewer.class, "SQLiteViewer.jLabel1.text")); // NOI18N

        numEntriesField.setEditable(false);
        numEntriesField.setText(org.openide.util.NbBundle.getMessage(SQLiteViewer.class, "SQLiteViewer.numEntriesField.text")); // NOI18N
        numEntriesField.setBorder(null);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(SQLiteViewer.class, "SQLiteViewer.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(currPageLabel, org.openide.util.NbBundle.getMessage(SQLiteViewer.class, "SQLiteViewer.currPageLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(SQLiteViewer.class, "SQLiteViewer.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(numPagesLabel, org.openide.util.NbBundle.getMessage(SQLiteViewer.class, "SQLiteViewer.numPagesLabel.text")); // NOI18N

        prevPageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/corecomponents/btn_step_back.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(prevPageButton, org.openide.util.NbBundle.getMessage(SQLiteViewer.class, "SQLiteViewer.prevPageButton.text")); // NOI18N
        prevPageButton.setBorderPainted(false);
        prevPageButton.setContentAreaFilled(false);
        prevPageButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/corecomponents/btn_step_back_disabled.png"))); // NOI18N
        prevPageButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
        prevPageButton.setPreferredSize(new java.awt.Dimension(23, 23));
        prevPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevPageButtonActionPerformed(evt);
            }
        });

        nextPageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/corecomponents/btn_step_forward.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(nextPageButton, org.openide.util.NbBundle.getMessage(SQLiteViewer.class, "SQLiteViewer.nextPageButton.text")); // NOI18N
        nextPageButton.setBorderPainted(false);
        nextPageButton.setContentAreaFilled(false);
        nextPageButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/org/sleuthkit/autopsy/corecomponents/btn_step_forward_disabled.png"))); // NOI18N
        nextPageButton.setMargin(new java.awt.Insets(2, 0, 2, 0));
        nextPageButton.setPreferredSize(new java.awt.Dimension(23, 23));
        nextPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextPageButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jHdrPanelLayout = new javax.swing.GroupLayout(jHdrPanel);
        jHdrPanel.setLayout(jHdrPanelLayout);
        jHdrPanelLayout.setHorizontalGroup(
            jHdrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jHdrPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tablesDropdownList, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(numEntriesField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currPageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numPagesLabel)
                .addGap(18, 18, 18)
                .addComponent(prevPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(nextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(133, Short.MAX_VALUE))
        );
        jHdrPanelLayout.setVerticalGroup(
            jHdrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jHdrPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jHdrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prevPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jHdrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tablesDropdownList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(numEntriesField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(currPageLabel)
                        .addComponent(jLabel3)
                        .addComponent(numPagesLabel)))
                .addContainerGap())
        );

        jTableDataPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jHdrPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTableDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jHdrPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTableDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextPageButtonActionPerformed

        currPage++;
        if (currPage * ROWS_PER_PAGE > numRows) {
            nextPageButton.setEnabled(false);
        }
        currPageLabel.setText(Integer.toString(currPage));
        prevPageButton.setEnabled(true);

        // read and display a page of rows
        String tableName = (String) this.tablesDropdownList.getSelectedItem();
        readTable(tableName, (currPage - 1) * ROWS_PER_PAGE + 1, ROWS_PER_PAGE);
    }//GEN-LAST:event_nextPageButtonActionPerformed

    private void prevPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevPageButtonActionPerformed

        currPage--;
        if (currPage == 1) {
            prevPageButton.setEnabled(false);
        }
        currPageLabel.setText(Integer.toString(currPage));
        nextPageButton.setEnabled(true);

        // read and display a page of rows
        String tableName = (String) this.tablesDropdownList.getSelectedItem();
        readTable(tableName, (currPage - 1) * ROWS_PER_PAGE + 1, ROWS_PER_PAGE);
    }//GEN-LAST:event_prevPageButtonActionPerformed

    private void tablesDropdownListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tablesDropdownListActionPerformed
        JComboBox<?> cb = (JComboBox<?>) evt.getSource();
        String tableName = (String) cb.getSelectedItem();
        if (null == tableName) {
            return;
        }

        selectTable(tableName);
    }//GEN-LAST:event_tablesDropdownListActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel currPageLabel;
    private javax.swing.JPanel jHdrPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jTableDataPanel;
    private javax.swing.JButton nextPageButton;
    private javax.swing.JTextField numEntriesField;
    private javax.swing.JLabel numPagesLabel;
    private javax.swing.JButton prevPageButton;
    private javax.swing.JComboBox<String> tablesDropdownList;
    // End of variables declaration//GEN-END:variables

    @Override
    public List<String> getSupportedMIMETypes() {
        return Arrays.asList(SUPPORTED_MIMETYPES);
    }

    @Override
    public void setFile(AbstractFile file) {
        processSQLiteFile(file);
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void resetComponent() {

        dbTablesMap.clear();

        tablesDropdownList.setEnabled(true);
        tablesDropdownList.removeAllItems();
        numEntriesField.setText("");

        // close DB connection to file
        if (null != connection) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Failed to close DB connection to file.", ex); //NON-NLS
            }
        }

        // delete last temp file
        if (null != tmpDBFile) {
            tmpDBFile.delete();
            tmpDBFile = null;
        }
    }

    /**
     * Process the given SQLite DB file
     *
     * @param sqliteFile -
     *
     * @return none
     */
    private void processSQLiteFile(AbstractFile sqliteFile) {

        tablesDropdownList.removeAllItems();

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {

                try {
                    // Copy the file to temp folder
                    tmpDBPathName = Case.getCurrentCase().getTempDirectory() + File.separator + sqliteFile.getName() + "-" + sqliteFile.getId();
                    tmpDBFile = new File(tmpDBPathName);
                    ContentUtils.writeToFile(sqliteFile, tmpDBFile);

                    // Open copy using JDBC
                    Class.forName("org.sqlite.JDBC"); //NON-NLS //load JDBC driver 
                    connection = DriverManager.getConnection("jdbc:sqlite:" + tmpDBPathName); //NON-NLS

                    // Read all table names and schema
                    return getTables();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Failed to copy DB file.", ex); //NON-NLS
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Failed to Open DB.", ex); //NON-NLS
                } catch (ClassNotFoundException ex) {
                    LOGGER.log(Level.SEVERE, "Failed to initialize JDBC Sqlite.", ex); //NON-NLS
                }
                return false;
            }

            @Override
            protected void done() {
                super.done();
                try {
                    boolean status = get();
                    if ((status == true) && (dbTablesMap.size() > 0)) {
                        dbTablesMap.keySet().forEach((tableName) -> {
                            tablesDropdownList.addItem(tableName);
                        });
                    } else {
                        // Populate error message
                        tablesDropdownList.addItem("No tables found");
                        tablesDropdownList.setEnabled(false);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.log(Level.SEVERE, "Unexpected exception while opening DB file", ex); //NON-NLS
                }
            }
        }.execute();

    }

    /**
     * Gets the table names and their schema from loaded SQLite db file
     *
     * @return true if success, false otherwise
     */
    private boolean getTables() {

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(
                    "SELECT name, sql FROM sqlite_master "
                    + " WHERE type= 'table' "
                    + " ORDER BY name;"); //NON-NLS

            while (resultSet.next()) {
                String tableName = resultSet.getString("name"); //NON-NLS
                String tableSQL = resultSet.getString("sql"); //NON-NLS

                dbTablesMap.put(tableName, tableSQL);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting table names from the DB", e); //NON-NLS
        }
        return true;
    }

    private void selectTable(String tableName) {
        if (worker != null && !worker.isDone()) {
            worker.cancel(false);
            worker = null;
        }

        worker = new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() throws Exception {

                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(
                            "SELECT count (*) as count FROM " + tableName); //NON-NLS

                    return resultSet.getInt("count");
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Failed to get data for table.", ex); //NON-NLS
                }
                //NON-NLS
                return 0;
            }

            @Override
            protected void done() {
                super.done();
                try {

                    numRows = get();
                    numEntriesField.setText(numRows + " entries");

                    currPage = 1;
                    currPageLabel.setText(Integer.toString(currPage));
                    numPagesLabel.setText(Integer.toString((numRows / ROWS_PER_PAGE) + 1));

                    prevPageButton.setEnabled(false);


                    if (numRows > 0) {
                        nextPageButton.setEnabled(((numRows > ROWS_PER_PAGE)));
                        readTable(tableName, (currPage - 1) * ROWS_PER_PAGE + 1, ROWS_PER_PAGE);
                    } else {
                        nextPageButton.setEnabled(false);
                        selectedTableView.setupTable(Collections.emptyList());
                    }

                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.log(Level.SEVERE, "Unexpected exception while reading table.", ex); //NON-NLS
                }
            }
        };
        worker.execute();
    }

    private void readTable(String tableName, int startRow, int numRowsToRead) {

        if (worker != null && !worker.isDone()) {
            worker.cancel(false);
            worker = null;
        }

        worker = new SwingWorker<ArrayList<Map<String, Object>>, Void>() {
            @Override
            protected ArrayList<Map<String, Object>> doInBackground() throws Exception {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(
                            "SELECT * FROM " + tableName
                            + " LIMIT " + Integer.toString(numRowsToRead)
                            + " OFFSET " + Integer.toString(startRow - 1)
                    ); //NON-NLS

                    return resultSetToArrayList(resultSet);
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Failed to get data for table.", ex); //NON-NLS
                }
                //NON-NLS
                return null;
            }

            @Override
            protected void done() {

                if (isCancelled()) {
                    return;
                }

                super.done();
                try {
                    ArrayList<Map<String, Object>> rows = get();
                    if (Objects.nonNull(rows)) {
                        selectedTableView.setupTable(rows);
                    }else{
                        selectedTableView.setupTable(Collections.emptyList());
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.log(Level.SEVERE, "Unexpected exception while reading table.", ex); //NON-NLS
                }
            }
        };

        worker.execute();
    }

    private ArrayList<Map<String, Object>> resultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ArrayList<Map<String, Object>> arraylist = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                if (rs.getObject(i) == null) {
                    row.put(md.getColumnName(i), "");
                } else {
                    if (md.getColumnTypeName(i).compareToIgnoreCase("blob") == 0) {
                        row.put(md.getColumnName(i), "BLOB Data not shown...");
                    } else {
                        row.put(md.getColumnName(i), rs.getObject(i));
                    }
                }
            }
            arraylist.add(row);
        }

        return arraylist;
    }
}
