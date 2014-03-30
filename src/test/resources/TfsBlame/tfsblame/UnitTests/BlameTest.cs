// =============================================================================
// =
// = FILE: BlameTest.cs
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
using Moq;

namespace BHI.JewelSuite.Tools.TfsBlame.unittests
{
    /// <summary>
    /// Summary description for BlameTest
    /// </summary>
    [TestClass]
    public class BlameTest
    {


        [TestMethod]
        public void DecorateLineTestShouldHaveFormatAsRequiredByScmActivity()
        {
            var commitsMock = new Mock<ICommits>();
            ICommits commits = commitsMock.Object;
            // scm activity expects the sequence to be as shown below
            String expectedCommit="12345 PeterStevens 2014-01-01";
            commitsMock.Setup(instance => instance.GetFormattedCommit(12345)).Returns(expectedCommit);
            Blame blame = new Blame(commits);
            String expectedLine = expectedCommit + " MyOriginalTest";
            String decoratedLine=blame.DecorateLine("12345 MyOriginalTest");
            Assert.AreEqual(expectedLine,decoratedLine);
        }

        public void TestBlame()
        {
            Blame blame = new Blame();
            String result = blame.RunAnnotate("joker");
            Assert.IsNotNull(result);
        }
    }
}
