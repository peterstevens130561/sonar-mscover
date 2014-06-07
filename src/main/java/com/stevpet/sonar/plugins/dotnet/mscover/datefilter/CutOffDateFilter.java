package com.stevpet.sonar.plugins.dotnet.mscover.datefilter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.TimeMachine;
import org.sonar.api.batch.TimeMachineQuery;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Resource;
import org.sonar.api.utils.SonarException;

import com.google.common.collect.ImmutableList;



public class CutOffDateFilter implements DateFilter {

    private static final Logger LOG = LoggerFactory
            .getLogger(CutOffDateFilter.class);
    private Date cutoffDate ;
    private Map<Integer,Integer> lineCoverageData = new HashMap<Integer,Integer>();
    private Map<Integer,Date> lineCommitsData; 
    
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
    
    private TimeMachine timeMachine;
    
    public void setTimeMachine(TimeMachine timeMachine) {
        this.timeMachine = timeMachine;
    }
    public void setCutOffDate(String date) {
        cutoffDate = getGenesis();
        if(StringUtils.isEmpty(date)) {
            return;
        }
        try {
            cutoffDate=dateFormat.parse(date);
        } catch (ParseException e) {
            LOG.error("Invalid cutoffDate (" + date + " ) ", e);
        }
    }

    public void setLineCoverage(Measure lineCoverage) {
        String[] lines = getData(lineCoverage);
        for(String line:lines) {
            parseCoverageLine(line);
        }
        
    }
    private void parseCoverageLine(String line) {
        String[] lineParts = line.split("=");
        if(lineParts.length ==1) {
            LOG.debug("Unparseable coverage line " + line);
            return;
        }
        int lineNr=Integer.parseInt(lineParts[0]);
        int visits=Integer.parseInt(lineParts[1]);
        lineCoverageData.put(lineNr,visits);
    }

    
    public void setLineCommitDates(Measure lineRevisions) {
        lineCommitsData = new HashMap<Integer,Date>();
        String[] lines = getData(lineRevisions);
        for(String line:lines) {
            parseCommitLine(line);
        }
    }
    private void parseCommitLine(String line) {
        if(StringUtils.isEmpty(line)) {
            return;
        }
        String[] lineParts = line.split("=");
        if(lineParts.length ==1) {
            String msg="Unparseable commitline " + line;
            throw new SonarException(msg);
        }
        int lineNr=Integer.parseInt(lineParts[0]);
        Date commitDate = null;
        try {
            commitDate = dateFormat.parse(lineParts[1]);
        } catch (ParseException e) {
            String msg="Invalid commitdate on line " + lineNr + "=" + lineParts[1] + " ";
            LOG.error(msg,e);
            throw new SonarException(e);
        }
        lineCommitsData.put(lineNr,commitDate);
    }

    String[] getData(Measure measure)  {
        if(measure == null) {
            return new String[0];
        }
        String data = measure.getData();
        if(data == null) {
            return new String[0];
        }
        return data.split(";");
        
    }
    
    public boolean isResourceIncludedInResults(Resource<?> resource) {
        LOG.debug("isResourceIncludedInResults" + resource.getLongName());
        Measure commitDates = getCommitDateTimesByLine(resource);
        setLineCommitDates(commitDates);
        return isIncludedInResults();
    }
    

    public boolean isIncludedInResults() {
        Date latestCommitDate=getLatestCommitDate();
        if(latestCommitDate==null ) {
            LOG.info("No commitdate found, passing through");
            return true;
        }
        LOG.info("Commitdate = " + latestCommitDate);
        return cutoffDate.before(latestCommitDate) || cutoffDate.equals(latestCommitDate);
   
    }
    private Date getLatestCommitDate() {
        if(lineCommitsData == null || lineCommitsData.size()==0) {
            LOG.debug("No history found for file");
            return null;
        }

        Date latestCommitDate = getGenesis();
        for(Date commitDate : lineCommitsData.values()) {
            if(commitDate.after(latestCommitDate)) {
                latestCommitDate=commitDate;
            }
        }
        return latestCommitDate;
    }
    
    public Measure getCommitDateTimesByLine(Resource<?> resource) {
        List<Metric> lastCommitDataTimesByLineMetrics = ImmutableList.of(
                CoreMetrics.SCM_LAST_COMMIT_DATETIMES_BY_LINE
        );
        TimeMachineQuery query = new TimeMachineQuery(resource).setOnlyLastAnalysis(true).setMetrics(lastCommitDataTimesByLineMetrics);
        if(timeMachine ==null) {
            LOG.error("TimeMachine not set");
            return null;
        }
        List<Measure> commitDateTimesByLine= timeMachine.getMeasures(query); 
        if(commitDateTimesByLine == null|| commitDateTimesByLine.size()==0) {
            return null;
        }
        return commitDateTimesByLine.get(0);

        }
    
    private Date getGenesis() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1970, 1, 1);
        return calendar.getTime();
    }  
}
