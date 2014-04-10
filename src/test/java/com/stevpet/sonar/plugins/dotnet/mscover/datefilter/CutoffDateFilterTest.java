package com.stevpet.sonar.plugins.dotnet.mscover.datefilter;

import org.junit.Assert;
import org.junit.Test;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.utils.SonarException;

import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.CutOffDateFilter;
import com.stevpet.sonar.plugins.dotnet.mscover.datefilter.DateFilter;

public class CutoffDateFilterTest {
    @Test
    public void FileHasCoverage_Included() {
        //Arrange
        boolean isIncluded=isFileIncludedForCutOffDate(null);
        //Assert
        Assert.assertTrue(isIncluded);

    }
    
    @Test
    public void DateAtEnd_ShouldBeIncluded() {
        //Arrange
        boolean isIncluded=isFileIncludedForCutOffDate("2014-01-05");
        //Assert
        Assert.assertTrue(isIncluded);

    }
    
    @Test 
    public void DateAfterEnd_ShouldNotBeIncluded() {
        //Arrange
        boolean isIncluded=isFileIncludedForCutOffDate("2014-01-06");
        //Assert
        Assert.assertFalse(isIncluded);
    }
    
    @Test 
    public void InvalidDate_ShouldBeIncluded() {
        //Arrange
        boolean isIncluded=isFileIncludedForCutOffDate("rabarber");
        //Assert
        Assert.assertTrue(isIncluded);
    }
    @Test(expected=SonarException.class)
    public void InvalidCommitLine_ShouldThrowException() {
        DateFilter dateFilter = new CutOffDateFilter();
        Measure lineRevisions = new Measure(CoreMetrics.SCM_LAST_COMMIT_DATETIMES_BY_LINE,"abcde");
        dateFilter.setLineCommitDates(lineRevisions);
    }
    
    @Test
    public void EmptyCommitLine_IsValid() {
        DateFilter dateFilter = new CutOffDateFilter();
        Measure lineRevisions = new Measure(CoreMetrics.SCM_LAST_COMMIT_DATETIMES_BY_LINE);
        dateFilter.setLineCommitDates(lineRevisions);
        dateFilter.setCutOffDate("2014-01-01"); 
        Assert.assertTrue(dateFilter.isIncludedInResults());
    }
    
    @Test
    public void NoMeasure_IsValid() {
        DateFilter dateFilter = new CutOffDateFilter();
        dateFilter.setCutOffDate("2014-01-01"); 
        Assert.assertTrue(dateFilter.isIncludedInResults());
    }
    @Test
    public void EmptyMeasuresValidDate_ShouldBeIncluded() {
        PropertiesBuilder<Integer, String> dates = new PropertiesBuilder<Integer,String>(CoreMetrics.SCM_LAST_COMMIT_DATETIMES_BY_LINE);
        
        PropertiesBuilder<String,Integer> lines = new PropertiesBuilder<String,Integer>(CoreMetrics.IT_COVERAGE_LINE_HITS_DATA);
        
        Measure lineCoverage =lines.build();
        Measure lineRevisions=dates.build();
        
        DateFilter dateFilter = new CutOffDateFilter();
        dateFilter.setLineCoverage(lineCoverage);
        dateFilter.setLineCommitDates(lineRevisions);
        dateFilter.setCutOffDate("2014-01-01"); 
        Assert.assertTrue(dateFilter.isIncludedInResults());
    }
    
    @Test
    public void NoMeasuresValidDate_ShouldBeIncluded() {
        DateFilter dateFilter = new CutOffDateFilter();
        dateFilter.setLineCoverage(null);
        dateFilter.setLineCommitDates(null);
        dateFilter.setCutOffDate("2014-01-01");        
        Assert.assertTrue(dateFilter.isIncludedInResults());
    }
    
    @Test(expected=NumberFormatException.class)
    public void InvalidData_ShouldSurvive() {
        PropertiesBuilder<String, String> dates = new PropertiesBuilder<String,String>(CoreMetrics.SCM_LAST_COMMIT_DATETIMES_BY_LINE);
        dates.add("j","burb");

        
        PropertiesBuilder<String,String> lines = new PropertiesBuilder<String,String>(CoreMetrics.IT_COVERAGE_LINE_HITS_DATA);
        lines.add("b","a");

        Measure lineCoverage =lines.build();
        Measure lineRevisions=dates.build();
        
        DateFilter dateFilter = new CutOffDateFilter();
        dateFilter.setLineCoverage(lineCoverage);
        dateFilter.setLineCommitDates(lineRevisions);
        dateFilter.setCutOffDate("2014-01-01");        
        Assert.assertTrue(dateFilter.isIncludedInResults());
    }
    
    @Test(expected=SonarException.class)
    public void InvalidDate_ShouldSurvive() {
        //Arrange
        PropertiesBuilder<String, String> dates = new PropertiesBuilder<String,String>(CoreMetrics.SCM_LAST_COMMIT_DATETIMES_BY_LINE);
        dates.add("1","burb");

        
        PropertiesBuilder<String,String> lines = new PropertiesBuilder<String,String>(CoreMetrics.IT_COVERAGE_LINE_HITS_DATA);
        lines.add("1","2");

        Measure lineCoverage =lines.build();
        Measure lineRevisions=dates.build();
        //Act
        DateFilter dateFilter = new CutOffDateFilter();
        dateFilter.setLineCoverage(lineCoverage);
        dateFilter.setLineCommitDates(lineRevisions);
        dateFilter.setCutOffDate("2014-01-01");  
        //Assert
        Assert.assertTrue(dateFilter.isIncludedInResults());
    }
    private boolean isFileIncludedForCutOffDate(String date) {
        PropertiesBuilder<Integer, String> dates = new PropertiesBuilder<Integer,String>(CoreMetrics.SCM_LAST_COMMIT_DATETIMES_BY_LINE);
        dates.add(1,"2014-01-01T00:20:12+0100");
        dates.add(2,"2014-01-02T00:20:12+0100");
        dates.add(3,"2014-01-05T00:20:12+0100");
        
        PropertiesBuilder<String,Integer> lines = new PropertiesBuilder<String,Integer>(CoreMetrics.IT_COVERAGE_LINE_HITS_DATA);
        lines.add("1",0);
        lines.add("2",1);
        lines.add("3",1);
        
        Measure lineCoverage =lines.build();
        Measure lineRevisions=dates.build();
        
        DateFilter dateFilter = new CutOffDateFilter();
        dateFilter.setLineCoverage(lineCoverage);
        dateFilter.setLineCommitDates(lineRevisions);
        dateFilter.setCutOffDate(date);
        return dateFilter.isIncludedInResults();
    }
    
 
}
