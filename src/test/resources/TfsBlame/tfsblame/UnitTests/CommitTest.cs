using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace BHI.JewelSuite.Tools.TfsBlame.unittests
{
    [TestClass]
    public class CommitTest
    {
        private DateTime timeStamp = new DateTime(2014, 11, 1, 18, 12, 09);
        private string TIMESTAMP = "2014/11/01T18:12:09";
        [TestMethod]
        public void TestMethod1()
        {
            var commit = new Commit(12345,"peter",timeStamp);
            var result = commit.ToString();
            Assert.AreEqual("12345 peter " + TIMESTAMP,result);
        }

        [TestMethod]
        public void TestMethod2()
        {
            var commit = new Commit(12345, "peter stevens", timeStamp);
            var result = commit.ToString();
            Assert.AreEqual("12345 peter_stevens "+ TIMESTAMP, result);
        }

        [TestMethod]
        public void TestMethod3()
        {
            var commit = new Commit(12345, "henny hendrika maria stevens", timeStamp);
            var result = commit.ToString();
            Assert.AreEqual("12345 henny_hendrika_maria_stevens " + TIMESTAMP, result);
        }
    }
}
