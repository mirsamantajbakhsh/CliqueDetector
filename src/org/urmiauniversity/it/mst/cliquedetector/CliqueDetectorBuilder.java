/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.urmiauniversity.it.mst.cliquedetector;

import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mir Saman Tajbakhsh
 */
@ServiceProvider(service = StatisticsBuilder.class)
public class CliqueDetectorBuilder implements org.gephi.statistics.spi.StatisticsBuilder {

    @Override
    public String getName() {
        return "Clique Detector";
    }

    @Override
    public Statistics getStatistics() {
        return new CliqueDetector();
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return CliqueDetector.class;
    }
    
}
