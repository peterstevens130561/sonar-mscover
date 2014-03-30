using System;

using Microsoft.TeamFoundation.VersionControl.Client;
// =============================================================================
// =
// =   FILE:		Commit.cs
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
namespace BHI.JewelSuite.Tools.TfsBlame
{
    internal class Commit
    {
        private DateTime m_timeStamp ;
        private int m_key;
        private String m_author { get; set; }

        public Commit(int key, String author, DateTime timeStamp)
        {
            m_key = key;
            m_author = author;
            m_timeStamp = timeStamp;
        }

        public Commit(Changeset changeset)
        {
            m_timeStamp= changeset.CreationDate;
            m_key = changeset.ChangesetId;
            m_author = changeset.CommitterDisplayName;
        }

        public override string ToString()
        {
            String timeStamp = formatAsISO8601WithTime(m_timeStamp);
            return m_key + " " + m_author.Replace(" ", "_") +" " + timeStamp;
        }

        private string formatAsISO8601WithTime(DateTime timeStamp)
        {
            return timeStamp.ToString("yyyy/MM/ddTHH:mm:ss");
        }
    }
}


