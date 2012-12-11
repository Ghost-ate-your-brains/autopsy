/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011 Basis Technology Corp.
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
package org.sleuthkit.autopsy.corecomponents;

import java.awt.Component;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import org.sleuthkit.autopsy.coreutils.Logger;
import javax.swing.BoxLayout;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.gstreamer.*;
import org.gstreamer.elements.PlayBin2;
import org.gstreamer.swing.VideoComponent;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Node;
import org.openide.util.Cancellable;
import org.openide.util.lookup.ServiceProvider;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.corecomponentinterfaces.DataContentViewer;
import org.sleuthkit.autopsy.datamodel.ContentUtils;
import org.sleuthkit.datamodel.File;
import org.sleuthkit.datamodel.TskData.TSK_FS_NAME_FLAG_ENUM;

/**
 *
 * @author dfickling
 */
@ServiceProvider(service = DataContentViewer.class, position = 5)
public class DataContentViewerMedia extends javax.swing.JPanel implements DataContentViewer {

    private static final String[] IMAGES = new String[]{".jpg", ".jpeg", ".png", ".gif", ".jpe", ".bmp"};
    private static final String[] VIDEOS = new String[]{".mov", ".m4v", ".flv", ".mp4", ".3gp", ".avi", ".mpg", ".mpeg"};
    private static final String[] AUDIOS = new String[]{".mp3", ".wav", ".wma"};
    private static final Logger logger = Logger.getLogger(DataContentViewerMedia.class.getName());
    private VideoComponent videoComponent;
    private PlayBin2 playbin2;
    private File currentFile;
    private long durationMillis = 0;
    private boolean autoTracking = false; // true if the slider is moving automatically
    private final Object playbinLock = new Object(); // lock for synchronization of playbin2 player

    /**
     * Creates new form DataContentViewerVideo
     */
    public DataContentViewerMedia() {
        initComponents();
        customizeComponents();
    }

