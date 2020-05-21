/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sleuthkit.autopsy.discovery;

import org.sleuthkit.autopsy.centralrepository.datamodel.CentralRepository;

/**
 *
 * @author wschaefer
 */
final class VideoFilterPanel extends AbstractFilterPanel {

    private static final long serialVersionUID = 1L;
    private static final FileSearchData.FileType FILE_TYPE = FileSearchData.FileType.VIDEO;

    /**
     * Creates new form VideoFilterPanel
     */
    VideoFilterPanel() {
        super();
        initComponents();
        SizeFilterPanel sizeFilterPanel = new SizeFilterPanel(FileSearchData.FileType.VIDEO);
        addFilter(sizeFilterPanel, true, null);
        addFilter(new DataSourceFilterPanel(), false, null);
        int[] pastOccurrencesIndices;
        if (!CentralRepository.isEnabled()) {
            pastOccurrencesIndices = new int[]{0};
        } else {
            pastOccurrencesIndices = new int[]{1, 2, 3, 4, 5, 6, 7};
        }
        addFilter(new PastOccurrencesFilterPanel(), true, pastOccurrencesIndices);
        addFilter(new UserCreatedFilterPanel(), false, null);
        addFilter(new HashSetFilterPanel(), false, null);
        addFilter(new InterestingItemsFilterPanel(), false, null);
        addFilter(new ObjectDetectedFilterPanel(), false, null);
        addFilter(new ParentFolderFilterPanel(), false, null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    @Override
    FileSearchData.FileType getFileType() {
        return FILE_TYPE;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
