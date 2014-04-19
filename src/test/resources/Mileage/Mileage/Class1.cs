

using System;

namespace com.bhi.rds.tools.sonarqube.test
 
{
    public class Mileage
    {
        public double Calculate(String distance, String usage)
        {
            double distanceNum = Double.Parse(distance);
            double usageNum = Double.Parse(usage);
            double result= distanceNum/usageNum;
            return result;
        }
    }
}
