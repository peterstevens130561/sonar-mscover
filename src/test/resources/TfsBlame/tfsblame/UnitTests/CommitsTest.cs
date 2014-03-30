using System;
using BHI.JewelSuite.Tools.TfsBlame;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace BHI.JewelSuite.tools.tfsblame.unittests
{
    [TestClass]
    public class CommitsTest
    {


        public void TestConnectFromTfsWorkspace()
        {
            Environment.CurrentDirectory = @"C:\development\Jewel.Release.Oahu";
            String url = "http://bhiabztfs01:8080";
            var commits = new Commits();
            commits.Connect(url);
            Assert.IsTrue(true);
        }
        private class CommitsNonTfsWorkspace : Commits
        {
            protected override Uri GetServerUri()
            {
                return null;
            }
        }
    }
}