    private void customizeComponents() {
        Gst.init();
        progressSlider.addChangeListener(new ChangeListener() {
            /**
             * Should always try to synchronize any call to
             * progressSlider.setValue() to avoid a different thread changing
             * playbin while stateChanged() is processing
             */
            @Override
            public void stateChanged(ChangeEvent e) {
                int time = progressSlider.getValue();
                synchronized (playbinLock) {
                    if (playbin2 != null && !autoTracking) {
                        State orig = playbin2.getState();
                        playbin2.pause();
                        playbin2.seek(ClockTime.fromMillis(time));
                        playbin2.setState(orig);
                    }
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pauseButton = new javax.swing.JButton();
        videoPanel = new javax.swing.JPanel();
        progressSlider = new javax.swing.JSlider();
        progressLabel = new javax.swing.JLabel();

        pauseButton.setText(org.openide.util.NbBundle.getMessage(DataContentViewerMedia.class, "DataContentViewerMedia.pauseButton.text")); // NOI18N
        pauseButton.setMaximumSize(new java.awt.Dimension(45, 23));
        pauseButton.setMinimumSize(new java.awt.Dimension(45, 23));
        pauseButton.setPreferredSize(new java.awt.Dimension(45, 23));
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout videoPanelLayout = new javax.swing.GroupLayout(videoPanel);
        videoPanel.setLayout(videoPanelLayout);
        videoPanelLayout.setHorizontalGroup(
            videoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 283, Short.MAX_VALUE)
        );
        videoPanelLayout.setVerticalGroup(
            videoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 242, Short.MAX_VALUE)
        );

        progressSlider.setValue(0);

        progressLabel.setText(org.openide.util.NbBundle.getMessage(DataContentViewerMedia.class, "DataContentViewerMedia.progressLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(videoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressLabel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(videoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pauseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        synchronized (playbinLock) {
            State state = playbin2.getState();
            if (state.equals(State.PLAYING)) {
                playbin2.pause();
                pauseButton.setText("►");
                playbin2.setState(State.PAUSED);
            } else if (state.equals(State.PAUSED)) {
                playbin2.play();
                pauseButton.setText("||");
                playbin2.setState(State.PLAYING);
            } else if (state.equals(State.READY)) {
                ExtractMedia em = new ExtractMedia(currentFile, getJFile(currentFile));
                em.execute();
            }
        }
    }//GEN-LAST:event_pauseButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton pauseButton;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JSlider progressSlider;
    private javax.swing.JPanel videoPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setNode(Node selectedNode) {
        reset();
        setComponentsVisibility(false);
        if (selectedNode == null) {
            return;
        }

        File file = selectedNode.getLookup().lookup(File.class);
        if (file == null) {
            return;
        }

        currentFile = file;
        if (containsExt(file.getName(), IMAGES)) {
            showImage(file);
        } else if (containsExt(file.getName(), VIDEOS) || containsExt(file.getName(), AUDIOS)) {
            setupVideo(file);
        }
    }

    /**
     * Initialize vars and display the image on the panel.
     *
     * @param file
     */
    private void showImage(File file) {
        java.io.File ioFile = getJFile(file);
        if (!ioFile.exists()) {
            try {
                ContentUtils.writeToFile(file, ioFile);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Error buffering file", ex);
            }
        }

        videoComponent = new VideoComponent();
        synchronized (playbinLock) {
            playbin2 = new PlayBin2("ImageViewer");
            playbin2.setVideoSink(videoComponent.getElement());
        }

        videoPanel.removeAll();
        videoPanel.setLayout(new BoxLayout(videoPanel, BoxLayout.Y_AXIS));
        videoPanel.add(videoComponent);
        videoPanel.revalidate();
        videoPanel.repaint();

        synchronized (playbinLock) {
            playbin2.setInputFile(ioFile);
            playbin2.play();
        }
        videoPanel.setVisible(true);
    }

    /**
     * Initialize all the necessary vars to play a video/audio file.
     *
     * @param file the File to play
     */
    private void setupVideo(File file) {
        java.io.File ioFile = getJFile(file);

        pauseButton.setText("►");
        progressSlider.setValue(0);

        videoComponent = new VideoComponent();
        synchronized (playbinLock) {
            playbin2 = new PlayBin2("VideoPlayer");
            playbin2.setVideoSink(videoComponent.getElement());
        }

        videoPanel.removeAll();
        videoPanel.setLayout(new BoxLayout(videoPanel, BoxLayout.Y_AXIS));
        videoPanel.add(videoComponent);
        videoPanel.revalidate();
        videoPanel.repaint();

        synchronized (playbinLock) {
            playbin2.setInputFile(ioFile);
            playbin2.setState(State.READY);
        }
        setComponentsVisibility(true);
    }

    /**
     * To set the visibility of specific components in this class.
     *
     * @param isVisible whether to show or hide the specific components
     */
    private void setComponentsVisibility(boolean isVisible) {
        pauseButton.setVisible(isVisible);
        progressLabel.setVisible(isVisible);
        progressSlider.setVisible(isVisible);
        videoPanel.setVisible(isVisible);
    }

    @Override
    public String getTitle() {
        return "Media View";
    }

    @Override
    public String getToolTip() {
        return "Displays supported multimedia files";
    }

    @Override
    public DataContentViewer getInstance() {
        return new DataContentViewerMedia();
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public void resetComponent() {
        // we don't want this to do anything
        // because we already reset on each selected node
    }

    private void reset() {
        synchronized (playbinLock) {
            if (playbin2 != null) {
                if (playbin2.isPlaying()) {
                    playbin2.stop();
                }
                playbin2.setState(State.NULL);
//                try {
//                    Thread.sleep(20); // gstreamer needs to catch up
//                } catch (InterruptedException ex) { }
                if (playbin2.getState().equals(State.NULL)) {
                    playbin2.dispose();
                }
                playbin2 = null;
            }
            videoComponent = null;
        }
    }

    @Override
    public boolean isSupported(Node node) {
        if (node == null) {
            return false;
        }

        File file = node.getLookup().lookup(File.class);
        if (file == null) {
            return false;
        }

        if (file.getDirFlag() == TSK_FS_NAME_FLAG_ENUM.UNALLOC) {
            return false;
        }

        if (file.getSize() == 0) {
            return false;
        }

        String name = file.getName().toLowerCase();

        if (containsExt(name, IMAGES) || containsExt(name, AUDIOS) || containsExt(name, VIDEOS)) {
            return true;
        }

        return false;
    }

    @Override
    public int isPreferred(Node node, boolean isSupported) {
        if (isSupported) {
            return 7;
        } else {
            return 0;
        }
    }

    private static boolean containsExt(String name, String[] exts) {
        int extStart = name.lastIndexOf(".");
        String ext = "";
        if (extStart != -1) {
            ext = name.substring(extStart, name.length()).toLowerCase();
        }
        return Arrays.asList(exts).contains(ext);
    }

    private java.io.File getJFile(File file) {
        // Get the temp folder path of the case
        String tempPath = Case.getCurrentCase().getTempDirectory();
        String name = file.getName();
        int extStart = name.lastIndexOf(".");
        String ext = "";
        if (extStart != -1) {
            ext = name.substring(extStart, name.length()).toLowerCase();
        }
        tempPath = tempPath + java.io.File.separator + file.getId() + ext;

        java.io.File tempFile = new java.io.File(tempPath);
        return tempFile;
    }

    /* Thread that extracts and plays a file */
    private class ExtractMedia extends SwingWorker<Object, Void> {

        private ProgressHandle progress;
        boolean success = false;
        private File sFile;
        private java.io.File jFile;
        String duration;
        String position;

        ExtractMedia(org.sleuthkit.datamodel.File sFile, java.io.File jFile) {
            this.sFile = sFile;
            this.jFile = jFile;
        }

        ;

        @Override
        protected Object doInBackground() throws Exception {
            success = false;
            progress = ProgressHandleFactory.createHandle("Buffering " + sFile.getName(), new Cancellable() {
                @Override
                public boolean cancel() {
                    return ExtractMedia.this.cancel(true);
                }
            });
            progressLabel.setText("Buffering...");
            progress.start();
            progress.switchToDeterminate(100);
            try {
                ContentUtils.writeToFile(sFile, jFile, progress, this, true);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Error buffering file", ex);
            }
            success = true;
            return null;
        }

        /* clean up or start the worker threads */
        @Override
        protected void done() {
            try {
                super.get(); //block and get all exceptions thrown while doInBackground()
            } catch (CancellationException ex) {
                logger.log(Level.INFO, "Media buffering was canceled.");
            } catch (InterruptedException ex) {
                logger.log(Level.INFO, "Media buffering was interrupted.");
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Fatal error during media buffering.", ex);
            } finally {
                progress.finish();
                if (!this.isCancelled()) {
                    play();
                }
            }
        }

        private void play() {
            if (jFile == null || !jFile.exists()) {
                progressLabel.setText("Error buffering file");
                return;
            }
                                ClockTime dur = null;
            synchronized (playbinLock) {
                playbin2.play(); // must play, then pause and get state to get duration.
                playbin2.pause();
                playbin2.getState();
                dur = playbin2.queryDuration();
            }
            duration = dur.toString();
            durationMillis = dur.toMillis();
            
            progressSlider.setMaximum((int) durationMillis);
            progressSlider.setMinimum(0);
            final String finalDuration;
            if (duration.length() == 8 && duration.substring(0, 3).equals("00:")) {
                finalDuration = duration.substring(3);
                progressLabel.setText("00:00/" + duration);
            } else {
                finalDuration = duration;
                progressLabel.setText("00:00:00/" + duration);
            }
            synchronized (playbinLock) {
                playbin2.play();
            }
            pauseButton.setText("||");
            new Thread(new Runnable() {
                private boolean isPlayBinReady() {
                    synchronized (playbinLock) {
                        return playbin2 != null && !playbin2.getState().equals(State.NULL);
                    }
                }

                @Override
                public void run() {
                    long positionMillis = 0;
                    while (positionMillis < durationMillis
                            && isPlayBinReady() ) {
                        ClockTime pos = null;
                        synchronized (playbinLock) {
                            pos = playbin2.queryPosition();
                        } 
                        position = pos.toString();
                        positionMillis = pos.toMillis();
                        
                        if (position.length() == 8) {
                            position = position.substring(3);
                        }
                        progressLabel.setText(position + "/" + finalDuration);
                        autoTracking = true;
                        progressSlider.setValue((int) positionMillis);
                        autoTracking = false;
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException ex) {
                        }
                    }
                    if (finalDuration.length() == 5) {
                        progressLabel.setText("00:00/" + finalDuration);
                    } else {
                        progressLabel.setText("00:00:00/" + finalDuration);
                    }
                    // If it reached the end
                    if (progressSlider.getValue() == progressSlider.getMaximum()) {
                        restartVideo();
                    }
                }

                public void restartVideo() {
                    synchronized (playbinLock) {
                        if (playbin2 != null) {
                            playbin2.stop();
                            playbin2.setState(State.READY); // ready to be played again
                        }
                    }
                    pauseButton.setText("►");
                    progressSlider.setValue(0);
                }
            }).start();
        }
    }
}
