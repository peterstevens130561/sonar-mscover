// =============================================================================
// =
// = FILE: Commits.cs
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
using System.Collections.Generic;
using Microsoft.TeamFoundation.Client;
using Microsoft.TeamFoundation.VersionControl.Client;

namespace BHI.JewelSuite.Tools.TfsBlame
{
    public class Commits : ICommits
    {

        private TfsTeamProjectCollection m_tfsTeamProjectCollection;
        private VersionControlServer m_versionControlServer;

        public Uri ServerUri { get; set; }
        private IDictionary<int, Commit> m_commits = new Dictionary<int, Commit>();

        public Commits()
        {
            
        }

        /// <summary>
        /// for testing purposed
        /// </summary>
        /// <param name="mVersionControlServer"></param>
        public Commits(VersionControlServer mVersionControlServer)
        {
            m_versionControlServer = mVersionControlServer;
        }

        /// <summary>
        /// get the commit info of the provided changeset
        /// </summary>
        /// <param name="changesetId"></param>
        /// <returns>changesetId, commiter, commit date nicely formatted</returns>
        public virtual String  GetFormattedCommit(int changesetId)
        {
            if (!m_commits.ContainsKey(changesetId))
            {
                var changeset = m_versionControlServer.GetChangeset(changesetId);
                m_commits.Add(changesetId, new Commit(changeset));
            }
            return m_commits[changesetId].ToString();
        }


        /// <summary>
        /// Connect to the workspace of the current directory
        /// </summary>
        public virtual void Connect(String url)
        {
            ServerUri= new Uri(url);
            m_tfsTeamProjectCollection = new TfsTeamProjectCollection(ServerUri);
            m_versionControlServer = m_tfsTeamProjectCollection.GetService<VersionControlServer>();
        }

        protected virtual Uri GetServerUri()
        {
            return new ServerUriFinder().FindUri(Environment.CurrentDirectory);
        }
    }

}
