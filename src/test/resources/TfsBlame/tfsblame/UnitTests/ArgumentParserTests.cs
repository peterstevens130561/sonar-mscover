// =============================================================================
// =
// = FILE: ArgumentParserTests.cs
// =
// =============================================================================
// =                                                                        
// = COPYRIGHT: Title to, ownership of and copyright of this software is
// = vested in Copyright 2003-2013  Baker Hughes Reservoir Software BV.
// = is a wholly-owned subsidiary of Baker Hughes Incorporated.
// = All rights reserved.
// =
// = Neither the whole nor any part of this software may be
// = reproduced, copied, stored in any retrieval system or
// = transmitted in any form or by any means without prior
// = written consent of the copyright owner.
// =
// = This software and the information and data it contains is
// = confidential. Neither the whole nor any part of the
// = software and the data and information it contains may be
// = disclosed to any third party without the prior written
// = consent of Copyright 2003-2013 Baker Hughes Reservoir Software BV, a
// = wholly-owned subsidiary of Baker Hughes Incorporated 
// =                                                                          
// =============================================================================

using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace BHI.JewelSuite.Tools.TfsBlame.unittests
{
    [TestClass]
    public class ArgumentParserTests
    {
        [TestMethod]
        public void InvocationAsExpectedShouldGetFile()
        {
            ArgumentParser parser = new ArgumentParser();
            var expected = "john.cs";
            string[] arguments = {"/noprompt", expected};
            parser.Parse(arguments);
            var actual = parser.File;
            Assert.AreEqual(expected,actual);
        }

        [TestMethod]
        public void InvocationWithJustFilenameShouldGetFile()
        {
            ArgumentParser parser = new ArgumentParser();
            var expected = "john.cs";
            string[] arguments = { expected };
            parser.Parse(arguments);
            var actual = parser.File;
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void UrlDefined_ShouldGetServerUrl()
        {
            ArgumentParser parser = new ArgumentParser();
            var expected = "http://bhiabztfs01:8080";
            string[] arguments = { "/url:" + expected };
            parser.Parse(arguments);
            var actual = parser.Url;
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void UrlIncomplete_ShouldGetEmptyUrl()
        {
            ArgumentParser parser = new ArgumentParser();
            String expected = null;
            string[] arguments = { "/url:" + expected };
            parser.Parse(arguments);
            var actual = parser.Url;
            Assert.AreEqual(null, actual);
        }
    }
}
