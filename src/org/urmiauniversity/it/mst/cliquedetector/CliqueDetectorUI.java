/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.urmiauniversity.it.mst.cliquedetector;

import javax.swing.JPanel;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mir Saman Tajbakhsh
 */
@ServiceProvider(service = StatisticsUI.class)
public class CliqueDetectorUI implements StatisticsUI {

    private CliqueDetectorPanel panel;
    private CliqueDetector myCliqueDetector;

    @Override
    public JPanel getSettingsPanel() {
        panel = new CliqueDetectorPanel();
        return panel;
    }

    @Override
    public void setup(Statistics ststcs) {
        this.myCliqueDetector = (CliqueDetector) ststcs;
        if (panel != null) {
            panel.setK(myCliqueDetector.getK());
        }
    }

    @Override
    public void unsetup() {
        if (panel != null) {
            myCliqueDetector.setK(panel.getK());
        }
        panel = null;
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return CliqueDetector.class;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Clique Detector";
    }

    @Override
    public String getShortDescription() {
        return "A simple clique detector of specific size";
    }

    @Override
    public String getCategory() {
        return CATEGORY_NETWORK_OVERVIEW;
    }

    @Override
    public int getPosition() {
        return 800;
    }

}
