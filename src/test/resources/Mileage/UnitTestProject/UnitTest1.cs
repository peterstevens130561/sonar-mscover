using System;
using com.bhi.rds.tools.sonarqube.test;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace UnitTestProject
{
    [TestClass]
    public class UnitTest1
    {
        [TestMethod]
        public void Mileage_SunnyDay_ShouldMatch()
        {
            Mileage mileage = new Mileage();
            double actual = mileage.Calculate("10", "5");
            Assert.AreEqual(2, actual, "0.000001");
        }

        [TestMethod]
        public void Mileage_SunnyDay_ShouldFail()
        {
            Mileage mileage = new Mileage();
            double actual = mileage.Calculate("10", "3");
            Assert.AreEqual(2, actual, "0.000001");
        }

        [TestMethod]
        public void Mileage_SunnyDay_ShouldInconclusive()
        {
            Mileage mileage = new Mileage();
            double actual = mileage.Calculate("10", "3");

        }

        [TestMethod]
        public void Mileage_DivideZero_UncaughtException()
        {
            Mileage mileage = new Mileage();
            double actual = mileage.Calculate("10", "0");

        }
    }
}
