using System;
using System.Collections.Specialized;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using BHI.JewelSuite.Tools.TfsBlame;
namespace BHI.JewelSuite.tools.tfsblame.unittests
{
    [TestClass]
    public class ServerUriFinderTest
    {

        [TestMethod]
        public void FindUriWithEnvironmentSetting_ShouldUseIt()
        {
            const string root = @"C:\users\stevpet\Documents\dev";
            const string serverUrl="https://bhiabztfs01:8080";
            const string sonarScmUrl = "scm:tfs:" + serverUrl;
            IServerUriFinder serverUriFinder = new ServerUriFinder();

            var actual = serverUriFinder.FindUri(root + @"\Jewel.Release.Oahu.Ux\BasicControls");
            Assert.AreEqual(serverUrl, actual.OriginalString);
        }

        [TestMethod]
        [ExpectedException(typeof(TfsBlameException))]
        public void FindUriWithoutEnvironmentSetting_ShouldThrowException()
        {
            const string root = @"C:\users\stevpet\Documents\dev";


            ServerUriFinder serverUriFinder = new ServerUriFinder();

            var actual = serverUriFinder.FindUri(root + @"\Jewel.Release.Oahu.Ux\BasicControls");
            Assert.Fail("should not get here");
        }

        [TestMethod]
        [ExpectedException(typeof(TfsBlameException))]
        public void FindUriWitInvalidEnvironmentSetting_ShouldThrowException()
        {
            const string root = @"C:\users\stevpet\Documents\dev";
            IServerUriFinder serverUriFinder = new ServerUriFinder();

            var actual = serverUriFinder.FindUri(root + @"\Jewel.Release.Oahu.Ux\BasicControls");
            Assert.Fail("should not get here");
        }

    }
}
